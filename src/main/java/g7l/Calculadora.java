package g7l;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/* Este programa requiere tres argumentos: Los puntajes asociados a partido acertado, a ronda completa acertada
 y a fase completa acertada, en ese orden */

public class Calculadora {
    public static void main(String[] args) throws SQLException, URISyntaxException, IOException {
        int[] puntos = new int[]{0, 0, 0};

        // Procesamos los argumentos
        try {
            puntos[0] = Integer.parseInt(args[0]);
            puntos[1] = Integer.parseInt(args[1]);
            puntos[2] = Integer.parseInt(args[2]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ERROR! Argumentos incorrectos.");
            System.exit(1);
        }

        // Creamos base de datos (si no existe)
        DatabaseManager.createDatabase("init.sql");

        // Mensaje de bienvenida
        System.out.printf("""
                Calculadora de Puntajes © 2023 por Grupo-7L y está bajo licencia CC BY-NC-ND 4.0.
                Para ver una copia de esta licencia, visita http://creativecommons.org/licenses/by-nc-nd/4.0/%n%n""");

        // Generamos un HashMap con los resultados
        // La primera clave es el número de la fase,
        // la segunda es el número de la ronda, la tercera es el hashcode de partido
        HashMap<Integer, HashMap<Integer, HashMap<Integer, Partido>>> resultados = getResultados();

        // Revisamos que los resultados están correctos
        /*
        for (Integer faseID : resultados.keySet()) {
            System.out.printf("Fase %d:\n", faseID);
            HashMap<Integer, HashMap<Integer, Partido>> middleMap = resultados.get(faseID);
            for (Integer rondaID : middleMap.keySet()) {
                System.out.printf("\tRonda %d:\n", rondaID);
                HashMap<Integer, Partido> innerMap = middleMap.get(rondaID);
                for (Integer p : innerMap.keySet()) {
                    System.out.println("\t\t" + innerMap.get(p).toString());
                }
            }
        } */

// ---------------------------------------------------------------------------------------------------------------------

        // Generamos un HashMap con los pronósticos
        // La primera clave es el nombre del participante,
        // la segunda es el hashcode del pronóstico
        HashMap<String, HashMap<Integer, Pronostico>> pronosticos = getPronosticos(resultados);

        // Revisamos que los pronósticos están correctos
        /*
        for (String participante : pronosticos.keySet()) {
            System.out.printf("%s:\n", participante);
            HashMap<Integer, Pronostico> innerMap = pronosticos.get(participante);
            for (Integer p : innerMap.keySet()) {
                System.out.println(innerMap.get(p).toString());
            }
        }
         */

        // Generamos un HashMap con los puntajes aplicando el método getPuntaje()
        // La clave es el nombre del participante
        HashMap<String, Integer> puntajes = getPuntaje(resultados, pronosticos, puntos);
        printPuntos(puntos);
        for (String participante : puntajes.keySet()) {
            System.out.printf("%s: %d%n", participante, puntajes.get(participante));
        }
    }

    // Método que devuelve un mapa de los resultados
    private static HashMap<Integer, HashMap<Integer, HashMap<Integer, Partido>>> getResultados() {
        HashMap<Integer, HashMap<Integer, HashMap<Integer, Partido>>> result = new HashMap<>();

        // En el try con recursos abrimos la conexión (no hace falta cerrar) y ejecutamos la solicitud sql
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM resultados")) {

            // Iteramos por los resultados
            while (rs.next()) {

                // Obtenemos los valores de una fila
                int faseID = rs.getInt("id_fase");
                int rondaID = rs.getInt("id_ronda");
                String eq1 = rs.getString("equipo_1");
                int goles1 = rs.getInt("goles_equipo_1");
                String eq2 = rs.getString("equipo_2");
                int goles2 = rs.getInt("goles_equipo_2");

                // Creamos el partido
                Equipo equipo1 = new Equipo(eq1);
                Equipo equipo2 = new Equipo(eq2);
                Partido partido = new Partido(equipo1, equipo2, goles1, goles2);

                // Creamos fase, ronda y partido (si no existen)
                if (!result.containsKey(faseID)) {
                    result.put(faseID, new HashMap<>());
                }
                if (!result.get(faseID).containsKey(rondaID)) {
                    result.get(faseID).put(rondaID, new HashMap<>());
                }
                if (!result.get(faseID).get(rondaID).containsKey(partido.hashCode())) {
                    result.get(faseID).get(rondaID).put(partido.hashCode(), partido);
                }
            }
        }
        catch (SQLException e) {
            System.out.println("Error al obtener la tabla de resultados.");
            System.exit(1);
        }
        return result;
    }

    // Método que genera los pronósticos, comparando con resultados mientras carga
    private static HashMap<String, HashMap<Integer,
            Pronostico>> getPronosticos(HashMap<Integer, HashMap<Integer, HashMap<Integer,
            Partido>>> resultados) {

        HashMap<String, HashMap<Integer, Pronostico>> result = new HashMap<>();

        // En el try con recursos abrimos la conexión (no hace falta cerrar) y ejecutamos la solicitud sql
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM pronosticos")) {

            // Iteramos por los pronósticos
            while (rs.next()) {

                // Obtenemos los valores de una fila
                int faseID = rs.getInt("id_fase");
                int rondaID = rs.getInt("id_ronda");
                String participante = rs.getString("participante");
                String eq1 = rs.getString("equipo_1");
                String eq2 = rs.getString("equipo_2");
                String resultado = rs.getString("resultado");

                // Preparamos los equipos
                Equipo equipo1 = new Equipo(eq1);
                Equipo equipo2 = new Equipo(eq2);
                Partido dummyPartido = new Partido(equipo1, equipo2);

                // Recuperamos el partido de los resultados comparando hashcodes
                Partido partido = resultados.get(faseID).get(rondaID).get(dummyPartido.hashCode());

                // Codificamos el resultado en dos variables
                Equipo equipo = codificarEquipo(resultado, equipo1, equipo2);
                ResultadoEnum resEnum = codificarResultado(resultado);

                // Creamos el pronóstico
                Pronostico pronostico = new Pronostico(faseID, rondaID, partido, equipo, resEnum);

                // Generamos participante y pronóstico (si no existen)
                if (!result.containsKey(participante)) {
                    result.put(participante, new HashMap<>());
                }
                if (!result.get(participante).containsKey(pronostico.hashCode())) {
                    result.get(participante).put(pronostico.hashCode(), pronostico);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la tabla de pronósticos.");
            System.exit(1);
        }
        return result;
    }

    // Método que devuelve un hashmap de puntajes
    // Utiliza resultados, pronósticos y el vector de puntos
    public static HashMap<String, Integer> getPuntaje(
            HashMap<Integer, HashMap<Integer, HashMap<Integer, Partido>>> resultados,
            HashMap<String, HashMap<Integer, Pronostico>> pronosticos,
            int[] puntos) {
        HashMap<String, Integer> result = new HashMap<>();  // Creamos el hashmap para guardar resultados

        // Iteramos una vez sobre los participantes para contar los puntos individuales
        for (String participante : pronosticos.keySet()) {
            int puntaje = 0;
            for (Pronostico p : pronosticos.get(participante).values()) {
                puntaje += p.puntos(puntos[0]);
            }
            result.put(participante, puntaje); // Para cada participante, guardamos el puntaje
        }

        // Iteramos sobre los pronósticos de cada participante, creando un subset para cada ronda,
        // y revisando si todos los pronósticos de una ronda son acertados
        for (String participante : pronosticos.keySet()) {
            for (Integer faseID : resultados.keySet()) {
                for (Integer rondaID : resultados.get(faseID).keySet()) {
                    List<Pronostico> pRonda = new ArrayList<>();
                    int pRondaAcertados = 0;
                    for (Pronostico p : pronosticos.get(participante).values()) {
                        if (Objects.equals(p.getRondaID(), rondaID) && Objects.equals(p.getFaseID(), faseID)) {
                            pRonda.add(p);
                            pRondaAcertados += p.contador();
                        }
                    }
                    if (pRonda.size() == pRondaAcertados) {
                        result.replace(participante, result.get(participante) + puntos[1]); // Si se aciertan todos, se suma el puntaje
                    }
                }
            }
        }

        // Iteramos sobre los pronósticos de cada participante, creando un subset para cada fase,
        // y revisando si todos los pronósticos de una fase son acertados
        for (String participante : pronosticos.keySet()) {
            for (Integer faseID : resultados.keySet()) {
                List<Pronostico> pFase = new ArrayList<>();
                int pFaseAcertados = 0;
                for (Pronostico p : pronosticos.get(participante).values()) {
                    if (Objects.equals(p.getFaseID(), faseID)) {
                        pFase.add(p);
                        pFaseAcertados += p.contador();
                    }
                }
                if (pFase.size() == pFaseAcertados) {
                    result.replace(participante, result.get(participante) + puntos[2]); // Si se aciertan todos, se suma el puntaje
                }
            }
        }
        return result;
    }

    // Métodos que codifican el resultado
    private static ResultadoEnum codificarResultado(String resultado) {
        ResultadoEnum r;

        if (resultado.equals("Gana1") || resultado.equals("Gana2")) {  // Significa que gana el equipo 1
            r = ResultadoEnum.GANADOR;
        } else if (resultado.equals("Empata")) {  // Significa empate
            r = ResultadoEnum.EMPATE;
        } else {
            throw new RuntimeException();  // Si el pronóstico tiene un error, devuelve este error.
        }
        return r;
    }

    private static Equipo codificarEquipo(String resultado, Equipo equipo1, Equipo equipo2) {
        Equipo equipo;

        if (resultado.equals("Gana1") || resultado.equals("Empata")) {
            equipo = equipo1;
        } else if (resultado.equals("Gana2")) {  // Significa que gana el equipo 2
            equipo = equipo2;  // El equipo ganador
        } else {
            throw new RuntimeException();  // Si el pronóstico tiene un error, devuelve este error.
        }
        return equipo;
    }


    private static void printPuntos(int[] puntajes) {
        System.out.printf("""
                                                    
                        Se computarán: %d %s por cada pronóstico acertado,
                        %-15s%d %s por ronda completa acertada, y
                        %-15s%d %s por fase completa acertada.
                        %n""",
                puntajes[0],
                puntajes[0] == 1 ? "punto" : "puntos",
                " ",
                puntajes[1],
                puntajes[1] == 1 ? "punto adicional" : "puntos adicionales",
                " ",
                puntajes[2],
                puntajes[2] == 1 ? "punto adicional" : "puntos adicionales");
    }
}
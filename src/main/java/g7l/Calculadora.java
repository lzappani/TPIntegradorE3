package g7l;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* Este programa requiere dos argumentos: Los nombres de los archivos de resultados y pronósticos
en la carpeta 'resources', en ese orden. */

public class Calculadora {
    public static void main(String[] args) {

        // Inicializamos las listas
        List<String> resultados = null;
        List<String> pronostico = null;

        try {
            resultados = FileReader(args[0]);
            pronostico = FileReader(args[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ERROR! Argumentos incorrectos.");
            System.exit(1);
        } catch (IOException ioe) {
            System.out.println("ERROR! Algún archivo de entrada no encontrado.");
            System.exit(1);
        }

        // Mensaje de bienvenida
        System.out.printf("""
                Calculadora de Puntajes © 2023 por Grupo-7L y está bajo licencia CC BY-NC-ND 4.0.
                Para ver una copia de esta licencia, visita http://creativecommons.org/licenses/by-nc-nd/4.0/%n%n""");
        System.out.printf("Se computarán: 1 punto por cada pronóstico acertado.%n");


        // Bloque para poblar la lista de rondaList creada con partidos, omitiendo la primera línea (encabezado)
        List<Ronda> rondaList = new ArrayList<>();
        rondaList.add(new Ronda(1));
        for (int i = 1; i < resultados.size(); i++) {
            String[] linea = resultados.get(i).split(",");

            if (linea.length != 5) {
                System.out.printf("Error en el número de campos en la linea %d de resultados.", i);
                System.exit(1);
            }
            // Creamos e inicializamos las variables
            Integer estaRondaID = null;
            String e1 = null;
            String e2 = null;
            Integer goles1 = null;
            Integer goles2 = null;

            // Comprobamos que los campos sean parseables y correctos
            try {
                estaRondaID = Integer.parseInt(linea[0]);
                if (estaRondaID < 1) throw new NumberFormatException();
                e1 = linea[1];
                e2 = linea[4];
                goles1 = Integer.parseInt(linea[2]);
                if (goles1 < 0) throw new NumberFormatException();
                goles2 = Integer.parseInt(linea[3]);
                if (goles2 <0) throw new NumberFormatException();
            }
            catch (NumberFormatException nfe) {
                System.out.printf("Error en algun campo en la linea %d de resultados.", i);
                System.exit(1);
            }


            // Este for crea la ronda, si esta no existe
            boolean rondaExiste = false;
            for (Ronda ronda : rondaList) {
                if (estaRondaID == ronda.getRondaID()) {
                    rondaExiste = true;
                    break;
                }
            }
            if (!rondaExiste) {
                rondaList.add(new Ronda(estaRondaID));
            }

            // Crea los objetos
            for (Ronda ronda : rondaList) {
                if (ronda.getRondaID() == estaRondaID) {
                    ronda.addPartido(new Partido(new Equipo(e1),
                            new Equipo(e2),
                            goles1,
                            goles2));
                }
            }
        }

        // Revisamos que la lista de rondas está correcta
        for (Ronda ronda : rondaList) {
            System.out.println(ronda.toString());
        }

// ---------------------------------------------------------------------------------------------------------------------

        // Poblamos la lista de personas con pronósticos, omitiendo la primera línea (encabezado)
        List<Persona> personaList = new ArrayList<>();
        for (int i = 1; i < pronostico.size(); i++) {
            String[] linea = pronostico.get(i).split(",");  // Aislamos la línea

            if (linea.length != 7) {
                System.out.printf("Error en el número de campos en la linea %d de pronosticos.", i);
                System.exit(1);
            }

            // Inicializamos las variables
            String estaPersona = null;
            Integer estaRondaID = null;
            String e1 = null;
            String e2 = null;

            Partido partido = null;
            ResultadoEnum resultado = null;
            Equipo equipo = null;


            try {
                estaPersona = linea[0];  // El nombre de la persona en esta línea
                estaRondaID = Integer.parseInt(linea[1]); // La ronda de esta línea
                e1 = linea[2];
                e2 = linea[6];
            }
            catch (NumberFormatException nfe) {
                System.out.printf("Error en algun campo en la linea %d de pronosticos.", i);
                System.exit(1);
            }


            // Buscamos si la persona ya existe en la lista, si no, la creamos
            boolean personaExiste = false;
            for (Persona persona : personaList) {
                if (estaPersona.equals(persona.getNombre())) {
                    personaExiste = true;
                    break;
                }
            }
            if (!personaExiste) {
                personaList.add(new Persona(estaPersona));
            }

            // Creamos los equipos del pronóstico
            Equipo equipo1 = new Equipo(e1);
            Equipo equipo2 = new Equipo(e2);

            // Buscamos el partido correspondiente en la ronda
            try {
                for (Ronda ronda : rondaList) {
                    if (ronda.getRondaID() == estaRondaID) {
                        partido = ronda.returnPartido(equipo1, equipo2);
                        break;
                    }
                }
            }
            catch (Exception e) {
                System.out.println("ERROR! Partido de pronostico no encontrado en los resultados.");
                System.exit(1);
            }
            // Aquí definimos un array de boolean para codificar el pronóstico
            boolean[] comparador = new boolean[3];
            for (int j = 3; j < 6; j++) {
                comparador[j - 3] = linea[j].equals("X");
            }

            if (Arrays.equals(comparador, new boolean[]{true, false, false})) {  // Gana equipo 1
                equipo = equipo1;  // El equipo ganador
                resultado = ResultadoEnum.GANADOR;
            } else if (Arrays.equals(comparador, new boolean[]{false, false, true})) {  // Gana equipo 2
                equipo = equipo2;  // El equipo ganador
                resultado = ResultadoEnum.GANADOR;
            } else if (Arrays.equals(comparador, new boolean[]{false, true, false})) {  // Empate
                equipo = equipo1;  // Si hay empate esto no importa, pero igual se define (también puede ser equipo2)
                resultado = ResultadoEnum.EMPATE;
            } else {
                System.out.printf("ERROR en el formato del pronóstico numero %d", i);
                System.exit(1);  // Si el pronóstico tiene un error, devuelve este error.
            }

            // Por último agregamos un pronóstico a la lista, con las variables que procesamos de la línea
            for (Persona persona : personaList) {
                if (persona.getNombre().equals(estaPersona)) {
                    persona.addPronostico(new Pronostico(estaRondaID, partido, equipo, resultado));
                }
            }
        }

        // Revisamos que la lista de personas está correcta
        for (Persona persona : personaList) {
            System.out.println(persona.toString());
        }


        // Iterando por los pronósticos de la lista de pronósticos, vamos sumando el puntaje
        for (Persona p : personaList) {
            System.out.printf("%s: %d%n", p.getNombre(), contarPuntos(p));
        }


    }

    public static List<String> FileReader(String archivo) throws IOException {
        return Files.readAllLines(Paths.get("src/main/resources/" + archivo));
    }

    //Método contarPuntos:
    public static int contarPuntos(Persona p) {
        int puntaje = 0;
        for (Pronostico q : p.getPronosticoList()) {
            puntaje += q.puntos();
        }
        return puntaje;
    }
}
package g7l;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.HashMap;

class CalculadoraTest {

    @Test
    public void testGetPuntaje() {
        // Hashmap resultados
        HashMap<Integer, HashMap<Integer, HashMap<Integer, Partido>>> resultados = new HashMap<>();


        // Crear equipos
        Equipo equipo1 = new Equipo("Argentina");
        Equipo equipo2 = new Equipo("Arabia Saudita");


        //Partidos de la fase 1

        //Primer partido, correspondiente a la primera ronda de la primera fase
        Partido partido1 = new Partido(equipo1, equipo2, 1, 2);

        //Segundo partido, correspondiente a la segunda ronda de la primera fase
        Partido partido2 = new Partido(equipo1, equipo2, 2, 0);


        //Partidos de la fase 2

        //Primer partido, correspondiente a la primera ronda de la segunda fase
        Partido partido3 = new Partido(equipo1, equipo2, 2, 1);
        //Segundo partido, correspondiente a la segunda ronda de la segunda fase
        Partido partido4 = new Partido(equipo1, equipo2, 0, 2);


        //Hashmap para las fases
        HashMap<Integer, HashMap<Integer, Partido>> fase1 = new HashMap<>();
        HashMap<Integer, HashMap<Integer, Partido>> fase2 = new HashMap<>();


        //Hashmap para las rondas correspondientes a cada fase
        HashMap<Integer, Partido> ronda1Fase1 = new HashMap<>();
        HashMap<Integer, Partido> ronda2Fase1 = new HashMap<>();
        HashMap<Integer, Partido> ronda1Fase2 = new HashMap<>();
        HashMap<Integer, Partido> ronda2Fase2 = new HashMap<>();


        //Metemos partidos dentro de cada ronda correspondiente a cada fase
        ronda1Fase1.put(partido1.hashCode(), partido1);
        ronda2Fase1.put(partido2.hashCode(), partido2);
        ronda1Fase2.put(partido3.hashCode(), partido3);
        ronda2Fase2.put(partido4.hashCode(), partido4);


        //Metemos rondas dentro de cada fase
        fase1.put(1, ronda1Fase1);
        fase1.put(2, ronda2Fase1);
        fase2.put(1, ronda1Fase2);
        fase2.put(2, ronda2Fase2);


        //Metemos los resultados de cada fase en hashmap resultados
        resultados.put(1, fase1);
        resultados.put(2, fase2);


        // Hashmap pronosticos
        HashMap<String, HashMap<Integer, Pronostico>> pronosticos = new HashMap<>();

        //Pronostico primer participante: acierta todos los pronósticos
        Pronostico pronostico1 = new Pronostico(1, 1, partido1, equipo2, ResultadoEnum.GANADOR);
        Pronostico pronostico2 = new Pronostico(1, 2, partido2, equipo1, ResultadoEnum.GANADOR);
        Pronostico pronostico3 = new Pronostico(2, 1, partido3, equipo1, ResultadoEnum.GANADOR);
        Pronostico pronostico4 = new Pronostico(2, 2, partido4, equipo2, ResultadoEnum.GANADOR);

        //Pronostico segundo participante: acierta todos los pronósticos excepto el último
        Pronostico pronostico5 = new Pronostico(1, 1, partido1, equipo1, ResultadoEnum.PERDEDOR);
        Pronostico pronostico6 = new Pronostico(1, 2, partido2, equipo2, ResultadoEnum.PERDEDOR);
        Pronostico pronostico7 = new Pronostico(2, 1, partido3, equipo2, ResultadoEnum.PERDEDOR);
        Pronostico pronostico8 = new Pronostico(2, 2, partido4, equipo1, ResultadoEnum.EMPATE);


        //Hashmap participante1
        HashMap<Integer, Pronostico> participante1 = new HashMap<>();

        //Almacenando cada pronóstico del participante1
        participante1.put(pronostico1.hashCode(), pronostico1);
        participante1.put(pronostico2.hashCode(), pronostico2);
        participante1.put(pronostico3.hashCode(), pronostico3);
        participante1.put(pronostico4.hashCode(), pronostico4);


        //Hashmap participante2
        HashMap<Integer, Pronostico> participante2 = new HashMap<>();

        //Almacenando cada pronóstico del participante2
        participante2.put(pronostico5.hashCode(), pronostico5);
        participante2.put(pronostico6.hashCode(), pronostico6);
        participante2.put(pronostico7.hashCode(), pronostico7);
        participante2.put(pronostico8.hashCode(), pronostico8);


        //Definiendo claves para ambos participantes
        pronosticos.put("Fede", participante1);
        pronosticos.put("Lucas", participante2);


        // Simulando argumentos del programa
        int[] puntos = new int[]{1, 2, 3};


        // Hashmap con los resultados esperables para cada participante
        HashMap<String, Integer> expected = new HashMap<>();

        /*
        Aciertos totales "Fede":
        4 partidos (1 + 1 + 1 + 1) +
        4 rondas (2 + 2 + 2 + 2) +
        2 fases (3 + 3) = 18
        */
        expected.put("Fede", 18);

        /*
        Aciertos totales "Lucas":
        3 partidos (1 + 1 + 1) +
        3 rondas (2 + 2 + 2) +
        1 fase (3) = 12
        */
        expected.put("Lucas", 12);


        // Hashmap con los resultados de cada participante
        HashMap<String, Integer> actual = Calculadora.getPuntaje(resultados, pronosticos, puntos);


        // Comprobando que los resultados esperables concuerden con los reales
        Assertions.assertEquals(expected, actual);
    }
}

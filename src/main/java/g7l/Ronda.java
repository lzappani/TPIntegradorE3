package g7l;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Ronda {

    // Atributos
    private final int rondaID;
    private final List<Partido> partidos;

    // Constructor
    public Ronda(int rondaID) {
        this.rondaID = rondaID;
        this.partidos = new ArrayList<>();
    }


    // Este método agrega partidos a la ronda.
    public void addPartido(Partido partido) {
        this.partidos.add(partido);
    }


    // Este método devuelve el partido de la ronda que coincide con los argumentos.
    public Partido returnPartido(Equipo equipo1, Equipo equipo2) throws Exception {
        Partido result = null;
        for (Partido partido : partidos) {
            if (partido.compararPartido(equipo1, equipo2)) {
                result = partido;
                break;
            }
        }
        if (result == null) throw new Exception();
        return result;
    }


    @Override
    public String toString() {
        return "Ronda " + rondaID +
                "\n\tPartidos: " + partidos;
    }


    /*
    public int puntos() {
        return 1;
    }
    */
}

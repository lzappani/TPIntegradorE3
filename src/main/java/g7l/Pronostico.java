package g7l;

import lombok.Data;

@Data
public class Pronostico {

    // Atributos:
    private final int rondaID;
    private final Partido partido;
    private final Equipo equipo;
    private final ResultadoEnum resultado;


    // Este método devuelve el puntaje del pronóstico
    public int puntos() {
        ResultadoEnum resultadoPartido = this.partido.resultado(this.equipo);
        return resultadoPartido.equals(resultado) ? 1 : 0;  // Operador ternario, version más simple del if()
    }


    @Override
    public String toString() {
        return "\n\t\tRonda: " + rondaID +
                " | partido: " + partido +
                " | " + equipo.getNombre() +
                " " + resultado;
    }
}

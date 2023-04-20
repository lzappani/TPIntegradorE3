package g7l;

import lombok.Data;

@Data
public class Pronostico {

    // Atributos:
    private final Integer faseID;
    private final Integer rondaID;
    private final Partido partido;
    private final Equipo equipo;
    private final ResultadoEnum resultado;


    // Este método devuelve el puntaje del pronóstico
    public int puntos(int valor) {
        ResultadoEnum resultadoPartido = this.partido.resultado(this.equipo);
        return resultadoPartido.equals(resultado) ? valor : 0;  // Operador ternario, version más simple del if()
    }

    public int contador() {
        ResultadoEnum resultadoPartido = this.partido.resultado(this.equipo);
        return resultadoPartido.equals(resultado) ? 1 : 0;  // Operador ternario, version más simple del if()
    }


    // Sobrescribimos métodos
    @Override
    public String toString() {
        if (this.resultado.equals(ResultadoEnum.EMPATE)) {
            return "\n\tFase: " + faseID +
                    ", Ronda: " + rondaID +
                    " | partido: " + partido +
                    " | " + resultado;
        } else {
            return "\n\tFase: " + faseID +
                    ", Ronda: " + rondaID +
                    " | partido: " + partido +
                    " | " + equipo.getNombre() +
                    " " + resultado;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pronostico that)) return false;
        return faseID.equals(that.faseID) && rondaID.equals(that.rondaID) && partido.equals(that.partido)
                && equipo.equals(that.equipo) && resultado == that.resultado;
    }
}

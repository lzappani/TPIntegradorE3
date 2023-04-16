package g7l;

import lombok.Data;
import lombok.NonNull;

import java.util.Objects;

@Data
public class Partido {

    // Atributos
    @NonNull
    Equipo equipo1;
    @NonNull
    Equipo equipo2;
    @NonNull
    int golesEquipo1;
    @NonNull
    int golesEquipo2;


    // Este método devuelve el estatus del equipo usado como argumento (GANADOR o PERDEDOR), o EMPATE si es empate
    public ResultadoEnum resultado(Equipo equipo) {
        ResultadoEnum resultado = null;
        if (equipo.equals(this.equipo1) || equipo.equals(this.equipo2)) {
            if (this.golesEquipo1 == this.golesEquipo2) {
                resultado = ResultadoEnum.EMPATE;
            } else if (this.golesEquipo1 > this.golesEquipo2) {
                resultado = equipo.equals(this.equipo1) ? ResultadoEnum.GANADOR : ResultadoEnum.PERDEDOR;
            } else {
                resultado = equipo.equals(this.equipo1) ? ResultadoEnum.PERDEDOR : ResultadoEnum.GANADOR;
            }
        }
        return resultado;
    }


    // Este método compara si los equipos usados como argumento coinciden con los del partido
    public boolean compararPartido(Equipo equipo1, Equipo equipo2) {
        boolean a = this.equipo1.equals(equipo1);
        boolean b = this.equipo2.equals(equipo2);
        boolean c = this.equipo1.equals(equipo2);
        boolean d = this.equipo2.equals(equipo1);
        return (a && b) || (c && d);
    }


    // Sobreescribimos estos métodos para poder comparar Partidos.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Partido partido)) return false;
        return equipo1.equals(partido.equipo1) && equipo2.equals(partido.equipo2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(equipo1, equipo2);
    }

    @Override
    public String toString() {
        return equipo1.getNombre() + " : " + equipo2.getNombre();
    }
}

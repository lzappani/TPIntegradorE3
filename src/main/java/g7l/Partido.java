package g7l;

import lombok.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor @AllArgsConstructor
public class Partido {
    // Atributos
    @NonNull
    Set<Equipo> equipos = new HashSet<>(2);
    HashMap<Equipo, Integer> goles = new HashMap<>();

    public Partido(@NonNull Equipo equipo1, @NonNull Equipo equipo2, int golesEquipo1, int golesEquipo2) {
        this.equipos.add(equipo1);
        this.equipos.add(equipo2);
        this.goles.put(equipo1, golesEquipo1);
        this.goles.put(equipo2, golesEquipo2);
    }

    public Partido(@NonNull Equipo equipo1, @NonNull Equipo equipo2) {
        this.equipos.add(equipo1);
        this.equipos.add(equipo2);
    }

    // Este método devuelve el estatus del equipo usado como argumento (GANADOR o PERDEDOR), o EMPATE si es empate
    public ResultadoEnum resultado(Equipo equipo) {
        ResultadoEnum resultado = null;
        if (this.equipos.contains(equipo)) {
            Equipo otroEquipo = this.equipos.stream().
                    filter(i -> !i.equals(equipo)).
                    findFirst().
                    orElse(null);
            if (Objects.equals(this.goles.get(equipo), this.goles.get(otroEquipo))) {
                resultado = ResultadoEnum.EMPATE;
            } else if (this.goles.get(equipo) > this.goles.get(otroEquipo)) {
                resultado = ResultadoEnum.GANADOR;
            } else {
                resultado = ResultadoEnum.PERDEDOR;
            }
        }
        return resultado;
    }


    // Sobrescribimos métodos

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Partido partido)) return false;
        return equipos.equals(partido.equipos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(equipos);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Equipo equipo : equipos) {
            result.append(equipo.toString()).append(" : ");
        }
        return  result.substring(0, result.length() - 3);
    }
}

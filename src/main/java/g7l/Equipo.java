package g7l;

import lombok.Data;

import java.util.Objects;

@Data
public class Equipo {

    // Atributos
    private final String nombre;
    private String descripcion;


    // Sobrescribimos estos métodos para poder comparar Equipos entre sí
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Equipo equipo)) return false;
        return nombre.equals(equipo.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    @Override
    public String toString() {
        return nombre;
    }
}

package g7l;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Persona {

    // Atributos
    private String nombre;
    private final List<Pronostico> pronosticoList;

    // Constructor
    public Persona(String nombre) {
        this.nombre = nombre;
        this.pronosticoList = new ArrayList<>();
    }


    public void addPronostico(Pronostico pronostico) {
        this.pronosticoList.add(pronostico);
    }


    @Override
    public String toString() {
        return "Persona: " + nombre + "\n" +
                "\tPronosticos: \n\t\t" + pronosticoList;
    }
}

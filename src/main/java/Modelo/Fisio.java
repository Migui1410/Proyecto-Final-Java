package Modelo;

public class Fisio extends Especialista {
    private String parteCuerpoEspecialidad;

    public Fisio(String dni, String nombre, String apellidos, double sueldo, int numCon, String parteCuerpoEspecialidad) {
        super(dni, nombre, apellidos, sueldo, numCon);
        this.parteCuerpoEspecialidad = parteCuerpoEspecialidad;
    }

    public String getParteCuerpoEspecialidad() {
        return parteCuerpoEspecialidad;
    }

    public void setParteCuerpoEspecialidad(String parteCuerpoEspecialidad) {
        this.parteCuerpoEspecialidad = parteCuerpoEspecialidad;
    }
}
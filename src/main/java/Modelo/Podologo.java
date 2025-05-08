package Modelo;

public class Podologo extends Especialista {
    private int anosDeExperiencia;

    public Podologo(String dni, String nombre, String apellidos, double sueldo, int numCon, int anosDeExperiencia) {
        super(dni, nombre, apellidos, sueldo, numCon);
        this.anosDeExperiencia = anosDeExperiencia;
    }

    public int getAnosDeExperiencia() {
        return anosDeExperiencia;
    }

    public void setAnosDeExperiencia(int anosDeExperiencia) {
        this.anosDeExperiencia = anosDeExperiencia;
    }
}
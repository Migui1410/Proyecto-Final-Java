package Modelo;

public class Consulta {
    private int numero;
    private String nombre;
    private int planta;

    public Consulta(int numero, String nombre, int planta) {
        this.numero = numero;
        this.nombre = nombre;
        this.planta = planta;
    }

    // Getters y Setters
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPlanta() {
        return planta;
    }

    public void setPlanta(int planta) {
        this.planta = planta;
    }
}
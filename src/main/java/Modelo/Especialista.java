package Modelo;

public class Especialista {
    private String dni;
    private String nombre;
    private String apellidos;
    private double sueldo;
    private int numCon;

    public Especialista(String dni, String nombre, String apellidos, double sueldo, int numCon) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.sueldo = sueldo;
        this.numCon = numCon;
    }

    // Getters y Setters
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public double getSueldo() {
        return sueldo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }

    public int getNumCon() {
        return numCon;
    }

    public void setNumCon(int numCon) {
        this.numCon = numCon;
    }
}
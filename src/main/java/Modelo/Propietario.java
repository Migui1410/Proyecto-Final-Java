package Modelo;

public class Propietario {
    private int id;
    private String nombre;
    private String apellidos;
    private int anoSiendoPropietario;

    public Propietario(int id, String nombre, String apellidos, int anoSiendoPropietario) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.anoSiendoPropietario = anoSiendoPropietario;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getAnoSiendoPropietario() {
        return anoSiendoPropietario;
    }

    public void setAnoSiendoPropietario(int anoSiendoPropietario) {
        this.anoSiendoPropietario = anoSiendoPropietario;
    }
}
package Modelo;

public class Receta {
    private int id;
    private String medicamentos;
    private double precio;
    private int idLesion;
    private String dniCliente;
    private String dniEsp;

    public Receta(int id, String medicamentos, double precio, int idLesion, String dniCliente, String dniEsp) {
        this.id = id;
        this.medicamentos = medicamentos;
        this.precio = precio;
        this.idLesion = idLesion;
        this.dniCliente = dniCliente;
        this.dniEsp = dniEsp;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(String medicamentos) {
        this.medicamentos = medicamentos;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getIdLesion() {
        return idLesion;
    }

    public void setIdLesion(int idLesion) {
        this.idLesion = idLesion;
    }

    public String getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }

    public String getDniEsp() {
        return dniEsp;
    }

    public void setDniEsp(String dniEsp) {
        this.dniEsp = dniEsp;
    }
}
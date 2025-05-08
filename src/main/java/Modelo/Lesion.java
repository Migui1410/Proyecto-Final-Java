package Modelo;

public class Lesion {
    private int id;
    private String nombre;
    private String parteCuerpoAfectada;

    public Lesion(int id, String nombre, String parteCuerpoAfectada) {
        this.id = id;
        this.nombre = nombre;
        this.parteCuerpoAfectada = parteCuerpoAfectada;
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

    public String getParteCuerpoAfectada() {
        return parteCuerpoAfectada;
    }

    public void setParteCuerpoAfectada(String parteCuerpoAfectada) {
        this.parteCuerpoAfectada = parteCuerpoAfectada;
    }
}
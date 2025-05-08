package Modelo;

import java.time.LocalDate;

public class Cita {
    private int id;
    private LocalDate fecha;

    public Cita(int id, LocalDate fecha) {
        this.id = id;
        this.fecha = fecha;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
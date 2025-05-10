package Modelo;

import java.time.LocalDate;
import java.util.ArrayList;

public class Cita {
	private ArrayList<Cliente> listacitas = new ArrayList<>();
    private int id;
    private LocalDate fecha;

    public Cita(int id, LocalDate fecha) {
        this.id = id;
        this.fecha = fecha;
    }
    public int longi() {
		return listacitas.size();
	}

    public ArrayList<Cliente> getListacitas() {
		return listacitas;
	}

	public void setListacitas(ArrayList<Cliente> listacitas) {
		this.listacitas = listacitas;
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
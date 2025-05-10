package Modelo;

import java.time.LocalDate;
import java.util.ArrayList;

public class Cita {
	private static ArrayList<Cita> listacitas = new ArrayList<>();
    private int id;
    private LocalDate fecha;
    private String nombreCli;
    private String nombreEsp;

    
    
    public Cita(int id, LocalDate fecha,String nombreCli,String nombreEsp) {
        this.id = id;
        this.fecha = fecha;
        this.nombreCli = nombreCli;
        this.nombreEsp = nombreEsp;
    }
    
    public void agregarCita(Cita c) {
 	   listacitas.add(c);
    }
     
    public String getNombreCli() {
		return nombreCli;
	}
	public void setNombreCli(String nombreCli) {
		this.nombreCli = nombreCli;
	}
	public String getNombreEsp() {
		return nombreEsp;
	}
	public void setNombreEsp(String nombreEsp) {
		this.nombreEsp = nombreEsp;
	}
	public int longi() {
		return listacitas.size();
	}

    public static ArrayList<Cita> getListacitas() {
		return listacitas;
	}

	public void setListacitas(ArrayList<Cita> listacitas) {
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
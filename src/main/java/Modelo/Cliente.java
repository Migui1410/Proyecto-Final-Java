package Modelo;

import java.time.LocalDate;
import java.util.ArrayList;

public class Cliente {
	private ArrayList<Cliente> listacli = new ArrayList<>();
    private String dni;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;

    public int longi() {
		return listacli.size();
	}
    public ArrayList<Cliente> getListacli() {
		return listacli;
	}

   
	public void setListacli(ArrayList<Cliente> listacli) {
		this.listacli = listacli;
	}

	public Cliente(String dni, String nombre, String apellidos, LocalDate fechaNacimiento) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
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

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
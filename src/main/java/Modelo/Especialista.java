package Modelo;

import java.util.ArrayList;

public class Especialista {
    private String dni;
    private String nombre;
    private String apellidos;
    private double sueldo;
    private int numCon;
    private static ArrayList<Especialista> listaesp = new ArrayList<>();

    public void agregarEsp(Especialista es) {
  	   listaesp.add(es);
     }
    
    public Especialista(String dni, String nombre, String apellidos, double sueldo, int numCon) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.sueldo = sueldo;
        this.numCon = numCon;
    }


    public static ArrayList<Especialista> getListaesp() {
		return listaesp;
	}

	public void setListaesp(ArrayList<Especialista> listaesp) {
		this.listaesp = listaesp;
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
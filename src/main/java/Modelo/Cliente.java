package Modelo;

import java.time.LocalDate;
import java.util.ArrayList;


public class Cliente extends Usuario {
    private static ArrayList<Cliente> listacli = new ArrayList<>();
    private String dni;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;

    public Cliente(String nombreUsuario, String contrasena, String dni, String nombre, String apellidos, LocalDate fechaNacimiento) {
        super(nombreUsuario, contrasena);  
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
    }

    public void agregarCliente(Cliente c) {
        listacli.add(c);
    }

    public static ArrayList<Cliente> getListacli() {
        return listacli;
    }

    public void setListacli(ArrayList<Cliente> listacli) {
        Cliente.listacli = listacli;
    }

    @Override
    public boolean esAdmin() {
        return false;
    }

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

    

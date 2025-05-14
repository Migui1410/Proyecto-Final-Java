package Modelo;

import java.time.LocalDate;
import java.util.ArrayList;


public class Cliente {
    private String dni;
    private String nombre;
    private String apellidos;
    private LocalDate fecha_nacimiento;

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
        return fecha_nacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fecha_nacimiento = fechaNacimiento;
    }
}

    

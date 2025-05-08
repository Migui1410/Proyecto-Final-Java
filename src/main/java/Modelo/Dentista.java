package Modelo;

public class Dentista extends Especialista {
    private String tipoImplante;
    private boolean realizaOperaciones;
    private String dniSupervisar;

    public Dentista(String dni, String nombre, String apellidos, double sueldo, int numCon, 
                   String tipoImplante, boolean realizaOperaciones, String dniSupervisar) {
        super(dni, nombre, apellidos, sueldo, numCon);
        this.tipoImplante = tipoImplante;
        this.realizaOperaciones = realizaOperaciones;
        this.dniSupervisar = dniSupervisar;
    }

    // Getters y Setters
    public String getTipoImplante() {
        return tipoImplante;
    }

    public void setTipoImplante(String tipoImplante) {
        this.tipoImplante = tipoImplante;
    }

    public boolean isRealizaOperaciones() {
        return realizaOperaciones;
    }

    public void setRealizaOperaciones(boolean realizaOperaciones) {
        this.realizaOperaciones = realizaOperaciones;
    }

    public String getDniSupervisar() {
        return dniSupervisar;
    }

    public void setDniSupervisar(String dniSupervisar) {
        this.dniSupervisar = dniSupervisar;
    }
}
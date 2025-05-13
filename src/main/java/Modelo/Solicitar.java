package Modelo;

public class Solicitar {
    private int idCita;
    private String dniCliente;
    private String dniEspecialista;

    public Solicitar(int idCita, String dniCliente, String dniEspecialista) {
        this.idCita = idCita;
        this.dniCliente = dniCliente;
        this.dniEspecialista = dniEspecialista;
    }

    // Getters y Setters
    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public String getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }

    public String getDniEspecialista() {
        return dniEspecialista;
    }

    public void setDniEspecialista(String dniEspecialista) {
        this.dniEspecialista = dniEspecialista;
    }
}

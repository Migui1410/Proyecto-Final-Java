package Modelo;

public class Solicitar {
    private int idCita;
    private String dniCliente;
    private String dniEsp;

    public Solicitar(int idCita, String dniCliente, String dniEspecialista) {
        this.idCita = idCita;
        this.dniCliente = dniCliente;
        this.dniEsp = dniEspecialista;
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
        return dniEsp;
    }

    public void setDniEspecialista(String dniEspecialista) {
        this.dniEsp = dniEspecialista;
    }
}

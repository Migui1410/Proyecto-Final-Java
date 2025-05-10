package Modelo;

public abstract class Usuario {
	private String nombreuser;
	private String contrasena;
	private boolean esAdmin = false;
	private boolean activo = false;
	
	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public Usuario(String nombreuser, String contrasena) {
		this.nombreuser = nombreuser;
		this.contrasena = contrasena;
	}

	public boolean verificarContrasena(String contrasena) {
		if (contrasena.equals(this.contrasena)) {
			return true;
		}else {
			return false;
		}
	}
	public String getNombreuser() {
		return nombreuser;
	}

	public void setNombreuser(String nombreuser) {
		this.nombreuser = nombreuser;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	
	
	public abstract boolean esAdmin();
	
}

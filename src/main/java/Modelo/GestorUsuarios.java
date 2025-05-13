package Modelo;

import java.util.ArrayList;

public class GestorUsuarios {
	private static ArrayList<Usuario> usuarios = new ArrayList<>();
	
	public static Usuario autenticar(String nombre,String passwd) {
		for(Usuario u: usuarios) {
			if (u.getNombreuser().equals(nombre) && u.verificarContrasena(passwd)) {
				u.setActivo(true);
				return u;
			}
		}
		return null;
	}
	
	public static ArrayList<Usuario> getUsuarios() {
		return usuarios;
	}

	public static void agregarUser(Usuario usuario) {
		usuarios.add(usuario);
	}
	
	 public static Usuario Activo() {
	        for (Usuario us : usuarios) {
	            if (us.isActivo()) {
	                return us;
	            }
	        }
	        return null; 
	    }
	 
	 public static void cerrarSesion(Usuario usuario) {
	        if (usuario != null) {
	            usuario.setActivo(false);
	        }
	    }
	
}

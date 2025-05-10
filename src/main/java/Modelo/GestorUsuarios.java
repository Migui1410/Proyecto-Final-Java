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
		Usuario c = null;
		for (Usuario us : usuarios) {
			if (us.isActivo()) {
				us.setActivo(false);
				return c = us;
			}
		}
		return c = null;
	}
	
}

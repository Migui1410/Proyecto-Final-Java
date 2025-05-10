package Modelo;

import java.util.ArrayList;

public class GestorUsuarios {
	private static ArrayList<Usuario> usuarios = new ArrayList<>();
	
	public static Usuario autenticar(String nombre,String passwd) {
		for(Usuario u: usuarios) {
			if (u.getNombreuser().equals(nombre) && u.verificarContrasena(passwd)) {
				return u;
			}
		}
		return null;
	}
	
	public static void agregarUser(Usuario usuario) {
		usuarios.add(usuario);
	}
}

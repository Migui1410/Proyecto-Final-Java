package Controlador;

import Modelo.Admin;
import Modelo.GestorUsuarios;
import Modelo.UsuarioNormal;
import Vista.InicioSesion;

public class Principal {
	private static Navegador nv = new Navegador();
	public static void main(String [] args){
		GestorUsuarios gs = new GestorUsuarios();
		
		gs.agregarUser(new Admin("Admin","1234"));
		gs.agregarUser(new UsuarioNormal("Pepe","1234"));
		gs.agregarUser(new UsuarioNormal("Paco","1234"));
		
		
		nv.crearVentana(new InicioSesion());
		nv.dispatcher("iniciosesion", true);
	
	}

}

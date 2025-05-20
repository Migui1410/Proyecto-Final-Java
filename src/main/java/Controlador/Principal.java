package Controlador;

import Modelo.Admin;
import Modelo.UsuarioNormal;
import Vista.InicioSesion;

public class Principal {
	private static Navegador nv = new Navegador();
	public static void main(String [] args){
		GestionBasedeDatos gs = new GestionBasedeDatos();
		nv.crearVentana(new InicioSesion());
		nv.dispatcher("iniciosesion", true);
	
	}

}

package Controlador;

import java.util.ArrayList;

import javax.swing.JFrame;

public class Navegador {
	public static ArrayList<JFrame> ventanas = new ArrayList <JFrame>();
	
	public static void crearVentana(JFrame m) {
		ventanas.add(m);
	}
	
	public static void dispatcher(String titulo, boolean c) {
		for(JFrame v:ventanas) {
			if (v.getTitle().equals(titulo)){
				v.setVisible(c);
				break;
			}
		}
	}
	
	public static boolean existe(String titulo) {
		for(JFrame v:ventanas) {
			if (v.getTitle().equals(titulo)){
				
				return true;
			}
		}
		return false;
	}
	
}


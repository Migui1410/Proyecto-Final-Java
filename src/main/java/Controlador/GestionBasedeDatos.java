package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GestionBasedeDatos {
	static String url= "jdbc:postgresql://localhost:5432/ClinicaViaParque";
	public static String user ="postgres";
	public static String pwd = "postgres";
	public static void main(String[] args) {
		prueba();	
	}
	
	public static Connection prueba() {
		Connection cn = null;
		try{
			cn = DriverManager.getConnection(url,user,pwd);
			System.out.println("Conexion establecida");
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		return cn;
	}

	
}


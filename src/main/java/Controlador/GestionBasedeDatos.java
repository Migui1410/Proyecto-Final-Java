package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GestionBasedeDatos {
	static String url= "jdbc:postgresql://localhost:5432/Clinica Via Parque";
	static String user ="postgres";
	static String pwd = "postgres";
	public static void main(String[] args) {
	prueba();	
	}
	
	public static void prueba() {
		try(Connection cn = DriverManager.getConnection(url,user,pwd)){
			String sql = "Select * from especialista";
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				System.out.println(rs.getString("nombre"));
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
}


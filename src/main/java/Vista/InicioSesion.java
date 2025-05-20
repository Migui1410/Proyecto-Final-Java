package Vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controlador.GestionBasedeDatos;
import Controlador.Navegador;

public class InicioSesion extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNombre;
	private JPasswordField passwordField;
	private Navegador nv = new Navegador();

	public InicioSesion() {
		Estilo.aplicarFuenteGlobal();

		setTitle("iniciosesion");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		contentPane = new JPanel();
		Estilo.aplicarEstiloBasico(contentPane);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 434, 261);
		Estilo.aplicarEstiloBasico(panel);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblTituloInicio = new JLabel("Inicio Sesión");
		lblTituloInicio.setBounds(140, 10, 200, 34);
		Estilo.estilizarEtiqueta(lblTituloInicio, true);
		panel.add(lblTituloInicio);

		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setBounds(75, 80, 70, 26);
		Estilo.estilizarEtiqueta(lblUsuario, false);
		panel.add(lblUsuario);

		JLabel lblPassword = new JLabel("Contraseña:");
		lblPassword.setBounds(75, 129, 100, 26);
		Estilo.estilizarEtiqueta(lblPassword, false);
		panel.add(lblPassword);

		textFieldNombre = new JTextField();
		textFieldNombre.setBounds(167, 85, 173, 20);
		panel.add(textFieldNombre);

		passwordField = new JPasswordField();
		passwordField.setBounds(167, 134, 173, 20);
		panel.add(passwordField);

		JButton btnIniciar = new JButton("Iniciar");
		btnIniciar.setBounds(251, 196, 89, 23);
		Estilo.aplicarEstiloBasico(btnIniciar);
		btnIniciar.setFocusPainted(false);
		panel.add(btnIniciar);

		JButton btnBorrar = new JButton("Borrar");
		btnBorrar.setBounds(107, 196, 89, 23);
		Estilo.aplicarEstiloBasico(btnBorrar);
		btnBorrar.setFocusPainted(false);
		panel.add(btnBorrar);

		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = textFieldNombre.getText();
				String passwd = new String(passwordField.getPassword());
				String sql = "SELECT permisos FROM users WHERE nombre = ? AND passwd = ?";
				int permisos = -1;
				try (Connection cn = GestionBasedeDatos.prueba();
					     PreparedStatement st = cn.prepareStatement(sql)) {
					     
					    st.setString(1, nombre);
					    st.setString(2, passwd);

					    ResultSet rs = st.executeQuery();

					    if (rs.next()) {
					        permisos = rs.getInt("permisos");

					        if (!nv.existe("principal")) {
					            VentanaPrincipal vp = new VentanaPrincipal();
					        	vp.actualizarUser(permisos,nombre);
					            nv.crearVentana(vp);
					            nv.dispatcher("principal", true);
					        } else {
					        	VentanaPrincipal vp = (VentanaPrincipal) nv.getVentana("principal");
					        	vp.actualizarUser(permisos,nombre);
					            nv.dispatcher("principal", true);
					        }
					        setVisible(false);
					    } else {
					        JOptionPane.showMessageDialog(null, "Credenciales incorrectas", 
					                "Error", JOptionPane.ERROR_MESSAGE);
					    }
					} catch(Exception ex) {
					    ex.printStackTrace();
					    JOptionPane.showMessageDialog(null, "Error al iniciar sesión", 
					            "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
				

		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)  {
			textFieldNombre.setText("");
			passwordField.setText("");
			}
		});
	}
}

package Vista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controlador.Navegador;
import Modelo.GestorUsuarios;

public class InicioSesion extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNombre;
	private static GestorUsuarios gu = new GestorUsuarios();
	private JPasswordField passwordField;
	private Navegador nv = new Navegador();

	public InicioSesion() {
		// Aplicar fuente global
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

		// Lógica de los botones
		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = textFieldNombre.getText();
				String passwd = new String(passwordField.getPassword());

				if (gu.autenticar(nombre, passwd) != null) {
					if (!nv.existe("principal")) {
						VentanaPrincipal vp = new VentanaPrincipal();
						nv.crearVentana(vp);
						nv.dispatcher("principal", true);
						vp.actualizarUsuarioActivo();
					} else {
						VentanaPrincipal vp = (VentanaPrincipal) nv.getVentana("principal");
						vp.actualizarUsuarioActivo();
						nv.dispatcher("principal", true);
					}
					setVisible(false);
				} else {
					JOptionPane.showMessageDialog(null, "Credenciales incorrectas", 
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnBorrar.addActionListener(e -> {
			textFieldNombre.setText("");
			passwordField.setText("");
		});
	}
}

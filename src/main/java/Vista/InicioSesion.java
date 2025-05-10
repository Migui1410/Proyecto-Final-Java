package Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Controlador.Navegador;
import Modelo.GestorUsuarios;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class InicioSesion extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNombre;
	private static GestorUsuarios gu = new GestorUsuarios();
	private JPasswordField passwordField;
	private Navegador nv = new Navegador();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InicioSesion frame = new InicioSesion();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public InicioSesion() {
		setTitle("iniciosesion");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 434, 261);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblTituloInicio = new JLabel("Inicio Sesion");
		lblTituloInicio.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTituloInicio.setBounds(147, 11, 120, 34);
		panel.add(lblTituloInicio);
		
		JLabel lblUsuario = new JLabel("Usuario");
		lblUsuario.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblUsuario.setBounds(75, 80, 55, 26);
		panel.add(lblUsuario);
		
		JLabel lblPassword = new JLabel("Contraseña");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPassword.setBounds(75, 129, 81, 26);
		panel.add(lblPassword);
		
		textFieldNombre = new JTextField();
		textFieldNombre.setBounds(167, 85, 173, 20);
		panel.add(textFieldNombre);
		textFieldNombre.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(166, 134, 174, 20);
		panel.add(passwordField);
		
		JButton btnIniciar = new JButton("Iniciar");
		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = textFieldNombre.getText();
				String passwd = new String(passwordField.getPassword());//Convertirlo de un array de char a String
			
				
				if(gu.autenticar(nombre, passwd) != null) {
		            if (!nv.existe("principal")) {
		            	nv.crearVentana(new VentanaPrincipal());
		            	nv.dispatcher("principal",true);
		            }else {
		            	nv.dispatcher("principal",true);
		            }
		            setVisible(false);
		        } else {
		            JOptionPane.showMessageDialog(null, "Credenciales incorrectas", 
		                "Error", JOptionPane.ERROR_MESSAGE);
		        }
				
			}
		});
		btnIniciar.setBounds(251, 196, 89, 23);
		panel.add(btnIniciar);
		
		JButton btnBorrar = new JButton("Borrar");
		btnBorrar.setBounds(107, 196, 89, 23);
		panel.add(btnBorrar);
		
		
	}
}

package Vista;

import java.awt.EventQueue;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Controlador.Navegador;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Anadir extends JFrame {
	private String tituloV;
	private JPanel contentPane;
	private Navegador nav = new Navegador();

	public Anadir(String titulo) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				setVisible(false);
				nav.dispatcher("principal", true);
			}
		});
		this.tituloV = titulo;
		setTitle("anadir" + titulo);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
        
		
		JPanel panelFormulario = new JPanel();
		panelFormulario.setBounds(0, 42, 434, 219);
		contentPane.add(panelFormulario);
		panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
		
		JLabel lblTituloAnadir = new JLabel();
		lblTituloAnadir.setText("Añadir " + tituloV);
		lblTituloAnadir.setBounds(166, 1, 135, 30);
		contentPane.add(lblTituloAnadir);
		
		
		
		switch (tituloV.toLowerCase()) {
			case "cliente":
				addCampo(panelFormulario,"DNI: ");
				addCampo(panelFormulario,"Nombre: ");
				addCampo(panelFormulario,"Apellido: ");
				addCampo(panelFormulario,"Fecha de Nacimiento: ");
				break;
			case "especialista":
				addCampo(panelFormulario,"Codigo: ");
				addCampo(panelFormulario,"Nombre: ");
				addCampo(panelFormulario,"Apellido: ");
				addCampo(panelFormulario,"Sueldo: ");
				break;
			case "citas":
				addCampo(panelFormulario,"ID: ");
				addCampo(panelFormulario,"Fecha: ");
				addCampo(panelFormulario,"Nombre Cliente: ");
				addCampo(panelFormulario,"Especialista: ");
				break;
		default:
			System.out.println("Ventana no encontrada");
			break;
		};
	}

	private void addCampo(JPanel formularioPanel, String etiqueta) {
		JLabel lb = new JLabel(etiqueta);
		JTextField txt = new JTextField();
		formularioPanel.add(lb);
		formularioPanel.add(txt);
	}
}
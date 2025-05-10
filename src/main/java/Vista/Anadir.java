package Vista;

import java.awt.EventQueue;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Controlador.Navegador;
import Modelo.Cita;
import Modelo.Cliente;
import Modelo.Especialista;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Anadir extends JFrame {
	private String tituloV;
	private JPanel contentPane;
	private Navegador nav = new Navegador();
	private  ArrayList<JTextField> camposTexto = new ArrayList<>();// ArrayList guardar campos textField

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
		panelFormulario.setBounds(0, 25, 434, 201);
		contentPane.add(panelFormulario);
		panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
		
		JLabel lblTituloAnadir = new JLabel();
		lblTituloAnadir.setText("Añadir " + tituloV);
		lblTituloAnadir.setBounds(166, 1, 135, 30);
		contentPane.add(lblTituloAnadir);
		
		JButton btnCrear = new JButton("Crear");
		
		btnCrear.setBounds(295, 227, 89, 23);
		contentPane.add(btnCrear);
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.setBounds(58, 227, 89, 23);
		contentPane.add(btnVolver);
		
		
		
		switch (tituloV.toLowerCase()) {
			case "cliente":
				addCampo(panelFormulario,"DNI: ");
				addCampo(panelFormulario,"Nombre: ");
				addCampo(panelFormulario,"Apellido: ");
				addCampo(panelFormulario,"Fecha de Nacimiento: ");
				btnCrear.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String dni = camposTexto.get(0).getText();
						String nombre = camposTexto.get(1).getText();
						String apellido = camposTexto.get(2).getText();
						LocalDate fecha = LocalDate.parse(camposTexto.get(3).getText());
						
						Cliente c = new Cliente(dni,nombre,apellido,fecha);
						c.agregarCliente(c);
						 for (JTextField campo : camposTexto) {
				                campo.setText("");
				            }
					}
				});
				break;
			case "especialista":
				addCampo(panelFormulario,"DNI: ");
				addCampo(panelFormulario,"Nombre: ");
				addCampo(panelFormulario,"Apellido: ");
				addCampo(panelFormulario,"Sueldo: ");
				addCampo(panelFormulario,"Num Consulta: ");
				btnCrear.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String dni = camposTexto.get(0).getText();
						String nombre = camposTexto.get(1).getText();
						String apellido = camposTexto.get(2).getText();
						double sueldo = Double.parseDouble(camposTexto.get(3).getText());
						int numcon = Integer.parseInt(camposTexto.get(4).getText());
						
						Especialista es = new Especialista(dni,nombre,apellido,sueldo,numcon);
						es.agregarEsp(es);
						 for (JTextField campo : camposTexto) {
				                campo.setText("");
				            }
					}
				});
				break;
			case "citas":
				addCampo(panelFormulario,"ID: ");
				addCampo(panelFormulario,"Fecha: ");
				addCampo(panelFormulario,"Nombre Cliente: ");
				addCampo(panelFormulario,"Especialista: ");
				btnCrear.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int id = Integer.parseInt(camposTexto.get(0).getText());
						LocalDate fecha = LocalDate.parse(camposTexto.get(1).getText());
						String nombrec = camposTexto.get(2).getText();
						String nombres = camposTexto.get(3).getText();
						
						Cita c = new Cita(id,fecha,nombrec,nombres);
						c.agregarCita(c);
						 for (JTextField campo : camposTexto) {
				                campo.setText("");
				            }
					}
				});
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
		camposTexto.add(txt);
	}
}
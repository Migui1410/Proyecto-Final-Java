package Vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Controlador.Navegador;


import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Mostrar extends JFrame {
	private String tituloV;
	private JTable tableMostrar;
	private static DefaultTableModel model;
	private JPanel contentPane;
	private Navegador nav = new Navegador();
	
	public Mostrar(String tipo) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				setVisible(false);
				nav.dispatcher("principal", true);
			}
		});
		this.tituloV = tipo;
		setTitle("mostrar" + tipo);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 35, 414, 172);
		contentPane.add(panel);
		
		JLabel lblTituloMostrar = new JLabel();
		lblTituloMostrar.setText("Mostrar " + tituloV);
		lblTituloMostrar.setBounds(180, 11, 126, 14);
		contentPane.add(lblTituloMostrar);
		
		JScrollPane scrollPane = new JScrollPane(tableMostrar);
	    panel.add(scrollPane, BorderLayout.CENTER);
	    contentPane.add(panel, BorderLayout.CENTER);
	
		
		//model.setRowCount(0);
		switch(tituloV.toLowerCase()){
		case "cliente":
			String[] columnasCli = {"DNI","Nombre","Apellido","Fecha de Nacimiento"};
			model = new DefaultTableModel(columnasCli, 0);
			break;
		case "especialista":
			String[] columnasEs = {"DNI","Nombre","Apellidos","Sueldo","Num Consulta"};
			model = new DefaultTableModel(columnasEs, 0);
			break;
		case "citas":
			String[] columnasCi = {"ID", "Fecha","Cliente", "Especialista"};
			model = new DefaultTableModel(columnasCi, 0);
			break;
		default:
			System.out.println("Ventana no encontrada");
			break;
		}
		 
		tableMostrar = new JTable(model);
	    contentPane.add(new JScrollPane(tableMostrar), BorderLayout.CENTER);
	    JButton btnNewButton = new JButton("New button");
	    btnNewButton.setBounds(48, 218, 89, 32);
	    contentPane.add(btnNewButton);
	    
	    JButton btnNewButton_1 = new JButton("New button");
	    btnNewButton_1.setBounds(289, 218, 89, 32);
	    contentPane.add(btnNewButton_1);
	    
	    
	}
	
}

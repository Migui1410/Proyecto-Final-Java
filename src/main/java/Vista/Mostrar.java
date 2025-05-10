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
import Modelo.Cita;
import Modelo.Cliente;
import Modelo.Especialista;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Mostrar extends JFrame {
	private static String tituloV;
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
		
			
			@Override
			public void windowActivated(WindowEvent e) {
				actualizarTabla();
			}
		});
		tituloV = tipo;
		setVisible(true);
		setTitle("mostrar" + tipo);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 450);
        setLocationRelativeTo(null);

        // Panel principal con BorderLayout
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);

        // Título centrado
        JLabel lblTitulo = new JLabel("Mostrar " + tipo, JLabel.CENTER);
        lblTitulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        contentPane.add(lblTitulo, BorderLayout.NORTH);
		 
        configurarModeloTabla();
        tableMostrar = new JTable(model);
        tableMostrar.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(tableMostrar);
        contentPane.add(scrollPane, BorderLayout.CENTER);
	    
	    /*JButton btnNewButton_1 = new JButton("New button");
	    btnNewButton_1.setBounds(289, 218, 89, 32);
	    contentPane.add(btnNewButton_1);
	    */
	    
	}
	
	
	private void configurarModeloTabla() {
        String[] columnas;
        switch(tituloV) {
            case "cliente":
                columnas = new String[]{"DNI", "Nombre", "Apellido", "Fecha Nacimiento"};
                break;
            case "especialista":
                columnas = new String[]{"DNI", "Nombre", "Apellidos", "Sueldo", "N° Consulta"};
                break;
            case "citas":
                columnas = new String[]{"ID", "Fecha", "Cliente", "Especialista"};
                break;
            default:
                columnas = new String[]{};
                break;
        }
        model = new DefaultTableModel(columnas, 0);
    }
	
	public static void actualizarTabla() {
		model.setRowCount(0); 
		
		
		switch(tituloV) {
	        case "cliente":
	        	for (Cliente j : Cliente.getListacli()) {
	    			Object[] fila = {
	    					j.getDni(),
	    					j.getNombre(),
	    					j.getApellidos(),
	    					j.getFechaNacimiento(),
	    			};
	    			model.addRow(fila);
	    		}
	            break;
	        case "especialista":
	        	for (Especialista j : Especialista.getListaesp()) {
	    			Object[] fila = {
	    					j.getDni(),
	    					j.getNombre(),
	    					j.getApellidos(),
	    					j.getSueldo(),
	    					j.getNumCon()
	    			};
	    			model.addRow(fila);
	    		}
	            break;
	        case "citas":
	        	for (Cita j : Cita.getListacitas()) {
	    			Object[] fila = {
	    					j.getId(),
	    					j.getFecha(),
	    					j.getNombreCli(),
	    					j.getNombreEsp(),
	    			};
	    			model.addRow(fila);
	    		}
	            break;
	        default:
	            break;
		}
		
	}
}

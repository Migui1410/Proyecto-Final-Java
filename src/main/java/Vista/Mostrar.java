package Vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Controlador.GestionBasedeDatos;
import Controlador.Navegador;
import Modelo.Admin;
import Modelo.Cita;
import Modelo.Cliente;
import Modelo.Especialista;
import Modelo.GestorUsuarios;
import Modelo.Solicitar;
import Modelo.Usuario;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Mostrar extends JFrame {
	private static String tituloV;
	private JTable tableMostrar;
	private static DefaultTableModel model;
	private JPanel contentPane;
	private Navegador nav = new Navegador();
	private GestorUsuarios gs = new GestorUsuarios();
	private Usuario userAct;
	
	public Mostrar(String tipo,Usuario user) {
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
		userAct = user;
		tituloV = tipo;
		setVisible(true);
		setTitle("mostrar" + tipo);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 450);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        Estilo.aplicarEstiloBasico(contentPane);
        setContentPane(contentPane);

        JLabel lblTitulo = new JLabel("Mostrar " + tipo, JLabel.CENTER);
        lblTitulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
        Estilo.estilizarEtiqueta(lblTitulo,true);
        contentPane.add(lblTitulo, BorderLayout.NORTH);
        
        configurarModeloTabla(tipo,userAct);
        tableMostrar = new JTable(model);
        Estilo.estilizarTabla(tableMostrar);
        tableMostrar.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(tableMostrar);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        
     // Panel Botones
        JPanel panelBotones = new JPanel();
        Estilo.aplicarEstiloBasico(panelBotones);
	    
        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               
            }
        });

        panelBotones.add(btnEditar);
        contentPane.add(panelBotones, BorderLayout.SOUTH);
	    
	}
	
	private String configurarModeloTabla(String item,Usuario user) {
	    switch (item.toLowerCase()) {
	        case "cliente":
	        	if (user instanceof Admin) {
	        		 return "SELECT * FROM cliente";
	        	}/*else{
	        		String dni = ((Cliente) user).getDni();
	        		PreparedStatement st = conexion.prepareStatement("SELECT * FROM Solicitar WHERE dni_cliente = ?");
	                st.setString(1, dni); // Asigna el valor al primer parámetro (índice 1)
	                return st;
	        	}*/
                
	           
	        case "especialista":
	            return "SELECT * FROM especialista";
	        case "cita":
	            return 
	                "SELECT id_cita, c.nombre AS \"Nombre Cliente\", e.nombre AS \"Nombre Especialista\", ci.fecha " +
	                "FROM solicitar s, cliente c, especialista e, cita ci " +
	                "WHERE s.id_cita = ci.id AND s.dni_cliente = c.dni AND s.dni_esp = e.dni"
	            ;
	        default:
	            throw new IllegalArgumentException("Consulta no definida para tipo: " + item);
	    }
	}
	
	public List<Solicitar> obtenerCitaPorCliente(String dniCliente) {
	    List<Solicitar> lista = new ArrayList<>();

	    String sql = "SELECT * FROM SOLICITAR WHERE dni_cliente = ?";

	    try (Connection conn = GestionBasedeDatos.prueba();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, dniCliente);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            int idCita = rs.getInt("id_cita");
	            String dniEsp = rs.getString("dni_esp");

	            lista.add(new Solicitar(idCita, dniCliente, dniEsp));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return lista;
	}	

	public void actualizarTabla() {
	    String query = configurarModeloTabla(tituloV,userAct); 
	    try (Connection cn = GestionBasedeDatos.prueba()) {
	        PreparedStatement st = cn.prepareStatement(query);
	        ResultSet rs = st.executeQuery();
	        ResultSetMetaData metaData = rs.getMetaData();
	
	        int columnCount = metaData.getColumnCount();
	        model = new DefaultTableModel(); 
	
	        for (int i = 1; i <= columnCount; i++) {
	            model.addColumn(metaData.getColumnLabel(i));
	        }

	        while (rs.next()) {
	            Object[] fila = new Object[columnCount];
	            for (int i = 0; i < columnCount; i++) {
	                fila[i] = rs.getObject(i + 1);
	            }
	            model.addRow(fila);
	        }
	
	        tableMostrar.setModel(model);
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(this, "Error al cargar datos", "Error", JOptionPane.ERROR_MESSAGE);
	        dispose();
	    }
	}
	

}

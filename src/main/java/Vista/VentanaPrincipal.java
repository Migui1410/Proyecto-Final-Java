package Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Controlador.Navegador;
import Modelo.Admin;
import Modelo.GestorUsuarios;
import Modelo.Usuario;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaPrincipal extends JFrame {
	private Navegador n = new Navegador();
	private static GestorUsuarios gs = new GestorUsuarios();
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Usuario user = gs.Activo();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal frame = new VentanaPrincipal();
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
	public VentanaPrincipal() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				user.setActivo(false);
			}
		});
		setTitle("principal");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setToolTipText("Busqueda");
		setJMenuBar(menuBar);
		
		
		/*---------------Gestion Clientes-------------------*/
		JMenu mnGestionCliente = new JMenu("Gestion Cliente");
		menuBar.add(mnGestionCliente);
		
		JMenuItem ItemAnadir = new JMenuItem("Añadir");
		ItemAnadir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tienePermiso()) {
					abrirVentanaAnadir("cliente");
				}
				
			}
		});
		mnGestionCliente.add(ItemAnadir);
		
		JMenuItem ItemModificar = new JMenuItem("Modificar");
		mnGestionCliente.add(ItemModificar);
		
		JMenuItem ItemEliminar = new JMenuItem("Eliminar");
		mnGestionCliente.add(ItemEliminar);
		
		JMenuItem ItemMostrar = new JMenuItem("Mostrar");
		ItemMostrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tienePermiso()) {
					abrirVentanaMostrar("cliente");				
				}
				
			}
		});
		mnGestionCliente.add(ItemMostrar);
		
		
		/*---------------Gestion Especialistas-------------------*/
		JMenu mnGestionEspecialista = new JMenu("Gestion Especilista");
		menuBar.add(mnGestionEspecialista);
		
		JMenuItem ItemAnadirE = new JMenuItem("Añadir");
		ItemAnadirE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tienePermiso()) {
					abrirVentanaAnadir("especialista");
				}
			}
		});
		mnGestionEspecialista.add(ItemAnadirE);
		
		JMenuItem ItemModificarE = new JMenuItem("Modifcar");
		mnGestionEspecialista.add(ItemModificarE);
		
		JMenuItem ItemEliminarE = new JMenuItem("Eliminar");
		mnGestionEspecialista.add(ItemEliminarE);
		
		JMenuItem ItemMostrarE = new JMenuItem("Mostrar");
		ItemMostrarE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					abrirVentanaMostrar("especialista");
			}
		});
		mnGestionEspecialista.add(ItemMostrarE);
		
		/*---------------Gestion Citas-------------------*/

		JMenu mnGestionCitas = new JMenu("Gestion Citas");
		menuBar.add(mnGestionCitas);
		
		JMenuItem ItemAnadirC = new JMenuItem("Añadir");
		ItemAnadirC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tienePermiso()) {
					abrirVentanaAnadir("citas");
				}
				
			}
		});
		mnGestionCitas.add(ItemAnadirC);
		
		JMenuItem ItemModificarC = new JMenuItem("Modificar");
		mnGestionCitas.add(ItemModificarC);
		
		JMenuItem ItemEliminarC = new JMenuItem("Eliminar");
		mnGestionCitas.add(ItemEliminarC);
		
		JMenuItem ItemMostrarC = new JMenuItem("Mostrar");
		ItemMostrarC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					abrirVentanaMostrar("citas");
			}
		});
		mnGestionCitas.add(ItemMostrarC);
		
		
		
		/*-----------------Busqueda---------------------*/

		JMenu mnGestionBusqueda = new JMenu("Buscar");
		menuBar.add(mnGestionBusqueda);
		
		JMenuItem BuscarCliente = new JMenuItem("Cliente");
		mnGestionBusqueda.add(BuscarCliente);
		
		JMenuItem BuscarEspecialista = new JMenuItem("Especialista");
		mnGestionBusqueda.add(BuscarEspecialista);
		
		JMenuItem BuscarCita = new JMenuItem("Citas");
		mnGestionBusqueda.add(BuscarCita);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 434, 239);
		contentPane.add(panel);
		
		
	}
	private boolean tienePermiso() {
		if (user instanceof Admin) {
			return true;
		}else {
			JOptionPane.showMessageDialog(null,"Necesita permisos para realizar esta acción", 
	                "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
	}
	
	private void abrirVentanaAnadir(String tipo) {
	    String titulo = tipo.toLowerCase(); 
	    if(!n.existe("anadir" + titulo)) {
	        n.crearVentana(new Anadir(titulo));
	        n.dispatcher("anadir"+titulo,true);
	    }else {
	        n.dispatcher("anadir"+titulo,true);

	    }
	}
	private void abrirVentanaMostrar(String tipo) {
	    String titulo = tipo.toLowerCase(); 
	    if(!n.existe("mostrar" + titulo)) {
	    	n.crearVentana(new Mostrar(titulo)); 
	        n.dispatcher("mostrar"+titulo, true);
	    }else {
	        n.dispatcher("mostrar"+titulo, true);

	    }
	}
}

package Vista;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import Controlador.GestionBasedeDatos;
import Controlador.Navegador;

public class VentanaPrincipal extends JFrame {
    private Navegador n = new Navegador();
    private static final long serialVersionUID = 1L;
    private int p;
    private String nom;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JLabel bienvenida;

    private JMenuBar menuBar;


    public VentanaPrincipal() {
        

        Estilo.aplicarFuenteGlobal();
        setTitle("principal");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        setMinimumSize(new Dimension(800, 500));

        addWindowListener(new WindowAdapter() {
                @Override
                public void windowOpened(WindowEvent e) {
                	actualizarUser(p,nom);
                	actualizarMenuYBotones();
                }

                @Override
                public void windowActivated(WindowEvent e) {
                	actualizarUser(p,nom);
                    actualizarMenuYBotones();
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    n.dispatcher("iniciosesion", true);
                }
            });

        getContentPane().setBackground(Estilo.FONDO_CONTENIDO);
        getContentPane().setLayout(new BorderLayout());

     // Panel cabecera
        JPanel panelCabecera = new JPanel(new BorderLayout());
        panelCabecera.setBackground(Estilo.FONDO_PANEL_TOP);

        bienvenida = new JLabel("Bienvenido, " + nom);
        Estilo.estilizarEtiqueta(bienvenida, true);
        bienvenida.setFont(Estilo.FUENTE_TITULO);
        bienvenida.setForeground(Color.WHITE);
        panelCabecera.add(bienvenida, BorderLayout.WEST);

        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        Estilo.aplicarEstiloBasico(btnCerrarSesion);
        btnCerrarSesion.setForeground(Estilo.FONDO_CONTENIDO);
        btnCerrarSesion.setOpaque(true);
        btnCerrarSesion.setContentAreaFilled(false);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.addActionListener(e -> {
            setVisible(false);
            n.dispatcher("iniciosesion", true);
            dispose();
        });
        panelCabecera.add(btnCerrarSesion, BorderLayout.EAST);

        getContentPane().add(panelCabecera, BorderLayout.NORTH);
        // Panel central
        getContentPane().add(crearPanelPrincipal(), BorderLayout.CENTER);

        // Menú
        menuBar = new JMenuBar();
        menuBar.setBackground(Estilo.FONDO_CONTENIDO);
        setJMenuBar(menuBar);
        actualizarMenuYBotones();
    }

    public void actualizarUser(int permisos,String nombre) {
    	p = permisos;
        nom = nombre;
    }
    private JPanel crearPanelPrincipal() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(Estilo.FONDO_GENERAL);

        leftPanel = new JPanel(); 
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        leftPanel.setBackground(Estilo.FONDO_GENERAL);

        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Estilo.FONDO_GENERAL);

        ImageIcon icono = new ImageIcon("src/main/java/Img/silueta.png");
        Image img = icono.getImage().getScaledInstance(300, 350, Image.SCALE_SMOOTH);
        JLabel lblImagen = new JLabel(new ImageIcon(img));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.add(lblImagen, BorderLayout.CENTER);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        return mainPanel;
    }

    private boolean tienePermiso() {
        if (p == 0) {
            return true;
        } else {
            return false;
        }
    }

    private void actualizarMenuYBotones() {
        menuBar.removeAll();
        leftPanel.removeAll();
     

        bienvenida.setText("Bienvenido, " + nom);

        if (tienePermiso()) {
            // Menú para admin
            JMenu mnGestionCliente = crearMenu("Gestión Cliente");
            JMenuItem ItemAnadir = crearItemMenu("Añadir");
            ItemAnadir.addActionListener(e -> abrirVentanaAnadir("cliente"));
            mnGestionCliente.add(ItemAnadir);

            JMenuItem ItemMostrar = crearItemMenu("Mostrar");
            ItemMostrar.addActionListener(e -> abrirVentanaMostrar("cliente"));
            mnGestionCliente.add(ItemMostrar);
            menuBar.add(mnGestionCliente);
            
            JMenu mnGestionEspecialista = crearMenu("Gestión Especialista");
            JMenuItem ItemAnadirE = crearItemMenu("Añadir");
            ItemAnadirE.addActionListener(e -> abrirVentanaAnadir("especialista"));
            mnGestionEspecialista.add(ItemAnadirE);

            JMenuItem ItemMostrarE = crearItemMenu("Mostrar");
            ItemMostrarE.addActionListener(e -> abrirVentanaMostrar("especialista"));
            mnGestionEspecialista.add(ItemMostrarE);
            menuBar.add(mnGestionEspecialista);
            
            JMenuItem mnImportar = crearItemMenu("Importar");
    		mnImportar.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				String ruta =  elegirRuta();
    				if(ruta != null) {
    					cargarDatos(ruta);
    				}
    			}
    		});
    		mnGestionEspecialista.add(mnImportar);
    		
    		JMenuItem mnExportar = crearItemMenu("Exportar");
    		mnExportar.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    					SelectorTipo();
    			}
    		});
    		mnGestionEspecialista.add(mnExportar);
    		menuBar.add(mnGestionEspecialista);

            JMenu mnGestionCitas = crearMenu("Gestión Citas");
            JMenuItem ItemAnadirC = crearItemMenu("Añadir");
            ItemAnadirC.addActionListener(e -> abrirVentanaAnadir("solicitar"));
            mnGestionCitas.add(ItemAnadirC);

            JMenuItem ItemMostrarC = crearItemMenu("Mostrar");
            ItemMostrarC.addActionListener(e -> abrirVentanaMostrar("solicitar"));
            mnGestionCitas.add(ItemMostrarC);
            menuBar.add(mnGestionCitas);
            
            JMenu mnGestionUsuarios = crearMenu("Gestión Usuarios");
            JMenuItem ItemAnadirU = crearItemMenu("Añadir");
            ItemAnadirU.addActionListener(e -> abrirVentanaAnadir("usuario"));
            mnGestionUsuarios.add(ItemAnadirU);

            JMenuItem ItemMostrarU = crearItemMenu("Mostrar");
            ItemMostrarU.addActionListener(e -> abrirVentanaMostrar("usuario"));
            mnGestionUsuarios.add(ItemMostrarU);
            menuBar.add(mnGestionUsuarios);

            // Botones para admin
            JButton btnBuscar = new JButton("Buscar");
            Estilo.estilizarBoton(btnBuscar);
            btnBuscar.setPreferredSize(new Dimension(200, 50));
            btnBuscar.setMaximumSize(new Dimension(200, 50));
            JButton btnConsultas = new JButton("Mostrar Consultas");
            Estilo.estilizarBoton(btnConsultas);
            btnConsultas.setPreferredSize(new Dimension(200, 50));
            btnConsultas.setMaximumSize(new Dimension(200, 50));
            JButton btnRecetas = new JButton("Mostras Recetas");
            Estilo.estilizarBoton(btnRecetas);
            btnRecetas.setPreferredSize(new Dimension(200, 50));
            btnRecetas.setMaximumSize(new Dimension(200, 50));

            btnBuscar.addActionListener(e -> abrirVentanaBuscar());
            btnConsultas.addActionListener(e -> abrirVentanaMostrar("consulta"));
            btnRecetas.addActionListener(e -> abrirVentanaMostrar("receta"));

            leftPanel.add(Box.createVerticalGlue());
            leftPanel.add(btnBuscar);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            leftPanel.add(btnConsultas);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            leftPanel.add(btnRecetas);
            leftPanel.add(Box.createVerticalGlue());

        } else {
            

            // Botones para usuario
            JButton btnAgregarCita = new JButton("Añadir Citas");
            Estilo.estilizarBoton(btnAgregarCita);
            btnAgregarCita.setPreferredSize(new Dimension(200, 50));
            btnAgregarCita.setMaximumSize(new Dimension(200, 50)); 
            
            JButton btnMisCitas = new JButton("Ver Mis Citas");
            Estilo.estilizarBoton(btnMisCitas);
            btnMisCitas.setPreferredSize(new Dimension(200, 50));
            btnMisCitas.setMaximumSize(new Dimension(200, 50));
            JButton btnBuscar = new JButton("Buscar");
            Estilo.estilizarBoton(btnBuscar);
            btnBuscar.setPreferredSize(new Dimension(200, 50));
            btnBuscar.setMaximumSize(new Dimension(200, 50)); 
       

            btnAgregarCita.addActionListener(e ->abrirVentanaAnadir("solicitar"));
            btnMisCitas.addActionListener(e -> abrirVentanaMostrar("solicitar"));
            btnBuscar.addActionListener(e -> abrirVentanaBuscar());

            leftPanel.add(Box.createVerticalGlue());
            leftPanel.add(btnAgregarCita);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            leftPanel.add(btnMisCitas);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            leftPanel.add(btnBuscar);
            leftPanel.add(Box.createVerticalGlue());
        }

        leftPanel.revalidate();
        leftPanel.repaint();
        menuBar.revalidate();
        menuBar.repaint();
    }


    private JMenu crearMenu(String texto) {
        JMenu menu = new JMenu(texto);
        menu.setFont(Estilo.FUENTE_BASE);
        menu.setForeground(Estilo.TEXTO_PRIMARIO);
        menu.setBackground(Estilo.FONDO_GENERAL);
        return menu;
    }

    private JMenuItem crearItemMenu(String texto) {
        JMenuItem item = new JMenuItem(texto);
        item.setFont(Estilo.FUENTE_BASE);
        item.setBackground(Estilo.FONDO_CONTENIDO);
        item.setForeground(Estilo.TEXTO_PRIMARIO);
        return item;
    }

    private void abrirVentanaAnadir(String tipo) {
        String titulo = tipo.toLowerCase();
        if (!n.existe("anadir" + titulo)) {
            n.crearVentana(new Anadir(titulo,nom,p));
            n.dispatcher("anadir" + titulo, true);
        } else {
        	Anadir a = (Anadir) n.getVentana("anadir" + titulo);
        	a.actualizarPermisosYUsuario(p,nom);
            n.dispatcher("anadir" + titulo, true);
        }
    }

    private void abrirVentanaMostrar(String tipo) {
        String titulo = tipo.toLowerCase();
        if (!n.existe("mostrar" + titulo)) {
            n.crearVentana(new Mostrar(titulo,nom,p));
            n.dispatcher("mostrar" + titulo, true);
        } else {
            Mostrar m = (Mostrar) n.getVentana("mostrar" + titulo);
            if (tipo.equalsIgnoreCase("especialista")) {
	            m.actualizarTablaEspecialistaConTipo();
	        } else {
	            m.actualizarTabla();
	        }
            m.permisos(nom,p);
            n.dispatcher("mostrar" + titulo, true);
        }
    }
    
    private void abrirVentanaBuscar() {
    	if(!n.existe("buscar")) {
            n.crearVentana(new Buscar(p));
            n.dispatcher("buscar" ,true);
            setVisible(false);
        } else {
        	Buscar b= (Buscar) n.getVentana("buscar");
            b.limpiarFormulario();
            b.limpiarTabla();
            b.permisos(p);
            b.cambiarFormulario();
        	n.dispatcher("buscar",true);
        	setVisible(false);
            
        }
    }
    
    protected String elegirRuta() {
		File f = new File("C:");
		JFileChooser j = new JFileChooser(f);
		j.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("archivos txt", "txt");
		j.addChoosableFileFilter(filter);
		j.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int r = j.showOpenDialog(null);
		if (r == JFileChooser.APPROVE_OPTION) {
			return j.getSelectedFile().getAbsolutePath();
		}else {
			return null;
		}
	}
    

    protected void SelectorTipo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como");

        String[] formatos = { "txt", "js" };
        JComboBox<String> formatoCombo = new JComboBox<>(formatos);

        // Usar panel
        JPanel panelFormato = new JPanel();
        panelFormato.add(new JLabel("Formato:"));
        panelFormato.add(formatoCombo);
        fileChooser.setAccessory(panelFormato);

        int seleccion = fileChooser.showSaveDialog(null);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            String formatoElegido = (String) formatoCombo.getSelectedItem();

            String ruta = archivo.getAbsolutePath();
            if (!ruta.endsWith("." + formatoElegido)) {
                ruta += "." + formatoElegido;
            }

            guardarDatos(ruta, formatoElegido);
        }
    }
    
    protected void guardarDatos(String ruta, String formato) {
        try (
            Connection conn = GestionBasedeDatos.prueba();
        ) {
            if (formato.equalsIgnoreCase("txt")) {
                try (
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM especialista");
                    ResultSet rs = stmt.executeQuery();
                    BufferedWriter bw = new BufferedWriter(new FileWriter(ruta));
                ) {
                    ResultSetMetaData meta = rs.getMetaData();
                    int columnas = meta.getColumnCount();

                    // Escribir encabezados
                    for (int i = 1; i <= columnas; i++) {
                        bw.write(meta.getColumnName(i));
                        if (i < columnas) bw.write(",");
                    }
                    bw.newLine();

                    // Escribir datos fila por fila
                    while (rs.next()) {
                        for (int i = 1; i <= columnas; i++) {
                            String valor = rs.getString(i);
                            if (valor != null) {
                                valor = valor.replace("\"", "\"\"");
                                if (valor.contains(",") || valor.contains("\n")) {
                                    valor = "\"" + valor + "\"";
                                }
                            }
                            bw.write(valor != null ? valor : "");
                            if (i < columnas) bw.write(",");
                        }
                        bw.newLine();
                    }
                }

            } else if (formato.equalsIgnoreCase("js")) {
                String[] tipos = {"fisio", "dentista", "podologo"};
                String[] etiquetas = {"Fisio", "Dentista", "Podologo"};
                StringBuilder json = new StringBuilder();
                json.append("const coleccion = [\n");

                boolean hayDatos = false; // para saber si escribimos algún objeto

                for (int j = 0; j < tipos.length; j++) {
                    String tipo = tipos[j];
                    String etiqueta = etiquetas[j];

                    String sql = "SELECT e.* FROM especialista e, " + tipo + " t WHERE e.dni = t.dni_esp";

                    try (
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        ResultSet rs = stmt.executeQuery();
                    ) {
                        ResultSetMetaData meta = rs.getMetaData();
                        int columnas = meta.getColumnCount();

                        while (rs.next()) {
                            hayDatos = true;
                            json.append("  {\n");
                            for (int i = 1; i <= columnas; i++) {
                                String nombreColumna = meta.getColumnName(i);
                                String valor = rs.getString(i);
                                json.append("    \"").append(nombreColumna).append("\": \"")
                                    .append(valor != null ? valor : "").append("\",\n");
                            }
                            // Añadir el campo tipo_especialista
                            json.append("    \"tipo_especialista\": \"").append(etiqueta).append("\"\n");
                            json.append("  },\n");
                        }
                    }
                }

                if (hayDatos) {
                    // Quitar última coma y salto de línea
                    json.setLength(json.length() - 2);
                    json.append("\n");
                }
                json.append("];");

                // Guardar archivo .js
                String rutaJs = ruta.replaceAll("\\.txt$", ".js");
                try (BufferedWriter jsonWriter = new BufferedWriter(new FileWriter(rutaJs))) {
                    jsonWriter.write(json.toString());
                }

            } else {
                JOptionPane.showMessageDialog(null, "Formato no válido. Usa 'txt' o 'js'.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(null, "Datos exportados correctamente en formato " + formato.toUpperCase(), "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

		
		
	
    protected void cargarDatos(String ruta) {
        String linea;

        try (
            BufferedReader br = new BufferedReader(new FileReader(ruta));
            Connection conn = GestionBasedeDatos.prueba();
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO cliente (dni, nombre, apellidos, fecha_nacimiento) VALUES (?, ?, ?, ?)")
        ) {
            // Leer encabezado (lo ignoramos)
            br.readLine();

            while ((linea = br.readLine()) != null) {
                String[] datos = estiloTxt(linea);
                if (datos.length == 4) {
                    stmt.setString(1, datos[0]);
                    stmt.setString(2, datos[1]);
                    stmt.setString(3, datos[2]);
                    stmt.setDate(4, Date.valueOf(datos[3])); // Formato yyyy-MM-dd
                    stmt.addBatch(); // Agregar a lote
                }
            }

            stmt.executeBatch(); // Ejecutar todos los INSERT juntos

            JOptionPane.showMessageDialog(this, "Datos insertados correctamente en la base de datos", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar datos en base de datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String[] estiloTxt(String linea) {
        ArrayList<String> campos = new ArrayList<>(); // Lista para almacenar los campos extraídos
        StringBuilder campo = new StringBuilder(); // Para construir cada campo carácter por carácter
        boolean enComillas = false; // Indica si estamos dentro de comillas dobles

        // Recorre cada carácter de la línea
        for (char c : linea.toCharArray()) {
            if (c == '\"') {
                // Si encontramos una comilla, alternamos el estado de enComillas
                enComillas = !enComillas;
            } else if (c == ',' && !enComillas) {
                // Si encontramos una coma fuera de comillas, significa que el campo ha terminado
                campos.add(campo.toString()); // Agrega el campo actual a la lista
                campo.setLength(0); // Limpia el StringBuilder para el siguiente campo
            } else {
                // Si es cualquier otro carácter, lo agregamos al campo actual
                campo.append(c);
            }
        }

        // Añadimos el último campo que queda después del bucle
        campos.add(campo.toString());

        // Convertimos la lista de campos a un array y lo devolvemos
        return campos.toArray(new String[0]);
    }

		
		
	
}
package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import Controlador.GestionBasedeDatos;
import Controlador.Navegador;

public class Anadir extends JFrame {
    private JPanel contentPane;

    // Campos de texto reutilizables
    private JTextField tf1, tf2, tf3, tf4;
    private JComboBox<String> comboEspecialista;

    // Mapa para guardar nombre del especialista -> dni
    private Map<String, String> especialistaDniMap = new HashMap<>();

    // Navegador (para volver atrás)
    private final Navegador nav = new Navegador();
    private JTextField tfFecha;
    private JTextField tfHora;

    public Anadir(String titulo) {
        setTitle("anadir" + titulo);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds(100, 100, 500, 400);

        // Acción al cerrar la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                setVisible(false);
                nav.dispatcher("principal", true);
            }
        });

        // Panel principal
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPane.setBackground(new Color(240, 248, 255));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        // Título del formulario
        JLabel lblTitulo = new JLabel("Añadir " + titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblTitulo, BorderLayout.NORTH);

        // Panel donde irán los campos
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(new Color(240, 248, 255));
        contentPane.add(panelFormulario, BorderLayout.CENTER);

        // Botones
        JButton btnCrear = new JButton("Crear");
        JButton btnVolver = new JButton("Volver");
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        panelBotones.setBackground(new Color(240, 248, 255));
        panelBotones.add(btnVolver);
        panelBotones.add(btnCrear);
        contentPane.add(panelBotones, BorderLayout.SOUTH);

        // Llamada para construir el formulario correspondiente
        construirFormulario(titulo, panelFormulario);

        // Acciones de los botones
        btnCrear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				crearRegistro(titulo);
				}
			});
        Estilo.estilizarBoton(btnCrear);

			
        btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { {
            setVisible(false);
            nav.dispatcher("principal", true);
			}
			}
        });
        Estilo.estilizarBoton(btnVolver);

    }

    // Método para construir los campos según el tipo de entidad
    private void construirFormulario(String titulo, JPanel panel) {
        switch (titulo.toLowerCase()) {
            case "cliente":
                tf1 = crearCampo(panel, "DNI");
                tf2 = crearCampo(panel, "Nombre");
                tf3 = crearCampo(panel, "Apellidos");
                tf4 = crearCampo(panel, "Fecha Nacimiento (YYYY-MM-DD)");
                break;

            case "especialista":
                tf1 = crearCampo(panel, "DNI");
                tf2 = crearCampo(panel, "Nombre");
                tf3 = crearCampo(panel, "Apellidos");
                tf4 = crearCampo(panel, "Sueldo");
                JTextField tfConsulta = crearCampo(panel, "Nombre de la Consulta");
                this.tfHora = tfConsulta; // lo reutilizamos para no crear nueva variable
                break;

            case "solicitar":
            	tf1 = crearCampo(panel, "DNI Cliente");
        	    JTextField tfFecha = crearCampo(panel, "Fecha (YYYY-MM-DD)");
        	    JTextField tfHora = crearCampo(panel, "Hora (HH:MM)");
        	    comboEspecialista = new JComboBox<>();
        	    cargarEspecialistas();

        	    // Guardamos referencias para usarlas luego en crearRegistro
        	    this.tfFecha = tfFecha;
        	    this.tfHora = tfHora;

        	    JPanel filaCombo = new JPanel(new BorderLayout(5, 5));
        	    filaCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        	    filaCombo.add(new JLabel("Especialista"), BorderLayout.WEST);
        	    filaCombo.add(comboEspecialista, BorderLayout.CENTER);
        	    panel.add(filaCombo);
        	    panel.add(Box.createVerticalStrut(10));
        	    break;
        	    
            case "usuario":
            	tf1 = crearCampo(panel,"Nombre");
            	tf2 = crearCampo(panel,"Contraseña");
            	tf3 = crearCampo(panel,"Permisos");
            	break;
        }
    }

    // Método auxiliar para crear campos de texto
    private JTextField crearCampo(JPanel panel, String etiqueta) {
        JPanel fila = new JPanel(new BorderLayout(5, 5));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        fila.setBackground(new Color(240, 248, 255));

        JLabel lb = new JLabel(etiqueta);
        lb.setPreferredSize(new Dimension(180, 25));
        JTextField txt = new JTextField();
        fila.add(lb, BorderLayout.WEST);
        fila.add(txt, BorderLayout.CENTER);
        panel.add(fila);
        panel.add(Box.createVerticalStrut(10));
        return txt;
    }

    // Carga especialistas y sus consultas al comboBox
    private void cargarEspecialistas() {
        try (Connection conn = GestionBasedeDatos.prueba();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT dni, nombre, num_con FROM especialista")) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String dni = rs.getString("dni");
                int numCon = rs.getInt("num_con");
                String item = nombre + " - Consulta " + numCon;
                comboEspecialista.addItem(item);
                especialistaDniMap.put(item, dni); // guardar clave completa
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar especialistas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Inserta el registro correspondiente
    private void crearRegistro(String titulo) {
        try (Connection conexion = GestionBasedeDatos.prueba()) {
            switch (titulo.toLowerCase()) {
                case "cliente":
                    PreparedStatement psCli = conexion.prepareStatement("INSERT INTO cliente (dni, nombre, apellidos, fecha_nacimiento) VALUES (?, ?, ?, ?)");
                    psCli.setString(1, tf1.getText());
                    psCli.setString(2, tf2.getText());
                    psCli.setString(3, tf3.getText());
                    psCli.setDate(4, Date.valueOf(tf4.getText()));
                    psCli.executeUpdate();
                    psCli.close();
                    break;

                case "especialista":
                    // Primero crear consulta vacía
                	String nombreConsulta = tfHora.getText(); // Campo de nombre de la consulta
                	PreparedStatement ps = conexion.prepareStatement("SELECT IncrementoId(?)::text");
                	ps.setString(1, nombreConsulta);
                	ResultSet rs = ps.executeQuery();
                	int numero = 0;
                	if (rs.next()) {
                	    numero = rs.getInt(1); // num_con que insertó la función
                	}
                	rs.close();
                	ps.close();
                	
                	
                	

                    // Luego insertar el especialista
                    PreparedStatement psEsp = conexion.prepareStatement("INSERT INTO especialista (dni, nombre, apellidos, sueldo, num_con) VALUES (?, ?, ?, ?, ?)");
                    psEsp.setString(1, tf1.getText());
                    psEsp.setString(2, tf2.getText());
                    psEsp.setString(3, tf3.getText());
                    psEsp.setDouble(4, Double.parseDouble(tf4.getText()));
                    psEsp.setInt(5, numero);
                    psEsp.executeUpdate();
                    psEsp.close();
                    break;

                case "solicitar":
                	 String dniCliente = tf1.getText();
                	    String fechaStr = tfFecha.getText();  // YYYY-MM-DD
                	    String horaStr = tfHora.getText();    // HH:MM

                	    // Combinamos fecha y hora en formato "YYYY-MM-DD HH:MM:00"
                	    String fechaHoraCompleta = fechaStr + " " + horaStr + ":00";
                	    java.sql.Timestamp fechaHoraSQL = java.sql.Timestamp.valueOf(fechaHoraCompleta);

                	    String seleccionado = (String) comboEspecialista.getSelectedItem();
                	    if (seleccionado == null || !seleccionado.contains(" - Consulta ")) {
                	        throw new Exception("Especialista no seleccionado correctamente.");
                	    }

                	    String[] partes = seleccionado.split(" - Consulta ");
                	    String nombreEsp = partes[0].trim();
                	    int numConsulta2 = Integer.parseInt(partes[1].trim());

                	    // Insertar cita y resto igual que antes
                	    PreparedStatement psInsertCita = conexion.prepareStatement(
                	        "INSERT INTO cita (fecha) VALUES (?)", 
                	        Statement.RETURN_GENERATED_KEYS
                	    );
                	    psInsertCita.setTimestamp(1, fechaHoraSQL);
                	    psInsertCita.executeUpdate();

                	    ResultSet rsCita = psInsertCita.getGeneratedKeys();
                	    int idCita = 0;
                	    if (rsCita.next()) {
                	        idCita = rsCita.getInt(1);
                	    }
                	    rsCita.close();
                	    psInsertCita.close();

                	    PreparedStatement psSelectDniEsp = conexion.prepareStatement(
                	        "SELECT dni FROM especialista WHERE nombre = ? AND num_con = ?"
                	    );
                	    psSelectDniEsp.setString(1, nombreEsp);
                	    psSelectDniEsp.setInt(2, numConsulta2);

                	    ResultSet rsEsp = psSelectDniEsp.executeQuery();
                	    String dniEsp = null;
                	    if (rsEsp.next()) {
                	        dniEsp = rsEsp.getString("dni");
                	    } else {
                	        throw new Exception("No se encontró el especialista con ese nombre y consulta.");
                	    }
                	    rsEsp.close();
                	    psSelectDniEsp.close();

                	    PreparedStatement psInsertSolicitar = conexion.prepareStatement(
                	        "INSERT INTO solicitar (id_cita, dni_cliente, dni_esp) VALUES (?, ?, ?)"
                	    );
                	    psInsertSolicitar.setInt(1, idCita);
                	    psInsertSolicitar.setString(2, dniCliente);
                	    psInsertSolicitar.setString(3, dniEsp);
                	    psInsertSolicitar.executeUpdate();
                	    psInsertSolicitar.close();
                	    break;
                	    
                case "usuario":
                    PreparedStatement psUse = conexion.prepareStatement("INSERT INTO users (nombre,passwd,permisos) VALUES (?, ?, ?)");
                    psUse.setString(1, tf1.getText());
                    psUse.setString(2, tf2.getText());
                    psUse.setInt(3, Integer.parseInt(tf3.getText()));
                    psUse.executeUpdate();
                    psUse.close();
                    break;
            }

            JOptionPane.showMessageDialog(this, "Registro creado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al crear el registro.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

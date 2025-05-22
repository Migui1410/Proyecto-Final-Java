package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import Controlador.GestionBasedeDatos;
import Controlador.Navegador;
import Controlador.Validacion;

public class Anadir extends JFrame {
    private JPanel contentPane;

    private JTextField tf1, tf2, tf3, tf4;
    private JComboBox<String> comboEspecialista;
    private Map<String, String> especialistaDniMap = new HashMap<>();
    private final Navegador nav = new Navegador();
    private JTextField tfFecha;
    private JTextField tfHora;
    private Validacion vl = new Validacion();
    private int permisos;
    private String user;

    private String tituloActual;   // Guardamos el título para reconstruir el formulario al actualizar

    public Anadir(String titulo, String user, int p) {
        permisos = p;
        this.user = user;
        this.tituloActual = titulo.toLowerCase();

        setTitle("anadir" + titulo);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds(100, 100, 500, 400);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                setVisible(false);
                nav.dispatcher("principal", true);
            }
        });

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPane.setBackground(new Color(240, 248, 255));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        JLabel lblTitulo = new JLabel("Añadir " + titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(new Color(240, 248, 255));
        contentPane.add(panelFormulario, BorderLayout.CENTER);

        JButton btnCrear = new JButton("Crear");
        JButton btnVolver = new JButton("Volver");
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        panelBotones.setBackground(new Color(240, 248, 255));
        panelBotones.add(btnVolver);
        panelBotones.add(btnCrear);
        contentPane.add(panelBotones, BorderLayout.SOUTH);

        construirFormulario(titulo, panelFormulario);

        btnCrear.addActionListener(e -> {
            crearRegistro(titulo);
            limpiarCampos();
        });
        Estilo.estilizarBoton(btnCrear);

        btnVolver.addActionListener(e -> {
            setVisible(false);
            limpiarCampos();
            nav.dispatcher("principal", true);
        });
        Estilo.estilizarBoton(btnVolver);
    }

    private void construirFormulario(String titulo, JPanel panel) {
        panel.removeAll();  // Limpia el panel antes de construir

        switch (titulo.toLowerCase()) {
            case "cliente":
                tf1 = crearCampo(panel, "DNI");
                vl.limitarASoloNumeros(tf1, 8);

                tf2 = crearCampo(panel, "Nombre");
                vl.soloLetras(tf2);

                tf3 = crearCampo(panel, "Apellidos");
                vl.soloLetras(tf3);

                tf4 = crearCampo(panel, "Fecha Nacimiento (YYYY-MM-DD)");
                vl.limitarAFecha(tf4);
                break;

            case "especialista":
                tf1 = crearCampo(panel, "DNI");
                vl.limitarASoloNumeros(tf1, 8);

                tf2 = crearCampo(panel, "Nombre");
                vl.soloLetras(tf2);

                tf3 = crearCampo(panel, "Apellidos");
                vl.soloLetras(tf3);

                tf4 = crearCampo(panel, "Sueldo");
                vl.limitarASoloNumeros(tf4, 6);

                JTextField tfConsulta = crearCampo(panel, "Nombre de la Consulta");
                this.tfHora = tfConsulta;
                break;

            case "solicitar":
                tf1 = crearCampo(panel, "DNI Cliente");
                vl.limitarASoloNumeros(tf1, 8);

                tfFecha = crearCampo(panel, "Fecha (YYYY-MM-DD)");
                vl.limitarAFecha(tfFecha);

                tfHora = crearCampo(panel, "Hora (HH:MM)");
                vl.limitarAHora(tfHora);

                if (permisos == 1) {
                    JPanel filaEspecialistaFijo = new JPanel(new BorderLayout(5, 5));
                    filaEspecialistaFijo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                    JLabel lblEspecialistaFijo = new JLabel("Especialista:");
                    JTextField tfEspecialistaFijo = new JTextField(user);
                    tfEspecialistaFijo.setEditable(false);
                    filaEspecialistaFijo.add(lblEspecialistaFijo, BorderLayout.WEST);
                    filaEspecialistaFijo.add(tfEspecialistaFijo, BorderLayout.CENTER);
                    filaEspecialistaFijo.setBackground(new Color(240, 248, 255));
                    panel.add(filaEspecialistaFijo);
                    panel.add(Box.createVerticalStrut(10));
                } else {
                    comboEspecialista = new JComboBox<>();
                    cargarEspecialistas();

                    JPanel filaCombo = new JPanel(new BorderLayout(5, 5));
                    filaCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                    JLabel lblEspecialista = new JLabel("Especialista");
                    filaCombo.add(lblEspecialista, BorderLayout.WEST);
                    filaCombo.add(comboEspecialista, BorderLayout.CENTER);
                    filaCombo.setBackground(new Color(240, 248, 255));
                    panel.add(filaCombo);
                    panel.add(Box.createVerticalStrut(10));
                }
                break;

            case "usuario":
                tf1 = crearCampo(panel, "Nombre");
                tf2 = crearCampo(panel, "Contraseña");
                tf3 = crearCampo(panel, "Permisos");
                vl.limitarASoloNumeros(tf3, 1);
                break;
        }

        panel.revalidate();
        panel.repaint();
    }

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

    private void cargarEspecialistas() {
        if (comboEspecialista == null) return;

        comboEspecialista.removeAllItems();
        especialistaDniMap.clear();

        try (Connection conn = GestionBasedeDatos.prueba();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT dni, nombre, num_con FROM especialista")) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String dni = rs.getString("dni");
                int numCon = rs.getInt("num_con");
                String item = nombre + " - Consulta " + numCon;
                comboEspecialista.addItem(item);
                especialistaDniMap.put(item, dni);
            }

            comboEspecialista.setSelectedIndex(-1);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar especialistas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

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
                    String nombreConsulta = tfHora.getText();
                    if (nombreConsulta.length() > 20) {
                        nombreConsulta = nombreConsulta.substring(0, 20);
                    }
                    PreparedStatement ps = conexion.prepareStatement("SELECT IncrementoId(?)");
                    ps.setString(1, nombreConsulta);
                    ResultSet rs = ps.executeQuery();
                    int numero = 0;
                    if (rs.next()) {
                        numero = rs.getInt(1);
                    }
                    rs.close();
                    ps.close();

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
                    String fechaStr = tfFecha.getText();
                    String horaStr = tfHora.getText();
                    String fechaHoraCompleta = fechaStr + " " + horaStr + ":00";
                    java.sql.Timestamp fechaHoraSQL = java.sql.Timestamp.valueOf(fechaHoraCompleta);

                    if (permisos == 1) {
                        String nombreEsp = user.toLowerCase();

                        PreparedStatement psEspDatos = conexion.prepareStatement("SELECT dni, num_con FROM especialista WHERE lower(nombre) = ?");
                        psEspDatos.setString(1, nombreEsp);
                        ResultSet rsEspDatos = psEspDatos.executeQuery();
                        String dniEsp = null;
                        int numConsulta2 = 0;
                        if (rsEspDatos.next()) {
                            dniEsp = rsEspDatos.getString("dni");
                            numConsulta2 = rsEspDatos.getInt("num_con");
                        } else {
                            throw new Exception("No se encontró el especialista con ese nombre.");
                        }
                        rsEspDatos.close();
                        psEspDatos.close();

                        PreparedStatement psInsertCita = conexion.prepareStatement("SELECT InsertarCita(?)");
                        psInsertCita.setTimestamp(1, fechaHoraSQL);
                        ResultSet rsCita = psInsertCita.executeQuery();

                        int idCita = 0;
                        if (rsCita.next()) {
                            idCita = rsCita.getInt(1);
                        } else {
                            throw new Exception("No se pudo obtener el ID de la cita.");
                        }
                        rsCita.close();
                        psInsertCita.close();

                        PreparedStatement psInsertSolicitar = conexion.prepareStatement("INSERT INTO solicitar (id_cita, dni_cliente, dni_esp) VALUES (?, ?, ?)");
                        psInsertSolicitar.setInt(1, idCita);
                        psInsertSolicitar.setString(2, dniCliente);
                        psInsertSolicitar.setString(3, dniEsp);
                        psInsertSolicitar.executeUpdate();
                        psInsertSolicitar.close();

                    } else {
                        String seleccionado = (String) comboEspecialista.getSelectedItem();
                        if (seleccionado == null || !seleccionado.contains(" - Consulta ")) {
                            throw new Exception("Especialista no seleccionado correctamente.");
                        }

                        String[] partes = seleccionado.split(" - Consulta ");
                        String nombreEsp = partes[0].trim();
                        int numConsulta2 = Integer.parseInt(partes[1].trim());

                        PreparedStatement psInsertCita = conexion.prepareStatement("SELECT InsertarCita(?)");
                        psInsertCita.setTimestamp(1, fechaHoraSQL);
                        ResultSet rsCita = psInsertCita.executeQuery();

                        int idCita = 0;
                        if (rsCita.next()) {
                            idCita = rsCita.getInt(1);
                        } else {
                            throw new Exception("No se pudo obtener el ID de la cita.");
                        }
                        rsCita.close();
                        psInsertCita.close();

                        PreparedStatement psSelectDniEsp = conexion.prepareStatement("SELECT dni FROM especialista WHERE nombre = ? AND num_con = ?");
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

                        PreparedStatement psInsertSolicitar = conexion.prepareStatement("INSERT INTO solicitar (id_cita, dni_cliente, dni_esp) VALUES (?, ?, ?)");
                        psInsertSolicitar.setInt(1, idCita);
                        psInsertSolicitar.setString(2, dniCliente);
                        psInsertSolicitar.setString(3, dniEsp);
                        psInsertSolicitar.executeUpdate();
                        psInsertSolicitar.close();
                    }
                    break;

                case "usuario":
                    PreparedStatement psUse = conexion.prepareStatement("INSERT INTO users (nombre, passwd, permisos) VALUES (?, ?, ?)");
                    psUse.setString(1, tf1.getText());
                    psUse.setString(2, tf2.getText());
                    psUse.setInt(3, Integer.parseInt(tf3.getText()));
                    psUse.executeUpdate();
                    psUse.close();
                    break;
            }

            JOptionPane.showMessageDialog(this, titulo + " creado con éxito.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        if (tf1 != null) tf1.setText("");
        if (tf2 != null) tf2.setText("");
        if (tf3 != null) tf3.setText("");
        if (tf4 != null) tf4.setText("");
        if (tfFecha != null) tfFecha.setText("");
        if (tfHora != null && !tituloActual.equalsIgnoreCase("especialista")) tfHora.setText("");
        if (comboEspecialista != null) comboEspecialista.setSelectedIndex(-1);
    }

    
    public void actualizarPermisosYUsuario(int permisos, String user) {
        this.permisos = permisos;
        this.user = user;

        JPanel panelCentral = (JPanel) getContentPane().getComponent(1);  // Asumimos que el panel central es el índice 1

        construirFormulario(tituloActual, panelCentral);
    }
}

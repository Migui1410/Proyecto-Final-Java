package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JCalendar;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import Controlador.GestionBasedeDatos;
import Controlador.Navegador;


public class Anadir extends JFrame {

    private String tituloV;
    private JPanel contentPane;
    private Navegador nav = new Navegador();
    private ArrayList<JTextField> camposTexto = new ArrayList<>();
    private Navegador nv = new Navegador();
    private Map<String, String> claveEsp = new HashMap<>();
    private JComboBox<String> comboEspecialistas;


    public Anadir(String titulo) {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                setVisible(false);
                nav.dispatcher("principal", true);
            }
        });

        tituloV = titulo;
        setTitle("anadir" + titulo);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds(100, 100, 500, 400);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPane.setBackground(new Color(240, 248, 255));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        JLabel lblTitulo = new JLabel((tituloV.equals("solicitar")) ? "Añadir Cita" : "Añadir "+ tituloV );
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(33, 37, 41));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblTitulo, BorderLayout.NORTH);

        // Panel formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(new Color(240, 248, 255));
        contentPane.add(panelFormulario, BorderLayout.CENTER);

        JButton btnCrear = new JButton("Crear");
        JButton btnVolver = new JButton("Volver");
        btnCrear.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        panelBotones.setBackground(new Color(240, 248, 255));
        panelBotones.add(btnVolver);
        panelBotones.add(btnCrear);
        contentPane.add(panelBotones, BorderLayout.SOUTH);

        try {
            // Obtener clase de entidad dinámicamente 
            Class<?> claseEntidad = getClaseEntidad(tituloV);
            if (claseEntidad != null) {
                Field[] campos = claseEntidad.getDeclaredFields();
                if (tituloV.equalsIgnoreCase("solicitar")) {
                    addCampo(panelFormulario, null); // null porque no usamos la etiqueta en este caso
                } else {
                    for (Field campo : campos) {
                        addCampo(panelFormulario, campo.getName());
                    }
                }
                

                btnCrear.addActionListener(e -> {
                    try {
                        Connection conexion = GestionBasedeDatos.prueba();
                        if (tituloV.equalsIgnoreCase("solicitar")) {
                        	System.out.println(tituloV);
                            boolean correcto = insertarCitaYSolicitud(conexion);
                            if (!correcto) return;
                        }else {
                        	// Crear el objeto de la clase correspondiente
                            Object entidad = claseEntidad.getConstructor().newInstance();
                            
                            for (int i = 0; i < campos.length; i++) {
                                String nombreCampo = campos[i].getName();
                                String valorCampo = camposTexto.get(i).getText();

                                Field campo = claseEntidad.getDeclaredField(nombreCampo);
                                campo.setAccessible(true);  //Poder modificar campos privados
                                if (campo.getType() == String.class) {
                                    campo.set(entidad, valorCampo);
                                } else if (campo.getType() == int.class) {
                                    campo.set(entidad, Integer.parseInt(valorCampo));
                                } else if (campo.getType() == double.class) {
                                    campo.set(entidad, Double.parseDouble(valorCampo));
                                } else if (campo.getType() == LocalDate.class) {
                                    campo.set(entidad, LocalDate.parse(valorCampo));
                                }
                            }

                            // Crear consulta de inserción
                            StringBuilder sql = new StringBuilder();
                            sql.append("INSERT INTO ").append(tituloV.toLowerCase()).append(" (");

                            StringBuilder valores = new StringBuilder();
                            for (int i = 0; i < campos.length; i++) {
                                sql.append(campos[i].getName());
                                valores.append("?");
                                if (i < campos.length - 1) {
                                    sql.append(", ");
                                    valores.append(", ");
                                }
                            }
                            sql.append(") VALUES (").append(valores).append(")");

                            // Asignar los valores
                            PreparedStatement ps = conexion.prepareStatement(sql.toString());
                            for (int i = 0; i < campos.length; i++) {
                                String valor = camposTexto.get(i).getText();
                                if (campos[i].getType() == String.class) {
                                    ps.setString(i + 1, valor);
                                } else if (campos[i].getType() == int.class) {
                                    ps.setInt(i + 1, Integer.parseInt(valor));
                                } else if (campos[i].getType() == double.class) {
                                    ps.setDouble(i + 1, Double.parseDouble(valor));
                                } else if (campos[i].getType() == LocalDate.class) {
                                    ps.setDate(i + 1, java.sql.Date.valueOf(valor));
                                }
                            }
                            ps.executeUpdate();// Ejecutar consulta inserción
                            ps.close();// Cerrar conexión
                        }
                        
                        
                        conexion.close();
             
                        JOptionPane.showMessageDialog(this, "Datos insertados correctamente.","Correcto",JOptionPane.INFORMATION_MESSAGE);
                        limpiarCampos();
                    } catch (Exception ex) {
                    	ex.printStackTrace();
                    	System.out.println(tituloV);
                        JOptionPane.showMessageDialog(this, "Error al insertar los datos.","Error",JOptionPane.ERROR_MESSAGE);
                    }
                });
            } else {
                System.out.println("Entidad desconocida");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnVolver.addActionListener(e -> {
            setVisible(false);
            nav.dispatcher("principal", true);
        });
    }

    private Class<?> getClaseEntidad(String titulo) {
        try {
            String nombreClase = "Modelo." + titulo.substring(0, 1).toUpperCase() + titulo.substring(1).toLowerCase();
            return Class.forName(nombreClase);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


	private void addCampo(JPanel panel, String etiqueta) {
	    JPanel fila = new JPanel();
	    fila.setLayout(new BorderLayout(5, 5));
	    fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
	    fila.setBackground(new Color(240, 248, 255));
	
	    if (tituloV.equalsIgnoreCase("solicitar")) {
	        String[] etiquetasCita = {"Fecha (AAAA-MM-DD)", "DNI Cliente", "DNI Especialista"};
	
	        for (String etiquetaCita : etiquetasCita) {
	            JPanel fila1 = new JPanel(new BorderLayout(5, 5));
	            fila1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
	            fila1.setBackground(new Color(240, 248, 255));
	
	            JLabel lb = new JLabel(etiquetaCita);
	            lb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	            lb.setPreferredSize(new Dimension(180, 25));
	
	            JTextField txt = new JTextField();
	            txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	
	            if (etiquetaCita.equals("DNI Especialista")) {
	                lb.setText("Especialista");
	
	                comboEspecialistas = new JComboBox<>();
	                comboEspecialistas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	
	                try {
	                    Connection conn = GestionBasedeDatos.prueba();
	                    Statement st = conn.createStatement();
	                    ResultSet rs = st.executeQuery("SELECT nombre, dni,num_con FROM especialista");
	
	                    while (rs.next()) {
	                    	String nombre = rs.getString("nombre");
	                    	String dni = rs.getString("dni");
	                    	String numeroConsulta = rs.getString("num_con");

	                    	String clave = nombre + " - Consulta " + numeroConsulta;
	                    	comboEspecialistas.addItem(clave);
	                    	claveEsp.put(clave, dni);

	                    }
	
	                    rs.close();
	                    st.close();
	                    conn.close();
	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                }
	
	                fila1.add(lb, BorderLayout.WEST);
	                fila1.add(comboEspecialistas, BorderLayout.CENTER);
	                panel.add(fila1);
	                panel.add(Box.createVerticalStrut(10));
	                continue; // Salta añadir a camposTexto
	            }
	
	            if (etiquetaCita.startsWith("Fecha")) {
	                txt.setToolTipText("Formato: AAAA-MM-DD");
	                fila1.add(lb, BorderLayout.WEST);
	                fila1.add(txt, BorderLayout.CENTER);
	                panel.add(fila1);
	                panel.add(Box.createVerticalStrut(10));
	                camposTexto.add(txt);
	
	                JPanel filaHora = new JPanel(new BorderLayout(5, 5));
	                filaHora.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
	                filaHora.setBackground(new Color(240, 248, 255));
	
	                JLabel lbHora = new JLabel("Hora (HH:MM)");
	                lbHora.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	                lbHora.setPreferredSize(new Dimension(180, 25));
	
	                JTextField txtHora = new JTextField();
	                txtHora.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	                txtHora.setToolTipText("Formato: HH:MM (ejemplo: 14:30)");
	
	                filaHora.add(lbHora, BorderLayout.WEST);
	                filaHora.add(txtHora, BorderLayout.CENTER);
	                panel.add(filaHora);
	                panel.add(Box.createVerticalStrut(10));
	                camposTexto.add(txtHora);
	                continue;
	            }
	
	            fila1.add(lb, BorderLayout.WEST);
	            fila1.add(txt, BorderLayout.CENTER);
	            panel.add(fila1);
	            panel.add(Box.createVerticalStrut(10));
	            camposTexto.add(txt);
	        }
	        return;
	    }
	
	    JLabel lb = new JLabel(etiqueta);
	    lb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	    lb.setPreferredSize(new Dimension(180, 25));
	
	    JTextField txt = new JTextField();
	    txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	
	    fila.add(lb, BorderLayout.WEST);
	    fila.add(txt, BorderLayout.CENTER);
	
	    panel.add(fila);
	    panel.add(Box.createVerticalStrut(10));
	
	    camposTexto.add(txt);
		}


        
    
    

    private void limpiarCampos() {
        for (JTextField campo : camposTexto) {
            campo.setText("");
        }
    }
    
    
    
    
    private boolean insertarCitaYSolicitud(Connection conexion) {
        try {
            conexion.setAutoCommit(false);

            // Validar y obtener fecha
            String fechaStr = camposTexto.get(0).getText().trim(); // Fecha: AAAA-MM-DD
            String horaStr = camposTexto.get(1).getText().trim();  // Hora: HH:MM

            if (fechaStr.isEmpty() || horaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fecha y hora son obligatorias", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            String fechaHoraStr = fechaStr + " " + horaStr + ":00";
            Timestamp fechaHora;
            try {
                fechaHora = Timestamp.valueOf(fechaHoraStr);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Formato incorrecto. Use AAAA-MM-DD y HH:MM", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Insertar en cita
            int idCitaGenerado;
            String sqlCita = "INSERT INTO cita (fecha) VALUES (?)";
            try (PreparedStatement psCita = conexion.prepareStatement(sqlCita, Statement.RETURN_GENERATED_KEYS)) {
                psCita.setTimestamp(1, fechaHora);
                psCita.executeUpdate();

                try (ResultSet rs = psCita.getGeneratedKeys()) {
                    if (rs.next()) {
                        idCitaGenerado = rs.getInt(1);
                    } else {
                        throw new SQLException("No se pudo obtener el ID generado de la cita.");
                    }
                }
            }

            String dniCliente = camposTexto.get(2).getText().trim(); 
            String nombreEspecialista = (String) comboEspecialistas.getSelectedItem();
            String dniEspecialista = claveEsp.get(nombreEspecialista);
           

            if (dniCliente.isEmpty() || dniEspecialista == null) {
                JOptionPane.showMessageDialog(this, "Debe completar todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                conexion.rollback();
                return false;
            }

            // Insertar en solicitar
            String sqlSolicitar = "INSERT INTO solicitar (id_cita, dni_cliente, dni_esp) VALUES (?, ?, ?)";
            try (PreparedStatement psSolicitar = conexion.prepareStatement(sqlSolicitar)) {
                psSolicitar.setInt(1, idCitaGenerado);
                psSolicitar.setString(2, dniCliente);
                psSolicitar.setString(3, dniEspecialista);
                psSolicitar.executeUpdate();
            }

            conexion.commit();
            JOptionPane.showMessageDialog(this, "Cita creada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (SQLException e) {
            try {
                conexion.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Error al guardar la cita: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                conexion.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

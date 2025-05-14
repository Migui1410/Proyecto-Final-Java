package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

import Controlador.GestionBasedeDatos;
import Controlador.Navegador;


public class Anadir extends JFrame {

    private String tituloV;
    private JPanel contentPane;
    private Navegador nav = new Navegador();
    private ArrayList<JTextField> camposTexto = new ArrayList<>();
    private Navegador nv = new Navegador();

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
        setBounds(100, 100, 500, 400);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPane.setBackground(new Color(240, 248, 255));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        JLabel lblTitulo = new JLabel("Añadir " + tituloV);
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
                for (Field campo : campos) {
                    addCampo(panelFormulario, campo.getName());
                }

                btnCrear.addActionListener(e -> {
                    try {
                        Connection conexion = GestionBasedeDatos.prueba();
                        if (tituloV.equalsIgnoreCase("cita")) {
                        	if(!nv.existe("anadircitanueva")) {
                    	    	nv.crearVentana(new AnadirCitaForm()); 
                    	        nv.dispatcher("anadircitanueva", true);
                    	    }else {
                    	        nv.dispatcher("anadircitanueva", true);

                    	    }
                        } else {
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
	                        conexion.close();
                        }

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
    
    
    
}

package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import Controlador.GestionBasedeDatos;
import Controlador.Validacion;

public class Editar extends JFrame {

    public Editar(String tipo, Object[] datos, String claveOriginal, DefaultTableModel model, Runnable onGuardarExterno) {
        setTitle("Editar " + tipo);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JTextField[] campos = new JTextField[datos.length];

        for (int i = 0; i < datos.length; i++) {
            JPanel fila = new JPanel(new FlowLayout());
            JLabel label = new JLabel(model.getColumnName(i) + ": ");
            JTextField campo = new JTextField(20);
            campo.setText(datos[i] != null ? datos[i].toString() : "");

            String nombreColumna = model.getColumnName(i).toLowerCase();
            if (nombreColumna.contains("nombre") || nombreColumna.contains("apellidos")) {
                Validacion.soloLetras(campo);
            } else if (nombreColumna.contains("dni")) {
                Validacion.limitarASoloNumeros(campo, 8);
            } else if (nombreColumna.contains("fecha")) {
                Validacion.limitarAFecha(campo);
            } else if (nombreColumna.contains("id") || nombreColumna.contains("num")
                    || nombreColumna.contains("planta") || nombreColumna.contains("sueldo")
                    || nombreColumna.contains("precio")) {
                Validacion.limitarASoloNumeros(campo, 10);
            }

            campos[i] = campo;
            fila.add(label);
            fila.add(campo);
            panel.add(fila);
        }

        add(panel, BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Guardar cambios");
        Estilo.estilizarBoton(btnGuardar);
        btnGuardar.addActionListener((ActionEvent e) -> {
            actualizarRegistroEnBD(tipo, campos, claveOriginal);
            onGuardarExterno.run(); // actualiza la tabla en Mostrar
            dispose();
        });

        add(btnGuardar, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void actualizarRegistroEnBD(String tipo, JTextField[] campos, String claveOriginal) {
        String tabla = tipo.toLowerCase();
        String sql = "";
        switch (tabla) {
            case "cliente" :
            	sql = "UPDATE cliente SET dni = ?, nombre = ?, apellidos = ?, fecha_nacimiento = ? WHERE dni = ?";
            	break;
            case "especialista" :
            	sql = "UPDATE especialista SET dni = ?, nombre = ?, apellidos = ?, sueldo = ?, num_con = ? WHERE dni = ?";
            	break;
            case "cita" :
            	sql = "UPDATE cita SET id = ?, fecha = ? WHERE id = ?";
            	break;
            case "solicitar" :
            	sql = "UPDATE solicitar SET id_cita = ?, dni_cliente = ?, dni_esp = ? WHERE id_cita = ?";
            	break;
            case "usuario" : 
            	sql = "UPDATE users SET nombre= ?, passwd = ?, permisos = ? WHERE nombre = ?";
            	break;
            case "consulta" :
            	sql = "UPDATE consulta SET id = ?, nombre = ?, planta = ? WHERE id = ?";
            	break;
            default :
            	sql = null;
            	break;
        };

        if (sql == null) {
            JOptionPane.showMessageDialog(this, "Edición no soportada para: " + tipo);
            return;
        }

        try (Connection cn = GestionBasedeDatos.prueba();
             PreparedStatement st = cn.prepareStatement(sql)) {

            switch (tabla) {
                case "cliente": {
                    st.setString(1, campos[0].getText());
                    st.setString(2, campos[1].getText());
                    st.setString(3, campos[2].getText());
                    st.setDate(4, Date.valueOf(campos[3].getText()));
                    st.setString(5, claveOriginal);
                    break;
                }
                case "especialista" :{
                    st.setString(1, campos[0].getText());
                    st.setString(2, campos[1].getText());
                    st.setString(3, campos[2].getText());
                    st.setBigDecimal(4, new java.math.BigDecimal(campos[3].getText()));
                    st.setInt(5, Integer.parseInt(campos[4].getText()));
                    st.setString(6, claveOriginal);
                    break;
                }
                case "cita" : {
                    st.setInt(1, Integer.parseInt(campos[0].getText()));
                    st.setDate(2, Date.valueOf(campos[1].getText()));
                    st.setInt(3, Integer.parseInt(claveOriginal));
                    break;
                }
                case "solicitar" : {
                    st.setInt(1, Integer.parseInt(campos[0].getText()));
                    st.setString(2, campos[1].getText());
                    st.setString(3, campos[2].getText());
                    st.setInt(4, Integer.parseInt(claveOriginal));
                    break;
                }
                case "usuario" : {
                    st.setString(1, campos[0].getText());
                    st.setString(2, campos[1].getText());
                    st.setInt(3, Integer.parseInt(campos[2].getText()));
                    st.setString(4, claveOriginal);
                    break;
                }
                case "consulta" : {
                    st.setInt(1, Integer.parseInt(campos[0].getText()));
                    st.setString(2, campos[1].getText());
                    st.setInt(3, Integer.parseInt(campos[2].getText()));
                    st.setInt(4, Integer.parseInt(claveOriginal));
                    break;
                }
            }

            st.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registro actualizado correctamente.");

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                JOptionPane.showMessageDialog(this, "Ya existe un registro con ese valor.");
            } else if ("23503".equals(e.getSQLState())) {
                JOptionPane.showMessageDialog(this, "Violación de restricción de clave foránea.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
            }
        } catch (IllegalArgumentException ie) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use yyyy-mm-dd.");
        }
    }
}

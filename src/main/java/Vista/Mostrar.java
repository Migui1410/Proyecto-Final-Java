package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Controlador.GestionBasedeDatos;
import Controlador.Navegador;
import Modelo.Usuario;
import Modelo.Admin;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Mostrar extends JFrame {
    private static String tituloV;
    private JTable tableMostrar;
    private static DefaultTableModel model;
    private JPanel contentPane;
    private Navegador nav = new Navegador();
    private String user;
    private int permisos;

    public Mostrar(String tipo, String nombre) {
        user = nombre;
        tituloV = tipo;

        setTitle("mostrar" + tipo);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 450);
        setLocationRelativeTo(null);

        // Panel principal
        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        Estilo.aplicarEstiloBasico(contentPane);
        setContentPane(contentPane);

        // Etiqueta de título
        JLabel lblTitulo = new JLabel("Mostrar " + tipo, JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        Estilo.estilizarEtiqueta(lblTitulo, true);
        contentPane.add(lblTitulo, BorderLayout.NORTH);

        // Crear modelo de tabla y tabla
        configurarModeloTabla(tipo, nombre,permisos);
        tableMostrar = new JTable(model);
        Estilo.estilizarTabla(tableMostrar);
        tableMostrar.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(tableMostrar);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Panel Botones
        JPanel panelBotones = new JPanel();
        Estilo.aplicarEstiloBasico(panelBotones);

        // Botón Editar
        JButton btnEditar = new JButton("Editar");
        Estilo.estilizarBoton(btnEditar); // Aplicar estilo aquí, justo tras crear
        btnEditar.addActionListener(e -> {
            int filaSeleccionada = tableMostrar.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una fila para editar.");
                return;
            }

            // Extraer los datos de la fila seleccionada
            int columnas = tableMostrar.getColumnCount();
            Object[] datosFila = new Object[columnas];
            for (int i = 0; i < columnas; i++) {
                datosFila[i] = tableMostrar.getValueAt(filaSeleccionada, i);
            }

            // Abrir ventana de edición con los datos
            abrirVentanaEditar(tituloV, datosFila);
        });
        panelBotones.add(btnEditar);

        // Botón Eliminar
        JButton btnEliminar = new JButton("Eliminar");
        Estilo.estilizarBoton(btnEliminar); // Aplicar estilo aquí
        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tableMostrar.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona una fila para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar este registro?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            Object id = tableMostrar.getValueAt(filaSeleccionada, 0);
            String tabla = tituloV.toLowerCase();
            String sql = null;

            switch (tabla) {
                case "cliente":
                    sql = "DELETE FROM cliente WHERE dni = ?";
                    break;
                case "especialista":
                    sql = "DELETE FROM especialista WHERE dni = ?";
                    break;
                case "solicitar":
                    sql = "DELETE FROM solicitar WHERE id_cita = ?";
                    break;
                case "cita":
                    sql = "DELETE FROM cita WHERE id = ?";
                    break;
                case "consulta":
                    sql = "DELETE FROM consulta where numero = ?";
                    break;
                case "receta":
                    sql = "DELETE FROM receta where id = ?";
                    break;
                case "usuario":
                	sql = "DELETE FROM users where nombre = ?";
                	break;
                default:
                    JOptionPane.showMessageDialog(this, "Eliminación no soportada para este tipo.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }

            try (Connection cn = GestionBasedeDatos.prueba();
                 PreparedStatement st = cn.prepareStatement(sql)) {

                st.setObject(1, id);
                st.executeUpdate();
                JOptionPane.showMessageDialog(this, "Registro eliminado correctamente.");
                actualizarTabla();

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al eliminar el registro.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panelBotones.add(btnEliminar);

        contentPane.add(panelBotones, BorderLayout.SOUTH);
    }

    // método se llama automáticamente cuando haces setVisible(true)
    @Override
    public void setVisible(boolean b) {
        if (b) {
            actualizarTabla(); // actualiza la tabla siempre que se muestra
        }
        super.setVisible(b); // muestra la ventana normalmente
    }

    // Devuelve el SQL según qué tipo de tabla mostrar
    private String configurarModeloTabla(String item, String user,int permisos) {
        switch (item.toLowerCase()) {
            case "cliente":
                    return "SELECT * FROM cliente";
            case "especialista":
                return "SELECT * FROM especialista";
            case "solicitar":
                return "SELECT ci.id AS \"ID CITA\", cl.nombre AS \"Nombre Cliente\", es.nombre AS \"Nombre Especialista\", ci.fecha " +
                        "FROM solicitar s, especialista es, cita ci, cliente cl " +
                        "WHERE s.dni_cliente = cl.dni " +
                        "AND s.dni_esp = es.dni " +
                        "AND s.id_cita = ci.id ";
            case "receta":
                return "SELECT medicamentos,precio,l.id AS Lesión,cli.nombre AS \"Nombre cliente\",esp.nombre AS \"Nombre especialista\"\r\n"
                        + "from receta r,lesion l, cliente cli,especialista esp, tratar t\r\n"
                        + "WHERE r.id_lesion = t.id_lesion\r\n"
                        + "AND r.dni_cliente = t.dni_cliente\r\n"
                        + "AND r.dni_esp = t.dni_esp\r\n"
                        + "AND t.id_lesion = l.id\r\n"
                        + "AND t.dni_cliente = cli.dni\r\n"
                        + "AND t.dni_esp = esp.dni";
            case "consulta":
                return "SELECT * from consulta";
            case "usuario":
            	return "Select * from users";
            default:
                throw new IllegalArgumentException("Consulta no definida para tipo: " + item);
        }
    }

    // Refresca la tabla leyendo los datos de la BD
    public void actualizarTabla() {
        String query = configurarModeloTabla(tituloV, user,permisos);
        try (Connection cn = GestionBasedeDatos.prueba();
             PreparedStatement st = cn.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            // Añadir nombres de columnas
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnLabel(i));
            }

            // Añadir filas de datos
            while (rs.next()) {
                Object[] fila = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                model.addRow(fila);
            }

            // Actualizar tabla
            tableMostrar.setModel(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void abrirVentanaEditar(String tipo, Object[] datos) {
        JFrame ventanaEdicion = new JFrame("Editar " + tipo);
        ventanaEdicion.setSize(400, 300);
        ventanaEdicion.setLocationRelativeTo(this);
        ventanaEdicion.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField[] campos = new JTextField[datos.length];
        for (int i = 0; i < datos.length; i++) {
            JPanel fila = new JPanel(new FlowLayout());
            JLabel label = new JLabel(model.getColumnName(i) + ": ");
            JTextField campo = new JTextField(20);
            campo.setText(datos[i].toString());
            if (tipo.equals("especialista")) {
                if (i == datos.length - 1) {
                    campo.setEditable(false);
                }
            }
            campos[i] = campo;
            fila.add(label);
            fila.add(campo);
            panel.add(fila);
        }

        ventanaEdicion.add(panel, BorderLayout.CENTER);

        // Guarda el valor original de la clave primaria para buscar el registro correcto en el UPDATE
        final String valorClaveOriginal = datos[0].toString();

        JButton btnGuardar = new JButton("Guardar cambios");
        Estilo.estilizarBoton(btnGuardar); 
        btnGuardar.addActionListener(e -> {
            actualizarRegistroEnBD(tipo, campos, valorClaveOriginal);
            ventanaEdicion.dispose();
            actualizarTabla();
        });

        ventanaEdicion.add(btnGuardar, BorderLayout.SOUTH);
        ventanaEdicion.setVisible(true);
    }

    private void actualizarRegistroEnBD(String tipo, JTextField[] campos, String claveOriginal) {
        String tabla = tipo.toLowerCase();
        String sql = null;

        switch (tabla) {
            case "cliente":
                sql = "UPDATE cliente SET dni = ?, nombre = ?, apellidos = ?, fecha_nacimiento = ? WHERE dni = ?";
                break;
            case "especialista":
                sql = "UPDATE especialista SET dni = ?, nombre = ?, apellidos = ?, sueldo = ?, num_con = ? WHERE dni = ?";
                break;
            case "cita":
                sql = "UPDATE cita SET id = ?, fecha = ? WHERE id = ?";
                break;
            case "solicitar":
                sql = "UPDATE solicitar SET id_cita = ?, dni_cliente = ?, dni_esp = ? WHERE id_cita = ?";
                break;
            case "usuario":
            	sql = "UPDATE users SET nombre= ?,passwd = ?,permisos = ? Where nombre = ?";
            	break;
            default:
                JOptionPane.showMessageDialog(this, "Edición no soportada para: " + tipo);
                return;
        }

        try (Connection cn = GestionBasedeDatos.prueba();
             PreparedStatement st = cn.prepareStatement(sql)) {

            switch (tabla) {
                case "cliente":
                    st.setString(1, campos[0].getText()); // nuevo dni
                    st.setString(2, campos[1].getText()); // nombre
                    st.setString(3, campos[2].getText()); // apellidos
                    st.setDate(4, Date.valueOf(campos[3].getText())); // fecha_nacimiento
                    st.setString(5, claveOriginal); // dni original en WHERE
                    break;

                case "especialista":
                    st.setString(1, campos[0].getText()); // nuevo dni
                    st.setString(2, campos[1].getText()); // nombre
                    st.setString(3, campos[2].getText()); // apellidos
                    st.setBigDecimal(4, new java.math.BigDecimal(campos[3].getText())); // sueldo
                    st.setInt(5, Integer.parseInt(campos[4].getText())); // num_con
                    st.setString(6, claveOriginal); // dni original en WHERE
                    break;

                case "cita":
                    st.setInt(1, Integer.parseInt(campos[0].getText())); // nuevo id
                    st.setDate(2, Date.valueOf(campos[1].getText())); // fecha
                    st.setInt(3, Integer.parseInt(claveOriginal)); // id original en WHERE
                    break;

                case "solicitar":
                    st.setInt(1, Integer.parseInt(campos[0].getText())); // nuevo id_cita
                    st.setString(2, campos[1].getText()); // dni_cliente
                    st.setString(3, campos[2].getText()); // dni_esp
                    st.setInt(4, Integer.parseInt(claveOriginal)); // id_cita original en WHERE
                    break;
                case  "usuario":
                    st.setString(1, campos[0].getText()); // nombre
                    st.setString(2, campos[1].getText()); // password
                	st.setInt(3, Integer.parseInt(campos[2].getText())); // permisos
                    st.setString(4, claveOriginal); // dni original en WHERE

            }

            st.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registro actualizado correctamente.");

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) { // Violación de UNIQUE o PK
                JOptionPane.showMessageDialog(this, "Error: Ya existe un registro con esa clave primaria o valor único duplicado.");
            } else if ("23503".equals(e.getSQLState())) { // Violación de clave foránea
                JOptionPane.showMessageDialog(this, "Error: El valor viola una restricción de clave foránea.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar el registro: " + e.getMessage());
            }
        } catch (IllegalArgumentException ie) {
            JOptionPane.showMessageDialog(this, "Error en el formato de la fecha. Use formato yyyy-mm-dd.");
        }
    }

}

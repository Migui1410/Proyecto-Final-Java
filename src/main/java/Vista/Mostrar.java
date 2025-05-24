package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Controlador.GestionBasedeDatos;
import Controlador.Navegador;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class Mostrar extends JFrame {
    private String tituloV;
    private JTable tableMostrar;
    private DefaultTableModel model;
    private JPanel contentPane;
    private Navegador nav = new Navegador();
    private String user;
    private int permisos;

    public Mostrar(String tipo, String nombre,int permisos) {
    	user = nombre;
        tituloV = tipo;
        this.permisos = permisos;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                if (tituloV.equalsIgnoreCase("especialista")) {
                    actualizarTablaEspecialistaConTipo();
                } else {
                    actualizarTabla();
                }
            }
        });
        setTitle("mostrar" + tipo);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 450);
        setLocationRelativeTo(null);

        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        Estilo.aplicarEstiloBasico(contentPane);
        setContentPane(contentPane);

        JLabel lblTitulo = new JLabel("Mostrar " + tipo, JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        Estilo.estilizarEtiqueta(lblTitulo, true);
        contentPane.add(lblTitulo, BorderLayout.NORTH);

        model = new DefaultTableModel();
        tableMostrar = new JTable(model);
        Estilo.estilizarTabla(tableMostrar);
        tableMostrar.setAutoCreateRowSorter(true);
        if (tituloV.equalsIgnoreCase("especialista")) {
            actualizarTablaEspecialistaConTipo();
        } else {
            actualizarTabla();
        }

        JScrollPane scrollPane = new JScrollPane(tableMostrar);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        Estilo.aplicarEstiloBasico(panelBotones);
        contentPane.add(panelBotones, BorderLayout.SOUTH);


        JButton btnVolver = new JButton("Volver");
        panelBotones.add(btnVolver);
        btnVolver.addActionListener(e -> {
            setVisible(false);
            nav.dispatcher("principal", true);
        });
        Estilo.estilizarBoton(btnVolver);

        JButton btnEditar = new JButton("Editar");
        Estilo.estilizarBoton(btnEditar);
        btnEditar.addActionListener(e -> {
            int filaSeleccionada = tableMostrar.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una fila para editar.");
                return;
            }
            int columnas = tableMostrar.getColumnCount();
            Object[] datosFila = new Object[columnas];
            for (int i = 0; i < columnas; i++) {
                datosFila[i] = tableMostrar.getValueAt(filaSeleccionada, i);
            }
            abrirVentanaEditar(tituloV, datosFila);
        });
        panelBotones.add(btnEditar);

        JButton btnEliminar = new JButton("Eliminar");
        Estilo.estilizarBoton(btnEliminar);
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
            case "especialista": {
                try (Connection cn = GestionBasedeDatos.prueba()) {
                    cn.setAutoCommit(false);
                    try (PreparedStatement psDentista = cn.prepareStatement("DELETE FROM dentista WHERE dni_esp = ?")) {
                        psDentista.setString(1, id.toString());
                        psDentista.executeUpdate();
                    }
                    try (PreparedStatement psFisio = cn.prepareStatement("DELETE FROM fisio WHERE dni_esp = ?")) {
                        psFisio.setString(1, id.toString());
                        psFisio.executeUpdate();
                    }
                    try (PreparedStatement psPodo = cn.prepareStatement("DELETE FROM podologo WHERE dni_esp = ?")) {
                        psPodo.setString(1, id.toString());
                        psPodo.executeUpdate();
                    }
                    try (PreparedStatement psEspecialista = cn.prepareStatement("DELETE FROM especialista WHERE dni = ?")) {
                        psEspecialista.setString(1, id.toString());
                        psEspecialista.executeUpdate();
                    }
                    cn.commit();
                    JOptionPane.showMessageDialog(this, "Registro eliminado correctamente.");
                    actualizarTablaEspecialistaConTipo();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error al eliminar el registro.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }            
            	break; // salir para no ejecutar el código común
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
                   if (tituloV.equalsIgnoreCase("especialista")) {
                       actualizarTablaEspecialistaConTipo();
                   } else {
                       actualizarTabla();
                   }
               } catch (SQLException ex) {
                   ex.printStackTrace();
                   JOptionPane.showMessageDialog(this, "Error al eliminar el registro.", "Error", JOptionPane.ERROR_MESSAGE);
               }
               
      });
        panelBotones.add(btnEliminar);
    }

    
    
        
    public void permisos(String n,int p) {
        permisos = p;
        user = n;
        actualizarTabla();
    }

    private String configurarModeloTabla(String item) {
        switch (item.toLowerCase()) {
            case "cliente":
                return "SELECT * FROM cliente";
            case "especialista":
                return "SELECT * FROM especialista";
            case "solicitar":
                if (permisos == 0) {
                    return "SELECT ci.id AS \"ID CITA\", cl.nombre AS \"Nombre Cliente\", es.nombre AS \"Nombre Especialista\", ci.fecha " +
                            "FROM solicitar s, especialista es, cita ci, cliente cl " +
                            "WHERE s.dni_cliente = cl.dni " +
                            "AND s.dni_esp = es.dni " +
                            "AND s.id_cita = ci.id ";
                } else {
                    return
                            "SELECT ci.id AS \"ID CITA\", cl.nombre AS \"Nombre Cliente\", es.nombre AS \"Nombre Especialista\", ci.fecha " +
                                    "FROM solicitar s, especialista es, cita ci, cliente cl " +
                                    "WHERE s.dni_cliente = cl.dni " +
                                    "AND s.dni_esp IN (SELECT dni FROM especialista WHERE Lower(nombre) = '" + user.toLowerCase() + "') " +
                                    "AND s.id_cita = ci.id " +
                                    "AND s.dni_esp = es.dni";
                }
            case "receta":
                return "SELECT medicamentos,precio,l.nombre AS Lesión,cli.nombre AS \"Nombre cliente\",esp.nombre AS \"Nombre especialista\"\r\n"
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

    public void actualizarTabla() {
        String query = configurarModeloTabla(tituloV);
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
            e.printStackTrace();
        }
    }

    // Nuevo método para especialistas con columna Tipo
    public void actualizarTablaEspecialistaConTipo() {
        String sqlEspecialistas = "SELECT dni, nombre, apellidos,sueldo,num_con FROM especialista";

        try (Connection cn = GestionBasedeDatos.prueba();
             PreparedStatement psEspecialistas = cn.prepareStatement(sqlEspecialistas);
             ResultSet rsEspecialistas = psEspecialistas.executeQuery()) {

            model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            model.addColumn("DNI");
            model.addColumn("Nombre");
            model.addColumn("Apellido");
            model.addColumn("Sueldo");
            model.addColumn("Numero consulta");
            model.addColumn("Tipo");

            while (rsEspecialistas.next()) {
                String dni = rsEspecialistas.getString("dni");
                String nombre = rsEspecialistas.getString("nombre");
                String apellido = rsEspecialistas.getString("apellidos");
                int sueldo = rsEspecialistas.getInt("sueldo");
                int consulta = rsEspecialistas.getInt("num_con");


                String tipo = obtenerTipoEspecialista(cn, dni);

                model.addRow(new Object[]{dni, nombre, apellido, sueldo,consulta, tipo});
            }

            tableMostrar.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar especialistas con tipo", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private String obtenerTipoEspecialista(Connection cn, String dni) throws SQLException {
        if (existeEnTabla(cn, "dentista", dni)) return "Dentista";
        if (existeEnTabla(cn, "fisio", dni)) return "Fisio";
        if (existeEnTabla(cn, "podologo", dni)) return "Podólogo";
        return "Especialista";
    }

    private boolean existeEnTabla(Connection cn, String tabla, String dni) throws SQLException {
        String sql = "SELECT 1 FROM " + tabla + " WHERE dni_esp = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void abrirVentanaEditar(String tipo, Object[] datos) {
        new Editar(tipo, datos, datos[0].toString(), model, this::actualizarTabla);
    }
}


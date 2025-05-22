package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Controlador.GestionBasedeDatos;
import Controlador.Navegador;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Buscar extends JFrame {
    private JComboBox<String> comboEntidad;
    private JPanel panelFormulario;
    private JTable tablaResultados;
    private DefaultTableModel modelResultados;
    private Navegador nav = new Navegador();
    private int permisos;

    private JTextField campo1, campo2, campo3;

    public Buscar(int permisos) {
        this.permisos = permisos;

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                nav.dispatcher("principal", true);
                setVisible(false);
            }
        });

        setTitle("buscar");
        setSize(1100, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(10, 10));
        Estilo.aplicarFuenteGlobal();

        // Panel izquierdo para combo y formulario
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelIzquierdo.setPreferredSize(new Dimension(350, 0));
        Estilo.aplicarEstiloBasico(panelIzquierdo);

        // Label y combo entidad
        JLabel lblSeleccion = new JLabel("Seleccione entidad:");
        lblSeleccion.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelIzquierdo.add(lblSeleccion);

        comboEntidad = new JComboBox<>();
        comboEntidad.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        comboEntidad.setAlignmentX(Component.LEFT_ALIGNMENT);
        Estilo.aplicarEstiloBasico(comboEntidad);
        panelIzquierdo.add(Box.createVerticalStrut(5));
        panelIzquierdo.add(comboEntidad);
        panelIzquierdo.add(Box.createVerticalStrut(10));

        // Panel del formulario
        panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setAlignmentX(Component.LEFT_ALIGNMENT);
        Estilo.aplicarEstiloBasico(panelFormulario);
        panelIzquierdo.add(panelFormulario);

        // Agregar panel izquierdo
        getContentPane().add(panelIzquierdo, BorderLayout.WEST);

        // Tabla de resultados
        modelResultados = new DefaultTableModel();
        tablaResultados = new JTable(modelResultados);
        tablaResultados.setFillsViewportHeight(true);
        Estilo.estilizarTabla(tablaResultados);

        JScrollPane scrollPane = new JScrollPane(tablaResultados);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        Estilo.aplicarEstiloBasico(getContentPane());

        // Inicializar combo y formulario
        actualizarComboEntidad();

        // Listener para cambio de entidad
        comboEntidad.addActionListener(e -> cambiarFormulario());
    }

    public void cambiarFormulario() {
        panelFormulario.removeAll();
        String tipo = (String) comboEntidad.getSelectedItem();

        campo1 = new JTextField(15);
        campo2 = new JTextField(15);
        campo3 = new JTextField(15);

        if (tipo == null) return;  // Evita ejecutar si no hay selección

        if (tipo.equals("cliente") || tipo.equals("especialista")) {
        	agregarCampo("DNI:", campo1);
            agregarCampo("Nombre:", campo2);
        }

        if ("cliente".equals(tipo)) {
            agregarCampo("Apellidos:", campo3);
        } else if ("especialista".equals(tipo)) {
            agregarCampo("Numero consulta:", campo3);
        } else if ("cita".equals(tipo)) {
            agregarCampo("Nombre cliente: ", campo1);
            agregarCampo("Nombre Especialista: ", campo2);
            agregarCampo("Fecha (yyyy-mm-dd):", campo3);
        }

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        Estilo.aplicarEstiloBasico(panelBotones);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscar(tipo));
        Estilo.estilizarBoton(btnBuscar);
        JButton btnBorrar = new JButton("Borrar");
        btnBorrar.addActionListener(e -> {
            campo1.setText("");
            campo2.setText("");
            if (campo3 != null) campo3.setText("");
            limpiarTabla();
        });
        Estilo.estilizarBoton(btnBorrar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnBorrar);

        JButton btnVolver = new JButton("Volver");
        Estilo.estilizarBoton(btnVolver);
        btnVolver.addActionListener(e -> {
            setVisible(false);
            nav.dispatcher("principal", true);
        });
        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelVolver.setOpaque(false);
        panelVolver.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        panelVolver.add(btnVolver);

        JPanel panelBotonesContainer = new JPanel();
        panelBotonesContainer.setLayout(new BoxLayout(panelBotonesContainer, BoxLayout.Y_AXIS));
        panelBotonesContainer.setOpaque(false);
        panelBotonesContainer.add(panelBotones);
        panelBotonesContainer.add(panelVolver);

        panelFormulario.add(Box.createVerticalStrut(10));
        panelFormulario.add(panelBotonesContainer);
        panelFormulario.revalidate();
        panelFormulario.repaint();
    }

    private void agregarCampo(String labelTexto, JTextField campo) {
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        Estilo.aplicarEstiloBasico(contenedor);

        JLabel label = new JLabel(labelTexto);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        contenedor.add(label);
        contenedor.add(campo);
        contenedor.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panelFormulario.add(contenedor);
    }

    private void buscar(String tipo) {
        String sql = "";
        switch (tipo) {
            case "cliente" :
            	sql = "SELECT * FROM cliente WHERE dni LIKE ? AND nombre LIKE ? AND apellidos LIKE ?";
            	break;
            case "especialista" :
            	sql = "SELECT * FROM especialista WHERE dni LIKE ? AND nombre LIKE ? AND num_con::text LIKE ?";
            	break;
            case "cita" :
            	sql = "SELECT cl.nombre AS \"Nombre Cliente\", esp.nombre As \"Nombre Especialista\" ,c.fecha"
            			+ " FROM solicitar s, cita c ,cliente cl,especialista esp "
            			+ "WHERE  dni_cliente IN (Select dni from cliente where nombre LIKE ?) "
            			+ "AND  dni_esp IN (Select dni from especialista where nombre LIKE ?) "
            			+ "AND s.id_cita = c.Id  AND s.dni_cliente = cl.dni AND esp.dni = s.dni_esp "
            			+ "AND CAST(c.fecha AS TEXT) LIKE ? ";
            	break;
            default :
                JOptionPane.showMessageDialog(this, "Entidad no soportada.", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            
        };
        try (Connection cn = GestionBasedeDatos.prueba();
             PreparedStatement st = cn.prepareStatement(sql)) {

            st.setString(1, "%" + campo1.getText().trim() + "%");
            st.setString(2, "%" + campo2.getText().trim() + "%");
            st.setString(3, "%" + campo3.getText().trim() + "%");
            

            try (ResultSet rs = st.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int cols = meta.getColumnCount();

                modelResultados.setRowCount(0);
                modelResultados.setColumnCount(0);

                for (int i = 1; i <= cols; i++) {
                    modelResultados.addColumn(meta.getColumnLabel(i));
                }

                while (rs.next()) {
                    Object[] fila = new Object[cols];
                    for (int i = 0; i < cols; i++) {
                        fila[i] = rs.getObject(i + 1);
                    }
                    modelResultados.addRow(fila);
                }

                if (modelResultados.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "No se encontraron resultados.");
                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al realizar la búsqueda.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarComboEntidad() {
    	Estilo.aplicarEstiloBasico(comboEntidad);
        if (comboEntidad == null) return;
        comboEntidad.removeAllItems();
        if (permisos == 1) {
            comboEntidad.addItem("cliente");
            comboEntidad.setEnabled(false);
        } else {
            comboEntidad.addItem("cliente");
            comboEntidad.addItem("especialista");
            comboEntidad.addItem("cita");
            comboEntidad.setEnabled(true);
        }
        comboEntidad.setSelectedIndex(0);
        cambiarFormulario();  // Se actualiza el formulario según el valor seleccionado
    }

    public void permisos(int p) {
        permisos = p;
        actualizarComboEntidad();
    }

    public void limpiarFormulario() {
        panelFormulario.removeAll();
        panelFormulario.revalidate();
        panelFormulario.repaint();
    }

    public void limpiarTabla() {
        modelResultados.setRowCount(0);
        modelResultados.setColumnCount(0);
    }
}

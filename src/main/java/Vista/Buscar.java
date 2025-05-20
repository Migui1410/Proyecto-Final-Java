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
    private Navegador n = new Navegador();

    private JTextField campo1, campo2, campo3;

    public Buscar() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                n.dispatcher("principal", true);
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


        // Label arriba combo
        JLabel lblSeleccion = new JLabel("Seleccione entidad:");
        lblSeleccion.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelIzquierdo.add(lblSeleccion);

        // Combo entidad
        comboEntidad = new JComboBox<>(new String[]{"cliente", "especialista", "cita"});
        comboEntidad.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        comboEntidad.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboEntidad.addActionListener(e -> {
            limpiarFormulario();
            limpiarTabla();
            cambiarFormulario();
        });
        Estilo.aplicarEstiloBasico(comboEntidad);
        panelIzquierdo.add(Box.createVerticalStrut(5));
        panelIzquierdo.add(comboEntidad);
        panelIzquierdo.add(Box.createVerticalStrut(10));

        // Panel del formulario
        panelFormulario = new JPanel();
        Estilo.aplicarEstiloBasico(panelFormulario);
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setAlignmentX(Component.LEFT_ALIGNMENT);
        Estilo.aplicarEstiloBasico(panelFormulario);
        panelIzquierdo.add(panelFormulario);

        getContentPane().add(panelIzquierdo, BorderLayout.WEST);

        // Tabla de resultados
        modelResultados = new DefaultTableModel();
        tablaResultados = new JTable(modelResultados);
        tablaResultados.setFillsViewportHeight(true);
        Estilo.estilizarTabla(tablaResultados);

        JScrollPane scrollPane = new JScrollPane(tablaResultados);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        cambiarFormulario();
        Estilo.aplicarEstiloBasico(getContentPane());
    }

    public void cambiarFormulario() {
        panelFormulario.removeAll();
        String tipo = (String) comboEntidad.getSelectedItem();

        campo1 = new JTextField(15);
        campo2 = new JTextField(15);
        campo3 = new JTextField(15);

        agregarCampo("DNI:", campo1);
        agregarCampo("Nombre:", campo2);
        if ("cliente".equals(tipo)) {
            agregarCampo("Apellidos:", campo3);
        } else if ("especialista".equals(tipo)) {
            agregarCampo("Especialidad (nº consulta):", campo3);
        } else if ("cita".equals(tipo)) {
            panelFormulario.removeAll();
            agregarCampo("ID:", campo1);
            agregarCampo("Fecha (yyyy-mm-dd):", campo2);
        }

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        Estilo.aplicarEstiloBasico(panelBotones);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscar(tipo));
        Estilo.estilizarBoton(btnBuscar);
        JButton btnBorrar = new JButton("Borrar");
        btnBorrar.addActionListener(e -> {
            campo1.setText("");
            campo2.setText("");
            campo3.setText("");
            limpiarTabla();
        });
        Estilo.estilizarBoton(btnBorrar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnBorrar);
        panelFormulario.add(Box.createVerticalStrut(10));
        panelFormulario.add(panelBotones);

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
            case "cliente":
                sql = "SELECT * FROM cliente WHERE dni LIKE ? AND nombre LIKE ? AND apellidos LIKE ?";
                break;
            case "especialista":
                sql = "SELECT * FROM especialista WHERE dni LIKE ? AND nombre LIKE ? AND num_con::text LIKE ?";
                break;
            case "cita":
                sql = "SELECT * FROM solicitar WHERE CAST(id AS TEXT) LIKE ? AND CAST(fecha AS TEXT) LIKE ?";
                break;
        }

        try (Connection cn = GestionBasedeDatos.prueba();
             PreparedStatement st = cn.prepareStatement(sql)) {

            st.setString(1, "%" + campo1.getText().trim() + "%");
            st.setString(2, "%" + campo2.getText().trim() + "%");
            if (!"cita".equals(tipo)) {
                st.setString(3, "%" + campo3.getText().trim() + "%");
            }

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

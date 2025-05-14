package Vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Controlador.GestionBasedeDatos;

public class AnadirCitaForm extends JFrame {
    private JTextField txtNombreCliente;
    private JTextField txtDniCliente;
    private JList<String> listaEspecialistas;
    private JTextField txtNumConsulta;
    private DefaultListModel<String> listModelEspecialistas;
    
    public AnadirCitaForm() {
        super("Anadircitanueva");
        setLayout(new BorderLayout(10, 10));
        
        // Panel superior para datos de cliente
        JPanel panelCliente = new JPanel(new GridLayout(2, 2, 5, 5));
        panelCliente.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        
        txtNombreCliente = new JTextField();
        txtDniCliente = new JTextField();
        txtDniCliente.setEditable(false); // Inicialmente no editable
        
        panelCliente.add(new JLabel("Nombre del Cliente:"));
        panelCliente.add(txtNombreCliente);
        panelCliente.add(new JLabel("DNI (autocompletado):"));
        panelCliente.add(txtDniCliente);
        
        // Panel central para selección de especialista
        JPanel panelEspecialista = new JPanel(new BorderLayout());
        panelEspecialista.setBorder(BorderFactory.createTitledBorder("Selección de Especialista"));
        
        listModelEspecialistas = new DefaultListModel<>();
        cargarEspecialistas(); // Método para cargar datos desde BD
        
        listaEspecialistas = new JList<>(listModelEspecialistas);
        listaEspecialistas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollEspecialistas = new JScrollPane(listaEspecialistas);
        
        panelEspecialista.add(scrollEspecialistas, BorderLayout.CENTER);
        
        // Panel inferior para número de consulta
        JPanel panelConsulta = new JPanel();
        panelConsulta.add(new JLabel("Número de Consulta:"));
        txtNumConsulta = new JTextField(10);
        panelConsulta.add(txtNumConsulta);
        
        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar Cita");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnGuardar.addActionListener(e -> guardarCita());
        btnCancelar.addActionListener(e -> dispose());
        
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);
        
        // Organización de componentes
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.add(panelCliente);
        panelPrincipal.add(panelEspecialista);
        panelPrincipal.add(panelConsulta);
        
        add(panelPrincipal, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        // Configuración del autocompletado
        configurarAutocompletado();
        
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    

	private void cargarEspecialistas() {
        try (Connection con = GestionBasedeDatos.prueba()) {
            String sql = "SELECT e.dni, e.nombre, e.apellidos, e.numCon FROM especialista e ORDER BY e.nombre";
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                
                while (rs.next()) {
                    String dni = rs.getString("dni");
                    String nombre = rs.getString("nombre");
                    String apellidos = rs.getString("apellidos");
                    int numCon = rs.getInt("numCon");
                    
                    String item = String.format("%s %s (Consulta %d) - DNI: %s", 
                                              nombre, apellidos, numCon, dni);
                    listModelEspecialistas.addElement(item);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar especialistas", 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void configurarAutocompletado() {
        txtNombreCliente.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                buscarCliente();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                buscarCliente();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                buscarCliente();
            }
            
            private void buscarCliente() {
                String nombre = txtNombreCliente.getText().trim();
                if (nombre.length() >= 3) { // Buscar después de 3 caracteres
                    try (Connection con = GestionBasedeDatos.prueba()) {
                        String sql = "SELECT dni FROM cliente WHERE nombre LIKE ? LIMIT 1";
                        try (PreparedStatement ps = con.prepareStatement(sql)) {
                            ps.setString(1, nombre + "%");
                            try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                    txtDniCliente.setText(rs.getString("dni"));
                                    txtDniCliente.setEditable(false); // Autocompletado
                                } else {
                                    txtDniCliente.setText("");
                                    txtDniCliente.setEditable(true); // Permitir edición manual
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
    
    private void guardarCita() {
        // Validar datos
        String dniCliente = txtDniCliente.getText().trim();
        String numConsultaStr = txtNumConsulta.getText().trim();
        int selectedIndex = listaEspecialistas.getSelectedIndex();
        
        if (dniCliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente válido", 
                                         "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un especialista", 
                                         "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (numConsultaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe indicar el número de consulta", 
                                         "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int numConsulta = Integer.parseInt(numConsultaStr);
            String itemEspecialista = listModelEspecialistas.getElementAt(selectedIndex);
            // Extraer DNI del especialista del texto del ítem
            String dniEspecialista = itemEspecialista.substring(itemEspecialista.lastIndexOf("DNI: ") + 5);
            
            // Obtener la fecha actual para la cita
            LocalDate fechaCita = LocalDate.now();
            
            // Insertar en la base de datos
            try (Connection con = GestionBasedeDatos.prueba()) {
                con.setAutoCommit(false);
                
                try {
                    // 1. Insertar cita
                    String sqlCita = "INSERT INTO cita (fecha, num_consulta) VALUES (?, ?)";
                    int idCita;
                    
                    try (PreparedStatement ps = con.prepareStatement(sqlCita, Statement.RETURN_GENERATED_KEYS)) {
                        ps.setDate(1, java.sql.Date.valueOf(fechaCita));
                        ps.setInt(2, numConsulta);
                        ps.executeUpdate();
                        
                        try (ResultSet rs = ps.getGeneratedKeys()) {
                            if (rs.next()) {
                                idCita = rs.getInt(1);
                            } else {
                                throw new SQLException("No se pudo obtener el ID de la cita");
                            }
                        }
                    }
                    
                    // 2. Insertar relación en solicitar
                    String sqlSolicitar = "INSERT INTO solicitar (id_cita, dni_cliente, dni_especialista) VALUES (?, ?, ?)";
                    try (PreparedStatement ps = con.prepareStatement(sqlSolicitar)) {
                        ps.setInt(1, idCita);
                        ps.setString(2, dniCliente);
                        ps.setString(3, dniEspecialista);
                        ps.executeUpdate();
                    }
                    
                    con.commit();
                    JOptionPane.showMessageDialog(this, "Cita guardada correctamente", 
                                                 "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    
                } catch (SQLException ex) {
                    con.rollback();
                    throw ex;
                } finally {
                    con.setAutoCommit(true);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Número de consulta inválido", 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar la cita: " + ex.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
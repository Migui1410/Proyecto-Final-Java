package Vista;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controlador.Navegador;
import Modelo.Admin;
import Modelo.Usuario;

public class VentanaPrincipal extends JFrame {
    private Navegador n = new Navegador();
    private static final long serialVersionUID = 1L;
    private int p;
    private String nom;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel panelCabecera;
    private JLabel bienvenida;

    private JMenuBar menuBar;


    public VentanaPrincipal() {
        

        Estilo.aplicarFuenteGlobal();
        setTitle("principal");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        setMinimumSize(new Dimension(800, 500));

        addWindowListener(new WindowAdapter() {
                @Override
                public void windowOpened(WindowEvent e) {
                	actualizarUser(p,nom);
                	actualizarMenuYBotones();
                }

                @Override
                public void windowActivated(WindowEvent e) {
                	actualizarUser(p,nom);
                    actualizarMenuYBotones();
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    n.dispatcher("iniciosesion", true);
                }
            });

        getContentPane().setBackground(Estilo.FONDO_CONTENIDO);
        getContentPane().setLayout(new BorderLayout());

     // Panel cabecera
        JPanel panelCabecera = new JPanel(new BorderLayout());
        panelCabecera.setBackground(Estilo.FONDO_PANEL_TOP);

        bienvenida = new JLabel("Bienvenido, " + nom);
        Estilo.estilizarEtiqueta(bienvenida, true);
        bienvenida.setFont(Estilo.FUENTE_TITULO);
        bienvenida.setForeground(Color.WHITE);
        panelCabecera.add(bienvenida, BorderLayout.WEST);

        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        Estilo.aplicarEstiloBasico(btnCerrarSesion);
        btnCerrarSesion.setForeground(Estilo.FONDO_CONTENIDO);
        btnCerrarSesion.setOpaque(true);
        btnCerrarSesion.setContentAreaFilled(false);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.addActionListener(e -> {
            setVisible(false);
            n.dispatcher("iniciosesion", true);
            dispose();
        });
        panelCabecera.add(btnCerrarSesion, BorderLayout.EAST);

        getContentPane().add(panelCabecera, BorderLayout.NORTH);
        // Panel central
        getContentPane().add(crearPanelPrincipal(), BorderLayout.CENTER);

        // Menú
        menuBar = new JMenuBar();
        menuBar.setBackground(Estilo.FONDO_CONTENIDO);
        setJMenuBar(menuBar);
        actualizarMenuYBotones();
    }

    public void actualizarUser(int permisos,String nombre) {
    	p = permisos;
        nom = nombre;
    }
    private JPanel crearPanelPrincipal() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(Estilo.FONDO_GENERAL);

        leftPanel = new JPanel(); // ← Hacerlo atributo si quieres cambiar los botones más tarde
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        leftPanel.setBackground(Estilo.FONDO_GENERAL);

        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Estilo.FONDO_GENERAL);

        ImageIcon icono = new ImageIcon("src/main/java/Img/silueta.png");
        Image img = icono.getImage().getScaledInstance(300, 350, Image.SCALE_SMOOTH);
        JLabel lblImagen = new JLabel(new ImageIcon(img));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.add(lblImagen, BorderLayout.CENTER);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        return mainPanel;
    }


    private JButton crearBotonEstilizado(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(Estilo.FUENTE_BASE);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setPreferredSize(new Dimension(220, 50));
        boton.setMaximumSize(new Dimension(220, 50));
        boton.setBackground(Estilo.GRIS_BOTON);
        boton.setForeground(Estilo.TEXTO_PRIMARIO);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Estilo.FONDO_CONTENIDO, 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        boton.setFocusPainted(false);

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(Estilo.FONDO_GENERAL);
                boton.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(Estilo.GRIS_BOTON);
                boton.setForeground(Estilo.TEXTO_PRIMARIO);
            }
        });

        return boton;
    }

    /*public void actualizarUsuarioActivo() {
        this.user = gs.Activo();
        menu(tienePermiso());
    }*/

    private boolean tienePermiso() {
        if (p == 0) {
            return true;
        } else {
            return false;
        }
    }

    private void actualizarMenuYBotones() {
        menuBar.removeAll();
        leftPanel.removeAll();
     

        bienvenida.setText("Bienvenido, " + nom);

        if (tienePermiso()) {
            // Menú para admin
            JMenu mnGestionCliente = crearMenu("Gestión Cliente");
            JMenuItem ItemAnadir = crearItemMenu("Añadir");
            ItemAnadir.addActionListener(e -> abrirVentanaAnadir("cliente"));
            mnGestionCliente.add(ItemAnadir);

            JMenuItem ItemMostrar = crearItemMenu("Mostrar");
            ItemMostrar.addActionListener(e -> abrirVentanaMostrar("cliente"));
            mnGestionCliente.add(ItemMostrar);
            menuBar.add(mnGestionCliente);

            JMenu mnGestionEspecialista = crearMenu("Gestión Especialista");
            JMenuItem ItemAnadirE = crearItemMenu("Añadir");
            ItemAnadirE.addActionListener(e -> abrirVentanaAnadir("especialista"));
            mnGestionEspecialista.add(ItemAnadirE);

            JMenuItem ItemMostrarE = crearItemMenu("Mostrar");
            ItemMostrarE.addActionListener(e -> abrirVentanaMostrar("especialista"));
            mnGestionEspecialista.add(ItemMostrarE);
            menuBar.add(mnGestionEspecialista);

            JMenu mnGestionCitas = crearMenu("Gestión Citas");
            JMenuItem ItemAnadirC = crearItemMenu("Añadir");
            ItemAnadirC.addActionListener(e -> abrirVentanaAnadir("solicitar"));
            mnGestionCitas.add(ItemAnadirC);

            JMenuItem ItemMostrarC = crearItemMenu("Mostrar");
            ItemMostrarC.addActionListener(e -> abrirVentanaMostrar("solicitar"));
            mnGestionCitas.add(ItemMostrarC);
            menuBar.add(mnGestionCitas);
            
            JMenu mnGestionUsuarios = crearMenu("Gestión Usuarios");
            JMenuItem ItemAnadirU = crearItemMenu("Añadir");
            ItemAnadirU.addActionListener(e -> abrirVentanaAnadir("usuario"));
            mnGestionUsuarios.add(ItemAnadirU);

            JMenuItem ItemMostrarU = crearItemMenu("Mostrar");
            ItemMostrarU.addActionListener(e -> abrirVentanaMostrar("usuario"));
            mnGestionUsuarios.add(ItemMostrarU);
            menuBar.add(mnGestionUsuarios);

            // Botones para admin
            JButton btnBuscar = crearBotonEstilizado("Buscar");
            JButton btnConsultas = crearBotonEstilizado("Mostrar Consultas");
            JButton btnRecetas = crearBotonEstilizado("Mostrar Recetas");

            btnBuscar.addActionListener(e -> abrirVentanaBuscar());
            btnConsultas.addActionListener(e -> abrirVentanaMostrar("consulta"));
            btnRecetas.addActionListener(e -> abrirVentanaMostrar("receta"));

            leftPanel.add(Box.createVerticalGlue());
            leftPanel.add(btnBuscar);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            leftPanel.add(btnConsultas);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            leftPanel.add(btnRecetas);
            leftPanel.add(Box.createVerticalGlue());

        } else {
            

            // Botones para usuario
            JButton btnMisCitas = crearBotonEstilizado("Mis Citas");
            JButton btnBuscar = crearBotonEstilizado("Buscar");
            JButton btnExtra = crearBotonEstilizado("Opción Extra");

            btnMisCitas.addActionListener(e -> abrirVentanaMostrar("cita"));
            btnBuscar.addActionListener(e -> abrirVentanaBuscar());
            btnExtra.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad futura"));

            leftPanel.add(Box.createVerticalGlue());
            leftPanel.add(btnMisCitas);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            leftPanel.add(btnBuscar);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            leftPanel.add(btnExtra);
            leftPanel.add(Box.createVerticalGlue());
        }

        leftPanel.revalidate();
        leftPanel.repaint();
        menuBar.revalidate();
        menuBar.repaint();
    }


    private JMenu crearMenu(String texto) {
        JMenu menu = new JMenu(texto);
        menu.setFont(Estilo.FUENTE_BASE);
        menu.setForeground(Estilo.TEXTO_PRIMARIO);
        menu.setBackground(Estilo.FONDO_GENERAL);
        return menu;
    }

    private JMenuItem crearItemMenu(String texto) {
        JMenuItem item = new JMenuItem(texto);
        item.setFont(Estilo.FUENTE_BASE);
        item.setBackground(Estilo.FONDO_CONTENIDO);
        item.setForeground(Estilo.TEXTO_PRIMARIO);
        return item;
    }

    private void abrirVentanaAnadir(String tipo) {
        String titulo = tipo.toLowerCase();
        if (!n.existe("anadir" + titulo)) {
            n.crearVentana(new Anadir(titulo));
            n.dispatcher("anadir" + titulo, true);
        } else {
            n.dispatcher("anadir" + titulo, true);
        }
    }

    private void abrirVentanaMostrar(String tipo) {
        String titulo = tipo.toLowerCase();
        if (!n.existe("mostrar" + titulo)) {
            n.crearVentana(new Mostrar(titulo,nom));
            n.dispatcher("mostrar" + titulo, true);
        } else {
            Mostrar m = (Mostrar) n.getVentana("mostrar" + titulo);
            m.actualizarTabla();
            n.dispatcher("mostrar" + titulo, true);
        }
    }
    
    private void abrirVentanaBuscar() {
    	if(!n.existe("buscar")) {
            n.crearVentana(new Buscar());
            n.dispatcher("buscar" ,true);
            setVisible(false);
        } else {
        	Buscar b= (Buscar) n.getVentana("buscar");
            b.limpiarFormulario();
            b.limpiarTabla();
            b.cambiarFormulario();
        	n.dispatcher("buscar",true);
        	setVisible(false);
            
        }
    }
}
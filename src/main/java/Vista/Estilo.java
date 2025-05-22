package Vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class Estilo {
    // Colores básicos para el diseño
    public static final Color FONDO_GENERAL = new Color(225, 225, 225);       // Gris claro para fondo general
    public static final Color FONDO_CONTENIDO = new Color(245, 245, 245);     // Gris más claro para campos y contenedores
    public static final Color TEXTO_PRIMARIO = new Color(40, 40, 40);          // Gris oscuro para texto
    public static final Color TEXTO_SECUNDARIO = new Color(50, 50, 50);    // Gris medio para texto secundario
    public static final Color GRIS_BOTON = new Color(180, 180, 180);
    public static final Color GRIS_BOTON_HOVER = new Color(150, 150, 150);
    public static final Color FONDO_PANEL_TOP = new Color(45, 45, 45);        // Gris oscuro
    public static final Color FONDO_MENU = new Color(70, 70, 70);  
    public static final Color NEGRO = new Color(10, 10, 10); 

    // Fuentes globales
    public static final Font FUENTE_BASE = new Font("Segoe UI", Font.PLAIN, 16); // Fuente más grande y legible
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 20); // Título más grande y destacado
    public static final Font FUENTE_ENFASIS = new Font("Segoe UI", Font.BOLD, 18); // Fuente con énfasis

    public static void aplicarEstiloBasico(Container contenedor) {
        if (contenedor == null) return;

        contenedor.setBackground(FONDO_GENERAL);  // Fondo gris claro para el contenedor

        for (Component c : contenedor.getComponents()) {
            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                label.setForeground(TEXTO_PRIMARIO);  // Texto en gris oscuro
                label.setFont(FUENTE_BASE);
            } else if (c instanceof JTextField) {
                JTextField campo = (JTextField) c;
                campo.setFont(FUENTE_BASE);
                campo.setBackground(FONDO_CONTENIDO);  // Fondo gris más claro para campos de texto
                campo.setForeground(Color.BLACK);      // Texto en negro para mejor contraste
                campo.setBorder(new LineBorder(FONDO_CONTENIDO, 1));  // Borde azul para los campos de texto
            } else if (c instanceof Container) {
                Container subContenedor = (Container) c;
                aplicarEstiloBasico(subContenedor);  // Aplicamos recursivamente a subcontenedores
            }else if (c instanceof JComboBox<?>) {
                JComboBox<?> combo = (JComboBox<?>) c;
                combo.setFont(FUENTE_BASE);
                combo.setBackground(FONDO_CONTENIDO);
                combo.setForeground(TEXTO_PRIMARIO);
                combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                combo.setBorder(new LineBorder(Color.LIGHT_GRAY));
            }

        }
    }

    // Método para aplicar una fuente global
    public static void aplicarFuenteGlobal() {
        UIManager.put("Label.font", FUENTE_BASE);
        UIManager.put("Button.font", FUENTE_BASE);
        UIManager.put("TextField.font", FUENTE_BASE);
        UIManager.put("PasswordField.font", FUENTE_BASE);
        UIManager.put("TextArea.font", FUENTE_BASE);
        UIManager.put("Table.font", FUENTE_BASE);
        UIManager.put("TableHeader.font", FUENTE_ENFASIS);
    }

    // Estiliza una etiqueta (JLabel) con colores y fuentes personalizadas
    public static void estilizarEtiqueta(JLabel label, boolean esTitulo) {
        label.setFont(esTitulo ? FUENTE_TITULO : FUENTE_BASE);
        label.setForeground(esTitulo ? TEXTO_PRIMARIO : TEXTO_SECUNDARIO);
    }

    // Estiliza una tabla con fuentes y colores adecuados
    public static void estilizarTabla(JTable tabla) {
        tabla.setFont(FUENTE_BASE);
        tabla.setRowHeight(24);
        tabla.setGridColor(Color.LIGHT_GRAY);
        tabla.setForeground(Color.BLACK);
        tabla.setBackground(Color.WHITE);
        tabla.setBorder(new LineBorder(Color.LIGHT_GRAY));

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(TEXTO_PRIMARIO);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));  // Fila alternada
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        JTableHeader header = tabla.getTableHeader();
        header.setFont(FUENTE_ENFASIS);
        header.setBackground(FONDO_GENERAL);
        header.setForeground(TEXTO_PRIMARIO);
        header.setReorderingAllowed(false);
    }
    
    public static void estilizarBoton(JButton boton) {
        boton.setFont(FUENTE_BASE);
        boton.setForeground(TEXTO_PRIMARIO);
        boton.setBackground(GRIS_BOTON);
        boton.setFocusPainted(true);
        boton.setBorderPainted(true);
        boton.setContentAreaFilled(false);
        boton.setOpaque(true);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setPreferredSize(new java.awt.Dimension(120, 40));

        boton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            protected void paintButtonPressed(java.awt.Graphics g, JComponent c) {
                super.paintButtonPressed(g, (AbstractButton) c);
                g.setColor(new Color(0, 0, 0, 40));
                g.fillRoundRect(3, 3, c.getWidth() - 6, c.getHeight() - 6, 10, 10);
            }
        });

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(GRIS_BOTON_HOVER);
                boton.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(GRIS_BOTON);
                boton.setForeground(FONDO_MENU);
            }
        });
    }
    
    public static void estilizarBotonHover(JButton boton) {
        boton.setFont(FUENTE_BASE);
        boton.setForeground(TEXTO_PRIMARIO);
        boton.setBackground(GRIS_BOTON);
        boton.setFocusPainted(false);
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setBorder(BorderFactory.createLineBorder(FONDO_CONTENIDO, 1));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(GRIS_BOTON_HOVER);
                boton.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(GRIS_BOTON);
                boton.setForeground(TEXTO_PRIMARIO);
            }
        });

        // Inicializa el color correctamente al crear el botón
        boton.setBackground(GRIS_BOTON);
        boton.setForeground(TEXTO_PRIMARIO);
    }

}

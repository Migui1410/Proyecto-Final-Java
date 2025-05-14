package Vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;

import javax.swing.AbstractButton;
import javax.swing.JButton;
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
    public static final Color AZUL_ACENTO = new Color(0, 120, 255);           // Azul de acento para botones y bordes
    public static final Color GRIS_BOTON = new Color(200, 200, 200);           // Gris oscuro para los botones
    public static final Color GRIS_BOTON_HOVER = new Color(170, 170, 170);     // Gris más oscuro para hover

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
            } else if (c instanceof JButton) {
                JButton boton = (JButton) c;
                boton.setFont(FUENTE_BASE);
                boton.setForeground(TEXTO_PRIMARIO);   // Texto en gris oscuro para contraste
                boton.setBackground(GRIS_BOTON);      // Fondo gris claro para el botón
                boton.setFocusPainted(true);          // Mostrar borde de enfoque
                boton.setBorder(new LineBorder(AZUL_ACENTO, 2)); // Borde azul para destacarlo

                // Efecto Hover: cambiar el color de fondo al pasar el ratón
                boton.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        boton.setBackground(AZUL_ACENTO);  // Fondo azul brillante al pasar el ratón
                        boton.setForeground(Color.WHITE);  // Texto blanco cuando se pasa el ratón
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        boton.setBackground(GRIS_BOTON);   // Fondo gris claro cuando no se pasa el ratón
                        boton.setForeground(TEXTO_PRIMARIO);  // Restaurar el texto en gris oscuro
                    }
                });

                // Añadir sombra sutil para un efecto 3D
                boton.setBorderPainted(true);
                boton.setContentAreaFilled(false);
                boton.setOpaque(true);
                boton.setBackground(GRIS_BOTON);
                boton.setFont(new Font("Segoe UI", Font.BOLD, 16));  // Fuente más grande y negrita
                boton.setPreferredSize(new java.awt.Dimension(120, 40));  // Botón más grande para mayor impacto

                // Efecto sombra sutil para darle profundidad
                boton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
                    protected void paintButtonPressed(java.awt.Graphics g, JComponent c) {
                        super.paintButtonPressed(g, (AbstractButton) c);
                        g.setColor(new Color(0, 0, 0, 40)); // Sombra sutil
                        g.fillRoundRect(3, 3, c.getWidth() - 6, c.getHeight() - 6, 10, 10);
                    }
                });
            } else if (c instanceof JTextField) {
                JTextField campo = (JTextField) c;
                campo.setFont(FUENTE_BASE);
                campo.setBackground(FONDO_CONTENIDO);  // Fondo gris más claro para campos de texto
                campo.setForeground(Color.BLACK);      // Texto en negro para mejor contraste
                campo.setBorder(new LineBorder(AZUL_ACENTO, 1));  // Borde azul para los campos de texto
            } else if (c instanceof Container) {
                Container subContenedor = (Container) c;
                aplicarEstiloBasico(subContenedor);  // Aplicamos recursivamente a subcontenedores
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
}

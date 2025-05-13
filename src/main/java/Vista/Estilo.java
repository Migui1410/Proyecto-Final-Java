

package Vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.UIManager;

public class Estilo {
    public static final Color FONDO_CLARO = new Color(240, 248, 255);
    public static final Color AZUL_SUAVE = new Color(173, 216, 230);
    public static final Color TEXTO_OSCURO = new Color(33, 33, 33);
    public static final Font FUENTE_BASE = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 18);

    public static void aplicarEstiloBasico(Container contenedor) {
        if (contenedor == null) return;  

        contenedor.setBackground(FONDO_CLARO);
        for (Component c : contenedor.getComponents()) {
            if (c instanceof JLabel) {
                c.setForeground(TEXTO_OSCURO);
                c.setFont(FUENTE_BASE);
            } else if (c instanceof Container) {
                aplicarEstiloBasico((Container) c);  
            }
        }
    }

    public static void aplicarFuenteGlobal() {
        UIManager.put("Label.font", FUENTE_BASE);
        UIManager.put("Button.font", FUENTE_BASE);
        UIManager.put("TextField.font", FUENTE_BASE);
        UIManager.put("PasswordField.font", FUENTE_BASE);
        UIManager.put("TextArea.font", FUENTE_BASE);
    }

    public static void estilizarEtiqueta(JLabel label, boolean esTitulo) {
        label.setFont(esTitulo ? FUENTE_TITULO : FUENTE_BASE);
        label.setForeground(TEXTO_OSCURO);
    }
}

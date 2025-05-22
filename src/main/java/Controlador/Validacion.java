package Controlador;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Validacion {

    public static void limitarASoloNumeros(JTextField campo, int maxLongitud) {
        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String texto = campo.getText();
                char c = e.getKeyChar();

                if (!Character.isDigit(c) || texto.length() >= maxLongitud) {
                    e.consume();
                }

                if (texto.length() == 8) {
                    try {
                        int dniNumero = Integer.parseInt(texto);
                        char letra = calcularLetraDNI(dniNumero);
                        campo.setText(texto + letra); // Ej: 12345678Z
                        e.consume(); // Evitar que escriba un 9º dígito
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "DNI inválido", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private static char calcularLetraDNI(int dni) {
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        return letras.charAt(dni % 23);
    }

    public static void limitarAFecha(JTextField campo) {
        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || c == '-') || campo.getText().length() >= 10) {
                    e.consume();
                }
            }
        });
    }

    public static void limitarAHora(JTextField campo) {
        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || c == ':') || campo.getText().length() >= 5) {
                    e.consume();
                }
            }
        });
    }

    public static void soloLetras(JTextField campo) {
        campo.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    e.consume();
                }
            }
        });
    }
}

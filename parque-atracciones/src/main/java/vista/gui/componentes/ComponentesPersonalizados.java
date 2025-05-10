package vista.gui.componentes;

import vista.gui.util.EstilosUI;

import javax.swing.*;
import java.awt.*;

import java.awt.geom.RoundRectangle2D;

/**
 * Proporciona componentes personalizados para la interfaz gráfica del sistema.
 * Estos componentes tienen un estilo consistente con el diseño general.
 */
public class ComponentesPersonalizados {

    /**
     * Crea un botón primario con el estilo de la aplicación.
     * @param texto Texto del botón
     * @return JButton personalizado
     */
    public static JButton crearBotonPrimario(String texto) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(EstilosUI.COLOR_PRINCIPAL.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(EstilosUI.COLOR_PRINCIPAL.brighter());
                } else {
                    g2.setColor(EstilosUI.COLOR_PRINCIPAL);
                }
                
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 
                        EstilosUI.RADIO_ESQUINAS, EstilosUI.RADIO_ESQUINAS));
                
                g2.setColor(Color.WHITE);
                FontMetrics metrics = g2.getFontMetrics(getFont());
                int x = (getWidth() - metrics.stringWidth(getText())) / 2;
                int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        
        boton.setFont(EstilosUI.FUENTE_NORMAL);
        boton.setForeground(Color.WHITE);
        boton.setBackground(EstilosUI.COLOR_PRINCIPAL);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setPreferredSize(new Dimension(EstilosUI.ANCHO_BOTON, EstilosUI.ALTO_BOTON));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return boton;
    }
    
    /**
     * Crea un botón secundario con el estilo de la aplicación.
     * @param texto Texto del botón
     * @return JButton personalizado
     */
    public static JButton crearBotonSecundario(String texto) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 
                        EstilosUI.RADIO_ESQUINAS, EstilosUI.RADIO_ESQUINAS));
                
                if (getModel().isPressed()) {
                    g2.setColor(EstilosUI.COLOR_PRINCIPAL.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(EstilosUI.COLOR_PRINCIPAL.brighter());
                } else {
                    g2.setColor(EstilosUI.COLOR_PRINCIPAL);
                }
                
                g2.setStroke(new BasicStroke(2));
                g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, 
                        EstilosUI.RADIO_ESQUINAS, EstilosUI.RADIO_ESQUINAS));
                
                g2.setColor(EstilosUI.COLOR_PRINCIPAL);
                FontMetrics metrics = g2.getFontMetrics(getFont());
                int x = (getWidth() - metrics.stringWidth(getText())) / 2;
                int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        
        boton.setFont(EstilosUI.FUENTE_NORMAL);
        boton.setForeground(EstilosUI.COLOR_PRINCIPAL);
        boton.setBackground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setPreferredSize(new Dimension(EstilosUI.ANCHO_BOTON, EstilosUI.ALTO_BOTON));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return boton;
    }
    
    /**
     * Crea un panel con bordes redondeados y fondo personalizado.
     * @return JPanel personalizado
     */
    public static JPanel crearPanelRedondeado() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 
                        EstilosUI.RADIO_ESQUINAS, EstilosUI.RADIO_ESQUINAS));
                g2.setColor(EstilosUI.COLOR_BORDE);
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 
                        EstilosUI.RADIO_ESQUINAS, EstilosUI.RADIO_ESQUINAS));
                g2.dispose();
            }
        };
        
        panel.setOpaque(false);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        return panel;
    }
    
    /**
     * Crea una etiqueta de título con el estilo de la aplicación.
     * @param texto Texto de la etiqueta
     * @return JLabel personalizado
     */
    public static JLabel crearEtiquetaTitulo(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(EstilosUI.FUENTE_TITULO);
        etiqueta.setForeground(EstilosUI.COLOR_TEXTO);
        return etiqueta;
    }
    
    /**
     * Crea una etiqueta de subtítulo con el estilo de la aplicación.
     * @param texto Texto de la etiqueta
     * @return JLabel personalizado
     */
    public static JLabel crearEtiquetaSubtitulo(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(EstilosUI.FUENTE_SUBTITULO);
        etiqueta.setForeground(EstilosUI.COLOR_TEXTO);
        return etiqueta;
    }
    
    /**
     * Configura una tabla con el estilo de la aplicación.
     * @param tabla JTable a configurar
     */
    public static void configurarTabla(JTable tabla) {
        tabla.setFont(EstilosUI.FUENTE_NORMAL);
        tabla.setRowHeight(35);
        tabla.setShowGrid(true);
        tabla.setGridColor(EstilosUI.COLOR_BORDE);
        tabla.setSelectionBackground(EstilosUI.COLOR_PRINCIPAL.brighter());
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setFocusable(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.getTableHeader().setFont(EstilosUI.FUENTE_SUBTITULO);
        tabla.getTableHeader().setBackground(EstilosUI.COLOR_PRINCIPAL);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setReorderingAllowed(false);
    }
    
    /**
     * Crea un campo de contraseña con el estilo de la aplicación.
     * @return JPasswordField personalizado
     */
    public static JPasswordField crearCampoPassword() {
        JPasswordField campo = new JPasswordField();
        campo.setFont(EstilosUI.FUENTE_NORMAL);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstilosUI.COLOR_BORDE),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        campo.setPreferredSize(new Dimension(200, 35));
        campo.setBackground(Color.WHITE);
        campo.setForeground(Color.BLACK);
        return campo;
    }

    /**
     * Crea un campo de texto con el estilo de la aplicación.
     * @return JTextField personalizado
     */
    public static JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(EstilosUI.FUENTE_NORMAL);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstilosUI.COLOR_BORDE),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        campo.setPreferredSize(new Dimension(200, 35));
        return campo;
    }
}

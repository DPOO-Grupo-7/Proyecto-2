package vista.gui.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Sistema de notificaciones visuales tipo toast/snackbar para la aplicación.
 * Proporciona feedback no intrusivo al usuario sobre operaciones realizadas.
 */
public class NotificacionUI {
    
    /**
     * Tipos de notificación disponibles con colores asociados.
     */
    public enum TipoNotificacion {
        EXITO, ERROR, INFO, ADVERTENCIA
    }
    
    // Duración de la notificación en milisegundos
    private static final int DURACION_DEFAULT = 3000;
    
    /**
     * Muestra una notificación tipo toast/snackbar en la parte inferior del panel.
     * 
     * @param panelPadre Panel donde se mostrará la notificación
     * @param mensaje Mensaje a mostrar
     * @param tipo Tipo de notificación (define el color)
     */
    public static void mostrarNotificacion(JPanel panelPadre, String mensaje, TipoNotificacion tipo) {
        mostrarNotificacion(panelPadre, mensaje, tipo, DURACION_DEFAULT);
    }
    
    /**
     * Muestra una notificación tipo toast/snackbar en la parte inferior del panel.
     * 
     * @param panelPadre Panel donde se mostrará la notificación
     * @param mensaje Mensaje a mostrar
     * @param tipo Tipo de notificación (define el color)
     * @param duracionMs Duración en milisegundos que permanecerá visible
     */
    public static void mostrarNotificacion(JPanel panelPadre, String mensaje, TipoNotificacion tipo, int duracionMs) {
        // Crear panel de notificación
        JPanel panelNotificacion = new JPanel(new BorderLayout(10, 0));
        panelNotificacion.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Determinar color según tipo
        Color colorFondo;
        switch (tipo) {
            case EXITO:
                colorFondo = EstilosUI.COLOR_EXITO;
                break;
            case ERROR:
                colorFondo = EstilosUI.COLOR_ERROR;
                break;
            case ADVERTENCIA:
                colorFondo = EstilosUI.COLOR_SECUNDARIO;
                break;
            case INFO:
            default:
                colorFondo = EstilosUI.COLOR_PRINCIPAL;
                break;
        }
        
        panelNotificacion.setBackground(colorFondo);
        
        // Etiqueta con el mensaje
        JLabel labelMensaje = new JLabel(mensaje);
        labelMensaje.setForeground(Color.WHITE);
        labelMensaje.setFont(EstilosUI.FUENTE_NORMAL);
        
        // Botón para cerrar la notificación
        JButton botonCerrar = new JButton("×");
        botonCerrar.setFont(new Font("Arial", Font.BOLD, 16));
        botonCerrar.setContentAreaFilled(false);
        botonCerrar.setBorderPainted(false);
        botonCerrar.setForeground(Color.WHITE);
        botonCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonCerrar.setFocusPainted(false);
        
        // Agregar componentes al panel
        panelNotificacion.add(labelMensaje, BorderLayout.CENTER);
        panelNotificacion.add(botonCerrar, BorderLayout.EAST);
        
        // Configurar tamaño y posición
        panelNotificacion.setPreferredSize(new Dimension(panelPadre.getWidth() - 40, 50));
        panelNotificacion.setMaximumSize(new Dimension(panelPadre.getWidth() - 40, 50));
        
        // Añadir comportamiento para cerrar al hacer clic
        botonCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelPadre.remove(panelNotificacion);
                panelPadre.revalidate();
                panelPadre.repaint();
            }
        });
        
        // Añadir efecto hover
        panelNotificacion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panelNotificacion.setBackground(colorFondo.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panelNotificacion.setBackground(colorFondo);
            }
        });
        
        // Agregar la notificación al panel padre
        panelPadre.add(panelNotificacion, BorderLayout.SOUTH);
        panelPadre.revalidate();
        panelPadre.repaint();
        
        // Temporizador para ocultar la notificación
        Timer timer = new Timer(duracionMs, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelPadre.remove(panelNotificacion);
                panelPadre.revalidate();
                panelPadre.repaint();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}

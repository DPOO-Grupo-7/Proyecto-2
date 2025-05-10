package vista.gui.util;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.util.EventListener;
import java.util.EventObject;
import javax.swing.event.EventListenerList;

/**
 * Clase de utilidad que define los estilos visuales de la aplicación.
 * Proporciona colores, fuentes y bordes consistentes para toda la interfaz.
 * Soporta cambio de temas (claro/oscuro) y notifica a los componentes registrados.
 */
public class EstilosUI {
    // Listener para cambios de tema
    private static final EventListenerList LISTENERS = new EventListenerList();
    
    // Tema actual
    private static boolean temaOscuro = false;
    
    // Colores del tema claro (default)
    private static final Color CLARO_COLOR_PRINCIPAL = new Color(0x1E88E5); // Azul
    private static final Color CLARO_COLOR_SECUNDARIO = new Color(0xFFC107); // Amarillo
    private static final Color CLARO_COLOR_EXITO = new Color(0x4CAF50); // Verde
    private static final Color CLARO_COLOR_ERROR = new Color(0xF44336); // Rojo
    private static final Color CLARO_COLOR_TEXTO = new Color(0x212121); // Negro
    private static final Color CLARO_COLOR_FONDO = new Color(0xFFFFFF); // Blanco
    private static final Color CLARO_COLOR_FONDO_ALTERNATIVO = new Color(0xF5F5F5); // Gris claro para filas alternas
    private static final Color CLARO_COLOR_BORDE = new Color(0xE0E0E0); // Gris para bordes
    
    // Colores del tema oscuro
    private static final Color OSCURO_COLOR_PRINCIPAL = new Color(0x42A5F5); // Azul más claro
    private static final Color OSCURO_COLOR_SECUNDARIO = new Color(0xFFD54F); // Amarillo más claro
    private static final Color OSCURO_COLOR_EXITO = new Color(0x66BB6A); // Verde más claro
    private static final Color OSCURO_COLOR_ERROR = new Color(0xEF5350); // Rojo más claro
    private static final Color OSCURO_COLOR_TEXTO = new Color(0xE0E0E0); // Gris claro
    private static final Color OSCURO_COLOR_FONDO = new Color(0x212121); // Gris muy oscuro
    private static final Color OSCURO_COLOR_FONDO_ALTERNATIVO = new Color(0x303030); // Gris oscuro para filas alternas
    private static final Color OSCURO_COLOR_BORDE = new Color(0x424242); // Gris para bordes
    
    // Colores actuales (se actualizan según el tema)
    public static Color COLOR_PRINCIPAL = CLARO_COLOR_PRINCIPAL;
    public static Color COLOR_SECUNDARIO = CLARO_COLOR_SECUNDARIO;
    public static Color COLOR_EXITO = CLARO_COLOR_EXITO;
    public static Color COLOR_ERROR = CLARO_COLOR_ERROR;
    public static Color COLOR_TEXTO = CLARO_COLOR_TEXTO;
    public static Color COLOR_FONDO = CLARO_COLOR_FONDO;
    public static Color COLOR_FONDO_ALTERNATIVO = CLARO_COLOR_FONDO_ALTERNATIVO;
    public static Color COLOR_BORDE = CLARO_COLOR_BORDE;
    
    // Fuentes
    public static Font FUENTE_TITULO = new Font("Roboto", Font.BOLD, 20);
    public static Font FUENTE_SUBTITULO = new Font("Roboto", Font.BOLD, 16);
    public static Font FUENTE_NORMAL = new Font("Roboto", Font.PLAIN, 14);
    public static Font FUENTE_PEQUEÑA = new Font("Roboto", Font.PLAIN, 12);
    
    // Bordes
    public static Border BORDE_REDONDEADO = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    public static Border BORDE_PANEL = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15));
    
    // Dimensiones estándar
    public static final int ANCHO_BOTON = 120;
    public static final int ALTO_BOTON = 40;
    public static final int PADDING_COMPONENTES = 10;
    public static final int RADIO_ESQUINAS = 10;
    
    // Dimensiones de interfaz
    public static final int ANCHO_VENTANA = 1200;
    public static final int ALTO_VENTANA = 800;
    public static final int ANCHO_PANEL_LATERAL = 220;
    
    /**
     * Cambia entre tema claro y oscuro.
     * 
     * @param oscuro true para tema oscuro, false para tema claro
     */
    public static void cambiarTema(boolean oscuro) {
        if (temaOscuro == oscuro) return; // No hay cambio
        
        temaOscuro = oscuro;
        
        if (oscuro) {
            // Aplicar tema oscuro
            COLOR_PRINCIPAL = OSCURO_COLOR_PRINCIPAL;
            COLOR_SECUNDARIO = OSCURO_COLOR_SECUNDARIO;
            COLOR_EXITO = OSCURO_COLOR_EXITO;
            COLOR_ERROR = OSCURO_COLOR_ERROR;
            COLOR_TEXTO = OSCURO_COLOR_TEXTO;
            COLOR_FONDO = OSCURO_COLOR_FONDO;
            COLOR_FONDO_ALTERNATIVO = OSCURO_COLOR_FONDO_ALTERNATIVO;
            COLOR_BORDE = OSCURO_COLOR_BORDE;
        } else {
            // Aplicar tema claro
            COLOR_PRINCIPAL = CLARO_COLOR_PRINCIPAL;
            COLOR_SECUNDARIO = CLARO_COLOR_SECUNDARIO;
            COLOR_EXITO = CLARO_COLOR_EXITO;
            COLOR_ERROR = CLARO_COLOR_ERROR;
            COLOR_TEXTO = CLARO_COLOR_TEXTO;
            COLOR_FONDO = CLARO_COLOR_FONDO;
            COLOR_FONDO_ALTERNATIVO = CLARO_COLOR_FONDO_ALTERNATIVO;
            COLOR_BORDE = CLARO_COLOR_BORDE;
        }
        
        // Actualizar bordes con los nuevos colores
        BORDE_REDONDEADO = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        BORDE_PANEL = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Notificar a los componentes sobre el cambio de tema
        fireThemeChanged(new ThemeChangedEvent(EstilosUI.class, oscuro));
    }
    
    /**
     * Verifica si el tema oscuro está activo.
     * 
     * @return true si el tema oscuro está activo, false en caso contrario
     */
    public static boolean isTemaOscuro() {
        return temaOscuro;
    }
    
    /**
     * Incrementa el tamaño de todas las fuentes.
     * 
     * @param incremento Cantidad a incrementar (puede ser negativo para reducir)
     */
    public static void incrementarTamañoFuente(int incremento) {
        FUENTE_TITULO = FUENTE_TITULO.deriveFont((float) (FUENTE_TITULO.getSize() + incremento));
        FUENTE_SUBTITULO = FUENTE_SUBTITULO.deriveFont((float) (FUENTE_SUBTITULO.getSize() + incremento));
        FUENTE_NORMAL = FUENTE_NORMAL.deriveFont((float) (FUENTE_NORMAL.getSize() + incremento));
        FUENTE_PEQUEÑA = FUENTE_PEQUEÑA.deriveFont((float) (FUENTE_PEQUEÑA.getSize() + incremento));
        
        // Notificar el cambio
        fireThemeChanged(new ThemeChangedEvent(EstilosUI.class, temaOscuro));
    }
    
    /**
     * Añade un listener para cambios de tema.
     * 
     * @param listener Listener a añadir
     */
    public static void addThemeChangedListener(ThemeChangedListener listener) {
        LISTENERS.add(ThemeChangedListener.class, listener);
    }
    
    /**
     * Elimina un listener para cambios de tema.
     * 
     * @param listener Listener a eliminar
     */
    public static void removeThemeChangedListener(ThemeChangedListener listener) {
        LISTENERS.remove(ThemeChangedListener.class, listener);
    }
    
    /**
     * Notifica a todos los listeners sobre un cambio de tema.
     * 
     * @param event Evento de cambio de tema
     */
    private static void fireThemeChanged(ThemeChangedEvent event) {
        for (ThemeChangedListener listener : LISTENERS.getListeners(ThemeChangedListener.class)) {
            listener.themeChanged(event);
        }
    }
    
    /**
     * Interfaz para escuchar cambios de tema.
     */
    public interface ThemeChangedListener extends EventListener {
        void themeChanged(ThemeChangedEvent event);
    }
    
    /**
     * Evento de cambio de tema.
     */
    public static class ThemeChangedEvent extends EventObject {
        private static final long serialVersionUID = 1L;
        private boolean temaOscuro;
        
        public ThemeChangedEvent(Object source, boolean temaOscuro) {
            super(source);
            this.temaOscuro = temaOscuro;
        }
        
        public boolean isTemaOscuro() {
            return temaOscuro;
        }
    }
}

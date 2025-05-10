package vista.gui.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

/**
 * Clase de utilidad para manejar diseños responsivos en la interfaz gráfica.
 * Permite adaptar los componentes a diferentes tamaños de pantalla.
 */
public class ResponsiveUI {
    
    // Constantes para los modos de visualización
    public static final int MODO_COMPACTO = 0;
    public static final int MODO_MEDIO = 1;
    public static final int MODO_COMPLETO = 2;
    
    // Umbrales de ancho para los diferentes modos
    private static final int UMBRAL_COMPACTO = 800;
    private static final int UMBRAL_MEDIO = 1200;
    
    /**
     * Aplica configuración responsiva a una ventana, detectando cambios de tamaño.
     * 
     * @param ventana JFrame a configurar
     */
    public static void aplicarResponsividad(JFrame ventana) {
        ventana.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    ajustarComponentes(ventana.getContentPane(), ventana.getWidth());
                });
            }
        });
    }
    
    /**
     * Ajusta los componentes de un contenedor según el ancho disponible.
     * 
     * @param container Contenedor cuyos componentes se ajustarán
     * @param anchoDisponible Ancho disponible para el ajuste
     */
    public static void ajustarComponentes(Container container, int anchoDisponible) {
        int modo = determinarModo(anchoDisponible);
        aplicarModo(container, modo);
    }
    
    /**
     * Determina el modo de visualización según el ancho disponible.
     * 
     * @param anchoDisponible Ancho disponible
     * @return El modo de visualización correspondiente
     */
    public static int determinarModo(int anchoDisponible) {
        if (anchoDisponible < UMBRAL_COMPACTO) {
            return MODO_COMPACTO;
        } else if (anchoDisponible < UMBRAL_MEDIO) {
            return MODO_MEDIO;
        } else {
            return MODO_COMPLETO;
        }
    }
    
    /**
     * Aplica un modo específico de visualización a un contenedor y sus componentes.
     * 
     * @param container Contenedor a configurar
     * @param modo Modo de visualización a aplicar
     */
    private static void aplicarModo(Container container, int modo) {
        // Configurar el contenedor según el modo
        switch (modo) {
            case MODO_COMPACTO:
                configurarModoCompacto(container);
                break;
            case MODO_MEDIO:
                configurarModoMedio(container);
                break;
            case MODO_COMPLETO:
                configurarModoCompleto(container);
                break;
        }
        
        // Aplicar recursivamente a los componentes hijos
        for (Component component : container.getComponents()) {
            if (component instanceof Container) {
                aplicarModo((Container) component, modo);
            }
        }
    }
    
    /**
     * Configura un componente para visualización en modo compacto.
     * 
     * @param container Contenedor a configurar
     */
    private static void configurarModoCompacto(Container container) {
        // Reducir tamaños de fuente para interfaces compactas
        ajustarTamañoFuente(container, -2);
        
        // Convertir layouts horizontales a verticales cuando sea posible
        if (container instanceof JPanel && container.getLayout() instanceof GridLayout) {
            GridLayout layout = (GridLayout) container.getLayout();
            if (layout.getRows() == 1 && layout.getColumns() > 1) {
                container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
            }
        }
        
        // Ajustar divisores para dar más espacio a componentes principales
        if (container instanceof JSplitPane) {
            JSplitPane splitPane = (JSplitPane) container;
            splitPane.setDividerLocation(0.2); // 20% para panel lateral, 80% para contenido
        }
    }
    
    /**
     * Configura un componente para visualización en modo medio.
     * 
     * @param container Contenedor a configurar
     */
    private static void configurarModoMedio(Container container) {
        // Restaurar tamaños de fuente predeterminados
        ajustarTamañoFuente(container, 0);
        
        // Ajustar divisores para equilibrar espacios
        if (container instanceof JSplitPane) {
            JSplitPane splitPane = (JSplitPane) container;
            splitPane.setDividerLocation(0.25); // 25% para panel lateral, 75% para contenido
        }
    }
    
    /**
     * Configura un componente para visualización en modo completo.
     * 
     * @param container Contenedor a configurar
     */
    private static void configurarModoCompleto(Container container) {
        // Mantener o aumentar ligeramente los tamaños de fuente
        ajustarTamañoFuente(container, 0);
        
        // Ajustar divisores para dar espacios equilibrados
        if (container instanceof JSplitPane) {
            JSplitPane splitPane = (JSplitPane) container;
            splitPane.setDividerLocation(0.3); // 30% para panel lateral, 70% para contenido
        }
    }
    
    /**
     * Ajusta el tamaño de fuente de todos los componentes en un contenedor.
     * 
     * @param container Contenedor con los componentes a ajustar
     * @param incremento Incremento (o decremento si es negativo) del tamaño de fuente
     */
    private static void ajustarTamañoFuente(Container container, int incremento) {
        for (Component component : container.getComponents()) {
            Font fuenteActual = component.getFont();
            if (fuenteActual != null) {
                Font nuevaFuente = fuenteActual.deriveFont((float) (fuenteActual.getSize() + incremento));
                component.setFont(nuevaFuente);
            }
            
            if (component instanceof Container) {
                ajustarTamañoFuente((Container) component, incremento);
            }
        }
    }
    
    /**
     * Ajusta dimensiones preferidas de componentes según el modo.
     * 
     * @param componente Componente a ajustar
     * @param modo Modo de visualización
     */
    public static void ajustarDimensionesPreferidas(JComponent componente, int modo) {
        Dimension dimensionActual = componente.getPreferredSize();
        
        switch (modo) {
            case MODO_COMPACTO:
                componente.setPreferredSize(new Dimension(
                    (int)(dimensionActual.width * 0.8),
                    dimensionActual.height
                ));
                break;
            case MODO_MEDIO:
                componente.setPreferredSize(new Dimension(
                    (int)(dimensionActual.width * 0.9),
                    dimensionActual.height
                ));
                break;
            case MODO_COMPLETO:
                // Mantener dimensión original
                break;
        }
    }
}

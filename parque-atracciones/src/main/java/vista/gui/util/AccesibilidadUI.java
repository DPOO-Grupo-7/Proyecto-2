package vista.gui.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 * Utilidades para manejo de errores y funciones de accesibilidad.
 */
public class AccesibilidadUI {
    
    private static final Logger logger = Logger.getLogger(AccesibilidadUI.class.getName());
    
    // Variables para control de tamaño de fuente
    private static int incrementoFuente = 0;
    private static boolean altoContraste = false;
    
    /**
     * Gestiona errores mostrando un mensaje adecuado al usuario.
     * 
     * @param padre Componente padre para el diálogo
     * @param e Excepción ocurrida
     * @param operacion Descripción de la operación que falló
     */
    public static void manejarError(Component padre, Exception e, String operacion) {
        logger.log(Level.SEVERE, "Error al " + operacion, e);
        
        String mensaje;
        if (e instanceof SQLException) {
            mensaje = "Error de base de datos al " + operacion + ".";
        } else if (e instanceof IOException) {
            mensaje = "Error de acceso a datos al " + operacion + ".";
        } else if (e instanceof IllegalArgumentException || e instanceof IllegalStateException) {
            mensaje = "Error de validación: " + e.getMessage();
        } else {
            mensaje = "Error inesperado al " + operacion + ".";
        }
        
        // Añadir detalles técnicos si no es un error de validación
        if (!(e instanceof IllegalArgumentException) && !(e instanceof IllegalStateException)) {
            mensaje += "\nDetalle técnico: " + e.getMessage();
        }
        
        JOptionPane.showMessageDialog(padre, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Configura la descripción accesible de un componente para lectores de pantalla.
     * 
     * @param componente Componente a configurar
     * @param descripcion Descripción accesible
     */
    public static void configurarAccesibilidad(JComponent componente, String descripcion) {
        componente.getAccessibleContext().setAccessibleDescription(descripcion);
    }
    
    /**
     * Activa o desactiva el modo de alto contraste para mejorar la visibilidad.
     * 
     * @param activado true para activar, false para desactivar
     */
    public static void configurarAltoContraste(boolean activado) {
        altoContraste = activado;
        
        // En una implementación real, esto modificaría los colores en EstilosUI
        // y dispararía un evento para actualizar la interfaz
    }
    
    /**
     * Incrementa o reduce el tamaño de las fuentes en la interfaz.
     * 
     * @param incremento Cantidad de puntos a incrementar (positivo) o reducir (negativo)
     */
    public static void ajustarTamañoFuente(int incremento) {
        incrementoFuente += incremento;
        
        // En una implementación real, esto modificaría las fuentes en EstilosUI
        // y dispararía un evento para actualizar la interfaz
    }
    
    /**
     * Aplica los ajustes de accesibilidad a un contenedor y todos sus componentes.
     * 
     * @param contenedor Contenedor donde aplicar los ajustes
     */
    public static void aplicarAjustesAccesibilidad(Container contenedor) {
        // Aplicar alto contraste si está activado
        if (altoContraste) {
            aplicarAltoContraste(contenedor);
        }
        
        // Ajustar tamaño de fuente si es necesario
        if (incrementoFuente != 0) {
            ajustarFuentes(contenedor, incrementoFuente);
        }
    }
    
    /**
     * Aplica el modo de alto contraste a un contenedor y sus componentes.
     * 
     * @param contenedor Contenedor donde aplicar alto contraste
     */
    private static void aplicarAltoContraste(Container contenedor) {
        // Aquí iría la lógica para cambiar colores para alto contraste
        // Por ejemplo, fondo blanco y texto negro para máximo contraste
        
        // Aplicar recursivamente a los componentes hijos
        for (Component componente : contenedor.getComponents()) {
            if (componente instanceof Container) {
                aplicarAltoContraste((Container) componente);
            }
        }
    }
    
    /**
     * Ajusta el tamaño de fuente de todos los componentes en un contenedor.
     * 
     * @param contenedor Contenedor donde ajustar fuentes
     * @param incremento Incremento o decremento de tamaño
     */
    private static void ajustarFuentes(Container contenedor, int incremento) {
        for (Component componente : contenedor.getComponents()) {
            Font fuenteActual = componente.getFont();
            if (fuenteActual != null) {
                Font nuevaFuente = fuenteActual.deriveFont((float) (fuenteActual.getSize() + incremento));
                componente.setFont(nuevaFuente);
            }
            
            if (componente instanceof Container) {
                ajustarFuentes((Container) componente, incremento);
            }
        }
    }
}

package vista.gui;

import vista.gui.util.EstilosUI;
import vista.gui.util.ResponsiveUI;
import vista.gui.util.NotificacionUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Ventana principal de la aplicación gráfica del sistema de gestión
 * de parque de atracciones.
 */
public class VentanaPrincipal extends JFrame {
    
    private String rolUsuario = "Administrador"; // Por defecto, puede cambiar en login
    private String nombreUsuario = "Usuario"; // Por defecto, se actualiza en login
    
    /**
     * Constructor de la ventana principal
     */
    public VentanaPrincipal() {
        configurarVentana();
        mostrarVentanaLogin();
    }
    
    private void configurarVentana() {
        setTitle("Sistema de Gestión - Parque de Atracciones");
        setSize(EstilosUI.ANCHO_VENTANA, EstilosUI.ALTO_VENTANA);
        setMinimumSize(new Dimension(1024, 768));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Icono de la aplicación (se debería tener un archivo de icono)
        // setIconImage(new ImageIcon("ruta/al/icono.png").getImage());
        
        // Manejador para confirmar salida
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int opcion = JOptionPane.showConfirmDialog(
                        VentanaPrincipal.this,
                        "¿Está seguro que desea salir de la aplicación?",
                        "Confirmar salida",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                
                if (opcion == JOptionPane.YES_OPTION) {
                    // Realizar operaciones de limpieza si es necesario
                    VentanaPrincipal.this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                } else {
                    VentanaPrincipal.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }
    
    private void inicializarComponentes() {
        // Utilizar el UIManager para crear el panel adecuado según el rol del usuario
        vista.gui.util.UIManager uiManager = new vista.gui.util.UIManager(rolUsuario, nombreUsuario);
        JPanel panelPrincipal = uiManager.crearPanelPrincipal();
        
        // Aplicar diseño responsivo
        ResponsiveUI.aplicarResponsividad(this);
        
        // Agregar panel principal a la ventana
        setContentPane(panelPrincipal);
        
        // Mostrar notificación de bienvenida
        SwingUtilities.invokeLater(() -> {
            JPanel panel = (JPanel) getContentPane();
            NotificacionUI.mostrarNotificacion(
                panel, 
                "Bienvenido/a " + nombreUsuario, 
                NotificacionUI.TipoNotificacion.INFO
            );
        });
    }
    
    private void mostrarVentanaLogin() {
        VentanaLogin ventanaLogin = new VentanaLogin(this);
        ventanaLogin.setVisible(true);
        
        // Verificar si el login fue exitoso
        if (ventanaLogin.isLoginExitoso()) {
            rolUsuario = ventanaLogin.getTipoUsuario();
            nombreUsuario = ventanaLogin.getNombreUsuario();
            inicializarComponentes();
            setVisible(true);
        } else {
            System.exit(0); // Salir si el usuario cancela el login
        }
    }
    
    /**
     * Método principal para iniciar la aplicación
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        // Configurar el look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Configurar fuentes globales
            Font fuenteGlobal = new Font("Roboto", Font.PLAIN, 14);
            UIManager.put("Button.font", fuenteGlobal);
            UIManager.put("Label.font", fuenteGlobal);
            UIManager.put("TextField.font", fuenteGlobal);
            UIManager.put("ComboBox.font", fuenteGlobal);
            UIManager.put("Table.font", fuenteGlobal);
            UIManager.put("TableHeader.font", new Font("Roboto", Font.BOLD, 14));
            UIManager.put("TabbedPane.font", fuenteGlobal);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Iniciar la aplicación en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal();
        });
    }
}

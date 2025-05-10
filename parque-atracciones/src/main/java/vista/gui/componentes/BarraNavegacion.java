package vista.gui.componentes;

import vista.gui.util.EstilosUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Barra de navegación principal para la aplicación.
 * Permite cambiar entre los diferentes módulos de la aplicación.
 */
public class BarraNavegacion extends JPanel {
    
    private JPanel panelContenido;
    private String usuarioActual;
    private CardLayout cardLayout;
    private Map<String, JPanel> opcionesMenu;
    private Map<String, JLabel> etiquetasMenu;
    
    /**
     * Constructor de la barra de navegación.
     * @param panelContenido Panel donde se mostrarán los contenidos
     * @param usuarioActual Nombre del usuario actual
     */
    public BarraNavegacion(JPanel panelContenido, String usuarioActual) {
        this.panelContenido = panelContenido;
        this.usuarioActual = usuarioActual;
        // Verificar que el panel de contenido tenga un CardLayout
        if (!(panelContenido.getLayout() instanceof CardLayout)) {
            panelContenido.setLayout(new CardLayout());
        }
        this.cardLayout = (CardLayout) panelContenido.getLayout();
        this.opcionesMenu = new HashMap<>();
        this.etiquetasMenu = new HashMap<>();
        
        configurarUI();
        inicializarComponentes();
    }
    
    private void configurarUI() {
        setLayout(new BorderLayout());
        setBackground(EstilosUI.COLOR_PRINCIPAL);
        setPreferredSize(new Dimension(0, 60));
    }
    
    private void inicializarComponentes() {
        // Panel para el logo y el nombre del parque
        JPanel panelLogo = new JPanel(new BorderLayout());
        panelLogo.setOpaque(false);
        JLabel labelLogo = new JLabel("ParqueAtracciones");
        labelLogo.setFont(new Font("Roboto", Font.BOLD, 18));
        labelLogo.setForeground(Color.WHITE);
        labelLogo.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        panelLogo.add(labelLogo, BorderLayout.CENTER);
        
        // Panel para las opciones del menú
        JPanel panelMenu = new JPanel();
        panelMenu.setOpaque(false);
        panelMenu.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0));
        
        // Agregar opciones de menú
        agregarOpcionMenu(panelMenu, "Inicio", "inicio");
        agregarOpcionMenu(panelMenu, "Atracciones", "atracciones");
        agregarOpcionMenu(panelMenu, "Empleados", "empleados");
        agregarOpcionMenu(panelMenu, "Clientes", "clientes");
        agregarOpcionMenu(panelMenu, "Tiquetes", "tiquetes");
        
        // Por defecto seleccionar "Inicio"
        seleccionarOpcion("inicio");
        
        // Panel para información de usuario
        JPanel panelUsuario = new JPanel(new BorderLayout());
        panelUsuario.setOpaque(false);
        JLabel labelUsuario = new JLabel(usuarioActual);
        labelUsuario.setFont(EstilosUI.FUENTE_NORMAL);
        labelUsuario.setForeground(Color.WHITE);
        labelUsuario.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        panelUsuario.add(labelUsuario, BorderLayout.CENTER);
        
        // Agregar componentes a la barra
        add(panelLogo, BorderLayout.WEST);
        add(panelMenu, BorderLayout.CENTER);
        add(panelUsuario, BorderLayout.EAST);
    }
    
    private void agregarOpcionMenu(JPanel panelMenu, String titulo, String idPanel) {
        JPanel opcion = new JPanel(new BorderLayout());
        opcion.setOpaque(false);
        opcion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setFont(EstilosUI.FUENTE_NORMAL);
        etiqueta.setForeground(Color.WHITE);
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        etiqueta.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        
        opcion.add(etiqueta, BorderLayout.CENTER);
        
        opcion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarOpcion(idPanel);
                try {
                    // Verificar que el panel exista antes de mostrarlo
                    boolean panelExiste = false;
                    for (Component comp : panelContenido.getComponents()) {
                        if (comp.isVisible() && comp.getName() != null && comp.getName().equals(idPanel)) {
                            panelExiste = true;
                            break;
                        }
                    }
                    
                    if (panelExiste) {
                        cardLayout.show(panelContenido, idPanel);
                    } else {
                        System.err.println("No se encontró el panel: " + idPanel);
                    }
                } catch (Exception ex) {
                    System.err.println("Error al cambiar al panel: " + idPanel);
                    ex.printStackTrace();
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!opcionesMenu.get(idPanel).isOpaque()) {
                    opcion.setBackground(new Color(0x1976D2));
                    opcion.setOpaque(true);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!opcionesMenu.get(idPanel).isOpaque()) {
                    opcion.setOpaque(false);
                }
            }
        });
        
        panelMenu.add(opcion);
        opcionesMenu.put(idPanel, opcion);
        etiquetasMenu.put(idPanel, etiqueta);
    }    private void seleccionarOpcion(String idPanel) {
        opcionesMenu.forEach((id, panel) -> {
            if (id.equals(idPanel)) {
                panel.setBackground(new Color(0x1976D2));
                panel.setOpaque(true);
                etiquetasMenu.get(id).setFont(new Font("Roboto", Font.BOLD, 14));
            } else {
                panel.setOpaque(false);
                etiquetasMenu.get(id).setFont(EstilosUI.FUENTE_NORMAL);
            }
        });
        
        repaint();
    }
    
    /**
     * Oculta una opción del menú por su identificador.
     * 
     * @param idPanel Identificador de la opción a ocultar
     */
    public void ocultarOpcion(String idPanel) {
        if (opcionesMenu.containsKey(idPanel)) {
            opcionesMenu.get(idPanel).setVisible(false);
        }
    }
}

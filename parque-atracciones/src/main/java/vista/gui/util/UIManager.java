package vista.gui.util;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import vista.gui.componentes.BarraNavegacion;
import vista.gui.componentes.ComponentesPersonalizados;
import vista.gui.paneles.*;

/**
 * Gestor de interfaces de usuario basadas en roles.
 * Crea diferentes layouts y configuraciones según el tipo de usuario.
 */
public class UIManager {
    
    private String rolUsuario;
    private String nombreUsuario;
    
    /**
     * Constructor del gestor de interfaces.
     * 
     * @param rolUsuario Rol del usuario (Administrador, Empleado, Cliente)
     * @param nombreUsuario Nombre del usuario para mostrar en la interfaz
     */
    public UIManager(String rolUsuario, String nombreUsuario) {
        this.rolUsuario = rolUsuario;
        this.nombreUsuario = nombreUsuario;
    }
    
    /**
     * Crea el panel principal adaptado al rol del usuario.
     * 
     * @return Panel principal con la interfaz específica para el rol
     */
    public JPanel crearPanelPrincipal() {
        switch (rolUsuario) {
            case "Administrador":
                return crearPanelAdministrador();
            case "Empleado":
                return crearPanelEmpleado();
            case "Cliente":
                return crearPanelCliente();
            default:
                throw new IllegalArgumentException("Rol no válido: " + rolUsuario);
        }
    }
    
    /**
     * Crea el panel para administradores con acceso completo.
     * 
     * @return Panel configurado para administradores
     */
    private JPanel crearPanelAdministrador() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        
        // Panel de contenido con CardLayout para cambiar entre vistas
        JPanel panelContenido = new JPanel(new CardLayout());
        panelContenido.setBackground(EstilosUI.COLOR_FONDO);
        
        // Inicializar todos los paneles administrativos
        PanelInicio panelInicio = new PanelInicio();
        PanelAtracciones panelAtracciones = new PanelAtracciones();
        PanelEmpleados panelEmpleados = new PanelEmpleados();
        PanelClientes panelClientes = new PanelClientes();
        PanelTiquetes panelTiquetes = new PanelTiquetes();
        
        // Panel adicional de reportes y estadísticas para administradores
        JPanel panelReportes = crearPanelReportes();
        
        // Establecer nombres únicos para los paneles
        panelInicio.setName("inicio");
        panelAtracciones.setName("atracciones");
        panelEmpleados.setName("empleados");
        panelClientes.setName("clientes");
        panelTiquetes.setName("tiquetes");
        panelReportes.setName("reportes");
        
        // Agregar paneles al CardLayout
        panelContenido.add(panelInicio, "inicio");
        panelContenido.add(panelAtracciones, "atracciones");
        panelContenido.add(panelEmpleados, "empleados");
        panelContenido.add(panelClientes, "clientes");
        panelContenido.add(panelTiquetes, "tiquetes");
        panelContenido.add(panelReportes, "reportes");
        
        // Barra de navegación administrativa
        BarraNavegacion barraNavegacion = new BarraNavegacionAdministrador(panelContenido, nombreUsuario);
        
        // Agregar componentes al panel principal
        panelPrincipal.add(barraNavegacion, BorderLayout.NORTH);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        
        return panelPrincipal;
    }
    
    /**
     * Crea el panel para empleados con funcionalidades limitadas.
     * 
     * @return Panel configurado para empleados
     */
    private JPanel crearPanelEmpleado() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        
        // Panel de contenido con CardLayout para cambiar entre vistas
        JPanel panelContenido = new JPanel(new CardLayout());
        panelContenido.setBackground(EstilosUI.COLOR_FONDO);
        
        // Inicializar paneles relevantes para empleados
        PanelInicio panelInicio = new PanelInicio();
        PanelAtracciones panelAtracciones = new PanelAtracciones();
        PanelTiquetes panelTiquetes = new PanelTiquetes();
        PanelClientes panelClientes = new PanelClientes();
        
        // Establecer nombres únicos para los paneles
        panelInicio.setName("inicio");
        panelAtracciones.setName("atracciones");
        panelClientes.setName("clientes");
        panelTiquetes.setName("tiquetes");
        
        // Agregar paneles al CardLayout
        panelContenido.add(panelInicio, "inicio");
        panelContenido.add(panelAtracciones, "atracciones");
        panelContenido.add(panelClientes, "clientes");
        panelContenido.add(panelTiquetes, "tiquetes");
        
        // Barra de navegación para empleados
        BarraNavegacion barraNavegacion = new BarraNavegacionEmpleado(panelContenido, nombreUsuario);
        
        // Agregar componentes al panel principal
        panelPrincipal.add(barraNavegacion, BorderLayout.NORTH);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        
        return panelPrincipal;
    }
    
    /**
     * Crea el panel para clientes con interfaz simplificada.
     * 
     * @return Panel configurado para clientes
     */
    private JPanel crearPanelCliente() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        
        // Panel de contenido con CardLayout para cambiar entre vistas
        JPanel panelContenido = new JPanel(new CardLayout());
        panelContenido.setBackground(EstilosUI.COLOR_FONDO);
        
        // Inicializar paneles relevantes para clientes
        PanelInicio panelInicio = new PanelInicio();
        // Versión especial del panel de atracciones para clientes
        PanelAtraccionesCliente panelAtracciones = new PanelAtraccionesCliente();
        // Panel de tiquetes personalizado para clientes
        PanelTiquetesCliente panelTiquetes = new PanelTiquetesCliente();
        // Panel de perfil para clientes
        JPanel panelPerfil = crearPanelPerfilCliente();
        
        // Establecer nombres únicos para los paneles
        panelInicio.setName("inicio");
        panelAtracciones.setName("atracciones");
        panelTiquetes.setName("tiquetes");
        panelPerfil.setName("perfil");
        
        // Agregar paneles al CardLayout
        panelContenido.add(panelInicio, "inicio");
        panelContenido.add(panelAtracciones, "atracciones");
        panelContenido.add(panelTiquetes, "tiquetes");
        panelContenido.add(panelPerfil, "perfil");
        
        // Barra de navegación para clientes
        BarraNavegacion barraNavegacion = new BarraNavegacionCliente(panelContenido, nombreUsuario);
        
        // Agregar componentes al panel principal
        panelPrincipal.add(barraNavegacion, BorderLayout.NORTH);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        
        return panelPrincipal;
    }
    
    /**
     * Crea un panel de reportes y estadísticas para administradores.
     * 
     * @return Panel de reportes
     */
    private JPanel crearPanelReportes() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(EstilosUI.COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título del panel
        JLabel labelTitulo = ComponentesPersonalizados.crearEtiquetaTitulo("Reportes y Estadísticas");
        panel.add(labelTitulo, BorderLayout.NORTH);
        
        // Panel principal con Grid para las tarjetas de reportes
        JPanel panelGrid = new JPanel(new GridBagLayout());
        panelGrid.setOpaque(false);
        
        // Crear tarjetas de reportes
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        // Primera fila
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelGrid.add(crearTarjetaReporte("Ingresos Diarios", "$1,250.00", "10% ↑"), gbc);
        
        gbc.gridx = 1;
        panelGrid.add(crearTarjetaReporte("Visitantes Hoy", "250", "15% ↑"), gbc);
        
        gbc.gridx = 2;
        panelGrid.add(crearTarjetaReporte("Tiquetes Vendidos", "180", "5% ↑"), gbc);
        
        // Segunda fila
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelGrid.add(crearTarjetaReporte("Atracciones Activas", "12/15", ""), gbc);
        
        gbc.gridx = 1;
        panelGrid.add(crearTarjetaReporte("Empleados Activos", "25", ""), gbc);
        
        gbc.gridx = 2;
        panelGrid.add(crearTarjetaReporte("Satisfacción", "92%", "3% ↑"), gbc);
        
        panel.add(panelGrid, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Crea una tarjeta visual para mostrar un reporte estadístico.
     * 
     * @param titulo Título del reporte
     * @param valor Valor principal a mostrar
     * @param tendencia Tendencia (puede estar vacío)
     * @return Panel con la tarjeta de reporte
     */
    private JPanel crearTarjetaReporte(String titulo, String valor, String tendencia) {
        JPanel panel = ComponentesPersonalizados.crearPanelRedondeado();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(250, 150));
        
        // Título
        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(EstilosUI.FUENTE_SUBTITULO);
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Valor principal
        JLabel labelValor = new JLabel(valor);
        labelValor.setFont(new Font("Roboto", Font.BOLD, 32));
        labelValor.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(Box.createVerticalStrut(10));
        panel.add(labelTitulo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(labelValor);
        
        // Tendencia si existe
        if (!tendencia.isEmpty()) {
            JLabel labelTendencia = new JLabel(tendencia);
            labelTendencia.setAlignmentX(Component.CENTER_ALIGNMENT);
            labelTendencia.setFont(EstilosUI.FUENTE_NORMAL);
            
            // Color según tendencia
            if (tendencia.contains("↑")) {
                labelTendencia.setForeground(EstilosUI.COLOR_EXITO);
            } else if (tendencia.contains("↓")) {
                labelTendencia.setForeground(EstilosUI.COLOR_ERROR);
            }
            
            panel.add(Box.createVerticalStrut(10));
            panel.add(labelTendencia);
        }
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    /**
     * Crea un panel de perfil para clientes.
     * 
     * @return Panel de perfil de cliente
     */
    private JPanel crearPanelPerfilCliente() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(EstilosUI.COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título del panel
        JLabel labelTitulo = ComponentesPersonalizados.crearEtiquetaTitulo("Mi Perfil");
        panel.add(labelTitulo, BorderLayout.NORTH);
        
        // Panel con información del perfil
        JPanel panelInfo = ComponentesPersonalizados.crearPanelRedondeado();
        panelInfo.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        // Datos personales
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel labelSubtitulo = ComponentesPersonalizados.crearEtiquetaSubtitulo("Datos Personales");
        panelInfo.add(labelSubtitulo, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy++;
        panelInfo.add(new JLabel("Nombre:"), gbc);
        
        gbc.gridx = 1;
        panelInfo.add(new JLabel(nombreUsuario), gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        panelInfo.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        panelInfo.add(new JLabel("usuario@ejemplo.com"), gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        panelInfo.add(new JLabel("Teléfono:"), gbc);
        
        gbc.gridx = 1;
        panelInfo.add(new JLabel("555-123-4567"), gbc);
        
        // Historial de visitas
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 5, 10);
        JLabel labelHistorial = ComponentesPersonalizados.crearEtiquetaSubtitulo("Historial de Visitas");
        panelInfo.add(labelHistorial, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(5, 10, 5, 10);
        panelInfo.add(new JLabel("Última visita: 15/05/2025"), gbc);
        
        gbc.gridy++;
        panelInfo.add(new JLabel("Total de visitas: 3"), gbc);
        
        panel.add(panelInfo, BorderLayout.CENTER);
        
        return panel;
    }
      /**
     * Clase interna para la barra de navegación de Administrador.
     */
    private class BarraNavegacionAdministrador extends BarraNavegacion {
        public BarraNavegacionAdministrador(JPanel panelContenido, String usuarioActual) {
            super(panelContenido, usuarioActual);
            
            // Agregar opción exclusiva de reportes para administradores
            JPanel panelMenu = new JPanel();
            panelMenu.setOpaque(false);
            panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.X_AXIS));
            
            // Crear botón de reportes manualmente
            JPanel opcionReportes = new JPanel(new BorderLayout());
            opcionReportes.setOpaque(false);
            opcionReportes.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            JLabel etiquetaReportes = new JLabel("Reportes");
            etiquetaReportes.setFont(new Font("Roboto", Font.PLAIN, 14));
            etiquetaReportes.setForeground(Color.WHITE);
            etiquetaReportes.setHorizontalAlignment(SwingConstants.CENTER);
            etiquetaReportes.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
            
            opcionReportes.add(etiquetaReportes, BorderLayout.CENTER);
            panelMenu.add(opcionReportes);
            add(panelMenu, BorderLayout.EAST);
        }
    }
    
    /**
     * Clase interna para la barra de navegación de Empleado.
     */
    private class BarraNavegacionEmpleado extends BarraNavegacion {
        public BarraNavegacionEmpleado(JPanel panelContenido, String usuarioActual) {
            super(panelContenido, usuarioActual);
            
            // No mostrar opciones de administración para empleados
            ocultarOpcion("empleados");
        }
    }
    
    /**
     * Clase interna para la barra de navegación de Cliente.
     */
    private class BarraNavegacionCliente extends BarraNavegacion {
        public BarraNavegacionCliente(JPanel panelContenido, String usuarioActual) {
            super(panelContenido, usuarioActual);
            
            // Ocultar opciones administrativas
            ocultarOpcion("empleados");
            ocultarOpcion("clientes");
            
            // Agregar opción de perfil para clientes
            JPanel panelMenu = new JPanel();
            panelMenu.setOpaque(false);
            panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.X_AXIS));
            
            // Crear botón de perfil manualmente
            JPanel opcionPerfil = new JPanel(new BorderLayout());
            opcionPerfil.setOpaque(false);
            opcionPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            JLabel etiquetaPerfil = new JLabel("Mi Perfil");
            etiquetaPerfil.setFont(new Font("Roboto", Font.PLAIN, 14));
            etiquetaPerfil.setForeground(Color.WHITE);
            etiquetaPerfil.setHorizontalAlignment(SwingConstants.CENTER);
            etiquetaPerfil.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
            
            opcionPerfil.add(etiquetaPerfil, BorderLayout.CENTER);
            panelMenu.add(opcionPerfil);
            add(panelMenu, BorderLayout.EAST);
        }
    }
}

package vista.gui.paneles;

import vista.gui.util.EstilosUI;
import vista.gui.componentes.ComponentesPersonalizados;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Panel principal que muestra el dashboard con información resumida
 * sobre el estado del parque de atracciones.
 */
public class PanelInicio extends JPanel {
    
    private JPanel panelKPIs;
    private JPanel panelVentas;
    private JPanel panelAtracciones;
    
    /**
     * Constructor del panel de inicio (dashboard)
     */
    public PanelInicio() {
        configurarUI();
        inicializarComponentes();
    }
    
    private void configurarUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(EstilosUI.COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
    
    private void inicializarComponentes() {
        // Panel superior para fecha y título
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        
        JLabel labelTitulo = ComponentesPersonalizados.crearEtiquetaTitulo("Dashboard");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy");
        String fechaFormateada = LocalDate.now().format(formatter);
        JLabel labelFecha = new JLabel(fechaFormateada);
        labelFecha.setFont(EstilosUI.FUENTE_NORMAL);
        labelFecha.setForeground(EstilosUI.COLOR_TEXTO);
        
        panelSuperior.add(labelTitulo, BorderLayout.WEST);
        panelSuperior.add(labelFecha, BorderLayout.EAST);
        
        // Panel con los indicadores clave (KPIs)
        panelKPIs = crearPanelKPIs();
        
        // Panel central que contiene ventas y estado de atracciones
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 15, 0));
        panelCentral.setOpaque(false);
        
        panelVentas = crearPanelVentas();
        panelAtracciones = crearPanelAtracciones();
        
        panelCentral.add(panelVentas);
        panelCentral.add(panelAtracciones);
        
        // Panel de notificaciones no se está utilizando actualmente
        // JPanel panelNotificaciones = crearPanelNotificaciones();
        
        // Agregar todos los paneles al layout principal
        add(panelSuperior, BorderLayout.NORTH);
        add(panelKPIs, BorderLayout.SOUTH);
        add(panelCentral, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelKPIs() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setOpaque(false);
        
        // KPI 1: Visitantes hoy
        panel.add(crearTarjetaKPI("Visitantes Hoy", "1,245", "↑ 12% vs ayer", EstilosUI.COLOR_PRINCIPAL));
        
        // KPI 2: Tiquetes vendidos
        panel.add(crearTarjetaKPI("Tiquetes Vendidos", "3,890", "↑ 5% vs semana anterior", EstilosUI.COLOR_EXITO));
        
        // KPI 3: Atracciones activas
        panel.add(crearTarjetaKPI("Atracciones Activas", "18/20", "2 en mantenimiento", EstilosUI.COLOR_SECUNDARIO));
        
        // KPI 4: Empleados presentes
        panel.add(crearTarjetaKPI("Empleados Presentes", "45/50", "90% asistencia", EstilosUI.COLOR_PRINCIPAL.darker()));
        
        return panel;
    }
    
    private JPanel crearTarjetaKPI(String titulo, String valor, String subtexto, Color color) {
        JPanel panel = ComponentesPersonalizados.crearPanelRedondeado();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(EstilosUI.FUENTE_PEQUEÑA);
        labelTitulo.setForeground(EstilosUI.COLOR_TEXTO);
        
        JLabel labelValor = new JLabel(valor);
        labelValor.setFont(new Font("Roboto", Font.BOLD, 24));
        labelValor.setForeground(color);
        
        JLabel labelSubtexto = new JLabel(subtexto);
        labelSubtexto.setFont(EstilosUI.FUENTE_PEQUEÑA);
        labelSubtexto.setForeground(new Color(0x757575));
        
        JPanel panelInfo = new JPanel(new GridLayout(2, 1));
        panelInfo.setOpaque(false);
        panelInfo.add(labelValor);
        panelInfo.add(labelSubtexto);
        
        panel.add(labelTitulo, BorderLayout.NORTH);
        panel.add(panelInfo, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelVentas() {
        JPanel panel = ComponentesPersonalizados.crearPanelRedondeado();
        panel.setLayout(new BorderLayout(0, 10));
        
        JLabel labelTitulo = ComponentesPersonalizados.crearEtiquetaSubtitulo("Ventas Diarias");
        
        // Panel para gráfico (simulado)
        JPanel panelGrafico = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Ejes
                g2d.setColor(EstilosUI.COLOR_BORDE);
                g2d.drawLine(50, getHeight() - 30, getWidth() - 20, getHeight() - 30); // eje X
                g2d.drawLine(50, 20, 50, getHeight() - 30); // eje Y
                
                // Datos (simulados)
                int[] datos = {120, 150, 80, 200, 170, 190, 220};
                int maxDato = 250;
                int anchoColumna = (getWidth() - 80) / datos.length;
                
                for (int i = 0; i < datos.length; i++) {
                    int altura = (int) (((float) datos[i] / maxDato) * (getHeight() - 60));
                    int x = 60 + (i * anchoColumna);
                    int y = getHeight() - 30 - altura;
                    
                    g2d.setColor(EstilosUI.COLOR_PRINCIPAL);
                    g2d.fillRect(x, y, anchoColumna - 10, altura);
                    
                    // Etiquetas eje X (días)
                    g2d.setColor(EstilosUI.COLOR_TEXTO);
                    g2d.setFont(EstilosUI.FUENTE_PEQUEÑA);
                    String dia = String.valueOf(i + 1);
                    g2d.drawString(dia, x + (anchoColumna / 2) - 5, getHeight() - 10);
                }
                
                // Etiquetas eje Y
                g2d.setColor(EstilosUI.COLOR_TEXTO);
                g2d.drawString("0", 35, getHeight() - 25);
                g2d.drawString("100", 25, getHeight() / 2);
                g2d.drawString("200+", 25, 30);
            }
        };
        panelGrafico.setPreferredSize(new Dimension(0, 200));
        
        // Leyenda
        JPanel panelLeyenda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelLeyenda.setOpaque(false);
        
        JLabel labelLeyenda = new JLabel("Ventas diarias (últimos 7 días)");
        labelLeyenda.setFont(EstilosUI.FUENTE_PEQUEÑA);
        panelLeyenda.add(labelLeyenda);
        
        panel.add(labelTitulo, BorderLayout.NORTH);
        panel.add(panelGrafico, BorderLayout.CENTER);
        panel.add(panelLeyenda, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelAtracciones() {
        JPanel panel = ComponentesPersonalizados.crearPanelRedondeado();
        panel.setLayout(new BorderLayout(0, 10));
        
        JLabel labelTitulo = ComponentesPersonalizados.crearEtiquetaSubtitulo("Estado de Atracciones");
        
        // Tabla de atracciones
        String[] columnas = {"Atracción", "Estado", "Espera"};
        Object[][] datos = {
            {"Montaña Rusa", "Activa", "45 min"},
            {"Carrusel", "Activa", "10 min"},
            {"Casa Embrujada", "Activa", "30 min"},
            {"Rueda de la Fortuna", "Mantenimiento", "N/A"},
            {"Splash Mountain", "Activa", "60 min"},
            {"Nave Espacial", "Activa", "25 min"}
        };
        
        JTable tabla = new JTable(datos, columnas);
        ComponentesPersonalizados.configurarTabla(tabla);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(null);
        
        panel.add(labelTitulo, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
}

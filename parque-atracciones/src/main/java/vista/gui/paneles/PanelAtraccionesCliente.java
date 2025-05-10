package vista.gui.paneles;

import vista.gui.componentes.ComponentesPersonalizados;
import vista.gui.util.EstilosUI;
import vista.gui.util.NotificacionUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel de atracciones específico para clientes.
 * Muestra las atracciones del parque con opciones para ver detalles y comprar tiquetes.
 */
public class PanelAtraccionesCliente extends JPanel {
    
    private JTable tablaAtracciones;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboFiltroTipo;
    private JComboBox<String> comboFiltroEstado;
    private JTextField campoBusqueda;
    private JButton botonFiltrar;
    
    /**
     * Constructor del panel de atracciones para clientes
     */
    public PanelAtraccionesCliente() {
        configurarUI();
        inicializarComponentes();
    }
    
    private void configurarUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(EstilosUI.COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
    
    private void inicializarComponentes() {
        // Panel superior con título
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        
        JLabel labelTitulo = ComponentesPersonalizados.crearEtiquetaTitulo("Atracciones del Parque");
        panelSuperior.add(labelTitulo, BorderLayout.WEST);
        
        // Panel de filtros
        JPanel panelFiltros = crearPanelFiltros();
        
        // Panel con información de atracciones destacadas
        JPanel panelDestacadas = crearPanelDestacadas();
        
        // Tabla de atracciones
        JPanel panelTabla = crearPanelTabla();
        
        // Panel central con layout tipo BoxLayout vertical
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setOpaque(false);
        
        panelCentral.add(panelDestacadas);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 15)));
        panelCentral.add(panelTabla);
        
        // Agregar componentes al panel principal
        add(panelSuperior, BorderLayout.NORTH);
        add(panelFiltros, BorderLayout.SOUTH);
        add(panelCentral, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelFiltros() {
        JPanel panel = ComponentesPersonalizados.crearPanelRedondeado();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setPreferredSize(new Dimension(0, 60));
        
        JLabel labelFiltroTipo = new JLabel("Tipo:");
        labelFiltroTipo.setFont(EstilosUI.FUENTE_NORMAL);
        
        comboFiltroTipo = new JComboBox<>(new String[]{
            "Todas", "Mecánicas", "Acuáticas", "Infantiles", "Extremas"
        });
        comboFiltroTipo.setFont(EstilosUI.FUENTE_NORMAL);
        comboFiltroTipo.setPreferredSize(new Dimension(150, 35));
        
        JLabel labelFiltroEstado = new JLabel("Estado:");
        labelFiltroEstado.setFont(EstilosUI.FUENTE_NORMAL);
        
        comboFiltroEstado = new JComboBox<>(new String[]{"Todas", "Disponible", "Mantenimiento"});
        comboFiltroEstado.setFont(EstilosUI.FUENTE_NORMAL);
        comboFiltroEstado.setPreferredSize(new Dimension(150, 35));
        
        JLabel labelBusqueda = new JLabel("Buscar:");
        labelBusqueda.setFont(EstilosUI.FUENTE_NORMAL);
        
        campoBusqueda = ComponentesPersonalizados.crearCampoTexto();
        campoBusqueda.setPreferredSize(new Dimension(200, 35));
        
        botonFiltrar = ComponentesPersonalizados.crearBotonSecundario("Filtrar");
        botonFiltrar.addActionListener(_ -> aplicarFiltros());
        
        panel.add(labelFiltroTipo);
        panel.add(comboFiltroTipo);
        panel.add(labelFiltroEstado);
        panel.add(comboFiltroEstado);
        panel.add(labelBusqueda);
        panel.add(campoBusqueda);
        panel.add(botonFiltrar);
        
        return panel;
    }
    
    private JPanel crearPanelDestacadas() {
        JPanel panel = ComponentesPersonalizados.crearPanelRedondeado();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(0, 180));
        
        JLabel labelSubtitulo = ComponentesPersonalizados.crearEtiquetaSubtitulo("Atracciones Destacadas");
        panel.add(labelSubtitulo, BorderLayout.NORTH);
        
        // Panel para las tarjetas de atracciones destacadas
        JPanel panelTarjetas = new JPanel(new GridLayout(1, 3, 15, 0));
        panelTarjetas.setOpaque(false);
        
        // Agregar tarjetas de atracciones destacadas
        panelTarjetas.add(crearTarjetaAtraccion("Montaña Rusa", "Extrema", "La atracción más emocionante del parque"));
        panelTarjetas.add(crearTarjetaAtraccion("Río Salvaje", "Acuática", "Diversión asegurada para toda la familia"));
        panelTarjetas.add(crearTarjetaAtraccion("Torre del Terror", "Mecánica", "¿Te atreves a caer desde 50 metros?"));
        
        panel.add(panelTarjetas, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearTarjetaAtraccion(String nombre, String tipo, String descripcion) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(EstilosUI.COLOR_FONDO);
        panel.setBorder(BorderFactory.createLineBorder(EstilosUI.COLOR_BORDE));
        
        JLabel labelNombre = new JLabel(nombre);
        labelNombre.setFont(EstilosUI.FUENTE_SUBTITULO);
        labelNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel labelTipo = new JLabel(tipo);
        labelTipo.setFont(EstilosUI.FUENTE_PEQUEÑA);
        labelTipo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextArea areaDescripcion = new JTextArea(descripcion);
        areaDescripcion.setWrapStyleWord(true);
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setEditable(false);
        areaDescripcion.setFont(EstilosUI.FUENTE_PEQUEÑA);
        areaDescripcion.setBackground(EstilosUI.COLOR_FONDO);
        areaDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
        areaDescripcion.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JButton botonComprar = ComponentesPersonalizados.crearBotonPrimario("Comprar Tiquete");
        botonComprar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonComprar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(PanelAtraccionesCliente.this,
                        "Redirigiendo a la compra de tiquetes para " + nombre,
                        "Comprar Tiquete", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        panel.add(Box.createVerticalStrut(10));
        panel.add(labelNombre);
        panel.add(Box.createVerticalStrut(5));
        panel.add(labelTipo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(areaDescripcion);
        panel.add(Box.createVerticalStrut(10));
        panel.add(botonComprar);
        panel.add(Box.createVerticalStrut(10));
        
        return panel;
    }
    
    private JPanel crearPanelTabla() {
        JPanel panel = ComponentesPersonalizados.crearPanelRedondeado();
        panel.setLayout(new BorderLayout());
        
        // Título de la sección
        JLabel labelSubtitulo = ComponentesPersonalizados.crearEtiquetaSubtitulo("Todas las Atracciones");
        panel.add(labelSubtitulo, BorderLayout.NORTH);
        
        // Crear modelo de tabla
        String[] columnas = {"Nombre", "Tipo", "Capacidad", "Tiempo", "Restricciones", "Estado", "Acciones"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Solo la columna de acciones es editable
            }
        };
        
        // Cargar datos de prueba
        cargarDatosPrueba();
        
        // Crear tabla
        tablaAtracciones = new JTable(modeloTabla);
        ComponentesPersonalizados.configurarTabla(tablaAtracciones);
        
        // Configurar renderizador para la columna de acciones
        tablaAtracciones.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        tablaAtracciones.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(tablaAtracciones);
        scrollPane.setBorder(null);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void cargarDatosPrueba() {
        // En una implementación real, estos datos vendrían del servicio
        Object[][] datos = {
            {"Montaña Rusa", "Extrema", "24 personas", "3 min", "Altura mín. 1.40m", "Disponible", ""},
            {"Río Salvaje", "Acuática", "8 personas", "5 min", "Ninguna", "Disponible", ""},
            {"Torre del Terror", "Mecánica", "16 personas", "2 min", "Altura mín. 1.30m", "Mantenimiento", ""},
            {"Carrusel", "Infantil", "20 personas", "4 min", "Ninguna", "Disponible", ""},
            {"Tren Fantasma", "Mecánica", "20 personas", "8 min", "No recomendado -10 años", "Disponible", ""},
            {"Caída Libre", "Extrema", "12 personas", "2 min", "Altura mín. 1.50m", "Disponible", ""}
        };
        
        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }
    }
    
    private void aplicarFiltros() {
        // Aquí se implementaría la lógica de filtrado real
        NotificacionUI.mostrarNotificacion(this, 
                "Filtros aplicados con éxito", 
                NotificacionUI.TipoNotificacion.EXITO);
    }
    
    // Clases para renderizar botones en la tabla
    
    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            // Solo mostrar el botón "Ver" y "Comprar" si la atracción está disponible
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(true);
            
            JButton btnVer = new JButton("Ver");
            btnVer.setFont(EstilosUI.FUENTE_PEQUEÑA);
            panel.add(btnVer);
            
            String estado = (String) table.getValueAt(row, 5);
            if ("Disponible".equals(estado)) {
                JButton btnComprar = new JButton("Comprar");
                btnComprar.setFont(EstilosUI.FUENTE_PEQUEÑA);
                panel.add(btnComprar);
            }
            
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }
            
            return panel;
        }
    }
    
    private class ButtonEditor extends DefaultCellEditor {
        protected JPanel panel;
        protected JButton btnVer;
        protected JButton btnComprar;
        protected String nombreAtraccion;
        protected String estadoAtraccion;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            
            btnVer = new JButton("Ver");
            btnVer.setFont(EstilosUI.FUENTE_PEQUEÑA);
            btnVer.addActionListener(_ -> verDetallesAtraccion());
            
            btnComprar = new JButton("Comprar");
            btnComprar.setFont(EstilosUI.FUENTE_PEQUEÑA);
            btnComprar.addActionListener(_ -> comprarTiquete());
            
            panel.add(btnVer);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            
            nombreAtraccion = (String) table.getValueAt(row, 0);
            estadoAtraccion = (String) table.getValueAt(row, 5);
            
            panel.removeAll();
            panel.add(btnVer);
            
            if ("Disponible".equals(estadoAtraccion)) {
                panel.add(btnComprar);
            }
            
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }
            
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "";
        }
        
        private void verDetallesAtraccion() {
            JOptionPane.showMessageDialog(null, "Detalles de la atracción: " + nombreAtraccion, 
                    "Detalles", JOptionPane.INFORMATION_MESSAGE);
            fireEditingStopped();
        }
        
        private void comprarTiquete() {
            if (!"Disponible".equals(estadoAtraccion)) {
                JOptionPane.showMessageDialog(null, 
                        "Esta atracción no está disponible actualmente.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JOptionPane.showMessageDialog(null, "Redirigiendo a la compra de tiquetes para " + nombreAtraccion, 
                    "Comprar Tiquete", JOptionPane.INFORMATION_MESSAGE);
            fireEditingStopped();
        }
    }
}

package vista.gui.paneles;

import vista.gui.componentes.ComponentesPersonalizados;
import vista.gui.util.EstilosUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


/**
 * Panel para la gestión de atracciones del parque.
 * Permite visualizar, filtrar, editar y crear atracciones.
 */
public class PanelAtracciones extends JPanel {
    
    private JTable tablaAtracciones;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboFiltroEstado;
    private JComboBox<String> comboFiltroTipo;
    private JTextField campoBusqueda;
    private JButton botonNuevaAtraccion;
    private JButton botonFiltrar;
    
    /**
     * Constructor del panel de atracciones
     */
    public PanelAtracciones() {
        //servicioElementos = new ServicioGestionElementosParque(); // Esto debería inyectarse
        configurarUI();
        inicializarComponentes();
    }
    
    private void configurarUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(EstilosUI.COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
    
    private void inicializarComponentes() {
        // Panel superior con título y botón de nueva atracción
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        
        JLabel labelTitulo = ComponentesPersonalizados.crearEtiquetaTitulo("Gestión de Atracciones");
        
        botonNuevaAtraccion = ComponentesPersonalizados.crearBotonPrimario("Nueva Atracción");
        botonNuevaAtraccion.addActionListener(_ -> mostrarDialogoNuevaAtraccion());
        
        panelSuperior.add(labelTitulo, BorderLayout.WEST);
        panelSuperior.add(botonNuevaAtraccion, BorderLayout.EAST);
        
        // Panel de filtros
        JPanel panelFiltros = crearPanelFiltros();
        
        // Tabla de atracciones
        JPanel panelTabla = crearPanelTabla();
        
        // Panel de detalles (aparecerá al seleccionar una atracción)
        // Se crea pero no se agrega hasta que sea necesario
        // JPanel panelDetalles = crearPanelDetalles();
        
        // Agregar componentes al panel principal
        add(panelSuperior, BorderLayout.NORTH);
        add(panelFiltros, BorderLayout.SOUTH);
        add(panelTabla, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelFiltros() {
        JPanel panel = ComponentesPersonalizados.crearPanelRedondeado();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setPreferredSize(new Dimension(0, 60));
        
        JLabel labelFiltroEstado = new JLabel("Estado:");
        labelFiltroEstado.setFont(EstilosUI.FUENTE_NORMAL);
        
        comboFiltroEstado = new JComboBox<>(new String[]{"Todos", "Activa", "Mantenimiento", "Inactiva"});
        comboFiltroEstado.setFont(EstilosUI.FUENTE_NORMAL);
        comboFiltroEstado.setPreferredSize(new Dimension(150, 35));
        
        JLabel labelFiltroTipo = new JLabel("Tipo:");
        labelFiltroTipo.setFont(EstilosUI.FUENTE_NORMAL);
        
        comboFiltroTipo = new JComboBox<>(new String[]{"Todos", "Mecánica", "Cultural"});
        comboFiltroTipo.setFont(EstilosUI.FUENTE_NORMAL);
        comboFiltroTipo.setPreferredSize(new Dimension(150, 35));
        
        JLabel labelBusqueda = new JLabel("Buscar:");
        labelBusqueda.setFont(EstilosUI.FUENTE_NORMAL);
        
        campoBusqueda = ComponentesPersonalizados.crearCampoTexto();
        campoBusqueda.setPreferredSize(new Dimension(200, 35));
        
        botonFiltrar = ComponentesPersonalizados.crearBotonSecundario("Filtrar");
        botonFiltrar.addActionListener(_ -> aplicarFiltros());
        
        panel.add(labelFiltroEstado);
        panel.add(comboFiltroEstado);
        panel.add(labelFiltroTipo);
        panel.add(comboFiltroTipo);
        panel.add(labelBusqueda);
        panel.add(campoBusqueda);
        panel.add(botonFiltrar);
        
        return panel;
    }
    
    private JPanel crearPanelTabla() {
        JPanel panel = ComponentesPersonalizados.crearPanelRedondeado();
        panel.setLayout(new BorderLayout());
        
        // Crear modelo de tabla
        String[] columnas = {"ID", "Nombre", "Tipo", "Estado", "Capacidad", "Tiempo de Espera", "Acciones"};
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
            {"ATR001", "Montaña Rusa", "Mecánica", "Activa", "30", "45 min", ""},
            {"ATR002", "Carrusel", "Mecánica", "Activa", "20", "10 min", ""},
            {"ATR003", "Casa Embrujada", "Mecánica", "Mantenimiento", "25", "30 min", ""},
            {"ATR004", "Rueda de la Fortuna", "Mecánica", "Activa", "40", "15 min", ""},
            {"ATR005", "Teatro 4D", "Cultural", "Activa", "50", "20 min", ""},
            {"ATR006", "Exposición Marina", "Cultural", "Inactiva", "35", "N/A", ""}
        };
        
        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }
    }
    
    private void aplicarFiltros() {
        // Aquí se implementaría la lógica de filtrado real
        JOptionPane.showMessageDialog(this, 
                "Filtros aplicados:\nEstado: " + comboFiltroEstado.getSelectedItem() + 
                "\nTipo: " + comboFiltroTipo.getSelectedItem() + 
                "\nBúsqueda: " + campoBusqueda.getText(),
                "Filtros", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarDialogoNuevaAtraccion() {
        // Diálogo para crear una nueva atracción
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nueva Atracción", true);
        dialogo.setSize(500, 600);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());
        
        JPanel panelFormulario = new JPanel(new GridLayout(0, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Campos del formulario
        JLabel labelNombre = new JLabel("Nombre:");
        JTextField campoNombre = ComponentesPersonalizados.crearCampoTexto();
        
        JLabel labelTipo = new JLabel("Tipo:");
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Mecánica", "Cultural"});
        
        JLabel labelUbicacion = new JLabel("Ubicación:");
        JTextField campoUbicacion = ComponentesPersonalizados.crearCampoTexto();
        
        JLabel labelCapacidad = new JLabel("Capacidad:");
        JTextField campoCapacidad = ComponentesPersonalizados.crearCampoTexto();
        
        JLabel labelEmpleadosMin = new JLabel("Empleados Mínimos:");
        JTextField campoEmpleadosMin = ComponentesPersonalizados.crearCampoTexto();
        
        // Agregar campos al formulario
        panelFormulario.add(labelNombre);
        panelFormulario.add(campoNombre);
        panelFormulario.add(labelTipo);
        panelFormulario.add(comboTipo);
        panelFormulario.add(labelUbicacion);
        panelFormulario.add(campoUbicacion);
        panelFormulario.add(labelCapacidad);
        panelFormulario.add(campoCapacidad);
        panelFormulario.add(labelEmpleadosMin);
        panelFormulario.add(campoEmpleadosMin);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonCancelar = ComponentesPersonalizados.crearBotonSecundario("Cancelar");
        JButton botonGuardar = ComponentesPersonalizados.crearBotonPrimario("Guardar");
        
        botonCancelar.addActionListener(_ -> dialogo.dispose());
        botonGuardar.addActionListener(_ -> {
            // Aquí se guardaría la nueva atracción
            JOptionPane.showMessageDialog(dialogo, "Atracción guardada con éxito", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dialogo.dispose();
        });
        
        panelBotones.add(botonCancelar);
        panelBotones.add(botonGuardar);
        
        dialogo.add(panelFormulario, BorderLayout.CENTER);
        dialogo.add(panelBotones, BorderLayout.SOUTH);
        
        dialogo.setVisible(true);
    }
    
    // Clases para renderizar botones en la tabla
    
    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(true);
            
            JButton btnVer = new JButton("Ver");
            btnVer.setFont(EstilosUI.FUENTE_PEQUEÑA);
            
            JButton btnEditar = new JButton("Editar");
            btnEditar.setFont(EstilosUI.FUENTE_PEQUEÑA);
            
            panel.add(btnVer);
            panel.add(btnEditar);
            
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
        protected JButton btnEditar;
        protected String idAtraccion;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            
            btnVer = new JButton("Ver");
            btnVer.setFont(EstilosUI.FUENTE_PEQUEÑA);
            btnVer.addActionListener(_ -> verDetallesAtraccion());
            
            btnEditar = new JButton("Editar");
            btnEditar.setFont(EstilosUI.FUENTE_PEQUEÑA);
            btnEditar.addActionListener(_ -> editarAtraccion());
            
            panel.add(btnVer);
            panel.add(btnEditar);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            
            idAtraccion = (String) table.getValueAt(row, 0);
            
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
            JOptionPane.showMessageDialog(null, "Ver detalles de atracción: " + idAtraccion, 
                    "Detalles", JOptionPane.INFORMATION_MESSAGE);
            fireEditingStopped();
        }
        
        private void editarAtraccion() {
            JOptionPane.showMessageDialog(null, "Editar atracción: " + idAtraccion, 
                    "Editar", JOptionPane.INFORMATION_MESSAGE);
            fireEditingStopped();
        }
    }
}

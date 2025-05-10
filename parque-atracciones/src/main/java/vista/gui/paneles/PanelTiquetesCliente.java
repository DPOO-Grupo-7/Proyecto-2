package vista.gui.paneles;

import vista.gui.componentes.ComponentesPersonalizados;
import vista.gui.util.EstilosUI;
import vista.gui.util.NotificacionUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Panel de tiquetes específico para clientes.
 * Permite ver y gestionar los tiquetes comprados por el cliente.
 */
public class PanelTiquetesCliente extends JPanel {
    
    private JTable tablaTiquetes;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboFiltroEstado;
    private JButton botonComprarNuevo;
    private JButton botonFiltrar;
    
    /**
     * Constructor del panel de tiquetes para clientes
     */
    public PanelTiquetesCliente() {
        configurarUI();
        inicializarComponentes();
    }
    
    private void configurarUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(EstilosUI.COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
    
    private void inicializarComponentes() {
        // Panel superior con título y botón para comprar nuevo tiquete
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        
        JLabel labelTitulo = ComponentesPersonalizados.crearEtiquetaTitulo("Mis Tiquetes");
        
        botonComprarNuevo = ComponentesPersonalizados.crearBotonPrimario("Comprar Tiquete");
        botonComprarNuevo.addActionListener(_ -> mostrarDialogoComprarTiquete());
        
        panelSuperior.add(labelTitulo, BorderLayout.WEST);
        panelSuperior.add(botonComprarNuevo, BorderLayout.EAST);
        
        // Panel de filtros
        JPanel panelFiltros = crearPanelFiltros();
        
        // Panel de resumen
        JPanel panelResumen = crearPanelResumen();
        
        // Tabla de tiquetes
        JPanel panelTabla = crearPanelTabla();
        
        // Panel central con layout tipo BoxLayout vertical
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setOpaque(false);
        
        panelCentral.add(panelResumen);
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
        
        JLabel labelFiltroEstado = new JLabel("Estado:");
        labelFiltroEstado.setFont(EstilosUI.FUENTE_NORMAL);
        
        comboFiltroEstado = new JComboBox<>(new String[]{"Todos", "Activo", "Usado", "Vencido"});
        comboFiltroEstado.setFont(EstilosUI.FUENTE_NORMAL);
        comboFiltroEstado.setPreferredSize(new Dimension(150, 35));
        
        botonFiltrar = ComponentesPersonalizados.crearBotonSecundario("Filtrar");
        botonFiltrar.addActionListener(_ -> aplicarFiltros());
        
        panel.add(labelFiltroEstado);
        panel.add(comboFiltroEstado);
        panel.add(botonFiltrar);
        
        return panel;
    }
    
    private JPanel crearPanelResumen() {
        JPanel panel = ComponentesPersonalizados.crearPanelRedondeado();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(0, 120));
        
        JLabel labelSubtitulo = ComponentesPersonalizados.crearEtiquetaSubtitulo("Resumen de Tiquetes");
        panel.add(labelSubtitulo, BorderLayout.NORTH);
        
        // Panel para las tarjetas de resumen
        JPanel panelTarjetas = new JPanel(new GridLayout(1, 3, 15, 0));
        panelTarjetas.setOpaque(false);
        
        // Agregar tarjetas de resumen
        panelTarjetas.add(crearTarjetaResumen("Tiquetes Activos", "3", EstilosUI.COLOR_EXITO));
        panelTarjetas.add(crearTarjetaResumen("Tiquetes Usados", "5", EstilosUI.COLOR_SECUNDARIO));
        panelTarjetas.add(crearTarjetaResumen("Tiquetes Vencidos", "1", EstilosUI.COLOR_ERROR));
        
        panel.add(panelTarjetas, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearTarjetaResumen(String titulo, String valor, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(EstilosUI.COLOR_FONDO);
        panel.setBorder(BorderFactory.createLineBorder(color, 2));
        
        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setFont(EstilosUI.FUENTE_NORMAL);
        labelTitulo.setForeground(color);
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel labelValor = new JLabel(valor);
        labelValor.setFont(new Font("Roboto", Font.BOLD, 24));
        labelValor.setForeground(color);
        labelValor.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(Box.createVerticalStrut(10));
        panel.add(labelTitulo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(labelValor);
        panel.add(Box.createVerticalStrut(10));
        
        return panel;
    }
    
    private JPanel crearPanelTabla() {
        JPanel panel = ComponentesPersonalizados.crearPanelRedondeado();
        panel.setLayout(new BorderLayout());
        
        // Título de la sección
        JLabel labelSubtitulo = ComponentesPersonalizados.crearEtiquetaSubtitulo("Mis Tiquetes");
        panel.add(labelSubtitulo, BorderLayout.NORTH);
        
        // Crear modelo de tabla
        String[] columnas = {"ID", "Atracción", "Fecha Compra", "Fecha Validez", "Estado", "Acciones"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Solo la columna de acciones es editable
            }
        };
        
        // Cargar datos de prueba
        cargarDatosPrueba();
        
        // Crear tabla
        tablaTiquetes = new JTable(modeloTabla);
        ComponentesPersonalizados.configurarTabla(tablaTiquetes);
        
        // Configurar renderizador para la columna de acciones
        tablaTiquetes.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        tablaTiquetes.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(tablaTiquetes);
        scrollPane.setBorder(null);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void cargarDatosPrueba() {
        // En una implementación real, estos datos vendrían del servicio
        Object[][] datos = {
            {"T001", "Montaña Rusa", "15/05/2025", "15/06/2025", "Activo", ""},
            {"T002", "Río Salvaje", "15/05/2025", "15/06/2025", "Activo", ""},
            {"T003", "Tren Fantasma", "15/05/2025", "15/06/2025", "Activo", ""},
            {"T004", "Carrusel", "10/04/2025", "10/05/2025", "Usado", ""},
            {"T005", "Caída Libre", "10/04/2025", "10/05/2025", "Usado", ""},
            {"T006", "Montaña Rusa", "10/04/2025", "10/05/2025", "Usado", ""},
            {"T007", "Río Salvaje", "10/04/2025", "10/05/2025", "Usado", ""},
            {"T008", "Tren Fantasma", "10/04/2025", "10/05/2025", "Usado", ""},
            {"T009", "Torre del Terror", "01/03/2025", "01/04/2025", "Vencido", ""}
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
    
    private void mostrarDialogoComprarTiquete() {
        // Diálogo para comprar un nuevo tiquete
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Comprar Tiquete", true);
        dialogo.setSize(400, 450);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());
        
        JPanel panelFormulario = new JPanel(new GridLayout(0, 1, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel labelTitulo = ComponentesPersonalizados.crearEtiquetaTitulo("Comprar Tiquete");
        
        // Campos del formulario
        JLabel labelAtraccion = new JLabel("Atracción:");
        JComboBox<String> comboAtraccion = new JComboBox<>(new String[]{
            "Seleccione una atracción", "Montaña Rusa", "Río Salvaje", "Tren Fantasma", "Carrusel", "Caída Libre"
        });
        
        JLabel labelFecha = new JLabel("Fecha de visita:");
        JComboBox<String> comboFecha = new JComboBox<>(new String[]{
            "Seleccione una fecha", "Hoy", "Mañana", "En 3 días", "En una semana", "En dos semanas"
        });
        
        JLabel labelCantidad = new JLabel("Cantidad de tiquetes:");
        JSpinner spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        
        JLabel labelTotal = new JLabel("Total a pagar: $0.00");
        labelTotal.setFont(EstilosUI.FUENTE_SUBTITULO);
        
        // Actualizar total al cambiar la cantidad
        spinnerCantidad.addChangeListener(_ -> {
            int cantidad = (int) spinnerCantidad.getValue();
            double precio = 25.00; // Precio base por tiquete
            labelTotal.setText(String.format("Total a pagar: $%.2f", cantidad * precio));
        });
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonCancelar = ComponentesPersonalizados.crearBotonSecundario("Cancelar");
        JButton botonComprar = ComponentesPersonalizados.crearBotonPrimario("Comprar");
        
        botonCancelar.addActionListener(_ -> dialogo.dispose());
        botonComprar.addActionListener(_ -> {
            // Validación básica
            if (comboAtraccion.getSelectedIndex() == 0 || comboFecha.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(dialogo, 
                        "Por favor, seleccione una atracción y una fecha de visita.", 
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Simulación de compra exitosa
            NotificacionUI.mostrarNotificacion(this, 
                    "¡Tiquete comprado con éxito!", 
                    NotificacionUI.TipoNotificacion.EXITO);
            
            dialogo.dispose();
            
            // Actualizar la tabla (en una implementación real se recargarían los datos del servicio)
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fechaCompra = sdf.format(new Date());
            String fechaValidez = "15/06/2025"; // Simulado
            
            modeloTabla.insertRow(0, new Object[]{
                "T" + (10 + modeloTabla.getRowCount()), // ID simulado
                comboAtraccion.getSelectedItem(),
                fechaCompra,
                fechaValidez,
                "Activo",
                ""
            });
        });
        
        panelBotones.add(botonCancelar);
        panelBotones.add(botonComprar);
        
        // Agregar componentes al panel de formulario
        panelFormulario.add(labelTitulo);
        panelFormulario.add(new JSeparator());
        panelFormulario.add(labelAtraccion);
        panelFormulario.add(comboAtraccion);
        panelFormulario.add(labelFecha);
        panelFormulario.add(comboFecha);
        panelFormulario.add(labelCantidad);
        panelFormulario.add(spinnerCantidad);
        panelFormulario.add(new JSeparator());
        panelFormulario.add(labelTotal);
        
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
            panel.add(btnVer);
            
            String estado = (String) table.getValueAt(row, 4);
            if ("Activo".equals(estado)) {
                JButton btnQR = new JButton("QR");
                btnQR.setFont(EstilosUI.FUENTE_PEQUEÑA);
                panel.add(btnQR);
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
        protected JButton btnQR;
        protected String idTiquete;
        protected String estadoTiquete;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            
            btnVer = new JButton("Ver");
            btnVer.setFont(EstilosUI.FUENTE_PEQUEÑA);
            btnVer.addActionListener(_ -> verDetallesTiquete());
            
            btnQR = new JButton("QR");
            btnQR.setFont(EstilosUI.FUENTE_PEQUEÑA);
            btnQR.addActionListener(_ -> mostrarQRTiquete());
            
            panel.add(btnVer);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            
            idTiquete = (String) table.getValueAt(row, 0);
            estadoTiquete = (String) table.getValueAt(row, 4);
            
            panel.removeAll();
            panel.add(btnVer);
            
            if ("Activo".equals(estadoTiquete)) {
                panel.add(btnQR);
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
        
        private void verDetallesTiquete() {
            JOptionPane.showMessageDialog(null, "Detalles del tiquete: " + idTiquete, 
                    "Detalles", JOptionPane.INFORMATION_MESSAGE);
            fireEditingStopped();
        }
        
        private void mostrarQRTiquete() {
            if (!"Activo".equals(estadoTiquete)) {
                JOptionPane.showMessageDialog(null, 
                        "Este tiquete no está activo actualmente.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JOptionPane.showMessageDialog(null, 
                    "Mostrando código QR para el tiquete " + idTiquete + "\n\n" +
                    "Presente este código en la entrada de la atracción.",
                    "Código QR", JOptionPane.INFORMATION_MESSAGE);
            fireEditingStopped();
        }
    }
}

package vista.gui.paneles;

import vista.gui.componentes.ComponentesPersonalizados;
import vista.gui.util.EstilosUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Panel para la gestión de tiquetes del parque.
 * Permite visualizar tipos de tiquetes, vender tiquetes y ver historial de ventas.
 */
public class PanelTiquetes extends JPanel {
    
    private JTabbedPane pestañas;
    private JPanel panelTiposTiquetes;
    private JPanel panelVentaRapida;
    private JPanel panelHistorialTransacciones;
    
    /**
     * Constructor del panel de tiquetes
     */
    public PanelTiquetes() {
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
        
        JLabel labelTitulo = ComponentesPersonalizados.crearEtiquetaTitulo("Gestión de Tiquetes");
        panelSuperior.add(labelTitulo, BorderLayout.WEST);
        
        // Pestañas para las diferentes secciones
        pestañas = new JTabbedPane();
        pestañas.setFont(EstilosUI.FUENTE_NORMAL);
        pestañas.setBackground(Color.WHITE);
        
        // Crear paneles para cada pestaña
        panelTiposTiquetes = crearPanelTiposTiquetes();
        panelVentaRapida = crearPanelVentaRapida();
        panelHistorialTransacciones = crearPanelHistorialTransacciones();
        
        // Agregar pestañas
        pestañas.addTab("Tipos de Tiquetes", panelTiposTiquetes);
        pestañas.addTab("Venta Rápida", panelVentaRapida);
        pestañas.addTab("Historial de Transacciones", panelHistorialTransacciones);
        
        // Agregar componentes al panel principal
        add(panelSuperior, BorderLayout.NORTH);
        add(pestañas, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelTiposTiquetes() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(EstilosUI.COLOR_FONDO);
        
        // Panel con los diferentes tipos de tiquetes
        JPanel panelTarjetas = new JPanel(new GridLayout(2, 2, 15, 15));
        panelTarjetas.setOpaque(false);
        
        // Crear tarjetas para cada tipo de tiquete
        panelTarjetas.add(crearTarjetaTiquete(
                "Entrada General", 
                "Acceso a todas las atracciones estándar del parque.",
                "$50.00",
                new String[]{"Válido para todo el día", "No incluye atracciones premium"}));
        
        panelTarjetas.add(crearTarjetaTiquete(
                "Fast Pass", 
                "Acceso preferencial para evitar filas en atracciones seleccionadas.",
                "$30.00 (adicional)",
                new String[]{"Evita filas en atracciones principales", "Válido para 5 atracciones"}));
        
        panelTarjetas.add(crearTarjetaTiquete(
                "Tiquete de Temporada", 
                "Acceso ilimitado al parque durante toda la temporada.",
                "$200.00",
                new String[]{"Válido por 3 meses", "Incluye descuentos en comida"}));
        
        panelTarjetas.add(crearTarjetaTiquete(
                "Entrada Individual", 
                "Acceso a una atracción específica.",
                "$15.00",
                new String[]{"Válido para una sola atracción", "Sin límite de tiempo"}));
        
        panel.add(panelTarjetas, BorderLayout.CENTER);
        
        // Instrucciones o información adicional
        JPanel panelInfo = ComponentesPersonalizados.crearPanelRedondeado();
        panelInfo.setLayout(new BorderLayout());
        panelInfo.setPreferredSize(new Dimension(0, 80));
        
        JLabel labelInfo = new JLabel(
                "<html><body>Los precios pueden variar según la temporada. Consulta descuentos disponibles.<br>" +
                "Los niños menores de 3 años entran gratis. Se requiere identificación para descuentos.</body></html>");
        labelInfo.setFont(EstilosUI.FUENTE_PEQUEÑA);
        
        panelInfo.add(labelInfo, BorderLayout.CENTER);
        
        panel.add(panelInfo, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearTarjetaTiquete(String titulo, String descripcion, String precio, String[] detalles) {
        JPanel panel = ComponentesPersonalizados.crearPanelRedondeado();
        panel.setLayout(new BorderLayout(10, 10));
        
        // Encabezado con título y precio
        JPanel panelEncabezado = new JPanel(new BorderLayout());
        panelEncabezado.setOpaque(false);
        
        JLabel labelTitulo = ComponentesPersonalizados.crearEtiquetaSubtitulo(titulo);
        
        JLabel labelPrecio = new JLabel(precio);
        labelPrecio.setFont(new Font("Roboto", Font.BOLD, 18));
        labelPrecio.setForeground(EstilosUI.COLOR_PRINCIPAL);
        
        panelEncabezado.add(labelTitulo, BorderLayout.WEST);
        panelEncabezado.add(labelPrecio, BorderLayout.EAST);
        
        // Descripción
        JLabel labelDescripcion = new JLabel(descripcion);
        labelDescripcion.setFont(EstilosUI.FUENTE_NORMAL);
        
        // Lista de detalles
        JPanel panelDetalles = new JPanel();
        panelDetalles.setLayout(new BoxLayout(panelDetalles, BoxLayout.Y_AXIS));
        panelDetalles.setOpaque(false);
        
        for (String detalle : detalles) {
            JLabel labelDetalle = new JLabel("• " + detalle);
            labelDetalle.setFont(EstilosUI.FUENTE_PEQUEÑA);
            panelDetalles.add(labelDetalle);
            panelDetalles.add(Box.createVerticalStrut(5));
        }
        
        // Botón de compra
        JButton botonComprar = ComponentesPersonalizados.crearBotonPrimario("Comprar");
        botonComprar.addActionListener(_ -> mostrarDialogoCompra(titulo, precio));
        
        // Agregar componentes al panel
        panel.add(panelEncabezado, BorderLayout.NORTH);
        panel.add(labelDescripcion, BorderLayout.CENTER);
        panel.add(panelDetalles, BorderLayout.SOUTH);
        
        // Panel para botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setOpaque(false);
        panelBotones.add(botonComprar);
        
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelVentaRapida() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(EstilosUI.COLOR_FONDO);
        
        // Panel de formulario para venta rápida
        JPanel panelFormulario = ComponentesPersonalizados.crearPanelRedondeado();
        panelFormulario.setLayout(new GridLayout(0, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Campos del formulario
        JLabel labelIdentificacion = new JLabel("Identificación del cliente:");
        JTextField campoIdentificacion = ComponentesPersonalizados.crearCampoTexto();
        
        JLabel labelTipoTiquete = new JLabel("Tipo de tiquete:");
        JComboBox<String> comboTipoTiquete = new JComboBox<>(new String[]{
            "Entrada General", "Fast Pass", "Tiquete de Temporada", "Entrada Individual"
        });
        
        JLabel labelCantidad = new JLabel("Cantidad:");
        JSpinner spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        spinnerCantidad.setFont(EstilosUI.FUENTE_NORMAL);
        ((JSpinner.DefaultEditor) spinnerCantidad.getEditor()).getTextField().setEditable(false);
        
        JLabel labelAtraccion = new JLabel("Atracción (solo para Entrada Individual):");
        JComboBox<String> comboAtraccion = new JComboBox<>(new String[]{
            "Seleccione una atracción", "Montaña Rusa", "Carrusel", "Casa Embrujada", 
            "Rueda de la Fortuna", "Splash Mountain", "Nave Espacial"
        });
        comboAtraccion.setEnabled(false);
        
        // Habilitar/deshabilitar comboAtraccion según el tipo de tiquete
        comboTipoTiquete.addActionListener(_ -> {
            boolean esEntradaIndividual = comboTipoTiquete.getSelectedItem().equals("Entrada Individual");
            comboAtraccion.setEnabled(esEntradaIndividual);
        });
        
        JLabel labelDescuento = new JLabel("Aplicar descuento:");
        JCheckBox checkDescuento = new JCheckBox("Cliente frecuente / Empleado");
        checkDescuento.setFont(EstilosUI.FUENTE_NORMAL);
        checkDescuento.setOpaque(false);
        
        // Agregar campos al formulario
        panelFormulario.add(labelIdentificacion);
        panelFormulario.add(campoIdentificacion);
        panelFormulario.add(labelTipoTiquete);
        panelFormulario.add(comboTipoTiquete);
        panelFormulario.add(labelCantidad);
        panelFormulario.add(spinnerCantidad);
        panelFormulario.add(labelAtraccion);
        panelFormulario.add(comboAtraccion);
        panelFormulario.add(labelDescuento);
        panelFormulario.add(checkDescuento);
        
        // Panel de resumen de venta
        JPanel panelResumen = ComponentesPersonalizados.crearPanelRedondeado();
        panelResumen.setLayout(new BorderLayout(0, 10));
        panelResumen.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelResumen.setPreferredSize(new Dimension(300, 0));
        
        JLabel labelTituloResumen = ComponentesPersonalizados.crearEtiquetaSubtitulo("Resumen de Venta");
        
        JPanel panelDetallesResumen = new JPanel(new GridLayout(0, 2, 5, 10));
        panelDetallesResumen.setOpaque(false);
        
        panelDetallesResumen.add(new JLabel("Tipo de tiquete:"));
        JLabel labelResumenTipo = new JLabel("Entrada General");
        panelDetallesResumen.add(labelResumenTipo);
        
        panelDetallesResumen.add(new JLabel("Cantidad:"));
        JLabel labelResumenCantidad = new JLabel("1");
        panelDetallesResumen.add(labelResumenCantidad);
        
        panelDetallesResumen.add(new JLabel("Precio unitario:"));
        JLabel labelResumenPrecioUnitario = new JLabel("$50.00");
        panelDetallesResumen.add(labelResumenPrecioUnitario);
        
        panelDetallesResumen.add(new JLabel("Descuento:"));
        JLabel labelResumenDescuento = new JLabel("$0.00");
        panelDetallesResumen.add(labelResumenDescuento);
        
        panelDetallesResumen.add(new JSeparator());
        panelDetallesResumen.add(new JLabel("TOTAL:"));
        JLabel labelResumenTotal = new JLabel("$50.00");
        labelResumenTotal.setFont(new Font("Roboto", Font.BOLD, 16));
        labelResumenTotal.setForeground(EstilosUI.COLOR_PRINCIPAL);
        panelDetallesResumen.add(labelResumenTotal);
        
        // Actualizar resumen al cambiar selecciones
        ActionListener actualizarResumen = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                labelResumenTipo.setText(comboTipoTiquete.getSelectedItem().toString());
                labelResumenCantidad.setText(spinnerCantidad.getValue().toString());
                
                double precioUnitario = 0.0;
                switch (comboTipoTiquete.getSelectedItem().toString()) {
                    case "Entrada General":
                        precioUnitario = 50.0;
                        break;
                    case "Fast Pass":
                        precioUnitario = 30.0;
                        break;
                    case "Tiquete de Temporada":
                        precioUnitario = 200.0;
                        break;
                    case "Entrada Individual":
                        precioUnitario = 15.0;
                        break;
                }
                
                labelResumenPrecioUnitario.setText(String.format("$%.2f", precioUnitario));
                
                int cantidad = (Integer) spinnerCantidad.getValue();
                double subtotal = precioUnitario * cantidad;
                
                double descuento = 0.0;
                if (checkDescuento.isSelected()) {
                    descuento = subtotal * 0.1; // 10% de descuento
                }
                
                labelResumenDescuento.setText(String.format("$%.2f", descuento));
                labelResumenTotal.setText(String.format("$%.2f", subtotal - descuento));
            }
        };
        
        comboTipoTiquete.addActionListener(actualizarResumen);
        spinnerCantidad.addChangeListener(_ -> actualizarResumen.actionPerformed(null));
        checkDescuento.addActionListener(actualizarResumen);
        
        JButton botonProcesarVenta = ComponentesPersonalizados.crearBotonPrimario("Procesar Venta");
        botonProcesarVenta.addActionListener(_ -> {
            if (campoIdentificacion.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Debe ingresar la identificación del cliente",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (comboTipoTiquete.getSelectedItem().equals("Entrada Individual") &&
                    comboAtraccion.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(panel, "Debe seleccionar una atracción para la Entrada Individual",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Procesar venta (aquí se llamaría al servicio correspondiente)
            JOptionPane.showMessageDialog(panel, "Venta procesada con éxito.\nSe han generado " +
                    spinnerCantidad.getValue() + " tiquetes.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            // Limpiar formulario
            campoIdentificacion.setText("");
            comboTipoTiquete.setSelectedIndex(0);
            spinnerCantidad.setValue(1);
            comboAtraccion.setSelectedIndex(0);
            checkDescuento.setSelected(false);
            actualizarResumen.actionPerformed(null);
        });
        
        panelResumen.add(labelTituloResumen, BorderLayout.NORTH);
        panelResumen.add(panelDetallesResumen, BorderLayout.CENTER);
        panelResumen.add(botonProcesarVenta, BorderLayout.SOUTH);
        
        // Panel principal con formulario y resumen
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 0));
        panelPrincipal.setOpaque(false);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        panelPrincipal.add(panelResumen, BorderLayout.EAST);
        
        panel.add(panelPrincipal, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelHistorialTransacciones() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(EstilosUI.COLOR_FONDO);
        
        // Panel de filtros
        JPanel panelFiltros = ComponentesPersonalizados.crearPanelRedondeado();
        panelFiltros.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelFiltros.setPreferredSize(new Dimension(0, 60));
        
        JLabel labelFechaDesde = new JLabel("Desde:");
        labelFechaDesde.setFont(EstilosUI.FUENTE_NORMAL);
        
        JTextField campoFechaDesde = ComponentesPersonalizados.crearCampoTexto();
        campoFechaDesde.setPreferredSize(new Dimension(120, 35));
        campoFechaDesde.setText("DD/MM/AAAA");
        
        JLabel labelFechaHasta = new JLabel("Hasta:");
        labelFechaHasta.setFont(EstilosUI.FUENTE_NORMAL);
        
        JTextField campoFechaHasta = ComponentesPersonalizados.crearCampoTexto();
        campoFechaHasta.setPreferredSize(new Dimension(120, 35));
        campoFechaHasta.setText("DD/MM/AAAA");
        
        JLabel labelTipoTiquete = new JLabel("Tipo:");
        labelTipoTiquete.setFont(EstilosUI.FUENTE_NORMAL);
        
        JComboBox<String> comboTipoTiquete = new JComboBox<>(new String[]{
            "Todos", "Entrada General", "Fast Pass", "Tiquete de Temporada", "Entrada Individual"
        });
        comboTipoTiquete.setFont(EstilosUI.FUENTE_NORMAL);
        comboTipoTiquete.setPreferredSize(new Dimension(150, 35));
        
        JButton botonFiltrar = ComponentesPersonalizados.crearBotonSecundario("Filtrar");
        
        panelFiltros.add(labelFechaDesde);
        panelFiltros.add(campoFechaDesde);
        panelFiltros.add(labelFechaHasta);
        panelFiltros.add(campoFechaHasta);
        panelFiltros.add(labelTipoTiquete);
        panelFiltros.add(comboTipoTiquete);
        panelFiltros.add(botonFiltrar);
        
        // Panel de tabla de transacciones
        JPanel panelTabla = ComponentesPersonalizados.crearPanelRedondeado();
        panelTabla.setLayout(new BorderLayout());
        
        // Crear modelo de tabla
        String[] columnas = {"Código", "Tipo", "Fecha y Hora", "Cliente", "Precio", "Acciones"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Solo la columna de acciones es editable
            }
        };
        
        // Cargar datos de prueba
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        Object[][] datos = {
            {"TG001", "Entrada General", LocalDateTime.now().minusDays(1).format(formatter), "Roberto García", "$50.00", ""},
            {"FP002", "Fast Pass", LocalDateTime.now().minusDays(1).format(formatter), "Laura Martínez", "$30.00", ""},
            {"TT003", "Tiquete de Temporada", LocalDateTime.now().minusDays(2).format(formatter), "Carlos Jiménez", "$200.00", ""},
            {"EI004", "Entrada Individual", LocalDateTime.now().minusDays(2).format(formatter), "Ana Rodríguez", "$15.00", ""},
            {"TG005", "Entrada General", LocalDateTime.now().minusDays(3).format(formatter), "Miguel Fernández", "$50.00", ""},
            {"FP006", "Fast Pass", LocalDateTime.now().minusDays(3).format(formatter), "Sofía López", "$30.00", ""}
        };
        
        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }
        
        // Crear tabla
        JTable tablaTransacciones = new JTable(modeloTabla);
        ComponentesPersonalizados.configurarTabla(tablaTransacciones);
        
        // Configurar renderizador para la columna de acciones
        tablaTransacciones.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        tablaTransacciones.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(tablaTransacciones);
        scrollPane.setBorder(null);
        
        panelTabla.add(scrollPane, BorderLayout.CENTER);
        
        // Panel resumen
        JPanel panelResumen = ComponentesPersonalizados.crearPanelRedondeado();
        panelResumen.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelResumen.setPreferredSize(new Dimension(0, 60));
        
        JLabel labelTotal = new JLabel("Total de ventas: $375.00");
        labelTotal.setFont(new Font("Roboto", Font.BOLD, 16));
        labelTotal.setForeground(EstilosUI.COLOR_PRINCIPAL);
        
        JButton botonExportar = ComponentesPersonalizados.crearBotonSecundario("Exportar");
        
        panelResumen.add(labelTotal);
        panelResumen.add(botonExportar);
        
        // Agregar paneles al panel principal
        panel.add(panelFiltros, BorderLayout.NORTH);
        panel.add(panelTabla, BorderLayout.CENTER);
        panel.add(panelResumen, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void mostrarDialogoCompra(String tipoTiquete, String precio) {
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Comprar " + tipoTiquete, true);
        dialogo.setSize(400, 300);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());
        
        JPanel panelFormulario = new JPanel(new GridLayout(0, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Campos del formulario
        JLabel labelIdentificacion = new JLabel("Identificación del cliente:");
        JTextField campoIdentificacion = ComponentesPersonalizados.crearCampoTexto();
        
        JLabel labelCantidad = new JLabel("Cantidad:");
        JSpinner spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        spinnerCantidad.setFont(EstilosUI.FUENTE_NORMAL);
        
        JLabel labelPrecioUnitario = new JLabel("Precio unitario:");
        JLabel labelPrecioUnitarioValor = new JLabel(precio);
        labelPrecioUnitarioValor.setFont(EstilosUI.FUENTE_NORMAL);
        
        JLabel labelTotal = new JLabel("Total:");
        JLabel labelTotalValor = new JLabel(precio);
        labelTotalValor.setFont(new Font("Roboto", Font.BOLD, 16));
        labelTotalValor.setForeground(EstilosUI.COLOR_PRINCIPAL);
        
        // Actualizar total al cambiar cantidad
        spinnerCantidad.addChangeListener(_ -> {
            int cantidad = (Integer) spinnerCantidad.getValue();
            double precioUnitario = Double.parseDouble(precio.substring(1));
            labelTotalValor.setText(String.format("$%.2f", cantidad * precioUnitario));
        });
        
        // Agregar campos al formulario
        panelFormulario.add(labelIdentificacion);
        panelFormulario.add(campoIdentificacion);
        panelFormulario.add(labelCantidad);
        panelFormulario.add(spinnerCantidad);
        panelFormulario.add(labelPrecioUnitario);
        panelFormulario.add(labelPrecioUnitarioValor);
        panelFormulario.add(labelTotal);
        panelFormulario.add(labelTotalValor);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonCancelar = ComponentesPersonalizados.crearBotonSecundario("Cancelar");
        JButton botonComprar = ComponentesPersonalizados.crearBotonPrimario("Comprar");
        
        botonCancelar.addActionListener(_ -> dialogo.dispose());
        botonComprar.addActionListener(_ -> {
            if (campoIdentificacion.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Debe ingresar la identificación del cliente",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Procesar compra (aquí se llamaría al servicio correspondiente)
            JOptionPane.showMessageDialog(dialogo, "Compra realizada con éxito.\nSe han generado " +
                    spinnerCantidad.getValue() + " tiquetes de tipo " + tipoTiquete + ".", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dialogo.dispose();
        });
        
        panelBotones.add(botonCancelar);
        panelBotones.add(botonComprar);
        
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
            
            JButton btnImprimir = new JButton("Imprimir");
            btnImprimir.setFont(EstilosUI.FUENTE_PEQUEÑA);
            
            panel.add(btnVer);
            panel.add(btnImprimir);
            
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
        protected JButton btnImprimir;
        protected String codigoTiquete;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            
            btnVer = new JButton("Ver");
            btnVer.setFont(EstilosUI.FUENTE_PEQUEÑA);
            btnVer.addActionListener(_ -> verDetalleTiquete());
            
            btnImprimir = new JButton("Imprimir");
            btnImprimir.setFont(EstilosUI.FUENTE_PEQUEÑA);
            btnImprimir.addActionListener(_ -> imprimirTiquete());
            
            panel.add(btnVer);
            panel.add(btnImprimir);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            
            codigoTiquete = (String) table.getValueAt(row, 0);
            
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
        
        private void verDetalleTiquete() {
            JOptionPane.showMessageDialog(null, "Ver detalle de tiquete: " + codigoTiquete, 
                    "Detalle", JOptionPane.INFORMATION_MESSAGE);
            fireEditingStopped();
        }
        
        private void imprimirTiquete() {
            JOptionPane.showMessageDialog(null, "Imprimir tiquete: " + codigoTiquete, 
                    "Imprimir", JOptionPane.INFORMATION_MESSAGE);
            fireEditingStopped();
        }
    }
}

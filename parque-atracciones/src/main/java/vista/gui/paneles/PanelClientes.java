package vista.gui.paneles;

import vista.gui.componentes.ComponentesPersonalizados;
import vista.gui.util.EstilosUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


/**
 * Panel para la gestión de clientes del parque.
 * Permite visualizar, filtrar, editar y crear clientes.
 */
public class PanelClientes extends JPanel {
    
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboFiltroMembresia;
    private JTextField campoFechaDesde;
    private JTextField campoFechaHasta;
    private JTextField campoBusqueda;
    private JButton botonNuevoCliente;
    private JButton botonFiltrar;
    
    /**
     * Constructor del panel de clientes
     */
    public PanelClientes() {
        configurarUI();
        inicializarComponentes();
    }
    
    private void configurarUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(EstilosUI.COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
    
    private void inicializarComponentes() {
        // Panel superior con título y botón de nuevo cliente
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        
        JLabel labelTitulo = ComponentesPersonalizados.crearEtiquetaTitulo("Gestión de Clientes");
        
        botonNuevoCliente = ComponentesPersonalizados.crearBotonPrimario("Nuevo Cliente");
        botonNuevoCliente.addActionListener(_ -> mostrarDialogoNuevoCliente());
        
        panelSuperior.add(labelTitulo, BorderLayout.WEST);
        panelSuperior.add(botonNuevoCliente, BorderLayout.EAST);
        
        // Panel de filtros
        JPanel panelFiltros = crearPanelFiltros();
        
        // Tabla de clientes
        JPanel panelTabla = crearPanelTabla();
        
        // Agregar componentes al panel principal
        add(panelSuperior, BorderLayout.NORTH);
        add(panelFiltros, BorderLayout.SOUTH);
        add(panelTabla, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelFiltros() {
        JPanel panel = ComponentesPersonalizados.crearPanelRedondeado();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setPreferredSize(new Dimension(0, 60));
        
        JLabel labelFiltroMembresia = new JLabel("Membresía:");
        labelFiltroMembresia.setFont(EstilosUI.FUENTE_NORMAL);
        
        comboFiltroMembresia = new JComboBox<>(new String[]{
            "Todas", "Estándar", "Premium", "VIP", "Sin membresía"
        });
        comboFiltroMembresia.setFont(EstilosUI.FUENTE_NORMAL);
        comboFiltroMembresia.setPreferredSize(new Dimension(150, 35));
        
        JLabel labelFechaRegistro = new JLabel("Registro:");
        labelFechaRegistro.setFont(EstilosUI.FUENTE_NORMAL);
        
        campoFechaDesde = ComponentesPersonalizados.crearCampoTexto();
        campoFechaDesde.setPreferredSize(new Dimension(100, 35));
        campoFechaDesde.setText("DD/MM/AAAA");
        
        JLabel labelHasta = new JLabel("hasta");
        labelHasta.setFont(EstilosUI.FUENTE_NORMAL);
        
        campoFechaHasta = ComponentesPersonalizados.crearCampoTexto();
        campoFechaHasta.setPreferredSize(new Dimension(100, 35));
        campoFechaHasta.setText("DD/MM/AAAA");
        
        JLabel labelBusqueda = new JLabel("Buscar:");
        labelBusqueda.setFont(EstilosUI.FUENTE_NORMAL);
        
        campoBusqueda = ComponentesPersonalizados.crearCampoTexto();
        campoBusqueda.setPreferredSize(new Dimension(150, 35));
        
        botonFiltrar = ComponentesPersonalizados.crearBotonSecundario("Filtrar");
        botonFiltrar.addActionListener(_ -> aplicarFiltros());
        
        panel.add(labelFiltroMembresia);
        panel.add(comboFiltroMembresia);
        panel.add(labelFechaRegistro);
        panel.add(campoFechaDesde);
        panel.add(labelHasta);
        panel.add(campoFechaHasta);
        panel.add(labelBusqueda);
        panel.add(campoBusqueda);
        panel.add(botonFiltrar);
        
        return panel;
    }
    
    private JPanel crearPanelTabla() {
        JPanel panel = ComponentesPersonalizados.crearPanelRedondeado();
        panel.setLayout(new BorderLayout());
        
        // Crear modelo de tabla
        String[] columnas = {"ID", "Nombre", "Email", "Membresía", "Última visita", "Acciones"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Solo la columna de acciones es editable
            }
        };
        
        // Cargar datos de prueba
        cargarDatosPrueba();
        
        // Crear tabla
        tablaClientes = new JTable(modeloTabla);
        ComponentesPersonalizados.configurarTabla(tablaClientes);
        
        // Configurar renderizador para la columna de acciones
        tablaClientes.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        tablaClientes.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        scrollPane.setBorder(null);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void cargarDatosPrueba() {
        // En una implementación real, estos datos vendrían del servicio
        Object[][] datos = {
            {"C001", "Roberto García", "roberto@mail.com", "Premium", "15/05/2025", ""},
            {"C002", "Laura Martínez", "laura@mail.com", "VIP", "10/05/2025", ""},
            {"C003", "Carlos Jiménez", "carlos@mail.com", "Estándar", "01/05/2025", ""},
            {"C004", "Ana Rodríguez", "ana@mail.com", "Sin membresía", "20/04/2025", ""},
            {"C005", "Miguel Fernández", "miguel@mail.com", "Premium", "05/05/2025", ""},
            {"C006", "Sofía López", "sofia@mail.com", "Estándar", "12/05/2025", ""}
        };
        
        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }
    }
    
    private void aplicarFiltros() {
        // Aquí se implementaría la lógica de filtrado real
        JOptionPane.showMessageDialog(this, 
                "Filtros aplicados:\nMembresía: " + comboFiltroMembresia.getSelectedItem() + 
                "\nFecha desde: " + campoFechaDesde.getText() +
                "\nFecha hasta: " + campoFechaHasta.getText() +
                "\nBúsqueda: " + campoBusqueda.getText(),
                "Filtros", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarDialogoNuevoCliente() {
        // Diálogo para crear un nuevo cliente
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nuevo Cliente", true);
        dialogo.setSize(500, 500);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());
        
        JPanel panelFormulario = new JPanel(new GridLayout(0, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Campos del formulario
        JLabel labelNombre = new JLabel("Nombre:");
        JTextField campoNombre = ComponentesPersonalizados.crearCampoTexto();
        
        JLabel labelIdentificacion = new JLabel("Identificación:");
        JTextField campoIdentificacion = ComponentesPersonalizados.crearCampoTexto();
        
        JLabel labelEmail = new JLabel("Email:");
        JTextField campoEmail = ComponentesPersonalizados.crearCampoTexto();
        
        JLabel labelTelefono = new JLabel("Teléfono:");
        JTextField campoTelefono = ComponentesPersonalizados.crearCampoTexto();
        
        JLabel labelMembresia = new JLabel("Membresía:");
        JComboBox<String> comboMembresia = new JComboBox<>(new String[]{
            "Estándar", "Premium", "VIP", "Sin membresía"
        });
        
        JLabel labelUsername = new JLabel("Usuario:");
        JTextField campoUsername = ComponentesPersonalizados.crearCampoTexto();
        
        JLabel labelPassword = new JLabel("Contraseña:");
        JPasswordField campoPassword = new JPasswordField();
        campoPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstilosUI.COLOR_BORDE),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        campoPassword.setPreferredSize(new Dimension(200, 35));
        
        // Agregar campos al formulario
        panelFormulario.add(labelNombre);
        panelFormulario.add(campoNombre);
        panelFormulario.add(labelIdentificacion);
        panelFormulario.add(campoIdentificacion);
        panelFormulario.add(labelEmail);
        panelFormulario.add(campoEmail);
        panelFormulario.add(labelTelefono);
        panelFormulario.add(campoTelefono);
        panelFormulario.add(labelMembresia);
        panelFormulario.add(comboMembresia);
        panelFormulario.add(labelUsername);
        panelFormulario.add(campoUsername);
        panelFormulario.add(labelPassword);
        panelFormulario.add(campoPassword);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonCancelar = ComponentesPersonalizados.crearBotonSecundario("Cancelar");
        JButton botonGuardar = ComponentesPersonalizados.crearBotonPrimario("Guardar");
        
        botonCancelar.addActionListener(_ -> dialogo.dispose());
        botonGuardar.addActionListener(_ -> {
            // Aquí se guardaría el nuevo cliente
            JOptionPane.showMessageDialog(dialogo, "Cliente guardado con éxito", 
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
            
            JButton btnHistorial = new JButton("Historial");
            btnHistorial.setFont(EstilosUI.FUENTE_PEQUEÑA);
            
            JButton btnEditar = new JButton("Editar");
            btnEditar.setFont(EstilosUI.FUENTE_PEQUEÑA);
            
            panel.add(btnHistorial);
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
        protected JButton btnHistorial;
        protected JButton btnEditar;
        protected String idCliente;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            
            btnHistorial = new JButton("Historial");
            btnHistorial.setFont(EstilosUI.FUENTE_PEQUEÑA);
            btnHistorial.addActionListener(_ -> verHistorialCliente());
            
            btnEditar = new JButton("Editar");
            btnEditar.setFont(EstilosUI.FUENTE_PEQUEÑA);
            btnEditar.addActionListener(_ -> editarCliente());
            
            panel.add(btnHistorial);
            panel.add(btnEditar);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            
            idCliente = (String) table.getValueAt(row, 0);
            
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
        
        private void verHistorialCliente() {
            JOptionPane.showMessageDialog(null, "Ver historial de cliente: " + idCliente, 
                    "Historial", JOptionPane.INFORMATION_MESSAGE);
            fireEditingStopped();
        }
        
        private void editarCliente() {
            JOptionPane.showMessageDialog(null, "Editar cliente: " + idCliente, 
                    "Editar", JOptionPane.INFORMATION_MESSAGE);
            fireEditingStopped();
        }
    }
}

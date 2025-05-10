package vista.gui.paneles;

import vista.gui.componentes.ComponentesPersonalizados;
import vista.gui.util.EstilosUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel para la gestión de empleados del parque.
 * Permite visualizar, filtrar, editar y crear empleados.
 */
public class PanelEmpleados extends JPanel {
    
    private JTable tablaEmpleados;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboFiltroDepartamento;
    private JComboBox<String> comboFiltroEstado;
    private JTextField campoBusqueda;
    private JButton botonNuevoEmpleado;
    private JButton botonFiltrar;
    
    /**
     * Constructor del panel de empleados
     */
    public PanelEmpleados() {
        configurarUI();
        inicializarComponentes();
    }
    
    private void configurarUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(EstilosUI.COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
    
    private void inicializarComponentes() {
        // Panel superior con título y botón de nuevo empleado
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        
        JLabel labelTitulo = ComponentesPersonalizados.crearEtiquetaTitulo("Gestión de Empleados");
        
        botonNuevoEmpleado = ComponentesPersonalizados.crearBotonPrimario("Nuevo Empleado");
        botonNuevoEmpleado.addActionListener(_ -> mostrarDialogoNuevoEmpleado());
        
        panelSuperior.add(labelTitulo, BorderLayout.WEST);
        panelSuperior.add(botonNuevoEmpleado, BorderLayout.EAST);
        
        // Panel de filtros
        JPanel panelFiltros = crearPanelFiltros();
        
        // Tabla de empleados
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
        
        JLabel labelFiltroDepartamento = new JLabel("Departamento:");
        labelFiltroDepartamento.setFont(EstilosUI.FUENTE_NORMAL);
        
        comboFiltroDepartamento = new JComboBox<>(new String[]{
            "Todos", "Operaciones", "Atención al cliente", "Cocina", "Mantenimiento"
        });
        comboFiltroDepartamento.setFont(EstilosUI.FUENTE_NORMAL);
        comboFiltroDepartamento.setPreferredSize(new Dimension(150, 35));
        
        JLabel labelFiltroEstado = new JLabel("Estado:");
        labelFiltroEstado.setFont(EstilosUI.FUENTE_NORMAL);
        
        comboFiltroEstado = new JComboBox<>(new String[]{"Todos", "Activo", "Inactivo", "Vacaciones"});
        comboFiltroEstado.setFont(EstilosUI.FUENTE_NORMAL);
        comboFiltroEstado.setPreferredSize(new Dimension(150, 35));
        
        JLabel labelBusqueda = new JLabel("Buscar:");
        labelBusqueda.setFont(EstilosUI.FUENTE_NORMAL);
        
        campoBusqueda = ComponentesPersonalizados.crearCampoTexto();
        campoBusqueda.setPreferredSize(new Dimension(200, 35));
        
        botonFiltrar = ComponentesPersonalizados.crearBotonSecundario("Filtrar");
        botonFiltrar.addActionListener(_ -> aplicarFiltros());
        
        panel.add(labelFiltroDepartamento);
        panel.add(comboFiltroDepartamento);
        panel.add(labelFiltroEstado);
        panel.add(comboFiltroEstado);
        panel.add(labelBusqueda);
        panel.add(campoBusqueda);
        panel.add(botonFiltrar);
        
        return panel;
    }
    
    private JPanel crearPanelTabla() {
        JPanel panel = ComponentesPersonalizados.crearPanelRedondeado();
        panel.setLayout(new BorderLayout());
        
        // Crear modelo de tabla
        String[] columnas = {"ID", "Nombre", "Cargo", "Departamento", "Estado", "Horario", "Acciones"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Solo la columna de acciones es editable
            }
        };
        
        // Cargar datos de prueba
        cargarDatosPrueba();
        
        // Crear tabla
        tablaEmpleados = new JTable(modeloTabla);
        ComponentesPersonalizados.configurarTabla(tablaEmpleados);
        
        // Configurar renderizador para la columna de acciones
        tablaEmpleados.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        tablaEmpleados.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(tablaEmpleados);
        scrollPane.setBorder(null);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void cargarDatosPrueba() {
        // En una implementación real, estos datos vendrían del servicio
        Object[][] datos = {
            {"E001", "Juan Pérez", "Operario", "Operaciones", "Activo", "8:00-16:00", ""},
            {"E002", "María López", "Cajera", "Atención al cliente", "Activo", "9:00-17:00", ""},
            {"E003", "Carlos Rodríguez", "Cocinero", "Cocina", "Activo", "7:00-15:00", ""},
            {"E004", "Ana Martínez", "Técnico", "Mantenimiento", "Vacaciones", "N/A", ""},
            {"E005", "Pedro Sánchez", "Operario", "Operaciones", "Inactivo", "N/A", ""},
            {"E006", "Lucía Fernández", "Servicio General", "Atención al cliente", "Activo", "10:00-18:00", ""}
        };
        
        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }
    }
    
    private void aplicarFiltros() {
        // Aquí se implementaría la lógica de filtrado real
        JOptionPane.showMessageDialog(this, 
                "Filtros aplicados:\nDepartamento: " + comboFiltroDepartamento.getSelectedItem() + 
                "\nEstado: " + comboFiltroEstado.getSelectedItem() + 
                "\nBúsqueda: " + campoBusqueda.getText(),
                "Filtros", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarDialogoNuevoEmpleado() {
        // Diálogo para crear un nuevo empleado
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nuevo Empleado", true);
        dialogo.setSize(500, 600);
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
        
        JLabel labelTipo = new JLabel("Tipo:");
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{
            "Operario", "Cajero", "Cocinero", "Servicio General"
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
        panelFormulario.add(labelTipo);
        panelFormulario.add(comboTipo);
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
            // Aquí se guardaría el nuevo empleado
            JOptionPane.showMessageDialog(dialogo, "Empleado guardado con éxito", 
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
        protected String idEmpleado;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            
            btnVer = new JButton("Ver");
            btnVer.setFont(EstilosUI.FUENTE_PEQUEÑA);
            btnVer.addActionListener(_ -> verDetallesEmpleado());
            
            btnEditar = new JButton("Editar");
            btnEditar.setFont(EstilosUI.FUENTE_PEQUEÑA);
            btnEditar.addActionListener(_ -> editarEmpleado());
            
            panel.add(btnVer);
            panel.add(btnEditar);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            
            idEmpleado = (String) table.getValueAt(row, 0);
            
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
        
        private void verDetallesEmpleado() {
            JOptionPane.showMessageDialog(null, "Ver detalles de empleado: " + idEmpleado, 
                    "Detalles", JOptionPane.INFORMATION_MESSAGE);
            fireEditingStopped();
        }
        
        private void editarEmpleado() {
            JOptionPane.showMessageDialog(null, "Editar empleado: " + idEmpleado, 
                    "Editar", JOptionPane.INFORMATION_MESSAGE);
            fireEditingStopped();
        }
    }
}

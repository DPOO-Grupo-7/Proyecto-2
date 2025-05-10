package vista.gui;

import vista.gui.componentes.ComponentesPersonalizados;
import vista.gui.util.EstilosUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * Ventana de inicio de sesión para el sistema de gestión
 * de parque de atracciones.
 */
public class VentanaLogin extends JDialog {
    
    private JTextField campoUsuario;
    private JPasswordField campoPassword;
    private JComboBox<String> comboTipoUsuario;
    private JButton botonIngresar;
    private JButton botonCancelar;
    
    private boolean loginExitoso = false;
    private String tipoUsuario = "";
    private String nombreUsuario = "";
    
    /**
     * Constructor de la ventana de login
     * @param parent Ventana padre
     */
    public VentanaLogin(Frame parent) {
        super(parent, "Iniciar Sesión", true);
        
        configurarVentana();
        inicializarComponentes();
        
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void configurarVentana() {
        setSize(500, 600); // Tamaño aumentado para mejor visibilidad
        setResizable(false);
        setUndecorated(true); // Sin bordes de ventana
        setShape(new RoundRectangle2D.Double(0, 0, 400, 450, 15, 15));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo principal
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Cabecera
                g2d.setColor(EstilosUI.COLOR_PRINCIPAL);
                g2d.fillRoundRect(0, 0, getWidth(), 120, 15, 15);
                g2d.fillRect(0, 60, getWidth(), 60);
                
                g2d.dispose();
            }
        };
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
// Borde rojo para depuración visual
panelPrincipal.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        
        // Panel superior con logo y título
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.setPreferredSize(new Dimension(400, 120));
        
        JLabel labelTitulo = new JLabel("Parque de Atracciones");
        labelTitulo.setFont(new Font("Roboto", Font.BOLD, 24));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        
        JLabel labelSubtitulo = new JLabel("Sistema de Gestión");
        labelSubtitulo.setFont(new Font("Roboto", Font.PLAIN, 16));
        labelSubtitulo.setForeground(Color.WHITE);
        labelSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        panelSuperior.add(labelTitulo, BorderLayout.CENTER);
        panelSuperior.add(labelSubtitulo, BorderLayout.SOUTH);
        
        // Panel de formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setOpaque(true); // Para que se vea el color de fondo
        panelFormulario.setBackground(Color.YELLOW); // Fondo llamativo para depuración
        panelFormulario.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 3)); // Borde visible para depuración
        System.out.println("[Depuración] panelFormulario inicializado y agregado");
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFormulario.setMaximumSize(new Dimension(400, 380));

        // Separador superior
        panelFormulario.add(Box.createVerticalStrut(15));

        // Etiqueta de instrucción
        JLabel labelInstruccion = new JLabel("Ingrese sus credenciales para acceder");
        labelInstruccion.setFont(EstilosUI.FUENTE_NORMAL);
        labelInstruccion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFormulario.add(labelInstruccion);
        panelFormulario.add(Box.createVerticalStrut(10));

        // Campo de usuario
        JLabel labelUsuario = new JLabel("Usuario:");
        labelUsuario.setFont(EstilosUI.FUENTE_NORMAL);
        labelUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFormulario.add(labelUsuario);
        campoUsuario = ComponentesPersonalizados.crearCampoTexto();
        campoUsuario.setMaximumSize(new Dimension(300, 40));
        campoUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFormulario.add(campoUsuario);
        panelFormulario.add(Box.createVerticalStrut(10));

        // Campo de contraseña
        JLabel labelPassword = new JLabel("Contraseña:");
        labelPassword.setFont(EstilosUI.FUENTE_NORMAL);
        labelPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFormulario.add(labelPassword);
        campoPassword = ComponentesPersonalizados.crearCampoPassword();
        campoPassword.setMaximumSize(new Dimension(300, 40));
        campoPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFormulario.add(campoPassword);
        panelFormulario.add(Box.createVerticalStrut(10));

        // Campo de tipo de usuario
        JLabel labelTipoUsuario = new JLabel("Tipo de usuario:");
        labelTipoUsuario.setFont(EstilosUI.FUENTE_NORMAL);
        labelTipoUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFormulario.add(labelTipoUsuario);
        comboTipoUsuario = new JComboBox<>(new String[]{"Administrador", "Empleado", "Cliente"});
        comboTipoUsuario.setFont(EstilosUI.FUENTE_NORMAL);
        comboTipoUsuario.setMaximumSize(new Dimension(300, 40));
        comboTipoUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFormulario.add(comboTipoUsuario);
        panelFormulario.add(Box.createVerticalStrut(20));

        // Panel para botones
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
        botonCancelar = ComponentesPersonalizados.crearBotonSecundario("Cancelar");
        botonCancelar.setMaximumSize(new Dimension(115, 45));
        botonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panelBotones.add(botonCancelar);
        panelBotones.add(Box.createHorizontalStrut(10));
        botonIngresar = ComponentesPersonalizados.crearBotonPrimario("Ingresar");
        botonIngresar.setMaximumSize(new Dimension(175, 45));
        botonIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                intentarLogin();
            }
        });
        panelBotones.add(botonIngresar);
        panelFormulario.add(panelBotones);
        panelFormulario.add(Box.createVerticalStrut(10));

        // Agregar paneles al panel principal
        panelPrincipal.add(panelSuperior);
        panelPrincipal.add(panelFormulario);
        
        // Agregar panel principal a la ventana
        setContentPane(panelPrincipal);
        
        // Asignar acción por defecto al presionar Enter
        getRootPane().setDefaultButton(botonIngresar);
    }
    
    private void intentarLogin() {
        String usuario = campoUsuario.getText();
        String password = new String(campoPassword.getPassword());
        tipoUsuario = (String) comboTipoUsuario.getSelectedItem();
        
        // Aquí iría la lógica real de autenticación
        // Por ahora, simulamos con credenciales predefinidas
        
        if (tipoUsuario.equals("Administrador") && usuario.equals("admin") && password.equals("admin123")) {
            loginExitoso = true;
            nombreUsuario = "Administrador";
            dispose();
        } else if (tipoUsuario.equals("Empleado") && usuario.equals("empleado") && password.equals("emp123")) {
            loginExitoso = true;
            nombreUsuario = "Juan Pérez (Empleado)";
            dispose();
        } else if (tipoUsuario.equals("Cliente") && usuario.equals("cliente") && password.equals("cli123")) {
            loginExitoso = true;
            nombreUsuario = "María López (Cliente)";
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Credenciales incorrectas. Por favor, intente nuevamente.",
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE);
            campoPassword.setText("");
            campoPassword.requestFocus();
        }
    }
    
    /**
     * Verifica si el login fue exitoso
     * @return true si el login fue exitoso, false en caso contrario
     */
    public boolean isLoginExitoso() {
        return loginExitoso;
    }
    
    /**
     * Obtiene el tipo de usuario que inició sesión
     * @return String con el tipo de usuario
     */
    public String getTipoUsuario() {
        return tipoUsuario;
    }
    
    /**
     * Obtiene el nombre del usuario que inició sesión
     * @return String con el nombre del usuario
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }
}

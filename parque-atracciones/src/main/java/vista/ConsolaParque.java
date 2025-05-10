package vista;

import aplicacion.*;
import infraestructura.persistencia.*;
import dominio.empleado.*;
import java.util.Scanner;
import java.util.Optional;

/**
 * Consola principal para la interacción con el sistema del parque de diversiones.
 * Sirve como punto de entrada para seleccionar el rol del usuario y dirigir al
 * sistema correspondiente: cliente, empleado o administrador.
 *
 * <b>Permite:</b>
 * <ul>
 *   <li>Selección de rol (cliente/empleado)</li>
 *   <li>Autenticación para empleados y administradores</li>
 *   <li>Redirección a la consola específica según el tipo de usuario</li>
 * </ul>
 * 
 * @autor Sistema Parque
 */
public class ConsolaParque {
    /**
     * Punto de entrada principal de la consola.
     *
     * <b>Precondiciones:</b> Ninguna.
     * <b>Poscondiciones:</b> Permite la interacción con el usuario.
     *
     * @param args Argumentos de línea de comandos (no usados).
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Define base names for directories/files within the 'data' folder
        String elementosDirName = "elementos.json"; // Directory containing mecanicas.json, culturales.json
        String espectaculosFileName = "atracciones.json"; // File for espectaculos
        String empleadosFileName = "empleados.json";
        String tiquetesFileName = "tiquetes.json";

        try {
            // Initialize repositories
            ElementoParqueRepositoryJson repoElementos = new ElementoParqueRepositoryJson(elementosDirName, espectaculosFileName);
            EmpleadoRepositoryJson repoEmpleados = new EmpleadoRepositoryJson(empleadosFileName);
            TiqueteRepositoryJson repoTiquetes = new TiqueteRepositoryJson(tiquetesFileName);

            // Initialize services
            ServicioGestionElementosParque servicioElementos = new ServicioGestionElementosParque(repoElementos);
            ServicioGestionEmpleados servicioEmpleados = new ServicioGestionEmpleados(repoEmpleados);
            ServicioVentaTiquetes servicioTiquetes = new ServicioVentaTiquetes(repoTiquetes, id -> servicioElementos.consultarAtraccionPorId(id).orElse(null));
            ServicioGestionClientes servicioClientes = new ServicioGestionClientes();
            System.out.println("Bienvenido al sistema del Parque de Diversiones");

            // Main Loop
            boolean salir = false;
            while (!salir) {
                System.out.println("\nSeleccione su rol:");
                System.out.println("1. Cliente");
                System.out.println("2. Empleado");
                System.out.println("3. Salir");
                
                String opcion = sc.nextLine();
                
                switch (opcion) {
                    case "1":
                        // Acceso directo a la consola de cliente sin autenticación
                        ConsolaCliente consolaCliente = new ConsolaCliente(sc, servicioElementos, servicioTiquetes,servicioClientes);
                        consolaCliente.iniciar();
                        break;
                        
                    case "2":
                        // Autenticación de empleado/administrador
                        if (autenticarYDirigir(sc, servicioElementos, servicioEmpleados, servicioTiquetes)) {
                            // La autenticación fue exitosa y ya se dirigió a la consola correspondiente
                        } else {
                            System.out.println("Autenticación fallida. Verifique sus credenciales e intente nuevamente.");
                        }
                        break;
                        
                    case "3":
                        salir = true;
                        break;
                        
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            }

            System.out.println("Gracias por usar el sistema del Parque de Diversiones.");

        } catch (Exception e) {
            System.err.println("Error fatal durante la inicialización o ejecución: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            System.out.println("El sistema no pudo iniciar correctamente. Saliendo.");
        } finally {
            sc.close(); // Close scanner in finally block
        }
    }

    /**
     * Autentica al usuario como empleado o administrador y lo dirige a la consola correspondiente.
     * 
     * @param sc Scanner para entrada del usuario
     * @param servicioElementos Servicio de gestión de elementos del parque
     * @param servicioEmpleados Servicio de gestión de empleados
     * @param servicioTiquetes Servicio de venta de tiquetes
     * @return true si la autenticación fue exitosa, false en caso contrario
     */
    private static boolean autenticarYDirigir(Scanner sc, ServicioGestionElementosParque servicioElementos,
                                         ServicioGestionEmpleados servicioEmpleados,
                                         ServicioVentaTiquetes servicioTiquetes) {
        System.out.println("\n--- AUTENTICACIÓN DE EMPLEADO ---");
        System.out.print("Ingrese su identificación: ");
        String id = sc.nextLine();
        
        System.out.print("Ingrese su contraseña: ");
        String password = sc.nextLine();
        
        // Verificar credenciales
        Optional<Empleado> empleadoOpt = servicioEmpleados.consultarEmpleadoPorIdentificacion(id);
        
        if (empleadoOpt.isPresent() && empleadoOpt.get().verificarPassword(password)) {
            Empleado empleado = empleadoOpt.get();
            
            // Dirigir a la consola según el tipo de empleado
            if (empleado instanceof Administrador) {
                // Dirigir a la consola de administrador
                ConsolaAdministrador consolaAdmin = new ConsolaAdministrador(
                    sc, (Administrador) empleado, servicioElementos, servicioEmpleados, servicioTiquetes
                );
                consolaAdmin.iniciar();
            } else {
                // Dirigir a la consola de empleado regular
                ConsolaEmpleado consolaEmpleado = new ConsolaEmpleado(
                    sc, empleado, servicioElementos, servicioEmpleados, servicioTiquetes
                );
                consolaEmpleado.iniciar();
            }
            
            return true; // Autenticación exitosa
        }
        
        return false; // Autenticación fallida
    }
}

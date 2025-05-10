package vista;

import aplicacion.*;
import dominio.elementoparque.*;
import dominio.tiquete.*;
import dominio.usuario.Cliente;
import dominio.usuario.Usuario;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * Consola específica para clientes del parque de diversiones.
 * Permite realizar operaciones como consultar atracciones y comprar tiquetes
 * sin requerir autenticación.
 *
 * <b>Permite:</b>
 * <ul>
 *   <li>Consultar atracciones disponibles</li>
 *   <li>Consultar precios de tiquetes</li>
 *   <li>Comprar tiquetes</li>
 * </ul>
 *
 * @author Sistema Parque
 */
public class ConsolaCliente {
    private Scanner sc;
    private ServicioGestionElementosParque servicioElementos;
    private ServicioVentaTiquetes servicioTiquetes;
    private aplicacion.ServicioGestionClientes servicioClientes;

    /**
     * Constructor de la consola de cliente.
     *
     * @param sc Scanner para entrada del usuario
     * @param servicioElementos Servicio de gestión de elementos del parque
     * @param servicioTiquetes Servicio de venta de tiquetes
     * @param servicioClientes Servicio de gestión de clientes
     */
    public ConsolaCliente(Scanner sc, ServicioGestionElementosParque servicioElementos, ServicioVentaTiquetes servicioTiquetes, aplicacion.ServicioGestionClientes servicioClientes) {
        this.sc = sc;
        this.servicioElementos = servicioElementos;
        this.servicioTiquetes = servicioTiquetes;
        this.servicioClientes = servicioClientes;
    }

    /**
     * Inicia la consola de cliente.
     */
    public void iniciar() {
        System.out.println("Bienvenido al sistema de cliente del Parque de Diversiones");
        
        boolean continuar = true;
        while (continuar) {
            System.out.println("\nOpciones de Cliente:");
            System.out.println("1. Ver atracciones disponibles");
            System.out.println("2. Comprar tiquete");
            System.out.println("3. Ver mis tiquetes");
            System.out.println("4. Consultar horarios de espectáculos");
            System.out.println("5. Volver al menú principal");
            
            String opcion = sc.nextLine();
            
            try {
                switch (opcion) {
                    case "1":
                        mostrarAtracciones();
                        break;
                    case "2":
                        comprarTiquete();
                        break;
                    case "3":
                        consultarTiquetes();
                        break;
                    case "4":
                        consultarHorariosEspectaculos();
                        break;
                    case "5":
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        
        System.out.println("Regresando al menú principal...");
    }

    /**
     * Muestra las atracciones disponibles en el parque.
     */
    private void mostrarAtracciones() {
        System.out.println("\n--- ATRACCIONES DISPONIBLES ---");
        servicioElementos.consultarTodasLasAtracciones().forEach(a -> 
            System.out.println(a.getId() + ": " + a.getNombre() + " - Ubicación: " + a.getUbicacion())
        );
    }

    /**
     * Permite al cliente comprar un tiquete.
     */
    private void comprarTiquete() {
        try {
            System.out.println("\n--- COMPRA DE TIQUETES ---");
            System.out.print("¿Tiene un usuario creado? (s/n): ");
            String tieneUsuario = sc.nextLine().trim().toLowerCase();
            Cliente cliente = null;
            if (tieneUsuario.equals("s")) {
                System.out.print("Nombre de usuario: ");
                String username = sc.nextLine();
                System.out.print("Contraseña: ");
                String password = sc.nextLine();
                if (!servicioClientes.userExists(username)) {
                    System.out.println("[ERROR] Usuario no encontrado.");
                    return;
                }
                if (!servicioClientes.isPasswordCorrect(username, password)) {
                    System.out.println("[ERROR] Contraseña incorrecta.");
                    return;
                }
                Usuario usuario = servicioClientes.getUser(username);
                if (usuario instanceof Cliente) {
                    cliente = (Cliente) usuario;
                } else {
                    System.out.println("[ERROR] El usuario no es un cliente válido.");
                    return;
                }
            } else if (tieneUsuario.equals("n")) {
                System.out.print("Nombre de usuario: ");
                String username = sc.nextLine();
                System.out.print("Contraseña: ");
                String password = sc.nextLine();
                System.out.print("Nombre: ");
                String nomC = sc.nextLine();
                System.out.print("ID cliente: ");
                String idC = sc.nextLine();
                if (idC == null || idC.trim().isEmpty()) {
                    System.out.println("[ERROR] El ID del cliente no puede estar vacío.");
                    return;
                }
                System.out.print("Email: ");
                String emailC = sc.nextLine();
                if (emailC == null || emailC.trim().isEmpty()) {
                    emailC = idC + "@test.com";
                }
                cliente = new Cliente(username, password, nomC, idC, emailC, "", LocalDate.of(2000,1,1), 1.7, 70);
                if (!servicioClientes.addUser(cliente)) {
                    System.out.println("[ERROR] No se pudo crear el usuario. Puede que ya exista.");
                    return;
                }
            } else {
                System.out.println("[ERROR] Opción no válida. Por favor, responda con 's' o 'n'.");
                return;
            }
            boolean volver = false;
            while (!volver) {
                System.out.println("\nTipos de tiquete disponibles:");
                System.out.println("1. Tiquete General");
                System.out.println("2. Tiquete de Temporada");
                System.out.println("3. Entrada Individual");
                System.out.println("4. FastPass");
                System.out.println("0. Volver al menú de cliente");
                System.out.print("Seleccione tipo de tiquete: ");
                String opcionTiquete = sc.nextLine();
                switch (opcionTiquete) {
                    case "1":
                        comprarTiqueteGeneral(cliente);
                        volver = true;
                        break;
                    case "2":
                        comprarTiqueteTemporada(cliente);
                        volver = true;
                        break;
                    case "3":
                        comprarTiqueteIndividual(cliente);
                        volver = true;
                        break;
                    case "4":
                        comprarFastPass(cliente);
                        volver = true;
                        break;
                    case "0":
                        volver = true;
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, seleccione una opción del 0 al 4.");
                }
            }
        } catch (Exception ex) {
            System.out.println("[ERROR] " + ex.getClass().getName() + ": " + ex.getMessage());
        }
    }

    /**
     * Compra un tiquete general.
     */
    private void comprarTiqueteGeneral(Cliente cliente) {
        System.out.println("\n--- TIQUETE GENERAL ---");
        System.out.println("Categorías disponibles: FAMILIAR, ORO, DIAMANTE, BASICO");
        System.out.print("Seleccione categoría: ");
        String catStr = sc.nextLine();
        CategoriaTiquete cat = CategoriaTiquete.valueOf(catStr);
        
        System.out.print("Precio: ");
        double precio = Double.parseDouble(sc.nextLine());
        
        TiqueteGeneral tg = servicioTiquetes.venderTiqueteGeneral(cliente, cat, precio);
        System.out.println("Tiquete general vendido exitosamente. Código: " + tg.getCodigo());
    }

    /**
     * Compra un tiquete de temporada.
     */
    private void comprarTiqueteTemporada(Cliente cliente) {
        System.out.println("\n--- TIQUETE DE TEMPORADA ---");
        System.out.println("Categorías disponibles: FAMILIAR, ORO, DIAMANTE");
        System.out.print("Seleccione categoría: ");
        String catStr = sc.nextLine();
        CategoriaTiquete cat = CategoriaTiquete.valueOf(catStr);
        
        System.out.print("Fecha de inicio (YYYY-MM-DDTHH:MM:SS): ");
        LocalDateTime fechaInicio = LocalDateTime.parse(sc.nextLine());
        
        System.out.print("Fecha de fin (YYYY-MM-DDTHH:MM:SS): ");
        LocalDateTime fechaFin = LocalDateTime.parse(sc.nextLine());
        
        System.out.print("Precio: ");
        double precio = Double.parseDouble(sc.nextLine());
        
        TiqueteTemporada tt = servicioTiquetes.venderTiqueteTemporada(cliente, cat, fechaInicio, fechaFin, precio);
        System.out.println("Tiquete de temporada vendido exitosamente. Código: " + tt.getCodigo());
    }

    /**
     * Compra una entrada individual.
     */
    private void comprarTiqueteIndividual(Cliente cliente) {
        System.out.println("\n--- ENTRADA INDIVIDUAL ---");
        
        // Mostrar atracciones disponibles
        mostrarAtracciones();
        
        System.out.print("Ingrese ID de la atracción: ");
        String idAtraccion = sc.nextLine();
        
        Atraccion atraccion = servicioElementos.consultarAtraccionPorId(idAtraccion).orElse(null);
        if (atraccion == null) {
            System.out.println("Atracción no encontrada.");
            return;
        }
        
        System.out.print("Precio: ");
        double precio = Double.parseDouble(sc.nextLine());
        
        EntradaIndividual ei = servicioTiquetes.venderEntradaIndividual(cliente, atraccion, precio);
        System.out.println("Entrada individual vendida exitosamente. Código: " + ei.getCodigo());
    }

    /**
     * Compra un FastPass.
     */
    private void comprarFastPass(Cliente cliente) {
        System.out.println("\n--- FASTPASS ---");
        System.out.print("Fecha válida (YYYY-MM-DDTHH:MM:SS): ");
        LocalDateTime fecha = LocalDateTime.parse(sc.nextLine());
        
        System.out.print("Precio: ");
        double precio = Double.parseDouble(sc.nextLine());
        
        FastPass fp = servicioTiquetes.venderFastPass(cliente, fecha, precio);
        System.out.println("FastPass vendido exitosamente. Código: " + fp.getCodigo());
    }

    /**
     * Consulta los tiquetes de un cliente.
     */
    private void consultarTiquetes() {
        System.out.print("Ingrese su ID de cliente: ");
        String idCliente = sc.nextLine();
        
        System.out.println("\n--- SUS TIQUETES ---");
        servicioTiquetes.consultarTiquetesPorUsuario(idCliente).forEach(t -> 
            System.out.println("Código: " + t.getCodigo() + " - Tipo: " + t.getClass().getSimpleName())
        );
    }

    /**
     * Consulta los horarios de los espectáculos.
     */
    private void consultarHorariosEspectaculos() {
        System.out.println("\n--- HORARIOS DE ESPECTÁCULOS ---");
        // Corrección: Usar consultarTodosLosElementos() para incluir Espectaculos
        servicioElementos.consultarTodosLosElementos().stream() 
            .filter(e -> e instanceof Espectaculo) // Filtrar solo los Espectaculos
            .map(e -> (Espectaculo) e) // Castear a Espectaculo
            .forEach(esp -> { // Cambiar nombre de variable para claridad
                System.out.println("\nEspectáculo: " + esp.getNombre() + " (" + esp.getId() + ")");
                System.out.println("Ubicación: " + esp.getUbicacion());
                System.out.println("Descripción: " + esp.getDescripcion());
                System.out.println("Horarios:");
                esp.getHorarios().forEach(h -> 
                    System.out.println("  - Desde: " + h.getInicio() + " hasta: " + h.getFin())
                );
            });
    }
}

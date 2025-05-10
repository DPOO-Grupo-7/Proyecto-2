package vista;

import aplicacion.*;
import dominio.elementoparque.*;
import dominio.empleado.*;
import dominio.tiquete.*;
import java.util.Scanner;

/**
 * Consola específica para empleados del parque de diversiones.
 * Permite realizar operaciones básicas como consultar atracciones, 
 * vender tiquetes y gestionar visitantes.
 *
 * <b>Permite:</b>
 * <ul>
 *   <li>Consultar atracciones</li>
 *   <li>Consultar empleados</li>
 *   <li>Vender tiquetes a clientes</li>
 *   <li>Consultar tiquetes por usuario</li>
 * </ul>
 *
 * @author Sistema Parque
 */
public class ConsolaEmpleado {
    private Scanner sc;
    private Empleado empleado;
    private ServicioGestionElementosParque servicioElementos;
    private ServicioGestionEmpleados servicioEmpleados;
    private ServicioVentaTiquetes servicioTiquetes;

    /**
     * Constructor de la consola de empleado.
     *
     * @param sc Scanner para entrada del usuario
     * @param empleado Empleado autenticado
     * @param servicioElementos Servicio de gestión de elementos del parque
     * @param servicioEmpleados Servicio de gestión de empleados
     * @param servicioTiquetes Servicio de venta de tiquetes
     */
    public ConsolaEmpleado(Scanner sc, Empleado empleado, ServicioGestionElementosParque servicioElementos, 
                           ServicioGestionEmpleados servicioEmpleados, ServicioVentaTiquetes servicioTiquetes) {
        this.sc = sc;
        this.empleado = empleado;
        this.servicioElementos = servicioElementos;
        this.servicioEmpleados = servicioEmpleados;
        this.servicioTiquetes = servicioTiquetes;
    }

    /**
     * Inicia la consola de empleado.
     */
    public void iniciar() {
        System.out.println("Bienvenido, " + empleado.getNombre() + " (" + empleado.getClass().getSimpleName() + ")");
        
        boolean continuar = true;
        while (continuar) {
            System.out.println("\nOpciones de Empleado:");
            System.out.println("1. Ver atracciones");
            System.out.println("2. Ver empleados");
            System.out.println("3. Vender tiquete");
            System.out.println("4. Consultar tiquetes por usuario");
            System.out.println("5. Volver al menú principal");
            
            String opcion = sc.nextLine();
            
            try {
                switch (opcion) {
                    case "1":
                        verAtracciones();
                        break;
                    case "2":
                        verEmpleados();
                        break;
                    case "3":
                        venderTiquete();
                        break;
                    case "4":
                        consultarTiquetesPorUsuario();
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
     * Muestra todas las atracciones del parque.
     */
    private void verAtracciones() {
        System.out.println("\n--- ATRACCIONES DEL PARQUE ---");
        servicioElementos.consultarTodasLasAtracciones().forEach(a -> 
            System.out.println(a.getId() + ": " + a.getNombre() + " - Ubicación: " + a.getUbicacion())
        );
    }

    /**
     * Muestra todos los empleados del parque.
     */
    private void verEmpleados() {
        System.out.println("\n--- EMPLEADOS DEL PARQUE ---");
        servicioEmpleados.consultarTodosLosEmpleados().forEach(e -> 
            System.out.println(e.getIdentificacion() + ": " + e.getNombre() + " - " + e.getClass().getSimpleName())
        );
    }

    /**
     * Permite vender un tiquete a un cliente.
     */
    private void venderTiquete() {
        try {
            System.out.println("\n--- VENTA DE TIQUETES ---");
            
            System.out.print("ID comprador: ");
            String idC = sc.nextLine();
            if (idC == null || idC.trim().isEmpty()) {
                System.out.println("[ERROR] El ID del comprador no puede estar vacío o ser nulo. Abortando venta de tiquete.");
                return;
            }
            
            System.out.print("Nombre comprador: ");
            String nomC = sc.nextLine();
            
            System.out.print("Email comprador: ");
            String emailC = sc.nextLine();
            if (emailC == null || emailC.trim().isEmpty()) {
                emailC = idC + "@test.com";
            }
            
            System.out.print("Tipo de tiquete (general/temporada/individual/fastpass): ");
            String tipo = sc.nextLine();
            
            dominio.usuario.Cliente comprador = new dominio.usuario.Cliente(idC, nomC, nomC, idC, emailC, "", java.time.LocalDate.now(), 1.7, 70);
            
            if (tipo.equals("general")) {
                System.out.print("Categoría (FAMILIAR/ORO/DIAMANTE/BASICO): ");
                String catStr = sc.nextLine();
                CategoriaTiquete cat = CategoriaTiquete.valueOf(catStr);
                
                System.out.print("Precio: ");
                String precioStr = sc.nextLine();
                double precio = Double.parseDouble(precioStr);
                
                TiqueteGeneral tg = servicioTiquetes.venderTiqueteGeneral(comprador, cat, precio);
                System.out.println("Tiquete vendido: " + tg.getCodigo());
                
            } else if (tipo.equals("temporada")) {
                System.out.print("Categoría (FAMILIAR/ORO/DIAMANTE): ");
                String catStr = sc.nextLine();
                CategoriaTiquete cat = CategoriaTiquete.valueOf(catStr);
                
                System.out.print("Fecha de inicio (YYYY-MM-DDTHH:MM:SS): ");
                String iniStr = sc.nextLine();
                java.time.LocalDateTime ini = java.time.LocalDateTime.parse(iniStr);
                
                System.out.print("Fecha de fin (YYYY-MM-DDTHH:MM:SS): ");
                String finStr = sc.nextLine();
                java.time.LocalDateTime fin = java.time.LocalDateTime.parse(finStr);
                
                System.out.print("Precio: ");
                String precioStr = sc.nextLine();
                double precio = Double.parseDouble(precioStr);
                
                TiqueteTemporada tt = servicioTiquetes.venderTiqueteTemporada(comprador, cat, ini, fin, precio);
                System.out.println("Tiquete vendido: " + tt.getCodigo());
                
            } else if (tipo.equals("individual")) {
                System.out.print("ID de la atracción: ");
                String idA = sc.nextLine();
                
                Atraccion atr = servicioElementos.consultarAtraccionPorId(idA).orElse(null);
                if (atr == null) {
                    System.out.println("Atracción no encontrada.");
                    return;
                }
                
                System.out.print("Precio: ");
                String precioStr = sc.nextLine();
                double precio = Double.parseDouble(precioStr);
                
                EntradaIndividual ei = servicioTiquetes.venderEntradaIndividual(comprador, atr, precio);
                System.out.println("Tiquete vendido: " + ei.getCodigo());
                
            } else if (tipo.equals("fastpass")) {
                System.out.print("Fecha válida (YYYY-MM-DDTHH:MM:SS): ");
                String fechaStr = sc.nextLine();
                java.time.LocalDateTime fecha = java.time.LocalDateTime.parse(fechaStr);
                
                System.out.print("Precio: ");
                String precioStr = sc.nextLine();
                double precio = Double.parseDouble(precioStr);
                
                FastPass fp = servicioTiquetes.venderFastPass(comprador, fecha, precio);
                System.out.println("Tiquete vendido: " + fp.getCodigo());
            }
        } catch (Exception ex) {
            System.out.println("[ERROR] " + ex.getClass().getName() + ": " + ex.getMessage());
        }
    }

    /**
     * Permite consultar los tiquetes de un usuario específico.
     */
    private void consultarTiquetesPorUsuario() {
        System.out.print("ID usuario: ");
        String idU = sc.nextLine();
        
        System.out.println("\n--- TIQUETES DEL USUARIO ---");
        servicioTiquetes.consultarTiquetesPorUsuario(idU).forEach(t -> 
            System.out.println(t.getCodigo() + ": " + t.getClass().getSimpleName() + " - Fecha Emisión: " + t.getFechaHoraEmision())
        );
    }
}
package vista;

import aplicacion.*;
import dominio.elementoparque.*;
import dominio.empleado.*;
import dominio.tiquete.*;
import dominio.trabajo.Turno;
import dominio.trabajo.LugarTrabajo;
import java.util.Scanner;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * Consola específica para administradores del parque de diversiones.
 * Permite realizar todas las operaciones administrativas del sistema.
 *
 * <b>Permite:</b>
 * <ul>
 *   <li>Gestión completa de atracciones</li>
 *   <li>Gestión completa de empleados</li>
 *   <li>Ventas y consultas de tiquetes</li>
 *   <li>Asignación de turnos</li>
 *   <li>Consultas avanzadas</li>
 * </ul>
 *
 * @author Sistema Parque
 */
public class ConsolaAdministrador {
    private Scanner sc;
    private Administrador admin;
    private ServicioGestionElementosParque servicioElementos;
    private ServicioGestionEmpleados servicioEmpleados;
    private ServicioVentaTiquetes servicioTiquetes;

    /**
     * Constructor de la consola de administrador.
     *
     * @param sc Scanner para entrada del usuario
     * @param admin Administrador autenticado
     * @param servicioElementos Servicio de gestión de elementos del parque
     * @param servicioEmpleados Servicio de gestión de empleados
     * @param servicioTiquetes Servicio de venta de tiquetes
     */
    public ConsolaAdministrador(Scanner sc, Administrador admin, ServicioGestionElementosParque servicioElementos, 
                               ServicioGestionEmpleados servicioEmpleados, ServicioVentaTiquetes servicioTiquetes) {
        this.sc = sc;
        this.admin = admin;
        this.servicioElementos = servicioElementos;
        this.servicioEmpleados = servicioEmpleados;
        this.servicioTiquetes = servicioTiquetes;
    }

    /**
     * Inicia la consola de administrador.
     */
    public void iniciar() {
        System.out.println("Bienvenido, " + admin.getNombre() + " (Administrador)");
        System.out.println("Áreas de responsabilidad: " + String.join(", ", admin.getAreasResponsabilidad()));
        
        boolean continuar = true;
        while (continuar) {
            System.out.println("\nOpciones de Administrador:");
            System.out.println("1. Ver atracciones");
            System.out.println("2. Ver empleados");
            System.out.println("3. Vender tiquete");
            System.out.println("4. Consultar tiquetes por usuario");
            System.out.println("5. Crear empleado");
            System.out.println("6. Crear atracción cultural");
            System.out.println("7. Crear atracción mecánica");
            System.out.println("8. Crear espectáculo");
            System.out.println("9. Actualizar atracción");
            System.out.println("10. Eliminar atracción");
            System.out.println("11. Actualizar empleado");
            System.out.println("12. Eliminar empleado");
            System.out.println("13. Asignar turno");
            System.out.println("14. Consultas avanzadas");
            System.out.println("15. Volver al menú principal");
            
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
                        crearEmpleado();
                        break;
                    case "6":
                        crearAtraccionCultural();
                        break;
                    case "7":
                        crearAtraccionMecanica();
                        break;
                    case "8":
                        crearEspectaculo();
                        break;
                    case "9":
                        actualizarAtraccion();
                        break;
                    case "10":
                        eliminarAtraccion();
                        break;
                    case "11":
                        actualizarEmpleado();
                        break;
                    case "12":
                        eliminarEmpleado();
                        break;
                    case "13":
                        asignarTurno();
                        break;
                    case "14":
                        consultasAvanzadas();
                        break;
                    case "15":
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
                ex.printStackTrace();
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
            System.out.println(a.getId() + ": " + a.getNombre() + " - Tipo: " + a.getClass().getSimpleName())
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
            
            dominio.usuario.Cliente comprador = new dominio.usuario.Cliente(idC, nomC, nomC, idC, emailC, "", LocalDate.now(), 1.7, 70);
            
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
                LocalDateTime ini = LocalDateTime.parse(iniStr);
                
                System.out.print("Fecha de fin (YYYY-MM-DDTHH:MM:SS): ");
                String finStr = sc.nextLine();
                LocalDateTime fin = LocalDateTime.parse(finStr);
                
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
                LocalDateTime fecha = LocalDateTime.parse(fechaStr);
                
                System.out.print("Precio: ");
                String precioStr = sc.nextLine();
                double precio = Double.parseDouble(precioStr);
                
                FastPass fp = servicioTiquetes.venderFastPass(comprador, fecha, precio);
                System.out.println("Tiquete vendido: " + fp.getCodigo());
            }
        } catch (Exception ex) {
            System.out.println("[ERROR] " + ex.getClass().getName() + ": " + ex.getMessage());
            ex.printStackTrace();
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
            System.out.println(t.getCodigo() + ": " + t.getClass().getSimpleName() + " - Precio: " + t.getPrecio())
        );
    }

    /**
     * Permite crear un nuevo empleado.
     */
    private void crearEmpleado() {
        System.out.println("\n--- CREAR EMPLEADO ---");
        
        System.out.print("Tipo (Cajero/Cocinero/OperarioAtraccion/ServicioGeneral/Administrador): ");
        String tipoEmp = sc.nextLine();
        
        System.out.print("ID: ");
        String idE = sc.nextLine();
        
        System.out.print("Nombre: ");
        String nomE = sc.nextLine();
        
        System.out.print("Email: ");
        String email = sc.nextLine();
        
        System.out.print("Teléfono: ");
        String telefono = sc.nextLine();
        
        System.out.print("Username: ");
        String username = sc.nextLine();
        
        System.out.print("Password: ");
        String passE = sc.nextLine();
        
        Empleado nuevo = null;
        
        switch (tipoEmp) {
            case "Cajero":
                System.out.print("Caja asignada: ");
                int caja = Integer.parseInt(sc.nextLine());
                
                System.out.print("Punto de venta: ");
                String puntoVenta = sc.nextLine();
                
                nuevo = new Cajero(idE, nomE, email, telefono, username, passE, caja, puntoVenta);
                break;
                
            case "Cocinero":
                System.out.print("Especialidad: ");
                String esp = sc.nextLine();
                
                nuevo = new Cocinero(idE, nomE, email, telefono, username, passE, esp);
                break;
                
            case "OperarioAtraccion":
                System.out.print("Certificado de seguridad (true/false): ");
                boolean certificado = Boolean.parseBoolean(sc.nextLine());
                
                System.out.print("IDs atracciones capacitadas (coma): ");
                List<String> atrs = Arrays.asList(sc.nextLine().split(","));
                
                nuevo = new OperarioAtraccion(idE, nomE, email, telefono, username, passE, certificado, atrs);
                break;
                
            case "ServicioGeneral":
                nuevo = new ServicioGeneral(idE, nomE, email, telefono, username, passE);
                break;
                
            case "Administrador":
                System.out.print("Áreas de responsabilidad (coma): ");
                String areas = sc.nextLine();
                
                nuevo = new Administrador(idE, nomE, email, telefono, username, passE, Arrays.asList(areas.split(",")));
                break;
        }
        
        if (nuevo != null) {
            servicioEmpleados.registrarEmpleado(nuevo);
            System.out.println("Empleado creado exitosamente.");
        }
    }

    /**
     * Permite crear una nueva atracción cultural.
     */
    private void crearAtraccionCultural() {
        System.out.println("\n--- CREAR ATRACCIÓN CULTURAL ---");
        
        System.out.print("ID: ");
        String idA = sc.nextLine();
        
        System.out.print("Nombre: ");
        String nomA = sc.nextLine();
        
        System.out.print("Ubicación: ");
        String ubi = sc.nextLine();
        
        System.out.print("Cupo máximo: ");
        int cupo = Integer.parseInt(sc.nextLine());
        
        System.out.print("Empleados mínimos: ");
        int minEmp = Integer.parseInt(sc.nextLine());
        
        System.out.print("Edad mínima: ");
        int edadMin = Integer.parseInt(sc.nextLine());
        
        System.out.print("Nivel exclusividad (FAMILIAR/ORO/DIAMANTE): ");
        NivelExclusividad ne = NivelExclusividad.valueOf(sc.nextLine());
        
        AtraccionCultural ac = servicioElementos.crearAtraccionCultural(idA, nomA, ubi, cupo, minEmp, edadMin, Collections.emptyList(), ne);
        System.out.println("Atracción cultural creada: " + ac.getId());
    }

    /**
     * Permite crear una nueva atracción mecánica.
     */
    private void crearAtraccionMecanica() {
        System.out.println("\n--- CREAR ATRACCIÓN MECÁNICA ---");
        
        System.out.print("ID: ");
        String idA = sc.nextLine();
        
        System.out.print("Nombre: ");
        String nomA = sc.nextLine();
        
        System.out.print("Ubicación: ");
        String ubi = sc.nextLine();
        
        System.out.print("Cupo máximo: ");
        int cupo = Integer.parseInt(sc.nextLine());
        
        System.out.print("Empleados mínimos: ");
        int minEmp = Integer.parseInt(sc.nextLine());
        
        System.out.print("Nivel exclusividad (FAMILIAR/ORO/DIAMANTE): ");
        NivelExclusividad ne = NivelExclusividad.valueOf(sc.nextLine());
        
        System.out.print("Nivel riesgo (MEDIO/ALTO): ");
        NivelRiesgo nr = NivelRiesgo.valueOf(sc.nextLine());
        
        System.out.print("Altura mínima: ");
        double altMin = Double.parseDouble(sc.nextLine());
        
        System.out.print("Altura máxima: ");
        double altMax = Double.parseDouble(sc.nextLine());
        
        System.out.print("Peso mínimo: ");
        double pesoMin = Double.parseDouble(sc.nextLine());
        
        System.out.print("Peso máximo: ");
        double pesoMax = Double.parseDouble(sc.nextLine());
        
        System.out.print("Contraindicaciones (coma): ");
        List<String> contra = Arrays.asList(sc.nextLine().split(","));
        
        System.out.print("Restricciones (coma): ");
        List<String> restr = Arrays.asList(sc.nextLine().split(","));
        
        Capacitacion cap = Capacitacion.OPERACION_ATRACCION_RIESGO_ALTO;
        
        AtraccionMecanica am = servicioElementos.crearAtraccionMecanica(idA, nomA, ubi, cupo, minEmp, ne, nr, altMin, altMax, 
                                                                        pesoMin, pesoMax, contra, restr, Collections.emptyList(), cap);
        System.out.println("Atracción mecánica creada: " + am.getId());
    }

    /**
     * Permite crear un nuevo espectáculo.
     */
    private void crearEspectaculo() {
        System.out.println("\n--- CREAR ESPECTÁCULO ---");
        
        System.out.print("ID: ");
        String idE = sc.nextLine();
        
        System.out.print("Nombre: ");
        String nomE = sc.nextLine();
        
        System.out.print("Ubicación: ");
        String ubi = sc.nextLine();
        
        System.out.print("Cupo máximo: ");
        int cupo = Integer.parseInt(sc.nextLine());
        
        System.out.print("Descripción: ");
        String desc = sc.nextLine();
        
        System.out.print("Cantidad de horarios: ");
        int nHor = Integer.parseInt(sc.nextLine());
        
        List<dominio.util.RangoFechaHora> horarios = new ArrayList<>();
        
        for (int i = 0; i < nHor; i++) {
            System.out.print("Inicio (YYYY-MM-DDTHH:MM:SS): ");
            LocalDateTime ini = LocalDateTime.parse(sc.nextLine());
            
            System.out.print("Fin (YYYY-MM-DDTHH:MM:SS): ");
            LocalDateTime fin = LocalDateTime.parse(sc.nextLine());
            
            horarios.add(new dominio.util.RangoFechaHora(ini, fin));
        }
        
        Espectaculo esp = servicioElementos.crearEspectaculo(idE, nomE, ubi, cupo, desc, horarios, Collections.emptyList());
        System.out.println("Espectáculo creado: " + esp.getId());
    }

    /**
     * Permite actualizar una atracción existente.
     */
    private void actualizarAtraccion() {
        System.out.println("\n--- ACTUALIZAR ATRACCIÓN ---");
        
        System.out.print("ID de la atracción a actualizar: ");
        String id = sc.nextLine();
        
        Optional<Atraccion> atrOpt = servicioElementos.consultarAtraccionPorId(id);
        if (atrOpt.isEmpty()) {
            System.out.println("Atracción no encontrada.");
            return;
        }
        
        Atraccion atr = atrOpt.get();
        
        System.out.print("Nuevo nombre (actual: " + atr.getNombre() + "): ");
        String nombre = sc.nextLine();
        if (!nombre.isEmpty()) atr.setNombre(nombre);
        
        servicioElementos.actualizarAtraccion(atr);
        System.out.println("Atracción actualizada correctamente.");
    }

    /**
     * Permite eliminar una atracción.
     */
    private void eliminarAtraccion() {
        System.out.println("\n--- ELIMINAR ATRACCIÓN ---");
        
        System.out.print("ID de la atracción a eliminar: ");
        String id = sc.nextLine();
        
        servicioElementos.eliminarAtraccion(id);
        System.out.println("Atracción eliminada correctamente.");
    }

    /**
     * Permite actualizar un empleado existente.
     */
    private void actualizarEmpleado() {
        System.out.println("\n--- ACTUALIZAR EMPLEADO ---");
        
        System.out.print("ID del empleado a actualizar: ");
        String id = sc.nextLine();
        
        Optional<Empleado> empOpt = servicioEmpleados.consultarEmpleadoPorIdentificacion(id);
        if (empOpt.isEmpty()) {
            System.out.println("Empleado no encontrado.");
            return;
        }
        
        Empleado emp = empOpt.get();
        
        System.out.print("Nuevo nombre (actual: " + emp.getNombre() + "): ");
        String nombre = sc.nextLine();
        if (!nombre.isEmpty()) emp.setNombre(nombre);
        
        servicioEmpleados.actualizarEmpleado(emp);
        System.out.println("Empleado actualizado correctamente.");
    }

    /**
     * Permite eliminar un empleado.
     */
    private void eliminarEmpleado() {
        System.out.println("\n--- ELIMINAR EMPLEADO ---");
        
        System.out.print("ID del empleado a eliminar: ");
        String id = sc.nextLine();
        
        servicioEmpleados.eliminarEmpleado(id);
        System.out.println("Empleado eliminado correctamente.");
    }

    /**
     * Permite asignar un turno a un empleado.
     */
    private void asignarTurno() {
        System.out.println("\n--- ASIGNAR TURNO ---");
        
        System.out.print("ID empleado: ");
        String idE = sc.nextLine();
        
        System.out.print("Fecha (YYYY-MM-DD): ");
        LocalDate fecha = LocalDate.parse(sc.nextLine());
        
        System.out.print("Turno (APERTURA/CIERRE): ");
        Turno turno = Turno.valueOf(sc.nextLine());
        
        System.out.print("Lugar trabajo (ID o vacío para ServicioGeneral): ");
        String idL = sc.nextLine();
        
        LugarTrabajo lugar = null;
        if (!idL.isEmpty()) {
            lugar = servicioElementos.consultarAtraccionPorId(idL).orElse(null);
        }
        
        servicioEmpleados.asignarTurno(idE, lugar, fecha, turno);
        System.out.println("Turno asignado correctamente.");
    }

    /**
     * Permite realizar consultas avanzadas.
     */
    private void consultasAvanzadas() {
        System.out.println("\n--- CONSULTAS AVANZADAS ---");
        
        System.out.println("1. Atracciones por exclusividad");
        System.out.println("2. Atracciones mecánicas por riesgo");
        System.out.println("3. Elementos restringidos por clima");
        
        System.out.print("Seleccione una opción: ");
        String op = sc.nextLine();
        
        switch (op) {
            case "1":
                System.out.print("Nivel exclusividad (FAMILIAR/ORO/DIAMANTE): ");
                NivelExclusividad ne = NivelExclusividad.valueOf(sc.nextLine());
                
                System.out.println("\n--- ATRACCIONES POR EXCLUSIVIDAD: " + ne + " ---");
                servicioElementos.consultarAtraccionesPorExclusividad(ne).forEach(a -> 
                    System.out.println(a.getId() + ": " + a.getNombre())
                );
                break;
                
            case "2":
                System.out.print("Nivel riesgo (MEDIO/ALTO): ");
                NivelRiesgo nr = NivelRiesgo.valueOf(sc.nextLine());
                
                System.out.println("\n--- ATRACCIONES MECÁNICAS POR RIESGO: " + nr + " ---");
                servicioElementos.consultarAtraccionesMecanicasPorRiesgo(nr).forEach(a -> 
                    System.out.println(a.getId() + ": " + a.getNombre())
                );
                break;
                
            case "3":
                System.out.print("Clima (LLUVIA/TORMENTA/FRIO/CALOR): ");
                dominio.util.CondicionClimatica clima = dominio.util.CondicionClimatica.valueOf(sc.nextLine());
                
                System.out.println("\n--- ELEMENTOS RESTRINGIDOS POR CLIMA: " + clima + " ---");
                servicioElementos.consultarElementosPorClima(clima).forEach(e -> 
                    System.out.println(e.getId() + ": " + e.getNombre())
                );
                break;
                
            default:
                System.out.println("Opción no válida.");
        }
    }
}
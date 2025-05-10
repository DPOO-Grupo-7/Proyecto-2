package aplicacion;
import dominio.elementoparque.*;
import dominio.empleado.*;
import dominio.trabajo.*; // Imports Tienda, Cafeteria, LugarTrabajo, AsignacionTurno, Turno
import dominio.trabajo.Taquilla; // Explicit import for Taquilla
// Importar excepciones personalizadas
import dominio.excepciones.DatosInvalidosException;
import dominio.excepciones.EmpleadoNoEncontradoException;
import dominio.excepciones.AsignacionInvalidaException;
import dominio.excepciones.CapacitacionInsuficienteException;
import infraestructura.persistencia.EmpleadoRepositoryJson;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de empleados, asignación de turnos y administración de capacitaciones
 * en el parque de diversiones.
 * <p>
 * Este servicio permite registrar empleados, consultar información, asignar turnos a lugares de trabajo
 * (atracciones, cafeterías, tiendas, taquillas o servicio general), y autorizar nuevas capacitaciones.
 * </p>
 *
 * <b>Contexto:</b> Forma parte del sistema administrativo del parque, gestionando la operación diaria
 * y la correcta asignación de personal según los requisitos de cada lugar y turno.
 *
 * <b>Funcionalidades principales:</b>
 * <ul>
 *   <li>Registro y consulta de empleados.</li>
 *   <li>Asignación de turnos a lugares de trabajo o servicio general.</li>
 *   <li>Verificación de requisitos de personal para cada lugar y turno.</li>
 *   <li>Autorización de nuevas capacitaciones para empleados.</li>
 * </ul>
 *
 * <b>Precondiciones generales:</b>
 * <ul>
 *   <li>Los objetos Empleado, LugarTrabajo, fechas y turnos no deben ser nulos.</li>
 *   <li>Las identificaciones deben ser válidas y únicas.</li>
 * </ul>
 *
 * <b>Poscondiciones generales:</b>
 * <ul>
 *   <li>Los empleados quedan registrados y asignados correctamente según las reglas del parque.</li>
 *   <li>Las capacitaciones autorizadas quedan reflejadas en el perfil del empleado.</li>
 * </ul>
 *
 * @author Sistema Parque
 */
public class ServicioGestionEmpleados {

    // --- Placeholders for Repositories ---
    private final Map<String, Empleado> empleados = new ConcurrentHashMap<>(); // Key: identificacion
    private final List<AsignacionTurno> asignaciones = new ArrayList<>();
    private final EmpleadoRepositoryJson empleadoRepository;
    // private AsignacionTurnoRepository asignacionTurnoRepository; // Placeholder
    // -------------------------------------

    public ServicioGestionEmpleados(EmpleadoRepositoryJson empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
        List<Empleado> cargados = empleadoRepository.cargarEmpleados();
        for (Empleado e : cargados) {
            empleados.put(e.getIdentificacion(), e);
            // System.out.println("[DEBUG] Empleado cargado: ID=" + e.getIdentificacion() + ", Username=" + e.getUsername() + ", Password=" + e.getPassword());
        }
    }

    /**
     * Registra un nuevo empleado en el sistema.
     *
     * <b>Precondiciones:</b>
     * <ul>
     *   <li>El empleado no debe ser nulo.</li>
     *   <li>No debe existir otro empleado con la misma identificación.</li>
     * </ul>
     *
     * <b>Poscondiciones:</b>
     * <ul>
     *   <li>El empleado queda registrado en el sistema.</li>
     * </ul>
     *
     * @param empleado El empleado a registrar.
     * @return El empleado registrado.
     * @throws DatosInvalidosException si el empleado es nulo o ya existe un empleado con la misma identificación.
     * @example
     * <pre>
     *     Empleado cajero = new Cajero(...);
     *     servicio.registrarEmpleado(cajero);
     * </pre>
     */
    public Empleado registrarEmpleado(Empleado empleado) {
        // Objects.requireNonNull(empleado, "El empleado no puede ser nulo."); // Constructor de Empleado ya valida
        if (empleado == null) {
             throw new DatosInvalidosException("El empleado a registrar no puede ser nulo.");
        }
        if (empleados.containsKey(empleado.getIdentificacion())) {
            // Usar excepción más específica si se desea
            throw new DatosInvalidosException("Ya existe un empleado con la identificación: " + empleado.getIdentificacion());
        }
        empleados.put(empleado.getIdentificacion(), empleado);
        empleadoRepository.guardarEmpleados(new ArrayList<>(empleados.values()));
        System.out.println("Empleado registrado: " + empleado.getNombre() + " (" + empleado.getClass().getSimpleName() + ")");
        return empleado;
    }

    /**
     * Busca un empleado por su identificación.
     *
     * <b>Precondiciones:</b> La identificación no debe ser nula ni vacía.
     * <b>Poscondiciones:</b> Devuelve un Optional con el empleado encontrado o vacío si no existe.
     *
     * @param identificacion Identificación del empleado.
     * @return Optional con el empleado encontrado.
     * @example
     * <pre>
     *     Optional<Empleado> emp = servicio.consultarEmpleadoPorIdentificacion("123");
     * </pre>
     */
    public Optional<Empleado> consultarEmpleadoPorIdentificacion(String identificacion) {
         if (identificacion == null || identificacion.trim().isEmpty()) {
             // Opcional: lanzar excepción o devolver vacío
             // throw new DatosInvalidosException("La identificación para búsqueda no puede ser nula o vacía.");
             return Optional.empty();
         }
        // En una app real: return empleadoRepository.findById(identificacion);
        return Optional.ofNullable(empleados.get(identificacion));
    }

    /**
     * Obtiene todos los empleados registrados en el sistema.
     *
     * <b>Precondiciones:</b> Ninguna.
     * <b>Poscondiciones:</b> Devuelve una lista con todos los empleados registrados.
     *
     * @return Lista de todos los empleados.
     * @example
     * <pre>
     *     List<Empleado> lista = servicio.consultarTodosLosEmpleados();
     * </pre>
     */
    public List<Empleado> consultarTodosLosEmpleados() {
        // En una app real: return empleadoRepository.findAll();
        return new ArrayList<>(empleados.values());
    }

    /**
     * Asigna un empleado a un lugar de trabajo para una fecha y turno específicos.
     *
     * <b>Precondiciones:</b>
     * <ul>
     *   <li>La identificación del empleado, el lugar de trabajo (o null para Servicio General), la fecha y el turno no deben ser nulos.</li>
     *   <li>El empleado debe existir y cumplir los requisitos del lugar y turno.</li>
     * </ul>
     *
     * <b>Poscondiciones:</b>
     * <ul>
     *   <li>El empleado queda asignado al lugar y turno indicados.</li>
     * </ul>
     *
     * @param identificacionEmpleado ID del empleado.
     * @param lugarTrabajo Lugar de trabajo (puede ser null para Servicio General).
     * @param fecha Fecha de la asignación.
     * @param turno Turno de la asignación.
     * @return La asignación creada.
     * @throws EmpleadoNoEncontradoException si el empleado no existe.
     * @throws AsignacionInvalidaException si la asignación no es válida (tipo incorrecto, sin lugar para no-general, etc.).
     * @throws CapacitacionInsuficienteException si el empleado no tiene las capacitaciones requeridas.
     * @throws DatosInvalidosException si fecha o turno son null.
     * @example
     * <pre>
     *     AsignacionTurno asignacion = servicio.asignarTurno("123", tienda, LocalDate.now(), Turno.APERTURA);
     * </pre>
     */
    public AsignacionTurno asignarTurno(String identificacionEmpleado, LugarTrabajo lugarTrabajo, LocalDate fecha, Turno turno) {
        if (fecha == null || turno == null) {
            throw new DatosInvalidosException("Fecha y turno no pueden ser nulos para la asignación.");
        }

        Empleado empleado = consultarEmpleadoPorIdentificacion(identificacionEmpleado)
                .orElseThrow(() -> new EmpleadoNoEncontradoException(identificacionEmpleado));

        AsignacionTurno nuevaAsignacion;
        String idAsignacion = AsignacionTurno.generarIdUnico();

        if (lugarTrabajo != null) {
            // Asignación a un lugar específico
            try {
                lugarTrabajo.asignarEmpleado(empleado); // Intenta asignar (valida y puede lanzar excepción)
                nuevaAsignacion = new AsignacionTurno(idAsignacion, empleado, lugarTrabajo, fecha, turno);
            } catch (CapacitacionInsuficienteException e) {
                // Re-lanzar la excepción específica capturada
                throw new AsignacionInvalidaException("El empleado no tiene las capacitaciones requeridas para el lugar de trabajo: " + e.getMessage());
            } catch (AsignacionInvalidaException e) {
                throw e;
            } catch (DatosInvalidosException e) {
                throw new AsignacionInvalidaException("Error al intentar asignar empleado: " + e.getMessage());
            }

        } else {
            // Asignación como Servicio General
            if (!(empleado instanceof ServicioGeneral)) { // Updated check
                throw new AsignacionInvalidaException("Solo los empleados de Servicio General pueden ser asignados sin un lugar de trabajo específico.");
            }
            // El constructor de AsignacionTurno valida los parámetros básicos
            nuevaAsignacion = AsignacionTurno.paraServicioGeneral(idAsignacion, empleado, fecha, turno);
             System.out.println("Asignación creada: " + empleado.getNombre() + " a Servicio General el " + fecha + " (" + turno + ")");
        }

        // Verificar si ya existe una asignación idéntica (mismo empleado, fecha, turno)
        // Esta lógica podría moverse a un repositorio o a una validación más robusta
        synchronized (asignaciones) {
            boolean yaAsignadoMismoTurno = asignaciones.stream()
                .anyMatch(a -> a.getEmpleado().equals(empleado) &&
                               a.getFecha().equals(fecha) &&
                               a.getTurno() == turno);

            if (yaAsignadoMismoTurno) {
                 // Decidir si es un error o una advertencia. Por ahora, error si es exactamente la misma asignación.
                 if (asignaciones.contains(nuevaAsignacion)) {
                     throw new AsignacionInvalidaException("El empleado ya tiene esta asignación exacta.");
                 } else {
                      System.out.println("Advertencia: El empleado " + empleado.getNombre() + " ya tiene otra asignación para " + fecha + " en el turno " + turno);
                      // Permitir asignaciones múltiples en el mismo turno a diferentes lugares si la regla de negocio lo permite.
                 }
            }
            asignaciones.add(nuevaAsignacion);
        }
        // En una app real: asignacionTurnoRepository.save(nuevaAsignacion);

        return nuevaAsignacion;
    }

    /**
     * Consulta las asignaciones de un empleado para un día específico.
     *
     * <b>Precondiciones:</b> La identificación del empleado y la fecha no deben ser nulas.
     * <b>Poscondiciones:</b> Devuelve una lista de asignaciones para ese empleado y día.
     *
     * @param identificacionEmpleado ID del empleado.
     * @param fecha Fecha a consultar.
     * @return Lista de asignaciones para ese empleado y día.
     * @example
     * <pre>
     *     List<AsignacionTurno> asignaciones = servicio.consultarAsignacionesEmpleadoDia("123", LocalDate.now());
     * </pre>
     */
    public List<AsignacionTurno> consultarAsignacionesEmpleadoDia(String identificacionEmpleado, LocalDate fecha) {
        // En una app real: return asignacionTurnoRepository.findByEmpleadoAndFecha(identificacionEmpleado, fecha);
        synchronized (asignaciones) {
            return asignaciones.stream()
                    .filter(a -> a.getEmpleado().getIdentificacion().equals(identificacionEmpleado) && a.getFecha().equals(fecha))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Consulta todas las asignaciones para una fecha específica.
     *
     * <b>Precondiciones:</b> La fecha no debe ser nula.
     * <b>Poscondiciones:</b> Devuelve una lista de todas las asignaciones para esa fecha.
     *
     * @param fecha Fecha a consultar.
     * @return Lista de todas las asignaciones para esa fecha.
     * @example
     * <pre>
     *     List<AsignacionTurno> asignaciones = servicio.consultarAsignacionesPorFecha(LocalDate.now());
     * </pre>
     */
    public List<AsignacionTurno> consultarAsignacionesPorFecha(LocalDate fecha) {
        // En una app real: return asignacionTurnoRepository.findByFecha(fecha);
        synchronized (asignaciones) {
            return asignaciones.stream()
                    .filter(a -> a.getFecha().equals(fecha))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Verifica si un lugar de trabajo cumple con los requisitos de personal para un turno específico.
     *
     * <b>Precondiciones:</b>
     * <ul>
     *   <li>El lugar de trabajo, la fecha y el turno no deben ser nulos.</li>
     * </ul>
     *
     * <b>Poscondiciones:</b>
     * <ul>
     *   <li>Devuelve true si el lugar cumple los requisitos de personal para ese turno, false en caso contrario.</li>
     * </ul>
     *
     * @param lugarTrabajo El lugar de trabajo a verificar.
     * @param fecha La fecha del turno.
     * @param turno El turno a verificar.
     * @return true si cumple los requisitos, false en caso contrario.
     * @throws DatosInvalidosException si lugarTrabajo, fecha o turno son null.
     * @example
     * <pre>
     *     boolean cumple = servicio.verificarRequisitosPersonalLugar(tienda, LocalDate.now(), Turno.APERTURA);
     * </pre>
     */
    public boolean verificarRequisitosPersonalLugar(LugarTrabajo lugarTrabajo, LocalDate fecha, Turno turno) {
         if (lugarTrabajo == null || fecha == null || turno == null) {
             throw new DatosInvalidosException("Lugar de trabajo, fecha y turno no pueden ser nulos para verificar requisitos.");
         }

        // Obtener los empleados asignados a este lugar, fecha y turno
        List<Empleado> empleadosAsignadosEsteTurno;
        synchronized (asignaciones) {
             empleadosAsignadosEsteTurno = asignaciones.stream()
                .filter(a -> a.getFecha().equals(fecha) &&
                               a.getTurno() == turno &&
                               lugarTrabajo.equals(a.getLugarTrabajo())) // Use equals for LugarTrabajo comparison
                .map(AsignacionTurno::getEmpleado)
                .collect(Collectors.toList());
        }

        // --- Verificación de Requisitos para el Turno Específico ---

        // 1. Verificar número mínimo de empleados
        int empleadosNecesarios = 0;
        if (lugarTrabajo instanceof Atraccion) empleadosNecesarios = ((Atraccion) lugarTrabajo).getEmpleadosMinimos();
        else if (lugarTrabajo instanceof Cafeteria) empleadosNecesarios = ((Cafeteria) lugarTrabajo).getEmpleadosRequeridos();
        else if (lugarTrabajo instanceof Taquilla) empleadosNecesarios = ((Taquilla) lugarTrabajo).getEmpleadosRequeridos(); // Check type Taquilla
        // Tienda no tiene mínimo explícito, pero requiere Cajero.

        if (empleadosNecesarios > 0 && empleadosAsignadosEsteTurno.size() < empleadosNecesarios) {
            System.out.println("Incumplimiento Requisitos: Faltan empleados en " + lugarTrabajo.getNombreLugar() + " para " + fecha + " " + turno + ". Necesarios: " + empleadosNecesarios + ", Asignados: " + empleadosAsignadosEsteTurno.size());
            return false;
        }

        // 2. Verificar si hay al menos un empleado asignable para ese lugar (tipo y capacitaciones generales)
        boolean hayAlMenosUnAsignable = empleadosAsignadosEsteTurno.stream()
                                        .anyMatch(lugarTrabajo::puedeAsignarEmpleado);

        // 3. Verificaciones específicas de roles requeridos para el turno
        boolean requisitosEspecificosCumplidos = true;
        if (lugarTrabajo instanceof Tienda || lugarTrabajo instanceof Taquilla) {
            // Verificación estricta: debe haber un Cajero con MANEJO_CAJA
            boolean tieneCajeroCapacitado = empleadosAsignadosEsteTurno.stream()
                .anyMatch(e -> e instanceof Cajero && ((Cajero) e).tieneCapacitacion(Capacitacion.MANEJO_CAJA));
            if (!tieneCajeroCapacitado) {
                requisitosEspecificosCumplidos = false;
            }
        }

        if (!hayAlMenosUnAsignable || !requisitosEspecificosCumplidos) {
            System.out.println("Incumplimiento Requisitos: Algunos empleados asignados a " + lugarTrabajo.getNombreLugar() + " para " + fecha + " " + turno + " no cumplen los requisitos básicos del puesto (tipo/capacitación general) o falta cajero capacitado.");
            return false;
        }

        System.out.println("Requisitos Cumplidos: El lugar " + lugarTrabajo.getNombreLugar() + " cumple requisitos para " + fecha + " " + turno);
        return true;
    }

    /**
     * Autoriza una nueva capacitación para un empleado. (Operación de Administrador)
     *
     * <b>Precondiciones:</b>
     * <ul>
     *   <li>La identificación del empleado y la capacitación no deben ser nulas.</li>
     *   <li>El empleado debe existir.</li>
     * </ul>
     *
     * <b>Poscondiciones:</b>
     * <ul>
     *   <li>La capacitación queda registrada en el perfil del empleado.</li>
     * </ul>
     *
     * @param identificacionEmpleado ID del empleado.
     * @param capacitacion La capacitación a agregar.
     * @throws EmpleadoNoEncontradoException si el empleado no existe.
     * @throws DatosInvalidosException si la capacitación es null.
     * @example
     * <pre>
     *     servicio.autorizarCapacitacion("123", Capacitacion.OPERACION_ATRACCION_RIESGO_ALTO);
     * </pre>
     */
     public void autorizarCapacitacion(String identificacionEmpleado, Capacitacion capacitacion) {
         if (capacitacion == null) {
             throw new DatosInvalidosException("La capacitación a autorizar no puede ser null.");
         }
         Empleado empleado = consultarEmpleadoPorIdentificacion(identificacionEmpleado)
                 .orElseThrow(() -> new EmpleadoNoEncontradoException(identificacionEmpleado));

         empleado.agregarCapacitacion(capacitacion);
         // En app real: empleadoRepository.save(empleado); // Guardar el cambio
         System.out.println("Capacitación " + capacitacion.name() + " autorizada para " + empleado.getNombre());
     }

    /**
     * Actualiza la información de un empleado existente.
     *
     * <b>Precondiciones:</b> El empleado no debe ser nulo y debe existir en el sistema.
     * <b>Poscondiciones:</b> La información del empleado queda actualizada.
     *
     * @param empleado El empleado con la información actualizada.
     * @throws EmpleadoNoEncontradoException si el empleado no existe.
     * @throws DatosInvalidosException si el empleado es null.
     * @example
     * <pre>
     *     servicio.actualizarEmpleado(empleado);
     * </pre>
     */
    public void actualizarEmpleado(Empleado empleado) {
        if (empleado == null) {
            throw new DatosInvalidosException("El empleado a actualizar no puede ser nulo.");
        }
        if (!empleados.containsKey(empleado.getIdentificacion())) {
            throw new EmpleadoNoEncontradoException(empleado.getIdentificacion());
        }
        empleados.put(empleado.getIdentificacion(), empleado);
        empleadoRepository.guardarEmpleados(new ArrayList<>(empleados.values()));
    }

    /**
     * Elimina un empleado por su identificación.
     *
     * <b>Precondiciones:</b> La identificación no debe ser nula ni vacía.
     * <b>Poscondiciones:</b> El empleado queda eliminado del sistema.
     *
     * @param identificacion Identificación del empleado a eliminar.
     * @throws EmpleadoNoEncontradoException si el empleado no existe.
     * @example
     * <pre>
     *     servicio.eliminarEmpleado("123");
     * </pre>
     */
    public void eliminarEmpleado(String identificacion) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            throw new DatosInvalidosException("La identificación no puede ser nula ni vacía.");
        }
        if (!empleados.containsKey(identificacion)) {
            throw new EmpleadoNoEncontradoException(identificacion);
        }
        empleados.remove(identificacion);
        empleadoRepository.guardarEmpleados(new ArrayList<>(empleados.values()));
    }

    /**
     * Consulta todos los empleados de un tipo específico (Cajero, Cocinero, etc).
     *
     * <b>Precondiciones:</b> El tipo de clase no debe ser nulo.
     * <b>Poscondiciones:</b> Devuelve una lista de empleados de ese tipo.
     *
     * @param tipo Clase del tipo de empleado (por ejemplo, Cajero.class).
     * @return Lista de empleados de ese tipo.
     * @throws DatosInvalidosException si el tipo es nulo.
     * @example
     * <pre>
     *     List<Cajero> cajeros = servicio.consultarEmpleadosPorTipo(Cajero.class);
     * </pre>
     */
    public <T extends Empleado> List<T> consultarEmpleadosPorTipo(Class<T> tipo) {
        if (tipo == null) {
            throw new DatosInvalidosException("El tipo de empleado no puede ser nulo.");
        }
        return empleados.values().stream()
                .filter(tipo::isInstance)
                .map(tipo::cast)
                .toList();
    }

    /**
     * Elimina todos los empleados de un tipo específico.
     *
     * <b>Precondiciones:</b> El tipo de clase no debe ser nulo.
     * <b>Poscondiciones:</b> Todos los empleados de ese tipo quedan eliminados.
     *
     * @param tipo Clase del tipo de empleado a eliminar.
     * @throws DatosInvalidosException si el tipo es nulo.
     * @example
     * <pre>
     *     servicio.eliminarEmpleadosPorTipo(Cocinero.class);
     * </pre>
     */
    public <T extends Empleado> void eliminarEmpleadosPorTipo(Class<T> tipo) {
        if (tipo == null) {
            throw new DatosInvalidosException("El tipo de empleado no puede ser nulo.");
        }
        List<String> idsAEliminar = empleados.values().stream()
                .filter(tipo::isInstance)
                .map(Empleado::getIdentificacion)
                .toList();
        for (String id : idsAEliminar) {
            empleados.remove(id);
        }
        empleadoRepository.guardarEmpleados(new ArrayList<>(empleados.values()));
    }
}

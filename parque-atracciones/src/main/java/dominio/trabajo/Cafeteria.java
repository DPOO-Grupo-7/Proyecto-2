package dominio.trabajo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import dominio.empleado.Cajero;
import dominio.empleado.Capacitacion;
import dominio.empleado.Cocinero;
import dominio.empleado.Empleado;
import dominio.excepciones.DatosInvalidosException; // Importar
import dominio.excepciones.AsignacionInvalidaException; // Importar
import dominio.excepciones.CapacitacionInsuficienteException; // Importar

/**
 * Clase que representa una cafetería en el parque.
 * <p>
 * Permite la gestión de personal asignado, validando roles y capacitaciones requeridas para Cajeros y Cocineros.
 * </p>
 *
 * <b>Contexto:</b> Modelo de dominio para puntos de venta de alimentos y bebidas en el parque.
 *
 * <b>Precondiciones generales:</b>
 * <ul>
 *   <li>El nombre no debe ser nulo ni vacío.</li>
 *   <li>El número de empleados requeridos debe ser al menos 1.</li>
 * </ul>
 *
 * <b>Poscondiciones generales:</b>
 * <ul>
 *   <li>La cafetería queda inicializada y lista para la asignación de empleados.</li>
 * </ul>
 *
 * @author Sistema Parque
 * @example
 * <pre>
 *     Cafeteria cafeteria = new Cafeteria("Cafetería Central", 3);
 *     cafeteria.asignarEmpleado(cocinero);
 *     cafeteria.asignarEmpleado(cajero);
 * </pre>
 */
public class Cafeteria implements LugarTrabajo {
    private String nombre;
    private List<Empleado> empleadosAsignados;
    private int empleadosRequeridos;
    private static final Set<Capacitacion> CAPACITACIONES_REQUERIDAS_COCINERO =
        Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            Capacitacion.MANIPULACION_ALIMENTOS_AVANZADO,
            Capacitacion.ATENCION_CLIENTE_GENERAL // Cocineros pueden interactuar
        )));
     private static final Set<Capacitacion> CAPACITACIONES_REQUERIDAS_CAJERO =
        Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            Capacitacion.MANEJO_CAJA,
            Capacitacion.ATENCION_CLIENTE_GENERAL,
            Capacitacion.MANIPULACION_ALIMENTOS_BASICO // Cajeros pueden manejar alimentos pre-empacados
        )));

    /**
     * Constructor de la cafetería.
     *
     * <b>Precondiciones:</b> El nombre no debe ser nulo ni vacío. El número de empleados requeridos debe ser al menos 1.
     * <b>Poscondiciones:</b> Cafetería inicializada y lista para asignación de empleados.
     *
     * @param nombre Nombre de la cafetería.
     * @param empleadosRequeridos Número mínimo de empleados requeridos.
     * @throws DatosInvalidosException si nombre es inválido o empleadosRequeridos < 1
     * @example
     * <pre>
     *     Cafeteria c = new Cafeteria("Cafetería Norte", 2);
     * </pre>
     */
    public Cafeteria(String nombre, int empleadosRequeridos) {
         if (nombre == null || nombre.trim().isEmpty()) {
             throw new DatosInvalidosException("El nombre de la cafetería no puede ser nulo o vacío.");
         }
        if (empleadosRequeridos < 1) {
            throw new DatosInvalidosException("El número de empleados requeridos debe ser al menos 1");
        }
        this.nombre = nombre;
        this.empleadosRequeridos = empleadosRequeridos;
        this.empleadosAsignados = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     * 
     * @return El nombre de la cafetería.
     */
    @Override
    public String getNombreLugar() {
        return nombre;
    }
    
    /**
     * Asigna un empleado a la cafetería si cumple los requisitos.
     *
     * <b>Precondiciones:</b> El empleado no debe ser nulo y debe ser Cocinero o Cajero con las capacitaciones requeridas.
     * <b>Poscondiciones:</b> El empleado queda asignado si cumple los requisitos.
     *
     * @param empleado Empleado a asignar.
     * @throws AsignacionInvalidaException si el empleado no es Cocinero o Cajero.
     * @throws CapacitacionInsuficienteException si el empleado no tiene las capacitaciones requeridas.
     * @throws DatosInvalidosException si el empleado es null.
     * @example
     * <pre>
     *     cafeteria.asignarEmpleado(cocinero);
     * </pre>
     */
    @Override
    public void asignarEmpleado(Empleado empleado) {
        Objects.requireNonNull(empleado, "El empleado no puede ser nulo."); // O usar DatosInvalidosException
        if (!puedeAsignarEmpleado(empleado)) {
            // Determinar la causa específica de la invalidez
            boolean esTipoValido = empleado instanceof Cocinero || empleado instanceof Cajero;
            if (!esTipoValido) {
                 throw new AsignacionInvalidaException("Solo Cocineros o Cajeros pueden ser asignados a una Cafetería.");
            }
            // Si el tipo es válido, el problema debe ser la capacitación
            throw new CapacitacionInsuficienteException(empleado, this);
        }
        if (!empleadosAsignados.contains(empleado)) {
            empleadosAsignados.add(empleado);
        }
    }
    
    /**
     * Verifica si la cafetería cumple con los requisitos de personal (tiene el número mínimo requerido de empleados asignados
     * Y cuenta con al menos un Cocinero y un Cajero capacitado).
     *
     * <b>Precondiciones:</b> Ninguna.
     * <b>Poscondiciones:</b> Devuelve true si cumple los requisitos, false en caso contrario.
     *
     * @return true si cumple los requisitos, false en caso contrario.
     * @example
     * <pre>
     *     boolean cumple = cafeteria.cumpleRequisitosPersonal();
     * </pre>
     */
    @Override
    public boolean cumpleRequisitosPersonal() {
        if (empleadosAsignados.size() < empleadosRequeridos) {
            return false;
        }
        boolean tieneCocinero = empleadosAsignados.stream()
                                            .anyMatch(e -> e instanceof Cocinero); // Check for at least one Cocinero
        boolean tieneCajero = empleadosAsignados.stream()
                                            .anyMatch(e -> e instanceof Cajero && e.tieneCapacitacion(Capacitacion.MANEJO_CAJA)); // Check for at least one qualified Cajero

        return tieneCocinero && tieneCajero;
    }

    /**
     * {@inheritDoc}
     * Devuelve las capacitaciones requeridas MÍNIMAS (las de Cajero).
     * La verificación específica se hace en puedeAsignarEmpleado.
     */
    @Override
    public Set<Capacitacion> getCapacitacionesRequeridas() {
        // Devolver un conjunto base o el más restrictivo?
        // Para la verificación general en ServicioGestionEmpleados, es complejo.
        // Devolvemos las de Cajero como base mínima.
        return CAPACITACIONES_REQUERIDAS_CAJERO;
    }
    
    /**
     * Obtiene la lista de empleados asignados a esta cafetería.
     *
     * @return Lista inmutable de empleados asignados.
     */
    public List<Empleado> getEmpleadosAsignados() {
        return new ArrayList<>(empleadosAsignados); // Devuelve copia para proteger encapsulamiento
    }

    /**
     * Obtiene el número de empleados requeridos para esta cafetería.
     *
     * @return Número de empleados requeridos.
     */
    public int getEmpleadosRequeridos() {
        return empleadosRequeridos;
    }

    /**
     * Verifica si un empleado puede ser asignado a la cafetería.
     *
     * <b>Precondiciones:</b> El empleado no debe ser nulo.
     * <b>Poscondiciones:</b> Devuelve true si puede ser asignado, false si no.
     *
     * @param empleado Empleado a verificar.
     * @return true si puede ser asignado, false si no.
     * @example
     * <pre>
     *     boolean puede = cafeteria.puedeAsignarEmpleado(cocinero);
     * </pre>
     */
    @Override
    public boolean puedeAsignarEmpleado(Empleado empleado) {
        Objects.requireNonNull(empleado, "El empleado no puede ser nulo.");
        if (empleado instanceof Cocinero) {
            return empleado.cumpleCapacitaciones(CAPACITACIONES_REQUERIDAS_COCINERO);
        } else if (empleado instanceof Cajero) {
             // Un cocinero también puede actuar como cajero si tiene las capacitaciones de cajero
             // (aunque ya las tiene por defecto en el constructor de Cocinero)
            return empleado.cumpleCapacitaciones(CAPACITACIONES_REQUERIDAS_CAJERO);
        }
        return false; // No es ni Cocinero ni Cajero
    }

    /**
     * Remueve un empleado de la lista de empleados asignados.
     *
     * <b>Precondiciones:</b> El empleado no debe ser nulo.
     * <b>Poscondiciones:</b> El empleado queda removido si estaba asignado.
     *
     * @param empleado El empleado a remover.
     * @return true si el empleado estaba asignado y fue removido, false en caso contrario.
     * @example
     * <pre>
     *     cafeteria.removerEmpleado(cocinero);
     * </pre>
     */
    public boolean removerEmpleado(Empleado empleado) {
        return empleadosAsignados.remove(empleado);
    }

    /**
     * Obtiene la lista de empleados asignados que son Cocineros.
     *
     * @return Una lista no modificable de Cocineros asignados.
     */
    public List<Cocinero> getCocinerosAsignados() {
        List<Cocinero> cocineros = new ArrayList<>();
        for (Empleado emp : empleadosAsignados) {
            if (emp instanceof Cocinero) {
                cocineros.add((Cocinero) emp);
            }
        }
        return Collections.unmodifiableList(cocineros);
    }

    /**
     * Obtiene la lista de empleados asignados que son Cajeros.
     *
     * @return Una lista no modificable de Cajeros asignados.
     */
    public List<Cajero> getCajerosAsignados() {
        List<Cajero> cajeros = new ArrayList<>();
        for (Empleado emp : empleadosAsignados) {
            if (emp instanceof Cajero) {
                cajeros.add((Cajero) emp);
            }
        }
        return Collections.unmodifiableList(cajeros);
    }
}

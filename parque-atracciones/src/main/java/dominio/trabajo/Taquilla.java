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
import dominio.empleado.Empleado;
import dominio.excepciones.DatosInvalidosException; // Importar
import dominio.excepciones.AsignacionInvalidaException; // Importar
import dominio.excepciones.CapacitacionInsuficienteException; // Importar

/**
 * Representa una taquilla dentro del parque, que es un tipo de LugarTrabajo.
 * <p>
 * Requiere empleados con capacitaciones específicas (MANEJO_CAJA, ATENCION_CLIENTE_GENERAL) y permite la gestión de asignación y remoción de empleados.
 * </p>
 *
 * <b>Contexto:</b> Modelo de dominio para puntos de venta de tiquetes y atención al cliente en el parque.
 *
 * <b>Precondiciones generales:</b>
 * <ul>
 *   <li>El nombre no debe ser nulo ni vacío.</li>
 *   <li>El número de empleados requeridos debe ser al menos 1.</li>
 * </ul>
 *
 * <b>Poscondiciones generales:</b>
 * <ul>
 *   <li>La taquilla queda inicializada y lista para la asignación de empleados.</li>
 * </ul>
 *
 * @author Sistema Parque
 * @example
 * <pre>
 *     Taquilla taquilla = new Taquilla("Taquilla Norte", 2);
 *     taquilla.asignarEmpleado(cajero);
 * </pre>
 */
public class Taquilla implements LugarTrabajo { // Implement LugarTrabajo
    private String nombre;
    private List<Empleado> empleadosAsignados;
    private int empleadosRequeridos;
    private static final Set<Capacitacion> CAPACITACIONES_REQUERIDAS = 
        Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            Capacitacion.MANEJO_CAJA,
            Capacitacion.ATENCION_CLIENTE_GENERAL
        )));

    /**
     * Constructor de la Taquilla.
     *
     * <b>Precondiciones:</b> El nombre no debe ser nulo ni vacío. El número de empleados requeridos debe ser al menos 1.
     * <b>Poscondiciones:</b> Taquilla inicializada y lista para asignación de empleados.
     *
     * @param nombre Nombre de la taquilla.
     * @param empleadosRequeridos Número mínimo de empleados requeridos.
     * @throws DatosInvalidosException si nombre es inválido o empleadosRequeridos < 1
     * @example
     * <pre>
     *     Taquilla t = new Taquilla("Taquilla Sur", 3);
     * </pre>
     */
    public Taquilla(String nombre, int empleadosRequeridos) {
         if (nombre == null || nombre.trim().isEmpty()) {
             throw new DatosInvalidosException("El nombre de la taquilla no puede ser nulo o vacío.");
         }
         if (empleadosRequeridos < 1) {
             throw new DatosInvalidosException("El número de empleados requeridos debe ser al menos 1.");
         }
        this.nombre = nombre;
        this.empleadosRequeridos = empleadosRequeridos;
        this.empleadosAsignados = new ArrayList<>();
    }

    @Override
    public String getNombreLugar() {
        return nombre;
    }

    /**
     * Asigna un empleado a la taquilla si cumple los requisitos.
     *
     * <b>Precondiciones:</b> El empleado no debe ser nulo y debe ser Cajero con las capacitaciones requeridas.
     * <b>Poscondiciones:</b> El empleado queda asignado si cumple los requisitos.
     *
     * @param empleado Empleado a asignar.
     * @throws AsignacionInvalidaException si el empleado no es Cajero.
     * @throws CapacitacionInsuficienteException si el empleado no tiene las capacitaciones requeridas.
     * @throws DatosInvalidosException si el empleado es null.
     * @example
     * <pre>
     *     taquilla.asignarEmpleado(cajero);
     * </pre>
     */
    @Override
    public void asignarEmpleado(Empleado empleado) {
        Objects.requireNonNull(empleado, "El empleado no puede ser nulo."); // O DatosInvalidosException
        if (!puedeAsignarEmpleado(empleado)) {
             // Determinar causa
             if (!(empleado instanceof Cajero)) {
                 throw new AsignacionInvalidaException("Solo Cajeros pueden ser asignados a una Taquilla.");
             }
             // Si es Cajero, falta capacitación
            throw new CapacitacionInsuficienteException(empleado, this);
        }
        if (!empleadosAsignados.contains(empleado)) { // Evitar duplicados
             empleadosAsignados.add(empleado);
        }
    }

    /**
     * Verifica si la taquilla cumple con los requisitos de personal (cantidad mínima y al menos un Cajero capacitado).
     *
     * <b>Precondiciones:</b> Ninguna.
     * <b>Poscondiciones:</b> Devuelve true si cumple los requisitos, false en caso contrario.
     *
     * @return true si cumple los requisitos, false en caso contrario.
     * @example
     * <pre>
     *     boolean cumple = taquilla.cumpleRequisitosPersonal();
     * </pre>
     */
    @Override
    public boolean cumpleRequisitosPersonal() {
        if (empleadosAsignados.size() < empleadosRequeridos) {
            return false;
        }
        // Adicionalmente, debe haber al menos un Cajero capacitado
        boolean tieneCajero = empleadosAsignados.stream()
                                            .anyMatch(e -> e instanceof Cajero && e.tieneCapacitacion(Capacitacion.MANEJO_CAJA));
        return tieneCajero;
    }

    /**
     * Verifica si un empleado puede ser asignado a la taquilla.
     *
     * <b>Precondiciones:</b> El empleado no debe ser nulo.
     * <b>Poscondiciones:</b> Devuelve true si puede ser asignado, false si no.
     *
     * @param empleado Empleado a verificar.
     * @return true si puede ser asignado, false si no.
     * @example
     * <pre>
     *     boolean puede = taquilla.puedeAsignarEmpleado(cajero);
     * </pre>
     */
    @Override
    public boolean puedeAsignarEmpleado(Empleado empleado) {
        Objects.requireNonNull(empleado, "El empleado no puede ser nulo.");
        // Verifica que el empleado sea Cajero y tenga las capacitaciones
        boolean esTipoValido = empleado instanceof Cajero;
        boolean tieneCapacitaciones = empleado.cumpleCapacitaciones(CAPACITACIONES_REQUERIDAS);
        return esTipoValido && tieneCapacitaciones;
    }

    /**
     * Obtiene el conjunto de capacitaciones requeridas para trabajar en la taquilla.
     *
     * @return Conjunto de capacitaciones requeridas.
     */
    @Override
    public Set<Capacitacion> getCapacitacionesRequeridas() {
        return CAPACITACIONES_REQUERIDAS;
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
     *     taquilla.removerEmpleado(cajero);
     * </pre>
     */
    public boolean removerEmpleado(Empleado empleado) {
        return empleadosAsignados.remove(empleado);
    }

    /**
     * Obtiene la lista de empleados asignados actualmente.
     *
     * @return Una lista no modificable de empleados asignados.
     */
    public List<Empleado> getEmpleadosAsignados() {
        return Collections.unmodifiableList(empleadosAsignados);
    }

    /**
     * Obtiene el nombre de la taquilla.
     * @return Nombre de la taquilla.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el número mínimo de empleados requeridos.
     * @return Número de empleados requeridos.
     */
    public int getEmpleadosRequeridos() {
        return empleadosRequeridos;
    }

    @Override
    public String toString() {
        return "Taquilla [nombre=" + nombre + ", empleadosRequeridos=" + empleadosRequeridos + ", empleadosAsignados="
                + empleadosAsignados.size() + "]";
    }
}

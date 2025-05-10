package dominio.empleado;

import java.util.HashSet;
import java.util.Set;
import dominio.usuario.Usuario;

/**
 * Representa un empleado del parque de diversiones.
 * <p>
 * Clase base para todos los tipos de empleados (cajero, cocinero, operario, servicio general, administrador).
 * Gestiona información personal, capacitaciones y métodos comunes.
 * </p>
 *
 * <b>Contexto:</b> Modelo de dominio para la gestión de recursos humanos del parque.
 *
 * <b>Precondiciones generales:</b>
 * <ul>
 *   <li>Identificación, nombre, email, teléfono, username y password válidos.</li>
 * </ul>
 *
 * <b>Poscondiciones generales:</b>
 * <ul>
 *   <li>Empleado inicializado y listo para asignación de turnos y capacitaciones.</li>
 * </ul>
 *
 * @author 
 * @example
 * <pre>
 *     Empleado emp = new Cajero("2001", "Carlos", "carlos@parque.com", "555-1111", "cajero1", "pass", 1, "Taquilla Norte");
 * </pre>
 */
public abstract class Empleado extends Usuario {
    private Set<Capacitacion> capacitaciones;

    /**
     * Constructor base para empleados del parque.
     *
     * <b>Precondiciones:</b> Todos los parámetros deben ser válidos y no nulos.
     * <b>Poscondiciones:</b> Empleado inicializado con los datos proporcionados.
     *
     * @param identificacion Identificación única.
     * @param nombre Nombre completo.
     * @param email Correo electrónico.
     * @param telefono Número de teléfono.
     * @param username Nombre de usuario.
     * @param password Contraseña.
     * @example
     * <pre>
     *     Empleado emp = new Cajero("2001", "Carlos", "carlos@parque.com", "555-1111", "cajero1", "pass", 1, "Taquilla Norte");
     * </pre>
     */
    public Empleado(String identificacion, String nombre, String email, String telefono, 
                    String username, String password) {
        // Call the Usuario constructor with all required parameters
        super(identificacion, nombre, email, telefono, username, password);
        // Initialize capacitaciones
        this.capacitaciones = new HashSet<>();
    }

    /**
     * Obtiene el conjunto de capacitaciones del empleado.
     * @return Set de capacitaciones.
     */
    public Set<Capacitacion> getCapacitaciones() {
        return new HashSet<>(capacitaciones);
    }

    /**
     * Agrega una capacitación al empleado.
     *
     * <b>Precondiciones:</b> La capacitación no debe ser nula.
     * <b>Poscondiciones:</b> La capacitación queda registrada en el empleado.
     *
     * @param capacitacion Capacitación a agregar.
     * @example
     * <pre>
     *     empleado.agregarCapacitacion(Capacitacion.PRIMEROS_AUXILIOS);
     * </pre>
     */
    public void agregarCapacitacion(Capacitacion capacitacion) {
        this.capacitaciones.add(capacitacion);
    }

    /**
     * Verifica si el empleado tiene una capacitación específica.
     *
     * <b>Precondiciones:</b> La capacitación no debe ser nula.
     * <b>Poscondiciones:</b> Devuelve true si el empleado tiene la capacitación.
     *
     * @param capacitacion Capacitación a verificar.
     * @return true si la tiene, false si no.
     * @example
     * <pre>
     *     boolean tiene = empleado.tieneCapacitacion(Capacitacion.MANEJO_CAJA);
     * </pre>
     */
    public boolean tieneCapacitacion(Capacitacion capacitacion) {
        return this.capacitaciones.contains(capacitacion);
    }

    /**
     * Verifica si el empleado cumple todas las capacitaciones requeridas.
     *
     * <b>Precondiciones:</b> El conjunto de capacitaciones requeridas no debe ser nulo.
     * <b>Poscondiciones:</b> Devuelve true si el empleado cumple todas las capacitaciones.
     *
     * @param requeridas Conjunto de capacitaciones requeridas.
     * @return true si cumple todas, false si no.
     * @example
     * <pre>
     *     boolean cumple = empleado.cumpleCapacitaciones(Set.of(Capacitacion.MANEJO_CAJA));
     * </pre>
     */
    public boolean cumpleCapacitaciones(Set<Capacitacion> capacitacionesRequeridas) {
        return this.capacitaciones.containsAll(capacitacionesRequeridas);
    }

}
package dominio.empleado;

// Removed: import java.util.List;

/**
 * Representa un empleado de servicio general del parque.
 * <p>
 * Encargado de labores de aseo y mantenimiento en cualquier lugar del parque.
 * </p>
 *
 * <b>Contexto:</b> Modelo de dominio para empleados que no tienen un lugar de trabajo fijo y pueden ser asignados a tareas generales.
 *
 * <b>Precondiciones generales:</b>
 * <ul>
 *   <li>Identificación, nombre, email, teléfono, username y password válidos.</li>
 * </ul>
 *
 * <b>Poscondiciones generales:</b>
 * <ul>
 *   <li>Empleado de servicio general inicializado y listo para asignación de turnos.</li>
 * </ul>
 *
 * @author Sistema Parque
 * @example
 * <pre>
 *     ServicioGeneral sg = new ServicioGeneral("4001", "Pedro", "pedro@parque.com", "555-3333", "servgen1", "pass");
 * </pre>
 */
public class ServicioGeneral extends Empleado {

    /**
     * Constructor para el empleado de Servicio General.
     *
     * <b>Precondiciones:</b> Todos los parámetros deben ser válidos y no nulos.
     * <b>Poscondiciones:</b> Servicio General inicializado con capacitaciones básicas.
     *
     * @param identificacion Identificación única.
     * @param nombre Nombre completo.
     * @param email Correo electrónico.
     * @param telefono Número de teléfono.
     * @param username Nombre de usuario.
     * @param password Contraseña.
     * @example
     * <pre>
     *     ServicioGeneral sg = new ServicioGeneral("4001", "Pedro", "pedro@parque.com", "555-3333", "servgen1", "pass");
     * </pre>
     */
    public ServicioGeneral(String identificacion, String nombre, String email, String telefono,
                           String username, String password) {
        super(identificacion, nombre, email, telefono, username, password);
        // Capacitaciones base para Servicio General (pueden interactuar con clientes)
        agregarCapacitacion(Capacitacion.ATENCION_CLIENTE_GENERAL);
        // Podrían necesitar otras como manejo básico de herramientas, etc.
    }

    // No methods specific to ServicioGeneral are defined in the requirements,
    // but could be added here if needed (e.g., list of areas covered).
}

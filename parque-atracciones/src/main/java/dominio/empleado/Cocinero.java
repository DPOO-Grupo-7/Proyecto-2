package dominio.empleado;


/**
 * Representa un empleado cocinero del parque.
 * <p>
 * Encargado de la preparación de alimentos en cafeterías, con capacitaciones en manipulación de alimentos,
 * atención al cliente y manejo de caja.
 * </p>
 *
 * <b>Contexto:</b> Modelo de dominio para empleados que trabajan en la cocina de cafeterías del parque.
 *
 * <b>Precondiciones generales:</b>
 * <ul>
 *   <li>Identificación, nombre, email, teléfono, username, password y especialidad válidos.</li>
 * </ul>
 *
 * <b>Poscondiciones generales:</b>
 * <ul>
 *   <li>Cocinero inicializado con capacitaciones requeridas y listo para asignación de turnos externos.</li>
 * </ul>
 *
 * @author Sistema Parque
 * @example
 * <pre>
 *     Cocinero cocinero = new Cocinero("3001", "Ana", "ana@parque.com", "555-2222", "cocinero1", "pass", "Repostería");
 * </pre>
 */
public class Cocinero extends Empleado {
    private String especialidad;
    // private Turno turno; // Removed attribute

    /**
     * Constructor para el empleado Cocinero.
     *
     * <b>Precondiciones:</b> Todos los parámetros deben ser válidos y no nulos.
     * <b>Poscondiciones:</b> Cocinero inicializado con capacitaciones de alimentos, atención al cliente y caja.
     *
     * @param identificacion Identificación única.
     * @param nombre Nombre completo.
     * @param email Correo electrónico.
     * @param telefono Número de teléfono.
     * @param username Nombre de usuario.
     * @param password Contraseña.
     * @param especialidad Especialidad culinaria.
     * @example
     * <pre>
     *     Cocinero cocinero = new Cocinero("3001", "Ana", "ana@parque.com", "555-2222", "cocinero1", "pass", "Repostería");
     * </pre>
     */
    public Cocinero(String identificacion, String nombre, String email, String telefono,
                   String username, String password,
                   String especialidad /* Removed Turno turno parameter */) {
        // Call the updated Empleado constructor
        super(identificacion, nombre, email, telefono, username, password);
        this.especialidad = especialidad;
        // this.turno = turno; // Removed assignment
        // Todo cocinero debe tener capacitación básica y avanzada de alimentos
        agregarCapacitacion(Capacitacion.MANIPULACION_ALIMENTOS_BASICO);
        agregarCapacitacion(Capacitacion.MANIPULACION_ALIMENTOS_AVANZADO);
        // Pueden cubrir caja, así que tienen capacitación de atención al cliente y manejo de caja
        agregarCapacitacion(Capacitacion.ATENCION_CLIENTE_GENERAL);
        agregarCapacitacion(Capacitacion.MANEJO_CAJA);
    }

    /**
     * Obtiene la especialidad culinaria del cocinero.
     *
     * <b>Precondiciones:</b> La especialidad debe haber sido definida al crear el objeto.
     * <b>Poscondiciones:</b> Devuelve la especialidad registrada.
     *
     * @return Especialidad del cocinero.
     * @example
     * <pre>
     *     String esp = cocinero.getEspecialidad();
     * </pre>
     */
    public String getEspecialidad() {
        return especialidad;
    }

    /**
     * Obtiene el turno de trabajo del cocinero.
     * @return Turno (mañana/tarde/noche)
     */
    // public Turno getTurno() { // Removed getter
    //     return turno;
    // }

    /**
     * Define el turno de trabajo del cocinero.
     * @param turno Nuevo turno.
     */
    // public void setTurno(Turno turno) { // Removed setter
    //     this.turno = turno;
    // }
}

package dominio.empleado;

import java.util.List;
import java.util.ArrayList; // Importar ArrayList
import dominio.excepciones.DatosInvalidosException; // Importar
import dominio.excepciones.CapacitacionInsuficienteException; // Importar

/**
 * Clase que representa a un empleado operario de atracción mecánica del parque.
 * <p>
 * Encargado de operar atracciones mecánicas, requiere capacitaciones específicas según el nivel de riesgo.
 * </p>
 *
 * <b>Contexto:</b> Modelo de dominio para empleados que operan atracciones mecánicas.
 *
 * <b>Precondiciones generales:</b>
 * <ul>
 *   <li>Identificación, nombre, email, teléfono, username y password válidos.</li>
 * </ul>
 *
 * <b>Poscondiciones generales:</b>
 * <ul>
 *   <li>Operario inicializado con capacitaciones requeridas y listo para asignación de turnos.</li>
 * </ul>
 *
 * @author Sistema Parque
 * @example
 * <pre>
 *     OperarioAtraccion op = new OperarioAtraccion("5001", "Luis", "luis@parque.com", "555-4444", "operario1", "pass", true, List.of("Montaña Rusa"));
 * </pre>
 */
public class OperarioAtraccion extends Empleado {
    private boolean certificadoSeguridad;
    private List<String> atraccionesHabilitadas;
    private boolean disponible;

    /**
     * Constructor para el empleado OperarioAtraccion.
     *
     * <b>Precondiciones:</b> Todos los parámetros deben ser válidos y no nulos.
     * <b>Poscondiciones:</b> Operario inicializado con capacitaciones requeridas y lista de atracciones habilitadas.
     *
     * @param identificacion Identificación única.
     * @param nombre Nombre completo.
     * @param email Correo electrónico.
     * @param telefono Número de teléfono.
     * @param username Nombre de usuario.
     * @param password Contraseña.
     * @param certificadoSeguridad Indica si tiene certificado de seguridad.
     * @param atraccionesHabilitadas Lista de atracciones que puede operar.
     * @throws DatosInvalidosException si atraccionesHabilitadas es null.
     * @example
     * <pre>
     *     OperarioAtraccion op = new OperarioAtraccion("5001", "Luis", "luis@parque.com", "555-4444", "operario1", "pass", true, List.of("Montaña Rusa"));
     * </pre>
     */
    public OperarioAtraccion(String identificacion, String nombre, String email, String telefono,
                           String username, String password,
                           boolean certificadoSeguridad, List<String> atraccionesHabilitadas) {
        super(identificacion, nombre, email, telefono, username, password);
        if (atraccionesHabilitadas == null) {
             throw new DatosInvalidosException("La lista de atracciones habilitadas no puede ser null.");
        }
        this.certificadoSeguridad = certificadoSeguridad;
        this.atraccionesHabilitadas = new ArrayList<>(atraccionesHabilitadas); // Crear copia
        this.disponible = true;
        // Capacitaciones base para cualquier operario
        agregarCapacitacion(Capacitacion.CERTIFICADO_SEGURIDAD_ATRACCIONES);
        agregarCapacitacion(Capacitacion.PRIMEROS_AUXILIOS);
        // La capacitación de riesgo MEDIO/ALTO se añade explícitamente después si aplica
    }

    public void habilitarParaRiesgoMedio() {
        agregarCapacitacion(Capacitacion.OPERACION_ATRACCION_RIESGO_MEDIO);
    }

    public void habilitarParaRiesgoAlto() {
        agregarCapacitacion(Capacitacion.OPERACION_ATRACCION_RIESGO_ALTO);
    }

    /**
     * Habilita al operario para una atracción específica de alto riesgo.
     * Requiere que ya tenga la capacitación general de alto riesgo.
     * @param capacitacionEspecifica La capacitación específica de la atracción.
     * @throws CapacitacionInsuficienteException si no tiene la capacitación de riesgo alto previa.
     * @throws DatosInvalidosException si la capacitación específica es null.
     */
    public void habilitarParaAtraccionEspecifica(Capacitacion capacitacionEspecifica) {
         if (capacitacionEspecifica == null) {
             throw new DatosInvalidosException("La capacitación específica no puede ser null.");
         }
        if (!tieneCapacitacion(Capacitacion.OPERACION_ATRACCION_RIESGO_ALTO)) {
            throw new CapacitacionInsuficienteException("Debe tener habilitación para riesgo alto antes de obtener capacitación específica.");
        }
        agregarCapacitacion(capacitacionEspecifica);
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public boolean puedeOperarAtraccionAltoRiesgo() {
        return tieneCapacitacion(Capacitacion.OPERACION_ATRACCION_RIESGO_ALTO);
    }

    public boolean puedeOperarAtraccionRiesgoMedio() {
        return tieneCapacitacion(Capacitacion.OPERACION_ATRACCION_RIESGO_MEDIO);
    }

    /**
     * Verifica si el operario tiene certificado de seguridad vigente.
     * @return true si tiene certificado, false en caso contrario
     */
    public boolean tieneCertificadoSeguridad() {
        return certificadoSeguridad;
    }

    /**
     * Obtiene la lista de atracciones que el operario está habilitado a manejar.
     * @return Una copia inmutable de la lista de nombres de atracciones
     */
    public List<String> getAtraccionesHabilitadas() {
        return List.copyOf(atraccionesHabilitadas); // Return immutable copy
    }
}

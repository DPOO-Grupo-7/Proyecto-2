package dominio.elementoparque;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dominio.empleado.Capacitacion;
import dominio.empleado.Empleado;
import dominio.empleado.OperarioAtraccion;
import dominio.excepciones.CapacitacionInsuficienteException;
import dominio.excepciones.DatosInvalidosException;
import dominio.util.CondicionClimatica; // Import CondicionClimatica

/**
 * Representa una atracción mecánica del parque de diversiones.
 * <p>
 * Incluye nivel de riesgo, restricciones físicas y de salud para los visitantes, y requisitos específicos de capacitación para sus operarios.
 * Valida altura, peso, salud y clima, y requiere capacitaciones específicas según el nivel de riesgo (MEDIO o ALTO).
 * </p>
 *
 * <b>Contexto:</b> Modelo de dominio para atracciones mecánicas como montañas rusas, barcos pirata, etc.
 *
 * <b>Precondiciones generales:</b>
 * <ul>
 *   <li>ID, nombre, ubicación, cupo, nivel de exclusividad y nivel de riesgo válidos.</li>
 *   <li>Restricciones físicas y listas no nulas.</li>
 *   <li>Capacitación específica obligatoria si el riesgo es ALTO.</li>
 * </ul>
 *
 * <b>Poscondiciones generales:</b>
 * <ul>
 *   <li>Atracción mecánica inicializada y lista para asignación de empleados y operación.</li>
 * </ul>
 *
 * @author Sistema Parque
 * @example
 * <pre>
 *     AtraccionMecanica am = new AtraccionMecanica("A2", "Montaña Rusa", "Zona 1", 24, 3, NivelExclusividad.DIAMANTE, NivelRiesgo.ALTO, 1.2, 2.0, 40, 120, List.of("Cardiopatía"), List.of("Vértigo"), List.of(CondicionClimatica.TORMENTA), Capacitacion.OPERACION_MONTAÑA_RUSA);
 * </pre>
 */
public class AtraccionMecanica extends Atraccion {
    // nivelRiesgo puede ser final si no cambia
    private final NivelRiesgo nivelRiesgo;
    // Quitar final para permitir modificación por administrador via setters
    private double restriccionAlturaMinima;
    private double restriccionAlturaMaxima;
    private double restriccionPesoMinimo;
    private double restriccionPesoMaximo;
    // Mantener como List<String> por ahora, podría ser un objeto más complejo
    private List<String> contraindicacionesSalud;
    private List<String> restriccionesSalud;
    // Puede ser final si se define al crear y no cambia
    private final Capacitacion capacitacionEspecifica;

    /**
     * Constructor para crear una atracción mecánica.
     *
     * <b>Precondiciones:</b>
     * <ul>
     *   <li>ID, nombre, ubicación, cupo, nivel de exclusividad y nivel de riesgo válidos.</li>
     *   <li>Restricciones físicas y listas no nulas.</li>
     *   <li>Capacitación específica obligatoria si el riesgo es ALTO.</li>
     * </ul>
     *
     * <b>Poscondiciones:</b>
     * <ul>
     *   <li>Atracción mecánica inicializada con restricciones y capacitaciones requeridas.</li>
     * </ul>
     *
     * @param id Identificador único.
     * @param nombre Nombre de la atracción.
     * @param ubicacion Ubicación física.
     * @param cupoMaximo Cupo máximo permitido.
     * @param empleadosMinimos Empleados mínimos requeridos.
     * @param nivelExclusividad Nivel de exclusividad requerido.
     * @param nivelRiesgo Nivel de riesgo (MEDIO o ALTO).
     * @param restriccionAlturaMinima Altura mínima en metros.
     * @param restriccionAlturaMaxima Altura máxima en metros.
     * @param restriccionPesoMinimo Peso mínimo en kg.
     * @param restriccionPesoMaximo Peso máximo en kg.
     * @param contraindicacionesSalud Lista de contraindicaciones de salud.
     * @param restriccionesSalud Lista de otras restricciones de salud.
     * @param climaNoPermitido Lista de condiciones climáticas no permitidas.
     * @param capacitacionEspecifica Capacitación específica requerida si el riesgo es ALTO.
     * @throws DatosInvalidosException Si algún parámetro es inválido.
     * @example
     * <pre>
     *     AtraccionMecanica am = new AtraccionMecanica("A2", "Montaña Rusa", "Zona 1", 24, 3, NivelExclusividad.DIAMANTE, NivelRiesgo.ALTO, 1.2, 2.0, 40, 120, List.of("Cardiopatía"), List.of("Vértigo"), List.of(CondicionClimatica.TORMENTA), Capacitacion.OPERACION_MONTAÑA_RUSA);
     * </pre>
     */
    public AtraccionMecanica(String id, String nombre, String ubicacion,
                           int cupoMaximo, int empleadosMinimos,
                           NivelExclusividad nivelExclusividad, NivelRiesgo nivelRiesgo,
                           double restriccionAlturaMinima, double restriccionAlturaMaxima,
                           double restriccionPesoMinimo, double restriccionPesoMaximo,
                           List<String> contraindicacionesSalud,
                           List<String> restriccionesSalud,
                           List<CondicionClimatica> climaNoPermitido, // Added climaNoPermitido
                           Capacitacion capacitacionEspecifica) {
        // CORRECTED super call
        super(id, nombre, ubicacion, cupoMaximo, empleadosMinimos);
        // Validation for constructor arguments
        if (nivelExclusividad == null) throw new DatosInvalidosException("El nivel de exclusividad no puede ser null.");
        if (nivelRiesgo == null) throw new DatosInvalidosException("El nivel de riesgo no puede ser null.");
        if (restriccionAlturaMinima < 0 || restriccionAlturaMinima >= restriccionAlturaMaxima) throw new DatosInvalidosException("Restricción de altura inválida.");
        if (restriccionPesoMinimo < 0 || restriccionPesoMinimo >= restriccionPesoMaximo) throw new DatosInvalidosException("Restricción de peso inválida.");
        if (contraindicacionesSalud == null) throw new DatosInvalidosException("La lista de contraindicaciones no puede ser null.");
        if (restriccionesSalud == null) throw new DatosInvalidosException("La lista de restricciones de salud no puede ser null.");
        if (nivelRiesgo == NivelRiesgo.ALTO && capacitacionEspecifica == null) {
            throw new DatosInvalidosException("Se requiere una capacitación específica para atracciones de riesgo ALTO.");
        }
        if (climaNoPermitido == null) throw new DatosInvalidosException("La lista de clima no permitido no puede ser null."); // Added validation

        super.setNivelExclusividad(nivelExclusividad); // Set nivelExclusividad using base setter
        this.nivelRiesgo = nivelRiesgo;
        this.restriccionAlturaMinima = restriccionAlturaMinima;
        this.restriccionAlturaMaxima = restriccionAlturaMaxima;
        this.restriccionPesoMinimo = restriccionPesoMinimo;
        this.restriccionPesoMaximo = restriccionPesoMaximo;
        this.contraindicacionesSalud = new ArrayList<>(contraindicacionesSalud);
        this.restriccionesSalud = new ArrayList<>(restriccionesSalud);
        this.capacitacionEspecifica = capacitacionEspecifica;
        super.setClimaNoPermitido(climaNoPermitido); // Set climate restrictions using base method

        // Configurar capacitaciones requeridas en la clase base
        Set<Capacitacion> capacitaciones = new HashSet<>();
        // Base capacitaciones for any OperarioAtraccion
        capacitaciones.add(Capacitacion.CERTIFICADO_SEGURIDAD_ATRACCIONES);
        capacitaciones.add(Capacitacion.PRIMEROS_AUXILIOS);

        // Add risk-specific capacitaciones
        if (nivelRiesgo == NivelRiesgo.ALTO) {
            capacitaciones.add(Capacitacion.OPERACION_ATRACCION_RIESGO_ALTO);
            if (this.capacitacionEspecifica != null) { // Already validated non-null for ALTO risk
                 capacitaciones.add(this.capacitacionEspecifica);
            }
        } else if (nivelRiesgo == NivelRiesgo.MEDIO) {
            capacitaciones.add(Capacitacion.OPERACION_ATRACCION_RIESGO_MEDIO);
        }
        // Llamar al setter protegido de la clase base
        super.setCapacitacionesRequeridas(capacitaciones);
    }

    // --- Getters ---
    /**
     * Obtiene el nivel de riesgo de la atracción mecánica.
     * @return El nivel de riesgo (MEDIO o ALTO).
     */
    public NivelRiesgo getNivelRiesgo() {
        return nivelRiesgo;
    }

    /**
     * Obtiene la restricción de altura mínima en metros.
     * @return La altura mínima requerida.
     */
    public double getRestriccionAlturaMinima() {
        return restriccionAlturaMinima;
    }

    /**
     * Obtiene la restricción de altura máxima en metros.
     * @return La altura máxima permitida.
     */
    public double getRestriccionAlturaMaxima() {
        return restriccionAlturaMaxima;
    }

    /**
     * Obtiene la restricción de peso mínimo en kg.
     * @return El peso mínimo requerido.
     */
    public double getRestriccionPesoMinimo() {
        return restriccionPesoMinimo;
    }

    /**
     * Obtiene la restricción de peso máximo en kg.
     * @return El peso máximo permitido.
     */
    public double getRestriccionPesoMaximo() {
        return restriccionPesoMaximo;
    }

    /**
     * Obtiene la lista de contraindicaciones de salud.
     * @return Una copia inmutable de la lista de contraindicaciones.
     */
    public List<String> getContraindicacionesSalud() {
        return List.copyOf(contraindicacionesSalud); // Return immutable copy
    }

    /**
     * Obtiene la lista de otras restricciones de salud.
     * @return Una copia inmutable de la lista de restricciones de salud.
     */
    public List<String> getRestriccionesSalud() {
        return List.copyOf(restriccionesSalud); // Return immutable copy
    }

    /**
     * Obtiene la capacitación específica requerida si la atracción es de ALTO riesgo.
     * @return La capacitación específica, o null si el riesgo es MEDIO.
     */
    public Capacitacion getCapacitacionEspecifica() {
        return capacitacionEspecifica;
    }

    // --- Setters (para campos no finales) ---
    /**
     * Establece la restricción de altura mínima.
     * @param alturaMinima Nueva altura mínima en metros. Debe ser >= 0 y < altura máxima actual.
     * @throws DatosInvalidosException Si la altura es inválida.
     */
    public void setRestriccionAlturaMinima(double alturaMinima) {
        if (alturaMinima < 0) {
            throw new DatosInvalidosException("La altura mínima no puede ser negativa.");
        }
        // Usa el getter para la validación cruzada
        if (alturaMinima >= getRestriccionAlturaMaxima()) {
            throw new DatosInvalidosException("La altura mínima debe ser menor que la máxima.");
        }
        this.restriccionAlturaMinima = alturaMinima;
    }

    /**
     * Establece la restricción de altura máxima.
     * @param alturaMaxima Nueva altura máxima en metros. Debe ser > altura mínima actual.
     * @throws DatosInvalidosException Si la altura es inválida.
     */
    public void setRestriccionAlturaMaxima(double alturaMaxima) {
        // Usa el getter para la validación cruzada
        if (alturaMaxima <= getRestriccionAlturaMinima()) {
            throw new DatosInvalidosException("La altura máxima debe ser mayor que la mínima.");
        }
        this.restriccionAlturaMaxima = alturaMaxima;
    }

    /**
     * Establece la restricción de peso mínimo.
     * @param pesoMinimo Nuevo peso mínimo en kg. Debe ser >= 0 y < peso máximo actual.
     * @throws DatosInvalidosException Si el peso es inválido.
     */
    public void setRestriccionPesoMinimo(double pesoMinimo) {
        if (pesoMinimo < 0) {
            throw new DatosInvalidosException("El peso mínimo no puede ser negativo.");
        }
        if (pesoMinimo >= getRestriccionPesoMaximo()) {
            throw new DatosInvalidosException("El peso mínimo debe ser menor que el máximo.");
        }
        this.restriccionPesoMinimo = pesoMinimo;
    }

    /**
     * Establece la restricción de peso máximo.
     * @param pesoMaximo Nuevo peso máximo en kg. Debe ser > peso mínimo actual.
     * @throws DatosInvalidosException Si el peso es inválido.
     */
    public void setRestriccionPesoMaximo(double pesoMaximo) {
        if (pesoMaximo <= getRestriccionPesoMinimo()) {
            throw new DatosInvalidosException("El peso máximo debe ser mayor que el mínimo.");
        }
        this.restriccionPesoMaximo = pesoMaximo;
    }

    /**
     * Establece la lista de contraindicaciones de salud.
     * Reemplaza la lista existente.
     * @param contraindicaciones Nueva lista de contraindicaciones. No puede ser null.
     * @throws DatosInvalidosException Si la lista es null.
     */
    public void setContraindicacionesSalud(List<String> contraindicaciones) {
        if (contraindicaciones == null) {
            throw new DatosInvalidosException("La lista de contraindicaciones no puede ser null.");
        }
        this.contraindicacionesSalud = new ArrayList<>(contraindicaciones); // Use copy
    }

    /**
     * Establece la lista de otras restricciones de salud.
     * Reemplaza la lista existente.
     * @param restricciones Nueva lista de restricciones. No puede ser null.
     * @throws DatosInvalidosException Si la lista es null.
     */
    public void setRestriccionesSalud(List<String> restricciones) {
        if (restricciones == null) {
            throw new DatosInvalidosException("La lista de restricciones no puede ser null.");
        }
        this.restriccionesSalud = new ArrayList<>(restricciones); // Use copy
    }

    // --- Lógica de Negocio y LugarTrabajo ---

    /**
     * Determina si un empleado específico puede ser asignado a esta atracción mecánica.
     * Verifica que sea un OperarioAtraccion y que posea todas las capacitaciones requeridas.
     *
     * <b>Precondiciones:</b> El empleado no debe ser nulo.
     * <b>Poscondiciones:</b> Devuelve true si cumple los requisitos, false si no.
     *
     * @param empleado El empleado a evaluar.
     * @return true si el empleado cumple los requisitos, false en caso contrario.
     * @example
     * <pre>
     *     boolean puede = am.puedeAsignarEmpleado(empleado);
     * </pre>
     */
    @Override
    public boolean puedeAsignarEmpleado(Empleado empleado) {
        // Requisito 1: Debe ser un OperarioAtraccion
        if (!(empleado instanceof OperarioAtraccion)) {
            return false;
        }
        // Requisito 2: Debe cumplir TODAS las capacitaciones requeridas por esta atracción
        // Se obtienen las capacitaciones configuradas en el constructor (vía super.setCapacitacionesRequeridas)
        return empleado.cumpleCapacitaciones(super.getCapacitacionesRequeridas());
    }

    /**
     * Asigna un empleado a la atracción mecánica.
     *
     * <b>Precondiciones:</b> Empleado no nulo, debe ser OperarioAtraccion y cumplir capacitaciones requeridas.
     * <b>Poscondiciones:</b> Empleado asignado si cumple requisitos.
     *
     * @param empleado Empleado a asignar.
     * @throws DatosInvalidosException si el empleado es nulo.
     * @throws CapacitacionInsuficienteException si no cumple las capacitaciones requeridas.
     * @example
     * <pre>
     *     am.asignarEmpleado(empleado);
     * </pre>
     */
    @Override
    public void asignarEmpleado(Empleado empleado) {
        if (empleado == null) {
            throw new DatosInvalidosException("El empleado a asignar no puede ser nulo.");
        }
        // Para cumplir con el test, cualquier empleado que no sea OperarioAtraccion o no tenga las capacitaciones requeridas
        // debe lanzar CapacitacionInsuficienteException
        if (!(empleado instanceof OperarioAtraccion) || !empleado.cumpleCapacitaciones(super.getCapacitacionesRequeridas())) {
            throw new CapacitacionInsuficienteException(empleado, this);
        }
        super.asignarEmpleado(empleado);
    }
}

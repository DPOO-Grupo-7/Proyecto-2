package dominio.elementoparque;

import java.util.ArrayList; // Importar ArrayList
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import dominio.empleado.Capacitacion;
import dominio.empleado.Empleado;
import dominio.excepciones.DatosInvalidosException; // Importar
import dominio.util.CondicionClimatica;
import dominio.excepciones.CapacitacionInsuficienteException; // Importar

/**
 * Representa una atracción cultural del parque de diversiones.
 * <p>
 * Incluye restricciones de edad mínima y condiciones climáticas no permitidas.
 * Requiere capacitaciones básicas de atención al cliente y primeros auxilios.
 * </p>
 *
 * <b>Contexto:</b> Modelo de dominio para atracciones tipo espectáculo o experiencia interactiva.
 *
 * <b>Precondiciones generales:</b>
 * <ul>
 *   <li>ID, nombre, ubicación y cupo válidos.</li>
 *   <li>Edad mínima no negativa.</li>
 * </ul>
 *
 * <b>Poscondiciones generales:</b>
 * <ul>
 *   <li>Atracción cultural inicializada y lista para asignación de empleados y operación.</li>
 * </ul>
 *
 * @author Sistema Parque
 */
public class AtraccionCultural extends Atraccion {
    private final int edadMinima;

    /**
     * Constructor para crear una atracción cultural.
     *
     * <b>Precondiciones:</b>
     * <ul>
     *   <li>ID, nombre, ubicación y cupo válidos.</li>
     *   <li>Edad mínima no negativa.</li>
     * </ul>
     *
     * <b>Poscondiciones:</b>
     * <ul>
     *   <li>Atracción cultural inicializada con restricciones y capacitaciones básicas.</li>
     * </ul>
     *
     * @param id Identificador único.
     * @param nombre Nombre de la atracción.
     * @param ubicacion Ubicación física.
     * @param cupoMaximo Cupo máximo permitido.
     * @param empleadosMinimos Empleados mínimos requeridos.
     * @param edadMinima Edad mínima permitida.
     * @param climaNoPermitido Lista de condiciones climáticas no permitidas.
     * @throws DatosInvalidosException si algún parámetro es inválido.
     * @example
     * <pre>
     *     AtraccionCultural ac = new AtraccionCultural("A1", "Castillo del Terror", "Zona 2", 40, 2, 12, List.of(CondicionClimatica.TORMENTA));
     * </pre>
     */
    public AtraccionCultural(String id, String nombre, String ubicacion, int cupoMaximo, 
                           int empleadosMinimos, int edadMinima, 
                           List<CondicionClimatica> climaNoPermitido) {
        super(id, nombre, ubicacion, cupoMaximo, empleadosMinimos); // Valida id, nombre, cupo, empleadosMinimos
        if (edadMinima < 0) {
            throw new DatosInvalidosException("La edad mínima no puede ser negativa.");
        }
        this.edadMinima = edadMinima;
        this.climaNoPermitido = climaNoPermitido != null ? new ArrayList<>(climaNoPermitido) : new ArrayList<>();
        
        // Las atracciones culturales requieren capacitaciones básicas
        Set<Capacitacion> capacitaciones = new HashSet<>();
        capacitaciones.add(Capacitacion.ATENCION_CLIENTE_GENERAL);
        capacitaciones.add(Capacitacion.PRIMEROS_AUXILIOS);
        setCapacitacionesRequeridas(capacitaciones);
    }

    /**
     * Obtiene la edad mínima permitida para ingresar a la atracción.
     * @return Edad mínima.
     */
    public int getEdadMinima() {
        return edadMinima;
    }

    /**
     * Devuelve una copia inmutable de la lista de condiciones climáticas no permitidas para la atracción cultural.
     *
     * <b>Precondiciones:</b> Ninguna.
     * <b>Poscondiciones:</b> Devuelve la lista de condiciones climáticas restringidas.
     *
     * @return Lista de condiciones climáticas no permitidas.
     * @example
     * <pre>
     *     List<CondicionClimatica> climas = ac.getClimaNoPermitido();
     * </pre>
     */
    public List<CondicionClimatica> getClimaNoPermitido() {
        return List.copyOf(climaNoPermitido);
    }

    /**
     * Indica si la atracción puede operar bajo una condición climática dada.
     *
     * <b>Precondiciones:</b> El parámetro clima no debe ser nulo.
     * <b>Poscondiciones:</b> Devuelve true si la atracción puede operar, false si está restringida por clima.
     *
     * @param clima Condición climática a evaluar.
     * @return true si puede operar, false si está restringido.
     * @example
     * <pre>
     *     boolean puede = ac.puedeOperarEnClima(CondicionClimatica.LLUVIOSO);
     * </pre>
     */
    public boolean puedeOperarEnClima(CondicionClimatica clima) {
        return !climaNoPermitido.contains(clima);
    }

    /**
     * Determina si un empleado puede ser asignado a la atracción cultural.
     *
     * <b>Precondiciones:</b> El empleado no debe ser nulo.
     * <b>Poscondiciones:</b> Devuelve true si cumple las capacitaciones requeridas.
     *
     * @param empleado Empleado a evaluar.
     * @return true si cumple las capacitaciones requeridas, false si no.
     * @example
     * <pre>
     *     boolean puede = ac.puedeAsignarEmpleado(empleado);
     * </pre>
     */
    @Override
    public boolean puedeAsignarEmpleado(Empleado empleado) {
        if (empleado == null) return false; // O lanzar DatosInvalidosException
        return empleado.cumpleCapacitaciones(getCapacitacionesRequeridas());
    }

    /**
     * Asigna un empleado a la atracción cultural.
     *
     * <b>Precondiciones:</b> Empleado no nulo y con capacitaciones requeridas.
     * <b>Poscondiciones:</b> Empleado asignado si cumple requisitos.
     *
     * @param empleado Empleado a asignar.
     * @throws DatosInvalidosException si el empleado es null.
     * @throws CapacitacionInsuficienteException si no cumple las capacitaciones requeridas.
     * @example
     * <pre>
     *     ac.asignarEmpleado(empleado);
     * </pre>
     */
    @Override
    public void asignarEmpleado(Empleado empleado) {
        if (empleado == null) {
            throw new DatosInvalidosException("El empleado a asignar no puede ser nulo.");
        }
        if (!puedeAsignarEmpleado(empleado)) {
            throw new CapacitacionInsuficienteException(empleado, this);
        }
        // Llama a la implementación base para añadir a la lista si no está ya
        super.asignarEmpleado(empleado);
    }

    /**
     * Indica si una persona puede acceder según su edad.
     * @param edad Edad del visitante.
     * @return true si la edad es suficiente, false si no.
     * @example
     * <pre>
     *     boolean acceso = ac.permiteAccesoPorEdad(15);
     * </pre>
     */
    public boolean permiteAccesoPorEdad(int edad) {
        return edad >= edadMinima;
    }
}

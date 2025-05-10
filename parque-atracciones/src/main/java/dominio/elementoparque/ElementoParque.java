package dominio.elementoparque;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import dominio.util.CondicionClimatica;
import dominio.excepciones.DatosInvalidosException;

/**
 * Representa un elemento del parque de diversiones, que puede ser una atracción o un espectáculo.
 * <p>
 * Esta clase abstracta define los atributos y comportamientos comunes a todos los elementos del parque,
 * como el nombre, cupo máximo, restricciones de clima y disponibilidad por temporada.
 * </p>
 *
 * <b>Contexto:</b> Forma parte del modelo de dominio del sistema de gestión del parque, permitiendo
 * controlar la operación y disponibilidad de atracciones y espectáculos.
 *
 * <b>Precondiciones generales:</b>
 * <ul>
 *   <li>El ID y el nombre no deben ser nulos ni vacíos.</li>
 *   <li>El cupo máximo debe ser mayor que cero.</li>
 * </ul>
 *
 * <b>Poscondiciones generales:</b>
 * <ul>
 *   <li>El elemento queda correctamente inicializado y puede ser consultado o modificado según reglas de negocio.</li>
 * </ul>
 *
 * @author Sistema Parque
 */
public abstract class ElementoParque {
    private final String id;
    private String nombre;
    protected int cupoMaximo;
    private boolean esDeTemporada;
    private LocalDateTime fechaInicioTemporada;
    private LocalDateTime fechaFinTemporada;
    protected List<CondicionClimatica> climaNoPermitido;

    /**
     * Constructor protegido para inicializar un elemento del parque.
     *
     * <b>Precondiciones:</b>
     * <ul>
     *   <li>El ID y el nombre no deben ser nulos ni vacíos.</li>
     *   <li>El cupo máximo debe ser mayor que cero.</li>
     * </ul>
     *
     * <b>Poscondiciones:</b>
     * <ul>
     *   <li>El elemento queda inicializado con los valores dados.</li>
     * </ul>
     *
     * @param id Identificador único del elemento.
     * @param nombre Nombre del elemento.
     * @param cupoMaximo Cupo máximo de personas permitido.
     * @throws DatosInvalidosException si algún parámetro es inválido.
     * @example
     * <pre>
     *     ElementoParque atraccion = new AtraccionCultural("A1", "Castillo del Terror", 40);
     * </pre>
     */
    protected ElementoParque(String id, String nombre, int cupoMaximo) {
        if (id == null || id.trim().isEmpty()) {
            throw new DatosInvalidosException("El ID no puede ser nulo o vacío.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre no puede ser nulo o vacío.");
        }
        if (cupoMaximo <= 0) {
            throw new DatosInvalidosException("El cupo máximo debe ser positivo.");
        }
        this.id = id;
        this.nombre = nombre;
        this.cupoMaximo = cupoMaximo;
        this.esDeTemporada = false;
        this.climaNoPermitido = new ArrayList<>();
    }

    /**
     * Obtiene el identificador único del elemento.
     * @return ID del elemento.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el nombre del elemento.
     * @return Nombre del elemento.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el cupo máximo de personas permitido.
     * @return Cupo máximo.
     */
    public int getCupoMaximo() {
        return cupoMaximo;
    }

    /**
     * Indica si el elemento es de temporada (solo disponible en ciertas fechas).
     * @return true si es de temporada, false en caso contrario.
     */
    public boolean isEsDeTemporada() {
        return esDeTemporada;
    }

    /**
     * Define el rango de fechas en el que el elemento está disponible (temporada).
     *
     * <b>Precondiciones:</b>
     * <ul>
     *   <li>Las fechas no deben ser nulas y la fecha de fin debe ser posterior a la de inicio.</li>
     * </ul>
     *
     * <b>Poscondiciones:</b>
     * <ul>
     *   <li>El elemento queda marcado como de temporada y con las fechas indicadas.</li>
     * </ul>
     *
     * @param fechaInicio Fecha de inicio de la temporada.
     * @param fechaFin Fecha de fin de la temporada.
     * @throws DatosInvalidosException si las fechas son inválidas.
     * @example
     * <pre>
     *     atraccion.setTemporada(LocalDateTime.of(2025,6,1,0,0), LocalDateTime.of(2025,8,31,23,59));
     * </pre>
     */
    public void setTemporada(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio == null || fechaFin == null || fechaFin.isBefore(fechaInicio)) {
            throw new DatosInvalidosException("Las fechas de temporada son inválidas.");
        }
        this.fechaInicioTemporada = fechaInicio;
        this.fechaFinTemporada = fechaFin;
        this.esDeTemporada = true;
    }

    /**
     * Obtiene la fecha de inicio de la temporada, si aplica.
     * @return Fecha de inicio de temporada o null si no es de temporada.
     */
    public LocalDateTime getFechaInicioTemporada() {
        return fechaInicioTemporada;
    }

    /**
     * Obtiene la fecha de fin de la temporada, si aplica.
     * @return Fecha de fin de temporada o null si no es de temporada.
     */
    public LocalDateTime getFechaFinTemporada() {
        return fechaFinTemporada;
    }

    /**
     * Obtiene la lista de condiciones climáticas en las que el elemento no puede operar.
     * @return Lista de condiciones climáticas no permitidas.
     */
    public List<CondicionClimatica> getClimaNoPermitido() {
        return new ArrayList<>(climaNoPermitido);
    }

    /**
     * Define las condiciones climáticas en las que el elemento no puede operar.
     *
     * <b>Precondiciones:</b> La lista no debe ser nula.
     * <b>Poscondiciones:</b> El elemento tendrá restringida la operación en los climas indicados.
     *
     * @param climaNoPermitido Lista de condiciones climáticas no permitidas.
     */
    public void setClimaNoPermitido(List<CondicionClimatica> climaNoPermitido) {
        this.climaNoPermitido = new ArrayList<>(climaNoPermitido);
    }

    /**
     * Indica si el elemento puede operar bajo una condición climática dada.
     *
     * <b>Precondiciones:</b> El clima no debe ser nulo.
     * <b>Poscondiciones:</b> Devuelve true si puede operar, false si está restringido.
     *
     * @param clima Condición climática a evaluar.
     * @return true si puede operar, false si está restringido.
     * @example
     * <pre>
     *     boolean puede = atraccion.puedeOperarEnClima(CondicionClimatica.TORMENTA);
     * </pre>
     */
    public boolean puedeOperarEnClima(CondicionClimatica clima) {
        return !climaNoPermitido.contains(clima);
    }

    /**
     * Indica si el elemento está disponible en una fecha dada (considerando temporada).
     *
     * <b>Precondiciones:</b> La fecha no debe ser nula.
     * <b>Poscondiciones:</b> Devuelve true si está disponible, false si no.
     *
     * @param fecha Fecha a consultar.
     * @return true si está disponible, false si no.
     * @example
     * <pre>
     *     boolean disponible = atraccion.estaDisponibleEnFecha(LocalDateTime.now());
     * </pre>
     */
    public boolean estaDisponibleEnFecha(LocalDateTime fecha) {
        if (!esDeTemporada) {
            return true;
        }
        return !fecha.isBefore(fechaInicioTemporada) && !fecha.isAfter(fechaFinTemporada);
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre no puede ser nulo o vacío.");
        }
        this.nombre = nombre;
    }
}

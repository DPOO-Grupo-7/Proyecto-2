package dominio.elementoparque;

import java.util.List;
import java.util.ArrayList;

import dominio.excepciones.DatosInvalidosException;
import dominio.util.CondicionClimatica;
import dominio.util.RangoFechaHora;

/**
 * Representa un espectáculo ofrecido en el parque, con horarios específicos,
 * posible estacionalidad y restricciones climáticas.
 * <p>
 * No tiene ubicación fija ni empleados directos del parque.
 * </p>
 *
 * <b>Contexto:</b> Modelo de dominio para espectáculos y eventos en el parque.
 *
 * <b>Precondiciones generales:</b>
 * <ul>
 *   <li>ID, nombre, ubicación, cupo y descripción válidos.</li>
 *   <li>Debe tener al menos un horario definido.</li>
 * </ul>
 *
 * <b>Poscondiciones generales:</b>
 * <ul>
 *   <li>Espectáculo inicializado y listo para consulta de horarios y restricciones.</li>
 * </ul>
 *
 * @author Sistema Parque
 */
public class Espectaculo extends ElementoParque {
    private String descripcion;
    private List<RangoFechaHora> horarios;
    private String ubicacion;

    /**
     * Constructor para crear un espectáculo.
     *
     * <b>Precondiciones:</b>
     * <ul>
     *   <li>ID, nombre, ubicación, cupo y descripción válidos.</li>
     *   <li>Debe tener al menos un horario definido.</li>
     * </ul>
     *
     * <b>Poscondiciones:</b>
     * <ul>
     *   <li>Espectáculo inicializado con horarios y restricciones climáticas.</li>
     * </ul>
     *
     * @param id Identificador único del espectáculo.
     * @param nombre Nombre del espectáculo.
     * @param ubicacion Descripción de la ubicación donde se realiza (puede ser variable).
     * @param cupoMaximo Capacidad máxima de personas simultáneas.
     * @param descripcion Breve descripción del espectáculo.
     * @param horarios Lista de horarios (rangos de fecha/hora) en los que se presenta.
     * @param climaNoPermitido Lista de condiciones climáticas que impiden su realización.
     * @throws DatosInvalidosException si los datos son inválidos.
     * @example
     * <pre>
     *     Espectaculo e = new Espectaculo("E1", "Show de Magia", "Plaza Central", 200, "Magia para toda la familia", List.of(new RangoFechaHora(...)), List.of(CondicionClimatica.TORMENTA));
     * </pre>
     */
    public Espectaculo(String id, String nombre, String ubicacion, int cupoMaximo,
                       String descripcion, List<RangoFechaHora> horarios, List<CondicionClimatica> climaNoPermitido) {
        super(id, nombre, cupoMaximo);
        
        if (ubicacion == null || ubicacion.trim().isEmpty()) {
            throw new DatosInvalidosException("La ubicación del espectáculo no puede ser nula o vacía.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new DatosInvalidosException("La descripción del espectáculo no puede ser nula o vacía.");
        }
        if (horarios == null || horarios.isEmpty()) {
             throw new DatosInvalidosException("Debe proporcionar al menos un horario para el espectáculo.");
        }
        
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.horarios = new ArrayList<>(horarios);
        super.setClimaNoPermitido(climaNoPermitido != null ? climaNoPermitido : new ArrayList<>());
    }

    /**
     * Obtiene la ubicación del espectáculo.
     * @return Ubicación del espectáculo.
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Define la ubicación del espectáculo.
     * @param ubicacion Nueva ubicación.
     * @throws DatosInvalidosException si la ubicación es nula o vacía.
     */
    public void setUbicacion(String ubicacion) {
        if (ubicacion == null || ubicacion.trim().isEmpty()) {
            throw new DatosInvalidosException("La ubicación del espectáculo no puede ser nula o vacía.");
        }
        this.ubicacion = ubicacion;
    }

    /**
     * Obtiene la descripción del espectáculo.
     * @return Descripción.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Define la descripción del espectáculo.
     * @param descripcion Nueva descripción.
     * @throws DatosInvalidosException si la descripción es nula o vacía.
     */
    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new DatosInvalidosException("La descripción del espectáculo no puede ser nula o vacía.");
        }
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la lista de horarios del espectáculo.
     * @return Lista de rangos de fecha/hora.
     */
    public List<RangoFechaHora> getHorarios() {
        return List.copyOf(horarios);
    }

    /**
     * Define la lista de horarios del espectáculo.
     * @param horarios Nueva lista de horarios.
     * @throws DatosInvalidosException si la lista es nula o vacía.
     */
    public void setHorarios(List<RangoFechaHora> horarios) {
         if (horarios == null || horarios.isEmpty()) {
             throw new DatosInvalidosException("Debe proporcionar al menos un horario para el espectáculo.");
         }
        this.horarios = new ArrayList<>(horarios);
    }

    /**
     * Define el cupo máximo del espectáculo.
     * @param cupoMaximo Nuevo cupo máximo.
     * @throws DatosInvalidosException si el cupo máximo no es positivo.
     */
    public void setCupoMaximo(int cupoMaximo) {
        if (cupoMaximo <= 0) {
            throw new DatosInvalidosException("El cupo máximo debe ser positivo.");
        }
        super.cupoMaximo = cupoMaximo;
    }
}
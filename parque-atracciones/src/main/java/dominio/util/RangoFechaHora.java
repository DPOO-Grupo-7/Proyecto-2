package dominio.util;

import java.time.LocalDateTime;
import dominio.excepciones.DatosInvalidosException;

/**
 * Representa un rango de fechas y horas.
 * <p>
 * Permite verificar inclusión de fechas y superposición de rangos.
 * </p>
 *
 * <b>Contexto:</b> Utilizado para definir horarios de espectáculos y disponibilidad de atracciones.
 *
 * <b>Precondiciones generales:</b>
 * <ul>
 *   <li>Las fechas de inicio y fin no deben ser nulas.</li>
 *   <li>La fecha de fin debe ser posterior o igual a la de inicio.</li>
 * </ul>
 *
 * <b>Poscondiciones generales:</b>
 * <ul>
 *   <li>El rango queda correctamente inicializado y puede ser consultado.</li>
 * </ul>
 *
 * @author Sistema Parque
 * @example
 * <pre>
 *     RangoFechaHora rango = new RangoFechaHora(LocalDateTime.of(2025,6,1,10,0), LocalDateTime.of(2025,6,1,12,0));
 *     boolean dentro = rango.incluyeFecha(LocalDateTime.of(2025,6,1,11,0));
 * </pre>
 */
public class RangoFechaHora {
    private LocalDateTime inicio;
    private LocalDateTime fin;

    /**
     * Constructor para crear un rango de fechas y horas.
     *
     * <b>Precondiciones:</b> Las fechas de inicio y fin no deben ser nulas. La fecha de fin debe ser posterior o igual a la de inicio.
     * <b>Poscondiciones:</b> El rango queda inicializado con los valores dados.
     *
     * @param inicio Fecha y hora de inicio.
     * @param fin Fecha y hora de fin.
     * @throws DatosInvalidosException si las fechas son nulas o el rango es inválido.
     * @example
     * <pre>
     *     RangoFechaHora r = new RangoFechaHora(LocalDateTime.of(2025,6,1,10,0), LocalDateTime.of(2025,6,1,12,0));
     * </pre>
     */
    public RangoFechaHora(LocalDateTime inicio, LocalDateTime fin) {
         if (inicio == null || fin == null) {
             throw new DatosInvalidosException("Las fechas de inicio y fin no pueden ser nulas.");
         }
        if (fin.isBefore(inicio)) {
            throw new DatosInvalidosException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        this.inicio = inicio;
        this.fin = fin;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    /**
     * Verifica si una fecha está incluida en el rango.
     *
     * <b>Precondiciones:</b> La fecha no debe ser nula.
     * <b>Poscondiciones:</b> Devuelve true si la fecha está dentro del rango.
     *
     * @param fecha Fecha a verificar.
     * @return true si la fecha está dentro del rango, false si no.
     * @example
     * <pre>
     *     boolean dentro = rango.incluyeFecha(LocalDateTime.now());
     * </pre>
     */
    public boolean incluyeFecha(LocalDateTime fecha) {
        return !fecha.isBefore(inicio) && !fecha.isAfter(fin);
    }

    /**
     * Verifica si este rango se superpone con otro rango.
     *
     * <b>Precondiciones:</b> El otro rango no debe ser nulo.
     * <b>Poscondiciones:</b> Devuelve true si hay superposición.
     *
     * @param otro Otro rango a comparar.
     * @return true si hay superposición, false si no.
     * @example
     * <pre>
     *     boolean solapa = rango.seSuperponeCon(otroRango);
     * </pre>
     */
    public boolean seSuperponeCon(RangoFechaHora otro) {
        return !this.fin.isBefore(otro.inicio) && !this.inicio.isAfter(otro.fin);
    }
}
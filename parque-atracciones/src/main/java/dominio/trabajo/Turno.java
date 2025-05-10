package dominio.trabajo;

/**
 * Enum que representa los turnos de trabajo en el parque de diversiones.
 * <p>
 * Cada turno tiene una descripción, hora de inicio y hora de fin.
 * </p>
 *
 * <b>Precondiciones:</b> Ninguna.
 * <b>Poscondiciones:</b> Permite distinguir entre turnos de apertura y cierre.
 *
 * @author Sistema Parque
 * @example
 * <pre>
 *     Turno turno = Turno.APERTURA;
 *     System.out.println(turno.getDescripcion()); // Salida: Turno de apertura
 * </pre>
 */
public enum Turno {
    APERTURA("Turno de apertura", "06:00", "14:00"),
    CIERRE("Turno de cierre", "14:00", "22:00");

    private final String descripcion;
    private final String horaInicio;
    private final String horaFin;

    Turno(String descripcion, String horaInicio, String horaFin) {
        this.descripcion = descripcion;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    /**
     * Obtiene la descripción del turno.
     * @return Descripción del turno.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Obtiene la hora de inicio del turno (formato HH:mm).
     * @return Hora de inicio.
     */
    public String getHoraInicio() {
        return horaInicio;
    }

    /**
     * Obtiene la hora de fin del turno (formato HH:mm).
     * @return Hora de fin.
     */
    public String getHoraFin() {
        return horaFin;
    }
}

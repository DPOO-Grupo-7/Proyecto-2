package dominio.trabajo;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import dominio.empleado.Empleado;
import dominio.empleado.ServicioGeneral;
import dominio.excepciones.DatosInvalidosException; // Importar

/**
 * Representa la asignación específica de un empleado a un lugar de trabajo
 * para una fecha y turno determinados.
 */
public class AsignacionTurno {
    private final String id;
    private final Empleado empleado;
    private final LugarTrabajo lugarTrabajo;
    private final LocalDate fecha;
    private final Turno turno;

    /**
     * Constructor para asignaciones a un lugar de trabajo específico.
     * @param id Identificador único de la asignación
     * @param empleado Empleado asignado
     * @param lugarTrabajo Lugar de trabajo donde se asigna
     * @param fecha Fecha de la asignación
     * @param turno Turno asignado
     * @throws DatosInvalidosException si algún parámetro es nulo (excepto lugarTrabajo que puede ser null)
     */
    public AsignacionTurno(String id, Empleado empleado, LugarTrabajo lugarTrabajo, 
                          LocalDate fecha, Turno turno) {
        validarParametros(id, empleado, fecha, turno); // lugarTrabajo puede ser null
        
        this.id = id;
        this.empleado = empleado;
        this.lugarTrabajo = lugarTrabajo;
        this.fecha = fecha;
        this.turno = turno;
    }

    /**
     * Constructor específico para asignaciones de servicio general (sin lugar fijo).
     * @param id Identificador único de la asignación
     * @param empleado Empleado de servicio general
     * @param fecha Fecha de la asignación
     * @param turno Turno asignado
     * @return Nueva instancia de AsignacionTurno para servicio general
     * @throws DatosInvalidosException si el empleado no es de servicio general o si otros params son null
     */
    public static AsignacionTurno paraServicioGeneral(String id, Empleado empleado, 
                                                     LocalDate fecha, Turno turno) {
        validarParametros(id, empleado, fecha, turno);

        if (!(empleado instanceof ServicioGeneral)) {
            throw new DatosInvalidosException(
                "Este constructor es solo para empleados de Servicio General.");
        }
        
        return new AsignacionTurno(id, empleado, null, fecha, turno);
    }

    /**
     * Valida los parámetros comunes obligatorios.
     * @throws DatosInvalidosException si algún parámetro es nulo.
     */
    private static void validarParametros(String id, Empleado empleado, LocalDate fecha, Turno turno) {
        if (id == null || id.trim().isEmpty()) { // ID también debe ser válido
            throw new DatosInvalidosException("El ID de la asignación no puede ser nulo o vacío");
        }
        if (empleado == null) {
            throw new DatosInvalidosException("El empleado no puede ser nulo para una asignación");
        }
        if (fecha == null) {
            throw new DatosInvalidosException("La fecha no puede ser nula para una asignación");
        }
        if (turno == null) {
            throw new DatosInvalidosException("El turno no puede ser nulo para una asignación");
        }
    }

    public String getId() {
        return id;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public LugarTrabajo getLugarTrabajo() {
        return lugarTrabajo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Turno getTurno() {
        return turno;
    }

    public boolean esDeServicioGeneral() {
        return this.lugarTrabajo == null && this.empleado instanceof ServicioGeneral;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AsignacionTurno that = (AsignacionTurno) o;
        return Objects.equals(empleado, that.empleado) &&
               Objects.equals(lugarTrabajo, that.lugarTrabajo) &&
               Objects.equals(fecha, that.fecha) &&
               turno == that.turno;
    }

    @Override
    public int hashCode() {
        return Objects.hash(empleado, lugarTrabajo, fecha, turno);
    }

    @Override
    public String toString() {
        String nombreLugar = (lugarTrabajo != null) ? 
            lugarTrabajo.getNombreLugar() : "Servicio General";
        return "AsignacionTurno{" +
               "id='" + id + '\'' +
               ", empleado=" + empleado.getNombre() +
               ", lugarTrabajo=" + nombreLugar +
               ", fecha=" + fecha +
               ", turno=" + turno +
               '}';
    }

    /**
     * Genera un identificador único para una nueva asignación.
     * @return String con el UUID generado
     */
    public static String generarIdUnico() {
        return UUID.randomUUID().toString();
    }
}

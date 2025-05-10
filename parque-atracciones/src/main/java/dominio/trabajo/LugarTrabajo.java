package dominio.trabajo;

import java.util.Set;

import dominio.empleado.Capacitacion;
import dominio.empleado.Empleado;
import dominio.excepciones.AsignacionInvalidaException; // Importar
import dominio.excepciones.DatosInvalidosException; // Importar

/**
 * Interfaz que define los lugares donde pueden trabajar los empleados.
 */
public interface LugarTrabajo {
    /**
     * Obtiene el nombre del lugar de trabajo.
     * @return Nombre del lugar
     */
    String getNombreLugar();
    
    /**
     * Asigna un empleado a este lugar de trabajo.
     * Verifica que el empleado tenga las capacitaciones requeridas y cumpla otros requisitos.
     * @param empleado Empleado a asignar
     * @throws AsignacionInvalidaException si el empleado no cumple con los requisitos (capacitación, tipo, etc.)
     * @throws DatosInvalidosException si el empleado es null
     */
    void asignarEmpleado(Empleado empleado);
    
    /**
     * Verifica si el lugar cumple con los requisitos de personal.
     * @return true si cumple los requisitos, false en caso contrario
     */
    boolean cumpleRequisitosPersonal();

    /**
     * Verifica si un empleado puede ser asignado a este lugar de trabajo.
     * @param empleado Empleado a verificar
     * @return true si el empleado puede ser asignado, false en caso contrario
     */
    boolean puedeAsignarEmpleado(Empleado empleado);

    /**
     * Obtiene las capacitaciones requeridas para trabajar en este lugar.
     * @return Conjunto de capacitaciones requeridas
     */
    Set<Capacitacion> getCapacitacionesRequeridas();
}

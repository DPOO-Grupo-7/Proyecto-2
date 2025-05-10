package dominio.trabajo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import dominio.empleado.Capacitacion;
import dominio.empleado.Empleado;
import dominio.empleado.Cajero;
import dominio.excepciones.DatosInvalidosException; // Importar
import dominio.excepciones.AsignacionInvalidaException; // Importar
import dominio.excepciones.CapacitacionInsuficienteException; // Importar

/**
 * Representa una tienda dentro del parque, un lugar de trabajo que requiere
 * empleados con capacitación específica (ej. Cajero, Ventas).
 */
public class Tienda implements LugarTrabajo {

    private String nombre;
    private Set<Capacitacion> capacitacionesRequeridas; 
    private List<Empleado> empleadosAsignados;
    private int maximoEmpleados;

    /**
     * Constructor de Tienda.
     *
     * @param nombre Nombre de la tienda.
     * @param maximoEmpleados Número máximo de empleados que pueden trabajar simultáneamente.
     * @throws DatosInvalidosException si nombre es inválido o maximoEmpleados < 1
     */
    public Tienda(String nombre, int maximoEmpleados) {
         if (nombre == null || nombre.trim().isEmpty()) {
             throw new DatosInvalidosException("El nombre de la tienda no puede ser nulo o vacío.");
         }
         if (maximoEmpleados < 1) {
             throw new DatosInvalidosException("El máximo de empleados debe ser al menos 1.");
         }
        this.nombre = nombre;
        this.maximoEmpleados = maximoEmpleados;
        this.empleadosAsignados = new ArrayList<>();
        this.capacitacionesRequeridas = new HashSet<>();
        this.capacitacionesRequeridas.add(Capacitacion.MANEJO_CAJA); 
        this.capacitacionesRequeridas.add(Capacitacion.ATENCION_CLIENTE_GENERAL); 
    }

    @Override
    public String getNombreLugar() {
        return nombre;
    }

    public List<Empleado> getEmpleadosAsignados() {
        return new ArrayList<>(empleadosAsignados); 
    }

    @Override
    public Set<Capacitacion> getCapacitacionesRequeridas() {
        return new HashSet<>(capacitacionesRequeridas); 
    }

    /**
     * Implementación de LugarTrabajo: Verifica si un empleado puede ser asignado.
     * Chequea si tiene las capacitaciones requeridas y si hay cupo.
     * Un empleado general con las capacitaciones puede trabajar aquí.
     *
     * @param empleado El empleado a verificar.
     * @return true si puede ser asignado, false en caso contrario.
     */
    @Override
    public boolean puedeAsignarEmpleado(Empleado empleado) {
        if (empleado == null) {
            return false; // O lanzar DatosInvalidosException
        }
        if (empleadosAsignados.size() >= maximoEmpleados) {
            System.out.println("Tienda '" + nombre + "' ya tiene el máximo de empleados (" + maximoEmpleados + ").");
            return false; // No hay cupo
        }
        // Verifica si tiene las capacitaciones requeridas para la tienda
        return empleado.cumpleCapacitaciones(this.capacitacionesRequeridas);
    }

    /**
     * Implementación de LugarTrabajo: Asigna un empleado si cumple los requisitos.
     *
     * @param empleado El empleado a asignar.
     * @throws DatosInvalidosException si el empleado es null.
     * @throws AsignacionInvalidaException Si no hay cupo.
     * @throws CapacitacionInsuficienteException Si no tiene las capacitaciones.
     */
    @Override
    public void asignarEmpleado(Empleado empleado) {
         if (empleado == null) {
             throw new DatosInvalidosException("El empleado a asignar no puede ser nulo.");
         }
         if (empleadosAsignados.size() >= maximoEmpleados) {
             throw new AsignacionInvalidaException("No hay cupo disponible en la tienda '" + nombre + "'.");
         }
        if (!empleado.cumpleCapacitaciones(this.capacitacionesRequeridas)) {
             throw new CapacitacionInsuficienteException(empleado, this);
        }

        if (!empleadosAsignados.contains(empleado)) {
             this.empleadosAsignados.add(empleado);
             System.out.println("Empleado '" + empleado.getNombre() + "' asignado a la tienda '" + nombre + "'.");
        } else {
            System.out.println("El empleado '" + empleado.getNombre() + "' ya está asignado a la tienda '" + nombre + "'.");
        }
    }

    @Override
    public boolean cumpleRequisitosPersonal() {
        boolean tieneCajero = empleadosAsignados.stream()
                                            .anyMatch(e -> e instanceof Cajero && e.tieneCapacitacion(Capacitacion.MANEJO_CAJA));
                                            
        return tieneCajero && empleadosAsignados.size() > 0; 
    }

    @Override
    public String toString() {
        return "Tienda [nombre=" + nombre + ", empleadosAsignados=" + empleadosAsignados.size() + ", maximoEmpleados=" + maximoEmpleados + "]";
    }
}

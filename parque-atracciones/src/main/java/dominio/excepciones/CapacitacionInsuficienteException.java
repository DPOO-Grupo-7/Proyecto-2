package dominio.excepciones;

import dominio.empleado.Empleado;
import dominio.trabajo.LugarTrabajo;

public class CapacitacionInsuficienteException extends AsignacionInvalidaException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CapacitacionInsuficienteException(Empleado empleado, LugarTrabajo lugar) {
        super("El empleado '" + empleado.getNombre() + "' no tiene las capacitaciones requeridas para '" + lugar.getNombreLugar() + "'.");
    }
     public CapacitacionInsuficienteException(String message) {
        super(message);
    }
}

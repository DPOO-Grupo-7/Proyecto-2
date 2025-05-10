package dominio.empleado;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;

class EmpleadoTest {
    @Test
    void testEmpleado() {
        Empleado emp = new ServicioGeneral("E1", "Sofia", "sofia@mail.com", "555-10", "sofia", "pass");
        assertEquals("Sofia", emp.getNombre());
        // Capacitaciones
        emp.agregarCapacitacion(Capacitacion.MANEJO_CAJA);
        assertTrue(emp.tieneCapacitacion(Capacitacion.MANEJO_CAJA));
        assertTrue(emp.cumpleCapacitaciones(emp.getCapacitaciones()));
        // No cumple si falta una
        assertFalse(emp.cumpleCapacitaciones(Set.of(Capacitacion.OPERACION_ATRACCION_RIESGO_ALTO)));
    }
}

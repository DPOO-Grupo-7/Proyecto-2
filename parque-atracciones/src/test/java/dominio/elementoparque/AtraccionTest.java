package dominio.elementoparque;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import dominio.empleado.*;
import dominio.excepciones.*;
import java.util.List;

class AtraccionTest {
    @Test
    void testAtraccion() {
        Atraccion atr = new AtraccionCultural("C2", "Teatro", "Zona D", 40, 2, 10, List.of());
        assertEquals("Teatro", atr.getNombre());
        // Asignar empleado válido
        Empleado emp = new ServicioGeneral("SG2", "Maria", "m@mail.com", "555-5", "maria", "pass");
        emp.agregarCapacitacion(dominio.empleado.Capacitacion.ATENCION_CLIENTE_GENERAL); // Agregar la capacitación requerida
        emp.agregarCapacitacion(dominio.empleado.Capacitacion.PRIMEROS_AUXILIOS); // Agregar ambas capacitaciones requeridas
        atr.asignarEmpleado(emp);
        assertTrue(atr.getEmpleadosAsignados().contains(emp));
        // No duplicados
        atr.asignarEmpleado(emp);
        assertEquals(1, atr.getEmpleadosAsignados().stream().filter(e -> e.equals(emp)).count());
        // Asignar empleado nulo lanza excepción
        assertThrows(DatosInvalidosException.class, () -> atr.asignarEmpleado(null));
    }
}

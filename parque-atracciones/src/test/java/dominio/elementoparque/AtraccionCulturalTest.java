package dominio.elementoparque;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import dominio.empleado.*;
import dominio.excepciones.*;
import java.util.List;

class AtraccionCulturalTest {
    @Test
    void testAtraccionCultural() {
        AtraccionCultural ac = new AtraccionCultural("C1", "Museo", "Zona B", 30, 1, 12, List.of());
        assertEquals("Museo", ac.getNombre());
        // Acceso por edad
        assertTrue(ac.permiteAccesoPorEdad(15));
        assertFalse(ac.permiteAccesoPorEdad(10));
        // Asignar empleado válido
        Empleado emp = new ServicioGeneral("SG1", "Juan", "j@mail.com", "555-1", "juan", "pass");
        emp.agregarCapacitacion(dominio.empleado.Capacitacion.ATENCION_CLIENTE_GENERAL); // Agregar la capacitación requerida
        emp.agregarCapacitacion(dominio.empleado.Capacitacion.PRIMEROS_AUXILIOS); // Agregar ambas capacitaciones requeridas
        ac.asignarEmpleado(emp);
        assertTrue(ac.getEmpleadosAsignados().contains(emp));
        // Asignar empleado nulo lanza excepción
        assertThrows(DatosInvalidosException.class, () -> ac.asignarEmpleado(null));
        // Asignar empleado sin capacitación lanza excepción
        Empleado sinCap = new Cajero("C2", "NoCap", "n@mail.com", "555-2", "nocap", "pass", 2, "T2");
        assertThrows(CapacitacionInsuficienteException.class, () -> ac.asignarEmpleado(sinCap));
    }
}

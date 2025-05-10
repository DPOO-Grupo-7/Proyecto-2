package dominio.elementoparque;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import dominio.empleado.*;
import dominio.excepciones.*;
import java.util.List;

class AtraccionMecanicaTest {
    @Test
    void testAtraccionMecanica() {
        AtraccionMecanica am = new AtraccionMecanica("M1", "Rueda", "Zona C", 15, 2, NivelExclusividad.FAMILIAR, NivelRiesgo.MEDIO, 1.0, 2.0, 20, 100, List.of(), List.of(), List.of(), Capacitacion.OPERACION_ATRACCION_RIESGO_MEDIO);
        assertEquals("Rueda", am.getNombre());
        // Restricciones de altura y peso
        assertTrue(am.getRestriccionAlturaMinima() <= 1.5 && am.getRestriccionAlturaMaxima() >= 1.5);
        assertTrue(am.getRestriccionPesoMinimo() <= 50 && am.getRestriccionPesoMaximo() >= 50);
        // Asignar empleado válido
        Empleado op = new OperarioAtraccion("O1", "Luis", "l@mail.com", "555-3", "luis", "pass", true, List.of("M1"));
        op.agregarCapacitacion(Capacitacion.OPERACION_ATRACCION_RIESGO_MEDIO);
        am.asignarEmpleado(op);
        assertTrue(am.getEmpleadosAsignados().contains(op));
        // Asignar empleado nulo lanza excepción
        assertThrows(DatosInvalidosException.class, () -> am.asignarEmpleado(null));
        // Asignar empleado sin capacitación lanza excepción
        Empleado sinCap = new Cajero("C3", "NoCap", "n@mail.com", "555-4", "nocap", "pass", 3, "T3");
        assertThrows(CapacitacionInsuficienteException.class, () -> am.asignarEmpleado(sinCap));
    }
}

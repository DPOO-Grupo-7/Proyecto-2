package dominio.empleado;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CocineroTest {
    @Test
    void testCocinero() {
        Cocinero cocinero = new Cocinero("CO1", "Luis", "l@mail.com", "555-9", "luis", "pass", "Postres");
        assertEquals("Luis", cocinero.getNombre());
        assertEquals("Postres", cocinero.getEspecialidad());
        // Capacitaciones básicas
        assertTrue(cocinero.tieneCapacitacion(Capacitacion.MANEJO_CAJA));
    }
}

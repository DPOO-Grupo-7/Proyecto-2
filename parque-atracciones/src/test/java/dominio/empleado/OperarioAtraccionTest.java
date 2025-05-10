package dominio.empleado;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class OperarioAtraccionTest {
    @Test
    void testOperarioAtraccion() {
        OperarioAtraccion op = new OperarioAtraccion("O1", "Mario", "mario@mail.com", "555-11", "mario", "pass", true, List.of("A1", "A2"));
        assertEquals("Mario", op.getNombre());
        assertTrue(op.tieneCertificadoSeguridad());
        assertTrue(op.getAtraccionesHabilitadas().contains("A1"));
        assertTrue(op.getAtraccionesHabilitadas().contains("A2"));
        assertTrue(op.isDisponible());
    }
}

package dominio.empleado;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ServicioGeneralTest {
    @Test
    void testServicioGeneral() {
        ServicioGeneral sg = new ServicioGeneral("SG1", "Laura", "laura@mail.com", "555-12", "laura", "pass");
        assertEquals("Laura", sg.getNombre());
        assertTrue(sg.tieneCapacitacion(Capacitacion.ATENCION_CLIENTE_GENERAL));
    }
}

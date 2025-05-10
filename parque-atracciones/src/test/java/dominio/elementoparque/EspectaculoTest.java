package dominio.elementoparque;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.time.LocalDateTime;
import dominio.util.RangoFechaHora;

class EspectaculoTest {
    @Test
    void testEspectaculo() {
        RangoFechaHora horario = new RangoFechaHora(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        List<RangoFechaHora> horarios = List.of(horario);
        Espectaculo esp = new Espectaculo("E1", "Show Magia", "Plaza", 100, "Magia familiar", horarios, List.of());
        assertEquals("Show Magia", esp.getNombre());
        assertEquals("Plaza", esp.getUbicacion());
        assertEquals(100, esp.getCupoMaximo());
        assertEquals("Magia familiar", esp.getDescripcion());
        assertNotNull(esp.getHorarios());
    }
}

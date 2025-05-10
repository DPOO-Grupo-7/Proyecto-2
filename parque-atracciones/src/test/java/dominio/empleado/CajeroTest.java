package dominio.empleado;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import dominio.usuario.Usuario;
import dominio.usuario.Cliente;
import dominio.tiquete.TiqueteGeneral;
import dominio.tiquete.CategoriaTiquete;

import java.time.LocalDateTime;
import java.time.LocalDate;

class CajeroTest {

    @Test
    void testCajero() {
        Cajero cajero = new Cajero("C1", "Pedro", "p@mail.com", "555-6", "pedro", "pass", 1, "Tienda1");

        assertEquals("Pedro", cajero.getNombre());
        assertEquals(1, cajero.getCajaAsignada());

        cajero.setPuntoVenta("Tienda2");
        assertEquals("Tienda2", cajero.getPuntoVenta());

        // Descuento para empleados
        Usuario otro = new Cajero("C2", "Ana", "a@mail.com", "555-7", "ana", "pass", 2, "Tienda2");
        assertEquals(0.20, cajero.calcularDescuentoEmpleado(otro));

        // Descuento para no empleados (Cliente)
        Usuario noEmp = new Cliente("cliuser", "pass", "Juan", "U1", "juan@mail.com", "555-8",
                LocalDate.of(2000, 1, 1), 1.7, 70);
        assertEquals(0.0, cajero.calcularDescuentoEmpleado(noEmp));

        // ✅ Registrar venta (con nuevo constructor que requiere Usuario)
        TiqueteGeneral tg = new TiqueteGeneral("T1", LocalDateTime.now(), 100,
                "C1", "Pedro", false, CategoriaTiquete.FAMILIAR, otro); // 👈 pass Usuario

        cajero.registrarVenta(tg, otro);
    }
}

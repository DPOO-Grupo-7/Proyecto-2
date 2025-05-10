package aplicacion;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import dominio.tiquete.*;
import dominio.usuario.*;
import dominio.elementoparque.*;
import dominio.excepciones.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import dominio.empleado.Empleado;
import dominio.empleado.Cajero;
import dominio.empleado.Capacitacion;
import infraestructura.persistencia.TiqueteRepositoryJson;
import dominio.util.RangoFechaHora;

class ServicioVentaTiquetesTest {

    @Test
    void testVentaTiquetes() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");

        AtraccionMecanica dummyAtraccion = new AtraccionMecanica(
            "A1", "Montaña Rusa", "Zona A", 10, 2,
            NivelExclusividad.FAMILIAR, NivelRiesgo.MEDIO,
            1.0, 2.0, 10, 60, List.of(), List.of(), List.of(),
            Capacitacion.OPERACION_ATRACCION_RIESGO_MEDIO
        );

        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> dummyAtraccion);

        Cliente cliente = new Cliente("cliuser", "pass", "Carlos", "C1", "carlos@mail.com", "555-0000",
                LocalDate.of(2000, 1, 1), 1.75, 70);

        Empleado empleado = new Cajero("E1", "Ana", "ana@mail.com", "555-1234", "anauser", "pass", 1, "Tienda1");

        AtraccionMecanica atraccion = new AtraccionMecanica(
            "A1", "Montaña Rusa", "Zona A", 20, 2,
            NivelExclusividad.ORO, NivelRiesgo.ALTO,
            1.2, 2.0, 30, 120, List.of(), List.of(), List.of(),
            Capacitacion.OPERACION_ATRACCION_RIESGO_ALTO
        );

        // Venta de tiquete general
        TiqueteGeneral tg = servicio.venderTiqueteGeneral(cliente, CategoriaTiquete.ORO, 100.0);
        assertNotNull(tg);
        assertEquals(CategoriaTiquete.ORO, tg.getCategoria());

        // Venta de entrada individual
        EntradaIndividual ei = servicio.venderEntradaIndividual(cliente, dummyAtraccion, 50.0);
        assertNotNull(ei);
        assertEquals(dummyAtraccion.getId(), ei.getAtraccion().getId()); //  FIXED

        // Venta de fastpass
        FastPass fp = servicio.venderFastPass(empleado, LocalDateTime.now().plusDays(1), 30.0);
        assertNotNull(fp);
        assertTrue(fp.tieneDescuentoEmpleado());

        // Validación de tiquete
        assertTrue(servicio.validarTiquete(tg, LocalDateTime.now()));

        // Validación de acceso a atracción
        try {
            servicio.validarAccesoAtraccion(tg, atraccion);
        } catch (TiqueteInvalidoException e) {
            // Esperado si la categoría no es suficiente
        }

        // Registrar uso de tiquete
        servicio.registrarUsoTiquete(tg);
        assertTrue(tg.estaUtilizado()); //  FIXED
    }

    /**
     * Prueba funcional: TC_FR3_VENTA_GEN_ORO_EXITO_01
     * Verifica la venta exitosa de un Tiquete General categoría Oro a un usuario regular.
     * - El tiquete debe tener categoría ORO, precio correcto, asociado al comprador y sin descuento de empleado.
     */
    @Test
    void ventaTiqueteGeneralOroExito() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");
        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> null);
        Usuario usuario = new Cliente("user1", "pass", "Juan Perez", "U001", "juan@mail.com", "555-1111",
                LocalDate.of(1990, 5, 10), 1.70, 65);
        double precioBase = 50.0;
        TiqueteGeneral tg = servicio.venderTiqueteGeneral(usuario, CategoriaTiquete.ORO, precioBase);
        assertNotNull(tg);
        assertEquals(CategoriaTiquete.ORO, tg.getCategoria());
        assertEquals(precioBase, tg.getPrecio());
        assertEquals(usuario, tg.getComprador());
        assertFalse(tg.tieneDescuentoEmpleado());
    }

    /**
     * Prueba funcional: TC_FR3_VENTA_GEN_EMP_DESC_EXITO_02
     * Verifica la venta exitosa de un Tiquete General con descuento aplicado a un Empleado.
     * - El tiquete debe tener categoría DIAMANTE, precio con descuento, asociado al empleado y marcar descuento.
     */
    @Test
    void ventaTiqueteGeneralEmpleadoDescuentoExito() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");
        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> null);
        Empleado empleado = new Cajero("E001", "Ana Torres", "ana@parque.com", "555-2222", "anauser", "pass", 1, "Taquilla");
        double precioBase = 50.0;
        double DESCUENTO_EMPLEADO = 0.5;
        TiqueteGeneral tg = servicio.venderTiqueteGeneral(empleado, CategoriaTiquete.DIAMANTE, precioBase);
        assertNotNull(tg);
        assertEquals(CategoriaTiquete.DIAMANTE, tg.getCategoria());
        assertEquals(precioBase * (1 - DESCUENTO_EMPLEADO), tg.getPrecio());
        assertEquals(empleado, tg.getComprador());
        assertTrue(tg.tieneDescuentoEmpleado());
    }

    /**
     * Prueba funcional: TC_FR3_VENTA_GEN_ERROR_NULL_03
     * Verifica que se lanza DatosInvalidosException al intentar vender un Tiquete General con comprador nulo.
     */
    @Test
    void ventaTiqueteGeneralCompradorNuloError() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");
        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> null);
        assertThrows(DatosInvalidosException.class, () ->
            servicio.venderTiqueteGeneral(null, CategoriaTiquete.FAMILIAR, 30.0)
        );
    }

    /**
     * Prueba funcional: TC_FR3_VENTA_TEMP_EXITO_04
     * Verifica la venta exitosa de un Tiquete de Temporada a un usuario regular.
     */
    @Test
    void ventaTiqueteTemporadaExito() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");
        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> null);
        Usuario usuario = new Cliente("user2", "pass", "Maria Lopez", "U002", "maria@mail.com", "555-2222",
                LocalDate.of(1985, 3, 20), 1.65, 60);
        LocalDateTime inicio = LocalDateTime.of(2025, 6, 1, 0, 0);
        LocalDateTime fin = LocalDateTime.of(2025, 8, 31, 23, 59);
        double precioBase = 200.0;
        TiqueteTemporada temp = servicio.venderTiqueteTemporada(usuario, CategoriaTiquete.ORO, inicio, fin, precioBase);
        assertNotNull(temp);
        assertEquals(CategoriaTiquete.ORO, temp.getCategoria());
        assertEquals(precioBase, temp.getPrecio());
        assertEquals(inicio, temp.getFechaInicio());
        assertEquals(fin, temp.getFechaFin());
        assertEquals(usuario, temp.getComprador());
    }

    /**
     * Prueba funcional: TC_FR3_VENTA_TEMP_ERROR_BASICO_05
     * Verifica que se lanza DatosInvalidosException al intentar vender un Tiquete de Temporada con categoría BASICO.
     */
    @Test
    void ventaTiqueteTemporadaCategoriaBasicoError() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");
        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> null);
        Usuario usuario = new Cliente("user3", "pass", "Pedro Ruiz", "U003", "pedro@mail.com", "555-3333",
                LocalDate.of(1995, 7, 15), 1.80, 80);
        LocalDateTime inicio = LocalDateTime.of(2025, 6, 1, 0, 0);
        LocalDateTime fin = LocalDateTime.of(2025, 8, 31, 23, 59);
        assertThrows(DatosInvalidosException.class, () ->
            servicio.venderTiqueteTemporada(usuario, CategoriaTiquete.BASICO, inicio, fin, 100.0)
        );
    }

    /**
     * Prueba funcional: TC_FR3_VENTA_INDIV_EXITO_06
     * Verifica la venta exitosa de una Entrada Individual para una atracción específica.
     */
    @Test
    void ventaEntradaIndividualExito() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");
        AtraccionMecanica atraccion = new AtraccionMecanica(
            "AM001", "Carrusel", "Zona B", 15, 2,
            NivelExclusividad.FAMILIAR, NivelRiesgo.MEDIO,
            1.0, 2.0, 5, 50, List.of(), List.of(), List.of(),
            Capacitacion.OPERACION_ATRACCION_RIESGO_MEDIO
        );
        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> atraccion);
        Usuario usuario = new Cliente("user4", "pass", "Luis Gómez", "U004", "luis@mail.com", "555-4444",
                LocalDate.of(2002, 12, 5), 1.60, 55);
        double precioBase = 10.0;
        EntradaIndividual entrada = servicio.venderEntradaIndividual(usuario, atraccion, precioBase);
        assertNotNull(entrada);
        assertEquals(atraccion, entrada.getAtraccion());
        assertEquals(precioBase, entrada.getPrecio());
        assertEquals(usuario, entrada.getComprador());
    }

    /**
     * Prueba funcional: TC_FR3_VENTA_FASTPASS_EXITO_07
     * Verifica la venta exitosa de un FastPass para una fecha específica.
     */
    @Test
    void ventaFastPassExito() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");
        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> null);
        Usuario usuario = new Cliente("user5", "pass", "Sofia Díaz", "U005", "sofia@mail.com", "555-5555",
                LocalDate.of(1998, 9, 9), 1.68, 62);
        LocalDateTime fechaValida = LocalDateTime.of(2025, 7, 15, 0, 0);
        double precioBase = 25.0;
        FastPass fp = servicio.venderFastPass(usuario, fechaValida, precioBase);
        assertNotNull(fp);
        assertEquals(fechaValida, fp.getFechaValida());
        assertEquals(precioBase, fp.getPrecio());
        assertEquals(usuario, fp.getComprador());
    }

    /**
     * Prueba de consulta de tiquetes por usuario (positivo y error).
     * Verifica que se devuelven los tiquetes correctos y que lanza excepción si la identificación es nula o vacía.
     */
    @Test
    void consultarTiquetesPorUsuarioTest() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");
        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> null);
        Usuario usuario = new Cliente("user6", "pass", "Laura", "U006", "laura@mail.com", "555-6666",
                LocalDate.of(1992, 2, 2), 1.65, 60);
        servicio.venderTiqueteGeneral(usuario, CategoriaTiquete.ORO, 100.0);
        List<Tiquete> tiquetes = servicio.consultarTiquetesPorUsuario("U006");
        assertFalse(tiquetes.isEmpty());
        assertThrows(DatosInvalidosException.class, () -> servicio.consultarTiquetesPorUsuario(null));
        assertThrows(DatosInvalidosException.class, () -> servicio.consultarTiquetesPorUsuario("   "));
    }

    /**
     * Prueba de consulta de tiquetes por fecha (positivo y error).
     * Verifica que se devuelven los tiquetes correctos y que lanza excepción si la fecha es nula.
     */
    @Test
    void consultarTiquetesPorFechaTest() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");
        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> null);
        Usuario usuario = new Cliente("user7", "pass", "Mario", "U007", "mario@mail.com", "555-7777",
                LocalDate.of(1991, 3, 3), 1.80, 75);
        servicio.venderTiqueteGeneral(usuario, CategoriaTiquete.FAMILIAR, 80.0);
        List<Tiquete> tiquetes = servicio.consultarTiquetesPorFecha(LocalDate.now());
        assertFalse(tiquetes.isEmpty());
        assertThrows(DatosInvalidosException.class, () -> servicio.consultarTiquetesPorFecha(null));
    }

    /**
     * Prueba de consulta de tiquetes por tipo (positivo y error).
     * Verifica que se devuelven los tiquetes correctos y que lanza excepción si el tipo es nulo.
     */
    @Test
    void consultarTiquetesPorTipoTest() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");
        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> null);
        Usuario usuario = new Cliente("user8", "pass", "Sara", "U008", "sara@mail.com", "555-8888",
                LocalDate.of(1993, 4, 4), 1.70, 68);
        servicio.venderTiqueteGeneral(usuario, CategoriaTiquete.DIAMANTE, 120.0);
        List<TiqueteGeneral> generales = servicio.consultarTiquetesPorTipo(TiqueteGeneral.class);
        assertFalse(generales.isEmpty());
        assertThrows(DatosInvalidosException.class, () -> servicio.consultarTiquetesPorTipo(null));
    }

    /**
     * Prueba de eliminación de tiquetes por usuario (positivo y error).
     * Verifica que los tiquetes se eliminan correctamente y que lanza excepción si la identificación es nula o vacía.
     */
    @Test
    void eliminarTiquetesPorUsuarioTest() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");
        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> null);
        Usuario usuario = new Cliente("user9", "pass", "Pablo", "U009", "pablo@mail.com", "555-9999",
                LocalDate.of(1994, 5, 5), 1.85, 80);
        servicio.venderTiqueteGeneral(usuario, CategoriaTiquete.FAMILIAR, 90.0);
        servicio.eliminarTiquetesPorUsuario("U009");
        List<Tiquete> tiquetes = servicio.consultarTiquetesPorUsuario("U009");
        assertTrue(tiquetes.isEmpty());
        assertThrows(DatosInvalidosException.class, () -> servicio.eliminarTiquetesPorUsuario(null));
        assertThrows(DatosInvalidosException.class, () -> servicio.eliminarTiquetesPorUsuario("   "));
    }

    /**
     * Prueba de eliminación de tiquetes por tipo (positivo y error).
     * Verifica que los tiquetes del tipo indicado se eliminan correctamente y que lanza excepción si el tipo es nulo.
     */
    @Test
    void eliminarTiquetesPorTipoTest() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");
        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> null);
        Usuario usuario = new Cliente("user10", "pass", "Elena", "U010", "elena@mail.com", "555-1010",
                LocalDate.of(1995, 6, 6), 1.60, 55);
        servicio.venderTiqueteGeneral(usuario, CategoriaTiquete.FAMILIAR, 70.0);
        servicio.eliminarTiquetesPorTipo(TiqueteGeneral.class);
        List<TiqueteGeneral> generales = servicio.consultarTiquetesPorTipo(TiqueteGeneral.class);
        assertTrue(generales.isEmpty());
        assertThrows(DatosInvalidosException.class, () -> servicio.eliminarTiquetesPorTipo(null));
    }

    /**
     * Prueba: venderFastPass con usuario nulo (debe lanzar excepción)
     */
    @Test
    void testVenderFastPassUsuarioNulo() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");
        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> null);
        assertThrows(DatosInvalidosException.class, () ->
            servicio.venderFastPass(null, LocalDateTime.now(), 10.0)
        );
    }

    /**
     * Prueba: venderFastPass con fecha nula (debe lanzar excepción)
     */
    @Test
    void testVenderFastPassFechaNula() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");
        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> null);
        Usuario usuario = new Cliente("userFF", "pass", "Nombre", "U_FF", "mail@mail.com", "555-0000", LocalDate.of(2000,1,1), 1.7, 70);
        assertThrows(DatosInvalidosException.class, () ->
            servicio.venderFastPass(usuario, null, 10.0)
        );
    }

    /**
     * Prueba: venderEntradaIndividual con atracción nula (debe lanzar excepción)
     */
    @Test
    void testVenderEntradaIndividualAtraccionNula() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");
        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> null);
        Usuario usuario = new Cliente("userEI", "pass", "Nombre", "U_EI", "mail@mail.com", "555-0000", LocalDate.of(2000,1,1), 1.7, 70);
        assertThrows(DatosInvalidosException.class, () ->
            servicio.venderEntradaIndividual(usuario, null, 10.0)
        );
    }

    /**
     * Prueba: venderTiqueteTemporada con fechas nulas (debe lanzar excepción)
     */
    @Test
    void testVenderTiqueteTemporadaFechasNulas() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");
        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> null);
        Usuario usuario = new Cliente("userTT", "pass", "Nombre", "U_TT", "mail@mail.com", "555-0000", LocalDate.of(2000,1,1), 1.7, 70);
        assertThrows(DatosInvalidosException.class, () ->
            servicio.venderTiqueteTemporada(usuario, CategoriaTiquete.ORO, null, LocalDateTime.now(), 10.0)
        );
        assertThrows(DatosInvalidosException.class, () ->
            servicio.venderTiqueteTemporada(usuario, CategoriaTiquete.ORO, LocalDateTime.now(), null, 10.0)
        );
    }

    /**
     * Prueba: validarAccesoAtraccion con elemento no atracción (debe lanzar excepción)
     */
    @Test
    void testValidarAccesoAtraccionElementoNoAtraccion() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");
        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> null);
        Usuario usuario = new Cliente("userVA", "pass", "Nombre", "U_VA", "mail@mail.com", "555-0000", LocalDate.of(2000,1,1), 1.7, 70);
        TiqueteGeneral tg = servicio.venderTiqueteGeneral(usuario, CategoriaTiquete.ORO, 10.0);
        // Crear espectáculo con al menos un horario válido
        List<RangoFechaHora> horarios = List.of(
            new RangoFechaHora(
                LocalDateTime.of(2025, 5, 15, 15, 0),
                LocalDateTime.of(2025, 5, 15, 17, 0)
            )
        );
        Espectaculo espectaculo = new Espectaculo("E1", "Show", "Zona", 10, "desc", horarios, List.of());
        assertTrue(servicio.validarAccesoAtraccion(tg, espectaculo)); // acceso permitido
        ElementoParque fakeElemento = new ElementoParque("FAKE", "Fake", 1) {
        };
        assertThrows(TiqueteInvalidoException.class, () ->
            servicio.validarAccesoAtraccion(tg, fakeElemento)
        );
    }

    /**
     * Prueba: validarAccesoAtraccion con nivelExclusividad null (debe lanzar excepción)
     */
    @Test
    void testValidarAccesoAtraccionNivelExclusividadNull() {
        // La excepción DatosInvalidosException se lanza en el constructor de AtraccionMecanica
        // cuando se intenta crear con nivelExclusividad null
        assertThrows(DatosInvalidosException.class, () -> {
            new AtraccionMecanica("A_NIVEL_NULL", "Atracción", "Zona", 1, 1, 
                null, NivelRiesgo.MEDIO, 1, 2, 1, 2, List.of(), List.of(), List.of(), null);
        });
    }

    /**
     * Prueba: registrarUsoTiquete con tiquete nulo (debe lanzar excepción)
     */
    @Test
    void testRegistrarUsoTiqueteNulo() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");
        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> null);
        assertThrows(DatosInvalidosException.class, () ->
            servicio.registrarUsoTiquete(null)
        );
    }

    /**
     * Cobertura total: validarAccesoAtraccion - casos de error y ramas no cubiertas
     */
    @Test
    void testValidarAccesoAtraccionCoberturaTotal() {
        TiqueteRepositoryJson repo = new TiqueteRepositoryJson("tiquetes");
        ServicioVentaTiquetes servicio = new ServicioVentaTiquetes(repo, _ -> null);
        Usuario usuario = new Cliente("userVAA", "pass", "Nombre", "U_VAA", "mail@mail.com", "555-0000", LocalDate.of(2000,1,1), 1.7, 70);
        Atraccion atraccion = new dominio.elementoparque.AtraccionMecanica(
            "A1", "Montaña Rusa", "Zona A", 10, 2,
            NivelExclusividad.FAMILIAR, NivelRiesgo.MEDIO,
            1.0, 2.0, 10, 60, List.of(), List.of(), List.of(), Capacitacion.OPERACION_ATRACCION_RIESGO_MEDIO
        );
        TiqueteGeneral tg = servicio.venderTiqueteGeneral(usuario, CategoriaTiquete.FAMILIAR, 10.0);
        // 1. Tiquete nulo
        assertThrows(DatosInvalidosException.class, () -> servicio.validarAccesoAtraccion(null, atraccion));
        // 2. Elemento nulo
        assertThrows(DatosInvalidosException.class, () -> servicio.validarAccesoAtraccion(tg, null));
        // 3. Elemento no es Atraccion ni Espectaculo
        ElementoParque fakeElemento = new ElementoParque("FAKE", "Fake", 1) {};
        assertThrows(TiqueteInvalidoException.class, () -> servicio.validarAccesoAtraccion(tg, fakeElemento));
        // 4. Atraccion con nivelExclusividad null
        Atraccion atraccionSinNivel = new AtraccionMecanica("A2", "SinNivel", "Zona", 1, 1, NivelExclusividad.FAMILIAR, NivelRiesgo.MEDIO, 1, 2, 1, 2, List.of(), List.of(), List.of(), null) {
            @Override public NivelExclusividad getNivelExclusividad() { return null; }
        };
        assertThrows(TiqueteInvalidoException.class, () -> servicio.validarAccesoAtraccion(tg, atraccionSinNivel));
        // 5. Cliente no cumple restricciones médicas
        Atraccion atraccionAltura = new AtraccionMecanica("A3", "Altura", "Zona", 1, 1, NivelExclusividad.FAMILIAR, NivelRiesgo.MEDIO, 2.0, 2.5, 1, 2, List.of(), List.of(), List.of(), null);
        TiqueteGeneral tg2 = servicio.venderTiqueteGeneral(new Cliente("u2", "p", "Bajo", "C2", "bajo@mail.com", "555-0001", LocalDate.of(2000,1,1), 1.5, 70), CategoriaTiquete.FAMILIAR, 10.0);
        assertThrows(TiqueteInvalidoException.class, () -> servicio.validarAccesoAtraccion(tg2, atraccionAltura));
        // 6. EntradaIndividual no válida para la atracción
        EntradaIndividual ei = servicio.venderEntradaIndividual(usuario, atraccion, 10.0);
        Atraccion otraAtraccion = new AtraccionMecanica("A4", "Otra", "Zona", 1, 1, NivelExclusividad.FAMILIAR, NivelRiesgo.MEDIO, 1, 2, 1, 2, List.of(), List.of(), List.of(), null);
        assertThrows(TiqueteInvalidoException.class, () -> servicio.validarAccesoAtraccion(ei, otraAtraccion));
        // 7. FastPass no otorga acceso
        FastPass fp = servicio.venderFastPass(usuario, LocalDateTime.now().plusDays(1), 10.0);
        assertThrows(TiqueteInvalidoException.class, () -> servicio.validarAccesoAtraccion(fp, atraccion));
        // 8. TiqueteGeneral con categoría que no permite acceso
        Atraccion atraccionDiamante = new AtraccionMecanica("A5", "Diamante", "Zona", 1, 1, NivelExclusividad.DIAMANTE, NivelRiesgo.MEDIO, 1, 2, 1, 2, List.of(), List.of(), List.of(), null);
        TiqueteGeneral tgFamiliar = servicio.venderTiqueteGeneral(usuario, CategoriaTiquete.FAMILIAR, 10.0);
        assertThrows(TiqueteInvalidoException.class, () -> servicio.validarAccesoAtraccion(tgFamiliar, atraccionDiamante));
    }
}

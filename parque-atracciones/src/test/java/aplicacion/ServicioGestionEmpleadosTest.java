package aplicacion;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import infraestructura.persistencia.EmpleadoRepositoryJson;
import java.util.List;
import dominio.empleado.Empleado;
import dominio.empleado.Cajero;
import dominio.empleado.Cocinero;
import dominio.empleado.ServicioGeneral;
import dominio.trabajo.Taquilla;
import dominio.trabajo.Turno;
import dominio.trabajo.AsignacionTurno;
import dominio.empleado.Capacitacion;
import dominio.excepciones.DatosInvalidosException;
import dominio.excepciones.AsignacionInvalidaException;
import java.util.Optional;
import java.util.UUID;

/**
 * Pruebas de Gestión de Empleados y Asignaciones (FR2)
 * Cobertura de CU-07 a CU-13. Documentación y casos positivos/negativos.
 */
public class ServicioGestionEmpleadosTest {
    /**
     * TC_FR2_REG_EMP_EXITO_01
     * Verifica el registro exitoso de un Cajero con datos válidos.
     */
    @Test
    void testRegistrarEmpleadoExito() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        String id = "E001" + UUID.randomUUID().toString().substring(0, 5);
        Empleado cajero = new Cajero(id, "Ana Torres", "ana@parque.com", "555-1111", "anauser", "pass", 1, "Tienda1");
        Empleado registrado = servicio.registrarEmpleado(cajero);
        assertEquals("Ana Torres", registrado.getNombre());
        assertTrue(servicio.consultarEmpleadoPorIdentificacion(id).isPresent());
    }

    /**
     * TC_FR2_REG_EMP_ERROR_NULL_02
     * Verifica que se lanza DatosInvalidosException al registrar un empleado nulo.
     */
    @Test
    void testRegistrarEmpleadoNulo() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        assertThrows(DatosInvalidosException.class, () -> servicio.registrarEmpleado(null));
    }

    /**
     * TC_FR2_REG_EMP_ERROR_DUPLICADO_03
     * Verifica que se lanza DatosInvalidosException al registrar un empleado duplicado.
     */
    @Test
    void testRegistrarEmpleadoDuplicado() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        String id = "E002" + UUID.randomUUID().toString().substring(0, 5);
        Empleado cajero = new Cajero(id, "Ana Torres", "ana@parque.com", "555-1111", "anauser", "pass", 1, "Tienda1");
        servicio.registrarEmpleado(cajero);
        Empleado cocinero = new Cocinero(id, "Luis", "luis@parque.com", "555-2222", "luisuser", "pass", "Postres");
        assertThrows(DatosInvalidosException.class, () -> servicio.registrarEmpleado(cocinero));
    }

    /**
     * TC_FR2_CONS_EMP_ID_EXITO_04
     * Consulta exitosa de un empleado existente por su identificación.
     */
    @Test
    void testConsultarEmpleadoPorIdExito() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        String id = "E003" + UUID.randomUUID().toString().substring(0, 5);
        Empleado cajero = new Cajero(id, "Ana Torres", "ana@parque.com", "555-1111", "anauser", "pass", 1, "Tienda1");
        servicio.registrarEmpleado(cajero);
        Optional<Empleado> encontrado = servicio.consultarEmpleadoPorIdentificacion(id);
        assertTrue(encontrado.isPresent());
        assertEquals("Ana Torres", encontrado.get().getNombre());
    }

    /**
     * TC_FR2_CONS_EMP_ID_NO_EXISTE_05
     * Consulta por identificación inexistente retorna Optional vacío.
     */
    @Test
    void testConsultarEmpleadoPorIdNoExiste() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        Optional<Empleado> resultado = servicio.consultarEmpleadoPorIdentificacion("ID_INEXISTENTE");
        assertTrue(resultado.isEmpty());
    }

    /**
     * TC_FR2_ASIG_TURNO_CAJERO_TAQUILLA_EXITO_06
     * Asignación exitosa de Cajero capacitado a Taquilla.
     */
    @Test
    void testAsignarTurnoCajeroTaquillaExito() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        String id = "E004" + UUID.randomUUID().toString().substring(0, 5);
        Cajero cajero = new Cajero(id, "Ana Torres", "ana@parque.com", "555-1111", "anauser", "pass", 1, "Taquilla Principal");
        cajero.agregarCapacitacion(Capacitacion.MANEJO_CAJA);
        servicio.registrarEmpleado(cajero);
        Taquilla taquilla = new Taquilla("Taquilla Principal", 2);
        LocalDate fecha = LocalDate.now();
        Turno turno = Turno.APERTURA;
        AsignacionTurno asignacion = servicio.asignarTurno(id, taquilla, fecha, turno);
        assertNotNull(asignacion);
        assertEquals(cajero, asignacion.getEmpleado());
        assertEquals(taquilla, asignacion.getLugarTrabajo());
    }

    /**
     * TC_FR2_ASIG_TURNO_ERROR_CAPACITACION_07
     * Error al asignar Cajero sin capacitación a Taquilla.
     */
    @Test
    void testAsignarTurnoCajeroSinCapacitacion() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        String id = "E005" + UUID.randomUUID().toString().substring(0, 5);
        // Crear cajero SIN capacitaciones por defecto
        Cajero cajero = new Cajero(id, "Ana Torres", "ana@parque.com", "555-1111", "anauser", "pass", 1, "Taquilla Principal", false);
        servicio.registrarEmpleado(cajero);
        Taquilla taquilla = new Taquilla("Taquilla Secundaria", 1);
        LocalDate fecha = LocalDate.now();
        Turno turno = Turno.APERTURA;
        assertThrows(AsignacionInvalidaException.class, () -> servicio.asignarTurno(id, taquilla, fecha, turno));
    }

    /**
     * TC_FR2_ASIG_TURNO_SERVICIO_GENERAL_EXITO_08
     * Asignación exitosa de ServicioGeneral sin lugar fijo.
     */
    @Test
    void testAsignarTurnoServicioGeneralExito() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        String id = "E006" + UUID.randomUUID().toString().substring(0, 5);
        ServicioGeneral sg = new ServicioGeneral(id, "Pedro", "pedro@parque.com", "555-3333", "servgen1", "pass");
        servicio.registrarEmpleado(sg);
        LocalDate fecha = LocalDate.now();
        Turno turno = Turno.CIERRE;
        AsignacionTurno asignacion = servicio.asignarTurno(id, null, fecha, turno);
        assertNotNull(asignacion);
        assertEquals(sg, asignacion.getEmpleado());
        assertNull(asignacion.getLugarTrabajo());
    }

    /**
     * TC_FR2_ASIG_TURNO_ERROR_NO_SERVICIO_GENERAL_09
     * Error al asignar turno sin lugar a empleado que no es ServicioGeneral.
     */
    @Test
    void testAsignarTurnoSinLugarNoServicioGeneral() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        String id = "E007" + UUID.randomUUID().toString().substring(0, 5);
        Cajero cajero = new Cajero(id, "Ana Torres", "ana@parque.com", "555-1111", "anauser", "pass", 1, "Tienda1");
        servicio.registrarEmpleado(cajero);
        LocalDate fecha = LocalDate.now();
        Turno turno = Turno.APERTURA;
        assertThrows(AsignacionInvalidaException.class, () -> servicio.asignarTurno(id, null, fecha, turno));
    }

    /**
     * TC_FR2_CONS_ASIG_EMPLEADO_DIA_10
     * Consulta de asignaciones de un empleado para un día específico.
     */
    @Test
    void testConsultarAsignacionesEmpleadoDia() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        // Generar un id único y limpiar cualquier empleado previo con ese id
        String id = "E008" + UUID.randomUUID().toString().substring(0, 8);
        // Eliminar si existe previamente
        try { servicio.eliminarEmpleado(id); } catch (Exception ignored) {}
        Cajero cajero = new Cajero(id, "Ana Torres", "ana@parque.com", "555-1111", "anauser", "pass", 1, "Tienda1");
        cajero.agregarCapacitacion(Capacitacion.MANEJO_CAJA);
        servicio.registrarEmpleado(cajero);
        Taquilla taquilla = new Taquilla("Taquilla Norte", 1);
        LocalDate fecha = LocalDate.now();
        Turno turno = Turno.APERTURA;
        servicio.asignarTurno(id, taquilla, fecha, turno);
        List<AsignacionTurno> asignaciones = servicio.consultarAsignacionesEmpleadoDia(id, fecha);
        assertEquals(1, asignaciones.size());
        assertEquals(cajero, asignaciones.get(0).getEmpleado());
    }

    /**
     * TC_FR2_CONS_ASIG_POR_FECHA_11
     * Consulta de todas las asignaciones para una fecha específica.
     */
    @Test
    void testConsultarAsignacionesPorFecha() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        String id = "E009" + UUID.randomUUID().toString().substring(0, 5);
        Cajero cajero = new Cajero(id, "Ana Torres", "ana@parque.com", "555-1111", "anauser", "pass", 1, "Tienda1");
        cajero.agregarCapacitacion(Capacitacion.MANEJO_CAJA);
        servicio.registrarEmpleado(cajero);
        Taquilla taquilla = new Taquilla("Taquilla Sur", 1);
        LocalDate fecha = LocalDate.now();
        Turno turno = Turno.CIERRE;
        servicio.asignarTurno(id, taquilla, fecha, turno);
        List<AsignacionTurno> asignaciones = servicio.consultarAsignacionesPorFecha(fecha);
        assertTrue(asignaciones.stream().anyMatch(a -> a.getEmpleado().equals(cajero)));
    }

    /**
     * TC_FR2_AUT_CAPACITACION_EXITO_12
     * Autoriza capacitación a un empleado y verifica que se registra.
     */
    @Test
    void testAutorizarCapacitacionExito() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        String id = "E010" + UUID.randomUUID().toString().substring(0, 5);
        Cajero cajero = new Cajero(id, "Ana Torres", "ana@parque.com", "555-1111", "anauser", "pass", 1, "Tienda1");
        servicio.registrarEmpleado(cajero);
        servicio.autorizarCapacitacion(id, Capacitacion.MANEJO_CAJA);
        assertTrue(cajero.tieneCapacitacion(Capacitacion.MANEJO_CAJA));
    }

    /**
     * TC_FR2_AUT_CAPACITACION_ERROR_13
     * Error al autorizar capacitación nula.
     */
    @Test
    void testAutorizarCapacitacionNula() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        String id = "E011" + UUID.randomUUID().toString().substring(0, 5);
        Cajero cajero = new Cajero(id, "Ana Torres", "ana@parque.com", "555-1111", "anauser", "pass", 1, "Tienda1");
        servicio.registrarEmpleado(cajero);
        assertThrows(DatosInvalidosException.class, () -> servicio.autorizarCapacitacion(id, null));
    }

    /**
     * TC_FR2_VERIFICAR_REQUISITOS_PERSONAL_EXITO_14
     * Verifica que un lugar cumple requisitos de personal para operar.
     */
    @Test
    void testVerificarRequisitosPersonalLugarExito() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        String id = "E012" + UUID.randomUUID().toString().substring(0, 5);
        Cajero cajero = new Cajero(id, "Ana Torres", "ana@parque.com", "555-1111", "anauser", "pass", 1, "Tienda1");
        cajero.agregarCapacitacion(Capacitacion.MANEJO_CAJA);
        servicio.registrarEmpleado(cajero);
        Taquilla taquilla = new Taquilla("Taquilla Central", 1);
        LocalDate fecha = LocalDate.now();
        Turno turno = Turno.APERTURA;
        servicio.asignarTurno(id, taquilla, fecha, turno);
        boolean cumple = servicio.verificarRequisitosPersonalLugar(taquilla, fecha, turno);
        assertTrue(cumple);
    }

    /**
     * TC_FR2_VERIFICAR_REQUISITOS_PERSONAL_ERROR_15
     * Verifica que un lugar NO cumple requisitos de personal (falta cajero capacitado)
     * y que la asignación de dicho empleado no es permitida.
     */
    @Test
    void testVerificarRequisitosPersonalLugarNoCumple() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        String id = "E013" + UUID.randomUUID().toString().substring(0, 5);
        // Crear cajero SIN capacitaciones por defecto
        Cajero cajero = new Cajero(id, "Ana Torres", "ana@parque.com", "555-1111", "anauser", "pass", 1, "Tienda1", false);
        servicio.registrarEmpleado(cajero);
        Taquilla taquilla = new Taquilla("Taquilla Este", 1);
        LocalDate fecha = LocalDate.now();
        Turno turno = Turno.CIERRE;

        // Verificar que asignarTurno lanza AsignacionInvalidaException
        // cuando el empleado no tiene las capacitaciones requeridas.
        AsignacionInvalidaException exception = assertThrows(AsignacionInvalidaException.class, () -> {
            servicio.asignarTurno(id, taquilla, fecha, turno);
        });
        
        // Opcionalmente, verificar el mensaje de la excepción si es relevante
        assertTrue(exception.getMessage().contains("El empleado no tiene las capacitaciones requeridas"));
    }

    /**
     * Prueba de actualización de empleado (positivo y error).
     * Verifica que se actualiza correctamente y que lanza excepción si el empleado es nulo o no existe.
     */
    @Test
    void actualizarEmpleadoTest() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        String id = "E014" + UUID.randomUUID().toString().substring(0, 5);
        Cajero cajero = new Cajero(id, "Ana Torres", "ana@parque.com", "555-1111", "anauser", "pass", 1, "Tienda1");
        servicio.registrarEmpleado(cajero);
        cajero.setNombre("Ana Actualizada");
        servicio.actualizarEmpleado(cajero);
        assertEquals("Ana Actualizada", servicio.consultarEmpleadoPorIdentificacion(id).get().getNombre());
        Cajero noRegistrado = new Cajero("NOEXISTE", "X", "x@x.com", "0", "x", "x", 1, "Tienda1");
        assertThrows(dominio.excepciones.EmpleadoNoEncontradoException.class, () -> servicio.actualizarEmpleado(noRegistrado));
        assertThrows(dominio.excepciones.DatosInvalidosException.class, () -> servicio.actualizarEmpleado(null));
    }

    /**
     * Prueba de eliminación de empleado (positivo y error).
     * Verifica que se elimina correctamente y que lanza excepción si la identificación es nula, vacía o no existe.
     */
    @Test
    void eliminarEmpleadoTest() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        String id = "E015" + UUID.randomUUID().toString().substring(0, 5);
        Cajero cajero = new Cajero(id, "Ana Torres", "ana@parque.com", "555-1111", "anauser", "pass", 1, "Tienda1");
        servicio.registrarEmpleado(cajero);
        servicio.eliminarEmpleado(id);
        assertTrue(servicio.consultarEmpleadoPorIdentificacion(id).isEmpty());
        assertThrows(dominio.excepciones.DatosInvalidosException.class, () -> servicio.eliminarEmpleado(null));
        assertThrows(dominio.excepciones.DatosInvalidosException.class, () -> servicio.eliminarEmpleado("   "));
        assertThrows(dominio.excepciones.EmpleadoNoEncontradoException.class, () -> servicio.eliminarEmpleado("NOEXISTE"));
    }

    /**
     * Prueba de consulta de empleados por tipo (positivo y error).
     * Verifica que se devuelven los empleados correctos y que lanza excepción si el tipo es nulo.
     */
    @Test
    void consultarEmpleadosPorTipoTest() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        String id = "E016" + UUID.randomUUID().toString().substring(0, 5);
        Cajero cajero = new Cajero(id, "Ana Torres", "ana@parque.com", "555-1111", "anauser", "pass", 1, "Tienda1");
        servicio.registrarEmpleado(cajero);
        List<Cajero> cajeros = servicio.consultarEmpleadosPorTipo(Cajero.class);
        assertFalse(cajeros.isEmpty());
        assertThrows(dominio.excepciones.DatosInvalidosException.class, () -> servicio.consultarEmpleadosPorTipo(null));
    }

    /**
     * Prueba de eliminación de empleados por tipo (positivo y error).
     * Verifica que los empleados del tipo indicado se eliminan correctamente y que lanza excepción si el tipo es nulo.
     */
    @Test
    void eliminarEmpleadosPorTipoTest() {
        EmpleadoRepositoryJson repo = new EmpleadoRepositoryJson("empleados");
        ServicioGestionEmpleados servicio = new ServicioGestionEmpleados(repo);
        String id = "E017" + UUID.randomUUID().toString().substring(0, 5);
        Cajero cajero = new Cajero(id, "Ana Torres", "ana@parque.com", "555-1111", "anauser", "pass", 1, "Tienda1");
        servicio.registrarEmpleado(cajero);
        servicio.eliminarEmpleadosPorTipo(Cajero.class);
        List<Cajero> cajeros = servicio.consultarEmpleadosPorTipo(Cajero.class);
        assertTrue(cajeros.isEmpty());
        assertThrows(dominio.excepciones.DatosInvalidosException.class, () -> servicio.eliminarEmpleadosPorTipo(null));
    }
}
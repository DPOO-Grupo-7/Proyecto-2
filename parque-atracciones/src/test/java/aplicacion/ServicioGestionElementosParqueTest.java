package aplicacion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import dominio.elementoparque.*;
import dominio.util.CondicionClimatica;
import dominio.util.RangoFechaHora;
import dominio.empleado.Capacitacion;
import dominio.excepciones.AtraccionNoEncontradaException;
import dominio.excepciones.DatosInvalidosException;

import java.time.LocalDateTime;
import java.util.*;

class DummyElementoParqueRepository implements ElementoParqueRepository {
    private final Map<String, Atraccion> atracciones = new HashMap<>();
    private final Map<String, Espectaculo> espectaculos = new HashMap<>();
    @Override public Atraccion save(Atraccion atraccion) { atracciones.put(atraccion.getId(), atraccion); return atraccion; }
    @Override public Espectaculo save(Espectaculo espectaculo) { espectaculos.put(espectaculo.getId(), espectaculo); return espectaculo; }
    @Override public Optional<Atraccion> findAtraccionById(String id) { return Optional.ofNullable(atracciones.get(id)); }
    @Override public Optional<Espectaculo> findEspectaculoById(String id) { return Optional.ofNullable(espectaculos.get(id)); }
    @Override public Optional<ElementoParque> findById(String id) { return Optional.ofNullable(atracciones.containsKey(id) ? atracciones.get(id) : espectaculos.get(id)); }
    @Override public Optional<Atraccion> findAtraccionByNombre(String nombre) { return atracciones.values().stream().filter(a -> a.getNombre().equals(nombre)).findFirst(); }
    @Override public Optional<Espectaculo> findEspectaculoByNombre(String nombre) { return espectaculos.values().stream().filter(e -> e.getNombre().equals(nombre)).findFirst(); }
    @Override public Optional<ElementoParque> findByNombre(String nombre) { return findAtraccionByNombre(nombre).map(a -> (ElementoParque)a).or(() -> findEspectaculoByNombre(nombre).map(e -> (ElementoParque)e)); }
    @Override public List<Atraccion> findAllAtracciones() { return new ArrayList<>(atracciones.values()); }
    @Override public List<ElementoParque> findAll() { List<ElementoParque> l = new ArrayList<>(); l.addAll(atracciones.values()); l.addAll(espectaculos.values()); return l; }
    @Override public void deleteById(String id) { atracciones.remove(id); espectaculos.remove(id); }
    @Override public List<AtraccionMecanica> findAllMecanicas() { return atracciones.values().stream().filter(a -> a instanceof AtraccionMecanica).map(a -> (AtraccionMecanica)a).toList(); }
    @Override public List<AtraccionCultural> findAllCulturales() { return atracciones.values().stream().filter(a -> a instanceof AtraccionCultural).map(a -> (AtraccionCultural)a).toList(); }
    @Override public List<Espectaculo> findAllEspectaculos() { return new ArrayList<>(espectaculos.values()); }
}

class ServicioGestionElementosParqueTest {
    private ElementoParqueRepository repo;
    private ServicioGestionElementosParque servicio;
    
    @BeforeEach
    void setUp() {
        repo = new DummyElementoParqueRepository();
        servicio = new ServicioGestionElementosParque(repo);
    }
    
    /**
     * ID: TC_CU01_CREAR_ATRACCION_MECANICA_EXITO_01
     * Componente/Clase Probada: ServicioGestionElementosParque, AtraccionMecanica
     * Método Probado: crearAtraccionMecanica(...)
     * Suite de Pruebas: Gestión de Elementos del Parque - Creación
     * Descripción/Propósito: Verifica la creación exitosa de una atracción mecánica de riesgo alto con datos válidos.
     * Precondiciones/Estado Inicial: Repositorio vacío, instancia de ServicioGestionElementosParque.
     * Pasos de Ejecución:
     *   1. Llamar a crearAtraccionMecanica con datos válidos.
     *   2. Verificar que el objeto retornado no es nulo y sus datos coinciden.
     *   3. Consultar por ID y nombre, verificar existencia.
     *   4. Consultar lista de atracciones mecánicas, verificar inclusión.
     * Datos de Entrada: ID="AM1", Nombre="Montaña Rusa", ... NivelRiesgo=ALTO, Capacitacion=OP. RIESGO ALTO
     * Resultado Esperado: Se crea la atracción correctamente, se puede consultar por ID/nombre, aparece en la lista.
     * Trazabilidad: CU-01 (Crear Atracción Mecánica)
     */
    @Test
    void testCrearAtraccionMecanicaExito() {
        // Datos de prueba para atracción mecánica
        String id = "AM1";
        String nombre = "Montaña Rusa";
        String ubicacion = "Zona A";
        int cupoMaximo = 20;
        int empleadosMinimos = 2;
        NivelExclusividad exclusividad = NivelExclusividad.ORO;
        NivelRiesgo riesgo = NivelRiesgo.ALTO;
        double alturaMin = 1.2, alturaMax = 2.0, pesoMin = 30, pesoMax = 120;
        List<String> contraindicaciones = List.of("Cardiopatía");
        List<String> restricciones = List.of("Vértigo");
        List<CondicionClimatica> clima = List.of(CondicionClimatica.LLUVIA_FUERTE);
        Capacitacion cap = Capacitacion.OPERACION_ATRACCION_RIESGO_ALTO;
        
        // Crear atracción mecánica
        AtraccionMecanica creada = servicio.crearAtraccionMecanica(id, nombre, ubicacion, cupoMaximo, empleadosMinimos,
                exclusividad, riesgo, alturaMin, alturaMax, pesoMin, pesoMax, contraindicaciones, restricciones, clima, cap);
        
        // Verificar creación correcta
        assertNotNull(creada);
        assertEquals(nombre, creada.getNombre());
        assertEquals(ubicacion, creada.getUbicacion());
        assertEquals(exclusividad, creada.getNivelExclusividad());
        assertEquals(riesgo, creada.getNivelRiesgo());
        assertEquals(clima, creada.getClimaNoPermitido());
        
        // Verificar que se puede recuperar
        Optional<Atraccion> encontrada = servicio.consultarAtraccionPorId(id);
        assertTrue(encontrada.isPresent());
        assertEquals(nombre, encontrada.get().getNombre());
        
        // Verificar consulta por nombre
        Optional<Atraccion> encontradaPorNombre = servicio.consultarAtraccionPorNombre(nombre);
        assertTrue(encontradaPorNombre.isPresent());
        assertEquals(id, encontradaPorNombre.get().getId());
        
        // Verificar lista de atracciones mecánicas
        List<AtraccionMecanica> mecanicas = servicio.consultarAtraccionesMecanicas();
        assertEquals(1, mecanicas.size());
        assertEquals(id, mecanicas.get(0).getId());
    }
    
    /**
     * ID: TC_CU02_CREAR_ATRACCION_CULTURAL_EXITO_01
     * Componente/Clase Probada: ServicioGestionElementosParque, AtraccionCultural
     * Método Probado: crearAtraccionCultural(...)
     * Suite de Pruebas: Gestión de Elementos del Parque - Creación
     * Descripción/Propósito: Verifica la creación exitosa de una atracción cultural con datos válidos.
     * Precondiciones/Estado Inicial: Repositorio vacío, instancia de ServicioGestionElementosParque.
     * Pasos de Ejecución:
     *   1. Llamar a crearAtraccionCultural con datos válidos.
     *   2. Verificar que el objeto retornado no es nulo y sus datos coinciden.
     *   3. Consultar por ID, verificar existencia y tipo.
     *   4. Consultar lista de atracciones culturales, verificar inclusión.
     * Datos de Entrada: ID="AC1", Nombre="Museo de Ciencias", ... EdadMinima=8, NivelExclusividad=FAMILIAR
     * Resultado Esperado: Se crea la atracción correctamente, se puede consultar por ID, aparece en la lista.
     * Trazabilidad: CU-02 (Crear Atracción Cultural)
     */
    @Test
    void testCrearAtraccionCulturalExito() {
        // Datos para atracción cultural
        String id = "AC1";
        String nombre = "Museo de Ciencias";
        String ubicacion = "Zona Educativa";
        int cupoMaximo = 50;
        int empleadosMinimos = 3;
        int edadMinima = 8;
        NivelExclusividad exclusividad = NivelExclusividad.FAMILIAR;
        List<CondicionClimatica> clima = List.of();
        
        // Crear atracción cultural
        AtraccionCultural creada = servicio.crearAtraccionCultural(id, nombre, ubicacion, cupoMaximo, empleadosMinimos, 
                edadMinima, clima, exclusividad);
        
        // Verificar creación correcta
        assertNotNull(creada);
        assertEquals(nombre, creada.getNombre());
        assertEquals(edadMinima, creada.getEdadMinima());
        assertEquals(exclusividad, creada.getNivelExclusividad());
        
        // Verificar que se puede recuperar
        Optional<ElementoParque> encontrado = servicio.consultarElementoPorId(id);
        assertTrue(encontrado.isPresent());
        assertTrue(encontrado.get() instanceof AtraccionCultural);
        assertEquals(nombre, encontrado.get().getNombre());
        
        // Verificar lista de atracciones culturales
        List<AtraccionCultural> culturales = servicio.consultarAtraccionesCulturales();
        assertEquals(1, culturales.size());
        assertEquals(id, culturales.get(0).getId());
    }
    
    /**
     * ID: TC_CU03_CREAR_ESPECTACULO_EXITO_01
     * Componente/Clase Probada: ServicioGestionElementosParque, Espectaculo
     * Método Probado: crearEspectaculo(...)
     * Suite de Pruebas: Gestión de Elementos del Parque - Creación
     * Descripción/Propósito: Verifica la creación exitosa de un espectáculo con datos válidos.
     * Precondiciones/Estado Inicial: Repositorio vacío, instancia de ServicioGestionElementosParque.
     * Pasos de Ejecución:
     *   1. Llamar a crearEspectaculo con datos válidos.
     *   2. Verificar que el objeto retornado no es nulo y sus datos coinciden.
     *   3. Consultar por ID y nombre, verificar existencia.
     *   4. Consultar lista de espectáculos, verificar inclusión.
     * Datos de Entrada: ID="E1", Nombre="Show de Magia", ... Horarios, ClimaNoPermitido
     * Resultado Esperado: Se crea el espectáculo correctamente, se puede consultar por ID/nombre, aparece en la lista.
     * Trazabilidad: CU-03 (Crear Espectáculo)
     */
    @Test
    void testCrearEspectaculoExito() {
        // Datos para espectáculo
        String id = "E1";
        String nombre = "Show de Magia";
        String ubicacion = "Plaza Central";
        int cupoMaximo = 100;
        String descripcion = "Magia para toda la familia";
        List<RangoFechaHora> horarios = List.of(
            new RangoFechaHora(
                LocalDateTime.of(2025, 4, 14, 10, 0),
                LocalDateTime.of(2025, 4, 14, 12, 0)
            )
        );
        List<CondicionClimatica> clima = List.of(CondicionClimatica.LLUVIA_FUERTE, CondicionClimatica.TORMENTA);
        
        // Crear espectáculo
        Espectaculo creado = servicio.crearEspectaculo(id, nombre, ubicacion, cupoMaximo, descripcion, horarios, clima);
        
        // Verificar creación correcta
        assertNotNull(creado);
        assertEquals(nombre, creado.getNombre());
        assertEquals(descripcion, creado.getDescripcion());
        assertEquals(horarios.size(), creado.getHorarios().size());
        assertEquals(clima, creado.getClimaNoPermitido());
        
        // Verificar que se puede recuperar
        Optional<Espectaculo> encontrado = servicio.consultarEspectaculoPorId(id);
        assertTrue(encontrado.isPresent());
        assertEquals(nombre, encontrado.get().getNombre());
        
        // Verificar consulta por nombre
        Optional<Espectaculo> encontradoPorNombre = servicio.consultarEspectaculoPorNombre(nombre);
        assertTrue(encontradoPorNombre.isPresent());
        assertEquals(id, encontradoPorNombre.get().getId());
        
        // Verificar lista de espectáculos
        List<Espectaculo> espectaculos = servicio.consultarEspectaculos();
        assertEquals(1, espectaculos.size());
        assertEquals(id, espectaculos.get(0).getId());
    }
    
    /**
     * ID: TC_CU05_ACTUALIZAR_ATRACCION_EXITO_01
     * Componente/Clase Probada: ServicioGestionElementosParque, AtraccionMecanica
     * Método Probado: actualizarAtraccion(...)
     * Suite de Pruebas: Gestión de Elementos del Parque - Actualización
     * Descripción/Propósito: Verifica la actualización exitosa de una atracción mecánica existente.
     * Precondiciones/Estado Inicial: Atracción mecánica creada previamente.
     * Pasos de Ejecución:
     *   1. Modificar nombre y nivel de exclusividad de la atracción.
     *   2. Llamar a actualizarAtraccion.
     *   3. Consultar por ID y verificar los cambios.
     * Datos de Entrada: ID="AM2", NuevoNombre="Noria Gigante", NuevoNivel=ORO
     * Resultado Esperado: La atracción se actualiza correctamente y los cambios son persistidos.
     * Trazabilidad: CU-05 (Actualizar Información de Atracción)
     */
    @Test
    void testActualizarAtraccionExito() {
        // Crear atracción para prueba
        String id = "AM2";
        String nombre = "La Vuelta al Mundo";
        String ubicacion = "Zona B";
        int cupoMaximo = 30;
        int empleadosMinimos = 2;
        NivelExclusividad exclusividad = NivelExclusividad.FAMILIAR;
        NivelRiesgo riesgo = NivelRiesgo.MEDIO;
        double alturaMin = 1.0, alturaMax = 2.2, pesoMin = 25, pesoMax = 130;
        List<String> contraindicaciones = List.of();
        List<String> restricciones = List.of();
        List<CondicionClimatica> clima = List.of(CondicionClimatica.TORMENTA);
        Capacitacion cap = null; // No necesita capacitación específica si el riesgo es MEDIO
        
        servicio.crearAtraccionMecanica(id, nombre, ubicacion, cupoMaximo, empleadosMinimos,
                exclusividad, riesgo, alturaMin, alturaMax, pesoMin, pesoMax, contraindicaciones, restricciones, clima, cap);
        
        // Modificar atracción
        String nuevoNombre = "Noria Gigante";
        NivelExclusividad nuevoNivel = NivelExclusividad.ORO;
        
        AtraccionMecanica atraccion = new AtraccionMecanica(id, nuevoNombre, ubicacion, cupoMaximo, empleadosMinimos,
                nuevoNivel, riesgo, alturaMin, alturaMax, pesoMin, pesoMax, contraindicaciones, restricciones, clima, cap);
        
        // Actualizar
        servicio.actualizarAtraccion(atraccion);
        
        // Verificar actualización
        Optional<Atraccion> actualizada = servicio.consultarAtraccionPorId(id);
        assertTrue(actualizada.isPresent());
        assertEquals(nuevoNombre, actualizada.get().getNombre());
        assertEquals(nuevoNivel, actualizada.get().getNivelExclusividad());
    }
    
    /**
     * ID: TC_CU05_ACTUALIZAR_ESPECTACULO_EXITO_01
     * Componente/Clase Probada: ServicioGestionElementosParque, Espectaculo
     * Método Probado: actualizarEspectaculo(...)
     * Suite de Pruebas: Gestión de Elementos del Parque - Actualización
     * Descripción/Propósito: Verifica la actualización exitosa de un espectáculo existente.
     * Precondiciones/Estado Inicial: Espectáculo creado previamente.
     * Pasos de Ejecución:
     *   1. Modificar descripción y cupo máximo del espectáculo.
     *   2. Llamar a actualizarEspectaculo.
     *   3. Consultar por ID y verificar los cambios.
     * Datos de Entrada: ID="E2", NuevaDescripcion, NuevoCupo
     * Resultado Esperado: El espectáculo se actualiza correctamente y los cambios son persistidos.
     * Trazabilidad: CU-05 (Actualizar Información de Espectáculo)
     */
    @Test
    void testActualizarEspectaculoExito() {
        // Crear espectáculo para prueba
        String id = "E2";
        String nombre = "Acrobacia Aérea";
        String ubicacion = "Plaza Este";
        int cupoMaximo = 120;
        String descripcion = "Acrobacias impresionantes";
        List<RangoFechaHora> horarios = List.of(
            new RangoFechaHora(
                LocalDateTime.of(2025, 5, 15, 15, 0),
                LocalDateTime.of(2025, 5, 15, 17, 0)
            )
        );
        List<CondicionClimatica> clima = List.of(CondicionClimatica.LLUVIA_FUERTE, CondicionClimatica.TORMENTA);
        
        Espectaculo espectaculo = servicio.crearEspectaculo(id, nombre, ubicacion, cupoMaximo, 
                descripcion, horarios, clima);
        
        // Modificar espectáculo
        String nuevaDescripcion = "Acrobacias impresionantes con artistas internacionales";
        espectaculo.setDescripcion(nuevaDescripcion);
        int nuevoCupo = 150;
        espectaculo.setCupoMaximo(nuevoCupo);
        
        // Actualizar
        servicio.actualizarEspectaculo(espectaculo);
        
        // Verificar actualización
        Optional<Espectaculo> actualizado = servicio.consultarEspectaculoPorId(id);
        assertTrue(actualizado.isPresent());
        assertEquals(nuevaDescripcion, actualizado.get().getDescripcion());
        assertEquals(nuevoCupo, actualizado.get().getCupoMaximo());
    }
    
    @Test
    void testEliminarElemento() {
        // Crear atracción para prueba
        String id = "A5";
        String nombre = "Tobogán Acuático";
        String ubicacion = "Zona Agua";
        int cupoMaximo = 25;
        int empleadosMinimos = 3;
        int edadMinima = 10;
        NivelExclusividad exclusividad = NivelExclusividad.DIAMANTE;
        List<CondicionClimatica> clima = List.of(CondicionClimatica.TORMENTA);
        
        servicio.crearAtraccionCultural(id, nombre, ubicacion, cupoMaximo, 
                empleadosMinimos, edadMinima, clima, exclusividad);
        
        // Verificar que existe
        assertTrue(servicio.consultarElementoPorId(id).isPresent());
        
        // Eliminar
        servicio.eliminarElemento(id);
        
        // Verificar que ya no existe
        assertTrue(servicio.consultarElementoPorId(id).isEmpty());
        
        // Intentar eliminar elemento inexistente debe lanzar excepción
        assertThrows(AtraccionNoEncontradaException.class, () -> 
            servicio.eliminarElemento("ID_INEXISTENTE"));
    }
    
    @Test
    void testConsultasAvanzadas() {
        // Crear atracciones y espectáculos para pruebas de consultas
        
        // Atracciones mecánicas
        servicio.crearAtraccionMecanica(
            "AM10", "Montaña Rusa", "Zona A", 20, 2, 
            NivelExclusividad.ORO, NivelRiesgo.ALTO, 
            1.2, 2.0, 30, 120, List.of("Cardiopatía"), List.of(), 
            List.of(CondicionClimatica.LLUVIA_FUERTE), Capacitacion.OPERACION_ATRACCION_RIESGO_ALTO
        );
        
        servicio.crearAtraccionMecanica(
            "AM11", "Tren Fantasma", "Zona B", 15, 2, 
            NivelExclusividad.ORO, NivelRiesgo.MEDIO, 
            1.0, 2.0, 0, 150, List.of(), List.of(), 
            List.of(CondicionClimatica.TORMENTA), null
        );
        
        // Atracciones culturales
        servicio.crearAtraccionCultural(
            "AC10", "Museo de Arte", "Zona Cultural", 50, 2, 
            6, List.of(), NivelExclusividad.FAMILIAR
        );
        
        servicio.crearAtraccionCultural(
            "AC11", "Cine 4D", "Zona Centro", 30, 1, 
            8, List.of(CondicionClimatica.TORMENTA), NivelExclusividad.ORO
        );
        
        // Espectáculos
        servicio.crearEspectaculo(
            "ESP10", "Show de Luces", "Plaza Central", 200, 
            "Espectáculo nocturno", 
            List.of(new RangoFechaHora(
                LocalDateTime.of(2025, 6, 1, 20, 0),
                LocalDateTime.of(2025, 6, 1, 22, 0)
            )),
            List.of(CondicionClimatica.LLUVIA_FUERTE, CondicionClimatica.TORMENTA)
        );
        
        // Pruebas de consulta
        
        // Consultar todas las atracciones
        List<Atraccion> todasAtracciones = servicio.consultarTodasLasAtracciones();
        assertEquals(4, todasAtracciones.size());
        
        // Consultar todos los elementos
        List<ElementoParque> todosElementos = servicio.consultarTodosLosElementos();
        assertEquals(5, todosElementos.size());
        
        // Consultar por nivel de exclusividad
        List<Atraccion> atraccionesOro = servicio.consultarAtraccionesPorExclusividad(NivelExclusividad.ORO);
        assertEquals(3, atraccionesOro.size());
        
        // Consultar por clima no permitido
        List<ElementoParque> elementosTormenta = servicio.consultarElementosPorClima(CondicionClimatica.TORMENTA);
        assertEquals(3, elementosTormenta.size());
        
        List<ElementoParque> elementosLluvia = servicio.consultarElementosPorClima(CondicionClimatica.LLUVIA_FUERTE);
        assertEquals(2, elementosLluvia.size());
    }
    
    @Test
    void testDefinirTemporadaElemento() {
        // Crear atracción para prueba
        String id = "AM20";
        String nombre = "Montaña Rusa Estacional";
        String ubicacion = "Zona A";
        int cupoMaximo = 20;
        int empleadosMinimos = 2;
        NivelExclusividad exclusividad = NivelExclusividad.ORO;
        NivelRiesgo riesgo = NivelRiesgo.ALTO;
        double alturaMin = 1.2, alturaMax = 2.0, pesoMin = 30, pesoMax = 120;
        List<String> contraindicaciones = List.of();
        List<String> restricciones = List.of();
        List<CondicionClimatica> clima = List.of();
        Capacitacion cap = Capacitacion.OPERACION_ATRACCION_RIESGO_ALTO;
        
        servicio.crearAtraccionMecanica(id, nombre, ubicacion, cupoMaximo, empleadosMinimos,
                exclusividad, riesgo, alturaMin, alturaMax, pesoMin, pesoMax, contraindicaciones, restricciones, clima, cap);
        
        // Definir temporada
        LocalDateTime inicio = LocalDateTime.of(2025, 6, 1, 0, 0);
        LocalDateTime fin = LocalDateTime.of(2025, 8, 31, 23, 59);
        
        servicio.definirTemporadaElemento(id, inicio, fin);
        
        // Verificar que se estableció la temporada
        Optional<ElementoParque> actualizado = servicio.consultarElementoPorId(id);
        assertTrue(actualizado.isPresent());
        assertTrue(actualizado.get().isEsDeTemporada());
    }
    
    @Test
    void testEliminarAtraccionesPorExclusividad() {
        // Crear atracciones para prueba
        servicio.crearAtraccionMecanica(
            "AM30", "Atracción VIP 1", "Zona A", 20, 2, 
            NivelExclusividad.DIAMANTE, NivelRiesgo.ALTO, 
            1.2, 2.0, 30, 120, List.of(), List.of(), 
            List.of(), Capacitacion.OPERACION_ATRACCION_RIESGO_ALTO
        );
        
        servicio.crearAtraccionMecanica(
            "AM31", "Atracción VIP 2", "Zona B", 15, 2, 
            NivelExclusividad.DIAMANTE, NivelRiesgo.MEDIO, 
            1.0, 2.0, 0, 150, List.of(), List.of(), 
            List.of(), null
        );
        
        servicio.crearAtraccionCultural(
            "AC30", "Museo Premium", "Zona Cultural", 50, 2, 
            6, List.of(), NivelExclusividad.ORO
        );
        
        // Verificar que existen
        assertEquals(2, servicio.consultarAtraccionesPorExclusividad(NivelExclusividad.DIAMANTE).size());
        
        // Eliminar atracciones de nivel DIAMANTE
        servicio.eliminarAtraccionesPorExclusividad(NivelExclusividad.DIAMANTE);
        
        // Verificar que se eliminaron
        assertEquals(0, servicio.consultarAtraccionesPorExclusividad(NivelExclusividad.DIAMANTE).size());
        assertEquals(1, servicio.consultarTodasLasAtracciones().size()); // Debe quedar la de ORO
    }
    
    @Test
    void testEliminarEspectaculosPorClima() {
        // Crear espectáculos para prueba
        servicio.crearEspectaculo(
            "ESP20", "Show de Agua", "Fuente Central", 200, 
            "Espectáculo con agua", 
            List.of(new RangoFechaHora(
                LocalDateTime.of(2025, 7, 1, 15, 0),
                LocalDateTime.of(2025, 7, 1, 16, 0)
            )),
            List.of(CondicionClimatica.TORMENTA)
        );
        
        servicio.crearEspectaculo(
            "ESP21", "Danzas", "Escenario Principal", 150, 
            "Espectáculo de danza", 
            List.of(new RangoFechaHora(
                LocalDateTime.of(2025, 7, 2, 18, 0),
                LocalDateTime.of(2025, 7, 2, 19, 30)
            )),
            List.of(CondicionClimatica.TORMENTA, CondicionClimatica.LLUVIA_FUERTE)
        );
        
        servicio.crearEspectaculo(
            "ESP22", "Concierto", "Anfiteatro", 300, 
            "Concierto musical", 
            List.of(new RangoFechaHora(
                LocalDateTime.of(2025, 7, 3, 20, 0),
                LocalDateTime.of(2025, 7, 3, 23, 0)
            )),
            List.of(CondicionClimatica.LLUVIA_FUERTE)
        );
        
        // Verificar que existen
        assertEquals(3, servicio.consultarEspectaculos().size());
        
        // Eliminar espectáculos que no permiten TORMENTA
        servicio.eliminarEspectaculosPorClima(CondicionClimatica.TORMENTA);
        
        // Verificar que se eliminaron los apropiados
        assertEquals(1, servicio.consultarEspectaculos().size()); // Solo queda el que solo tiene LLUVIA_FUERTE
        Optional<Espectaculo> restante = servicio.consultarEspectaculoPorId("ESP22");
        assertTrue(restante.isPresent());
    }
    
    @Test
    void testErroresValidacionDatos() {
        // Prueba con datos inválidos
        
        // ID nulo o vacío
        assertThrows(DatosInvalidosException.class, () -> 
            servicio.crearAtraccionMecanica(null, "Test", "Ubicación", 20, 2, 
                NivelExclusividad.FAMILIAR, NivelRiesgo.MEDIO, 1.0, 2.0, 30, 120, 
                List.of(), List.of(), List.of(), null));
        
        // Nombre nulo
        assertThrows(DatosInvalidosException.class, () -> 
            servicio.crearAtraccionMecanica("ID1", null, "Ubicación", 20, 2, 
                NivelExclusividad.FAMILIAR, NivelRiesgo.MEDIO, 1.0, 2.0, 30, 120, 
                List.of(), List.of(), List.of(), null));
        
        // Empleados mínimos negativos
        assertThrows(DatosInvalidosException.class, () -> 
            servicio.crearAtraccionMecanica("ID1", "Test", "Ubicación", 20, -1, 
                NivelExclusividad.FAMILIAR, NivelRiesgo.MEDIO, 1.0, 2.0, 30, 120, 
                List.of(), List.of(), List.of(), null));
        
        // Alturas inválidas (min >= max)
        assertThrows(DatosInvalidosException.class, () -> 
            servicio.crearAtraccionMecanica("ID1", "Test", "Ubicación", 20, 2, 
                NivelExclusividad.FAMILIAR, NivelRiesgo.MEDIO, 2.0, 1.5, 30, 120, 
                List.of(), List.of(), List.of(), null));
        
        // Riesgo ALTO sin capacitación específica
        assertThrows(DatosInvalidosException.class, () -> 
            servicio.crearAtraccionMecanica("ID1", "Test", "Ubicación", 20, 2, 
                NivelExclusividad.FAMILIAR, NivelRiesgo.ALTO, 1.0, 2.0, 30, 120, 
                List.of(), List.of(), List.of(), null));
    }
    
    @Test
    void testGestionElementosCompleto() {
        ElementoParqueRepository repo = new DummyElementoParqueRepository();
        ServicioGestionElementosParque servicio = new ServicioGestionElementosParque(repo);

        // Datos de prueba para atracción mecánica
        String id = "A1";
        String nombre = "Montaña Rusa";
        String ubicacion = "Zona A";
        int cupoMaximo = 20;
        int empleadosMinimos = 2;
        NivelExclusividad exclusividad = NivelExclusividad.ORO;
        NivelRiesgo riesgo = NivelRiesgo.ALTO;
        double alturaMin = 1.2, alturaMax = 2.0, pesoMin = 30, pesoMax = 120;
        List<String> contraindicaciones = List.of("Cardiopatía");
        List<String> restricciones = List.of("Vértigo");
        List<CondicionClimatica> clima = List.of();
        Capacitacion cap = Capacitacion.OPERACION_ATRACCION_RIESGO_ALTO;

        AtraccionMecanica atraccion = new AtraccionMecanica(id, nombre, ubicacion, cupoMaximo, empleadosMinimos,
                exclusividad, riesgo, alturaMin, alturaMax, pesoMin, pesoMax, contraindicaciones, restricciones, clima, cap);
        repo.save(atraccion);

        // Crear atracción mecánica
        AtraccionMecanica creada = servicio.crearAtraccionMecanica(id, nombre, ubicacion, cupoMaximo, empleadosMinimos,
                exclusividad, riesgo, alturaMin, alturaMax, pesoMin, pesoMax, contraindicaciones, restricciones, clima, cap);
        assertNotNull(creada);
        assertEquals(nombre, creada.getNombre());

        // Consultar por nombre
        Optional<Atraccion> encontrada = servicio.consultarAtraccionPorNombre(nombre);
        assertTrue(encontrada.isPresent());
        assertEquals(nombre, encontrada.get().getNombre());

        // Datos de prueba para espectáculo
        String idEsp = "E1";
        String nombreEsp = "Show de Magia";
        String ubicacionEsp = "Plaza Central";
        int cupoEsp = 100;
        String descripcion = "Magia para toda la familia";
        // Proveer al menos un horario válido
        List<dominio.util.RangoFechaHora> horarios = List.of(
            new dominio.util.RangoFechaHora(
                java.time.LocalDateTime.of(2025, 4, 14, 10, 0),
                java.time.LocalDateTime.of(2025, 4, 14, 12, 0)
            )
        );
        Espectaculo espectaculo = new Espectaculo(idEsp, nombreEsp, ubicacionEsp, cupoEsp, descripcion, horarios, clima);
        repo.save(espectaculo);

        // Crear espectáculo
        Espectaculo creadoEsp = servicio.crearEspectaculo(idEsp, nombreEsp, ubicacionEsp, cupoEsp, descripcion, horarios, clima);
        assertNotNull(creadoEsp);
        assertEquals(nombreEsp, creadoEsp.getNombre());

        // Consultar espectáculo por nombre
        Optional<Espectaculo> encontradoEsp = servicio.consultarEspectaculoPorNombre(nombreEsp);
        assertTrue(encontradoEsp.isPresent());
        assertEquals(nombreEsp, encontradoEsp.get().getNombre());
    }

    @Test
    void testConsultarAtraccionesMecanicasPorRiesgo() {
        // Crear varias atracciones mecánicas con diferentes niveles de riesgo
        servicio.crearAtraccionMecanica(
            "AM100", "Rápidos", "Zona Agua", 10, 2, 
            NivelExclusividad.FAMILIAR, NivelRiesgo.ALTO, 
            1.2, 2.0, 30, 120, List.of(), List.of(), List.of(), Capacitacion.OPERACION_ATRACCION_RIESGO_ALTO
        );
        servicio.crearAtraccionMecanica(
            "AM101", "Tren Infantil", "Zona Niños", 8, 1, 
            NivelExclusividad.FAMILIAR, NivelRiesgo.MEDIO, 
            0.8, 1.5, 10, 50, List.of(), List.of(), List.of(), null
        );
        // Consulta por riesgo ALTO
        List<AtraccionMecanica> alto = servicio.consultarAtraccionesMecanicasPorRiesgo(NivelRiesgo.ALTO);
        assertEquals(1, alto.size());
        assertEquals("AM100", alto.get(0).getId());
        // Consulta por riesgo MEDIO
        List<AtraccionMecanica> medio = servicio.consultarAtraccionesMecanicasPorRiesgo(NivelRiesgo.MEDIO);
        assertEquals(1, medio.size());
        assertEquals("AM101", medio.get(0).getId());
        // Error por riesgo nulo
        assertThrows(DatosInvalidosException.class, () -> servicio.consultarAtraccionesMecanicasPorRiesgo(null));
    }

    @Test
    void testErroresConsultasYEliminaciones() {
        // Consultas con parámetros nulos o vacíos
        assertTrue(servicio.consultarAtraccionPorId(null).isEmpty());
        assertTrue(servicio.consultarAtraccionPorId("").isEmpty());
        assertTrue(servicio.consultarEspectaculoPorId(null).isEmpty());
        assertTrue(servicio.consultarEspectaculoPorId("").isEmpty());
        assertTrue(servicio.consultarElementoPorId(null).isEmpty());
        assertTrue(servicio.consultarElementoPorId("").isEmpty());
        assertTrue(servicio.consultarAtraccionPorNombre(null).isEmpty());
        assertTrue(servicio.consultarAtraccionPorNombre("").isEmpty());
        assertTrue(servicio.consultarEspectaculoPorNombre(null).isEmpty());
        assertTrue(servicio.consultarEspectaculoPorNombre("").isEmpty());
        // Eliminar atracción inexistente no lanza excepción (por diseño)
        servicio.eliminarAtraccion("NOEXISTE");
        // Eliminar atracciones por exclusividad nula
        assertThrows(DatosInvalidosException.class, () -> servicio.eliminarAtraccionesPorExclusividad(null));
        // Eliminar espectáculos por clima nulo
        assertThrows(DatosInvalidosException.class, () -> servicio.eliminarEspectaculosPorClima(null));
        // Consultar atracciones por exclusividad nula
        assertThrows(DatosInvalidosException.class, () -> servicio.consultarAtraccionesPorExclusividad(null));
        // Consultar elementos por clima nulo
        assertThrows(DatosInvalidosException.class, () -> servicio.consultarElementosPorClima(null));
    }

    /**
     * Prueba: crearAtraccionCultural con nivelExclusividad null (debe lanzar excepción)
     */
    @Test
    void testCrearAtraccionCulturalNivelExclusividadNull() {
        assertThrows(DatosInvalidosException.class, () ->
            servicio.crearAtraccionCultural("AC_NULL", "Cultural Null", "Zona X", 10, 1, 5, List.of(), null)
        );
    }

    /**
     * Prueba: actualizarAtraccion con atracción null (debe lanzar excepción)
     */
    @Test
    void testActualizarAtraccionNull() {
        assertThrows(DatosInvalidosException.class, () ->
            servicio.actualizarAtraccion(null)
        );
    }

    /**
     * Prueba: actualizarEspectaculo con espectáculo null (debe lanzar excepción)
     */
    @Test
    void testActualizarEspectaculoNull() {
        assertThrows(DatosInvalidosException.class, () ->
            servicio.actualizarEspectaculo(null)
        );
    }

    /**
     * Prueba: actualizarEspectaculo con espectáculo inexistente (debe lanzar excepción)
     */
    @Test
    void testActualizarEspectaculoInexistente() {
        // Crear espectáculo con al menos un horario válido
        List<RangoFechaHora> horarios = List.of(
            new RangoFechaHora(
                LocalDateTime.of(2025, 5, 15, 15, 0),
                LocalDateTime.of(2025, 5, 15, 17, 0)
            )
        );
        Espectaculo espectaculo = new Espectaculo("E999", "No existe", "Zona Z", 10, "desc", horarios, List.of());
        assertThrows(AtraccionNoEncontradaException.class, () ->
            servicio.actualizarEspectaculo(espectaculo)
        );
    }

    /**
     * Prueba: definirTemporadaElemento con id inexistente (debe lanzar excepción)
     */
    @Test
    void testDefinirTemporadaElementoInexistente() {
        assertThrows(AtraccionNoEncontradaException.class, () ->
            servicio.definirTemporadaElemento("ID_NO_EXISTE", LocalDateTime.now(), LocalDateTime.now().plusDays(1))
        );
    }

    /**
     * Prueba: definirTemporadaElemento con tipo de elemento desconocido (debe lanzar IllegalArgumentException)
     */
    @Test
    void testDefinirTemporadaElementoTipoDesconocido() {
        // Crear un tipo de ElementoParque anónimo no manejado
        ElementoParqueRepository repo = new DummyElementoParqueRepository() {
            @Override public Optional<ElementoParque> findById(String id) {
                return Optional.of(new ElementoParque("X1", "Desconocido", 1) {});
            }
        };
        ServicioGestionElementosParque servicio = new ServicioGestionElementosParque(repo);
        assertThrows(IllegalArgumentException.class, () ->
            servicio.definirTemporadaElemento("X1", LocalDateTime.now(), LocalDateTime.now().plusDays(1))
        );
    }
}

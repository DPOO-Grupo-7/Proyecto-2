package aplicacion;

import dominio.elementoparque.*;
import dominio.empleado.Capacitacion;
// Importar excepciones personalizadas
import dominio.excepciones.AtraccionNoEncontradaException;
import dominio.excepciones.DatosInvalidosException;
import dominio.util.CondicionClimatica;
import dominio.util.RangoFechaHora;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de atracciones y espectáculos del parque de diversiones.
 * <p>
 * Permite crear, consultar, modificar y eliminar atracciones y espectáculos, así como definir temporadas.
 * Todas las operaciones de modificación se asumen realizadas por un Administrador.
 * </p>
 *
 * <b>Precondiciones generales:</b>
 * <ul>
 *   <li>Los parámetros de entrada no deben ser nulos y deben cumplir las restricciones de dominio.</li>
 *   <li>El repositorio debe estar correctamente inyectado.</li>
 * </ul>
 *
 * <b>Poscondiciones generales:</b>
 * <ul>
 *   <li>Las entidades quedan creadas, actualizadas o eliminadas según la operación.</li>
 * </ul>
 *
 * @author Sistema Parque
 */
public class ServicioGestionElementosParque {

    private final ElementoParqueRepository elementoParqueRepository;

    public ServicioGestionElementosParque(ElementoParqueRepository elementoParqueRepository) {
        this.elementoParqueRepository = elementoParqueRepository;
    }

    /**
     * Crea y guarda una nueva atracción mecánica.
     *
     * <b>Precondiciones:</b>
     * <ul>
     *   <li>Todos los parámetros deben ser válidos y no nulos.</li>
     *   <li>El nivel de riesgo y exclusividad deben estar definidos.</li>
     * </ul>
     *
     * <b>Poscondiciones:</b>
     * <ul>
     *   <li>La atracción mecánica queda registrada en el sistema.</li>
     * </ul>
     *
     * @param id ID único de la atracción.
     * @param nombre Nombre de la atracción.
     * @param ubicacion Ubicación física.
     * @param cupoMaximo Cupo máximo permitido.
     * @param empleadosMinimos Número mínimo de empleados requeridos.
     * @param nivelExclusividad Nivel de exclusividad requerido.
     * @param nivelRiesgo Nivel de riesgo de la atracción.
     * @param alturaMinima Altura mínima permitida.
     * @param alturaMaxima Altura máxima permitida.
     * @param pesoMinimo Peso mínimo permitido.
     * @param pesoMaximo Peso máximo permitido.
     * @param contraindicaciones Lista de contraindicaciones de salud.
     * @param restricciones Lista de restricciones de salud.
     * @param climaNoPermitido Condiciones climáticas no permitidas.
     * @param capacitacionEspecifica Capacitación específica requerida (si aplica).
     * @return La atracción mecánica creada.
     * @throws DatosInvalidosException si los datos son inválidos.
     * @example
     * <pre>
     *     AtraccionMecanica a = servicio.crearAtraccionMecanica("A1", "Montaña Rusa", "Zona A", 30, 3, NivelExclusividad.ORO, NivelRiesgo.ALTO, 1.2, 2.0, 30, 120, List.of("Cardiopatía"), List.of(), List.of(CondicionClimatica.LLUVIOSO), Capacitacion.OPERACION_ATRACCION_RIESGO_ALTO);
     * </pre>
     */
    public AtraccionMecanica crearAtraccionMecanica(String id, String nombre, String ubicacion,
                                                  int cupoMaximo, int empleadosMinimos,
                                                  NivelExclusividad nivelExclusividad, NivelRiesgo nivelRiesgo,
                                                  double alturaMinima, double alturaMaxima,
                                                  double pesoMinimo, double pesoMaximo,
                                                  List<String> contraindicaciones, List<String> restricciones,
                                                  List<CondicionClimatica> climaNoPermitido,
                                                  Capacitacion capacitacionEspecifica) {
        // Las validaciones ahora están principalmente en el constructor de AtraccionMecanica
        AtraccionMecanica nueva = new AtraccionMecanica(id, nombre, ubicacion, cupoMaximo, empleadosMinimos,
                nivelExclusividad, nivelRiesgo, alturaMinima, alturaMaxima, pesoMinimo, pesoMaximo,
                contraindicaciones, restricciones, climaNoPermitido, capacitacionEspecifica);
        elementoParqueRepository.save(nueva);
        return nueva;
    }

    /**
     * Crea y guarda una nueva atracción cultural.
     *
     * <b>Precondiciones:</b>
     * <ul>
     *   <li>Todos los parámetros deben ser válidos y no nulos.</li>
     *   <li>El nivel de exclusividad debe estar definido.</li>
     * </ul>
     *
     * <b>Poscondiciones:</b>
     * <ul>
     *   <li>La atracción cultural queda registrada en el sistema.</li>
     * </ul>
     *
     * @param id ID único de la atracción.
     * @param nombre Nombre de la atracción.
     * @param ubicacion Ubicación física.
     * @param cupoMaximo Cupo máximo permitido.
     * @param empleadosMinimos Número mínimo de empleados requeridos.
     * @param edadMinima Edad mínima requerida.
     * @param climaNoPermitido Condiciones climáticas no permitidas.
     * @param nivelExclusividad Nivel de exclusividad requerido.
     * @return La atracción cultural creada.
     * @throws DatosInvalidosException si los datos son inválidos.
     * @example
     * <pre>
     *     AtraccionCultural a = servicio.crearAtraccionCultural("C1", "Teatro de Marionetas", "Zona B", 50, 2, 5, List.of(), NivelExclusividad.FAMILIAR);
     * </pre>
     */
    public AtraccionCultural crearAtraccionCultural(String id, String nombre, String ubicacion, int cupoMaximo,
                                                  int empleadosMinimos, int edadMinima,
                                                  List<CondicionClimatica> climaNoPermitido,
                                                  NivelExclusividad nivelExclusividad) {
        // Las validaciones ahora están principalmente en el constructor de AtraccionCultural
         if (nivelExclusividad == null) {
             throw new DatosInvalidosException("El nivel de exclusividad no puede ser null para Atracción Cultural.");
         }
        AtraccionCultural nueva = new AtraccionCultural(id, nombre, ubicacion, cupoMaximo, empleadosMinimos,
                edadMinima, climaNoPermitido);
        nueva.setNivelExclusividad(nivelExclusividad); // Establecer nivel de exclusividad
        elementoParqueRepository.save(nueva);
        return nueva;
    }

    /**
     * Crea y guarda un nuevo espectáculo.
     *
     * <b>Precondiciones:</b>
     * <ul>
     *   <li>Todos los parámetros deben ser válidos y no nulos.</li>
     * </ul>
     *
     * <b>Poscondiciones:</b>
     * <ul>
     *   <li>El espectáculo queda registrado en el sistema.</li>
     * </ul>
     *
     * @param id ID único del espectáculo.
     * @param nombre Nombre del espectáculo.
     * @param ubicacion Ubicación física.
     * @param cupoMaximo Cupo máximo permitido.
     * @param descripcion Descripción del espectáculo.
     * @param horarios Lista de horarios.
     * @param climaNoPermitido Condiciones climáticas no permitidas.
     * @return El espectáculo creado.
     * @throws DatosInvalidosException si los datos son inválidos.
     * @example
     * <pre>
     *     Espectaculo e = servicio.crearEspectaculo("E1", "Show de Magia", "Zona C", 100, "Magia para toda la familia", List.of(horario), List.of());
     * </pre>
     */
    public Espectaculo crearEspectaculo(String id, String nombre, String ubicacion, int cupoMaximo,
                                      String descripcion, List<RangoFechaHora> horarios,
                                      List<CondicionClimatica> climaNoPermitido) {
        // Las validaciones ahora están principalmente en el constructor de Espectaculo
        Espectaculo nuevo = new Espectaculo(id, nombre, ubicacion, cupoMaximo,
                descripcion, horarios, climaNoPermitido);
        // Los espectáculos no tienen nivel de exclusividad según los requisitos.
        elementoParqueRepository.save(nuevo);
        return nuevo;
    }

    /**
     * Busca una atracción por su ID.
     *
     * <b>Precondiciones:</b> El ID no debe ser nulo ni vacío.
     * <b>Poscondiciones:</b> Devuelve un Optional con la atracción encontrada o vacío si no existe.
     *
     * @param id ID de la atracción.
     * @return Optional con la atracción encontrada.
     * @example
     * <pre>
     *     Optional<Atraccion> a = servicio.consultarAtraccionPorId("A1");
     * </pre>
     */
    public Optional<Atraccion> consultarAtraccionPorId(String id) {
         if (id == null || id.trim().isEmpty()) {
             return Optional.empty();
         }
        return elementoParqueRepository.findAtraccionById(id);
    }
    
    /**
     * Busca un espectáculo por su ID.
     *
     * <b>Precondiciones:</b> El ID no debe ser nulo ni vacío.
     * <b>Poscondiciones:</b> Devuelve un Optional con el espectáculo encontrado o vacío si no existe.
     *
     * @param id ID del espectáculo.
     * @return Optional con el espectáculo encontrado.
     * @example
     * <pre>
     *     Optional<Espectaculo> e = servicio.consultarEspectaculoPorId("E1");
     * </pre>
     */
    public Optional<Espectaculo> consultarEspectaculoPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        return elementoParqueRepository.findEspectaculoById(id);
    }
    
    /**
     * Busca un elemento del parque por su ID (puede ser atracción o espectáculo).
     *
     * <b>Precondiciones:</b> El ID no debe ser nulo ni vacío.
     * <b>Poscondiciones:</b> Devuelve un Optional con el elemento encontrado o vacío si no existe.
     *
     * @param id ID del elemento del parque.
     * @return Optional con el elemento encontrado.
     * @example
     * <pre>
     *     Optional<ElementoParque> ep = servicio.consultarElementoPorId("A1");
     * </pre>
     */
    public Optional<ElementoParque> consultarElementoPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        return elementoParqueRepository.findById(id);
    }

    /**
     * Busca una atracción por su nombre.
     *
     * <b>Precondiciones:</b> El nombre no debe ser nulo ni vacío.
     * <b>Poscondiciones:</b> Devuelve un Optional con la atracción encontrada o vacío si no existe.
     *
     * @param nombre Nombre de la atracción.
     * @return Optional con la atracción encontrada.
     * @example
     * <pre>
     *     Optional<Atraccion> a = servicio.consultarAtraccionPorNombre("Montaña Rusa");
     * </pre>
     */
    public Optional<Atraccion> consultarAtraccionPorNombre(String nombre) {
         if (nombre == null || nombre.trim().isEmpty()) {
             return Optional.empty();
         }
        return elementoParqueRepository.findAtraccionByNombre(nombre);
    }
    
    /**
     * Busca un espectáculo por su nombre.
     *
     * <b>Precondiciones:</b> El nombre no debe ser nulo ni vacío.
     * <b>Poscondiciones:</b> Devuelve un Optional con el espectáculo encontrado o vacío si no existe.
     *
     * @param nombre Nombre del espectáculo.
     * @return Optional con el espectáculo encontrado.
     * @example
     * <pre>
     *     Optional<Espectaculo> e = servicio.consultarEspectaculoPorNombre("Show de Magia");
     * </pre>
     */
    public Optional<Espectaculo> consultarEspectaculoPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return Optional.empty();
        }
        return elementoParqueRepository.findEspectaculoByNombre(nombre);
    }

    /**
     * Obtiene todas las atracciones registradas en el sistema.
     *
     * <b>Precondiciones:</b> Ninguna.
     * <b>Poscondiciones:</b> Devuelve una lista de todas las atracciones.
     *
     * @return Lista de todas las atracciones.
     * @example
     * <pre>
     *     List<Atraccion> lista = servicio.consultarTodasLasAtracciones();
     * </pre>
     */
    public List<Atraccion> consultarTodasLasAtracciones() {
        return elementoParqueRepository.findAllAtracciones();
    }
    
    /**
     * Obtiene todos los elementos del parque (atracciones y espectáculos).
     *
     * <b>Precondiciones:</b> Ninguna.
     * <b>Poscondiciones:</b> Devuelve una lista de todos los elementos del parque.
     *
     * @return Lista de todos los elementos del parque.
     * @example
     * <pre>
     *     List<ElementoParque> lista = servicio.consultarTodosLosElementos();
     * </pre>
     */
    public List<ElementoParque> consultarTodosLosElementos() {
        return elementoParqueRepository.findAll();
    }

    /**
     * Obtiene todas las atracciones mecánicas.
     *
     * <b>Precondiciones:</b> Ninguna.
     * <b>Poscondiciones:</b> Devuelve una lista de atracciones mecánicas.
     *
     * @return Lista de atracciones mecánicas.
     * @example
     * <pre>
     *     List<AtraccionMecanica> lista = servicio.consultarAtraccionesMecanicas();
     * </pre>
     */
    public List<AtraccionMecanica> consultarAtraccionesMecanicas() {
        return elementoParqueRepository.findAllMecanicas();
    }

    /**
     * Obtiene todas las atracciones culturales.
     *
     * <b>Precondiciones:</b> Ninguna.
     * <b>Poscondiciones:</b> Devuelve una lista de atracciones culturales.
     *
     * @return Lista de atracciones culturales.
     * @example
     * <pre>
     *     List<AtraccionCultural> lista = servicio.consultarAtraccionesCulturales();
     * </pre>
     */
    public List<AtraccionCultural> consultarAtraccionesCulturales() {
        return elementoParqueRepository.findAllCulturales();
    }

    /**
     * Obtiene todos los espectáculos registrados.
     *
     * <b>Precondiciones:</b> Ninguna.
     * <b>Poscondiciones:</b> Devuelve una lista de espectáculos.
     *
     * @return Lista de espectáculos.
     * @example
     * <pre>
     *     List<Espectaculo> lista = servicio.consultarEspectaculos();
     * </pre>
     */
    public List<Espectaculo> consultarEspectaculos() {
        return elementoParqueRepository.findAllEspectaculos();
    }

    /**
     * Actualiza la información de una atracción existente.
     *
     * <b>Precondiciones:</b> La atracción no debe ser nula y debe existir en el sistema.
     * <b>Poscondiciones:</b> La información de la atracción queda actualizada.
     *
     * @param atraccion La atracción con la información actualizada.
     * @throws AtraccionNoEncontradaException si la atracción no existe.
     * @throws DatosInvalidosException si la atracción es null.
     * @example
     * <pre>
     *     servicio.actualizarAtraccion(atraccion);
     * </pre>
     */
    public void actualizarAtraccion(Atraccion atraccion) {
         if (atraccion == null) {
             throw new DatosInvalidosException("La atracción a actualizar no puede ser null.");
         }
        // Verificar que la atracción existe antes de intentar guardar
        elementoParqueRepository.findAtraccionById(atraccion.getId())
                .orElseThrow(() -> new AtraccionNoEncontradaException("No se puede actualizar una atracción que no existe: " + atraccion.getId()));

        elementoParqueRepository.save(atraccion);
    }
    
    /**
     * Actualiza la información de un espectáculo existente.
     *
     * <b>Precondiciones:</b> El espectáculo no debe ser nulo y debe existir en el sistema.
     * <b>Poscondiciones:</b> La información del espectáculo queda actualizada.
     *
     * @param espectaculo El espectáculo con la información actualizada.
     * @throws AtraccionNoEncontradaException si el espectáculo no existe.
     * @throws DatosInvalidosException si el espectáculo es null.
     * @example
     * <pre>
     *     servicio.actualizarEspectaculo(espectaculo);
     * </pre>
     */
    public void actualizarEspectaculo(Espectaculo espectaculo) {
        if (espectaculo == null) {
            throw new DatosInvalidosException("El espectáculo a actualizar no puede ser null.");
        }
        // Verificar que el espectáculo existe antes de intentar guardar
        elementoParqueRepository.findEspectaculoById(espectaculo.getId())
                .orElseThrow(() -> new AtraccionNoEncontradaException("No se puede actualizar un espectáculo que no existe: " + espectaculo.getId()));

        elementoParqueRepository.save(espectaculo);
    }

    /**
     * Establece o actualiza el período de temporada para un elemento del parque.
     *
     * <b>Precondiciones:</b> El ID del elemento y las fechas no deben ser nulos.
     * <b>Poscondiciones:</b> El elemento queda con la temporada definida.
     *
     * @param idElemento ID del elemento del parque.
     * @param fechaInicio Inicio de la temporada.
     * @param fechaFin Fin de la temporada.
     * @throws AtraccionNoEncontradaException si el elemento no existe.
     * @throws DatosInvalidosException si las fechas son inválidas.
     * @example
     * <pre>
     *     servicio.definirTemporadaElemento("A1", LocalDateTime.of(2025,6,1,0,0), LocalDateTime.of(2025,6,30,23,59));
     * </pre>
     */
    public void definirTemporadaElemento(String idElemento, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        Optional<ElementoParque> elementoOpt = elementoParqueRepository.findById(idElemento);
        if (elementoOpt.isEmpty()) {
            throw new AtraccionNoEncontradaException(idElemento);
        }
        
        ElementoParque elemento = elementoOpt.get();
        elemento.setTemporada(fechaInicio, fechaFin);
        
        // Use instanceof to determine the correct save method
        if (elemento instanceof AtraccionMecanica) {
            elementoParqueRepository.save((AtraccionMecanica) elemento);
        } else if (elemento instanceof AtraccionCultural) {
            elementoParqueRepository.save((AtraccionCultural) elemento);
        } else if (elemento instanceof Espectaculo) {
            elementoParqueRepository.save((Espectaculo) elemento);
        } else {
            throw new IllegalArgumentException("Tipo de elemento desconocido: " + elemento.getClass().getName());
        }
    }

    /**
     * Elimina una atracción por su ID.
     *
     * <b>Precondiciones:</b> El ID de la atracción no debe ser nulo ni vacío.
     * <b>Poscondiciones:</b> La atracción queda eliminada del sistema.
     *
     * @param idAtraccion ID de la atracción a eliminar.
     * @throws AtraccionNoEncontradaException si la atracción no existe.
     * @example
     * <pre>
     *     servicio.eliminarAtraccion("A1");
     * </pre>
     */
    public void eliminarAtraccion(String idAtraccion) {
         // Opcional: verificar si existe antes de borrar para lanzar excepción
         // if (elementoParqueRepository.findById(idAtraccion).isEmpty()) {
         //     throw new AtraccionNoEncontradaException(idAtraccion);
         // }
        elementoParqueRepository.deleteById(idAtraccion);
    }
    
    /**
     * Elimina un elemento del parque por su ID.
     *
     * <b>Precondiciones:</b> El ID del elemento no debe ser nulo ni vacío.
     * <b>Poscondiciones:</b> El elemento queda eliminado del sistema.
     *
     * @param idElemento ID del elemento a eliminar.
     * @throws AtraccionNoEncontradaException si el elemento no existe.
     * @example
     * <pre>
     *     servicio.eliminarElemento("E1");
     * </pre>
     */
    public void eliminarElemento(String idElemento) {
        // Optional: verificar si existe antes de borrar para lanzar excepción
        if (elementoParqueRepository.findById(idElemento).isEmpty()) {
            throw new AtraccionNoEncontradaException(idElemento);
        }
        elementoParqueRepository.deleteById(idElemento);
    }

    /**
     * Consulta todas las atracciones por nivel de exclusividad.
     *
     * <b>Precondiciones:</b> El nivel de exclusividad no debe ser nulo.
     * <b>Poscondiciones:</b> Devuelve una lista de atracciones con el nivel indicado.
     *
     * @param nivelExclusividad Nivel de exclusividad a filtrar.
     * @return Lista de atracciones con ese nivel.
     * @throws DatosInvalidosException si el nivel es nulo.
     * @example
     * <pre>
     *     List<Atraccion> oro = servicio.consultarAtraccionesPorExclusividad(NivelExclusividad.ORO);
     * </pre>
     */
    public List<Atraccion> consultarAtraccionesPorExclusividad(NivelExclusividad nivelExclusividad) {
        if (nivelExclusividad == null) {
            throw new DatosInvalidosException("El nivel de exclusividad no puede ser nulo.");
        }
        return consultarTodasLasAtracciones().stream()
                .filter(a -> nivelExclusividad.equals(a.getNivelExclusividad()))
                .toList();
    }

    /**
     * Consulta todas las atracciones mecánicas por nivel de riesgo.
     *
     * <b>Precondiciones:</b> El nivel de riesgo no debe ser nulo.
     * <b>Poscondiciones:</b> Devuelve una lista de atracciones mecánicas con el nivel indicado.
     *
     * @param nivelRiesgo Nivel de riesgo a filtrar.
     * @return Lista de atracciones mecánicas con ese nivel de riesgo.
     * @throws DatosInvalidosException si el nivel es nulo.
     * @example
     * <pre>
     *     List<AtraccionMecanica> alto = servicio.consultarAtraccionesMecanicasPorRiesgo(NivelRiesgo.ALTO);
     * </pre>
     */
    public List<AtraccionMecanica> consultarAtraccionesMecanicasPorRiesgo(NivelRiesgo nivelRiesgo) {
        if (nivelRiesgo == null) {
            throw new DatosInvalidosException("El nivel de riesgo no puede ser nulo.");
        }
        return consultarAtraccionesMecanicas().stream()
                .filter(a -> nivelRiesgo.equals(a.getNivelRiesgo()))
                .toList();
    }

    /**
     * Consulta todos los elementos del parque restringidos por una condición climática.
     *
     * <b>Precondiciones:</b> El clima no debe ser nulo.
     * <b>Poscondiciones:</b> Devuelve una lista de elementos restringidos por ese clima.
     *
     * @param clima Condición climática a filtrar.
     * @return Lista de elementos restringidos por ese clima.
     * @throws DatosInvalidosException si el clima es nulo.
     * @example
     * <pre>
     *     List<ElementoParque> restringidos = servicio.consultarElementosPorClima(CondicionClimatica.TORMENTA);
     * </pre>
     */
    public List<ElementoParque> consultarElementosPorClima(CondicionClimatica clima) {
        if (clima == null) {
            throw new DatosInvalidosException("El clima no puede ser nulo.");
        }
        return consultarTodosLosElementos().stream()
                .filter(e -> e.getClimaNoPermitido().contains(clima))
                .toList();
    }

    /**
     * Elimina todas las atracciones de un nivel de exclusividad dado.
     *
     * <b>Precondiciones:</b> El nivel de exclusividad no debe ser nulo.
     * <b>Poscondiciones:</b> Todas las atracciones de ese nivel quedan eliminadas.
     *
     * @param nivelExclusividad Nivel de exclusividad a eliminar.
     * @throws DatosInvalidosException si el nivel es nulo.
     * @example
     * <pre>
     *     servicio.eliminarAtraccionesPorExclusividad(NivelExclusividad.FAMILIAR);
     * </pre>
     */
    public void eliminarAtraccionesPorExclusividad(NivelExclusividad nivelExclusividad) {
        if (nivelExclusividad == null) {
            throw new DatosInvalidosException("El nivel de exclusividad no puede ser nulo.");
        }
        consultarTodasLasAtracciones().stream()
                .filter(a -> nivelExclusividad.equals(a.getNivelExclusividad()))
                .forEach(a -> elementoParqueRepository.deleteById(a.getId()));
    }

    /**
     * Elimina todos los espectáculos que tengan una condición climática restringida dada.
     *
     * <b>Precondiciones:</b> El clima no debe ser nulo.
     * <b>Poscondiciones:</b> Todos los espectáculos restringidos por ese clima quedan eliminados.
     *
     * @param clima Condición climática a eliminar.
     * @throws DatosInvalidosException si el clima es nulo.
     * @example
     * <pre>
     *     servicio.eliminarEspectaculosPorClima(CondicionClimatica.TORMENTA);
     * </pre>
     */
    public void eliminarEspectaculosPorClima(CondicionClimatica clima) {
        if (clima == null) {
            throw new DatosInvalidosException("El clima no puede ser nulo.");
        }
        consultarEspectaculos().stream()
                .filter(e -> e.getClimaNoPermitido().contains(clima))
                .forEach(e -> elementoParqueRepository.deleteById(e.getId()));
    }
}

package dominio.elementoparque;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de persistencia para las atracciones y espectáculos.
 * Sigue el patrón Repository para abstraer el acceso a datos.
 */
public interface ElementoParqueRepository {
    /**
     * Guarda una atracción en el repositorio.
     *
     * @param atraccion La atracción a guardar.
     * @return La atracción guardada.
     */
    Atraccion save(Atraccion atraccion);
    
    /**
     * Guarda un espectáculo en el repositorio.
     *
     * @param espectaculo El espectáculo a guardar.
     * @return El espectáculo guardado.
     */
    Espectaculo save(Espectaculo espectaculo);
    
    /**
     * Busca una atracción por su ID.
     *
     * @param id El ID a buscar.
     * @return Un Optional con la atracción encontrada, o vacío si no existe.
     */
    Optional<Atraccion> findAtraccionById(String id);
    
    /**
     * Busca un espectáculo por su ID.
     *
     * @param id El ID a buscar.
     * @return Un Optional con el espectáculo encontrado, o vacío si no existe.
     */
    Optional<Espectaculo> findEspectaculoById(String id);
    
    /**
     * Busca un elemento del parque por su ID (puede ser atracción o espectáculo).
     *
     * @param id El ID a buscar.
     * @return Un Optional con el elemento encontrado, o vacío si no existe.
     */
    Optional<ElementoParque> findById(String id);

    /**
     * Busca una atracción por su nombre.
     *
     * @param nombre El nombre a buscar.
     * @return Un Optional con la atracción encontrada.
     */
    Optional<Atraccion> findAtraccionByNombre(String nombre);
    
    /**
     * Busca un espectáculo por su nombre.
     *
     * @param nombre El nombre a buscar.
     * @return Un Optional con el espectáculo encontrado.
     */
    Optional<Espectaculo> findEspectaculoByNombre(String nombre);
    
    /**
     * Busca cualquier elemento por su nombre.
     *
     * @param nombre El nombre a buscar.
     * @return Un Optional con el elemento encontrado.
     */
    Optional<ElementoParque> findByNombre(String nombre);

    /**
     * Devuelve todas las atracciones almacenadas.
     *
     * @return Una lista con todas las atracciones.
     */
    List<Atraccion> findAllAtracciones();
    
    /**
     * Devuelve todos los elementos del parque (atracciones y espectáculos).
     *
     * @return Una lista con todos los elementos.
     */
    List<ElementoParque> findAll();

    /**
     * Elimina una atracción por su ID.
     *
     * @param id El ID de la atracción a eliminar.
     */
    void deleteById(String id);
    
    /**
     * Devuelve todas las atracciones mecánicas.
     *
     * @return Una lista con todas las atracciones mecánicas.
     */
    List<AtraccionMecanica> findAllMecanicas();

    /**
     * Devuelve todas las atracciones culturales.
     *
     * @return Una lista con todas las atracciones culturales.
     */
    List<AtraccionCultural> findAllCulturales();

    /**
     * Devuelve todos los espectáculos.
     *
     * @return Una lista con todos los espectáculos.
     */
    List<Espectaculo> findAllEspectaculos();
}

package infraestructura.mapper;

import dominio.elementoparque.ElementoParque;
import infraestructura.dto.ElementoParqueDTO;

/**
 * Interfaz base para mappers de ElementoParque.
 * 
 * @param <E> Tipo de entidad de dominio
 * @param <D> Tipo de DTO
 */
public interface ElementoParqueMapper<E extends ElementoParque, D extends ElementoParqueDTO> {
    
    /**
     * Convierte una entidad de dominio a DTO.
     * 
     * @param entity Entidad de dominio
     * @return DTO correspondiente
     */
    D toDto(E entity);
    
    /**
     * Convierte un DTO a entidad de dominio.
     * 
     * @param dto DTO
     * @return Entidad de dominio correspondiente
     */
    E toEntity(D dto);
}

package infraestructura.mapper;

import dominio.elementoparque.AtraccionCultural;
import dominio.elementoparque.NivelExclusividad;
import dominio.empleado.Capacitacion;
import dominio.util.CondicionClimatica;
import infraestructura.dto.AtraccionCulturalDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de mapper para AtraccionCultural.
 */
public class AtraccionCulturalMapper implements ElementoParqueMapper<AtraccionCultural, AtraccionCulturalDTO> {

    @Override
    public AtraccionCulturalDTO toDto(AtraccionCultural entity) {
        if (entity == null) return null;
        
        AtraccionCulturalDTO dto = new AtraccionCulturalDTO();
        
        // Datos básicos de ElementoParque
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setCupoMaximo(entity.getCupoMaximo());
        dto.setFechaInicioTemporada(entity.getFechaInicioTemporada());
        dto.setFechaFinTemporada(entity.getFechaFinTemporada());
        dto.setClimaNoPermitido(entity.getClimaNoPermitido().stream()
                .map(CondicionClimatica::name)
                .collect(Collectors.toList()));
        
        // Datos de Atraccion
        dto.setUbicacion(entity.getUbicacion());
        dto.setEmpleadosMinimos(entity.getEmpleadosMinimos());
        dto.setNivelExclusividad(entity.getNivelExclusividad() != null ? 
                entity.getNivelExclusividad().name() : null);
        
        // Empleados asignados (guardamos solo IDs)
        dto.setEmpleadosAsignados(entity.getEmpleadosAsignados().stream()
                .map(e -> e.getIdentificacion())
                .collect(Collectors.toList()));
        
        // Capacitaciones requeridas
        dto.setCapacitacionesRequeridas(entity.getCapacitacionesRequeridas().stream()
                .map(Capacitacion::name)
                .collect(Collectors.toList()));
        
        // Datos específicos de AtraccionCultural
        dto.setEdadMinima(entity.getEdadMinima());
        
        return dto;
    }

    @Override
    public AtraccionCultural toEntity(AtraccionCulturalDTO dto) {
        if (dto == null) return null;
        
        // Conversion de enumeraciones
        NivelExclusividad nivelExclusividad = dto.getNivelExclusividad() != null ? 
                NivelExclusividad.valueOf(dto.getNivelExclusividad()) : null;
        
        // Conversion de clima no permitido
        List<CondicionClimatica> climaNoPermitido = dto.getClimaNoPermitido().stream()
                .map(CondicionClimatica::valueOf)
                .collect(Collectors.toList());
        
        // Crear la entidad
        AtraccionCultural entity = new AtraccionCultural(
            dto.getId(),
            dto.getNombre(),
            dto.getUbicacion(),
            dto.getCupoMaximo(),
            dto.getEmpleadosMinimos(),
            dto.getEdadMinima(),
            climaNoPermitido
        );
        
        // Establecer nivel de exclusividad
        if (nivelExclusividad != null) {
            entity.setNivelExclusividad(nivelExclusividad);
        }
        
        // Configurar temporada si está definida
        if (dto.getFechaInicioTemporada() != null && dto.getFechaFinTemporada() != null) {
            entity.setTemporada(dto.getFechaInicioTemporada(), dto.getFechaFinTemporada());
        }
        
        // Los empleados asignados deben manejarse externamente ya que requerimos
        // instancias completas de Empleado, no solo IDs
        
        return entity;
    }
}

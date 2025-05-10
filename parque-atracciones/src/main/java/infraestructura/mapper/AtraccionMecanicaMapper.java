package infraestructura.mapper;

import dominio.elementoparque.AtraccionMecanica;
import dominio.elementoparque.NivelExclusividad;
import dominio.elementoparque.NivelRiesgo;
import dominio.empleado.Capacitacion;
import dominio.util.CondicionClimatica;
import infraestructura.dto.AtraccionMecanicaDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de mapper para AtraccionMecanica.
 */
public class AtraccionMecanicaMapper implements ElementoParqueMapper<AtraccionMecanica, AtraccionMecanicaDTO> {

    @Override
    public AtraccionMecanicaDTO toDto(AtraccionMecanica entity) {
        if (entity == null) return null;
        
        AtraccionMecanicaDTO dto = new AtraccionMecanicaDTO();
        
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
        
        // Datos específicos de AtraccionMecanica
        dto.setNivelRiesgo(entity.getNivelRiesgo().name());
        dto.setRestriccionAlturaMinima(entity.getRestriccionAlturaMinima());
        dto.setRestriccionAlturaMaxima(entity.getRestriccionAlturaMaxima());
        dto.setRestriccionPesoMinimo(entity.getRestriccionPesoMinimo());
        dto.setRestriccionPesoMaximo(entity.getRestriccionPesoMaximo());
        dto.setContraindicacionesSalud(new ArrayList<>(entity.getContraindicacionesSalud()));
        dto.setRestriccionesSalud(new ArrayList<>(entity.getRestriccionesSalud()));
        dto.setCapacitacionEspecifica(entity.getCapacitacionEspecifica() != null ? 
                entity.getCapacitacionEspecifica().name() : null);
        
        return dto;
    }

    @Override
    public AtraccionMecanica toEntity(AtraccionMecanicaDTO dto) {
        if (dto == null) return null;
        
        // Conversion de enumeraciones
        NivelExclusividad nivelExclusividad = dto.getNivelExclusividad() != null ? 
                NivelExclusividad.valueOf(dto.getNivelExclusividad()) : null;
        NivelRiesgo nivelRiesgo = NivelRiesgo.valueOf(dto.getNivelRiesgo());
        Capacitacion capacitacionEspecifica = dto.getCapacitacionEspecifica() != null ? 
                Capacitacion.valueOf(dto.getCapacitacionEspecifica()) : null;
        
        // Conversion de clima no permitido
        List<CondicionClimatica> climaNoPermitido = dto.getClimaNoPermitido().stream()
                .map(CondicionClimatica::valueOf)
                .collect(Collectors.toList());
        
        // Crear la entidad
        AtraccionMecanica entity = new AtraccionMecanica(
            dto.getId(),
            dto.getNombre(),
            dto.getUbicacion(),
            dto.getCupoMaximo(),
            dto.getEmpleadosMinimos(),
            nivelExclusividad,
            nivelRiesgo,
            dto.getRestriccionAlturaMinima(),
            dto.getRestriccionAlturaMaxima(),
            dto.getRestriccionPesoMinimo(),
            dto.getRestriccionPesoMaximo(),
            dto.getContraindicacionesSalud(),
            dto.getRestriccionesSalud(),
            climaNoPermitido,
            capacitacionEspecifica
        );
        
        // Configurar temporada si está definida
        if (dto.getFechaInicioTemporada() != null && dto.getFechaFinTemporada() != null) {
            entity.setTemporada(dto.getFechaInicioTemporada(), dto.getFechaFinTemporada());
        }
        
        // Los empleados asignados deben manejarse externamente ya que requerimos
        // instancias completas de Empleado, no solo IDs
        
        return entity;
    }
}

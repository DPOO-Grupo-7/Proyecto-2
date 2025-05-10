package infraestructura.mapper;

import dominio.elementoparque.Espectaculo;
import dominio.util.CondicionClimatica;
import dominio.util.RangoFechaHora;
import infraestructura.dto.EspectaculoDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementación de mapper para Espectaculo.
 */
public class EspectaculoMapper implements ElementoParqueMapper<Espectaculo, EspectaculoDTO> {
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public EspectaculoDTO toDto(Espectaculo entity) {
        if (entity == null) return null;
        
        EspectaculoDTO dto = new EspectaculoDTO();
        
        // Datos básicos de ElementoParque
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setCupoMaximo(entity.getCupoMaximo());
        dto.setFechaInicioTemporada(entity.getFechaInicioTemporada());
        dto.setFechaFinTemporada(entity.getFechaFinTemporada());
        dto.setClimaNoPermitido(entity.getClimaNoPermitido().stream()
                .map(CondicionClimatica::name)
                .collect(Collectors.toList()));
        
        // Datos específicos de Espectaculo
        dto.setDescripcion(entity.getDescripcion());
        dto.setUbicacion(entity.getUbicacion());
        
        // Convertir RangoFechaHora a Map<String, String>
        List<Map<String, String>> horariosDto = new ArrayList<>();
        for (RangoFechaHora rango : entity.getHorarios()) {
            Map<String, String> horarioMap = new HashMap<>();
            horarioMap.put("inicio", rango.getInicio().format(formatter));
            horarioMap.put("fin", rango.getFin().format(formatter));
            horariosDto.add(horarioMap);
        }
        dto.setHorarios(horariosDto);
        
        return dto;
    }

    @Override
    public Espectaculo toEntity(EspectaculoDTO dto) {
        if (dto == null) return null;
        
        // Conversion de clima no permitido
        List<CondicionClimatica> climaNoPermitido = dto.getClimaNoPermitido().stream()
                .map(CondicionClimatica::valueOf)
                .collect(Collectors.toList());
        
        // Convertir horarios de Map<String, String> a RangoFechaHora
        List<RangoFechaHora> horarios = new ArrayList<>();
        for (Map<String, String> horarioMap : dto.getHorarios()) {
            LocalDateTime inicio = LocalDateTime.parse(horarioMap.get("inicio"), formatter);
            LocalDateTime fin = LocalDateTime.parse(horarioMap.get("fin"), formatter);
            horarios.add(new RangoFechaHora(inicio, fin));
        }
        
        // Crear la entidad
        Espectaculo entity = new Espectaculo(
            dto.getId(),
            dto.getNombre(),
            dto.getUbicacion(),
            dto.getCupoMaximo(),
            dto.getDescripcion(),
            horarios,
            climaNoPermitido
        );
        
        // Configurar temporada si está definida
        if (dto.getFechaInicioTemporada() != null && dto.getFechaFinTemporada() != null) {
            entity.setTemporada(dto.getFechaInicioTemporada(), dto.getFechaFinTemporada());
        }
        
        return entity;
    }
}

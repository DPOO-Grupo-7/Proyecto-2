package infraestructura.persistencia;

import com.google.gson.reflect.TypeToken;
import dominio.elementoparque.*;
import infraestructura.dto.AtraccionCulturalDTO;
import infraestructura.dto.AtraccionMecanicaDTO;
import infraestructura.dto.ElementoParqueDTO;
import infraestructura.dto.EspectaculoDTO;
import infraestructura.mapper.AtraccionCulturalMapper;
import infraestructura.mapper.AtraccionMecanicaMapper;
import infraestructura.mapper.EspectaculoMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación de ElementoParqueRepository usando persistencia JSON.
 */
public class JsonElementoParqueRepository implements ElementoParqueRepository {
    
    private static final String ELEMENTOS_FILE = "elementos_parque";
    
    private final AtraccionMecanicaMapper atraccionMecanicaMapper = new AtraccionMecanicaMapper();
    private final AtraccionCulturalMapper atraccionCulturalMapper = new AtraccionCulturalMapper();
    private final EspectaculoMapper espectaculoMapper = new EspectaculoMapper();
    
    // Cache de elementos para evitar leer el archivo repetidamente
    private List<ElementoParqueDTO> elementosCache;
    
    /**
     * Obtiene todos los elementos del archivo JSON.
     * @return Lista de DTOs de elementos del parque
     */
    private List<ElementoParqueDTO> getElementos() {
        if (elementosCache == null) {
            TypeToken<List<ElementoParqueDTO>> typeToken = new TypeToken<>() {};
            List<ElementoParqueDTO> elementos = JsonUtil.readFromFileAbsolute(JsonUtil.getDataFilePath(ELEMENTOS_FILE + ".json"), typeToken);
            elementosCache = elementos;
        }
        return elementosCache;
    }
    
    /**
     * Guarda todos los elementos en el archivo JSON.
     * @param elementos Lista de DTOs a guardar
     */
    private void saveElementos(List<ElementoParqueDTO> elementos) {
        JsonUtil.writeToFileAbsolute(JsonUtil.getDataFilePath(ELEMENTOS_FILE + ".json"), elementos);
        elementosCache = elementos;
    }

    @Override
    public Atraccion save(Atraccion atraccion) {
        if (atraccion == null) return null;
        
        List<ElementoParqueDTO> elementos = getElementos();
        ElementoParqueDTO dto;
        
        // Convertir la entidad a DTO según su tipo
        if (atraccion instanceof AtraccionMecanica) {
            dto = atraccionMecanicaMapper.toDto((AtraccionMecanica) atraccion);
        } else if (atraccion instanceof AtraccionCultural) {
            dto = atraccionCulturalMapper.toDto((AtraccionCultural) atraccion);
        } else {
            throw new IllegalArgumentException("Tipo de atracción desconocido: " + atraccion.getClass().getName());
        }
        
        // Buscar si ya existe para actualizar o añadir nuevo
        boolean found = false;
        for (int i = 0; i < elementos.size(); i++) {
            if (elementos.get(i).getId().equals(dto.getId())) {
                elementos.set(i, dto);
                found = true;
                break;
            }
        }
        
        if (!found) {
            elementos.add(dto);
        }
        
        saveElementos(elementos);
        return atraccion;
    }

    @Override
    public Espectaculo save(Espectaculo espectaculo) {
        if (espectaculo == null) return null;
        
        List<ElementoParqueDTO> elementos = getElementos();
        EspectaculoDTO dto = espectaculoMapper.toDto(espectaculo);
        
        // Buscar si ya existe para actualizar o añadir nuevo
        boolean found = false;
        for (int i = 0; i < elementos.size(); i++) {
            if (elementos.get(i).getId().equals(dto.getId())) {
                elementos.set(i, dto);
                found = true;
                break;
            }
        }
        
        if (!found) {
            elementos.add(dto);
        }
        
        saveElementos(elementos);
        return espectaculo;
    }

    @Override
    public Optional<Atraccion> findAtraccionById(String id) {
        if (id == null) return Optional.empty();
        
        return getElementos().stream()
                .filter(dto -> id.equals(dto.getId()) && !dto.getTipo().equals("Espectaculo"))
                .findFirst()
                .map(this::convertDtoToAtraccion);
    }

    @Override
    public Optional<Espectaculo> findEspectaculoById(String id) {
        if (id == null) return Optional.empty();
        
        return getElementos().stream()
                .filter(dto -> id.equals(dto.getId()) && dto.getTipo().equals("Espectaculo"))
                .findFirst()
                .map(dto -> espectaculoMapper.toEntity((EspectaculoDTO) dto));
    }

    @Override
    public Optional<ElementoParque> findById(String id) {
        if (id == null) return Optional.empty();
        
        return getElementos().stream()
                .filter(dto -> id.equals(dto.getId()))
                .findFirst()
                .map(this::convertDtoToElemento);
    }

    @Override
    public Optional<Atraccion> findAtraccionByNombre(String nombre) {
        if (nombre == null) return Optional.empty();
        
        return getElementos().stream()
                .filter(dto -> nombre.equals(dto.getNombre()) && !dto.getTipo().equals("Espectaculo"))
                .findFirst()
                .map(this::convertDtoToAtraccion);
    }

    @Override
    public Optional<Espectaculo> findEspectaculoByNombre(String nombre) {
        if (nombre == null) return Optional.empty();
        
        return getElementos().stream()
                .filter(dto -> nombre.equals(dto.getNombre()) && dto.getTipo().equals("Espectaculo"))
                .findFirst()
                .map(dto -> espectaculoMapper.toEntity((EspectaculoDTO) dto));
    }

    @Override
    public Optional<ElementoParque> findByNombre(String nombre) {
        if (nombre == null) return Optional.empty();
        
        return getElementos().stream()
                .filter(dto -> nombre.equals(dto.getNombre()))
                .findFirst()
                .map(this::convertDtoToElemento);
    }

    @Override
    public List<Atraccion> findAllAtracciones() {
        return getElementos().stream()
                .filter(dto -> !dto.getTipo().equals("Espectaculo"))
                .map(this::convertDtoToAtraccion)
                .collect(Collectors.toList());
    }

    @Override
    public List<ElementoParque> findAll() {
        return getElementos().stream()
                .map(this::convertDtoToElemento)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        if (id == null) return;
        
        List<ElementoParqueDTO> elementos = getElementos();
        List<ElementoParqueDTO> filtrados = elementos.stream()
                .filter(dto -> !id.equals(dto.getId()))
                .collect(Collectors.toList());
        
        if (filtrados.size() < elementos.size()) {
            saveElementos(filtrados);
        }
    }

    @Override
    public List<AtraccionMecanica> findAllMecanicas() {
        return getElementos().stream()
                .filter(dto -> dto.getTipo().equals("AtraccionMecanica"))
                .map(dto -> atraccionMecanicaMapper.toEntity((AtraccionMecanicaDTO) dto))
                .collect(Collectors.toList());
    }

    @Override
    public List<AtraccionCultural> findAllCulturales() {
        return getElementos().stream()
                .filter(dto -> dto.getTipo().equals("AtraccionCultural"))
                .map(dto -> atraccionCulturalMapper.toEntity((AtraccionCulturalDTO) dto))
                .collect(Collectors.toList());
    }

    @Override
    public List<Espectaculo> findAllEspectaculos() {
        return getElementos().stream()
                .filter(dto -> dto.getTipo().equals("Espectaculo"))
                .map(dto -> espectaculoMapper.toEntity((EspectaculoDTO) dto))
                .collect(Collectors.toList());
    }
    
    /**
     * Convierte un DTO a la entidad ElementoParque correspondiente.
     * @param dto DTO a convertir
     * @return Entidad ElementoParque
     */
    private ElementoParque convertDtoToElemento(ElementoParqueDTO dto) {
        if (dto == null) return null;
        
        switch (dto.getTipo()) {
            case "AtraccionMecanica":
                return atraccionMecanicaMapper.toEntity((AtraccionMecanicaDTO) dto);
            case "AtraccionCultural":
                return atraccionCulturalMapper.toEntity((AtraccionCulturalDTO) dto);
            case "Espectaculo":
                return espectaculoMapper.toEntity((EspectaculoDTO) dto);
            default:
                throw new IllegalArgumentException("Tipo de elemento desconocido: " + dto.getTipo());
        }
    }
    
    /**
     * Convierte un DTO a la entidad Atraccion correspondiente.
     * @param dto DTO a convertir
     * @return Entidad Atraccion
     */
    private Atraccion convertDtoToAtraccion(ElementoParqueDTO dto) {
        if (dto == null) return null;
        
        switch (dto.getTipo()) {
            case "AtraccionMecanica":
                return atraccionMecanicaMapper.toEntity((AtraccionMecanicaDTO) dto);
            case "AtraccionCultural":
                return atraccionCulturalMapper.toEntity((AtraccionCulturalDTO) dto);
            default:
                throw new IllegalArgumentException("Tipo de atracción desconocido: " + dto.getTipo());
        }
    }
}

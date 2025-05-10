package infraestructura.persistencia;

import infraestructura.dto.TiqueteDTO;
import infraestructura.mapper.TiqueteMapper;
import dominio.tiquete.Tiquete;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.reflect.TypeToken; // Necesario para la deserialización con Gson

/**
 * Repositorio para la persistencia de tiquetes en archivos JSON.
 *
 * <b>Uso:</b> Permite guardar y cargar tiquetes desde archivos JSON en disco.
 *
 * @author Sistema Parque
 */
public class TiqueteRepositoryJson {
    private final String archivoAbsoluto; // Store the absolute path

    /**
     * Constructor for TiqueteRepositoryJson.
     * @param archivoBaseName The base file name under 'data' (e.g., "tiquetes.json").
     */
    public TiqueteRepositoryJson(String archivoBaseName) {
        // Resolve absolute path using JsonUtil immediately
        this.archivoAbsoluto = JsonUtil.getDataFilePath(archivoBaseName);
    }

    /**
     * Guarda la lista de tiquetes en el archivo JSON.
     *
     * @param tiquetes Lista de tiquetes a guardar.
     */
    public void guardarTiquetes(List<Tiquete> tiquetes) {
        List<TiqueteDTO> dtos = new ArrayList<>();
        for (Tiquete t : tiquetes) {
            dtos.add(TiqueteMapper.toDTO(t));
        }
        // Use the absolute path and JsonUtil's writing method
        JsonUtil.writeToFileAbsolute(archivoAbsoluto, dtos);
    }

    /**
     * Carga la lista de tiquetes desde el archivo JSON.
     * Utiliza un resolvedor de atracciones para reconstruir objetos Tiquete complejos.
     *
     * @param atraccionResolver Función para buscar una Atraccion por su ID.
     * @return Lista de tiquetes cargados.
     */
    public List<Tiquete> cargarTiquetes(java.util.function.Function<String, dominio.elementoparque.Atraccion> atraccionResolver) {
        // Use the absolute path and JsonUtil's reading method
        List<TiqueteDTO> dtos = JsonUtil.readFromFileAbsolute(archivoAbsoluto, new TypeToken<List<TiqueteDTO>>(){});

        List<Tiquete> tiquetes = new ArrayList<>();
        if (dtos != null) {
            for (TiqueteDTO dto : dtos) {
                 try {
                    Tiquete t = TiqueteMapper.fromDTO(dto, atraccionResolver);
                    if (t != null) {
                        tiquetes.add(t);
                    } else {
                         System.err.println("[WARN] TiqueteMapper devolvió null para DTO: " + dto);
                    }
                 } catch (Exception ex) {
                    // Log mapping errors for specific DTOs
                    System.err.println("[ERROR] Error al mapear TiqueteDTO a Tiquete: " + dto + " - Error: " + ex.getMessage());
                    // Optionally log stack trace: ex.printStackTrace();
                 }
            }
        }
        return tiquetes;
    }

     /**
     * Carga la lista de tiquetes desde el archivo JSON (sin resolvedor de atracciones).
     * Advertencia: Puede no reconstruir completamente tiquetes que dependen de atracciones.
     *
     * @return Lista de tiquetes cargados (potencialmente incompletos).
     * @deprecated Usar {@link #cargarTiquetes(java.util.function.Function)} para asegurar la correcta reconstrucción.
     */
    @Deprecated
    public List<Tiquete> cargarTiquetes() {
        return cargarTiquetes(id -> {
            System.err.println("[ERROR] Se necesita Atraccion con ID '" + id + "' pero no se proporcionó resolvedor.");
            return null; // O lanzar una excepción si es preferible
        });
    }
}

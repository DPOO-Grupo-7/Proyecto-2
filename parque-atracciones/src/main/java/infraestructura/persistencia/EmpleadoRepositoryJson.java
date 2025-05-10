package infraestructura.persistencia;

import infraestructura.dto.EmpleadoDTO;
import infraestructura.mapper.EmpleadoMapper;
import dominio.empleado.Empleado;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.reflect.TypeToken;

/**
 * Repositorio para la persistencia de empleados en archivos JSON.
 *
 * <b>Uso:</b> Permite guardar y cargar empleados desde archivos JSON en disco.
 *
 * @author Sistema Parque
 */
public class EmpleadoRepositoryJson {
    private final String archivoAbsoluto; // Store the absolute path

    /**
     * Constructor for EmpleadoRepositoryJson.
     * @param archivoBaseName The base file name under 'data' (e.g., "empleados.json").
     */
    public EmpleadoRepositoryJson(String archivoBaseName) {
        // Resolve absolute path using JsonUtil immediately
        this.archivoAbsoluto = JsonUtil.getDataFilePath(archivoBaseName);
    }

    /**
     * Guarda la lista de empleados en el archivo JSON.
     *
     * @param empleados Lista de empleados a guardar.
     */
    public void guardarEmpleados(List<Empleado> empleados) {
        List<EmpleadoDTO> dtos = new ArrayList<>();
        for (Empleado e : empleados) {
            dtos.add(EmpleadoMapper.toDTO(e));
        }
        // Use the absolute path and JsonUtil's writing method
        JsonUtil.writeToFileAbsolute(archivoAbsoluto, dtos);
    }

    /**
     * Carga la lista de empleados desde el archivo JSON.
     *
     * @return Lista de empleados cargados.
     */
    public List<Empleado> cargarEmpleados() {
        // Use the absolute path and JsonUtil's reading method
        List<EmpleadoDTO> dtos = JsonUtil.readFromFileAbsolute(archivoAbsoluto, new TypeToken<List<EmpleadoDTO>>() {});

        List<Empleado> empleados = new ArrayList<>();
        if (dtos != null) {
            for (EmpleadoDTO dto : dtos) {
                try {
                    Empleado e = EmpleadoMapper.fromDTO(dto);
                    if (e != null) {
                        empleados.add(e);
                    }
                } catch (Exception ex) {
                    // Log mapping errors for specific DTOs
                }
            }
        }
        return empleados;
        // Removed redundant try-catch block as JsonUtil handles file reading errors
    }
}

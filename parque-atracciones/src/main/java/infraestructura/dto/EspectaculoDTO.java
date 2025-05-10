package infraestructura.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO para Espectaculo.
 */
public class EspectaculoDTO extends ElementoParqueDTO {
    private String descripcion;
    private String ubicacion;
    private List<Map<String, String>> horarios; // Inicio y fin de cada horario

    public EspectaculoDTO() {
        setTipo("Espectaculo");
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List<Map<String, String>> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Map<String, String>> horarios) {
        this.horarios = horarios;
    }
}

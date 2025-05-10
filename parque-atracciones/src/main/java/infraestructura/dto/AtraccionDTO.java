package infraestructura.dto;

import java.util.List;

/**
 * DTO base para Atraccion.
 */
public abstract class AtraccionDTO extends ElementoParqueDTO {
    private String ubicacion;
    private int empleadosMinimos;
    private String nivelExclusividad;
    private List<String> capacitacionesRequeridas;
    private List<String> empleadosAsignados; // Solo IDs

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getEmpleadosMinimos() {
        return empleadosMinimos;
    }

    public void setEmpleadosMinimos(int empleadosMinimos) {
        this.empleadosMinimos = empleadosMinimos;
    }

    public String getNivelExclusividad() {
        return nivelExclusividad;
    }

    public void setNivelExclusividad(String nivelExclusividad) {
        this.nivelExclusividad = nivelExclusividad;
    }

    public List<String> getCapacitacionesRequeridas() {
        return capacitacionesRequeridas;
    }

    public void setCapacitacionesRequeridas(List<String> capacitacionesRequeridas) {
        this.capacitacionesRequeridas = capacitacionesRequeridas;
    }

    public List<String> getEmpleadosAsignados() {
        return empleadosAsignados;
    }

    public void setEmpleadosAsignados(List<String> empleadosAsignados) {
        this.empleadosAsignados = empleadosAsignados;
    }
}

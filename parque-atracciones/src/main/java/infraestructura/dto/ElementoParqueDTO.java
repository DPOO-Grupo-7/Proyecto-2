package infraestructura.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO base para ElementoParque.
 */
public abstract class ElementoParqueDTO {
    private String id;
    private String nombre;
    private int cupoMaximo;
    private String tipo; // "AtraccionMecanica", "AtraccionCultural", "Espectaculo"
    private LocalDateTime fechaInicioTemporada;
    private LocalDateTime fechaFinTemporada;
    private List<String> climaNoPermitido;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFechaInicioTemporada() {
        return fechaInicioTemporada;
    }

    public void setFechaInicioTemporada(LocalDateTime fechaInicioTemporada) {
        this.fechaInicioTemporada = fechaInicioTemporada;
    }

    public LocalDateTime getFechaFinTemporada() {
        return fechaFinTemporada;
    }

    public void setFechaFinTemporada(LocalDateTime fechaFinTemporada) {
        this.fechaFinTemporada = fechaFinTemporada;
    }

    public List<String> getClimaNoPermitido() {
        return climaNoPermitido;
    }

    public void setClimaNoPermitido(List<String> climaNoPermitido) {
        this.climaNoPermitido = climaNoPermitido;
    }
}

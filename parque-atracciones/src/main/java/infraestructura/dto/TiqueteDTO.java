package infraestructura.dto;

import java.time.LocalDateTime;

/**
 * DTO para la transferencia y persistencia de datos de tiquetes.
 *
 * <b>Uso:</b> Facilita la serialización y deserialización de tiquetes en archivos JSON.
 *
 * @author Sistema Parque
 */
public class TiqueteDTO {
    public String tipo;
    public String codigo;
    public LocalDateTime fechaCompra;
    public double precio;
    public String idComprador;
    public String nombreComprador;
    public boolean esEmpleado;
    public String categoria;
    public LocalDateTime fechaInicio;
    public LocalDateTime fechaFin;
    public String idAtraccion;
    public LocalDateTime fechaValida;
    public boolean utilizado;
    // Otros campos relevantes según el tipo de tiquete
}

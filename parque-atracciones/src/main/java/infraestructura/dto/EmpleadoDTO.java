package infraestructura.dto;

import java.util.List;

/**
 * DTO para la transferencia y persistencia de datos de empleados.
 *
 * <b>Uso:</b> Facilita la serialización y deserialización de empleados en archivos JSON.
 *
 * @author Sistema Parque
 */
public class EmpleadoDTO {
    public String tipo;
    public String puesto;
    public String identificacion;
    public String nombre;
    public String email;
    public String telefono;
    public String username;
    public String password;
    public Integer cajaAsignada;
    public String puntoVenta;
    public String especialidad;
    public Boolean certificadoSeguridad;
    public List<String> atraccionesHabilitadas;
    public List<String> areasResponsabilidad;
    public List<String> capacitaciones;
}

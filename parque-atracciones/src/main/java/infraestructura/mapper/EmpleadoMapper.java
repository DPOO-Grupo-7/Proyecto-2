package infraestructura.mapper;

import dominio.empleado.*;
import infraestructura.dto.EmpleadoDTO;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre objetos Empleado y EmpleadoDTO.
 *
 * <b>Uso:</b> Facilita la conversión entre entidades de dominio y DTO para persistencia.
 *
 * @author Sistema Parque
 */
public class EmpleadoMapper {
    public static EmpleadoDTO toDTO(Empleado empleado) {
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.tipo = empleado.getClass().getSimpleName();
        dto.puesto = dto.tipo;
        dto.identificacion = empleado.getIdentificacion();
        dto.nombre = empleado.getNombre();
        dto.email = empleado.getEmail();
        dto.telefono = empleado.getTelefono();
        dto.username = empleado.getUsername();
        dto.password = empleado.getPassword();
        dto.capacitaciones = empleado.getCapacitaciones().stream().map(Enum::name).collect(Collectors.toList());
        if (empleado instanceof Cajero) {
            Cajero cajero = (Cajero) empleado;
            dto.cajaAsignada = cajero.getCajaAsignada();
            dto.puntoVenta = cajero.getPuntoVenta();
        }
        if (empleado instanceof Cocinero) {
            Cocinero cocinero = (Cocinero) empleado;
            dto.especialidad = cocinero.getEspecialidad();
        }
        if (empleado instanceof OperarioAtraccion) {
            OperarioAtraccion op = (OperarioAtraccion) empleado;
            dto.atraccionesHabilitadas = op.getAtraccionesHabilitadas();
            dto.certificadoSeguridad = op.tieneCertificadoSeguridad();
        }
        if (empleado instanceof Administrador) {
            Administrador admin = (Administrador) empleado;
            dto.areasResponsabilidad = admin.getAreasResponsabilidad();
        }
        // ServicioGeneral no tiene campos extra
        return dto;
    }

    public static Empleado fromDTO(EmpleadoDTO dto) {
        if (dto == null) return null;
        Empleado empleado = null;
        switch (dto.puesto) {
            case "Cajero":
                empleado = new Cajero(dto.identificacion, dto.nombre, dto.email != null ? dto.email : "", dto.telefono != null ? dto.telefono : "", dto.username != null ? dto.username : "", dto.password != null ? dto.password : "", dto.cajaAsignada != null ? dto.cajaAsignada : 0, dto.puntoVenta != null ? dto.puntoVenta : "");
                break;
            case "Cocinero":
                empleado = new Cocinero(dto.identificacion, dto.nombre, dto.email != null ? dto.email : "", dto.telefono != null ? dto.telefono : "", dto.username != null ? dto.username : "", dto.password != null ? dto.password : "", dto.especialidad != null ? dto.especialidad : "");
                break;
            case "OperarioAtraccion":
                empleado = new OperarioAtraccion(dto.identificacion, dto.nombre, dto.email != null ? dto.email : "", dto.telefono != null ? dto.telefono : "", dto.username != null ? dto.username : "", dto.password != null ? dto.password : "", dto.certificadoSeguridad != null ? dto.certificadoSeguridad : false, dto.atraccionesHabilitadas);
                break;
            case "ServicioGeneral":
                empleado = new ServicioGeneral(dto.identificacion, dto.nombre, dto.email != null ? dto.email : "", dto.telefono != null ? dto.telefono : "", dto.username != null ? dto.username : "", dto.password != null ? dto.password : "");
                break;
            case "Administrador":
                empleado = new Administrador(dto.identificacion, dto.nombre, dto.email != null ? dto.email : "", dto.telefono != null ? dto.telefono : "", dto.username != null ? dto.username : "", dto.password != null ? dto.password : "", dto.areasResponsabilidad != null ? dto.areasResponsabilidad : java.util.Collections.emptyList());
                break;
            default:
                return null;
        }
        if (dto.capacitaciones != null) {
            for (String cap : dto.capacitaciones) {
                empleado.agregarCapacitacion(Capacitacion.valueOf(cap));
            }
        }
        return empleado;
    }
}

package infraestructura.mapper;

import dominio.tiquete.*;
import infraestructura.dto.TiqueteDTO;
import dominio.usuario.Cliente;
import java.time.LocalDate;

public class TiqueteMapper {

    public static TiqueteDTO toDTO(Tiquete tiquete) {
        TiqueteDTO dto = new TiqueteDTO();
        dto.tipo = tiquete.getClass().getSimpleName();
        dto.codigo = tiquete.getCodigo();
        dto.fechaCompra = tiquete.getFechaHoraEmision();
        dto.precio = tiquete.getPrecio();
        dto.idComprador = tiquete.getIdentificacionComprador();
        dto.nombreComprador = tiquete.getNombreComprador();
        dto.esEmpleado = tiquete.tieneDescuentoEmpleado();
        dto.utilizado = tiquete.estaUtilizado();

        if (tiquete instanceof TiqueteGeneral) {
            dto.categoria = ((TiqueteGeneral) tiquete).getCategoria().name();
        } else if (tiquete instanceof TiqueteTemporada) {
            dto.categoria = ((TiqueteTemporada) tiquete).getCategoria().name();
            dto.fechaInicio = ((TiqueteTemporada) tiquete).getFechaInicio();
            dto.fechaFin = ((TiqueteTemporada) tiquete).getFechaFin();
        } else if (tiquete instanceof EntradaIndividual) {
            if (((EntradaIndividual) tiquete).getAtraccion() != null) {
                dto.idAtraccion = ((EntradaIndividual) tiquete).getAtraccion().getId();
            } else {
                System.err.println("[WARN] EntradaIndividual con código " + tiquete.getCodigo() + " no tiene atracción asociada.");
            }
        } else if (tiquete instanceof FastPass) {
            dto.fechaValida = ((FastPass) tiquete).getFechaValida();
        }

        return dto;
    }

    public static Tiquete fromDTO(TiqueteDTO dto, java.util.function.Function<String, dominio.elementoparque.Atraccion> atraccionResolver) {
        if (dto == null) return null;

        Cliente comprador = new Cliente(dto.idComprador, "fakepass", dto.nombreComprador, dto.idComprador,
                "fake@email.com", "000000", LocalDate.of(2000, 1, 1), 1.75, 70);

        switch (dto.tipo) {
            case "TiqueteGeneral":
                return new TiqueteGeneral(dto.codigo, dto.fechaCompra, dto.precio,
                        dto.idComprador, dto.nombreComprador, dto.esEmpleado,
                        CategoriaTiquete.valueOf(dto.categoria), comprador);

            case "TiqueteTemporada":
                return new TiqueteTemporada(dto.codigo, dto.fechaCompra, dto.precio,
                        dto.idComprador, dto.nombreComprador, dto.esEmpleado,
                        CategoriaTiquete.valueOf(dto.categoria),
                        dto.fechaInicio, dto.fechaFin, comprador);

            case "EntradaIndividual":
                dominio.elementoparque.Atraccion atraccion = atraccionResolver.apply(dto.idAtraccion);
                return new EntradaIndividual(dto.codigo, dto.fechaCompra, dto.precio,
                        dto.idComprador, dto.nombreComprador, dto.esEmpleado,
                        atraccion, comprador);

            case "FastPass":
                return new FastPass(dto.codigo, dto.fechaCompra, dto.fechaValida,
                        dto.precio, dto.idComprador, dto.nombreComprador,
                        dto.esEmpleado, comprador);

            default:
                return null;
        }
    }
}

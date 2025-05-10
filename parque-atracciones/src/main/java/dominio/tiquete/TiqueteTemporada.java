package dominio.tiquete;

import java.time.LocalDateTime;
import dominio.usuario.Usuario;
import dominio.elementoparque.ElementoParque;

public class TiqueteTemporada extends Tiquete {

    private static final long serialVersionUID = 1L;

    private boolean esEmpleado;
    private CategoriaTiquete categoria;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    public TiqueteTemporada(String codigo, LocalDateTime fechaHoraEmision, double precio,
                             String identificacionComprador, String nombreComprador,
                             boolean esEmpleado, CategoriaTiquete categoria,
                             LocalDateTime fechaInicio, LocalDateTime fechaFin,
                             Usuario comprador) {
        super(codigo, fechaHoraEmision, precio, identificacionComprador, nombreComprador);
        this.esEmpleado = esEmpleado;
        this.categoria = categoria;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.comprador = comprador;
    }

    public CategoriaTiquete getCategoria() {
        return categoria;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    @Override
    public boolean tieneDescuentoEmpleado() {
        return esEmpleado;
    }

    @Override
    public boolean esValidoParaFecha(LocalDateTime fecha) {
        return !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
    }

    @Override
    public void utilizar(ElementoParque elemento) {
        this.marcarComoUtilizado();
        System.out.println("TiqueteTemporada utilizado en: " + elemento.getNombre());
    }
}

package dominio.tiquete;

import java.time.LocalDateTime;
import dominio.usuario.Usuario;
import dominio.elementoparque.ElementoParque;

public class FastPass extends Tiquete {

    private static final long serialVersionUID = 1L;

    private boolean esEmpleado;
    private LocalDateTime fechaValida;

    public FastPass(String codigo, LocalDateTime fechaHoraEmision, LocalDateTime fechaValida, double precio,
                    String identificacionComprador, String nombreComprador,
                    boolean esEmpleado, Usuario comprador) {
        super(codigo, fechaHoraEmision, precio, identificacionComprador, nombreComprador);
        this.fechaValida = fechaValida;
        this.esEmpleado = esEmpleado;
        this.comprador = comprador;
    }

    public LocalDateTime getFechaValida() {
        return fechaValida;
    }

    @Override
    public boolean tieneDescuentoEmpleado() {
        return esEmpleado;
    }

    @Override
    public boolean esValidoParaFecha(LocalDateTime fecha) {
        return fecha.toLocalDate().equals(fechaValida.toLocalDate());
    }

    @Override
    public void utilizar(ElementoParque elemento) {
        this.marcarComoUtilizado();
        System.out.println("FastPass utilizado en: " + elemento.getNombre());
    }
}

package dominio.tiquete;

import java.time.LocalDateTime;
import dominio.usuario.Usuario;
import dominio.elementoparque.ElementoParque;
import dominio.elementoparque.Atraccion;

public class EntradaIndividual extends Tiquete {

    private static final long serialVersionUID = 1L;

    private boolean esEmpleado;
    private Atraccion atraccion;

    public EntradaIndividual(String codigo, LocalDateTime fechaHoraEmision, double precio,
                              String identificacionComprador, String nombreComprador,
                              boolean esEmpleado, Atraccion atraccion,
                              Usuario comprador) {
        super(codigo, fechaHoraEmision, precio, identificacionComprador, nombreComprador);
        this.esEmpleado = esEmpleado;
        this.atraccion = atraccion;
        this.comprador = comprador;
    }

    public Atraccion getAtraccion() {
        return atraccion;
    }

    public boolean esValidoParaAtraccion(Atraccion a) {
        return this.atraccion.equals(a);
    }

    @Override
    public boolean tieneDescuentoEmpleado() {
        return esEmpleado;
    }

    @Override
    public void utilizar(ElementoParque elemento) {
        this.marcarComoUtilizado();
        System.out.println("EntradaIndividual utilizada en: " + elemento.getNombre());
    }
}

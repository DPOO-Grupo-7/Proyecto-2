package dominio.tiquete;

import java.time.LocalDateTime;
import dominio.usuario.Usuario;
import dominio.elementoparque.ElementoParque;

public class TiqueteGeneral extends Tiquete {

    private static final long serialVersionUID = 1L;

    private boolean esEmpleado;
    private CategoriaTiquete categoria;

    public TiqueteGeneral(String codigo, LocalDateTime fechaHoraEmision, double precio,
                          String identificacionComprador, String nombreComprador,
                          boolean esEmpleado, CategoriaTiquete categoria,
                          Usuario comprador) {
        super(codigo, fechaHoraEmision, precio, identificacionComprador, nombreComprador);
        this.esEmpleado = esEmpleado;
        this.categoria = categoria;
        this.comprador = comprador;
    }

    public CategoriaTiquete getCategoria() {
        return categoria;
    }

    @Override
    public boolean tieneDescuentoEmpleado() {
        return esEmpleado;
    }

    @Override
    public void utilizar(ElementoParque elemento) {
        this.marcarComoUtilizado();
        System.out.println("TiqueteGeneral utilizado en: " + elemento.getNombre());
    }
}

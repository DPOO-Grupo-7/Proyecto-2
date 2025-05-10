package dominio.tiquete;

import java.io.Serializable;
import java.time.LocalDateTime;
import dominio.usuario.Usuario;
import dominio.elementoparque.ElementoParque;

public abstract class Tiquete implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codigo;
    private LocalDateTime fechaHoraEmision;
    private double precio;

    private String identificacionComprador;
    private String nombreComprador;

    private boolean utilizado = false;

    protected Usuario comprador;

    public Tiquete(String codigo, LocalDateTime fechaHoraEmision, double precio,
                   String identificacionComprador, String nombreComprador) {
        this.codigo = codigo;
        this.fechaHoraEmision = fechaHoraEmision;
        this.precio = precio;
        this.identificacionComprador = identificacionComprador;
        this.nombreComprador = nombreComprador;
    }

    public String getCodigo() {
        return codigo;
    }

    public LocalDateTime getFechaHoraEmision() {
        return fechaHoraEmision;
    }

    public double getPrecio() {
        return precio;
    }

    public String getIdentificacionComprador() {
        return identificacionComprador;
    }

    public String getNombreComprador() {
        return nombreComprador;
    }

    public boolean estaUtilizado() {
        return utilizado;
    }

    public void marcarComoUtilizado() {
        this.utilizado = true;
    }

    public Usuario getComprador() {
        return comprador;
    }

    public void setComprador(Usuario comprador) {
        this.comprador = comprador;
    }

    // ✅ Default implementation
    public boolean tieneDescuentoEmpleado() {
        return false;
    }

    public boolean esValidoParaFecha(LocalDateTime fecha) {
        return fecha.toLocalDate().equals(fechaHoraEmision.toLocalDate());
    }

    public abstract void utilizar(ElementoParque elemento);
}

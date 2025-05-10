package dominio.empleado;

// import java.time.LocalDate; // Unused
// import java.util.List; // Unused
import dominio.tiquete.Tiquete;
import dominio.usuario.Usuario;
/**
 * Representa un empleado cajero del parque.
 * <p>
 * Encargado de la venta y registro de tiquetes, atención al cliente y manejo de caja.
 * </p>
 *
 * <b>Contexto:</b> Modelo de dominio para empleados que operan puntos de venta, taquillas o tiendas.
 *
 * <b>Precondiciones generales:</b>
 * <ul>
 *   <li>Identificación, nombre, email, teléfono, username, password, caja y punto de venta válidos.</li>
 * </ul>
 *
 * <b>Poscondiciones generales:</b>
 * <ul>
 *   <li>Cajero inicializado con capacitaciones requeridas y listo para registrar ventas.</li>
 * </ul>
 *
 * @author Sistema Parque
 * @example
 * <pre>
 *     Cajero cajero = new Cajero("2001", "Carlos", "carlos@parque.com", "555-1111", "cajero1", "pass", 1, "Taquilla Norte");
 * </pre>
 */
public class Cajero extends Empleado {
    private int cajaAsignada;
    private String puntoVenta;

    /**
     * Constructor para el empleado Cajero.
     *
     * <b>Precondiciones:</b> Todos los parámetros deben ser válidos y no nulos.
     * <b>Poscondiciones:</b> Cajero inicializado con capacitaciones de atención al cliente y manejo de caja.
     *
     * @param identificacion Identificación única.
     * @param nombre Nombre completo.
     * @param email Correo electrónico.
     * @param telefono Número de teléfono.
     * @param username Nombre de usuario.
     * @param password Contraseña.
     * @param cajaAsignada Número de la caja asignada.
     * @param puntoVenta Nombre o identificador del punto de venta.
     * @example
     * <pre>
     *     Cajero cajero = new Cajero("2001", "Carlos", "carlos@parque.com", "555-1111", "cajero1", "pass", 1, "Taquilla Norte");
     * </pre>
     */
    public Cajero(String identificacion, String nombre, String email, String telefono,
                 String username, String password, 
                 int cajaAsignada, String puntoVenta) {
        // Call the updated Empleado constructor
        super(identificacion, nombre, email, telefono, username, password);
        this.cajaAsignada = cajaAsignada;
        this.puntoVenta = puntoVenta;
        // Todo cajero debe tener capacitación en atención al cliente y manejo de caja
        agregarCapacitacion(Capacitacion.ATENCION_CLIENTE_GENERAL);
        agregarCapacitacion(Capacitacion.MANEJO_CAJA);
    }

    /**
     * Constructor para el empleado Cajero.
     * Permite opcionalmente no agregar capacitaciones por defecto (solo para pruebas).
     */
    public Cajero(String identificacion, String nombre, String email, String telefono,
                 String username, String password, 
                 int cajaAsignada, String puntoVenta, boolean agregarCapacitacionesPorDefecto) {
        super(identificacion, nombre, email, telefono, username, password);
        this.cajaAsignada = cajaAsignada;
        this.puntoVenta = puntoVenta;
        if (agregarCapacitacionesPorDefecto) {
            agregarCapacitacion(Capacitacion.ATENCION_CLIENTE_GENERAL);
            agregarCapacitacion(Capacitacion.MANEJO_CAJA);
        }
    }

    /**
     * Obtiene el número de la caja asignada al cajero.
     * @return Número de caja.
     */
    public int getCajaAsignada() {
        return cajaAsignada;
    }

    /**
     * Obtiene el nombre o identificador del punto de venta.
     * @return Punto de venta.
     */
    public String getPuntoVenta() {
        return puntoVenta;
    }

    /**
     * Define el punto de venta del cajero.
     * @param puntoVenta Nuevo punto de venta.
     */
    public void setPuntoVenta(String puntoVenta) {
        this.puntoVenta = puntoVenta;
    }

    /**
     * Calcula el descuento aplicable si el comprador es empleado.
     *
     * <b>Precondiciones:</b> El comprador no debe ser nulo.
     * <b>Poscondiciones:</b> Devuelve el porcentaje de descuento correspondiente.
     *
     * @param comprador Usuario que realiza la compra.
     * @return Porcentaje de descuento (0.20 para empleados, 0.0 para otros).
     * @example
     * <pre>
     *     double desc = cajero.calcularDescuentoEmpleado(comprador);
     * </pre>
     */
    public double calcularDescuentoEmpleado(Usuario comprador) {
        if (comprador instanceof Empleado) {
            return 0.20; // 20% de descuento para empleados
        }
        return 0.0;
    }

    /**
     * Registra la venta de un tiquete a un usuario.
     *
     * <b>Precondiciones:</b> El tiquete y el comprador no deben ser nulos.
     * <b>Poscondiciones:</b> El registro de venta se realiza correctamente.
     *
     * @param tiquete Tiquete vendido.
     * @param comprador Usuario que realiza la compra.
     * @example
     * <pre>
     *     cajero.registrarVenta(tiquete, comprador);
     * </pre>
     */
    public void registrarVenta(Tiquete tiquete, Usuario comprador) {
        double descuento = calcularDescuentoEmpleado(comprador);
        if (descuento > 0) {
            // El precio final se calcula al usar el tiquete o al mostrarlo, no se setea aquí.
            // tiquete.setPrecio(tiquete.getPrecio() * (1 - descuento)); // <- Eliminar esta línea, el precio se pasa al constructor
        }
        // Aquí iría la lógica adicional de registro de venta
    }
}

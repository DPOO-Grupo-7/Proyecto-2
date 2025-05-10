package dominio.usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import dominio.tiquete.Tiquete;
import dominio.excepciones.DatosInvalidosException; // Importar

/**
 * Representa un cliente del parque.
 * <p>
 * Los clientes son usuarios que pueden comprar tiquetes pero no son empleados.
 * Incluye información de salud y métodos para validar restricciones de acceso a atracciones.
 * </p>
 *
 * <b>Contexto:</b> Modelo de dominio para clientes del parque y su historial de compras.
 *
 * <b>Precondiciones generales:</b>
 * <ul>
 *   <li>Todos los parámetros del constructor deben ser válidos y no nulos.</li>
 *   <li>La altura y el peso deben ser positivos.</li>
 * </ul>
 *
 * <b>Poscondiciones generales:</b>
 * <ul>
 *   <li>Cliente inicializado y listo para comprar tiquetes y validar restricciones.</li>
 * </ul>
 *
 * @author Sistema Parque
 * @example
 * <pre>
 *     Cliente c = new Cliente("user1", "pass", "Juan", "123", "mail@x.com", "555-1234", LocalDate.of(2000,1,1), 1.7, 70);
 *     c.agregarCondicionMedica("Asma");
 *     boolean puede = c.cumpleRestriccionesAtraccion(1.2, 2.0, 40, 120, List.of("Asma"));
 * </pre>
 */
public class Cliente extends Usuario {
    private LocalDate fechaNacimiento;
    private double altura;
    private double peso;
    private List<Tiquete> tiquetesComprados;

    /**
     * Constructor para la clase Cliente.
     *
     * <b>Precondiciones:</b> Todos los parámetros deben ser válidos y no nulos. Altura y peso positivos.
     * <b>Poscondiciones:</b> Cliente inicializado con los datos y sin condiciones médicas ni tiquetes.
     *
     * @param username Nombre de usuario del cliente.
     * @param password Contraseña del cliente.
     * @param nombre Nombre completo del cliente.
     * @param identificacion Identificación única del cliente.
     * @param email Correo electrónico del cliente.
     * @param telefono Teléfono del cliente.
     * @param fechaNacimiento Fecha de nacimiento del cliente.
     * @param altura Altura del cliente.
     * @param peso Peso del cliente.
     * @throws DatosInvalidosException si fechaNacimiento es null o altura/peso no son positivos.
     * @example
     * <pre>
     *     Cliente c = new Cliente("user1", "pass", "Juan", "123", "mail@x.com", "555-1234", LocalDate.of(2000,1,1), 1.7, 70);
     * </pre>
     */
    public Cliente(String username, String password, String nombre, String identificacion,
                   String email, String telefono,
                   LocalDate fechaNacimiento, double altura, double peso) {
        super(identificacion, nombre, email, telefono, username, password);
        if (fechaNacimiento == null) {
            throw new DatosInvalidosException("La fecha de nacimiento no puede ser null");
        }
        if (altura <= 0) {
            throw new DatosInvalidosException("La altura debe ser positiva");
        }
        if (peso <= 0) {
            throw new DatosInvalidosException("El peso debe ser positivo");
        }

        this.fechaNacimiento = fechaNacimiento;
        this.altura = altura;
        this.peso = peso;
        this.condicionesMedicas = new ArrayList<>();
        this.tiquetesComprados = new ArrayList<>();
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public double getAltura() {
        return altura;
    }

    public double getPeso() {
        return peso;
    }

    public List<String> getCondicionesMedicas() {
        return List.copyOf(condicionesMedicas);
    }

    public void agregarCondicionMedica(String condicion) {
        this.condicionesMedicas.add(condicion);
    }

    public List<Tiquete> getTiquetesComprados() {
        return List.copyOf(tiquetesComprados);
    }

    public void agregarTiquete(Tiquete tiquete) {
        this.tiquetesComprados.add(tiquete);
    }

    public int getEdad() {
        return LocalDate.now().getYear() - fechaNacimiento.getYear();
    }

    /**
     * Verifica si el cliente cumple las restricciones de una atracción.
     *
     * <b>Precondiciones:</b> Los parámetros deben ser válidos y positivos.
     * <b>Poscondiciones:</b> Devuelve true si el cliente cumple todas las restricciones.
     *
     * @param alturaMinima Altura mínima permitida.
     * @param alturaMaxima Altura máxima permitida.
     * @param pesoMinimo Peso mínimo permitido.
     * @param pesoMaximo Peso máximo permitido.
     * @param contraindicaciones Lista de contraindicaciones médicas.
     * @return true si cumple todas las restricciones, false si no.
     * @example
     * <pre>
     *     boolean puede = cliente.cumpleRestriccionesAtraccion(1.2, 2.0, 40, 120, List.of("Asma"));
     * </pre>
     */
    public boolean cumpleRestriccionesAtraccion(double alturaMinima, double alturaMaxima,
                                              double pesoMinimo, double pesoMaximo,
                                              List<String> contraindicaciones) {
        if (altura < alturaMinima || altura > alturaMaxima) {
            return false;
        }
        if (peso < pesoMinimo || peso > pesoMaximo) {
            return false;
        }
        
        // Verifica que ninguna de las condiciones médicas del cliente esté en las contraindicaciones
        for (String condicion : condicionesMedicas) {
            if (contraindicaciones.contains(condicion)) {
                return false;
            }
        }
        return true;
    }
}

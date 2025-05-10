package dominio.empleado;

import java.util.List;


/**
 * Representa un empleado administrador del parque.
 * <p>
 * Los administradores pueden gestionar empleados, atracciones y operaciones generales del parque.
 * </p>
 *
 * <b>Contexto:</b> Modelo de dominio para usuarios con privilegios administrativos.
 *
 * <b>Precondiciones generales:</b>
 * <ul>
 *   <li>Identificación, nombre, email, teléfono, username y password válidos.</li>
 * </ul>
 *
 * <b>Poscondiciones generales:</b>
 * <ul>
 *   <li>Administrador inicializado y listo para operaciones administrativas.</li>
 * </ul>
 *
 * @author Sistema Parque
 * @example
 * <pre>
 *     Administrador admin = new Administrador("1001", "Admin", "admin@parque.com", "555-0000", "admin", "adminpass");
 * </pre>
 */
public class Administrador extends Empleado {
    private List<String> areasResponsabilidad;

    /**
     * Constructor para el empleado Administrador.
     *
     * <b>Precondiciones:</b> Todos los parámetros deben ser válidos y no nulos.
     * <b>Poscondiciones:</b> Administrador inicializado con los datos proporcionados.
     *
     * @param identificacion Identificación única.
     * @param nombre Nombre completo.
     * @param email Correo electrónico.
     * @param telefono Número de teléfono.
     * @param username Nombre de usuario.
     * @param password Contraseña.
     * @param areasResponsabilidad Lista de áreas de responsabilidad del administrador.
     * @example
     * <pre>
     *     Administrador admin = new Administrador("1001", "Admin", "admin@parque.com", "555-0000", "admin", "adminpass");
     * </pre>
     */
    public Administrador(String identificacion, String nombre, String email, 
                        String telefono, String username, String password, 
                        List<String> areasResponsabilidad) {
        super(identificacion, nombre, email, telefono, username, password);
        this.areasResponsabilidad = areasResponsabilidad;
        // Todo administrador tiene capacitaciones básicas y administrativas
        agregarCapacitacion(Capacitacion.ATENCION_CLIENTE_GENERAL);
        agregarCapacitacion(Capacitacion.CERTIFICADO_SEGURIDAD_ATRACCIONES);
        agregarCapacitacion(Capacitacion.PRIMEROS_AUXILIOS);
    }

    public List<String> getAreasResponsabilidad() {
        return List.copyOf(areasResponsabilidad);
    }


    
}

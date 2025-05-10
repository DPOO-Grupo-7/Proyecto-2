package dominio.excepciones;

/**
 * Excepción lanzada cuando se detectan datos inválidos en la operación de dominio.
 * <p>
 * Utilizada para validar parámetros y estados en constructores y métodos de dominio.
 * </p>
 *
 * <b>Contexto:</b> Se lanza en validaciones de entidades, servicios y lógica de negocio.
 *
 * @author Sistema Parque
 * @example
 * <pre>
 *     throw new DatosInvalidosException("El nombre no puede ser vacío");
 * </pre>
 */
public class DatosInvalidosException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Crea una nueva excepción de datos inválidos con un mensaje descriptivo.
     *
     * @param message Mensaje descriptivo del error.
     */
    public DatosInvalidosException(String message) {
        super(message);
    }
}

package dominio.excepciones;

public class AtraccionNoEncontradaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AtraccionNoEncontradaException(String id) {
        super("No se encontró la atracción con ID: " + id);
    }

    public AtraccionNoEncontradaException(String message, Throwable cause) {
        super(message, cause);
    }
}

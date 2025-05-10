package dominio.excepciones;

public class AsignacionInvalidaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AsignacionInvalidaException(String message) {
        super(message);
    }
}

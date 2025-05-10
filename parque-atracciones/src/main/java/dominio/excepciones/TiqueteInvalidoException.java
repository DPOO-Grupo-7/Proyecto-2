package dominio.excepciones;

public class TiqueteInvalidoException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public TiqueteInvalidoException(String message) {
        super(message);
    }
}

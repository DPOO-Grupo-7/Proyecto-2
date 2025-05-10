package dominio.excepciones;

public class TiqueteYaUtilizadoException extends IllegalStateException {
    private static final long serialVersionUID = 1L;

    public TiqueteYaUtilizadoException(String codigoTiquete) {
        super("El tiquete con código '" + codigoTiquete + "' ya ha sido utilizado.");
    }
}

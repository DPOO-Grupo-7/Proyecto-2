package dominio.excepciones;

public class EmpleadoNoEncontradoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EmpleadoNoEncontradoException(String identificacion) {
        super("No se encontró el empleado con identificación: " + identificacion);
    }
}

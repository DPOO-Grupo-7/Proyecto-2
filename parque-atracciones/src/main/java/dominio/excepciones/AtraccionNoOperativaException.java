package dominio.excepciones;

/**
 * Excepción lanzada cuando una atracción no puede operar debido a condiciones
 * externas como el clima o falta de personal calificado.
 */
public class AtraccionNoOperativaException extends RuntimeException { // O extiende una excepción base del dominio si existe

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final MotivoNoOperativa motivo;
    private final String nombreAtraccion;

    /**
     * Constructor para la excepción.
     * @param motivo La razón específica (CLIMA_INVALIDO o FALTA_PERSONAL).
     * @param nombreAtraccion El nombre de la atracción afectada.
     */
    public AtraccionNoOperativaException(MotivoNoOperativa motivo, String nombreAtraccion) {
        super("La atracción '" + nombreAtraccion + "' no puede operar: " + motivo.getDescripcion());
        this.motivo = motivo;
        this.nombreAtraccion = nombreAtraccion;
    }

    /**
     * Constructor alternativo sin nombre de atracción específico.
     * @param motivo La razón específica.
     */
    public AtraccionNoOperativaException(MotivoNoOperativa motivo) {
        super("La atracción no puede operar: " + motivo.getDescripcion());
        this.motivo = motivo;
        this.nombreAtraccion = null; // O un valor por defecto
    }

    public MotivoNoOperativa getMotivo() {
        return motivo;
    }

    public String getNombreAtraccion() {
        return nombreAtraccion;
    }
}

package dominio.tiquete;

/**
 * Enum que representa las categorías de tiquete disponibles en el parque.
 * <p>
 * Cada categoría determina el nivel de acceso a las atracciones.
 * </p>
 *
 * <b>Contexto:</b> Asociado a la venta de tiquetes y control de acceso.
 * 
 * @author Sistema Parque
 * @example
 * <pre>
 *     CategoriaTiquete cat = CategoriaTiquete.ORO;
 *     System.out.println(cat.getDescripcion()); // Salida: Acceso a atracciones familiares y oro
 * </pre>
 */
public enum CategoriaTiquete {
    BASICO("Acceso básico al parque sin atracciones"),
    FAMILIAR("Acceso a atracciones familiares"),
    ORO("Acceso a atracciones familiares y oro"),
    DIAMANTE("Acceso a todas las atracciones");

    private final String descripcion;

    CategoriaTiquete(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la descripción de la categoría de tiquete.
     * @return Descripción textual de la categoría.
     */
    public String getDescripcion() {
        return descripcion;
    }
}

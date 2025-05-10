package dominio.util;

/**
 * Enum que representa las condiciones climáticas relevantes para la operación de atracciones y espectáculos en el parque.
 * <p>
 * Cada valor tiene una descripción textual asociada.
 * </p>
 *
 * <b>Contexto:</b> Utilizado para restringir la operación de elementos del parque bajo ciertas condiciones meteorológicas.
 *
 * @author Sistema Parque
 * @example
 * <pre>
 *     CondicionClimatica clima = CondicionClimatica.TORMENTA;
 *     System.out.println(clima.getDescripcion()); // Salida: Condiciones de tormenta
 * </pre>
 */
public enum CondicionClimatica {
    TORMENTA("Condiciones de tormenta"),
    LLUVIA_FUERTE("Lluvia intensa"),
    FRIO_EXTREMO("Temperaturas muy bajas"),
    CALOR_EXTREMO("Temperaturas muy altas"),
    VIENTO_FUERTE("Vientos fuertes"),
    NORMAL("Condiciones normales");

    private final String descripcion;

    CondicionClimatica(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la descripción textual de la condición climática.
     * @return Descripción de la condición climática.
     */
    public String getDescripcion() {
        return descripcion;
    }
}
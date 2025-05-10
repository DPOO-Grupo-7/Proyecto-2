package dominio.empleado;

/**
 * Representa una capacitación o certificación que puede tener un empleado del parque.
 * <p>
 * Utilizado para validar la asignación de empleados a lugares de trabajo y atracciones.
 * </p>
 *
 * <b>Contexto:</b> Enum para tipos de capacitación requeridos en el parque.
 *
 * @author Sistema Parque
 * @example
 * <pre>
 *     Capacitacion cap = Capacitacion.PRIMEROS_AUXILIOS;
 *     System.out.println(cap.getDescripcion());
 * </pre>
 */
public enum Capacitacion {
    OPERACION_ATRACCION_RIESGO_MEDIO("Operación de atracciones de riesgo medio"),
    OPERACION_ATRACCION_RIESGO_ALTO("Operación de atracciones de riesgo alto"),
    CERTIFICADO_SEGURIDAD_ATRACCIONES("Certificado de seguridad para atracciones"),
    PRIMEROS_AUXILIOS("Certificación en primeros auxilios"),
    ATENCION_CLIENTE_GENERAL("Atención al cliente general"),
    MANIPULACION_ALIMENTOS_BASICO("Manipulación básica de alimentos"),
    MANIPULACION_ALIMENTOS_AVANZADO("Manipulación avanzada de alimentos"),
    MANEJO_CAJA("Manejo de caja y transacciones"),
    MONTAÑA_RUSA_A1("Operación específica Montaña Rusa A1"),
    BARCO_PIRATA_B1("Operación específica Barco Pirata B1");

    private final String descripcion;

    Capacitacion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la descripción textual de la capacitación.
     * @return Descripción de la capacitación.
     */
    public String getDescripcion() {
        return descripcion;
    }
}
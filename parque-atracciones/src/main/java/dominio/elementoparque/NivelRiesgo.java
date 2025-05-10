package dominio.elementoparque;

/**
 * Enum para representar los niveles de riesgo de las atracciones mecánicas del parque.
 * <p>
 * Determina los requisitos de capacitación y las restricciones de operación según el riesgo.
 * </p>
 *
 * <b>Contexto:</b> Asociado a la gestión de atracciones mecánicas y asignación de empleados.
 *
 * <b>Precondiciones generales:</b> El nivel debe ser uno de los valores definidos.
 * <b>Poscondiciones generales:</b> Permite distinguir entre atracciones de riesgo medio y alto.
 *
 * @author Sistema Parque
 */
public enum NivelRiesgo {
    MEDIO("Riesgo moderado - Requiere capacitación general"),
    ALTO("Riesgo alto - Requiere capacitación específica");

    private final String descripcion;

    NivelRiesgo(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la descripción del nivel de riesgo.
     * @return Descripción textual.
     */
    public String getDescripcion() {
        return descripcion;
    }
}

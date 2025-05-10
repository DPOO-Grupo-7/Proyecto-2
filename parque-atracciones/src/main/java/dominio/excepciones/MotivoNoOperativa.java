package dominio.excepciones;

/**
 * Enum para especificar la razón por la cual una atracción no está operativa.
 */
public enum MotivoNoOperativa {
    CLIMA_INVALIDO("Condiciones climáticas no permiten la operación"),
    FALTA_PERSONAL("No hay suficiente personal calificado asignado");

    private final String descripcion;

    MotivoNoOperativa(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
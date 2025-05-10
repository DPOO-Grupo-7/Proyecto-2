package dominio.elementoparque;

import dominio.tiquete.CategoriaTiquete;

/**
 * Enum que representa los niveles de exclusividad de las atracciones del parque.
 * <p>
 * Determina el tipo de tiquete requerido para acceder a una atracción:
 * Familiar, Oro o Diamante.
 * </p>
 *
 * <b>Contexto:</b> Asociado a la venta de tiquetes y control de acceso a atracciones.
 *
 * <b>Precondiciones generales:</b> El nivel debe ser uno de los valores definidos.
 * <b>Poscondiciones generales:</b> Permite verificar acceso según la categoría del tiquete.
 *
 * @author Sistema Parque
 */
public enum NivelExclusividad {
    FAMILIAR("Acceso con tiquete Familiar o superior"),
    ORO("Acceso con tiquete Oro o superior"),
    DIAMANTE("Acceso exclusivo con tiquete Diamante");

    private final String descripcion;

    NivelExclusividad(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la descripción del nivel de exclusividad.
     * @return Descripción textual.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Indica si una categoría de tiquete permite el acceso a este nivel de exclusividad.
     *
     * <b>Precondiciones:</b> La categoría no debe ser nula.
     * <b>Poscondiciones:</b> Devuelve true si la categoría permite el acceso, false si no.
     *
     * @param categoria Categoría de tiquete a evaluar.
     * @return true si permite acceso, false si no.
     * @example
     * <pre>
     *     boolean acceso = NivelExclusividad.ORO.permiteAccesoConCategoria(CategoriaTiquete.DIAMANTE);
     * </pre>
     */
    public boolean permiteAccesoConCategoria(CategoriaTiquete categoria) {
        switch (this) {
            case FAMILIAR:
                return categoria != CategoriaTiquete.BASICO;
            case ORO:
                return categoria == CategoriaTiquete.ORO || 
                       categoria == CategoriaTiquete.DIAMANTE;
            case DIAMANTE:
                return categoria == CategoriaTiquete.DIAMANTE;
            default:
                return false;
        }
    }
}

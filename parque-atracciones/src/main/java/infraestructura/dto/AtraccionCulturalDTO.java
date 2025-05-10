package infraestructura.dto;

/**
 * DTO para AtraccionCultural.
 */
public class AtraccionCulturalDTO extends AtraccionDTO {
    private int edadMinima;

    public AtraccionCulturalDTO() {
        setTipo("AtraccionCultural");
    }

    public int getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(int edadMinima) {
        this.edadMinima = edadMinima;
    }
}

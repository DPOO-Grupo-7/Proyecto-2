package infraestructura.dto;

import java.util.List;

/**
 * DTO para AtraccionMecanica.
 */
public class AtraccionMecanicaDTO extends AtraccionDTO {
    private String nivelRiesgo;
    private double restriccionAlturaMinima;
    private double restriccionAlturaMaxima;
    private double restriccionPesoMinimo;
    private double restriccionPesoMaximo;
    private List<String> contraindicacionesSalud;
    private List<String> restriccionesSalud;
    private String capacitacionEspecifica;

    public AtraccionMecanicaDTO() {
        setTipo("AtraccionMecanica");
    }

    public String getNivelRiesgo() {
        return nivelRiesgo;
    }

    public void setNivelRiesgo(String nivelRiesgo) {
        this.nivelRiesgo = nivelRiesgo;
    }

    public double getRestriccionAlturaMinima() {
        return restriccionAlturaMinima;
    }

    public void setRestriccionAlturaMinima(double restriccionAlturaMinima) {
        this.restriccionAlturaMinima = restriccionAlturaMinima;
    }

    public double getRestriccionAlturaMaxima() {
        return restriccionAlturaMaxima;
    }

    public void setRestriccionAlturaMaxima(double restriccionAlturaMaxima) {
        this.restriccionAlturaMaxima = restriccionAlturaMaxima;
    }

    public double getRestriccionPesoMinimo() {
        return restriccionPesoMinimo;
    }

    public void setRestriccionPesoMinimo(double restriccionPesoMinimo) {
        this.restriccionPesoMinimo = restriccionPesoMinimo;
    }

    public double getRestriccionPesoMaximo() {
        return restriccionPesoMaximo;
    }

    public void setRestriccionPesoMaximo(double restriccionPesoMaximo) {
        this.restriccionPesoMaximo = restriccionPesoMaximo;
    }

    public List<String> getContraindicacionesSalud() {
        return contraindicacionesSalud;
    }

    public void setContraindicacionesSalud(List<String> contraindicacionesSalud) {
        this.contraindicacionesSalud = contraindicacionesSalud;
    }

    public List<String> getRestriccionesSalud() {
        return restriccionesSalud;
    }

    public void setRestriccionesSalud(List<String> restriccionesSalud) {
        this.restriccionesSalud = restriccionesSalud;
    }

    public String getCapacitacionEspecifica() {
        return capacitacionEspecifica;
    }

    public void setCapacitacionEspecifica(String capacitacionEspecifica) {
        this.capacitacionEspecifica = capacitacionEspecifica;
    }
}

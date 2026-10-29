package mz.agrogest.model;

import java.io.Serializable;
import java.time.LocalDate;

import mz.agrogest.util.Formatos;

/** Cultivo registado dentro de uma campanha. */
public class Cultivo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int campanhaId;
    private String campanhaNome;
    private String cultura;
    private String variedade;
    private double hectares;
    private LocalDate dataPlantio;

    public Cultivo() {
    }

    public Cultivo(int id, Campanha campanha, String cultura, String variedade, double hectares,
                   LocalDate dataPlantio) {
        this.id = id;
        this.campanhaId = campanha.getId();
        this.campanhaNome = campanha.getNome();
        this.cultura = cultura;
        this.variedade = variedade;
        this.hectares = hectares;
        this.dataPlantio = dataPlantio;
    }

    public String getDataPlantioTexto() { return Formatos.data(dataPlantio); }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCampanhaId() { return campanhaId; }
    public void setCampanhaId(int campanhaId) { this.campanhaId = campanhaId; }

    public String getCampanhaNome() { return campanhaNome; }
    public void setCampanhaNome(String campanhaNome) { this.campanhaNome = campanhaNome; }

    public String getCultura() { return cultura; }
    public void setCultura(String cultura) { this.cultura = cultura; }

    public String getVariedade() { return variedade; }
    public void setVariedade(String variedade) { this.variedade = variedade; }

    public double getHectares() { return hectares; }
    public void setHectares(double hectares) { this.hectares = hectares; }

    public LocalDate getDataPlantio() { return dataPlantio; }
    public void setDataPlantio(LocalDate dataPlantio) { this.dataPlantio = dataPlantio; }
}

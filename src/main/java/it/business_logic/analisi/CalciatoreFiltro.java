package it.business_logic.analisi;

import java.util.List;

public class CalciatoreFiltro {

    // --- ANAGRAFICA ---
    private Integer minEta;
    private Integer maxEta;

    // --- RUOLI ---
    private List<String> sigleRuoli;

    // --- SQUADRA / CAMPIONATO / LISTA ---
    private Integer idSquadra;
    private String campionato;
    private Integer idLista;

    // --- CONTRATTO ---
    private Integer minAnniContratto;
    private Float minStipendio;
    private Float maxStipendio;

    // --- STATISTICHE (Aggiornate a 18 campi totali) ---
    private Integer minMinutiGiocati;
    private Double minGol;
    private Double minAssist;
    private Double minXG;
    private Double minXA;
    private Double minTiriTotali; // Aggiunto
    private Double minTiriInPorta;
    private Double minDribblingRiusciti;
    private Double minTocchiInAreaAvversaria; // Aggiunto
    private Double minContrastiVinti;
    private Double minDuelliAereiVinti; // Aggiunto
    private Double minPassaggiChiave; // Aggiunto
    private Double minCrossRiusciti; // Aggiunto
    private Double minPassaggiRealizzati; // Aggiunto
    private Double minParate;
    private Double minCleanSheet;
    private Integer maxCartelliniGialli; // Aggiunto (solitamente si filtra per il "massimo")
    private Integer maxCartelliniRossi;  // Aggiunto

    private String stagione;

    // --- GETTERS & SETTERS (Nuovi Metodi) ---

    public Double getMinTiriTotali() { return minTiriTotali; }
    public void setMinTiriTotali(Double minTiriTotali) { this.minTiriTotali = minTiriTotali; }

    public Double getMinTocchiInAreaAvversaria() { return minTocchiInAreaAvversaria; }
    public void setMinTocchiInAreaAvversaria(Double minTocchiInAreaAvversaria) { this.minTocchiInAreaAvversaria = minTocchiInAreaAvversaria; }

    public Double getMinDuelliAereiVinti() { return minDuelliAereiVinti; }
    public void setMinDuelliAereiVinti(Double minDuelliAereiVinti) { this.minDuelliAereiVinti = minDuelliAereiVinti; }

    public Double getMinPassaggiChiave() { return minPassaggiChiave; }
    public void setMinPassaggiChiave(Double minPassaggiChiave) { this.minPassaggiChiave = minPassaggiChiave; }

    public Double getMinCrossRiusciti() { return minCrossRiusciti; }
    public void setMinCrossRiusciti(Double minCrossRiusciti) { this.minCrossRiusciti = minCrossRiusciti; }

    public Double getMinPassaggiRealizzati() { return minPassaggiRealizzati; }
    public void setMinPassaggiRealizzati(Double minPassaggiRealizzati) { this.minPassaggiRealizzati = minPassaggiRealizzati; }

    public Integer getMaxCartelliniGialli() { return maxCartelliniGialli; }
    public void setMaxCartelliniGialli(Integer maxCartelliniGialli) { this.maxCartelliniGialli = maxCartelliniGialli; }

    public Integer getMaxCartelliniRossi() { return maxCartelliniRossi; }
    public void setMaxCartelliniRossi(Integer maxCartelliniRossi) { this.maxCartelliniRossi = maxCartelliniRossi; }

    // --- GETTERS & SETTERS (Esistenti rimasti invariati) ---

    public Integer getMinEta() { return minEta; }
    public void setMinEta(Integer minEta) { this.minEta = minEta; }
    public Integer getMaxEta() { return maxEta; }
    public void setMaxEta(Integer maxEta) { this.maxEta = maxEta; }
    public List<String> getSigleRuoli() { return sigleRuoli; }
    public void setSigleRuoli(List<String> sigleRuoli) { this.sigleRuoli = sigleRuoli; }
    public Integer getIdSquadra() { return idSquadra; }
    public void setIdSquadra(Integer idSquadra) { this.idSquadra = idSquadra; }
    public String getCampionato() { return campionato; }
    public void setCampionato(String campionato) { this.campionato = campionato; }
    public Integer getIdLista() { return idLista; }
    public void setIdLista(Integer idLista) { this.idLista = idLista; }
    public Integer getMinAnniContratto() { return minAnniContratto; }
    public void setMinAnniContratto(Integer minAnniContratto) { this.minAnniContratto = minAnniContratto; }
    public Float getMinStipendio() { return minStipendio; }
    public void setMinStipendio(Float minStipendio) { this.minStipendio = minStipendio; }
    public Float getMaxStipendio() { return maxStipendio; }
    public void setMaxStipendio(Float maxStipendio) { this.maxStipendio = maxStipendio; }
    public Integer getMinMinutiGiocati() { return minMinutiGiocati; }
    public void setMinMinutiGiocati(Integer minMinutiGiocati) { this.minMinutiGiocati = minMinutiGiocati; }
    public Double getMinGol() { return minGol; }
    public void setMinGol(Double minGol) { this.minGol = minGol; }
    public Double getMinAssist() { return minAssist; }
    public void setMinAssist(Double minAssist) { this.minAssist = minAssist; }
    public Double getMinXG() { return minXG; }
    public void setMinXG(Double minXG) { this.minXG = minXG; }
    public Double getMinXA() { return minXA; }
    public void setMinXA(Double minXA) { this.minXA = minXA; }
    public Double getMinTiriInPorta() { return minTiriInPorta; }
    public void setMinTiriInPorta(Double minTiriInPorta) { this.minTiriInPorta = minTiriInPorta; }
    public Double getMinDribblingRiusciti() { return minDribblingRiusciti; }
    public void setMinDribblingRiusciti(Double minDribblingRiusciti) { this.minDribblingRiusciti = minDribblingRiusciti; }
    public Double getMinContrastiVinti() { return minContrastiVinti; }
    public void setMinContrastiVinti(Double minContrastiVinti) { this.minContrastiVinti = minContrastiVinti; }
    public Double getMinParate() { return minParate; }
    public void setMinParate(Double minParate) { this.minParate = minParate; }
    public Double getMinCleanSheet() { return minCleanSheet; }
    public void setMinCleanSheet(Double minCleanSheet) { this.minCleanSheet = minCleanSheet; }
    public String getStagione() { return stagione; }
    public void setStagione(String stagione) { this.stagione = stagione; }
}
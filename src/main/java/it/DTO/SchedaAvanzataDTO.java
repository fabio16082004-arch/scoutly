package it.DTO;

import it.domain_model.giocatori.Ruolo;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SchedaAvanzataDTO {
    private final String nome;
    private final String cognome;
    private final int eta;
    private final String nazionalita;
    private final float peso;
    private final float altezza;
    private final Set<String> ruoli;
    private final String nomeSquadraAttuale;
    private final LocalDate fineContratto;
    private final float stipendio;
    private final Map<String, Number> statistiche;

    public SchedaAvanzataDTO(String nome, String cognome, int eta, String nazionalita,
                                    float peso, float altezza, Set<String> ruoli,
                                    String nomeSquadraAttuale, LocalDate fineContratto, float stipendio,
                             Map<String, Number> statistiche) {
        this.nome = nome;
        this.cognome = cognome;
        this.eta = eta;
        this.nazionalita = nazionalita;
        this.peso = peso;
        this.altezza = altezza;
        this.ruoli = Set.copyOf(ruoli);
        this.nomeSquadraAttuale = nomeSquadraAttuale;
        this.fineContratto = fineContratto;
        this.stipendio = stipendio;
        this.statistiche = new HashMap<>(statistiche);
    }

    public Map<String, Number> getStatistiche() {
        return Map.copyOf(statistiche);
    }

    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public int getEta() { return eta; }
    public String getNazionalita() { return nazionalita; }
    public float getPeso() { return peso; }
    public float getAltezza() { return altezza; }
    public Set<String> getRuoli() { return ruoli; }
    public String getNomeSquadraAttuale() { return nomeSquadraAttuale; }
    public LocalDate getFineContratto() { return fineContratto; }
    public float getStipendio() { return stipendio; }
}
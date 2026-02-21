package it.domain_model.scouting;

import it.domain_model.giocatori.Calciatore;
import it.domain_model.giocatori.Partita;
import it.domain_model.utenti.Osservatore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Report {
    private int idReport;

    private Osservatore utente;
    private Calciatore calciatore;
    private List<Partita> partite;
    private Map<String, Integer> voti;
    private int votoComplessivo;
    private String noteFinali;
    private LocalDate dataCreazione;

    public Report(Osservatore utente, Calciatore calciatore) {
        this.utente = utente;
        this.calciatore = calciatore;
        this.partite = new ArrayList<>();
        this.voti = new HashMap<>();
        this.dataCreazione = LocalDate.now();
        this.votoComplessivo = 0;
    }

    public int getIdReport() {
        return idReport;
    }

    public void setIdReport(int idReport) {
        this.idReport = idReport;
    }

    public void aggiungiPartita(Partita partita){
        if(partita != null){
            partite.add(partita);
        }
    }

    public void aggiungiVoto(String parametro, int voto){
        if(voto >= 1 && voto <= 10){
            voti.put(parametro, voto);
        } else {
            throw new IllegalArgumentException("Il voto dato per un parametro deve essere compreso tra 1 e 10");
        }
    }

    public void calcolaVotoComplessivo(){
        if (voti.isEmpty()) {
            this.votoComplessivo = 0;
            return;
        }
        float votoFinale = 0;
        for(Integer voto : voti.values()){
            votoFinale += voto;
        }
        this.votoComplessivo = Math.round(votoFinale / voti.size());
    }

    public void setNoteFinali(String note) { this.noteFinali = note; }

    public List<Partita> getPartite() { return partite; }

    public Osservatore getUtente() { return utente; }

    public Calciatore getCalciatore() { return calciatore; }

    public Map<String, Integer> getVoti() { return voti; }

    public int getVotoComplessivo() { return votoComplessivo; }

    public String getNoteFinali() { return noteFinali; }

    public LocalDate getDataCreazione() { return dataCreazione; }

    public Map<String, Integer> getMappaVoti() {
        return voti;
    }

    public void setVotoComplessivo(int votoComplessivo){
        if(votoComplessivo < 0 || votoComplessivo > 10)
            throw new IllegalArgumentException("Il voto deve essere compreso tra 1 e 10");
        this.votoComplessivo = votoComplessivo;
    }
}
package it.domain_model.scouting;

import it.domain_model.giocatori.Calciatore;
import it.domain_model.giocatori.Ruolo;
import it.domain_model.utenti.Osservatore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Lista {

    private int idLista;

    private String nomeLista;
    private String descrizione;
    private LocalDate dataCreazione;
    private List<Calciatore> calciatori;
    private Osservatore osservatore;

    public Lista(String nomeLista, String descrizione, Osservatore osservatore) {
        this.nomeLista = nomeLista;
        this.descrizione = descrizione;
        this.dataCreazione = LocalDate.now();
        this.calciatori = new ArrayList<>();
        this.osservatore = osservatore;
    }

    public Lista(int idLista, String nomeLista, String descrizione,
                 Osservatore osservatore, LocalDate dataCreazione) {

        this.idLista = idLista;
        this.nomeLista = nomeLista;
        this.descrizione = descrizione;
        this.osservatore = osservatore;
        this.dataCreazione = dataCreazione;
        this.calciatori = new ArrayList<>();
    }

    public void aggiungiCalciatore(Calciatore c) {
        if (c != null && !calciatori.contains(c)) {
            calciatori.add(c);
        }
    }

    public void rimuoviCalciatore(int idCalciatore) {
        calciatori.removeIf(c -> c.getId() == idCalciatore);
    }

    public List<Calciatore> filtraPerRuolo(Ruolo ruolo) {
        return calciatori.stream()
                .filter(c -> c.getRuoli().contains(ruolo))
                .collect(Collectors.toList());
    }

    public int getIdLista() { return idLista; }
    public void setIdLista(int idLista) { this.idLista = idLista; }

    public String getNomeLista() { return nomeLista; }
    public void setNomeLista(String nomeLista) { this.nomeLista = nomeLista; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public List<Calciatore> getCalciatori() { return calciatori; }

    public Osservatore getOsservatore() { return osservatore; }

    public LocalDate getDataCreazione() { return dataCreazione; }
}

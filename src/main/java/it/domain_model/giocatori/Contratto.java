package it.domain_model.giocatori;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Contratto {
    private final Calciatore calciatore;
    private final Squadra squadra;
    private final LocalDate dataInizioContratto;

    private float stipendio;
    private LocalDate dataFineContratto;

    public Contratto(Calciatore calciatore, Squadra squadra, float stipendio,
                     LocalDate dataInizio, LocalDate dataFine) {

        if (calciatore == null) {
            throw new IllegalArgumentException("Il calciatore è obbligatorio per un contratto.");
        }
        if (squadra == null) {
            throw new IllegalArgumentException("La squadra è obbligatoria per un contratto.");
        }

        validaData(dataInizio, dataFine);
        validaStipendio(stipendio);

        this.calciatore = calciatore;
        this.squadra = squadra;
        this.stipendio = stipendio;
        this.dataInizioContratto = dataInizio;
        this.dataFineContratto = dataFine;
    }

    private void validaData(LocalDate inizio, LocalDate fine) {
        if (inizio == null || fine == null) {
            throw new IllegalArgumentException("Le date del contratto non possono essere nulle.");
        }
        if (fine.isBefore(inizio)) {
            throw new IllegalArgumentException("La data di fine (" + fine + ") non può precedere quella di inizio (" + inizio + ").");
        }
    }

    private void validaStipendio(float stipendio) {
        if (stipendio < 0) {
            throw new IllegalArgumentException("Lo stipendio non può essere negativo: " + stipendio);
        }
    }

    public boolean isScaduto() {
        return LocalDate.now().isAfter(dataFineContratto);
    }

    public float getStipendio() { return stipendio; }

    public void setStipendio(float stipendio) {
        validaStipendio(stipendio);
        this.stipendio = stipendio;
    }

    public void setDataFineContratto(LocalDate nuovaScadenza) {
        validaData(this.dataInizioContratto, nuovaScadenza);
        this.dataFineContratto = nuovaScadenza;
    }

    public Calciatore getCalciatore() { return calciatore; }
    public Squadra getSquadra() { return squadra; }
    public LocalDate getDataInizioContratto() { return dataInizioContratto; }
    public LocalDate getDataFineContratto() { return dataFineContratto; }
}
package it.domain_model.giocatori;

import java.time.LocalDate;

public class Partita {
    private int idPartita;
    private Squadra squadraCasa;
    private Squadra squadraOspite;
    private int punteggioCasa;
    private int punteggioOspite;
    private LocalDate data;
    private String stagione;

    public Partita(Squadra squadraCasa, Squadra squadraOspite, LocalDate data, String stagione) {
        this.idPartita = 0;
        this.squadraCasa = squadraCasa;
        this.squadraOspite = squadraOspite;
        this.data = data;
        this.punteggioCasa = 0;
        this.punteggioOspite = 0;
        validaStagione(stagione);
    }

    public Partita(int idPartita, int punteggioCasa, int punteggioOspite, LocalDate data, Squadra squadraCasa, Squadra squadraOspite, String stagione) {
        this.idPartita = idPartita;
        this.punteggioCasa = punteggioCasa;
        this.punteggioOspite = punteggioOspite;
        this.data = data;
        this.squadraCasa = squadraCasa;
        this.squadraOspite = squadraOspite;
        validaStagione(stagione);
    }

    public int getIdPartita() { return idPartita; }
    public void setIdPartita(int idPartita) { this.idPartita = idPartita; }

    public Squadra getSquadraCasa() { return squadraCasa; }
    public void setSquadraCasa(Squadra squadraCasa) { this.squadraCasa = squadraCasa; }

    public Squadra getSquadraOspite() { return squadraOspite; }
    public void setSquadraOspite(Squadra squadraOspite) { this.squadraOspite = squadraOspite; }

    public int getPunteggioCasa() { return punteggioCasa; }
    public int getPunteggioOspite() { return punteggioOspite; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }


    private void validaStagione(String stagione) {
        if (stagione == null) {
            throw new IllegalArgumentException("La stagione non può essere null");
        }

        if (!stagione.matches("^[0-9]{4}/[0-9]{4}$")) {
            throw new IllegalArgumentException("Formato stagione non valido. Usa solo numeri nel formato YYYY/YYYY");
        }

        String[] anni = stagione.split("/");

        int start = Integer.parseInt(anni[0]);
        int end = Integer.parseInt(anni[1]);

        if (start < 1900) {
            throw new IllegalArgumentException("L'anno iniziale deve essere un numero >= 1900");
        }

        if (end != start + 1) {
            throw new IllegalArgumentException("La stagione deve coprire due anni consecutivi (es: 2023/2024)");
        }

        this.stagione = stagione;
    }
}
package it.domain_model.analisi;

import it.domain_model.giocatori.Calciatore;
import it.domain_model.giocatori.Partita;

import java.util.HashMap;
import java.util.Map;

public class StatisticheCalciatorePartita {

    private final Calciatore calciatore;
    private final Partita partita;

    // Overview
    private int minutiGiocati;
    private int gol;
    private int assist;
    private double xG;
    private double xA;

    // Attack
    private int tiriTotali;
    private int tiriInPorta;
    private int dribblingRiusciti;
    private int tocchiInAreaAvversaria;

    // Defence
    private int contrastiVinti;
    private int duelliAereiVinti;

    // Construction
    private int passaggiChiave;
    private int crossRiusciti;
    private double precisionePassaggi;

    // Goalkeeping
    private int parate;
    private int cleanSheet;

    // Discipline
    private int cartelliniGialli;
    private int cartelliniRossi;

    public StatisticheCalciatorePartita(Calciatore calciatore, Partita partita) {
        if (calciatore == null || partita == null)
            throw new IllegalArgumentException("Calciatore e partita sono obbligatori.");

        this.calciatore = calciatore;
        this.partita = partita;
    }

    private void validateNonNegative(int value, String msg) {
        if (value < 0) throw new IllegalArgumentException(msg);
    }

    private void validatePercentage(double value, String msg) {
        if (value < 0 || value > 100) throw new IllegalArgumentException(msg);
    }

    public void setMinutiGiocati(int minutiGiocati) {
        validateNonNegative(minutiGiocati, "Minuti giocati non validi.");
        this.minutiGiocati = minutiGiocati;
    }

    public void setGol(int gol) {
        validateNonNegative(gol, "I gol non possono essere negativi.");
        this.gol = gol;
    }

    public void setAssist(int assist) {
        validateNonNegative(assist, "Gli assist non possono essere negativi.");
        this.assist = assist;
    }

    public void setxG(double xG) {
        if (xG < 0) throw new IllegalArgumentException("xG non può essere negativo.");
        this.xG = xG;
    }

    public void setxA(double xA) {
        if (xA < 0) throw new IllegalArgumentException("xA non può essere negativo.");
        this.xA = xA;
    }

    public void setTiriTotali(int tiriTotali) {
        validateNonNegative(tiriTotali, "I tiri totali non possono essere negativi.");
        this.tiriTotali = tiriTotali;
    }

    public void setTiriInPorta(int tiriInPorta) {
        validateNonNegative(tiriInPorta, "I tiri in porta non possono essere negativi.");
        if (tiriInPorta > tiriTotali)
            throw new IllegalArgumentException("I tiri in porta non possono superare i tiri totali.");
        this.tiriInPorta = tiriInPorta;
    }

    public void setDribblingRiusciti(int dribblingRiusciti) {
        validateNonNegative(dribblingRiusciti, "I dribbling riusciti non possono essere negativi.");
        this.dribblingRiusciti = dribblingRiusciti;
    }

    public void setTocchiInAreaAvversaria(int tocchi) {
        validateNonNegative(tocchi, "I tocchi in area non possono essere negativi.");
        this.tocchiInAreaAvversaria = tocchi;
    }

    public void setContrastiVinti(int contrastiVinti) {
        validateNonNegative(contrastiVinti, "I contrasti vinti non possono essere negativi.");
        this.contrastiVinti = contrastiVinti;
    }

    public void setPassaggiChiave(int passaggiChiave) {
        validateNonNegative(passaggiChiave, "I passaggi chiave non possono essere negativi.");
        this.passaggiChiave = passaggiChiave;
    }

    public void setPrecisionePassaggi(double precisionePassaggi) {
        validatePercentage(precisionePassaggi, "La precisione passaggi deve essere tra 0 e 100.");
        this.precisionePassaggi = precisionePassaggi;
    }

    public void setParate(int parate) {
        validateNonNegative(parate, "Le parate non possono essere negative.");
        this.parate = parate;
    }

    public void setCleanSheet(int cleanSheet) {
        validateNonNegative(cleanSheet, "I clean sheet non possono essere negativi.");
        this.cleanSheet = cleanSheet;
    }

    public void setCartelliniGialli(int gialli) {
        if (gialli < 0 || gialli > 2)
            throw new IllegalArgumentException("Gialli per partita devono essere tra 0 e 2.");
        this.cartelliniGialli = gialli;
    }

    public void setCartelliniRossi(int rossi) {
        if (rossi < 0 || rossi > 1)
            throw new IllegalArgumentException("Rossi per partita devono essere 0 o 1.");
        this.cartelliniRossi = rossi;
    }

    public int getDuelliAereiVinti() { return duelliAereiVinti; }
    public int getCrossRiusciti() { return crossRiusciti; }
    public int getMinutiGiocati() { return minutiGiocati; }
    public int getGol() { return gol; }
    public int getAssist() { return assist; }
    public double getxG() { return xG; }
    public double getxA() { return xA; }
    public int getTiriTotali() { return tiriTotali; }
    public int getTiriInPorta() { return tiriInPorta; }
    public int getDribblingRiusciti() { return dribblingRiusciti; }
    public int getTocchiInAreaAvversaria() { return tocchiInAreaAvversaria; }
    public int getContrastiVinti() { return contrastiVinti; }
    public int getPassaggiChiave() { return passaggiChiave; }
    public double getPrecisionePassaggi() { return precisionePassaggi; }
    public int getParate() { return parate; }
    public int getCleanSheet() { return cleanSheet; }
    public int getCartelliniGialli() { return cartelliniGialli; }
    public int getCartelliniRossi() { return cartelliniRossi; }

}

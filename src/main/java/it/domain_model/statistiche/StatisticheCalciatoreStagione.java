package it.domain_model.statistiche;

public class StatisticheCalciatoreStagione {

    private String stagione;
    private int minutiGiocati;
    private int gol;
    private int assist;
    private double xG;
    private double xA;

    // Attacco
    private int tiriTotali;
    private int tiriInPorta;
    private int dribblingRiusciti;
    private int tocchiInAreaAvversaria;

    // Difesa
    private int contrastiVinti;
    private int duelliAereiVinti;

    // Costruzione
    private int passaggiChiave;
    private int crossRiusciti;
    private int passaggiRealizzati;

    // Portiere
    private int parate;
    private int cleanSheet;

    // Disciplina
    private int cartelliniGialli;
    private int cartelliniRossi;

    public StatisticheCalciatoreStagione(int minutiGiocati, int gol, int assist, double xG, double xA,
                                         int tiriTotali, int tiriInPorta, int dribblingRiusciti,
                                         int tocchiInAreaAvversaria, int contrastiVinti, int duelliAereiVinti,
                                         int passaggiChiave, int crossRiusciti, int passaggiRealizzati,
                                         int parate, int cleanSheet, int cartelliniGialli, int cartelliniRossi,
                                         String stagione) {
        this.setMinutiGiocati(minutiGiocati);
        this.setGol(gol);
        this.setAssist(assist);
        this.setxG(xG);
        this.setxA(xA);
        this.setTiriTotali(tiriTotali);
        this.setTiriInPorta(tiriInPorta);
        this.setDribblingRiusciti(dribblingRiusciti);
        this.setTocchiInAreaAvversaria(tocchiInAreaAvversaria);
        this.setContrastiVinti(contrastiVinti);
        this.setDuelliAereiVinti(duelliAereiVinti);
        this.setPassaggiChiave(passaggiChiave);
        this.setCrossRiusciti(crossRiusciti);
        this.setPassaggiRealizzati(passaggiRealizzati);
        this.setParate(parate);
        this.setCleanSheet(cleanSheet);
        this.setCartelliniGialli(cartelliniGialli);
        this.setCartelliniRossi(cartelliniRossi);
        this.setStagione(stagione);
    }

    private void validateNonNegative(int value, String msg) {
        if (value < 0) throw new IllegalArgumentException(msg);
    }

    private void validateNonNegative(double value, String msg) {
        if (value < 0) throw new IllegalArgumentException(msg);
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
        validateNonNegative(xG, "xG non può essere negativo.");
        this.xG = xG;
    }

    public void setxA(double xA) {
        validateNonNegative(xA, "xA non può essere negativo.");
        this.xA = xA;
    }

    public void setTiriTotali(int tiriTotali) {
        validateNonNegative(tiriTotali, "I tiri totali non possono essere negativi.");
        this.tiriTotali = tiriTotali;
    }

    public void setTiriInPorta(int tiriInPorta) {
        validateNonNegative(tiriInPorta, "I tiri in porta non possono essere negativi.");
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

    public void setDuelliAereiVinti(int duelli) {
        validateNonNegative(duelli, "I duelli aerei vinti non possono essere negativi.");
        this.duelliAereiVinti = duelli;
    }

    public void setPassaggiChiave(int passaggiChiave) {
        validateNonNegative(passaggiChiave, "I passaggi chiave non possono essere negativi.");
        this.passaggiChiave = passaggiChiave;
    }

    public void setCrossRiusciti(int cross) {
        validateNonNegative(cross, "I cross riusciti non possono essere negativi.");
        this.crossRiusciti = cross;
    }

    public void setPassaggiRealizzati(int passaggiRealizzati) {
        validateNonNegative(passaggiRealizzati, "I passaggi realizzati non possono essere negativi.");
        this.passaggiRealizzati = passaggiRealizzati;
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
        validateNonNegative(gialli, "I cartellini gialli non possono essere negativi.");
        this.cartelliniGialli = gialli;
    }

    public void setCartelliniRossi(int rossi) {
        validateNonNegative(rossi, "I cartellini rossi non possono essere negativi.");
        this.cartelliniRossi = rossi;
    }

    public void setStagione(String stagione) {
        if (stagione == null || stagione.isBlank())
            throw new IllegalArgumentException("Stagione non valida.");
        this.stagione = stagione;
    }

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
    public int getDuelliAereiVinti() { return duelliAereiVinti; }
    public int getPassaggiChiave() { return passaggiChiave; }
    public int getCrossRiusciti() { return crossRiusciti; }
    public int getPassaggiRealizzati() { return passaggiRealizzati; }
    public int getParate() { return parate; }
    public int getCleanSheet() { return cleanSheet; }
    public int getCartelliniGialli() { return cartelliniGialli; }
    public int getCartelliniRossi() { return cartelliniRossi; }
    public String getStagione() { return stagione; }
}
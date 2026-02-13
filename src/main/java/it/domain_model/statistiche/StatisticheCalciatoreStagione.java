package it.domain_model.statistiche;

public class StatisticheCalciatoreStagione {

    private int minutiGiocati;
    private double gol;
    private double assist;
    private double xG;
    private double xA;

    private double tiriTotali;
    private double tiriInPorta;
    private double dribblingRiusciti;
    private double tocchiInAreaAvversaria;

    private double contrastiVinti;
    private double duelliAereiVinti;

    private double passaggiChiave;
    private double crossRiusciti;
    private double passaggiRealizzati;

    private double parate;
    private double cleanSheet;

    private int cartelliniGialli;
    private int cartelliniRossi;

    public StatisticheCalciatoreStagione(int minutiGiocati, double gol, double assist, double xG, double xA,
                                         double tiriTotali, double tiriInPorta, double dribblingRiusciti,
                                         double tocchiInAreaAvversaria, double contrastiVinti, double duelliAereiVinti,
                                         double passaggiChiave, double crossRiusciti, double passaggiRealizzati,
                                         double parate, double cleanSheet, int cartelliniGialli, int cartelliniRossi) {

        setMinutiGiocati(minutiGiocati);
        setGol(gol);
        setAssist(assist);
        setxG(xG);
        setxA(xA);
        setTiriTotali(tiriTotali);
        setTiriInPorta(tiriInPorta);
        setDribblingRiusciti(dribblingRiusciti);
        setTocchiInAreaAvversaria(tocchiInAreaAvversaria);
        setContrastiVinti(contrastiVinti);
        setDuelliAereiVinti(duelliAereiVinti);
        setPassaggiChiave(passaggiChiave);
        setCrossRiusciti(crossRiusciti);
        setPassaggiRealizzati(passaggiRealizzati);
        setParate(parate);
        setCleanSheet(cleanSheet);
        setCartelliniGialli(cartelliniGialli);
        setCartelliniRossi(cartelliniRossi);
    }

    private void validateNonNegative(double value, String msg) {
        if (value < 0) throw new IllegalArgumentException(msg);
    }

    private void validateNonNegativeInt(int value, String msg) {
        if (value < 0) throw new IllegalArgumentException(msg);
    }

    // --- SETTERS ---

    public void setMinutiGiocati(int minutiGiocati) {
        validateNonNegativeInt(minutiGiocati, "Minuti giocati non validi.");
        this.minutiGiocati = minutiGiocati;
    }

    public void setGol(double gol) {
        validateNonNegative(gol, "I gol non possono essere negativi.");
        this.gol = gol;
    }

    public void setAssist(double assist) {
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

    public void setTiriTotali(double tiriTotali) {
        validateNonNegative(tiriTotali, "I tiri totali non possono essere negativi.");
        this.tiriTotali = tiriTotali;
    }

    public void setTiriInPorta(double tiriInPorta) {
        validateNonNegative(tiriInPorta, "I tiri in porta non possono essere negativi.");
        this.tiriInPorta = tiriInPorta;
    }

    public void setDribblingRiusciti(double dribblingRiusciti) {
        validateNonNegative(dribblingRiusciti, "I dribbling riusciti non possono essere negativi.");
        this.dribblingRiusciti = dribblingRiusciti;
    }

    public void setTocchiInAreaAvversaria(double tocchi) {
        validateNonNegative(tocchi, "I tocchi in area non possono essere negativi.");
        this.tocchiInAreaAvversaria = tocchi;
    }

    public void setContrastiVinti(double contrastiVinti) {
        validateNonNegative(contrastiVinti, "I contrasti vinti non possono essere negativi.");
        this.contrastiVinti = contrastiVinti;
    }

    public void setDuelliAereiVinti(double duelli) {
        validateNonNegative(duelli, "I duelli aerei vinti non possono essere negativi.");
        this.duelliAereiVinti = duelli;
    }

    public void setPassaggiChiave(double passaggiChiave) {
        validateNonNegative(passaggiChiave, "I passaggi chiave non possono essere negativi.");
        this.passaggiChiave = passaggiChiave;
    }

    public void setCrossRiusciti(double cross) {
        validateNonNegative(cross, "I cross riusciti non possono essere negativi.");
        this.crossRiusciti = cross;
    }

    public void setPassaggiRealizzati(double passaggiRealizzati) {
        validateNonNegative(passaggiRealizzati, "I passaggi realizzati non possono essere negativi.");
        this.passaggiRealizzati = passaggiRealizzati;
    }

    public void setParate(double parate) {
        validateNonNegative(parate, "Le parate non possono essere negative.");
        this.parate = parate;
    }

    public void setCleanSheet(double cleanSheet) {
        validateNonNegative(cleanSheet, "I clean sheet non possono essere negativi.");
        this.cleanSheet = cleanSheet;
    }

    public void setCartelliniGialli(int gialli) {
        validateNonNegativeInt(gialli, "I cartellini gialli non possono essere negativi.");
        this.cartelliniGialli = gialli;
    }

    public void setCartelliniRossi(int rossi) {
        validateNonNegativeInt(rossi, "I cartellini rossi non possono essere negativi.");
        this.cartelliniRossi = rossi;
    }

    public int getMinutiGiocati() { return minutiGiocati; }
    public double getGol() { return gol; }
    public double getAssist() { return assist; }
    public double getxG() { return xG; }
    public double getxA() { return xA; }
    public double getTiriTotali() { return tiriTotali; }
    public double getTiriInPorta() { return tiriInPorta; }
    public double getDribblingRiusciti() { return dribblingRiusciti; }
    public double getTocchiInAreaAvversaria() { return tocchiInAreaAvversaria; }
    public double getContrastiVinti() { return contrastiVinti; }
    public double getDuelliAereiVinti() { return duelliAereiVinti; }
    public double getPassaggiChiave() { return passaggiChiave; }
    public double getCrossRiusciti() { return crossRiusciti; }
    public double getPassaggiRealizzati() { return passaggiRealizzati; }
    public double getParate() { return parate; }
    public double getCleanSheet() { return cleanSheet; }
    public int getCartelliniGialli() { return cartelliniGialli; }
    public int getCartelliniRossi() { return cartelliniRossi; }
}
package it.domain_model.analisi;

public class RisultatoRanking {
    private int idCalciatore;
    private String nomeCompleto;
    private double valore;
    private int minutiTotali;

    public RisultatoRanking(int idCalciatore, String nome, double valore, int minuti) {
        this.idCalciatore = idCalciatore;
        this.nomeCompleto = nome;
        this.valore = valore;
        this.minutiTotali = minuti;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public double getValore() {
        return valore;
    }

    public int getMinutiTotali() {
        return minutiTotali;
    }

    public void setIdCalciatore(int idCalciatore) {
        this.idCalciatore = idCalciatore;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public void setValore(double valore) {
        this.valore = valore;
    }

    public void setMinutiTotali(int minutiTotali) {
        this.minutiTotali = minutiTotali;
    }
}

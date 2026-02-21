package it.domain_model.giocatori;

public class Squadra {
    private int idSquadra;
    private String nome;
    private String campionato;
    private String nazione;

    public Squadra(int idSquadra, String nome, String campionato, String nazione) {
        if(nome == null || nome.isBlank()) throw new IllegalArgumentException("Il nome del calciatore non può essere vuoto");
        this.idSquadra = idSquadra;
        this.nome = nome;
        this.campionato = campionato;
        this.nazione = nazione;
    }

    public int getIdSquadra() { return idSquadra; }

    public String getNome() { return nome; }

    public String getCampionato() { return campionato; }

    public String getNazione() { return nazione; }
}
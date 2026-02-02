package it.domain_model.giocatori;

public class Squadra {
    private int idSquadra;
    private String nome;
    private String campionato;
    private String nazione;

    public Squadra(int idSquadra, String nome, String campionato, String nazione) {
        this.idSquadra = idSquadra;
        setNome(nome);
        this.campionato = campionato;
        this.nazione = nazione;
    }

    // Getter e Setter per l'ID
    public int getIdSquadra() { return idSquadra; }
    public void setIdSquadra(int idSquadra) { this.idSquadra = idSquadra; }

    public String getNome() { return nome; }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome della squadra non può essere vuoto.");
        }
        this.nome = nome;
    }

    public String getCampionato() { return campionato; }
    public void setCampionato(String campionato) { this.campionato = campionato; }

    public String getNazione() { return nazione; }
    public void setNazione(String nazione) { this.nazione = nazione; }
}
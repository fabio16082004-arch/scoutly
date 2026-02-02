package it.domain_model.giocatori;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Calciatore {
    private final int id;
    private final String nome;
    private final String cognome;
    private final LocalDate dataNascita;
    private final String nazionalita;

    private float peso;
    private float altezza;
    private Set<Ruolo> ruoli;

    private Contratto contrattoAttuale;

    public Calciatore(int id, String nome, String cognome, LocalDate dataNascita,
                      String nazionalita, float peso, float altezza, Set<Ruolo> ruoli) {

        if (nome == null || nome.isBlank() || cognome == null || cognome.isBlank()) {
            throw new IllegalArgumentException("Nome e cognome sono obbligatori.");
        }

        if (dataNascita == null || Period.between(dataNascita, LocalDate.now()).getYears() < 14) {
            throw new IllegalArgumentException("Il calciatore deve avere almeno 14 anni.");
        }

        if (peso <= 0 || altezza <= 0) {
            throw new IllegalArgumentException("Peso e altezza devono essere positivi.");
        }

        if (ruoli == null || ruoli.isEmpty()) {
            throw new IllegalArgumentException("Il calciatore deve avere almeno un ruolo.");
        }

        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.nazionalita = nazionalita;
        this.peso = peso;
        this.altezza = altezza;
        this.ruoli = new HashSet<>(ruoli);
    }

    public void setContrattoAttuale(Contratto contratto) {
        if (contratto == null) {
            this.contrattoAttuale = null;
            return;
        }

        if (contratto.getCalciatore() != this) {
            throw new IllegalArgumentException("Questo contratto non appartiene a questo calciatore.");
        }
        this.contrattoAttuale = contratto;
    }

    public boolean isSvincolato() {
        return contrattoAttuale == null || contrattoAttuale.isScaduto();
    }

    public int getEta() {
        return Period.between(dataNascita, LocalDate.now()).getYears();
    }

    public void setPeso(float peso) {
        if (peso <= 0) throw new IllegalArgumentException("Peso non valido.");
        this.peso = peso;
    }

    public void setAltezza(float altezza) {
        if (altezza <= 0) throw new IllegalArgumentException("Altezza non valida.");
        this.altezza = altezza;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public LocalDate getDataNascita() { return dataNascita; }
    public String getNazionalita() { return nazionalita; }
    public float getPeso() { return peso; }
    public float getAltezza() { return altezza; }

    public Set<Ruolo> getRuoli() {
        return Collections.unmodifiableSet(ruoli);
    }

    public Contratto getContrattoAttuale() {
        return contrattoAttuale;
    }
}
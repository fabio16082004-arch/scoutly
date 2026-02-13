package it.domain_model.giocatori;

import java.util.List;
import java.util.Collections;

public enum Ruolo {

    // PORTIERI
    PORTIERE("POR", "Portiere"),

    // DIFENSORI
    DIFENSORE_CENTRALE("DC", "Difensore centrale"),
    TERZINO_DESTRO("TD", "Terzino destro"),
    TERZINO_SINISTRO("TS", "Terzino sinistro"),

    // CENTROCAMPISTI
    ESTERNO_DESTRO("ED", "Esterno destro"),
    ESTERNO_SINISTRO("ES", "Esterno sinistro"),
    MEDIANO("MED", "Centrocampista difensivo"),
    REGISTA("CC", "Centrocampista centrale"),
    TREQUARTISTA("TRQ", "Trequartista"),

    // ATTACCANTI
    ALA_DESTRA("AD", "Ala destra"),
    ALA_SINISTRA("AS", "Ala sinistra"),
    PUNTA_CENTRALE("PC", "Punta centrale");

    private final String sigla;
    private final String descrizioneEstesa;

    Ruolo(String sigla, String descrizioneEstesa) {
        this.sigla = sigla;
        this.descrizioneEstesa = descrizioneEstesa;
    }

    public String getSigla() { return sigla; }
    public String getDescrizioneEstesa() { return descrizioneEstesa; }

}
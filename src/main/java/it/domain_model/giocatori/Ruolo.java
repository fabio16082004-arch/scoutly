package it.domain_model.giocatori;

import java.util.List;
import java.util.Collections;

public enum Ruolo {

    // PORTIERI
    PORTIERE("POR", "Portiere", "Portiere"),

    // DIFENSORI
    DIFENSORE_CENTRALE("DC", "Difensore centrale", "Difensore"),
    TERZINO_DESTRO("TD", "Terzino destro", "Difensore"),
    TERZINO_SINISTRO("TS", "Terzino sinistro", "Difensore"),

    // CENTROCAMPISTI
    ESTERNO_DESTRO("ED", "Esterno destro", "Centrocampista"),
    ESTERNO_SINISTRO("ES", "Esterno sinistro", "Centrocampista"),
    MEDIANO("MED", "Centrocampista difensivo", "Centrocampista"),
    REGISTA("CC", "Centrocampista centrale", "Centrocampista"),
    TREQUARTISTA("TRQ", "Trequartista", "Centrocampista"),

    // ATTACCANTI
    ALA_DESTRA("AD", "Ala destra", "Attaccante"),
    ALA_SINISTRA("AS", "Ala sinistra", "Attaccante"),
    PUNTA_CENTRALE("PC", "Punta centrale", "Attaccante");

    private final String macroRuolo;
    private final String sigla;
    private final String descrizioneEstesa;

    Ruolo(String sigla, String descrizioneEstesa, String macroRuolo) {
        this.sigla = sigla;
        this.descrizioneEstesa = descrizioneEstesa;
        this.macroRuolo = macroRuolo;
    }

    public static Ruolo valueOfSigla(String sigla) {
        for (Ruolo r : Ruolo.values()) {
            if (r.getSigla().equals(sigla)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Nessun ruolo trovato per la sigla: " + sigla);
    }

    public String getSigla() { return sigla; }
    public String getDescrizioneEstesa() { return descrizioneEstesa; }
    public String getMacroRuolo() { return macroRuolo; }


}
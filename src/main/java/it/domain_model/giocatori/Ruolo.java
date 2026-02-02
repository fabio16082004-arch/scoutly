package it.domain_model.giocatori;

import java.util.List;
import java.util.Collections;

public enum Ruolo {

    // PORTIERI
    PORTIERE("POR", "Portiere", "Portiere",
            List.of("parate", "cleanSheet", "precisionePassaggi")),

    // DIFENSORI
    DIFENSORE_CENTRALE("DC", "Difensore centrale", "Difensore",
            List.of("passaggiChiave", "precisionePassaggi", "dribblingRiusciti", "contrastiVinti", "crossRiusciti")),
    TERZINO_DESTRO("TD", "Terzino destro", "Difensore",
            List.of("passaggiChiave", "precisionePassaggi", "dribblingRiusciti", "contrastiVinti", "crossRiusciti")),
    TERZINO_SINISTRO("TS", "Terzino sinistro", "Difensore",
            List.of("passaggiChiave", "precisionePassaggi", "dribblingRiusciti", "contrastiVinti", "crossRiusciti")),

    // CENTROCAMPISTI
    ESTERNO_DESTRO("ED", "Esterno destro", "Centrocampista",
            List.of("passaggiChiave", "precisionePassaggi", "dribblingRiusciti", "contrastiVinti", "crossRiusciti")),
    ESTERNO_SINISTRO("ES", "Esterno sinistro", "Centrocampista",
            List.of("passaggiChiave", "precisionePassaggi", "dribblingRiusciti", "contrastiVinti", "crossRiusciti")),
    MEDIANO("MED", "Centrocampista difensivo", "Centrocampista",
            List.of("passaggiChiave", "precisionePassaggi", "dribblingRiusciti", "contrastiVinti", "crossRiusciti")),
    REGISTA("CC", "Centrocampista centrale", "Centrocampista",
            List.of("passaggiChiave", "precisionePassaggi", "dribblingRiusciti", "contrastiVinti", "crossRiusciti")),
    TREQUARTISTA("TRQ", "Trequartista", "Centrocampista",
            List.of("passaggiChiave", "precisionePassaggi", "dribblingRiusciti", "contrastiVinti", "crossRiusciti")),

    // ATTACCANTI
    ALA_DESTRA("AD", "Ala destra", "Attaccante",
            List.of("tiriTotali", "tiriInPorta", "tocchiInAreaAvversaria", "dribblingRiusciti", "crossRiusciti")),
    ALA_SINISTRA("AS", "Ala sinistra", "Attaccante",
            List.of("tiriTotali", "tiriInPorta", "tocchiInAreaAvversaria", "dribblingRiusciti", "crossRiusciti")),
    PUNTA_CENTRALE("PC", "Punta centrale", "Attaccante",
            List.of("tiriTotali", "tiriInPorta", "tocchiInAreaAvversaria", "dribblingRiusciti", "crossRiusciti"));

    private final String macroRuolo;
    private final String sigla;
    private final String descrizioneEstesa;
    private final List<String> metricheSpecifiche;

    Ruolo(String sigla, String descrizioneEstesa, String macroRuolo, List<String> metricheSpecifiche) {
        this.sigla = sigla;
        this.descrizioneEstesa = descrizioneEstesa;
        this.macroRuolo = macroRuolo;
        this.metricheSpecifiche = metricheSpecifiche;
    }

    public String getSigla() { return sigla; }
    public String getDescrizioneEstesa() { return descrizioneEstesa; }
    public String getMacroRuolo() { return macroRuolo; }


    public List<String> getMetricheSpecifiche() {
        return Collections.unmodifiableList(metricheSpecifiche);
    }
}
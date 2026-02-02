package it.domain_model.trattamento_statistiche;

import it.domain_model.analisi.StatisticheCalciatoreStagione;
import it.domain_model.giocatori.Ruolo;

import java.util.HashMap;
import java.util.Map;

public class StrategiaNormalizzata implements StrategiaElaborazioneStatistiche {

    @Override
    public StatisticheCalciatoreStagione elabora(StatisticheCalciatoreStagione rawStats) {
        if (rawStats == null) return null;

        int minuti = rawStats.getMinutiGiocati();
        double factor = (minuti > 0) ? (90.0 / minuti) : 0.0;

        return new StatisticheCalciatoreStagione(
                minuti,
                rawStats.getGol() * factor,
                rawStats.getAssist() * factor,
                rawStats.getxG() * factor,
                rawStats.getxA() * factor,
                rawStats.getTiriTotali() * factor,
                rawStats.getTiriInPorta() * factor,
                rawStats.getDribblingRiusciti() * factor,
                rawStats.getTocchiInAreaAvversaria() * factor,
                rawStats.getContrastiVinti() * factor,
                rawStats.getDuelliAereiVinti() * factor,
                rawStats.getPassaggiChiave() * factor,
                rawStats.getCrossRiusciti() * factor,
                rawStats.getPassaggiRealizzati() * factor,
                rawStats.getParate() * factor,
                rawStats.getCleanSheet() * factor,
                rawStats.getCartelliniGialli(),
                rawStats.getCartelliniRossi()
        );
    }

}

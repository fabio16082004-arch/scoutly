package it.domain_model.trattamento_statistiche;

import it.domain_model.analisi.RisultatoRanking;
import it.domain_model.analisi.StatisticheCalciatoreStagione;
import it.domain_model.giocatori.Ruolo;

import java.util.List;
import java.util.Map;

public class StrategiaAssoluta implements StrategiaElaborazioneStatistiche {

    @Override
    public StatisticheCalciatoreStagione elabora(StatisticheCalciatoreStagione rawStats) {
        return rawStats;
    }

    @Override
    public void applicaAlRanking(List<RisultatoRanking> ranking, String statistica) {

    }
}

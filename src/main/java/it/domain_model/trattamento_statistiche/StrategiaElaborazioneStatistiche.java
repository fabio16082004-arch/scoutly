package it.domain_model.trattamento_statistiche;

import it.domain_model.analisi.RisultatoRanking;
import it.domain_model.analisi.StatisticheCalciatoreStagione;

import java.util.List;

public interface StrategiaElaborazioneStatistiche {
    StatisticheCalciatoreStagione elabora(StatisticheCalciatoreStagione rawStats);

    void applicaAlRanking(List<RisultatoRanking> ranking, String statistica);
}

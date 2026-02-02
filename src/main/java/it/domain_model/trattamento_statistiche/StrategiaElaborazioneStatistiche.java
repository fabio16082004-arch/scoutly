package it.domain_model.trattamento_statistiche;

import it.domain_model.analisi.StatisticheCalciatoreStagione;
import it.domain_model.giocatori.Ruolo;
import java.util.Map;

public interface StrategiaElaborazioneStatistiche {
    StatisticheCalciatoreStagione elabora(StatisticheCalciatoreStagione rawStats);
}

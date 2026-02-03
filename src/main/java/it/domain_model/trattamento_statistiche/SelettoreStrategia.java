package it.domain_model.trattamento_statistiche;

import it.domain_model.analisi.RisultatoRanking;
import it.domain_model.analisi.StatisticheCalciatoreStagione;

import java.util.List;

public class SelettoreStrategia {
    StrategiaElaborazioneStatistiche ses;

    public void selezionaStrategia(String strategia){
        if(strategia.equals("assoluto"))
            ses = new StrategiaAssoluta();
        else
            ses = new StrategiaNormalizzata();
    }

    public StatisticheCalciatoreStagione elabora(StatisticheCalciatoreStagione rawData){
        return ses.elabora(rawData);
    }

    public void applicaAlRanking(List<RisultatoRanking> ranking, String statistica) {
        ses.applicaAlRanking(ranking, statistica);
    }
}

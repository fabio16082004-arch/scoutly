package it.domain_model.analisi;

import it.domain_model.giocatori.Calciatore;

import java.util.Map;

public class RisultatoStatisticheCalciatore {

    private final Calciatore calciatore;
    private final Map<String, Double> statistiche;

    public RisultatoStatisticheCalciatore(
            Calciatore calciatore,
            Map<String, Double> statistiche
    ) {
        this.calciatore = calciatore;
        this.statistiche = Map.copyOf(statistiche);
    }

    public Calciatore getCalciatore() {
        return calciatore;
    }

    public Map<String, Double> getStatistiche() {
        return statistiche;
    }

    public Double getStat(String metrica) {
        return statistiche.get(metrica);
    }
}


package it.domain_model.analisi;

import it.domain_model.giocatori.Calciatore;

import java.util.List;
import java.util.Map;

public class RisultatoConfrontoCalciatori {

    private final List<RisultatoStatisticheCalciatore> risultati;
    private final Map<String, Calciatore> miglioriPerMetrica;

    public RisultatoConfrontoCalciatori(
            List<RisultatoStatisticheCalciatore> risultati,
            Map<String, Calciatore> miglioriPerMetrica
    ) {
        this.risultati = List.copyOf(risultati);
        this.miglioriPerMetrica = Map.copyOf(miglioriPerMetrica);
    }

    public List<RisultatoStatisticheCalciatore> getRisultati() {
        return risultati;
    }

    public Calciatore getMigliore(String metrica) {
        return miglioriPerMetrica.get(metrica);
    }
}

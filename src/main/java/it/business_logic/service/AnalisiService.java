package it.business_logic.service;

import it.ORM.DAO.CalciatoreDAO;
import it.ORM.DAO.ContrattoDAO;
import it.ORM.DAO.StatisticheDAO;
import it.domain_model.giocatori.Contratto;
import it.domain_model.statistiche.StatisticheCalciatoreStagione;

import it.business_logic.analisi.CalciatoreFiltro;
import it.domain_model.giocatori.Calciatore;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnalisiService {
    private final StatisticheDAO statisticheDAO;
    private final CalciatoreDAO calciatoreDAO;
    private final ContrattoDAO contrattoDAO;

    public AnalisiService(StatisticheDAO sDAO, CalciatoreDAO cDAO, ContrattoDAO coDAO) {
        this.statisticheDAO = sDAO;
        this.calciatoreDAO = cDAO;
        this.contrattoDAO = coDAO;
    }

    public Map<Calciatore, Float> cercaGiocatori(CalciatoreFiltro filtro) {
        List<Calciatore> calciatori = calciatoreDAO.cerca(filtro);
        Map<Calciatore, Float> mappaRisultati = new LinkedHashMap<>();

        for (Calciatore c : calciatori) {
            Contratto con = contrattoDAO.getContrattoAttuale(c);
            float stipendio = (con != null) ? con.getStipendio() : 0;

            mappaRisultati.put(c, stipendio);
        }
        return mappaRisultati;
    }

    public StatisticheCalciatoreStagione getStats(int id, String stagione, boolean normalizza) {
        StatisticheCalciatoreStagione stats = statisticheDAO.getStatisticheCalciatorePerStagione(id, stagione);

        if (stats != null && normalizza) {
            return normalizzaPer90(stats);
        }
        return stats;
    }

    private StatisticheCalciatoreStagione normalizzaPer90(StatisticheCalciatoreStagione s) {
        double factor = (s.getMinutiGiocati() > 0) ? (90.0 / s.getMinutiGiocati()) : 0.0;

        return new StatisticheCalciatoreStagione(
                s.getMinutiGiocati(),
                s.getGol() * factor,
                s.getAssist() * factor,
                s.getxG() * factor,
                s.getxA() * factor,
                s.getTiriTotali() * factor,
                s.getTiriInPorta() * factor,
                s.getDribblingRiusciti() * factor,
                s.getTocchiInAreaAvversaria() * factor,
                s.getContrastiVinti() * factor,
                s.getDuelliAereiVinti() * factor,
                s.getPassaggiChiave() * factor,
                s.getCrossRiusciti() * factor,
                s.getPassaggiRealizzati() * factor,
                s.getParate() * factor,
                s.getCleanSheet() * factor,
                s.getCartelliniGialli(),
                s.getCartelliniRossi()
        );
    }
}

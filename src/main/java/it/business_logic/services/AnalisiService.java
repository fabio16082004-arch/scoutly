package it.business_logic.services;

import it.DTO.SchedaAvanzataDTO;
import it.ORM.DAO.CalciatoreDAO;
import it.ORM.DAO.ContrattoDAO;
import it.ORM.DAO.StatisticheDAO;
import it.domain_model.giocatori.Contratto;
import it.domain_model.giocatori.Ruolo;
import it.domain_model.statistiche.StatisticheCalciatoreStagione;

import it.business_logic.filtro.CalciatoreFiltro;
import it.domain_model.giocatori.Calciatore;

import java.util.*;

public class AnalisiService {
    private final StatisticheDAO statisticheDAO;
    private final CalciatoreDAO calciatoreDAO;
    private final ContrattoDAO contrattoDAO;

    public AnalisiService(StatisticheDAO sDAO, CalciatoreDAO cDAO, ContrattoDAO coDAO) {
        this.statisticheDAO = sDAO;
        this.calciatoreDAO = cDAO;
        this.contrattoDAO = coDAO;
    }

    public List<Calciatore> cercaGiocatori(CalciatoreFiltro filtro) {
        return calciatoreDAO.cerca(filtro);
    }

    public SchedaAvanzataDTO getStats(int id, String stagione) {
        Calciatore c = calciatoreDAO.getById(id);
         StatisticheCalciatoreStagione stc = statisticheDAO.getStatisticheCalciatorePerStagione(id, stagione);
         Contratto co = contrattoDAO.getContrattoAttuale(c);

        Set<String> ruoliStringa = new HashSet<>();
        for (Ruolo ruolo : c.getRuoli()) {
            ruoliStringa.add(ruolo.getSigla());
        }

        Map<String, Number> stats = new HashMap<>();
        stats.put("minutiGiocati", stc.getMinutiGiocati());
        stats.put("gol", stc.getGol());
        stats.put("assist", stc.getAssist());
        stats.put("xG", stc.getxG());
        stats.put("xA", stc.getxA());
        stats.put("cartelliniGialli", stc.getCartelliniGialli());
        stats.put("cartelliniRossi", stc.getCartelliniRossi());

        if (c.getRuoli().iterator().next() == Ruolo.PORTIERE) {
            stats.put("parate", stc.getParate());
            stats.put("cleanSheet", stc.getCleanSheet());
        }else{
            stats.put("tiriTotali", stc.getTiriTotali());
            stats.put("tiriInPorta", stc.getTiriInPorta());
            stats.put("dribblingRiusciti", stc.getDribblingRiusciti());
            stats.put("tocchiInAreaAvversaria", stc.getTocchiInAreaAvversaria());
            stats.put("contrastiVinti", stc.getContrastiVinti());
            stats.put("duelliAereiVinti", stc.getDuelliAereiVinti());
            stats.put("passaggiChiave", stc.getPassaggiChiave());
            stats.put("crossRiusciti", stc.getCrossRiusciti());
            stats.put("passaggiRealizzati", stc.getPassaggiRealizzati());
        }

         return new SchedaAvanzataDTO(
                 c.getNome(),
                 c.getCognome(),
                 c.getEta(),
                 c.getNazionalita(),
                 c.getPeso(),
                 c.getAltezza(),
                 ruoliStringa,
                 co.getSquadra().getNome(),
                 co.getDataFineContratto(),
                 co.getStipendio(),
                 stats
         );
    }
}

package it.business_logic.controllers;

import it.business_logic.analisi.CalciatoreFiltro;
import it.domain_model.scouting.Lista;
import it.domain_model.statistiche.StatisticheCalciatoreStagione;
import it.business_logic.service.AnalisiService;
import it.business_logic.service.ListaService;
import it.business_logic.service.ReportService;
import it.domain_model.giocatori.Calciatore;
import it.domain_model.scouting.Report;
import it.domain_model.utenti.Osservatore;

import java.util.List;
import java.util.Map;

public class OsservatoreController {
    private final ReportService scoutingService;
    private final ListaService listService;
    private final AnalisiService analisiService;

    public OsservatoreController(ReportService scoutingService,
                                 ListaService listService,
                                 AnalisiService analisiService) {
        this.scoutingService = scoutingService;
        this.listService = listService;
        this.analisiService = analisiService;
    }

    public Report creaNuovoReport(int idOsservatore, int idCalciatore,
                                  List<Integer> idPartite, Map<String, Integer> voti,
                                  String noteFinali, boolean calcolaVoto, int votoComplessivo) {

        return scoutingService.creaReport(idOsservatore, idCalciatore, idPartite, voti, noteFinali, calcolaVoto, votoComplessivo);
    }

    public void cancellaReport(int idReport) {
        scoutingService.eliminaReport(idReport);
    }

    public Report ottieniReport(int idReport){
        return scoutingService.ottieniReport(idReport);
    }

    public void creaNuovaLista(String nome, String descrizione, Osservatore osservatore) {
        listService.creaLista(nome, descrizione, osservatore); //
    }

    public void aggiungiCalciatoreALista(int idLista, int idCalciatore) {
        listService.aggiungiCalciatore(idLista, idCalciatore); // Passaggio di ID pulito
    }

    public void rimuoviCalciatoreDaLista(int idLista, int idCalciatore) {
        listService.rimuoviCalciatore(idLista, idCalciatore); //
    }

    public void eliminaLista(int idLista) {
        listService.eliminaLista(idLista);
    }

    public Map<Calciatore, Float> cercaGiocatori(CalciatoreFiltro filtro) {
        return analisiService.cercaGiocatori(filtro);
    }

    public StatisticheCalciatoreStagione visualizzaDettagliCalciatore(int idCalciatore, String stagione, boolean normalizza) {
        return analisiService.getStats(idCalciatore, stagione, normalizza);
    }

    public List<Lista> getListeOsservatore(int idOsservatore){
        return listService.getListeOsservatore(idOsservatore);
    }
}

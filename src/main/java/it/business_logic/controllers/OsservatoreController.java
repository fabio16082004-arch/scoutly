package it.business_logic.controllers;

import it.DTO.SchedaAvanzataDTO;
import it.business_logic.filtro.CalciatoreFiltro;
import it.domain_model.scouting.Lista;
import it.business_logic.services.AnalisiService;
import it.business_logic.services.ListaService;
import it.business_logic.services.ReportService;
import it.domain_model.giocatori.Calciatore;
import it.domain_model.scouting.Report;
import it.domain_model.utenti.Osservatore;

import java.util.List;
import java.util.Map;

public class OsservatoreController {
    private final ReportService reportService;
    private final ListaService listaService;
    private final AnalisiService analisiService;

    public OsservatoreController(ReportService reportService,
                                 ListaService listaService,
                                 AnalisiService analisiService) {
        this.reportService = reportService;
        this.listaService = listaService;
        this.analisiService = analisiService;
    }

    public Report creaNuovoReport(int idOsservatore, int idCalciatore,
                                  List<Integer> idPartite, Map<String, Integer> voti,
                                  String noteFinali, boolean calcolaVoto, int votoComplessivo) {

        return reportService.creaReport(idOsservatore, idCalciatore, idPartite, voti, noteFinali, calcolaVoto, votoComplessivo);
    }

    public void cancellaReport(int idReport) {
        reportService.eliminaReport(idReport);
    }

    public Report ottieniReport(int idReport){
        return reportService.ottieniReport(idReport);
    }

    public void creaNuovaLista(String nome, String descrizione, Osservatore osservatore) {
        listaService.creaLista(nome, descrizione, osservatore);
    }

    public void aggiungiCalciatoreALista(int idLista, int idCalciatore) {
        listaService.aggiungiCalciatore(idLista, idCalciatore);
    }

    public void rimuoviCalciatoreDaLista(int idLista, int idCalciatore) {
        listaService.rimuoviCalciatore(idLista, idCalciatore);
    }

    public void eliminaLista(int idLista) {
        listaService.eliminaLista(idLista);
    }

    public List<Calciatore> cercaGiocatori(CalciatoreFiltro filtro) {
        return analisiService.cercaGiocatori(filtro);
    }

    public SchedaAvanzataDTO getDettagliCalciatore(int idCalciatore, String stagione) {
        return analisiService.getStats(idCalciatore, stagione);
    }

    public Lista getDettagliLista(int idLista) {
        return listaService.getDettagliLista(idLista);
    }

    public List<Lista> getListeOsservatore(int idOsservatore){
        return listaService.getListeOsservatore(idOsservatore);
    }


}

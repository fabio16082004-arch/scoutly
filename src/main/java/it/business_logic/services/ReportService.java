package it.business_logic.services;

import it.ORM.DAO.ReportDAO;
import it.ORM.DAO.CalciatoreDAO;
import it.ORM.DAO.OsservatoreDAO;
import it.ORM.DAO.PartitaDAO;
import it.domain_model.giocatori.Calciatore;
import it.domain_model.giocatori.Partita;
import it.domain_model.scouting.Report;
import it.domain_model.utenti.Osservatore;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class ReportService {
    private final ReportDAO reportDAO;
    private final CalciatoreDAO calciatoreDAO;
    private final OsservatoreDAO osservatoreDAO;
    private final PartitaDAO partitaDAO;

    public ReportService(ReportDAO reportDAO, CalciatoreDAO calciatoreDAO,
                         OsservatoreDAO osservatoreDAO, PartitaDAO partitaDAO) {
        this.reportDAO = reportDAO;
        this.calciatoreDAO = calciatoreDAO;
        this.osservatoreDAO = osservatoreDAO;
        this.partitaDAO = partitaDAO;
    }


    public Report creaReport(int idOsservatore, int idCalciatore, List<Integer> idPartite,
                             Map<String, Integer> voti, String note, boolean calcolaVoto, int votoManuale) {

        Osservatore osservatore = osservatoreDAO.getById(idOsservatore);
        Calciatore calciatore = calciatoreDAO.getById(idCalciatore);

        if (osservatore == null || calciatore == null) {
            throw new IllegalArgumentException("Impossibile creare il report: Osservatore o Calciatore non trovati.");
        }

        Report report = new Report(osservatore, calciatore);

        if (idPartite != null && !idPartite.isEmpty()) {
            for (Integer idPartita : idPartite) {
                Partita partita = partitaDAO.getById(idPartita);
                if (partita != null) {
                    report.aggiungiPartita(partita);
                }
            }
        }

        if (voti != null) {
            voti.forEach(report::aggiungiVoto);
        }
        report.setNoteFinali(note);

        if (calcolaVoto) {
            report.calcolaVotoComplessivo();
        } else {
            report.setVotoComplessivo(votoManuale);
        }

        reportDAO.salva(report);

        return report;
    }

    public void eliminaReport(int idReport) {
        boolean successo = reportDAO.eliminaReport(idReport);

        if (!successo) {
            throw new IllegalArgumentException("Impossibile eliminare: Report non trovato.");
        }
    }

    public Report ottieniReport(int idReport) {
        return reportDAO.getReportById(idReport);
    }
}
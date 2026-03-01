package it.ORM.DAO;

import it.ORM.db.DBConnection;
import it.domain_model.giocatori.Calciatore;
import it.domain_model.giocatori.Partita;

import it.domain_model.scouting.Report;
import it.domain_model.utenti.Osservatore;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ReportDAO {
    private final CalciatoreDAO calciatoreDAO;
    private final PartitaDAO partitaDAO;
    private final OsservatoreDAO osservatoreDAO;

    public ReportDAO(CalciatoreDAO calciatoreDAO, PartitaDAO partitaDAO, OsservatoreDAO osservatoreDAO) {
        this.calciatoreDAO = calciatoreDAO;
        this.partitaDAO = partitaDAO;
        this.osservatoreDAO = osservatoreDAO;
    }

    public Report getReportById(int idReport) {
        String sqlReport = "SELECT * FROM Report WHERE idReport = ?";
        String sqlVoti = "SELECT nota, punteggio FROM Voto WHERE idReport = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlReport)) {

            ps.setInt(1, idReport);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Calciatore calciatore = calciatoreDAO.getById(rs.getInt("calciatore"));

                Osservatore osservatore = osservatoreDAO.getById(rs.getInt("utente"));

                Report report = new Report(osservatore, calciatore);
                report.setVotoComplessivo(rs.getInt("votoComplessivo"));
                report.setNoteFinali(rs.getString("noteFinali"));

                try (PreparedStatement psVoti = conn.prepareStatement(sqlVoti)) {
                    psVoti.setInt(1, idReport);
                    ResultSet rsVoti = psVoti.executeQuery();
                    while (rsVoti.next()) {
                        report.aggiungiVoto(rsVoti.getString("nota"), rsVoti.getInt("punteggio"));
                    }
                }

                List<Partita> partiteViste = partitaDAO.getPartitePerReport(idReport);
                for (Partita p : partiteViste) {
                    report.aggiungiPartita(p);
                }

                return report;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void salva(Report report) {
        String inserisciReport =
                "INSERT INTO Report (votoComplessivo, noteFinali, dataCreazione, utente, calciatore) " +
                        "VALUES (?, ?, ?, ?, ?) RETURNING idReport";
        String inserisciVoto = "INSERT INTO Voto (idReport, nota, punteggio) VALUES (?, ?, ?)";

        int idReportGenerato = -1;
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(inserisciReport)) {
                ps.setInt(1, report.getVotoComplessivo());
                ps.setString(2, report.getNoteFinali());
                ps.setDate(3, Date.valueOf(report.getDataCreazione()));
                ps.setInt(4, report.getUtente().getId());
                ps.setInt(5, report.getCalciatore().getId());

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idReportGenerato = rs.getInt(1);
                        report.setIdReport(idReportGenerato);
                    } else {
                        throw new RuntimeException("Inserimento report fallito: nessun ID generato");
                    }
                }
            }

            if (report.getMappaVoti() != null && !report.getMappaVoti().isEmpty()) {
                try (PreparedStatement ps = conn.prepareStatement(inserisciVoto)) {
                    for (Map.Entry<String, Integer> entry : report.getMappaVoti().entrySet()) {
                        ps.setInt(1, idReportGenerato);
                        ps.setString(2, entry.getKey());
                        ps.setInt(3, entry.getValue());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            throw new RuntimeException("Errore durante il salvataggio del report", e);

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public boolean eliminaReport(int idReport){
        String sql = "DELETE FROM report WHERE idReport = ?";
        try(Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, idReport);
            int righeCancellate = ps.executeUpdate();

            return righeCancellate == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Errore nella cancellazione del report con ID: " + idReport, e);
        }
    }
}

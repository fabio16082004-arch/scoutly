package it.DAO;

import it.db.DBConnection;
import it.domain_model.giocatori.Calciatore;
import it.domain_model.giocatori.Partita;
import it.domain_model.giocatori.Ruolo;
import it.domain_model.giocatori.Squadra;
import it.domain_model.scouting.Report;
import it.domain_model.utenti.Osservatore;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ReportDAO {
    public void salva(Report report) {
        String inserisciReport =
                "INSERT INTO Report (votoComplessivo, noteFinali, dataCreazione, utente, calciatore) " +
                        "VALUES (?, ?, ?, ?, ?) RETURNING idReport";

        String inserisciVoto =
                "INSERT INTO Voto (Report, categoria, punteggio) VALUES (?, ?, ?)";

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
    public Report getReportById(int idReport) {
        Report report = null;

        String sqlReport = "SELECT * FROM Report WHERE idReport = ?";
        String sqlVoti = "SELECT categoria, punteggio FROM Voto WHERE Report = ?";
        String sqlUtente = "SELECT * FROM Osservatore WHERE idOsservatore = ?";
        String sqlCalciatore = "SELECT c.*, GROUP_CONCAT(rc.Sigla SEPARATOR ', ') as sigle_ruoli " +
                "FROM Calciatore c LEFT JOIN RuoloCalciatore rc ON c.idCalciatore = rc.Calciatore " +
                "WHERE c.idCalciatore = ? GROUP BY c.idCalciatore";
        String sqlPartite = "SELECT p.*," +
                "sc.idSquadra AS idCasa, sc.nome AS nomeCasa, sc.campionato AS campCasa, sc.nazione AS nazCasa," +
                "so.idSquadra AS idOspite, so.nome AS nomeOspite, so.campionato AS campOspite, so.nazione AS nazOspite" +
                "FROM Partita p" +
                "JOIN ReportPartite rp ON p.idPartita = rp.idPartita" +
                "JOIN Squadra sc ON p.squadraCasa = sc.idSquadra" +
                "JOIN Squadra so ON p.squadraOspite = so.idSquadra" +
                "WHERE rp.idReport = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement psReport = conn.prepareStatement(sqlReport)) {
                psReport.setInt(1, idReport);
                ResultSet rsReport = psReport.executeQuery();

                if (rsReport.next()) {
                    int idCalciatore = rsReport.getInt("calciatore");
                    int idUtente = rsReport.getInt("utente");

                    Calciatore calciatore = null;
                    try (PreparedStatement psCalc = conn.prepareStatement(sqlCalciatore)) {
                        psCalc.setInt(1, idCalciatore);
                        ResultSet rsCalc = psCalc.executeQuery();
                        if (rsCalc.next()) {
                            Set<Ruolo> ruoli = new HashSet<>();
                            String sigle = rsCalc.getString("sigle_ruoli");
                            if (sigle != null) {
                                for (String s : sigle.split(", ")) {
                                    try {
                                        ruoli.add(Ruolo.valueOf(s));
                                    }
                                    catch (Exception ignored)
                                    {

                                    }
                                }
                            }
                            calciatore = new Calciatore(
                                    rsCalc.getInt("idCalciatore"), rsCalc.getString("nome"),
                                    rsCalc.getString("cognome"), rsCalc.getDate("dataNascita").toLocalDate(),
                                    rsCalc.getString("nazionalità"), rsCalc.getFloat("peso"),
                                    rsCalc.getFloat("altezza"), ruoli
                            );
                        }
                    }

                    Osservatore osservatore = null;
                    try (PreparedStatement psObs = conn.prepareStatement(sqlUtente)) {
                        psObs.setInt(1, idUtente);
                        ResultSet rsObs = psObs.executeQuery();
                        if (rsObs.next()) {
                            osservatore = new Osservatore(
                                    rsObs.getInt("idOsservatore"),
                                    rsObs.getString("username"),
                                    rsObs.getString("email")
                            );
                        }
                    }

                    report = new Report(osservatore, calciatore);
                    report.setVotoComplessivo(rsReport.getInt("votoComplessivo"));
                    report.setNoteFinali(rsReport.getString("noteFinali"));

                    try (PreparedStatement psVoti = conn.prepareStatement(sqlVoti)) {
                        psVoti.setInt(1, idReport);
                        ResultSet rsVoti = psVoti.executeQuery();
                        while (rsVoti.next()) {
                            report.aggiungiVoto(rsVoti.getString("categoria"), rsVoti.getInt("punteggio"));
                        }
                    }

                    try (PreparedStatement psPartite = conn.prepareStatement(sqlPartite)) {
                        psPartite.setInt(1, idReport);
                        ResultSet rs = psPartite.executeQuery();
                        while (rs.next()) {
                            Squadra casa = new Squadra(rs.getInt("idCasa"), rs.getString("nomeCasa"),
                                    rs.getString("campCasa"), rs.getString("nazCasa"));

                            Squadra ospite = new Squadra(rs.getInt("idOspite"), rs.getString("nomeOspite"),
                                    rs.getString("campOspite"), rs.getString("nazOspite"));

                            Partita partita = new Partita(
                                    rs.getInt("idPartita"),
                                    rs.getInt("punteggioCasa"),
                                    rs.getInt("punteggioOspite"),
                                    rs.getDate("data").toLocalDate(),
                                    casa,
                                    ospite,
                                    rs.getString("stagione")
                            );

                            report.aggiungiPartita(partita);
                        }
                    }
                }
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
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
        return report;
    }
    public boolean eliminaReport(int idReport){
        String sql = "DELETE FROM report WHERE id = ?";
        try(Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, idReport);
            int righeCancellate = ps.executeUpdate();

            return righeCancellate == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Errore nella cancellazione del report con ID: " + idReport, e);
        }
    }
    public boolean esisteReport(int idReport) {
        String sql = "SELECT idReport FROM Report WHERE idReport = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReport);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }
}

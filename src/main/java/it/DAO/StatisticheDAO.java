package it.DAO;

import it.db.DBConnection;
import it.domain_model.analisi.RisultatoRanking;
import it.domain_model.analisi.StatisticheCalciatoreStagione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class StatisticheDAO {
    public List<RisultatoRanking> getRankingCalciatori(String campionato, String stagione, String macroRuolo, String statisticaRiferimento, int minMinutiGiocati) {

        List<RisultatoRanking> ranking = new ArrayList<>();

        String funzioneAggregazione = statisticaRiferimento.toLowerCase().contains("precisione") ? "AVG" : "SUM";

        String sql =
                "SELECT c.idCalciatore, c.nome, c.cognome, " +
                        funzioneAggregazione + "(s." + statisticaRiferimento + ") AS valore, " +
                        "SUM(s.minutiGiocati) AS minutiTotali " +
                        "FROM Calciatore c " +
                        "JOIN Statistiche s ON c.idCalciatore = s.Calciatore " +
                        "JOIN Partita p ON s.Partita = p.idPartita " +
                        "JOIN Ruolo r ON c.idRuolo = r.idRuolo " +
                        "WHERE p.campionato = ? AND p.stagione = ? AND r.macroRuolo = ? " +
                        "GROUP BY c.idCalciatore, c.nome, c.cognome " +
                        "HAVING SUM(s.minutiGiocati) >= ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, campionato);
            ps.setString(2, stagione);
            ps.setString(3, macroRuolo);
            ps.setInt(4, minMinutiGiocati);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                RisultatoRanking rr = new RisultatoRanking(
                        rs.getInt("idCalciatore"),
                        rs.getString("nome") + " " + rs.getString("cognome"),
                        rs.getDouble("valore"),
                        rs.getInt("minutiTotali")
                );

                ranking.add(rr);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore nell'estrazione del ranking", e);
        }

        return ranking;
    }


    public StatisticheCalciatoreStagione getStatisticheCalciatorePerStagione(int idCalciatore, String stagione) {
        String sql = "SELECT SUM(s.minutiGiocati), SUM(s.gol), SUM(s.assist), SUM(s.xG), SUM(s.xA), " +
                "SUM(s.tiriTotali), SUM(s.tiriInPorta), SUM(s.dribblingRiusciti), " +
                "SUM(s.tocchiInAreaAvversaria), SUM(s.contrastiVinti), SUM(s.duelliAereiVinti), " +
                "SUM(s.passaggiChiave), SUM(s.crossRiusciti), SUM(s.passaggiRealizzati), " +
                "SUM(s.parate), SUM(s.cleanSheet), SUM(s.cartelliniGialli), SUM(s.cartelliniRossi) " +
                "FROM Statistiche s JOIN Partita p ON s.Partita = p.idPartita " +
                "WHERE s.Calciatore = ? AND p.stagione = ? GROUP BY s.Calciatore";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCalciatore);
            ps.setString(2, stagione);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new StatisticheCalciatoreStagione(
                        rs.getInt(1),    // minutiGiocati (int)
                        rs.getDouble(2), // gol (double)
                        rs.getDouble(3), // assist (double)
                        rs.getDouble(4), // xG
                        rs.getDouble(5), // xA
                        rs.getDouble(6), // tiriTotali
                        rs.getDouble(7), // tiriInPorta
                        rs.getDouble(8), // dribbling
                        rs.getDouble(9), // tocchiArea
                        rs.getDouble(10),// contrasti
                        rs.getDouble(11),// duelli
                        rs.getDouble(12),// passaggiChiave
                        rs.getDouble(13),// cross
                        rs.getDouble(14),// passaggiRealizzati
                        rs.getDouble(15),// parate
                        rs.getDouble(16),// cleanSheet
                        rs.getInt(17),   // gialli (int)
                        rs.getInt(18)    // rossi (int)
                );
            }
        } catch (SQLException e) {
            System.err.println("Errore nell'estrazione delle statistiche: " + e.getMessage());
        }
        return null;
    }
}

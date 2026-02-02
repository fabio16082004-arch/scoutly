package it.DAO;

import it.db.DBConnection;
import it.domain_model.analisi.RisultatoRanking;

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


    public Map<String, Double> getStatisticheCalciatorePerStagione(int idCalciatore, List<String> statistiche, String stagione) {
        String targetList = statistiche.stream()
                .map(s -> {
                    if (s.toLowerCase().contains("precisione")) {
                        return "AVG(s." + s + ") AS " + s;
                    } else {
                        return "SUM(s." + s + ") AS " + s;
                    }
                }).collect(Collectors.joining(", "));

        String sql = "SELECT " + targetList + " " +
                "FROM Statistiche s " +
                "JOIN Partita p ON s.Partita = p.idPartita " +
                "WHERE s.Calciatore = ? AND p.stagione = ? " +
                "GROUP BY s.Calciatore";

        Map<String, Double> map = new HashMap<>();

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCalciatore);
            ps.setString(2, stagione);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                for (String statistica : statistiche) {
                    map.put(statistica, rs.getDouble(statistica));
                }
            }

            return map;

        } catch (SQLException e) {
            System.out.println("Errore nel caricamento delle statistiche: " + e.getMessage());
            return new HashMap<>();
        }
    }
}

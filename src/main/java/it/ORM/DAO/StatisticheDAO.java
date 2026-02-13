package it.ORM.DAO;

import it.ORM.db.DBConnection;
import it.domain_model.statistiche.StatisticheCalciatoreStagione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticheDAO {

    public StatisticheCalciatoreStagione getStatisticheCalciatorePerStagione(int idCalciatore, String stagione) {

            String sql = "SELECT " +
                "SUM(s.minutiGiocati), " +
                "SUM(s.gol), " +
                "SUM(s.assist), " +
                "SUM(s.xG), " +
                "SUM(s.xA), " +
                "SUM(s.tiriTotali), " +
                "SUM(s.tiriInPorta), " +
                "SUM(s.dribblingRiusciti), " +
                "SUM(s.tocchiInAreaAvversaria), " +
                "SUM(s.contrastiVinti), " +
                "SUM(s.duelliAereiVinti), " +
                "SUM(s.passaggiChiave), " +
                "SUM(s.crossRiusciti), " +
                "SUM(s.passaggiRealizzati), " +
                "SUM(s.parate), " +
                "SUM(s.cleanSheet), " +
                "SUM(s.cartelliniGialli), " +
                "SUM(s.cartelliniRossi) " +
                "FROM Statistiche s " +
                "JOIN Partita p ON s.Partita = p.idPartita " +
                "WHERE s.Calciatore = ? AND p.stagione = ? " +
                "GROUP BY s.Calciatore";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCalciatore);
            ps.setString(2, stagione);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new StatisticheCalciatoreStagione(
                            rs.getInt(1),    // minutiGiocati
                            rs.getDouble(2), // gol
                            rs.getDouble(3), // assist
                            rs.getDouble(4), // xG
                            rs.getDouble(5), // xA
                            rs.getDouble(6), // tiriTotali
                            rs.getDouble(7), // tiriInPorta
                            rs.getDouble(8), // dribblingRiusciti
                            rs.getDouble(9), // tocchiInAreaAvversaria
                            rs.getDouble(10),// contrastiVinti
                            rs.getDouble(11),// duelliAereiVinti
                            rs.getDouble(12),// passaggiChiave
                            rs.getDouble(13),// crossRiusciti
                            rs.getDouble(14),// passaggiRealizzati
                            rs.getDouble(15),// parate
                            rs.getDouble(16),// cleanSheet
                            rs.getInt(17),   // cartelliniGialli
                            rs.getInt(18)    // cartelliniRossi
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore nell'estrazione delle statistiche: " + e.getMessage());
        }

        return null;
    }
}
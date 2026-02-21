package it.ORM.DAO;

import it.ORM.db.DBConnection;
import it.domain_model.giocatori.Calciatore;
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
                "SUM(CASE WHEN s.cleanSheet THEN 1 ELSE 0 END), " +
                "SUM(s.cartelliniGialli), " +
                "SUM(s.cartelliniRossi), " +
                 "p.stagione " +
                "FROM Statistiche s " +
                "JOIN Partita p ON s.Partita = p.idPartita " +
                "WHERE s.Calciatore = ? AND p.stagione = ? " +
                "GROUP BY s.Calciatore, p.stagione";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCalciatore);
            ps.setString(2, stagione);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new StatisticheCalciatoreStagione(
                            rs.getInt(1),    // minutiGiocati
                            rs.getInt(2), // gol
                            rs.getInt(3), // assist
                            rs.getInt(4), // xG
                            rs.getInt(5), // xA
                            rs.getInt(6), // tiriTotali
                            rs.getInt(7), // tiriInPorta
                            rs.getInt(8), // dribblingRiusciti
                            rs.getInt(9), // tocchiInAreaAvversaria
                            rs.getInt(10),// contrastiVinti
                            rs.getInt(11),// duelliAereiVinti
                            rs.getInt(12),// passaggiChiave
                            rs.getInt(13),// crossRiusciti
                            rs.getInt(14),// passaggiRealizzati
                            rs.getInt(15),// parate
                            rs.getInt(16),// cleanSheet
                            rs.getInt(17),   // cartelliniGialli
                            rs.getInt(18),   // cartelliniRossi
                            rs.getString(19) //stagione
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore nell'estrazione delle statistiche: " + e.getMessage());
        }

        return null;
    }


}
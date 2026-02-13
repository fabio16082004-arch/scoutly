package it.ORM.DAO;

import it.ORM.db.DBConnection;
import it.domain_model.giocatori.Partita;
import it.domain_model.giocatori.Squadra;
import java.sql.*;
import java.util.*;

public class PartitaDAO {
    private final SquadraDAO squadraDAO;

    public PartitaDAO(SquadraDAO squadraDAO) {
        this.squadraDAO = squadraDAO;
    }

    public Partita getById(int idPartita) {
        String sql = "SELECT * FROM Partita WHERE idPartita = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPartita);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Squadra casa = squadraDAO.getById(rs.getInt("squadraCasa"));
                Squadra ospite = squadraDAO.getById(rs.getInt("squadraOspite"));
                return new Partita(
                        rs.getInt("idPartita"),
                        rs.getInt("punteggioCasa"),
                        rs.getInt("punteggioOspite"),
                        rs.getDate("data").toLocalDate(),
                        casa, ospite,
                        rs.getString("stagione")
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Partita> getPartitePerReport(int idReport) {
        List<Partita> partite = new ArrayList<>();
        String sql = "SELECT p.* FROM Partita p " +
                "JOIN ReportPartite rp ON p.idPartita = rp.idPartita " +
                "WHERE rp.idReport = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReport);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Squadra casa = squadraDAO.getById(rs.getInt("squadraCasa"));
                Squadra ospite = squadraDAO.getById(rs.getInt("squadraOspite"));

                Partita p = new Partita(
                        rs.getInt("idPartita"),
                        rs.getInt("punteggioCasa"),
                        rs.getInt("punteggioOspite"),
                        rs.getDate("data").toLocalDate(),
                        casa,
                        ospite,
                        rs.getString("stagione")
                );
                partite.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return partite;
    }
}
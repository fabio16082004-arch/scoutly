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

        String sql = "SELECT p.*, " +
                "s_casa.nome AS casa_nome, s_casa.campionato AS casa_comp, s_casa.nazione AS casa_naz, " +
                "s_ospite.nome AS ospite_nome, s_ospite.campionato AS ospite_comp, s_ospite.nazione AS ospite_naz " +
                "FROM Partita p " +
                "JOIN ReportPartite rp ON p.idPartita = rp.idPartita " +
                "JOIN Squadra s_casa ON p.squadraCasa = s_casa.idSquadra " +
                "JOIN Squadra s_ospite ON p.squadraOspite = s_ospite.idSquadra " +
                "WHERE rp.idReport = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReport);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Squadra casa = new Squadra(rs.getInt("squadraCasa"),
                        rs.getString("casa_nome"),
                        rs.getString("casa_comp"),
                        rs.getString("casa_naz")
                );

                Squadra ospite = new Squadra(
                        rs.getInt("squadraOspite"),
                        rs.getString("ospite_nome"),
                        rs.getString("ospite_comp"),
                        rs.getString("ospite_naz")
                );

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partite;
    }
}
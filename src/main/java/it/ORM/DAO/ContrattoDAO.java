package it.ORM.DAO;

import it.ORM.db.DBConnection;
import it.domain_model.giocatori.*;
import java.sql.*;

public class ContrattoDAO {

    public Contratto getContrattoAttuale(Calciatore c) {
        String sql = "SELECT co.*, sq.idSquadra, sq.nome as nome_squadra, sq.campionato, sq.nazione " +
                "FROM Contratto co JOIN Squadra sq ON co.squadra = sq.idSquadra " +
                "WHERE co.Calciatore = ? AND co.dataFine >= CURRENT_DATE LIMIT 1";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Squadra s = new Squadra(
                        rs.getInt("idSquadra"),
                        rs.getString("nome_squadra"),
                        rs.getString("campionato"),
                        rs.getString("nazione")
                );

                return new Contratto(c, s, rs.getFloat("stipendio"),
                        rs.getDate("dataInizio").toLocalDate(),
                        rs.getDate("dataFine").toLocalDate());
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

}
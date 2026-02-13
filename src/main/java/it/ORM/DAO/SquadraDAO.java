package it.ORM.DAO;

import it.ORM.db.DBConnection;
import it.domain_model.giocatori.Squadra;
import java.sql.*;

public class SquadraDAO {
    public Squadra getById(int idSquadra) {
        String sql = "SELECT idSquadra, nome as nome_squadra, campionato, nazione FROM Squadra WHERE idSquadra = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSquadra);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new Squadra(
                        rs.getInt("idSquadra"),
                        rs.getString("nome_squadra"),
                        rs.getString("campionato"),
                        rs.getString("nazione"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
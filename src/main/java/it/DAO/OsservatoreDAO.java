package it.DAO;

import it.db.DBConnection;
import it.domain_model.utenti.Osservatore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OsservatoreDAO {
    public void registraOsservatore(String username, String utente, String password) {
        String sql = "INSERT INTO Osservatore (username, email, password) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, utente);
            ps.setString(3, password);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Errore nella creazione dell'account");
        }
    }

    // LOGIN
    public Osservatore login(String username, String passwordInserita) {
        String sql = "SELECT * FROM Osservatore WHERE username = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String passwordDalDB = rs.getString("password");

                if (passwordInserita.equals(passwordDalDB)) {
                    return new Osservatore(
                            rs.getInt("idOsservatore"),
                            rs.getString("username"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Errrore nel login");
        }
        return null;
    }

}

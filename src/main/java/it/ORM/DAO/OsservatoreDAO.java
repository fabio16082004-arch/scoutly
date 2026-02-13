package it.ORM.DAO;

import it.ORM.db.DBConnection;
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

    public Osservatore getById(int idOsservatore) {
        String sql = "SELECT * FROM Osservatore WHERE idOsservatore = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idOsservatore);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Osservatore(
                            rs.getInt("idOsservatore"),
                            rs.getString("username"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean esisteUsername(String username) {
        String sql = "SELECT 1 FROM Osservatore WHERE username = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Ritorna true se lo username esiste già
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean esisteEmail(String email) {
        String sql = "SELECT 1 FROM Osservatore WHERE email = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Ritorna true se l'email esiste già
            }
        } catch (SQLException e) {
            return false;
        }
    }

}

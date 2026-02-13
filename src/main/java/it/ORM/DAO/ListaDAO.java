package it.ORM.DAO;

import it.ORM.db.DBConnection;
import it.domain_model.giocatori.Calciatore;
import it.domain_model.scouting.Lista;
import it.domain_model.utenti.Osservatore;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class ListaDAO {

    private final CalciatoreDAO calciatoreDAO;
    private OsservatoreDAO osservatoreDAO;

    public ListaDAO(CalciatoreDAO calciatoreDAO, OsservatoreDAO osservatoreDAO) {
        this.calciatoreDAO = calciatoreDAO;
        this.osservatoreDAO = osservatoreDAO;
    }

    public Lista getListaById(int idLista) {
        String query = "SELECT * FROM Lista WHERE idLista = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idLista);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nomeLista");
                String descrizione = rs.getString("descrizione");
                LocalDate dataCreazione = rs.getDate("dataCreazione").toLocalDate();
                int idOsservatore = rs.getInt("osservatore");

                Osservatore osservatore = osservatoreDAO.getById(idOsservatore);

                Lista lista = new Lista(idLista, nome, descrizione, osservatore, dataCreazione);

                List<Calciatore> calciatoriAssociati = calciatoreDAO.getCalciatoriByLista(idLista);

                for (Calciatore c : calciatoriAssociati) {
                    lista.aggiungiCalciatore(c);
                }

                return lista;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void creaLista(Lista lista) {
        String sql = "INSERT INTO Lista (nomeLista, descrizione, dataCreazione, osservatore) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, lista.getNomeLista());
            ps.setString(2, lista.getDescrizione());
            ps.setObject(3, lista.getDataCreazione());
            ps.setInt(4, lista.getOsservatore().getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Errore nella creazione della lista", e);
        }
    }

    public boolean eliminaLista(int idLista) {
        String deleteLegami = "DELETE FROM ListaCalciatore WHERE idLista = ?";
        String deleteLista = "DELETE FROM Lista WHERE idLista = ?";

        Connection conn = null;

        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(deleteLegami)) {
                ps1.setInt(1, idLista);
                ps1.executeUpdate();
            }

            int righeCacellate;
            try (PreparedStatement ps2 = conn.prepareStatement(deleteLista)) {
                ps2.setInt(1, idLista);
                righeCacellate = ps2.executeUpdate();
            }

            conn.commit();

            return righeCacellate == 1;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            throw new RuntimeException("Errore durante l'eliminazione della lista", e);

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {}
            }
        }
    }

    public boolean aggiungiCalciatoreAllaLista(int idLista, int idCalciatore) {
        String sql = "INSERT INTO ListaCalciatore (idLista, Calciatore) VALUES (?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLista);
            ps.setInt(2, idCalciatore);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                return false;
            }
            throw new RuntimeException("Errore durante l'aggiunta del calciatore alla lista", e);
        }
    }

    public boolean rimuoviCalciatoreDallaLista(int idLista, int idCalciatore) {
        String sql = "DELETE FROM ListaCalciatore WHERE idLista = ? AND Calciatore = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLista);
            ps.setInt(2, idCalciatore);
            int righeCancellate = ps.executeUpdate();
            return righeCancellate == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Errore nella rimozione del calciatore dalla lista", e);
        }
    }

    public List<Lista> getListePerUtente(int idUtente) {

        String sql = """
        SELECT l.idLista, l.nomeLista, l.descrizione, l.dataCreazione,
               o.idOsservatore, o.username, o.email
        FROM Lista l
        JOIN Osservatore o ON l.osservatore = o.idOsservatore
        WHERE l.osservatore = ?
        """;

        List<Lista> liste = new ArrayList<>();

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Osservatore osservatore = new Osservatore(
                        rs.getInt("idOsservatore"),
                        rs.getString("username"),
                        rs.getString("email")
                );

                Lista lista = new Lista(
                        rs.getInt("idLista"),
                        rs.getString("nomeLista"),
                        rs.getString("descrizione"),
                        osservatore,
                        rs.getDate("dataCreazione").toLocalDate()
                );

                liste.add(lista);
            }

            return liste;

        } catch (SQLException e) {
            System.out.println("Errore nella ricerca delle liste create dall'utente con ID: " + idUtente);
            return null;
        }
    }
}

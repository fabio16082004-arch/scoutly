package it.DAO;

import it.db.DBConnection;
import it.domain_model.giocatori.Calciatore;
import it.domain_model.giocatori.Ruolo;
import it.domain_model.scouting.Lista;
import it.domain_model.utenti.Osservatore;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class ListaDAO {

    public int creaLista(Lista lista) {
        String sql = "INSERT INTO Lista (nomeLista, descrizione, dataCreazione, osservatore) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING idLista";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, lista.getNomeLista());
            ps.setString(2, lista.getDescrizione());
            ps.setObject(3, lista.getDataCreazione());
            ps.setInt(4, lista.getOsservatore().getId());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new RuntimeException("Creazione lista fallita: nessun ID restituito");
            }

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

    public List<Calciatore> getTuttiCalciatoriInLista(int idLista) {
        List<Calciatore> filtrati = new ArrayList<>();

        String sql = "SELECT c.*, STRING_AGG(rc.Sigla, ', ') AS sigle_ruoli" +
            "FROM Calciatore c" +
            "JOIN ListaCalciatore lc ON c.idCalciatore = lc.Calciatore" +
            "LEFT JOIN RuoloCalciatore rc ON rc.Calciatore = c.idCalciatore" +
            "WHERE lc.idLista = ?" +
            "GROUP BY c.idCalciatore";


        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLista);

            getCalciatori(filtrati, ps);

        } catch (SQLException e) {
            throw new RuntimeException("Errore SQL durante il recupero di tutti i calciatori in lista", e);
        }

        return filtrati;
    }

    private void getCalciatori(List<Calciatore> filtrati, PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("idcalciatore");
                String nome = rs.getString("nome");
                String cognome = rs.getString("cognome");
                LocalDate dataNascita = rs.getObject("dataNascita", LocalDate.class);
                String nazionalita = rs.getString("nazionalità");
                float peso = rs.getFloat("peso");
                float altezza = rs.getFloat("altezza");

                Set<Ruolo> ruoliSet = new HashSet<>();
                String ruoliAggregati = rs.getString("sigle_ruoli");

                if (ruoliAggregati != null) {
                    String[] sigle = ruoliAggregati.split(", ");
                    for (String s : sigle) {
                        try {
                            ruoliSet.add(Ruolo.valueOf(s));
                        } catch (IllegalArgumentException ignored) {}
                    }
                }

                Calciatore c = new Calciatore(id, nome, cognome, dataNascita, nazionalita, peso, altezza, ruoliSet);
                filtrati.add(c);
            }
        }
    }

    public Lista getListaById(int idLista) {
        String sql = """
        SELECT l.idLista, l.nomeLista, l.descrizione, l.dataCreazione,
               o.idOsservatore, o.username, o.email
        FROM Lista l
        JOIN Osservatore o ON l.osservatore = o.idOsservatore
        WHERE l.idLista = ?
    """;

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLista);

            try (ResultSet rs = ps.executeQuery()) {

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

                List<Calciatore> calciatori = getTuttiCalciatoriInLista(idLista);
                calciatori.forEach(lista::aggiungiCalciatore);

                return lista;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore nel recupero della lista con ID: " + idLista, e);
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

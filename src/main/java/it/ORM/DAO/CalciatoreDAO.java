package it.ORM.DAO;

import it.DTO.SchedaAvanzataDTO;
import it.ORM.db.DBConnection;
import it.business_logic.filtro.CalciatoreFiltro;
import it.domain_model.giocatori.*;
import java.sql.*;
import java.util.*;

public class CalciatoreDAO {

    public List<Calciatore> cerca(CalciatoreFiltro filtro) {

        StringBuilder sql = new StringBuilder(
                "SELECT c.*, STRING_AGG(DISTINCT rc.Sigla, ',') AS sigle_ruoli " +
                        "FROM Calciatore c " +
                        "LEFT JOIN RuoloCalciatore rc ON rc.Calciatore = c.idCalciatore " +
                        "LEFT JOIN Contratto co ON co.Calciatore = c.idCalciatore " +
                        "LEFT JOIN Squadra sq ON sq.idSquadra = co.squadra " +
                        "LEFT JOIN Statistiche st ON st.Calciatore = c.idCalciatore " +
                        "LEFT JOIN Partita p ON p.idPartita = st.Partita " +
                        "WHERE 1=1 "
        );


        List<Object> params = new ArrayList<>();
        filtro.applicaFiltri(sql, params);

        sql.append(" GROUP BY c.idCalciatore");

        List<Calciatore> risultati = new ArrayList<>();

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                risultati.add(new Calciatore(
                        rs.getInt("idCalciatore"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getDate("dataNascita").toLocalDate(),
                        rs.getString("nazionalità"),
                        rs.getFloat("peso"),
                        rs.getFloat("altezza"),
                        parseRuoli(rs.getString("sigle_ruoli"))
                ));
            }

        } catch (SQLException e) {
            System.err.println("Errore nella ricerca calciatori: " + e.getMessage());
        }

        return risultati;
    }

    public Calciatore getById(int idCalciatore) {
        String sql = "SELECT c.*, STRING_AGG(DISTINCT rc.Sigla, ',') as sigle_ruoli " +
                "FROM Calciatore c " +
                "LEFT JOIN RuoloCalciatore rc ON c.idCalciatore = rc.Calciatore " +
                "WHERE c.idCalciatore = ? " +
                "GROUP BY c.idCalciatore";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCalciatore);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Calciatore(
                        rs.getInt("idCalciatore"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getDate("dataNascita").toLocalDate(),
                        rs.getString("nazionalità"),
                        rs.getFloat("peso"),
                        rs.getFloat("altezza"),
                        parseRuoli(rs.getString("sigle_ruoli"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Calciatore> getCalciatoriByLista(int idLista) {
        List<Calciatore> calciatori = new ArrayList<>();
        String query = "SELECT c.* FROM Calciatore c " +
                "JOIN ListaCalciatore lc ON c.idCalciatore = lc.Calciatore " +
                "WHERE lc.idLista = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idLista);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                calciatori.add(new Calciatore(
                        rs.getInt("idCalciatore"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getDate("dataNascita").toLocalDate(),
                        rs.getString("nazionalità"),
                        rs.getFloat("peso"),
                        rs.getFloat("altezza"),
                        parseRuoli(rs.getString("sigle_ruoli"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return calciatori;
    }

    private Set<Ruolo> parseRuoli(String sigle) {
        Set<Ruolo> set = new HashSet<>();
        if (sigle == null) return set;
        for (String s : sigle.split(",")) {
            for (Ruolo r : Ruolo.values()) {
                if (r.getSigla().equals(s)) { set.add(r); break; }
            }
        }
        return set;
    }

}
package it.ORM.DAO;

import it.ORM.db.DBConnection;
import it.business_logic.analisi.CalciatoreFiltro;
import it.domain_model.giocatori.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class CalciatoreDAO {

    public List<Calciatore> cerca(CalciatoreFiltro filtro) {

        StringBuilder sql = new StringBuilder(
                "SELECT c.*, STRING_AGG(DISTINCT rc.Sigla, ',') AS sigle_ruoli " +
                        "FROM Calciatore c " +
                        "LEFT JOIN RuoloCalciatore rc ON rc.Calciatore = c.idCalciatore " +
                        "LEFT JOIN Squadra sq ON sq.idSquadra = c.Squadra " +
                        "LEFT JOIN Contratto co ON co.Calciatore = c.idCalciatore " +
                        "LEFT JOIN Statistiche st ON st.Calciatore = c.idCalciatore " +
                        "LEFT JOIN Partita p ON p.idPartita = st.Partita " +
                        "WHERE 1=1 "
        );


        List<Object> params = new ArrayList<>();

        applicaFiltriAnagrafici(filtro, sql, params);
        applicaFiltriRuoli(filtro, sql, params);
        applicaFiltriSquadra(filtro, sql, params);
        applicaFiltriContratto(filtro, sql, params);
        applicaFiltriStatistiche(filtro, sql, params);

        sql.append(" GROUP BY c.idCalciatore ");

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
                        rs.getString("nazionalita"),
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

    private void applicaFiltriAnagrafici(CalciatoreFiltro f, StringBuilder sql, List<Object> params) {

        if (f.getMinEta() != null) {
            sql.append(" AND EXTRACT(YEAR FROM AGE(NOW(), c.dataNascita)) >= ? ");
            params.add(f.getMinEta());
        }

        if (f.getMaxEta() != null) {
            sql.append(" AND EXTRACT(YEAR FROM AGE(NOW(), c.dataNascita)) <= ? ");
            params.add(f.getMaxEta());
        }
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

    private void applicaFiltriRuoli(CalciatoreFiltro f, StringBuilder sql, List<Object> params) {

        if (f.getSigleRuoli() != null && !f.getSigleRuoli().isEmpty()) {
            sql.append(" AND rc.Sigla IN (");
            sql.append(String.join(",", Collections.nCopies(f.getSigleRuoli().size(), "?")));
            sql.append(") ");
            params.addAll(f.getSigleRuoli());
        }
    }

    private void applicaFiltriSquadra(CalciatoreFiltro f, StringBuilder sql, List<Object> params) {

        if (f.getIdSquadra() != null) {
            sql.append(" AND sq.idSquadra = ? ");
            params.add(f.getIdSquadra());
        }

        if (f.getCampionato() != null) {
            sql.append(" AND sq.campionato = ? ");
            params.add(f.getCampionato());
        }

        if (f.getIdLista() != null) {
            sql.append(" AND c.idCalciatore IN (SELECT Calciatore FROM ListaCalciatore WHERE idLista = ?) ");
            params.add(f.getIdLista());
        }
    }

    private void applicaFiltriContratto(CalciatoreFiltro f, StringBuilder sql, List<Object> params) {

        if (f.getMinAnniContratto() != null) {
            sql.append(" AND co.dataFine >= CURRENT_DATE + (? * INTERVAL '1 year') ");
            params.add(f.getMinAnniContratto());
        }

        if (f.getMinStipendio() != null) {
            sql.append(" AND co.stipendio >= ? ");
            params.add(f.getMinStipendio());
        }

        if (f.getMaxStipendio() != null) {
            sql.append(" AND co.stipendio <= ? ");
            params.add(f.getMaxStipendio());
        }
    }

    private void applicaFiltriStatistiche(CalciatoreFiltro f, StringBuilder sql, List<Object> params) {

        if (f.getMinMinutiGiocati() != null) {
            sql.append(" AND st.minutiGiocati >= ? ");
            params.add(f.getMinMinutiGiocati());
        }

        if (f.getMinGol() != null) {
            sql.append(" AND st.gol >= ? ");
            params.add(f.getMinGol());
        }

        if (f.getMinAssist() != null) {
            sql.append(" AND st.assist >= ? ");
            params.add(f.getMinAssist());
        }

        if (f.getMinXG() != null) {
            sql.append(" AND st.xG >= ? ");
            params.add(f.getMinXG());
        }

        if (f.getMinXA() != null) {
            sql.append(" AND st.xA >= ? ");
            params.add(f.getMinXA());
        }

        // --- NUOVE STATISTICHE AGGIUNTE ---
        if (f.getMinTiriTotali() != null) {
            sql.append(" AND st.tiriTotali >= ? ");
            params.add(f.getMinTiriTotali());
        }

        if (f.getMinTiriInPorta() != null) {
            sql.append(" AND st.tiriInPorta >= ? ");
            params.add(f.getMinTiriInPorta());
        }

        if (f.getMinDribblingRiusciti() != null) {
            sql.append(" AND st.dribblingRiusciti >= ? ");
            params.add(f.getMinDribblingRiusciti());
        }

        if (f.getMinTocchiInAreaAvversaria() != null) {
            sql.append(" AND st.tocchiInAreaAvversaria >= ? ");
            params.add(f.getMinTocchiInAreaAvversaria());
        }

        if (f.getMinContrastiVinti() != null) {
            sql.append(" AND st.contrastiVinti >= ? ");
            params.add(f.getMinContrastiVinti());
        }

        if (f.getMinDuelliAereiVinti() != null) {
            sql.append(" AND st.duelliAereiVinti >= ? ");
            params.add(f.getMinDuelliAereiVinti());
        }

        if (f.getMinPassaggiChiave() != null) {
            sql.append(" AND st.passaggiChiave >= ? ");
            params.add(f.getMinPassaggiChiave());
        }

        if (f.getMinCrossRiusciti() != null) {
            sql.append(" AND st.crossRiusciti >= ? ");
            params.add(f.getMinCrossRiusciti());
        }

        if (f.getMinPassaggiRealizzati() != null) {
            sql.append(" AND st.passaggiRealizzati >= ? ");
            params.add(f.getMinPassaggiRealizzati());
        }

        if (f.getMinParate() != null) {
            sql.append(" AND st.parate >= ? ");
            params.add(f.getMinParate());
        }

        if (f.getMinCleanSheet() != null) {
            sql.append(" AND st.cleanSheet >= ? ");
            params.add(f.getMinCleanSheet());
        }

        // --- CARTELLINI (Logica del Massimo) ---
        if (f.getMaxCartelliniGialli() != null) {
            sql.append(" AND st.cartelliniGialli <= ? ");
            params.add(f.getMaxCartelliniGialli());
        }

        if (f.getMaxCartelliniRossi() != null) {
            sql.append(" AND st.cartelliniRossi <= ? ");
            params.add(f.getMaxCartelliniRossi());
        }

        if (f.getStagione() != null) {
            sql.append(" AND p.stagione = ? ");
            params.add(f.getStagione());
        }
    }
}
package it.DAO;

import it.db.DBConnection;
import it.domain_model.giocatori.Calciatore;
import it.domain_model.giocatori.Contratto;
import it.domain_model.giocatori.Ruolo;
import it.domain_model.giocatori.Squadra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CalciatoreDAO {
    public List<Calciatore> cerca(List<String> sigleRuoli, Integer idSquadra, String stagione,
                                  String campionato, Integer minEta, Integer maxEta,
                                  Integer minAnniContratto) {
        List<Calciatore> risultati = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT c.idCalciatore, c.nome, c.cognome, c.dataNascita, c.nazionalità, c.peso, c.altezza, " +
                        "co.stipendio, co.dataInizio, co.dataFine, " +
                        "sq.idSquadra, sq.nome as nome_squadra, sq.campionato, sq.nazione, " +
                        "STRING_AGG(DISTINCT rc.Sigla, ',') as sigle_ruoli " +
                        "FROM Calciatore c " +
                        "LEFT JOIN RuoloCalciatore rc ON c.idCalciatore = rc.Calciatore " +
                        "LEFT JOIN Statistiche s ON c.idCalciatore = s.Calciatore " +
                        "LEFT JOIN Partita p ON s.Partita = p.idPartita " +
                        "LEFT JOIN Contratto co ON c.idCalciatore = co.Calciatore " +
                        "LEFT JOIN Squadra sq ON co.squadra = sq.idSquadra " +
                        "WHERE 1=1 "
        );

        if (campionato != null) sql.append(" AND sq.campionato = ? ");

        if (sigleRuoli != null && !sigleRuoli.isEmpty()) {
            String placeholders = String.join(",", Collections.nCopies(sigleRuoli.size(), "?"));
            sql.append(" AND rc.Sigla IN (").append(placeholders).append(") ");
        }

        if (idSquadra != null) {
            sql.append(" AND co.squadra = ? AND co.dataFine > CURRENT_DATE ");
        }

        if (stagione != null && !stagione.isEmpty()) {
            sql.append(" AND p.stagione = ? ");
        }

        if (minAnniContratto != null && minAnniContratto > 0) {
            sql.append(" AND co.dataFine >= CURRENT_DATE + CAST(? || ' years' AS INTERVAL) ");
        } else if (minAnniContratto != null && minAnniContratto == 0) {
            sql.append(" AND (co.idSquadra IS NULL OR co.dataFine <= CURRENT_DATE) ");
        }

        sql.append(" AND EXTRACT(YEAR FROM AGE(NOW(), c.dataNascita)) >= ? ")
                .append(" AND EXTRACT(YEAR FROM AGE(NOW(), c.dataNascita)) <= ? ");

        sql.append(" GROUP BY c.idCalciatore, c.nome, c.cognome, c.dataNascita, c.nazionalità, c.peso, c.altezza");

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if (campionato != null) ps.setString(paramIndex++, campionato);

            if (sigleRuoli != null && !sigleRuoli.isEmpty()) {
                for (String sigla : sigleRuoli) ps.setString(paramIndex++, sigla);
            }

            if (idSquadra != null) ps.setInt(paramIndex++, idSquadra);

            if (stagione != null && !stagione.isEmpty()) ps.setString(paramIndex++, stagione);

            if (minAnniContratto != null && minAnniContratto > 0) {
                ps.setInt(paramIndex++, minAnniContratto);
            }

            ps.setInt(paramIndex++, minEta);
            ps.setInt(paramIndex, maxEta);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               Calciatore calciatore = new Calciatore(
                        rs.getInt("idCalciatore"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getDate("dataNascita").toLocalDate(),
                        rs.getString("nazionalità"),
                        rs.getFloat("peso"),
                        rs.getFloat("altezza"),
                        parseRuoli(rs.getString("sigle_ruoli"))
               );
               int idSq = rs.getInt("idSquadra");
               if (!rs.wasNull()) {
                    Squadra squadra = new Squadra(
                            idSq,
                            rs.getString("nome_squadra"),
                            rs.getString("campionato"),
                            rs.getString("nazione")
                    );

                    Contratto contratto = new Contratto(
                            calciatore,
                            squadra,
                            rs.getFloat("stipendio"),
                            rs.getDate("dataInizio").toLocalDate(),
                            rs.getDate("dataFine").toLocalDate()
                    );

                    calciatore.setContrattoAttuale(contratto);
               }

               risultati.add(calciatore);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return risultati;
    }

    private Set<Ruolo> parseRuoli(String sigle) {
        Set<Ruolo> set = new HashSet<>();
        if (sigle != null) {
            for (String s : sigle.split(",")) {
                for (Ruolo r : Ruolo.values()) {
                    if (r.getSigla().equals(s)) {
                        set.add(r);
                        break;
                    }
                }
            }
        }
        return set;
    }
}
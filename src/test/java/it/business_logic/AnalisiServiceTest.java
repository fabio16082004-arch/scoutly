package it.business_logic;

import it.DTO.SchedaAvanzataDTO;
import it.ORM.DAO.CalciatoreDAO;
import it.ORM.DAO.ContrattoDAO;
import it.ORM.DAO.StatisticheDAO;
import it.ORM.db.DBConnection;
import it.business_logic.filtro.CalciatoreFiltro;
import it.business_logic.services.AnalisiService;
import it.domain_model.giocatori.Calciatore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnalisiServiceTest {

    private CalciatoreDAO calciatoreDAO;
    private StatisticheDAO statisticheDAO;
    private ContrattoDAO contrattoDAO;
    private AnalisiService analisiService;

    @BeforeEach
    void setUp() throws Exception {
        try (Connection conn = DBConnection.getInstance().getConnection()) {

            eseguiScript(conn, "schema.sql");

            try (Statement stmt = conn.createStatement()) {
                stmt.execute("TRUNCATE TABLE Ruolo, Squadra, Osservatore, Calciatore, " +
                        "RuoloCalciatore, Contratto, Statistiche RESTART IDENTITY CASCADE");
            }

            eseguiScript(conn, "data.sql");
        }

        calciatoreDAO = new CalciatoreDAO();
        statisticheDAO = new StatisticheDAO();
        contrattoDAO = new ContrattoDAO();

        analisiService = new AnalisiService(statisticheDAO, calciatoreDAO, contrattoDAO);
    }

    @Test
    void testGetStatsPortiere() {
        int idPortiere = 6;
        String stagione = "2024/2025";

        SchedaAvanzataDTO scheda = analisiService.getStats(idPortiere, stagione);

        assertNotNull(scheda, "La scheda non dovrebbe essere null");

        assertTrue(scheda.getStatistiche().containsKey("parate"));
        assertTrue(scheda.getStatistiche().containsKey("cleanSheet"));

        assertFalse(scheda.getStatistiche().containsKey("dribblingRiusciti"));
    }

    @Test
    void testGetStatsNonPortiere() {
        int idAttaccante = 1;
        String stagione = "2024/2025";

        SchedaAvanzataDTO scheda = analisiService.getStats(idAttaccante, stagione);

        assertNotNull(scheda);

        assertFalse(scheda.getStatistiche().containsKey("parate"));
        assertFalse(scheda.getStatistiche().containsKey("cleanSheet"));

        assertTrue(scheda.getStatistiche().containsKey("gol"));
        assertTrue(scheda.getStatistiche().containsKey("assist"));
    }



    @Test
    void testCercaGiocatoriFiltroCombinato() {


        CalciatoreFiltro filtro = new CalciatoreFiltro.Builder("2024/2025")
                .withSquadra(2)                         // Solo Inter
                .withStipendio(7500000.0f, 8500000.0f)
                .withEta(27, 29)
                .withAnniContratto(0, 1)
                .withGol(1)
                .withXG(1.5)
                .withMinutiGiocati(80)
                .build();

        List<Calciatore> risultati = analisiService.cercaGiocatori(filtro);

        assertEquals("Martinez", risultati.get(0).getCognome());
        assertEquals("Lautaro", risultati.get(0).getNome());
    }



    private void eseguiScript(Connection conn, String fileName) throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        if (is == null) throw new RuntimeException("File non trovato: " + fileName);

        String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        String[] comandi = sql.split(";");

        try (Statement stmt = conn.createStatement()) {
            for (String comando : comandi) {
                String c = comando.trim();
                if (!c.isEmpty()) {
                    stmt.execute(c);
                }
            }
        }
    }
}
package it.business_logic;

import it.ORM.DAO.*;
import it.ORM.db.DBConnection;
import it.business_logic.services.ReportService;
import it.domain_model.scouting.Report;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ReportServiceTest {

    private ReportDAO reportDAO;
    private CalciatoreDAO calciatoreDAO;
    private OsservatoreDAO osservatoreDAO;
    private PartitaDAO partitaDAO;
    private ReportService reportService;
    private SquadraDAO squadraDAO;

    @BeforeEach
    void setUp() throws Exception {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            eseguiScript(conn, "schema.sql");

            try (Statement stmt = conn.createStatement()) {
                stmt.execute("TRUNCATE TABLE Ruolo, Squadra, Osservatore, Calciatore, " +
                        "RuoloCalciatore, Contratto, Partita, Report, Voto, ReportPartite RESTART IDENTITY CASCADE");
            }

            eseguiScript(conn, "data.sql");
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("SELECT setval(pg_get_serial_sequence('report', 'idreport'), (SELECT MAX(idreport) FROM report))");
            }
        }

        squadraDAO = new SquadraDAO();
        calciatoreDAO = new CalciatoreDAO();
        osservatoreDAO = new OsservatoreDAO();
        partitaDAO = new PartitaDAO(squadraDAO);
        reportDAO = new ReportDAO(calciatoreDAO, partitaDAO, osservatoreDAO);

        reportService = new ReportService(reportDAO, calciatoreDAO, osservatoreDAO, partitaDAO);
    }

    @Test
    void testCreaReportSuccessoConVotoManuale() {
        int idOsservatore = 1;
        int idCalciatore = 4;
        Report report = reportService.creaReport(idOsservatore, idCalciatore, null, null, "Ottimo giocatore", false, 8);

        assertNotNull(report);
        assertNotNull(reportService.ottieniReport(report.getIdReport()));
    }

    @Test
    void CreaReportSuccessoConVotoAutomatico() {
        int idOsservatore = 1;
        int idCalciatore = 2;
        Map<String, Integer> votiDettaglio = new HashMap<>();
        votiDettaglio.put("Tecnica", 8);
        votiDettaglio.put("Fisico", 7);
        votiDettaglio.put("Tattica", 9);

        Report report = reportService.creaReport(idOsservatore, idCalciatore, null, votiDettaglio, "Ottimo prospetto", true, 0);
        assertNotNull(report);
        Report reportRecuperato = reportService.ottieniReport(report.getIdReport());
        assertNotNull(reportRecuperato);
        assertEquals(8, reportRecuperato.getVotoComplessivo());
    }

    @Test
    void testCreaReportConPartiteReali() {
        int idOsservatore = 1;
        int idCalciatore = 3;
        List<Integer> idPartite = List.of(1, 4);

        Report report = reportService.creaReport(idOsservatore, idCalciatore, idPartite, null, "Visionato in campionato", false, 7);

        assertNotNull(report);
        assertNotNull(reportService.ottieniReport(report.getIdReport()));
    }

    @Test
    void testEliminaReport() {
        Report report = reportService.creaReport(1, 2, null, null, "Temporaneo", false, 6);
        int id = report.getIdReport();

        assertDoesNotThrow(() -> reportService.eliminaReport(id));
        assertNull(reportService.ottieniReport(id));
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
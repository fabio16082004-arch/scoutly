package it.business_logic;

import it.ORM.DAO.CalciatoreDAO;
import it.ORM.DAO.ListaDAO;
import it.ORM.DAO.OsservatoreDAO;
import it.ORM.db.DBConnection;
import it.business_logic.services.ListaService;
import it.domain_model.giocatori.Calciatore;
import it.domain_model.scouting.Lista;
import it.domain_model.utenti.Osservatore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListaServiceTest {

    private ListaDAO listaDAO;
    private CalciatoreDAO calciatoreDAO;
    private OsservatoreDAO osservatoreDAO;
    private ListaService listaService;

    @BeforeEach
    void setUp() throws Exception {
        try (Connection conn = DBConnection.getInstance().getConnection()) {
            eseguiScript(conn, "schema.sql");

            try (Statement stmt = conn.createStatement()) {
                stmt.execute("TRUNCATE TABLE Ruolo, Squadra, Osservatore, Calciatore, " +
                        "RuoloCalciatore, Contratto, Lista, ListaCalciatore RESTART IDENTITY CASCADE");
            }

            eseguiScript(conn, "data.sql");

            try (Statement stmt = conn.createStatement()) {

                stmt.execute("SELECT setval(pg_get_serial_sequence('lista', 'idlista'), (SELECT MAX(idlista) FROM lista))");
            } catch (Exception e) {
                System.out.println("Sequenza non aggiornata (possibile DB non Postgres o tabella vuota): " + e.getMessage());
            }
        }
        osservatoreDAO = new OsservatoreDAO();
        calciatoreDAO = new CalciatoreDAO();
        listaDAO = new ListaDAO(calciatoreDAO, osservatoreDAO);
        listaService = new ListaService(listaDAO);
    }

    @Test
    void testCreaListaSuccesso() {
        Osservatore o = osservatoreDAO.getById(1);
        assertNotNull(o);

        assertDoesNotThrow(() -> {
            listaService.creaLista("Nuova Lista", "Descrizione Test", o);
        });

        List<Lista> liste = listaService.getListeOsservatore(1);
        assertFalse(liste.isEmpty());
    }

    @Test
    void testAggiungiCalciatoreSuccesso() {
        int idLista = 1;

        int idCalciatore = 3;

        assertDoesNotThrow(() -> listaService.aggiungiCalciatore(idLista, idCalciatore));
    }

    @Test
    void testAggiungiCalciatoreGiaPresente() {
        int idLista = 1;
        int idCalciatore = 1;

        assertThrows(IllegalStateException.class, () -> listaService.aggiungiCalciatore(idLista, idCalciatore));
    }

    @Test
    void testRimuoviCalciatoreSuccesso() {
        int idLista = 1;
        int idCalciatore = 1;

        assertDoesNotThrow(() -> listaService.rimuoviCalciatore(idLista, idCalciatore));

        Lista listaAggiornata = listaService.getDettagliLista(idLista);
        assertNotNull(listaAggiornata);

        boolean ancoraPresente = false;
        for (Calciatore c : listaAggiornata.getCalciatori()) {
            if (c.getId() == idCalciatore) {
                ancoraPresente = true;
                break;
            }
        }
        assertFalse(ancoraPresente);
    }

    @Test
    void testEliminaListaSuccesso() {
        Osservatore o = osservatoreDAO.getById(1);
        listaService.creaLista("Da Eliminare", "Desc", o);

        List<Lista> listePrima = listaService.getListeOsservatore(1);
        Lista listaDaEliminare = listePrima.get(listePrima.size() - 1);
        int idLista = listaDaEliminare.getIdLista();

        assertDoesNotThrow(() -> listaService.eliminaLista(idLista));
        assertNull(listaService.getDettagliLista(idLista));
    }

    @Test
    void testGetListeOsservatoreSuccesso() {
        List<Lista> liste = listaService.getListeOsservatore(1);
        assertEquals(2, liste.size());
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
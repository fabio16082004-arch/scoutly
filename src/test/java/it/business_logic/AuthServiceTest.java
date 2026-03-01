package it.business_logic;

import it.ORM.DAO.OsservatoreDAO;
import it.ORM.db.DBConnection;
import it.business_logic.services.AuthService;
import it.domain_model.utenti.Osservatore;
import org.junit.jupiter.api.*;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private OsservatoreDAO osservatoreDAO;
    private AuthService authService;

    @BeforeEach
    void setUp() throws Exception {
        try (Connection conn = DBConnection.getInstance().getConnection()) {

            eseguiScript(conn, "schema.sql");

            try (Statement stmt = conn.createStatement()) {
                stmt.execute("TRUNCATE TABLE Osservatore RESTART IDENTITY CASCADE");
            }

            eseguiScript(conn, "data.sql");
        }
        osservatoreDAO = new OsservatoreDAO();
        authService = new AuthService(osservatoreDAO);
    }

    @Test
    void testLoginCredenzialiNonValide() {
        assertThrows(RuntimeException.class, () -> authService.login("mario.rossi", "passwordSbagliata"));
    }

    @Test
    void testLoginUtenteNonEsistente() {
        assertThrows(RuntimeException.class,
                () -> authService.login("utente.inesistente", "qualsiasi"));
    }

    @Test
    void testLoginSuccesso() {
        Osservatore result = authService.login("mario.rossi", "$2a$10$hashedpassword1");
        assertNotNull(result);
        assertEquals("mario.rossi", result.getUsername());
        assertEquals("mario.rossi@scouting.it", result.getEmail());
    }

    @Test
    void testRegistrazioneUsernameInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> authService.registraNuovoOsservatore(null, "email@test.com", "password123"));
        assertThrows(IllegalArgumentException.class,
                () -> authService.registraNuovoOsservatore("  ", "email@test.com", "password123"));
    }

    @Test
    void testRegistrazioneEmailInvalida() {
        assertThrows(IllegalArgumentException.class,
                () -> authService.registraNuovoOsservatore("nuovoutente", "emailnonvalida", "password123"));
    }

    @Test
    void testRegistrazionePasswordTroppoCorta() {
        assertThrows(IllegalArgumentException.class,
                () -> authService.registraNuovoOsservatore("nuovoutente", "email@test.com", "short"));
    }

    @Test
    void testRegistrazioneUsernameDuplicato() {
        assertThrows(IllegalStateException.class,
                () -> authService.registraNuovoOsservatore(
                        "mario.rossi", "nuovo@test.com", "password123"));
    }

    @Test
    void testRegistrazioneEmailDuplicata() {
        assertThrows(IllegalStateException.class,
                () -> authService.registraNuovoOsservatore(
                        "nuovoutente", "mario.rossi@scouting.it", "password123"));
    }

    @Test
    void testRegistrazioneSuccesso() {
        assertDoesNotThrow(() ->
                authService.registraNuovoOsservatore(
                        "nuovo.utente", "nuovo@test.com", "password123"));

        Osservatore result = authService.login("nuovo.utente", "password123");
        assertNotNull(result);
        assertEquals("nuovo.utente", result.getUsername());
    }

    private void eseguiScript(Connection conn, String fileName) throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        if (is == null) throw new RuntimeException("File non trovato: " + fileName);

        String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
}
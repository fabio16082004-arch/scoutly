package it.domain_model.utenti;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OsservatoreTest {

    @Test
    void costruttoreEGetterValidi() {
        Osservatore o = new Osservatore(1, "scout_master", "scout@scoutly.it");

        assertAll("Verifica stato iniziale osservatore",
                () -> assertEquals(1, o.getId()),
                () -> assertEquals("scout_master", o.getUsername()),
                () -> assertEquals("scout@scoutly.it", o.getEmail())
        );
    }

    @Test
    void usernameInvalido() {
        assertThrows(IllegalArgumentException.class, () ->
                new Osservatore(1, null, "test@test.it"));

        assertThrows(IllegalArgumentException.class, () ->
                new Osservatore(1, "", "test@test.it"));

        assertThrows(IllegalArgumentException.class, () -> {
            Osservatore o = new Osservatore(1, "Valido", "test@test.it");
            o.setUsername("   ");
        });
    }

    @Test
    void emailInvalida() {
        assertThrows(IllegalArgumentException.class, () ->
                new Osservatore(1, "user", "email_senza_chicciola.it"));

        assertThrows(IllegalArgumentException.class, () ->
                new Osservatore(1, "user", null));

        Osservatore o = new Osservatore(1, "user", "ok@test.it");
        assertThrows(IllegalArgumentException.class, () -> o.setEmail("email-non_valida"));
    }

    @Test
    void setEmailValida() {
        Osservatore o = new Osservatore(1, "user", "vecchia@test.it");
        o.setEmail("nuova@test.it");
        assertEquals("nuova@test.it", o.getEmail());
    }
}
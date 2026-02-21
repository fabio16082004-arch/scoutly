package it.domain_model.utenti;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
            Osservatore o = new Osservatore(1, "   ", "test@test.it");
        });
    }

    @Test
    void emailInvalida() {
        assertThrows(IllegalArgumentException.class, () ->
                new Osservatore(1, "user", "email_senza_chicciola.it"));

        assertThrows(IllegalArgumentException.class, () ->
                new Osservatore(1, "user", null));
    }

    @Test
    void setEmailValida() {
        assertDoesNotThrow( () -> new Osservatore(1, "user", "vecchia@test.it"));
    }
}
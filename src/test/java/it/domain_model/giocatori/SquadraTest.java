package it.domain_model.giocatori;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SquadraTest {

    @Test
    void setNomeValido() {
        Squadra s = new Squadra(1, "Inter", "Serie A", "Italia");
        assertAll(
                () -> assertEquals("Inter", s.getNome()),
                () -> assertEquals(1, s.getIdSquadra()),
                () -> assertEquals("Serie A", s.getCampionato())
        );
    }

    @Test
    void setNomeVuoto() {
        assertThrows(IllegalArgumentException.class, () -> new Squadra(1, null, "A", "I"));

        assertThrows(IllegalArgumentException.class, () -> new Squadra(1, "", "A", "I"));

        assertThrows(IllegalArgumentException.class, () -> {
            Squadra s = new Squadra(1, "Milan", "A", "I");
            s.setNome("   ");
        });
    }
}


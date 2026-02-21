package it.domain_model.giocatori;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalciatoreTest {

    private Set<Ruolo> ruoliBase;

    @Mock
    private Contratto contrattoMock;

    @BeforeEach
    void setUp() {
        ruoliBase = new HashSet<>();
        ruoliBase.add(Ruolo.PUNTA_CENTRALE);
    }

    @Test
    void testGetEta() {
        LocalDate dataNascita = LocalDate.now().minusYears(25);
        Calciatore c = new Calciatore(1, "Mario", "Rossi", dataNascita, "Italiana", 75, 180, ruoliBase);

        assertEquals(25, c.getEta());
    }

    @Test
    void testDataNascitaInvalida() {
        LocalDate dataTroppoRecente = LocalDate.now().minusYears(10);

        assertThrows(IllegalArgumentException.class, () -> {
            new Calciatore(1, "Luca", "Verdi", dataTroppoRecente, "Italia", 60, 170, ruoliBase);
        });
    }

    @Test
    void testNomeInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Calciatore(1, "", "Rossi", LocalDate.of(1995, 1, 1), "Italia", 70, 180, ruoliBase);
        });
    }

    @Test
    void testImmutabilitaRuoli() {
        Calciatore c = new Calciatore(1, "Mario", "Rossi", LocalDate.of(1995, 1, 1), "Italia", 75, 180, ruoliBase);
        Set<Ruolo> ruoliEstratti = c.getRuoli();

        assertThrows(UnsupportedOperationException.class, () -> {
            ruoliEstratti.add(Ruolo.DIFENSORE_CENTRALE);
        });
    }

    @Test
    void testRuoliVuoti() {
        Set<Ruolo> ruoliVuoti = new HashSet<>();
        assertThrows(IllegalArgumentException.class, () -> {
            new Calciatore(1, "Mario", "Rossi", LocalDate.of(1995, 1, 1), "Italia", 75, 180, ruoliVuoti);
        });
    }
}
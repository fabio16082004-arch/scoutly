package it.domain_model.giocatori;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContrattoTest {

    @Mock
    private Calciatore calciatoreMock;

    @Mock
    private Squadra squadraMock;

    private LocalDate inizio;
    private LocalDate fine;

    @BeforeEach
    void setUp() {
        inizio = LocalDate.now().minusMonths(6);
        fine = LocalDate.now().plusMonths(6);
    }

    @Test
    void testCreazioneContrattoValido() {
        Contratto c = new Contratto(calciatoreMock, squadraMock, 1000000, inizio, fine);
        assertNotNull(c);
        assertEquals(calciatoreMock, c.getCalciatore());
        assertEquals(1000000, c.getStipendio());
    }

    @Test
    void testContrattoInvalido() {
        // 1. Validazione riferimenti obbligatori (null check)
        assertThrows(IllegalArgumentException.class, () ->
                new Contratto(null, squadraMock, 1000, inizio, fine));
        assertThrows(IllegalArgumentException.class, () ->
                new Contratto(calciatoreMock, null, 1000, inizio, fine));
        // 2. Validazione vincoli economici (stipendio negativo)
        assertThrows(IllegalArgumentException.class, () ->
                new Contratto(calciatoreMock, squadraMock, -1, inizio, fine));
        // 3. Validazione coerenza temporale (date invertite)
        LocalDate dataInizio = LocalDate.of(2025, 1, 1);
        LocalDate dataFinePrecedente = LocalDate.of(2024, 12, 31);
        assertThrows(IllegalArgumentException.class, () ->
                new Contratto(calciatoreMock, squadraMock, 1000, dataInizio, dataFinePrecedente));
    }
}
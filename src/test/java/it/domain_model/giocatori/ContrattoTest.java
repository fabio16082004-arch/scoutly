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
    void testEccezioneCalciatoreOSquadraNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Contratto(null, squadraMock, 1000, inizio, fine));

        assertThrows(IllegalArgumentException.class, () ->
                new Contratto(calciatoreMock, null, 1000, inizio, fine));
    }

    @Test
    void testStipendioNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Contratto(calciatoreMock, squadraMock, -1, inizio, fine));
    }

    @Test
    void testDateInvalide() {
        LocalDate dataInizio = LocalDate.of(2025, 1, 1);
        LocalDate dataFinePrecedente = LocalDate.of(2024, 12, 31);

        assertThrows(IllegalArgumentException.class, () ->
                new Contratto(calciatoreMock, squadraMock, 1000, dataInizio, dataFinePrecedente));
    }

    @Test
    void testSetStipendioValidoENegativo() {
        Contratto c = new Contratto(calciatoreMock, squadraMock, 1000, inizio, fine);

        c.setStipendio(2000f);
        assertEquals(2000f, c.getStipendio());

        assertThrows(IllegalArgumentException.class, () -> c.setStipendio(-500));
    }

    @Test
    void testSetDataFineContrattoInvalida() {
        Contratto c = new Contratto(calciatoreMock, squadraMock, 1000, inizio, fine);

        LocalDate scadenzaImpossibile = inizio.minusDays(1);

        assertThrows(IllegalArgumentException.class, () ->
                c.setDataFineContratto(scadenzaImpossibile));
    }
}
package it.domain_model.scouting;

import it.domain_model.giocatori.Calciatore;
import it.domain_model.giocatori.Ruolo;
import it.domain_model.utenti.Osservatore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListaTest {
    @Mock
    private Osservatore osservatoreMock;
    @Mock
    private Calciatore c1, c2;

    private Lista lista;

    @BeforeEach
    void setUp() {
        lista = new Lista("Talenti 2026", "Lista per giovani promesse", osservatoreMock);
    }

    @Test
    void aggiungiCalciatoreValido() {
        lista.aggiungiCalciatore(c1);
        assertEquals(1, lista.getCalciatori().size());
        assertTrue(lista.getCalciatori().contains(c1));
    }

    @Test
    void aggiungiCalciatoreNull() {
        lista.aggiungiCalciatore(null);
        assertTrue(lista.getCalciatori().isEmpty());
    }

    @Test
    void aggiungiDuplicato() {
        lista.aggiungiCalciatore(c1);
        lista.aggiungiCalciatore(c1);
        assertEquals(1, lista.getCalciatori().size());
    }

    @Test
    void testRimuoviCalciatoreEsistente() {
        when(c1.getId()).thenReturn(1);
        when(c2.getId()).thenReturn(2);

        lista.aggiungiCalciatore(c1);
        lista.aggiungiCalciatore(c2);

        lista.rimuoviCalciatore(1);

        assertEquals(1, lista.getCalciatori().size());
        assertFalse(lista.getCalciatori().contains(c1));
        assertTrue(lista.getCalciatori().contains(c2));
    }

    @Test
    void testFiltraPerRuolo() {
        when(c1.getRuoli()).thenReturn(Set.of(Ruolo.PUNTA_CENTRALE));
        when(c2.getRuoli()).thenReturn(Set.of(Ruolo.DIFENSORE_CENTRALE));

        lista.aggiungiCalciatore(c1);
        lista.aggiungiCalciatore(c2);

        List<Calciatore> punte = lista.filtraPerRuolo(Ruolo.PUNTA_CENTRALE);

        assertEquals(1, punte.size());
        assertEquals(c1, punte.get(0));
    }

    @Test
    void testDatiInizialiCostruttore() {
        assertNotNull(lista.getDataCreazione());
        assertEquals("Talenti 2026", lista.getNomeLista());
        assertEquals(osservatoreMock, lista.getOsservatore());
    }
}
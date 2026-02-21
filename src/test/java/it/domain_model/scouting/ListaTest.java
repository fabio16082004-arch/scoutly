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
    void testDatiInizialiCostruttore() {
        assertNotNull(lista.getDataCreazione());
        assertEquals("Talenti 2026", lista.getNomeLista());
        assertEquals(osservatoreMock, lista.getOsservatore());
    }
}
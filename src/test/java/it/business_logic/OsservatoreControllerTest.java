package it.business_logic;

import it.ORM.DAO.*;
import it.domain_model.statistiche.StatisticheCalciatoreStagione;
import it.domain_model.giocatori.Calciatore;
import it.domain_model.giocatori.Ruolo;
import it.domain_model.scouting.Lista;
import it.domain_model.utenti.Osservatore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OsservatoreControllerTest {

    private StatisticheDAO statisticheDAO;
    private ListaDAO listaDAO;
    private ReportDAO reportDAO;
    private CalciatoreDAO calciatoreDAO;
    private OsservatoreDAO osservatoreDAO;
    private OsservatoreController controller;

    @BeforeEach
    void setUp() {
        statisticheDAO = mock(StatisticheDAO.class);
        listaDAO = mock(ListaDAO.class);
        reportDAO = mock(ReportDAO.class);
        calciatoreDAO = mock(CalciatoreDAO.class);
        osservatoreDAO = mock(OsservatoreDAO.class);
        controller = new OsservatoreController(statisticheDAO, listaDAO, reportDAO, calciatoreDAO, osservatoreDAO);
    }

    @Test
    void testCreaNuovoReportValidazione() {
        assertThrows(IllegalArgumentException.class, () -> controller.creaNuovoReport(null, mock(Calciatore.class), null, null, "", false, 0));
        assertThrows(IllegalArgumentException.class, () -> controller.creaNuovoReport(mock(Osservatore.class), null, null, null, "", false, 0));
    }

    @Test
    void testCreaNuovaListaValidazione() {
        Osservatore o = mock(Osservatore.class);
        assertThrows(IllegalArgumentException.class, () -> controller.creaNuovaLista("", "Desc", o));
        assertThrows(IllegalArgumentException.class, () -> controller.creaNuovaLista("Nome", "Desc", null));
    }

    @Test
    void testEliminaListaFallimento() {
        when(listaDAO.eliminaLista(anyInt())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> controller.eliminaLista(1));
    }

    @Test
    void testAggiungiCalciatoreSuccesso() {
        Lista lista = mock(Lista.class);
        Calciatore calciatore = mock(Calciatore.class);
        when(lista.getCalciatori()).thenReturn(new ArrayList<>());
        when(calciatore.getId()).thenReturn(10);
        when(lista.getIdLista()).thenReturn(1);
        when(listaDAO.aggiungiCalciatoreAllaLista(1, 10)).thenReturn(true);

        controller.aggiungiCalciatore(lista, calciatore);

        verify(lista).aggiungiCalciatore(calciatore);
        verify(listaDAO).aggiungiCalciatoreAllaLista(1, 10);
    }

    @Test
    void testRimuoviCalciatoreFallimentoDB() {
        Lista lista = mock(Lista.class);
        when(lista.getIdLista()).thenReturn(1);
        when(listaDAO.rimuoviCalciatoreDallaLista(1, 10)).thenReturn(false);
        assertThrows(IllegalStateException.class, () -> controller.rimuoviCalciatoreDaLista(lista, 10));
    }


    @Test
    void testLoginValidazione() {
        assertThrows(IllegalArgumentException.class, () -> controller.login("", "pass"));
        assertThrows(IllegalArgumentException.class, () -> controller.login("user", null));
    }

    @Test
    void testLoginSuccesso() {
        Osservatore o = mock(Osservatore.class);
        when(osservatoreDAO.login("user", "pass")).thenReturn(o);
        assertEquals(o, controller.login("user", "pass"));
    }

    // --- TEST METODI MULTI-RIGA: STATISTICHE E COMPARAZIONE ---

    @Test
    void testGetDettaglioStatisticheCasiNull() {
        assertNull(controller.getDettaglioStatistiche(null, "2023", false));
        Calciatore c = mock(Calciatore.class);
        when(statisticheDAO.getStatisticheCalciatorePerStagione(anyInt(), anyString())).thenReturn(null);
        assertNull(controller.getDettaglioStatistiche(c, "2023", false));
    }

    @Test
    void testComparaCalciatoriValidazione() {
        assertThrows(IllegalArgumentException.class, () -> controller.comparaCalciatori(Collections.emptyList(), "2023", false));
        List<Calciatore> troppi = Collections.nCopies(6, mock(Calciatore.class));
        assertThrows(IllegalArgumentException.class, () -> controller.comparaCalciatori(troppi, "2023", false));
        assertThrows(IllegalArgumentException.class, () -> controller.comparaCalciatori(List.of(mock(Calciatore.class)), " ", false));
    }

    @Test
    void testComparaCalciatoriRamiRuoli() {
        Calciatore dc = mock(Calciatore.class);
        when(dc.getNome()).thenReturn("Chiellini");
        when(dc.getId()).thenReturn(3);
        when(dc.getRuoli()).thenReturn(Set.of(Ruolo.DIFENSORE_CENTRALE));

        when(dc.getRuoli()).thenReturn(Set.of(Ruolo.DIFENSORE_CENTRALE));

        StatisticheCalciatoreStagione stats = new StatisticheCalciatoreStagione(
                1000, 0, 0, 0, 0, 0, 0, 0, 0, 20, 15, 0, 0, 500, 0, 0, 1, 0
        );
        when(statisticheDAO.getStatisticheCalciatorePerStagione(3, "2023")).thenReturn(stats);

        ConfrontoCalciatoriResult res = controller.comparaCalciatori(List.of(dc), "2023", false);

        Map<String, Number> statsRisultanti = res.getStatsDi("Chiellini");

        assertTrue(statsRisultanti.containsKey("Contrasti Vinti"), "Dovrebbe contenere contrasti (decoratore)");
        assertTrue(statsRisultanti.containsKey("Gol"), "Dovrebbe contenere i gol (base)");
        assertFalse(statsRisultanti.containsKey("Parate"), "Non dovrebbe contenere parate (decoratore portiere)");
    }

    @Test
    void testTrovaPrimoRuoloComuneNessunMatch() {
        Calciatore c1 = mock(Calciatore.class);
        when(c1.getNome()).thenReturn("Portiere");
        when(c1.getId()).thenReturn(1);
        when(c1.getRuoli()).thenReturn(Set.of(Ruolo.PORTIERE));

        Calciatore c2 = mock(Calciatore.class);
        when(c2.getNome()).thenReturn("Attaccante");
        when(c2.getId()).thenReturn(2);
        when(c2.getRuoli()).thenReturn(Set.of(Ruolo.PUNTA_CENTRALE));

        StatisticheCalciatoreStagione s1 = mock(StatisticheCalciatoreStagione.class);
        StatisticheCalciatoreStagione s2 = mock(StatisticheCalciatoreStagione.class);
        when(statisticheDAO.getStatisticheCalciatorePerStagione(1, "2023")).thenReturn(s1);
        when(statisticheDAO.getStatisticheCalciatorePerStagione(2, "2023")).thenReturn(s2);

        ConfrontoCalciatoriResult res = controller.comparaCalciatori(List.of(c1, c2), "2023", false);


        assertEquals(5, res.getStatsDi("Portiere").size());
        assertEquals(5, res.getStatsDi("Attaccante").size());
    }

    @Test
    void testComparaCalciatoriConMatchDiRuolo() {
        Calciatore c1 = mock(Calciatore.class);
        when(c1.getNome()).thenReturn("Locatelli");
        when(c1.getId()).thenReturn(5);
        when(c1.getRuoli()).thenReturn(Set.of(Ruolo.MEDIANO, Ruolo.REGISTA));

        Calciatore c2 = mock(Calciatore.class);
        when(c2.getNome()).thenReturn("Anguissa");
        when(c2.getId()).thenReturn(8);
        when(c2.getRuoli()).thenReturn(Set.of(Ruolo.MEDIANO, Ruolo.DIFENSORE_CENTRALE));

        StatisticheCalciatoreStagione s1 = mock(StatisticheCalciatoreStagione.class);
        StatisticheCalciatoreStagione s2 = mock(StatisticheCalciatoreStagione.class);
        when(statisticheDAO.getStatisticheCalciatorePerStagione(5, "2023")).thenReturn(s1);
        when(statisticheDAO.getStatisticheCalciatorePerStagione(8, "2023")).thenReturn(s2);

        ConfrontoCalciatoriResult res = controller.comparaCalciatori(List.of(c1, c2), "2023", false);

        Map<String, Number> statsLocatelli = res.getStatsDi("Locatelli");
        assertEquals(10, statsLocatelli.size());

        assertTrue(statsLocatelli.containsKey("Contrasti Vinti"));
        assertTrue(statsLocatelli.containsKey("Gol"));
        assertFalse(statsLocatelli.containsKey("Parate"));
    }
}
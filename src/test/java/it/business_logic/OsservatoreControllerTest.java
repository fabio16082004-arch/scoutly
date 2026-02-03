package it.business_logic;

import it.DAO.*;
import it.domain_model.analisi.RisultatoRanking;
import it.domain_model.analisi.StatisticheCalciatoreStagione;
import it.domain_model.giocatori.Calciatore;
import it.domain_model.giocatori.Ruolo;
import it.domain_model.scouting.Lista;
import it.domain_model.utenti.Osservatore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
    void testOttieniRankingNormalizzato() {
        String campionato = "Serie A";
        String statistica = "gol";
        List<RisultatoRanking> mockRanking = new ArrayList<>();
        mockRanking.add(new RisultatoRanking(1, "Mario Rossi", 1.0, 45));

        when(statisticheDAO.getRankingCalciatori(anyString(), anyString(), any(), anyString(), anyInt()))
                .thenReturn(mockRanking);

        List<RisultatoRanking> risultato = controller.ottieniRanking(campionato, "2023", statistica, Ruolo.PUNTA_CENTRALE, 10, "normalizzata");

        assertEquals(2.0, risultato.get(0).getValore());
    }

    @Test
    void testOttieniRankingAssoluto() {
        List<RisultatoRanking> mockRanking = new ArrayList<>();
        mockRanking.add(new RisultatoRanking(1, "Mario Rossi", 5.0, 450));

        when(statisticheDAO.getRankingCalciatori(anyString(), anyString(), any(), anyString(), anyInt()))
                .thenReturn(mockRanking);

        List<RisultatoRanking> risultato = controller.ottieniRanking("Serie A", "2023", "gol", Ruolo.PUNTA_CENTRALE, 10, "assoluto");

        assertEquals(5.0, risultato.get(0).getValore());
    }

    @Test
    void testGetDettaglioStatisticheNormalizzato() {
        Calciatore c = mock(Calciatore.class);
        when(c.getId()).thenReturn(1);

        StatisticheCalciatoreStagione rawStats = new StatisticheCalciatoreStagione(
                1800, 10, 5, 8.0, 4.0, 40, 20, 15, 30, 10, 5, 20, 10, 800, 0, 0, 2, 0
        );

        when(statisticheDAO.getStatisticheCalciatorePerStagione(1, "2023/24")).thenReturn(rawStats);

        StatisticheCalciatoreStagione statsNormalizzate = controller.getDettaglioStatistiche(c, "2023/24", "normalizzata");

        assertNotNull(statsNormalizzate);
        assertEquals(0.5, statsNormalizzate.getGol());
        assertEquals(2, statsNormalizzate.getCartelliniGialli());
    }

    @Test
    void testCreaNuovaListaSenzaNome() {
        Osservatore o = mock(Osservatore.class);

        assertThrows(IllegalArgumentException.class, () -> {
            controller.creaNuovaLista("", "Descrizione", o);
        });
    }

    @Test
    void testLoginInvalido() {
        when(osservatoreDAO.login("user", "wrong_pass")).thenReturn(null);

        Osservatore result = controller.login("user", "wrong_pass");

        assertNull(result);
    }

    @Test
    void testAggiungiCalciatoreGiaPresente() {
        Lista lista = mock(Lista.class);
        Calciatore calciatore = mock(Calciatore.class);

        when(lista.getCalciatori()).thenReturn(List.of(calciatore));
        when(calciatore.getNome()).thenReturn("Luca");

        assertThrows(IllegalStateException.class, () -> {
            controller.aggiungiCalciatore(lista, calciatore);
        });
    }
}
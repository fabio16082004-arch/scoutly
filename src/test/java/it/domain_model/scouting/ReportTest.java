package it.domain_model.scouting;

import it.domain_model.giocatori.Calciatore;
import it.domain_model.giocatori.Partita;
import it.domain_model.utenti.Osservatore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReportTest {
    private Report report;

    @Mock
    private Osservatore osservatoreMock;

    @Mock
    private Calciatore calciatoreMock;

    @Mock
    private Partita partitaMock;

    @BeforeEach
    public void setup() {
        report = new Report(osservatoreMock, calciatoreMock);
    }

    @Test
    public void testAggiungiPartitaValida() {
        report.aggiungiPartita(partitaMock);
        assertEquals(1, report.getPartite().size());
        assertTrue(report.getPartite().contains(partitaMock));}
    @Test
    public void testAggiungiPartitaNull() {
        report.aggiungiPartita(null);
        assertTrue(report.getPartite().isEmpty());
    }
    @Test
    public void testAggiungiVotoValido() {
        report.aggiungiVoto("Tecnica", 8);
        Map<String, Integer> voti = report.getVoti();
        assertEquals(1, voti.size());
        assertEquals(8, voti.get("Tecnica"));
    }
    @Test
    public void testAggiungiVotoNonValidoLanciaEccezione() {
        assertThrows(IllegalArgumentException.class,
                () -> report.aggiungiVoto("Fisico", 12));}
    @Test
    public void testCalcolaVotoComplessivo() {
        report.aggiungiVoto("Tecnica", 8);
        report.aggiungiVoto("Tattica", 6);
        report.calcolaVotoComplessivo();
        assertEquals(7, report.getVotoComplessivo());
    }
    @Test
    public void testCalcolaVotoSenzaVoti() {
        report.calcolaVotoComplessivo();
        assertEquals(0, report.getVotoComplessivo(), "Il voto dovrebbe essere 0 se non ci sono parametri");
    }
    @Test
    public void testSetVotoComplessivoValido() {
        report.setVotoComplessivo(0);
        assertEquals(0, report.getVotoComplessivo());
    }
    @Test
    public void testSetVotoComplessivoNonValido() {
        assertThrows(IllegalArgumentException.class,
                () -> report.setVotoComplessivo(11));
    }
}
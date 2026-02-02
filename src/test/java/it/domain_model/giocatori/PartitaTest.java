package it.domain_model.giocatori;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class PartitaTest {
    @Mock
    private Squadra squadraCasa;

    @Mock
    private Squadra squadraOspite;

    @Test
    void impostaStagioneValida(){
        Partita partita = new Partita(0, 2, 0,
                LocalDate.of(2023, 4, 5), squadraCasa,
                squadraOspite, "2023/2024");
        assertNotNull(partita);
        assertEquals(0, partita.getIdPartita());
        assertEquals(2, partita.getPunteggioCasa());
        assertEquals(0, partita.getPunteggioOspite());
        assertEquals(LocalDate.of(2023, 4, 5), partita.getData());
    }

    @Test
    void impostaStagioneFormatoNonValido(){
        Partita partita = new Partita(0, 2, 0,
                LocalDate.of(2023, 4, 5), squadraCasa,
                squadraOspite, "2023/2024");

        assertThrows(IllegalArgumentException.class,
                () -> partita.setStagione("890/2021"));
        assertThrows(IllegalArgumentException.class,
                () -> partita.setStagione("2020-2021"));
        assertThrows(IllegalArgumentException.class,
                () -> partita.setStagione("Ciao/Mondo"));
    }

    @Test
    void impostaStagioneAnniNonValidi(){
        Partita partita = new Partita(0, 2, 0,
                LocalDate.of(2023, 4, 5), squadraCasa,
                squadraOspite, "2023/2024");
        assertThrows(IllegalArgumentException.class,
                () -> partita.setStagione("1888/1889"));
        assertThrows(IllegalArgumentException.class,
                () -> partita.setStagione("2021/2023"));
    }
}

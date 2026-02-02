package it.business_logic;

import it.domain_model.analisi.StatisticheCalciatoreStagione;
import it.domain_model.trattamento_statistiche.SelettoreStrategia;
import it.domain_model.analisi.RisultatoRanking;
import it.domain_model.giocatori.Calciatore;
import it.domain_model.giocatori.Partita;
import it.domain_model.giocatori.Ruolo;
import it.domain_model.scouting.Lista;
import it.domain_model.scouting.Report;
import it.domain_model.utenti.Osservatore;
import it.DAO.StatisticheDAO;
import it.DAO.ListaDAO;
import it.DAO.ReportDAO;
import it.DAO.CalciatoreDAO;
import it.DAO.OsservatoreDAO;

import java.util.List;
import java.util.Map;

public class OsservatoreController {

    private final StatisticheDAO statisticheDAO;
    private final ListaDAO listaDAO;
    private final ReportDAO reportDAO;
    private final CalciatoreDAO calciatoreDAO;
    private final OsservatoreDAO osservatoreDAO;

    public OsservatoreController(StatisticheDAO sDAO, ListaDAO lDAO, ReportDAO rDAO, CalciatoreDAO cDAO, OsservatoreDAO oDAO) {
        this.statisticheDAO = sDAO;
        this.listaDAO = lDAO;
        this.reportDAO = rDAO;
        this.calciatoreDAO = cDAO;
        this.osservatoreDAO = oDAO;
    }

    public List<RisultatoRanking> ottieniRanking(String campionato, String stagione, String statistica, Ruolo ruolo, int minMinutiGiocati) {
        if (campionato == null || campionato.isBlank()) {
            throw new IllegalArgumentException("Il campionato è obbligatorio.");
        }
        if (statistica == null || statistica.isBlank()) {
            throw new IllegalArgumentException("La statistica è obbligatoria.");
        }
        if (stagione == null || stagione.isBlank()) {
            throw new IllegalArgumentException("La stagione è obbligatoria.");
        }

        List<RisultatoRanking> ranking = statisticheDAO.getRankingCalciatori(campionato, stagione, ruolo.getMacroRuolo(), statistica, minMinutiGiocati);

        for (RisultatoRanking riga : ranking) {
            double valore = riga.getValore();
            int minutiTotali = riga.getMinutiTotali();
            double factor = (minutiTotali > 0) ? (90.0 / minutiTotali) : 0.0;

            if (!(statistica.equals("minutiGiocati") || statistica.equals("cartellini") || statistica.equals("precisione"))) {
                riga.setValore(valore * factor);
            }
        }

        ranking.sort((m1, m2) -> Double.compare(m2.getValore(), m1.getValore()));
        return ranking;
    }

    public Report creaNuovoReport(Osservatore osservatore, Calciatore calciatore,
                                  List<Partita> partite, Map<String, Integer> voti,
                                  String noteFinali, boolean calcolaVoto, int votoComplessivo) {

        if (osservatore == null || calciatore == null) {
            throw new IllegalArgumentException("Osservatore e calciatore sono obbligatori.");
        }

        Report report = new Report(osservatore, calciatore);
        if (partite != null) partite.forEach(report::aggiungiPartita);
        if (voti != null) voti.forEach(report::aggiungiVoto);

        report.setNoteFinali(noteFinali);

        if (calcolaVoto) {
            report.calcolaVotoComplessivo();
        } else {
            report.setVotoComplessivo(votoComplessivo);
        }

        reportDAO.salva(report);
        return report;
    }

    public void cancellaReport(int idReport) {
        if (!reportDAO.esisteReport(idReport)) {
            throw new IllegalArgumentException("Report con ID " + idReport + " non trovato.");
        }
        if (!reportDAO.eliminaReport(idReport)) {
            throw new IllegalStateException("Errore imprevisto durante l'eliminazione del report.");
        }
    }

    public void creaNuovaLista(String nome, String descrizione, Osservatore osservatore) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Il nome della lista è obbligatorio.");
        }
        if (osservatore == null) {
            throw new IllegalArgumentException("La lista deve essere associata a un osservatore.");
        }
        Lista lista = new Lista(nome, descrizione, osservatore);
        listaDAO.creaLista(lista);
    }

    public void eliminaLista(int idLista) {
        boolean success = listaDAO.eliminaLista(idLista);
        if (!success) {
            throw new IllegalArgumentException("Impossibile eliminare: lista non trovata.");
        }
    }

    public void aggiungiCalciatore(Lista lista, Calciatore calciatore) {
        if (lista.getCalciatori().contains(calciatore)) {
            throw new IllegalStateException("Il calciatore " + calciatore.getNome() +
                    " è già nella lista " + lista.getNomeLista());
        }

        if (!listaDAO.aggiungiCalciatoreAllaLista(lista.getIdLista(), calciatore.getId())) {
            throw new RuntimeException("Errore nel salvataggio sul database");
        }

        lista.aggiungiCalciatore(calciatore);
    }

    public List<Lista> getListeUtente(int idUtente) {
        return listaDAO.getListePerUtente(idUtente);
    }

    public Lista apriLista(int idLista) {
        return listaDAO.getListaById(idLista);
    }

    public List<Calciatore> filtraCalciatori(Lista lista, Ruolo ruolo) {
        if (ruolo == null) return lista.getCalciatori();

        return lista.filtraPerRuolo(ruolo);
    }

    public void rimuoviCalciatoreDaLista(Lista lista, int idCalciatore) {
        boolean rimossoDalDB = listaDAO.rimuoviCalciatoreDallaLista(lista.getIdLista(), idCalciatore);

        if (!rimossoDalDB) {
            throw new IllegalStateException("Impossibile rimuovere: il calciatore non era presente nel database.");
        }

        lista.rimuoviCalciatore(idCalciatore);
    }

    public StatisticheCalciatoreStagione getDettaglioStatistiche(Calciatore selezionato, String stagione, String strategia) {
        if (selezionato == null) return null;

        StatisticheCalciatoreStagione stats = statisticheDAO.getStatisticheCalciatorePerStagione(selezionato.getId(), stagione);

        if (stats == null) return null;

        SelettoreStrategia ss = new SelettoreStrategia();
        ss.selezionaStrategia(strategia);
        stats = ss.elabora(stats);
        return stats;
    }

    public List<Calciatore> cercaGiocatori(List<String> sigleRuoli, Integer idSquadra, String stagione,
                                           String campionato, Integer minEta, Integer maxEta,
                                           Integer minAnniContratto) {
        int min = (minEta == null || minEta < 14) ? 14 : minEta;
        int max = (maxEta == null || maxEta > 50) ? 50 : maxEta;

        return calciatoreDAO.cerca(sigleRuoli, idSquadra, stagione, campionato, min, max, minAnniContratto);
    }
    public Osservatore login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Credenziali obbligatorie.");
        }
        Osservatore o = osservatoreDAO.login(username, password);
        if (o == null) System.out.println("Credenziali non valide per: " + username);
        return o;
    }

    public void registraNuovoOsservatore(String username, String email, String password) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username obbligatorio.");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Email non valida.");
        if (password == null || password.length() < 8) throw new IllegalArgumentException("Password troppo corta (min 8).");

        osservatoreDAO.registraOsservatore(username, email, password);
    }
}
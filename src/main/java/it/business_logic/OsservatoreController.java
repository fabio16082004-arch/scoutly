package it.business_logic;

import it.domain_model.analisi.RisultatoConfrontoCalciatori;
import it.domain_model.analisi.RisultatoRanking;
import it.domain_model.analisi.RisultatoStatisticheCalciatore;
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

import java.util.ArrayList;
import java.util.HashMap;
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

    // --- LISTE ---

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

    public RisultatoConfrontoCalciatori comparaCalciatori(List<Calciatore> calciatori, String stagione) {
        if (calciatori.size() > 5) {
            throw new IllegalArgumentException("Non si possono confrontare più di 5 calciatori contemporaneamente");
        }

        Ruolo ruoloRiferimento = getRuoloRiferimento(calciatori);
        List<RisultatoStatisticheCalciatore> risultati = new ArrayList<>();

        for (Calciatore c : calciatori) {
            Map<String, Double> stats = getStatisticheCalciatoreNormalizzate(c.getId(), ruoloRiferimento, stagione);
            risultati.add(new RisultatoStatisticheCalciatore(c, stats));
        }

        Map<String, Calciatore> miglioriPerMetrica = new HashMap<>();
        if (!risultati.isEmpty()) {
            for (String metrica : risultati.get(0).getStatistiche().keySet()) {
                RisultatoStatisticheCalciatore best = null;
                double maxValue = -1.0;

                for (RisultatoStatisticheCalciatore r : risultati) {
                    Double valore = r.getStat(metrica);
                    if (valore != null && valore > maxValue) {
                        maxValue = valore;
                        best = r;
                    }
                }
                if (best != null) miglioriPerMetrica.put(metrica, best.getCalciatore());
            }
        }

        return new RisultatoConfrontoCalciatori(risultati, miglioriPerMetrica);
    }

    private Map<String, Double> getStatisticheCalciatoreNormalizzate(int idCalciatore, Ruolo ruoloRiferimento, String stagione) {
        List<String> metriche = new ArrayList<>(List.of("minutiGiocati", "goal", "xG", "assist", "xA"));

        if (ruoloRiferimento != null) {
            metriche.addAll(ruoloRiferimento.getMetricheSpecifiche());
        }

        Map<String, Double> rawData = statisticheDAO.getStatisticheCalciatorePerStagione(idCalciatore, metriche, stagione);
        if (rawData.isEmpty()) return rawData;

        double minuti = rawData.getOrDefault("minutiGiocati", 0.0);
        double factor = (minuti > 0) ? (90.0 / minuti) : 0.0;

        Map<String, Double> risultato = new HashMap<>();

        for (Map.Entry<String, Double> entry : rawData.entrySet()) {
            String k = entry.getKey();
            if (k.equals("minutiGiocati") || k.contains("precisione") || k.startsWith("cartellini")) {
                risultato.put(k, entry.getValue());
            } else {
                risultato.put(k, entry.getValue() * factor);
            }
        }
        return risultato;
    }

    private Ruolo getRuoloRiferimento(List<Calciatore> calciatori) {
        if (calciatori == null || calciatori.isEmpty()) return null;
        for (Ruolo r1 : calciatori.get(0).getRuoli()) {
            String target = r1.getMacroRuolo();
            boolean common = true;
            for (int i = 1; i < calciatori.size(); i++) {
                if (calciatori.get(i).getRuoli().stream().noneMatch(r -> r.getMacroRuolo().equals(target))) {
                    common = false;
                    break;
                }
            }
            if (common) return r1;
        }
        return null;
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
package it.business_logic.service;

import it.ORM.DAO.ListaDAO;
import it.domain_model.scouting.Lista;
import it.domain_model.utenti.Osservatore;

import java.util.ArrayList;
import java.util.List;

public class ListaService {
    private final ListaDAO listaDAO;

    public ListaService(ListaDAO listaDAO) {
        this.listaDAO = listaDAO;
    }

    public void creaLista(String nome, String descrizione, Osservatore osservatore) {
        Lista lista = new Lista(nome, descrizione, osservatore);
        listaDAO.creaLista(lista);
    }

    public void aggiungiCalciatore(int idLista, int idCalciatore) {
        boolean successo = listaDAO.aggiungiCalciatoreAllaLista(idLista, idCalciatore);

        if (!successo) {
            throw new IllegalStateException("Il calciatore è già presente in questa lista.");
        }
    }

    public void rimuoviCalciatore(int idLista, int idCalciatore) {
        if (!listaDAO.rimuoviCalciatoreDallaLista(idLista, idCalciatore)) {
            throw new IllegalStateException("Impossibile rimuovere: legame non trovato.");
        }
    }

    public void eliminaLista(int idLista) {
        boolean eliminata = listaDAO.eliminaLista(idLista);

        if (!eliminata) {
            throw new RuntimeException("Errore durante l'eliminazione della lista nel database.");
        }
    }

    public List<Lista> getListeOsservatore(int idOsservatore) {
        List<Lista> liste = listaDAO.getListePerUtente(idOsservatore);

        if (liste == null) {
            return new ArrayList<>();
        }

        return liste;
    }
}
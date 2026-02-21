package it.business_logic.services;
import it.ORM.DAO.OsservatoreDAO;
import it.domain_model.utenti.Osservatore;

public class AuthService {
    private final OsservatoreDAO osservatoreDAO;

    public AuthService(OsservatoreDAO osservatoreDAO) {
        this.osservatoreDAO = osservatoreDAO;
    }

    public Osservatore login(String username, String password) {
        Osservatore osservatore = osservatoreDAO.login(username, password);

        if (osservatore == null) {
            throw new RuntimeException("Credenziali non valide");
        }

        return osservatore;
    }

    public void registraNuovoOsservatore(String username, String email, String password) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username obbligatorio.");
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Email non valida.");
        if (password == null || password.length() < 8) throw new IllegalArgumentException("Password troppo corta (min 8).");

        if (osservatoreDAO.esisteUsername(username)) {
            throw new IllegalStateException("Lo username '" + username + "' è già presente nel sistema.");
        }

        if (osservatoreDAO.esisteEmail(email)) {
            throw new IllegalStateException("L'email '" + email + "' è già associata a un account.");
        }

        osservatoreDAO.registraOsservatore(username, email, password);
    }
}
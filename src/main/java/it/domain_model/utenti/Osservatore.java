package it.domain_model.utenti;

public class Osservatore {
    private int id;
    private String username;
    private String email;

    public Osservatore(int id, String username, String email) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username non valido.");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Formato email non valido.");
        }
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public int getId() { return id; }

    public String getUsername() { return username; }

    public String getEmail() { return email; }

}
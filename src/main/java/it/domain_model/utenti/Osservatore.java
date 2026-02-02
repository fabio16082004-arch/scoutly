package it.domain_model.utenti;

public class Osservatore {
    private int id;
    private String username;
    private String email;

    public Osservatore(int id, String username, String email) {
        this.id = id;
        setUsername(username);
        setEmail(email);
    }

    public int getId() { return id; }

    public String getUsername() { return username; }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username non valido.");
        }
        this.username = username;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Formato email non valido.");
        }
        this.email = email;
    }
}
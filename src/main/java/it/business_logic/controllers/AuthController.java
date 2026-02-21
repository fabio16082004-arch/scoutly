package it.business_logic.controllers;

import it.business_logic.services.AuthService;
import it.domain_model.utenti.Osservatore;

public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public void registraUtente(String username, String email, String password) {
        authService.registraNuovoOsservatore(username, email, password); //
    }

    public Osservatore login(String username, String password) {
        return authService.login(username, password); //
    }
}
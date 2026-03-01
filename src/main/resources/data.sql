-- RUOLO
INSERT INTO Ruolo (Sigla, descrizioneEstesa) VALUES
                                                 ('POR', 'Portiere'), ('DC', 'Difensore centrale'), ('TD', 'Terzino destro'),
                                                 ('TS', 'Terzino sinistro'), ('ED', 'Esterno destro'), ('ES', 'Esterno sinistro'),
                                                 ('MED', 'Centrocampista difensivo'), ('CC', 'Centrocampista centrale'),
                                                 ('TRQ', 'Trequartista'), ('AD', 'Ala destra'), ('AS', 'Ala sinistra'), ('PC', 'Punta centrale');

-- SQUADRA
INSERT INTO Squadra (nome, campionato, nazione) VALUES
                                                    ('Juventus', 'Serie A', 'Italia'), ('Inter', 'Serie A', 'Italia'),
                                                    ('Napoli', 'Serie A', 'Italia'), ('Milan', 'Serie A', 'Italia'), ('Roma', 'Serie A', 'Italia'),
                                                    ('Real Madrid', 'La Liga', 'Spagna'), ('Barcelona', 'La Liga', 'Spagna'),
                                                    ('Manchester City', 'Premier League', 'Inghilterra'), ('Bayern Monaco', 'Bundesliga', 'Germania'),
                                                    ('PSG', 'Ligue 1', 'Francia');

-- OSSERVATORE
INSERT INTO Osservatore (username, email, password) VALUES
                                                        ('mario.rossi', 'mario.rossi@scouting.it', '$2a$10$hashedpassword1'),
                                                        ('luca.bianchi', 'luca.bianchi@scouting.it', '$2a$10$hashedpassword2'),
                                                        ('anna.verdi', 'anna.verdi@scouting.it', '$2a$10$hashedpassword3'),
                                                        ('paolo.neri', 'paolo.neri@scouting.it', '$2a$10$hashedpassword4');

-- CALCIATORE
INSERT INTO Calciatore (nome, cognome, dataNascita, nazionalità, peso, altezza) VALUES
                                                                                    ('Lautaro', 'Martinez', '1997-08-22', 'Argentina', 76.00, 174),
                                                                                    ('Dusan', 'Vlahovic', '2000-01-28', 'Serbia', 89.00, 190),
                                                                                    ('Rafael', 'Leao', '1999-06-10', 'Portogallo', 78.00, 188),
                                                                                    ('Khvicha', 'Kvaratskhelia', '1999-02-12', 'Georgia', 73.00, 183),
                                                                                    ('Federico', 'Chiesa', '1997-10-25', 'Italia', 75.00, 175),
                                                                                    ('Mike', 'Maignan', '1995-07-03', 'Francia', 91.00, 191),
                                                                                    ('Alessandro', 'Bastoni', '1999-04-13', 'Italia', 83.00, 191),
                                                                                    ('Nicolo', 'Barella', '1997-02-07', 'Italia', 68.00, 172),
                                                                                    ('Lorenzo', 'Pellegrini', '1996-06-19', 'Italia', 77.00, 180),
                                                                                    ('Ademola', 'Lookman', '1997-10-20', 'Nigeria', 70.00, 175);

-- RUOLOCALCIATORE
INSERT INTO RuoloCalciatore (Sigla, Calciatore) VALUES
                                                    ('PC', 1), ('PC', 2), ('AD', 3), ('AS', 4), ('AD', 5), ('POR', 6), ('DC', 7), ('CC', 8), ('TRQ', 9), ('AS', 10);

-- CONTRATTO
INSERT INTO Contratto (Calciatore, squadra, stipendio, dataInizio, dataFine) VALUES
                                                                                 (1, 2, 8000000.00, '2022-07-01', '2026-06-30'), (2, 1, 12000000.00, '2021-07-01', '2026-06-30'),
                                                                                 (3, 4, 7000000.00, '2021-07-01', '2025-06-30'), (4, 3, 5500000.00, '2022-07-01', '2027-06-30'),
                                                                                 (5, 2, 4000000.00, '2023-07-01', '2027-06-30'), (6, 4, 6000000.00, '2021-07-01', '2026-06-30'),
                                                                                 (7, 2, 5000000.00, '2020-07-01', '2026-06-30'), (8, 2, 6500000.00, '2016-07-01', '2026-06-30'),
                                                                                 (9, 5, 4500000.00, '2020-07-01', '2025-06-30'), (10, 3, 3000000.00, '2022-07-01', '2026-06-30');

-- PARTITA
INSERT INTO Partita (idPartita, punteggioCasa, punteggioOspite, data, stagione, squadraCasa, squadraOspite) VALUES
                                                                                                                (1, 2, 1, '2024-09-15', '2024/2025', 2, 4), (2, 1, 1, '2024-09-22', '2024/2025', 1, 3),
                                                                                                                (3, 3, 0, '2024-10-05', '2024/2025', 3, 5), (4, 0, 2, '2024-10-20', '2024/2025', 5, 2),
                                                                                                                (5, 1, 2, '2024-11-03', '2024/2025', 4, 1), (6, 2, 2, '2024-11-10', '2024/2025', 2, 3),
                                                                                                                (7, 1, 0, '2024-11-24', '2024/2025', 1, 5);
-- ... (inserimenti precedenti di Squadra, Calciatore, ecc.)

-- REPORT (Usiamo idReport 10 per i test)
INSERT INTO Report (idReport, votoComplessivo, noteFinali, dataCreazione, utente, calciatore)
VALUES (10, 8.0, 'Giocatore fondamentale per il gioco offensivo.', CURRENT_TIMESTAMP, 1, 1);

-- REPORTPARTITE
INSERT INTO ReportPartite (idReport, idPartita) VALUES (10, 1);
INSERT INTO ReportPartite (idReport, idPartita) VALUES (10, 4);

-- VOTO (La colonna si chiama idReport come definito nello schema)
INSERT INTO Voto (idReport, nota, punteggio) VALUES (10, 'Tecnica', 9);
INSERT INTO Voto (idReport, nota, punteggio) VALUES (10, 'Fisico', 7);

-- ... (inserimenti successivi di Lista, Statistiche, ecc.)

-- LISTA
INSERT INTO Lista (idLista, nomeLista, descrizione, osservatore) VALUES
                                                                     (1, 'Attaccanti Serie A', 'Migliori attaccanti', 1), (2, 'Prospetti Under 23', 'Giovani talenti', 2),
                                                                     (3, 'Centrocampisti Top', 'Livello internazionale', 1), (4, 'Difensori Affidabili', 'Reparto solido', 3);

-- LISTACALCIATORE
INSERT INTO ListaCalciatore (idLista, Calciatore) VALUES
                                                      (1, 1), (1, 2), (1, 10), (2, 4), (2, 3), (3, 8), (3, 9), (4, 7);

-- STATISTICHE
INSERT INTO Statistiche (Calciatore, Partita, minutiGiocati, gol, assist, xG, xA, tiriTotali, tiriInPorta, dribblingRiusciti, parate, cleanSheet) VALUES
                                                                                                                                                      (1, 1, 90, 1, 1, 1.80, 0.50, 4, 3, 2, 0, FALSE), (1, 4, 90, 2, 0, 2.10, 0.20, 5, 4, 1, 0, FALSE),
                                                                                                                                                      (2, 5, 85, 1, 0, 1.50, 0.10, 3, 2, 0, 0, FALSE), (2, 2, 90, 0, 1, 0.80, 0.60, 2, 1, 1, 0, FALSE),
                                                                                                                                                      (3, 1, 90, 0, 1, 0.90, 0.80, 3, 1, 5, 0, FALSE), (3, 5, 90, 1, 1, 1.20, 0.70, 4, 2, 6, 0, FALSE),
                                                                                                                                                      (6, 1, 90, 0, 0, 0.00, 0.00, 0, 0, 0, 3, FALSE), (6, 5, 90, 0, 0, 0.00, 0.00, 0, 0, 0, 5, TRUE),
                                                                                                                                                      (8, 1, 90, 0, 1, 0.20, 0.50, 1, 0, 3, 0, FALSE), (8, 6, 90, 1, 0, 0.40, 0.30, 2, 1, 2, 0, FALSE);
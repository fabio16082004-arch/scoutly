-- Pulizia iniziale di tutte le tabelle (ordine corretto per le Foreign Keys)
DROP TABLE IF EXISTS Statistiche, ReportPartite, Voto, Report, ListaCalciatore, Lista, Contratto, RuoloCalciatore, Partita, Calciatore, Squadra, Osservatore, Ruolo CASCADE;

-- Definizione Tabelle Anagrafiche
CREATE TABLE Ruolo (
                       Sigla VARCHAR(5) PRIMARY KEY,
                       descrizioneEstesa VARCHAR(100)
);

CREATE TABLE Squadra (
                         idSquadra SERIAL PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         campionato VARCHAR(100),
                         nazione VARCHAR(50)
);

CREATE TABLE Osservatore (
                             idOsservatore SERIAL PRIMARY KEY,
                             username VARCHAR(50) UNIQUE NOT NULL,
                             email VARCHAR(100) UNIQUE NOT NULL,
                             password VARCHAR(255) NOT NULL
);

CREATE TABLE Calciatore (
                            idCalciatore SERIAL PRIMARY KEY,
                            nome VARCHAR(50) NOT NULL,
                            cognome VARCHAR(50) NOT NULL,
                            dataNascita DATE,
                            nazionalità VARCHAR(50),
                            peso DECIMAL(5,2),
                            altezza INT
);

-- Relazioni e Strutture Core
CREATE TABLE RuoloCalciatore (
                                 Sigla VARCHAR(5) REFERENCES Ruolo(Sigla),
                                 Calciatore INT REFERENCES Calciatore(idCalciatore),
                                 PRIMARY KEY (Sigla, Calciatore)
);

CREATE TABLE Contratto (
                           Calciatore INT REFERENCES Calciatore(idCalciatore),
                           squadra INT REFERENCES Squadra(idSquadra),
                           stipendio DECIMAL(15,2),
                           dataInizio DATE,
                           dataFine DATE,
                           PRIMARY KEY (Calciatore, squadra, dataInizio)
);

CREATE TABLE Lista (
                       idLista SERIAL PRIMARY KEY,
                       nomeLista VARCHAR(100) NOT NULL,
                       descrizione TEXT,
                       dataCreazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       osservatore INT REFERENCES Osservatore(idOsservatore)
);

CREATE TABLE ListaCalciatore (
                                 idLista INT REFERENCES Lista(idLista),
                                 Calciatore INT REFERENCES Calciatore(idCalciatore),
                                 PRIMARY KEY (idLista, Calciatore)
);

CREATE TABLE Partita (
                         idPartita SERIAL PRIMARY KEY,
                         punteggioCasa INT,
                         punteggioOspite INT,
                         data DATE NOT NULL,
                         stagione VARCHAR(9) NOT NULL,
                         squadraCasa INT REFERENCES Squadra(idSquadra),
                         squadraOspite INT REFERENCES Squadra(idSquadra)
);

-- Sezione Report e Valutazioni
CREATE TABLE Report (
                        idReport SERIAL PRIMARY KEY,
                        votoComplessivo DECIMAL(3,1),
                        noteFinali TEXT,
                        dataCreazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        utente INT NOT NULL REFERENCES Osservatore(idOsservatore),
                        calciatore INT NOT NULL REFERENCES Calciatore(idCalciatore),
                        UNIQUE (utente, calciatore)
);

CREATE TABLE Voto (
                      idReport INT REFERENCES Report(idReport) ON DELETE CASCADE,
                      categoria VARCHAR(50),
                      punteggio INT,
                      PRIMARY KEY (idReport, categoria)
);

CREATE TABLE ReportPartite (
                               idReport INT REFERENCES Report(idReport) ON DELETE CASCADE,
                               idPartita INT REFERENCES Partita(idPartita),
                               PRIMARY KEY (idReport, idPartita)
);

-- Sezione Statistiche Avanzate
CREATE TABLE Statistiche (
                             Calciatore INT REFERENCES Calciatore(idCalciatore),
                             Partita INT REFERENCES Partita(idPartita),
                             minutiGiocati INT DEFAULT 0,
                             gol INT DEFAULT 0,
                             assist INT DEFAULT 0,
                             xG DECIMAL(5,2),
                             xA DECIMAL(5,2),
                             tiriTotali INT DEFAULT 0,
                             tiriInPorta INT DEFAULT 0,
                             dribblingRiusciti INT DEFAULT 0,
                             tocchiInAreaAvversaria INT DEFAULT 0,
                             contrastiVinti INT DEFAULT 0,
                             duelliAereiVinti INT DEFAULT 0,
                             passaggiChiave INT DEFAULT 0,
                             crossRiusciti INT DEFAULT 0,
                             passaggiRealizzati INT DEFAULT 0,
                             parate INT DEFAULT 0,
                             cleanSheet BOOLEAN DEFAULT FALSE,
                             cartelliniGialli INT DEFAULT 0,
                             cartelliniRossi INT DEFAULT 0,
                             PRIMARY KEY (Calciatore, Partita)
);
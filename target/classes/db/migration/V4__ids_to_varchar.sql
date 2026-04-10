-- =============================================================
-- Koha Simulator — Recreate all tables with VARCHAR(36) primary keys
-- Drops existing tables and recreates them with proper types.
-- Passwords: admin → "admin123" | patrons → "password123"
-- =============================================================

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS checkouts;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS patrons;
DROP TABLE IF EXISTS biblios;
DROP TABLE IF EXISTS branches;

SET FOREIGN_KEY_CHECKS = 1;

-- ── branches ──────────────────────────────────────────────────────────────────
CREATE TABLE branches (
    branch_id      VARCHAR(36)  PRIMARY KEY,
    branchcode     VARCHAR(10)  NOT NULL UNIQUE,
    branchname     VARCHAR(100) NOT NULL,
    branchaddress1 VARCHAR(100),
    branchphone    VARCHAR(20),
    branchemail    VARCHAR(100),
    created_at     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- ── patrons ───────────────────────────────────────────────────────────────────
CREATE TABLE patrons (
    patron_id     VARCHAR(36)  PRIMARY KEY,
    cardnumber    VARCHAR(32)  UNIQUE,
    userid        VARCHAR(75)  NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    firstname     VARCHAR(100),
    surname       VARCHAR(100) NOT NULL,
    email         VARCHAR(100),
    phone         VARCHAR(25),
    address       VARCHAR(100),
    categorycode  VARCHAR(10)  NOT NULL DEFAULT 'PT',
    branchcode    VARCHAR(10)  NOT NULL,
    flags         BIGINT       DEFAULT 0,
    active        BOOLEAN      NOT NULL DEFAULT TRUE,
    date_enrolled DATE,
    expiry_date   DATE,
    created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (branchcode) REFERENCES branches(branchcode)
);

-- ── biblios ───────────────────────────────────────────────────────────────────
CREATE TABLE biblios (
    biblio_id        VARCHAR(36)  PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    author           VARCHAR(255),
    isbn             VARCHAR(30),
    publisher        VARCHAR(255),
    publication_year INT,
    language         VARCHAR(10)  DEFAULT 'es',
    created_at       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- ── items ─────────────────────────────────────────────────────────────────────
CREATE TABLE items (
    item_id    VARCHAR(36)  PRIMARY KEY,
    biblio_id  VARCHAR(36)  NOT NULL,
    barcode    VARCHAR(20)  NOT NULL UNIQUE,
    location   VARCHAR(80),
    callnumber VARCHAR(255),
    itype      VARCHAR(10)  DEFAULT 'BK',
    branchcode VARCHAR(10)  NOT NULL,
    available  BOOLEAN      NOT NULL DEFAULT TRUE,
    damaged    BOOLEAN      NOT NULL DEFAULT FALSE,
    lost       BOOLEAN      NOT NULL DEFAULT FALSE,
    withdrawn  BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (biblio_id)  REFERENCES biblios(biblio_id),
    FOREIGN KEY (branchcode) REFERENCES branches(branchcode)
);

-- ── checkouts ─────────────────────────────────────────────────────────────────
CREATE TABLE checkouts (
    checkout_id    VARCHAR(36)  PRIMARY KEY,
    patron_id      VARCHAR(36)  NOT NULL,
    item_id        VARCHAR(36)  NOT NULL,
    library_id     VARCHAR(10)  NOT NULL,
    checkout_date  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    due_date       TIMESTAMP    NOT NULL,
    return_date    TIMESTAMP,
    renewals_count INT          DEFAULT 0,
    auto_renew     BOOLEAN      DEFAULT FALSE,
    FOREIGN KEY (patron_id)  REFERENCES patrons(patron_id),
    FOREIGN KEY (item_id)    REFERENCES items(item_id),
    FOREIGN KEY (library_id) REFERENCES branches(branchcode)
);

-- =============================================================
-- Sample data — fixed UUIDs for reproducibility
-- =============================================================

-- Branches
INSERT INTO branches (branch_id, branchcode, branchname, branchaddress1, branchemail) VALUES
    ('00000000-0000-0001-0000-000000000001', 'CPL', 'Biblioteca Central APEC',   'Av. Máximo Gómez 72, Santo Domingo', 'biblioteca@apec.edu.do'),
    ('00000000-0000-0001-0000-000000000002', 'BRA', 'Biblioteca Sede Norte',     'Calle El Conde 120, Santiago',        'biblioteca.norte@apec.edu.do');

-- Admin (password: admin123)
INSERT INTO patrons (patron_id, cardnumber, userid, password_hash, firstname, surname, email,
                     categorycode, branchcode, flags, active, date_enrolled, expiry_date)
VALUES ('00000000-0000-0002-0000-000000000001', 'ADM0001', 'admin',
        '$2b$10$8oZxYV1jQXii0SnoBR5Sge9dN4UKbNaqSkmjn2ZMwMK./fuJm5F1G',
        'Admin', 'Sistema', 'admin@apec.edu.do',
        'ST', 'CPL', 1, TRUE, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 2 YEAR));

-- Regular patrons (password: password123)
INSERT INTO patrons (patron_id, cardnumber, userid, password_hash, firstname, surname, email,
                     phone, address, categorycode, branchcode, flags, active, date_enrolled, expiry_date)
VALUES
    ('00000000-0000-0002-0000-000000000002', '2024001', 'jperez',
     '$2b$10$CPBoHtvChJwSwhrBCVO4B.pGIdPLTGNWNQK5bRZOOVsZ/Q0Wg/Y1m',
     'Juan', 'Pérez', 'jperez@estudiante.apec.edu.do',
     '809-555-0101', 'Calle Primera 10, Santo Domingo',
     'PT', 'CPL', 0, TRUE, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 YEAR)),

    ('00000000-0000-0002-0000-000000000003', '2024002', 'mgarcia',
     '$2b$10$CPBoHtvChJwSwhrBCVO4B.pGIdPLTGNWNQK5bRZOOVsZ/Q0Wg/Y1m',
     'María', 'García', 'mgarcia@estudiante.apec.edu.do',
     '809-555-0102', 'Calle Segunda 20, Santo Domingo',
     'PT', 'CPL', 0, TRUE, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 YEAR)),

    ('00000000-0000-0002-0000-000000000004', '2024003', 'cramos',
     '$2b$10$CPBoHtvChJwSwhrBCVO4B.pGIdPLTGNWNQK5bRZOOVsZ/Q0Wg/Y1m',
     'Carlos', 'Ramos', 'cramos@estudiante.apec.edu.do',
     '809-555-0103', 'Calle Tercera 30, Santiago',
     'PT', 'BRA', 0, TRUE, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 YEAR));

-- Bibliographic records
INSERT INTO biblios (biblio_id, title, author, isbn, publisher, publication_year, language) VALUES
    ('00000000-0000-0003-0000-000000000001', 'Introducción a la Programación con Java',
     'Deitel, Paul J.',       '9780137001910', 'Pearson',          2022, 'es'),
    ('00000000-0000-0003-0000-000000000002', 'Bases de Datos: Diseño e Implementación',
     'Connolly, Thomas',      '9780132943260', 'Addison-Wesley',   2021, 'es'),
    ('00000000-0000-0003-0000-000000000003', 'Sistemas Operativos Modernos',
     'Tanenbaum, Andrew S.',  '9780136006633', 'Prentice Hall',    2020, 'es'),
    ('00000000-0000-0003-0000-000000000004', 'Algoritmos y Estructuras de Datos',
     'Cormen, Thomas H.',     '9780262033848', 'MIT Press',        2022, 'en'),
    ('00000000-0000-0003-0000-000000000005', 'Redes de Computadoras',
     'Tanenbaum, Andrew S.',  '9780132126953', 'Prentice Hall',    2021, 'es'),
    ('00000000-0000-0003-0000-000000000006', 'Ingeniería de Software',
     'Sommerville, Ian',     '9780137035151', 'Addison-Wesley',   2020, 'es'),
    ('00000000-0000-0003-0000-000000000007', 'Cálculo Diferencial e Integral',
     'Stewart, James',        '9781285740621', 'Cengage Learning', 2021, 'es'),
    ('00000000-0000-0003-0000-000000000008', 'Estadística para Administración',
     'Berenson, Mark L.',     '9780132936927', 'Pearson',          2022, 'es');

-- Items (2 copies per biblio — barcode = ISBN for first copy, ISBN-2 for second)
INSERT INTO items (item_id, biblio_id, barcode, location, callnumber, itype, branchcode, available) VALUES
    ('00000000-0000-0004-0000-000000000001', '00000000-0000-0003-0000-000000000001', '9780137001910',   'Sala A', 'QA76.73.J38 D45',  'BK', 'CPL', TRUE),
    ('00000000-0000-0004-0000-000000000002', '00000000-0000-0003-0000-000000000001', '9780137001910-2', 'Sala A', 'QA76.73.J38 D45',  'BK', 'CPL', TRUE),
    ('00000000-0000-0004-0000-000000000003', '00000000-0000-0003-0000-000000000002', '9780132943260',   'Sala A', 'QA76.9.D3 C66',    'BK', 'CPL', TRUE),
    ('00000000-0000-0004-0000-000000000004', '00000000-0000-0003-0000-000000000002', '9780132943260-2', 'Sala A', 'QA76.9.D3 C66',    'BK', 'CPL', TRUE),
    ('00000000-0000-0004-0000-000000000005', '00000000-0000-0003-0000-000000000003', '9780136006633',   'Sala B', 'QA76.76.O63 T36',  'BK', 'CPL', TRUE),
    ('00000000-0000-0004-0000-000000000006', '00000000-0000-0003-0000-000000000003', '9780136006633-2', 'Sala A', 'QA76.76.O63 T36',  'BK', 'BRA', TRUE),
    ('00000000-0000-0004-0000-000000000007', '00000000-0000-0003-0000-000000000004', '9780262033848',   'Sala B', 'QA76.9.A43 C67',   'BK', 'CPL', TRUE),
    ('00000000-0000-0004-0000-000000000008', '00000000-0000-0003-0000-000000000004', '9780262033848-2', 'Sala B', 'QA76.9.A43 C67',   'BK', 'CPL', TRUE),
    ('00000000-0000-0004-0000-000000000009', '00000000-0000-0003-0000-000000000005', '9780132126953',   'Sala C', 'TK5105.5 .T36',    'BK', 'CPL', TRUE),
    ('00000000-0000-0004-0000-000000000010', '00000000-0000-0003-0000-000000000005', '9780132126953-2', 'Sala B', 'TK5105.5 .T36',    'BK', 'BRA', TRUE),
    ('00000000-0000-0004-0000-000000000011', '00000000-0000-0003-0000-000000000006', '9780137035151',   'Sala C', 'QA76.758 .S65',    'BK', 'CPL', TRUE),
    ('00000000-0000-0004-0000-000000000012', '00000000-0000-0003-0000-000000000006', '9780137035151-2', 'Sala C', 'QA76.758 .S65',    'BK', 'CPL', TRUE),
    ('00000000-0000-0004-0000-000000000013', '00000000-0000-0003-0000-000000000007', '9781285740621',   'Sala D', 'QA303.2 .S74',     'BK', 'CPL', TRUE),
    ('00000000-0000-0004-0000-000000000014', '00000000-0000-0003-0000-000000000007', '9781285740621-2', 'Sala C', 'QA303.2 .S74',     'BK', 'BRA', TRUE),
    ('00000000-0000-0004-0000-000000000015', '00000000-0000-0003-0000-000000000008', '9780132936927',   'Sala D', 'QA276.12 .B47',    'BK', 'CPL', TRUE),
    ('00000000-0000-0004-0000-000000000016', '00000000-0000-0003-0000-000000000008', '9780132936927-2', 'Sala D', 'QA276.12 .B47',    'BK', 'CPL', TRUE);

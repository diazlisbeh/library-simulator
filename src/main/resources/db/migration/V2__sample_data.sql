-- =============================================================
-- Koha Simulator — Sample Data
-- Passwords are BCrypt hashes of "password123"
-- Admin password is BCrypt hash of "admin123"
-- NOTE: if the DB already has V2 applied, use V3 migration to fix the admin hash.
-- =============================================================

INSERT INTO branches (branchcode, branchname, branchaddress1, branchemail) VALUES
    ('CPL', 'Biblioteca Central APEC',   'Av. Máximo Gómez 72, Santo Domingo', 'biblioteca@apec.edu.do'),
    ('BRA', 'Biblioteca Sede Norte',     'Calle El Conde 120, Santiago',        'biblioteca.norte@apec.edu.do');

-- Admin librarian (flags=1 → superlibrarian)
-- Password: admin123
INSERT INTO patrons (cardnumber, userid, password_hash, firstname, surname, email,
                     categorycode, branchcode, flags, active, date_enrolled, expiry_date)
VALUES ('ADM0001', 'admin',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'Admin', 'Sistema', 'admin@apec.edu.do',
        'ST', 'CPL', 1, TRUE, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 2 YEAR));

-- Regular patrons (password: password123)
INSERT INTO patrons (cardnumber, userid, password_hash, firstname, surname, email,
                     phone, address, categorycode, branchcode, flags, active, date_enrolled, expiry_date)
VALUES
    ('2024001', 'jperez',
     '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW',
     'Juan', 'Pérez', 'jperez@estudiante.apec.edu.do',
     '809-555-0101', 'Calle Primera 10, Santo Domingo',
     'PT', 'CPL', 0, TRUE, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 YEAR)),

    ('2024002', 'mgarcia',
     '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW',
     'María', 'García', 'mgarcia@estudiante.apec.edu.do',
     '809-555-0102', 'Calle Segunda 20, Santo Domingo',
     'PT', 'CPL', 0, TRUE, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 YEAR)),

    ('2024003', 'cramos',
     '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW',
     'Carlos', 'Ramos', 'cramos@estudiante.apec.edu.do',
     '809-555-0103', 'Calle Tercera 30, Santiago',
     'PT', 'BRA', 0, TRUE, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 YEAR));

-- Bibliographic records
INSERT INTO biblios (title, author, isbn, publisher, publication_year, language) VALUES
    ('Introducción a la Programación con Java',
     'Deitel, Paul J.',       '9780137001910', 'Pearson',          2022, 'es'),
    ('Bases de Datos: Diseño e Implementación',
     'Connolly, Thomas',      '9780132943260', 'Addison-Wesley',   2021, 'es'),
    ('Sistemas Operativos Modernos',
     'Tanenbaum, Andrew S.',  '9780136006633', 'Prentice Hall',    2020, 'es'),
    ('Algoritmos y Estructuras de Datos',
     'Cormen, Thomas H.',     '9780262033848', 'MIT Press',        2022, 'en'),
    ('Redes de Computadoras',
     'Tanenbaum, Andrew S.',  '9780132126953', 'Prentice Hall',    2021, 'es'),
    ('Ingeniería de Software',
     'Sommerville, Ian',     '9780137035151', 'Addison-Wesley',   2020, 'es'),
    ('Cálculo Diferencial e Integral',
     'Stewart, James',        '9781285740621', 'Cengage Learning', 2021, 'es'),
    ('Estadística para Administración',
     'Berenson, Mark L.',     '9780132936927', 'Pearson',          2022, 'es');

-- Items (physical copies) — 2 copies per biblio
INSERT INTO items (biblio_id, barcode, location, callnumber, itype, branchcode, available) VALUES
    (1, 'CPL-001-A', 'Sala A', 'QA76.73.J38 D45',  'BK', 'CPL', TRUE),
    (1, 'CPL-001-B', 'Sala A', 'QA76.73.J38 D45',  'BK', 'CPL', TRUE),
    (2, 'CPL-002-A', 'Sala A', 'QA76.9.D3 C66',    'BK', 'CPL', TRUE),
    (2, 'CPL-002-B', 'Sala A', 'QA76.9.D3 C66',    'BK', 'CPL', TRUE),
    (3, 'CPL-003-A', 'Sala B', 'QA76.76.O63 T36',  'BK', 'CPL', TRUE),
    (3, 'BRA-003-A', 'Sala A', 'QA76.76.O63 T36',  'BK', 'BRA', TRUE),
    (4, 'CPL-004-A', 'Sala B', 'QA76.9.A43 C67',   'BK', 'CPL', TRUE),
    (4, 'CPL-004-B', 'Sala B', 'QA76.9.A43 C67',   'BK', 'CPL', TRUE),
    (5, 'CPL-005-A', 'Sala C', 'TK5105.5 .T36',    'BK', 'CPL', TRUE),
    (5, 'BRA-005-A', 'Sala B', 'TK5105.5 .T36',    'BK', 'BRA', TRUE),
    (6, 'CPL-006-A', 'Sala C', 'QA76.758 .S65',    'BK', 'CPL', TRUE),
    (6, 'CPL-006-B', 'Sala C', 'QA76.758 .S65',    'BK', 'CPL', TRUE),
    (7, 'CPL-007-A', 'Sala D', 'QA303.2 .S74',     'BK', 'CPL', TRUE),
    (7, 'BRA-007-A', 'Sala C', 'QA303.2 .S74',     'BK', 'BRA', TRUE),
    (8, 'CPL-008-A', 'Sala D', 'QA276.12 .B47',    'BK', 'CPL', TRUE),
    (8, 'CPL-008-B', 'Sala D', 'QA276.12 .B47',    'BK', 'CPL', TRUE);

-- =============================================================
-- Koha Simulator — Database Schema (SQLite compatible)
-- =============================================================

CREATE TABLE IF NOT EXISTS branches (
    branch_id      INTEGER      PRIMARY KEY AUTOINCREMENT,
    branchcode     VARCHAR(10)  NOT NULL UNIQUE,
    branchname     VARCHAR(100) NOT NULL,
    branchaddress1 VARCHAR(100),
    branchphone    VARCHAR(20),
    branchemail    VARCHAR(100),
    created_at     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS patrons (
    patron_id     INTEGER      PRIMARY KEY AUTOINCREMENT,
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
    updated_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (branchcode) REFERENCES branches(branchcode)
);

CREATE TABLE IF NOT EXISTS biblios (
    biblio_id        INTEGER      PRIMARY KEY AUTOINCREMENT,
    title            VARCHAR(255) NOT NULL,
    author           VARCHAR(255),
    isbn             VARCHAR(30),
    publisher        VARCHAR(255),
    publication_year INTEGER,
    language         VARCHAR(10)  DEFAULT 'es',
    created_at       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS items (
    item_id    INTEGER      PRIMARY KEY AUTOINCREMENT,
    biblio_id  INTEGER      NOT NULL,
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

CREATE TABLE IF NOT EXISTS checkouts (
    checkout_id    INTEGER     PRIMARY KEY AUTOINCREMENT,
    patron_id      INTEGER     NOT NULL,
    item_id        INTEGER     NOT NULL,
    library_id     VARCHAR(10) NOT NULL,
    checkout_date  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    due_date       TIMESTAMP   NOT NULL,
    return_date    TIMESTAMP,
    renewals_count INTEGER     DEFAULT 0,
    auto_renew     BOOLEAN     DEFAULT FALSE,
    FOREIGN KEY (patron_id)  REFERENCES patrons(patron_id),
    FOREIGN KEY (item_id)    REFERENCES items(item_id),
    FOREIGN KEY (library_id) REFERENCES branches(branchcode)
);

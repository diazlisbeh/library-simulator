-- =============================================================
-- Koha Simulator — Add DEFAULT (UUID()) to all primary key columns.
-- Requires MySQL 8.0.13+ (expression defaults).
-- Without this, manual INSERTs without providing an ID will fail
-- because AUTO_INCREMENT was removed in V4.
-- =============================================================

SET FOREIGN_KEY_CHECKS = 0;

ALTER TABLE branches  MODIFY COLUMN branch_id   VARCHAR(36) NOT NULL DEFAULT (UUID());
ALTER TABLE patrons   MODIFY COLUMN patron_id   VARCHAR(36) NOT NULL DEFAULT (UUID());
ALTER TABLE biblios   MODIFY COLUMN biblio_id   VARCHAR(36) NOT NULL DEFAULT (UUID());
ALTER TABLE items     MODIFY COLUMN item_id     VARCHAR(36) NOT NULL DEFAULT (UUID());
ALTER TABLE checkouts MODIFY COLUMN checkout_id VARCHAR(36) NOT NULL DEFAULT (UUID());

SET FOREIGN_KEY_CHECKS = 1;

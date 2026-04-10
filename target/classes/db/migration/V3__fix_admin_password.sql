-- =============================================================
-- Fix: admin password hash in V2 was invalid (did not match any password).
-- Correct BCrypt hash of "admin123" (rounds=10, prefix $2b$).
-- Spring Security's BCryptPasswordEncoder accepts $2b$ hashes.
-- =============================================================

UPDATE patrons
SET password_hash = '$2b$10$8oZxYV1jQXii0SnoBR5Sge9dN4UKbNaqSkmjn2ZMwMK./fuJm5F1G'
WHERE userid = 'admin';

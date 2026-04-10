package com.robotica.kohasimulator.repository;

import com.robotica.kohasimulator.model.Biblio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiblioRepository extends JpaRepository<Biblio, String> {
}

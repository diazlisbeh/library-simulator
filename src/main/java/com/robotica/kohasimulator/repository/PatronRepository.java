package com.robotica.kohasimulator.repository;

import com.robotica.kohasimulator.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatronRepository extends JpaRepository<Patron, String> {
    Optional<Patron> findByUserid(String userid);
    Optional<Patron> findByCardnumber(String cardnumber);
}

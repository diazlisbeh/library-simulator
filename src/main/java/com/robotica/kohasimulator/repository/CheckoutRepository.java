package com.robotica.kohasimulator.repository;

import com.robotica.kohasimulator.model.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CheckoutRepository extends JpaRepository<Checkout, String> {
    List<Checkout> findByPatron_PatronIdAndReturnDateIsNull(String patronId);
    Optional<Checkout> findByItem_ItemIdAndReturnDateIsNull(String itemId);
}

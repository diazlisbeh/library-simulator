package com.robotica.kohasimulator.repository;

import com.robotica.kohasimulator.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, String> {
    Optional<Item> findByBarcode(String barcode);
    List<Item> findByBiblio_BiblioId(String biblioId);
    List<Item> findByBiblio_Isbn(String isbn);
}

package com.robotica.kohasimulator.service;

import com.robotica.kohasimulator.dto.BiblioResponse;
import com.robotica.kohasimulator.dto.ItemResponse;
import com.robotica.kohasimulator.repository.BiblioRepository;
import com.robotica.kohasimulator.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final BiblioRepository biblioRepository;

    public ItemResponse getById(String itemId) {
        return itemRepository.findById(itemId)
            .map(ItemResponse::from)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Item not found: " + itemId));
    }

    public List<ItemResponse> findByBarcode(String barcode) {
        return itemRepository.findByBarcode(barcode)
            .map(i -> List.of(ItemResponse.from(i)))
            .orElse(List.of());
    }

    public List<ItemResponse> findByIsbn(String isbn) {
        return itemRepository.findByBiblio_Isbn(isbn)
            .stream()
            .map(ItemResponse::from)
            .toList();
    }

    public BiblioResponse getBiblioById(String biblioId) {
        return biblioRepository.findById(biblioId)
            .map(BiblioResponse::from)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Biblio not found: " + biblioId));
    }
}

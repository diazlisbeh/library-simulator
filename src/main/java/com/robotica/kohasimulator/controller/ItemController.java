package com.robotica.kohasimulator.controller;

import com.robotica.kohasimulator.dto.ItemResponse;
import com.robotica.kohasimulator.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
@Tag(name = "Items", description = "Item (physical copy) endpoints")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "Search items — filter by ISBN or by barcode (external_id)")
    @GetMapping
    public List<ItemResponse> searchItems(
        @Parameter(description = "ISBN del libro — devuelve todos los ejemplares de esa edición")
        @RequestParam(required = false) String isbn,
        @Parameter(description = "Barcode físico del ejemplar (Koha external_id)")
        @RequestParam(name = "external_id", required = false) String barcode
    ) {
        if (isbn != null) {
            return itemService.findByIsbn(isbn);
        }
        if (barcode != null) {
            return itemService.findByBarcode(barcode);
        }
        return List.of();
    }

    @Operation(summary = "Get item by ID")
    @GetMapping("/{item_id}")
    public ItemResponse getItem(@PathVariable("item_id") String itemId) {
        return itemService.getById(itemId);
    }
}

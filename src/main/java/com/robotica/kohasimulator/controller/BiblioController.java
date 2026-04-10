package com.robotica.kohasimulator.controller;

import com.robotica.kohasimulator.dto.BiblioResponse;
import com.robotica.kohasimulator.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/biblios")
@Tag(name = "Biblios", description = "Bibliographic record endpoints")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class BiblioController {

    private final ItemService itemService;

    @Operation(summary = "Get bibliographic record by ID")
    @GetMapping("/{biblio_id}")
    public BiblioResponse getBiblio(@PathVariable("biblio_id") String biblioId) {
        return itemService.getBiblioById(biblioId);
    }
}

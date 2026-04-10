package com.robotica.kohasimulator.controller;

import com.robotica.kohasimulator.dto.CheckoutResponse;
import com.robotica.kohasimulator.dto.PatronResponse;
import com.robotica.kohasimulator.service.CheckoutService;
import com.robotica.kohasimulator.service.PatronService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patrons")
@Tag(name = "Patrons", description = "Patron (borrower) endpoints")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class PatronController {

    private final PatronService patronService;
    private final CheckoutService checkoutService;

    @Operation(summary = "Search patrons — filter by cardnumber",
               description = "Used by the bridge to resolve a cardnumber (from the student ID card) to a Koha patron_id.")
    @GetMapping
    public List<PatronResponse> searchPatrons(
        @Parameter(description = "Exact cardnumber to search (matricula del estudiante)")
        @RequestParam(required = false) String cardnumber
    ) {
        if (cardnumber != null && !cardnumber.isBlank()) {
            return patronService.findByCardnumber(cardnumber)
                .map(List::of)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No patron found with cardnumber: " + cardnumber));
        }
        return List.of();
    }

    @Operation(summary = "Get patron by ID")
    @GetMapping("/{patron_id}")
    public PatronResponse getPatron(@PathVariable("patron_id") String patronId) {
        return patronService.getById(patronId);
    }

    @Operation(summary = "Get active checkouts for a patron")
    @GetMapping("/{patron_id}/checkouts")
    public List<CheckoutResponse> getPatronCheckouts(@PathVariable("patron_id") String patronId) {
        return checkoutService.getActiveByPatron(patronId);
    }
}

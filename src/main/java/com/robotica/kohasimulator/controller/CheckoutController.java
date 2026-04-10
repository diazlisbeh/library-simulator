package com.robotica.kohasimulator.controller;

import com.robotica.kohasimulator.dto.CheckoutRequest;
import com.robotica.kohasimulator.dto.CheckoutResponse;
import com.robotica.kohasimulator.dto.ReturnRequest;
import com.robotica.kohasimulator.service.CheckoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Checkouts & Returns", description = "Self-checkout and return endpoints")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @Operation(summary = "Create checkout (self-checkout)")
    @PostMapping("/api/v1/checkouts")
    @ResponseStatus(HttpStatus.CREATED)
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest req) {
        return checkoutService.checkout(req);
    }

    @Operation(summary = "Get checkout by ID")
    @GetMapping("/api/v1/checkouts/{checkout_id}")
    public CheckoutResponse getCheckout(@PathVariable("checkout_id") String checkoutId) {
        return checkoutService.getById(checkoutId);
    }

    @Operation(summary = "Return item (by checkout ID)")
    @DeleteMapping("/api/v1/checkouts/{checkout_id}")
    public CheckoutResponse returnItem(@PathVariable("checkout_id") String checkoutId) {
        return checkoutService.returnByCheckoutId(checkoutId);
    }

    @Operation(summary = "Return item by barcode", description = "Koha /api/v1/returns endpoint — accepts barcode in body")
    @PostMapping("/api/v1/returns")
    public CheckoutResponse returnByBarcode(@Valid @RequestBody ReturnRequest req) {
        return checkoutService.returnByBarcode(req.barcode());
    }
}

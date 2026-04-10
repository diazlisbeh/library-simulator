package com.robotica.kohasimulator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.robotica.kohasimulator.model.Checkout;

import java.time.LocalDateTime;

public record CheckoutResponse(
    @JsonProperty("checkout_id")    String checkoutId,
    @JsonProperty("patron_id")      String patronId,
    @JsonProperty("item_id")        String itemId,
    @JsonProperty("biblio_id")      String biblioId,
    @JsonProperty("library_id")     String libraryId,
    @JsonProperty("checkout_date")  LocalDateTime checkoutDate,
    @JsonProperty("due_date")       LocalDateTime dueDate,
    @JsonProperty("return_date")    LocalDateTime returnDate,
    @JsonProperty("renewals_count") Integer renewalsCount,
    @JsonProperty("title")          String title,
    @JsonProperty("author")         String author,
    @JsonProperty("barcode")        String barcode
) {
    public static CheckoutResponse from(Checkout c) {
        return new CheckoutResponse(
            c.getCheckoutId(),
            c.getPatron().getPatronId(),
            c.getItem().getItemId(),
            c.getItem().getBiblio().getBiblioId(),
            c.getLibraryId(),
            c.getCheckoutDate(),
            c.getDueDate(),
            c.getReturnDate(),
            c.getRenewalsCount(),
            c.getItem().getBiblio().getTitle(),
            c.getItem().getBiblio().getAuthor(),
            c.getItem().getBarcode()
        );
    }
}

package com.robotica.kohasimulator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record CheckoutRequest(
    @NotNull @JsonProperty("patron_id")  String patronId,
    @NotNull @JsonProperty("item_id")    String itemId,
    @JsonProperty("library_id")          String libraryId,
    @JsonProperty("due_date")            String dueDate    // ISO-8601, optional
) {}

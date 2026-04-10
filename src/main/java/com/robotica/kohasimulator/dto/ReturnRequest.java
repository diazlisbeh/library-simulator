package com.robotica.kohasimulator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record ReturnRequest(
    @NotBlank @JsonProperty("barcode") String barcode,
    @JsonProperty("library_id")        String libraryId
) {}

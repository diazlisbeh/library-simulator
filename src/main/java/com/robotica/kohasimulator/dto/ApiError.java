package com.robotica.kohasimulator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiError(
    @JsonProperty("error")   String error,
    @JsonProperty("message") String message
) {
    public static ApiError of(String error, String message) {
        return new ApiError(error, message);
    }
}

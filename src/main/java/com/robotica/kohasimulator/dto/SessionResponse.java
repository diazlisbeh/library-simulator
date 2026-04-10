package com.robotica.kohasimulator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

// Matches Koha's POST /api/v1/auth/session response format
public record SessionResponse(
    @JsonProperty("patron_id")    String patronId,
    @JsonProperty("cardnumber")   String cardnumber,
    @JsonProperty("surname")      String surname,
    @JsonProperty("firstname")    String firstname,
    @JsonProperty("email")        String email,
    @JsonProperty("categorycode") String categorycode,
    @JsonProperty("branchcode")   String branchcode,
    @JsonProperty("flags")        Long flags,
    // JWT token — returned in body AND set as CGISESSID cookie
    @JsonProperty("token")        String token
) {}

package com.robotica.kohasimulator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.robotica.kohasimulator.model.Patron;

import java.time.LocalDate;

public record PatronResponse(
    @JsonProperty("patron_id")    String patronId,
    @JsonProperty("cardnumber")   String cardnumber,
    @JsonProperty("userid")       String userid,
    @JsonProperty("firstname")    String firstname,
    @JsonProperty("surname")      String surname,
    @JsonProperty("email")        String email,
    @JsonProperty("phone")        String phone,
    @JsonProperty("address")      String address,
    @JsonProperty("categorycode") String categorycode,
    @JsonProperty("branchcode")   String branchcode,
    @JsonProperty("flags")        Long flags,
    @JsonProperty("active")       Boolean active,
    @JsonProperty("date_enrolled") LocalDate dateEnrolled,
    @JsonProperty("expiry_date")  LocalDate expiryDate
) {
    public static PatronResponse from(Patron p) {
        return new PatronResponse(
            p.getPatronId(), p.getCardnumber(), p.getUserid(),
            p.getFirstname(), p.getSurname(), p.getEmail(),
            p.getPhone(), p.getAddress(), p.getCategorycode(),
            p.getBranchcode(), p.getFlags(), p.getActive(),
            p.getDateEnrolled(), p.getExpiryDate()
        );
    }
}

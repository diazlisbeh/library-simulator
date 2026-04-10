package com.robotica.kohasimulator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.robotica.kohasimulator.model.Biblio;

public record BiblioResponse(
    @JsonProperty("biblio_id")         String biblioId,
    @JsonProperty("title")             String title,
    @JsonProperty("author")            String author,
    @JsonProperty("isbn")              String isbn,
    @JsonProperty("publisher")         String publisher,
    @JsonProperty("publication_year")  Integer publicationYear,
    @JsonProperty("language")          String language
) {
    public static BiblioResponse from(Biblio b) {
        return new BiblioResponse(
            b.getBiblioId(), b.getTitle(), b.getAuthor(), b.getIsbn(),
            b.getPublisher(), b.getPublicationYear(), b.getLanguage()
        );
    }
}

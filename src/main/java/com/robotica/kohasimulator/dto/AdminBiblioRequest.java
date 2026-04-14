package com.robotica.kohasimulator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Cuerpo para crear o actualizar un biblio via los endpoints de administración.
 * En creación: title es obligatorio.
 * En actualización: solo se aplican los campos no nulos.
 */
public record AdminBiblioRequest(
    @JsonProperty("title")            String title,
    @JsonProperty("author")           String author,
    @JsonProperty("isbn")             String isbn,
    @JsonProperty("publisher")        String publisher,
    @JsonProperty("publication_year") Integer publicationYear,
    @JsonProperty("language")         String language
) {}

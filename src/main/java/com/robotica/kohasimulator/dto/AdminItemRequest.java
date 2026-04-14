package com.robotica.kohasimulator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Cuerpo para agregar un ejemplar (item) a un biblio existente.
 * biblioId y barcode son obligatorios.
 */
public record AdminItemRequest(
    @JsonProperty("biblio_id")  String biblioId,
    @JsonProperty("barcode")    String barcode,
    @JsonProperty("branchcode") String branchcode,   // default "CPL"
    @JsonProperty("location")   String location,
    @JsonProperty("callnumber") String callnumber,
    @JsonProperty("itype")      String itype          // default "BK"
) {}

package com.robotica.kohasimulator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.robotica.kohasimulator.model.Item;

public record ItemResponse(
    @JsonProperty("item_id")    String itemId,
    @JsonProperty("biblio_id")  String biblioId,
    @JsonProperty("external_id") String externalId,   // barcode — Koha calls it external_id
    @JsonProperty("barcode")    String barcode,
    @JsonProperty("location")   String location,
    @JsonProperty("callnumber") String callnumber,
    @JsonProperty("itype")      String itype,
    @JsonProperty("branchcode") String branchcode,
    @JsonProperty("available")  Boolean available,
    @JsonProperty("damaged")    Boolean damaged,
    @JsonProperty("lost")       Boolean lost,
    @JsonProperty("withdrawn")  Boolean withdrawn
) {
    public static ItemResponse from(Item i) {
        return new ItemResponse(
            i.getItemId(), i.getBiblio().getBiblioId(),
            i.getBarcode(), i.getBarcode(),
            i.getLocation(), i.getCallnumber(), i.getItype(),
            i.getBranchcode(), i.getAvailable(), i.getDamaged(),
            i.getLost(), i.getWithdrawn()
        );
    }
}

package com.robotica.kohasimulator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Cuerpo para crear o actualizar un patron via los endpoints de administración.
 * En creación: cardnumber, userid, password y surname son obligatorios.
 * En actualización: solo se aplican los campos no nulos.
 */
public record AdminPatronRequest(
    @JsonProperty("cardnumber")   String cardnumber,
    @JsonProperty("userid")       String userid,
    @JsonProperty("password")     String password,      // texto plano — se hashea en el servicio
    @JsonProperty("firstname")    String firstname,
    @JsonProperty("surname")      String surname,
    @JsonProperty("email")        String email,
    @JsonProperty("phone")        String phone,
    @JsonProperty("address")      String address,
    @JsonProperty("categorycode") String categorycode,  // default "PT"
    @JsonProperty("branchcode")   String branchcode,    // default "CPL"
    @JsonProperty("flags")        Long flags,
    @JsonProperty("active")       Boolean active
) {}

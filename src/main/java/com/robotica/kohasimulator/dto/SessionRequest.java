package com.robotica.kohasimulator.dto;

import jakarta.validation.constraints.NotBlank;

public record SessionRequest(
    @NotBlank String userid,
    @NotBlank String password
) {}

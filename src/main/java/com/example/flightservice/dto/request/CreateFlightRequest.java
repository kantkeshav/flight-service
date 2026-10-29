package com.example.flightservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateFlightRequest(
    @NotBlank String flightNumber,
    @NotBlank String origin,
    @NotBlank String destination,
    @Min(0) int availableSeats
) {}

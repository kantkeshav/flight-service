package com.example.flightservice.dto.request;

import jakarta.validation.constraints.Min;

public record ReserveSeatsRequest(
    @Min(1) int numberOfSeats
) {}

package com.example.flightservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class FlightReservation {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long flightId;
    private int numberOfSeats;
    private String status;

    @Column(unique = true) // The unique constraint ensures that two reservation records cannot normally be stored with the same non-null key.
    private String idempotencyKey;

    protected FlightReservation() {}

    public FlightReservation(Long flightId, int numberOfSeats, String idempotencyKey) {
        this.flightId = flightId;
        this.numberOfSeats = numberOfSeats;
        this.idempotencyKey = idempotencyKey;
        this.status = "RESERVED";
    }

    public Long getId() {
        return id;
    }

    public Long getFlightId() {
        return flightId;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public String getStatus() {
        return status;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void release() {
        this.status = "RELEASED";
    }


}

/*
Why store a reservation record?
- Each reservation gets a unique ID.
- We know which flight and how many seats were reserved.
- Its status tells us whether it is still active or has been released
 */

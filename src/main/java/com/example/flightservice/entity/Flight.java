package com.example.flightservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name="flights")
public class Flight {

    @Id     //identifies the primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY)     //lets the database generate the ID when a new flight is inserted.
    private  Long id;

    private  String flightNumber;
    private  String origin;
    private  String destination;
    private  int availableSeats;

    public Flight(){}
    
    public Flight(String flightNumber, String origin, String destination, int availableSeats) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.availableSeats = availableSeats;
    }

    // Getter
    public Long getId() {return id;}
    public String getFlightNumber() {return flightNumber;}
    public String getOrigin() {return origin;}
    public String getDestination() {return destination;}
    public int getAvailableSeats() {return availableSeats;}

    @Override
    public String toString() {
        return "Flight [id=" + id + ", flightNumber=" + flightNumber + ", origin=" + origin + ", destination="
                + destination + ", availableSeats=" + availableSeats + "]";
    }

}

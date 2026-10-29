package com.example.flightservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.flightservice.entity.Flight;

public interface FlightRepository extends JpaRepository<Flight,Long>{

    Optional<Flight> findById(Long id);

    @Modifying
    @Query("""
        UPDATE Flight f
        SET f.availableSeats = f.availableSeats - :seats
        WHERE f.id = :flightId
        AND f.availableSeats >= :seats
        """)
    int reserveSeats(
        @Param("flightId") Long flightId,
        @Param("seats") int seats
    );

    // This method restores the seats. We'll call it only when a reservation is successfully changed from RESERVED to RELEASED.
    @Modifying
    @Query("""
        UPDATE Flight f
        SET f.availableSeats = f.availableSeats + :seats
        WHERE f.id = :flightId
        """)
    int releaseSeats(
            @Param("flightId") Long flightId,
            @Param("seats") int seats
    );

}

/*
By extending JpaRepository, Spring Data JPA provides common database operations such as:

findAll() — retrieve all flights.
findById(id) — retrieve a flight by ID.
save(flight) — insert or update a flight.
deleteById(id) — delete a flight.

*/

/* 
How it works (int reserveSeats):

If enough seats exist, the database deducts them and returns 1.
If the flight doesn't exist or has insufficient seats, it returns 0.
The database checks availability and performs the deduction as one operation, preventing concurrent requests from overselling seats through this operation.

*/

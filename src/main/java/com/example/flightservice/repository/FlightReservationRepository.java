package com.example.flightservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.flightservice.entity.FlightReservation;

import jakarta.persistence.LockModeType;

public interface FlightReservationRepository extends JpaRepository<FlightReservation,Long> {

    // If two requests try to release the same reservation, the database lock makes them process it one at a time.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM FlightReservation r WHERE r.id = :id")
    Optional<FlightReservation> findByIdForUpdate(@Param("id") Long id);

    Optional<FlightReservation> findByIdempotencyKey(String idempotencyKey);

}

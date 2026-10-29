package com.example.flightservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.flightservice.entity.FlightReservation;
import com.example.flightservice.repository.FlightRepository;
import com.example.flightservice.repository.FlightReservationRepository;

import jakarta.transaction.Transactional;

@Service 
public class ReservationService {

    private final FlightRepository flightRepository;
    private final FlightReservationRepository reservationRepository;

    public ReservationService(FlightRepository flightRepository,FlightReservationRepository reservationRepository) {
        this.flightRepository = flightRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public FlightReservation reserveSeats(
            Long flightId,
            int seats,
            String idempotencyKey) {

        var existing = reservationRepository
                .findByIdempotencyKey(idempotencyKey);

        if (existing.isPresent()) {
            FlightReservation reservation = existing.get();

            if (!reservation.getFlightId().equals(flightId)
                    || reservation.getNumberOfSeats() != seats) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Idempotency key reused with different request");
            }

            return reservation;
        }

        int updated = flightRepository.reserveSeats(flightId, seats);

        if (updated == 0) {
            return null;
        }

        return reservationRepository.save(
                new FlightReservation(flightId, seats, idempotencyKey));
    }

    // The method checks the status while holding the lock. It restores seats and changes the status within the same transaction.
    @Transactional
    public boolean releaseReservation(Long reservationId) {

        FlightReservation reservation = reservationRepository
                .findByIdForUpdate(reservationId)
                .orElse(null);

        if (reservation == null) {
            return false;
        }

        if ("RELEASED".equals(reservation.getStatus())) {
            return true;
        }

        int updated = flightRepository.releaseSeats(
                reservation.getFlightId(),
                reservation.getNumberOfSeats());

        if (updated == 0) {
            throw new IllegalStateException(
                    "Flight not found while releasing reservation");
        }

        reservation.release();
        return true;
    }

}

/*
Why @Transactional?

If the seat deduction succeeds but saving the reservation fails,
Spring rolls back the seat deduction as well. Both operations use the same Flight Service database and transaction.
 */

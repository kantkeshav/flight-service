package com.example.flightservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.flightservice.dto.request.CreateFlightRequest;
import com.example.flightservice.dto.request.ReserveSeatsRequest;
import com.example.flightservice.entity.Flight;
import com.example.flightservice.entity.FlightReservation;
import com.example.flightservice.service.FlightService;
import com.example.flightservice.service.ReservationService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;



@RestController 
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;
    private final ReservationService reservationService;

    public FlightController(FlightService flightService, ReservationService reservationService) {
        this.flightService = flightService;
        this.reservationService = reservationService;
    }

    @GetMapping("/health")
    public String health(){
        return "Flight Service is running";
    }

    @GetMapping("/all")
    public List<Flight> getAllFlights() {
        return flightService.getAllFlights();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        return flightService.getFlightById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Flight> createFlight(@Valid @RequestBody CreateFlightRequest request){
        Flight flight = flightService.createFlight(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(flight);
    }

    @PostMapping("/{id}/reservations")
    public ResponseEntity<?> reserveSeats(@PathVariable Long id, @RequestHeader("Idempotency-Key") String idempotencyKey, @Valid @RequestBody ReserveSeatsRequest request) {

    FlightReservation reservation = reservationService.reserveSeats(id, request.numberOfSeats(), idempotencyKey);

    if (reservation == null) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
         .body("Flight not found or insufficient seats");
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @PostMapping("/reservations/{reservationId}/release")
    public ResponseEntity<String> releaseReservation(@PathVariable Long reservationId) {

        boolean released = reservationService.releaseReservation(reservationId);

        if (!released) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Reservation not found or already released");
        }

        return ResponseEntity.ok("Reservation released successfully");
    }

}

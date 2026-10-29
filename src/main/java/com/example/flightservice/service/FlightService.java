package com.example.flightservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.flightservice.dto.request.CreateFlightRequest;
import com.example.flightservice.entity.Flight;
import com.example.flightservice.repository.FlightRepository;

import jakarta.transaction.Transactional;

@Service 
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository){
        this.flightRepository = flightRepository;
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Optional<Flight> getFlightById(Long id) {
        return flightRepository.findById(id);
    }

    public Flight createFlight(CreateFlightRequest request) {
    Flight flight = new Flight(
        request.flightNumber(),
        request.origin(),
        request.destination(),
        request.availableSeats()
    );

    return flightRepository.save(flight);
    }

    @Transactional
    public boolean reserveSeats(Long flightId, int seats) {
        return flightRepository.reserveSeats(flightId, seats) == 1;
    }
    
    

}

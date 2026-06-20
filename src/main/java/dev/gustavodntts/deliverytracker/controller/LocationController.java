package dev.gustavodntts.deliverytracker.controller;

import dev.gustavodntts.deliverytracker.dto.LocationRequest;
import dev.gustavodntts.deliverytracker.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void updateLocation(@RequestBody LocationRequest locationRequest) {
        locationService.updateLocation(locationRequest.orderId(), locationRequest.lat(), locationRequest.lng());
    }
}

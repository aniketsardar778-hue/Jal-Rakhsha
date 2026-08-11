package com.demo.jalrakhsa.controller;

import com.demo.jalrakhsa.entity.LocationData;
import com.demo.jalrakhsa.repository.LocationDataRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/locations")
@CrossOrigin(origins = "*")
public class LocationController {
    private final LocationDataRepo locationDataRepo;

    public LocationController(LocationDataRepo locationDataRepo) { this.locationDataRepo = locationDataRepo; }

    @GetMapping
    public ResponseEntity<List<LocationData>> getAllLocations() {
        return ResponseEntity.ok(locationDataRepo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationData> getLocationById(@PathVariable Long id) {
        return ResponseEntity.ok(locationDataRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found")));
    }

    @GetMapping("/search")
    public ResponseEntity<List<LocationData>> searchLocation(@RequestParam String name) {
        return ResponseEntity.ok(locationDataRepo.findByLocationNameContainingIgnoreCase(name));
    }

    // Admin/data-entry endpoint: environmental data belongs in LocationData, not in Assessment.
    @PostMapping
    public ResponseEntity<LocationData> createLocation(@RequestBody LocationData location) {
        location.setId(null);
        return ResponseEntity.ok(locationDataRepo.save(location));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationData> updateLocation(@PathVariable Long id, @RequestBody LocationData input) {
        LocationData existing = locationDataRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        input.setId(existing.getId());
        return ResponseEntity.ok(locationDataRepo.save(input));
    }

    // GPS uses this endpoint to choose the nearest STORED location/environment record; frontend uses its locationName.
    @GetMapping("/nearest")
    public ResponseEntity<LocationData> nearest(@RequestParam double latitude,
                                                @RequestParam double longitude) {
        List<LocationData> locations = locationDataRepo.findAll();
        LocationData nearest = locations.stream()
                .filter(l -> l.getLatitude() != null && l.getLongitude() != null)
                .min(Comparator.comparingDouble(l -> distanceKm(latitude, longitude, l.getLatitude(), l.getLongitude())))
                .orElseThrow(() -> new IllegalArgumentException("No LocationData with coordinates exists in the database"));
        return ResponseEntity.ok(nearest);
    }

    private double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        double r = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return r * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}

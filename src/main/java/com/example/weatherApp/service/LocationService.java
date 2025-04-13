package com.example.weatherApp.service;

import com.example.weatherApp.model.Location;
import com.example.weatherApp.repositories.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public void save(Location location) {
        locationRepository.save(location);
    }

    public Location findById(Long id) {
        return locationRepository.findById(id);
    }

    public void delete(Long locationId) {
        locationRepository.delete(locationId);
    }

    public List<Location> findLocationsByUserId(Long userId) {
        return locationRepository.findLocationsByUserId(userId);
    }


}

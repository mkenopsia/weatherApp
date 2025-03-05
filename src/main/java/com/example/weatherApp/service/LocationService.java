package com.example.weatherApp.service;

import com.example.weatherApp.model.Location;
import com.example.weatherApp.repositories.LocationRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    public void save(Location location) {
        locationRepository.save(location);
    }

    public Location findById(Long id) {
        return locationRepository.findById(id);
    }

    public List<Location> findLocationsByUserId(Long userId) {
        return locationRepository.findLocationsByUserId(userId);
    }
}

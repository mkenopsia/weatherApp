package com.example.weatherApp.service;

import com.example.weatherApp.model.Location;
import com.example.weatherApp.service.payload.ListSearchLocation;
import com.example.weatherApp.service.payload.SavedLocationInfo;
import com.example.weatherApp.service.payload.SearchLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OpenWeatherApiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final LocationService locationService;

    private final String apiKey;

    public String getSearchRequest(String city) {
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/find?q=%s&appid=%s&lang=ru",
                city,
                apiKey
        );
        return restTemplate.getForObject(url, String.class);
    }

    public List<SearchLocation> getLocations(String city) throws JsonProcessingException {
        return objectMapper.readValue(getSearchRequest(city), ListSearchLocation.class).getList();
    }

    public List<SavedLocationInfo> getSavedLocationsWeatherInfo(Long userId) throws JsonProcessingException {
        List<Location> savedLocations = locationService.findLocationsByUserId(userId);
        List<SavedLocationInfo> locationsWeatherInfo = new ArrayList<>();
        for(Location location : savedLocations) {
            String url = String.format(
                    "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=metric",
                    location.getLatitude(),
                    location.getLongitude(),
                    apiKey
            );
            SavedLocationInfo currLocation;
            String getRequest = restTemplate.getForObject(url, String.class);
            currLocation = objectMapper.readValue(getRequest, SavedLocationInfo.class);
            currLocation.setId(location.getId());
            locationsWeatherInfo.add(currLocation);
        }
        return locationsWeatherInfo;
    }
}

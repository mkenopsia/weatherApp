package com.example.weatherApp.service;

import com.example.weatherApp.service.payload.ListSearchLocation;
import com.example.weatherApp.model.Location;
import com.example.weatherApp.service.payload.SavedLocationInfo;
import com.example.weatherApp.service.payload.SearchLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenWeatherApiService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LocationService locationService;

    public String getSearchRequest(String city) {
        String url = String.format(
                "https://ru.api.openweathermap.org/data/2.5/find?q=%s&appid=839477f9e96302537b9c73cfaf97c58e&lang=ru",
                city
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
                    "https://ru.api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=metric",
                    location.getLatitude(), location.getLongitude(),
                    "839477f9e96302537b9c73cfaf97c58e"
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

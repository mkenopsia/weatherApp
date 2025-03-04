package com.example.weatherApp.service;

import com.example.weatherApp.model.ApiResponse;
import com.example.weatherApp.model.Location;
import com.example.weatherApp.model.LocationInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OpenWeatherApiService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public String getRequest(String city) {
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/find?q=%s&appid=839477f9e96302537b9c73cfaf97c58e&lang=ru",
                city
        );
        String resp = restTemplate.getForObject(url, String.class);
        return resp;
    }

    public ApiResponse getLocations(String city) throws JsonProcessingException {
        return objectMapper.readValue(getRequest(city), ApiResponse.class);
    }

}

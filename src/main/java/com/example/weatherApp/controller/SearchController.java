package com.example.weatherApp.controller;

import com.example.weatherApp.service.OpenWeatherApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @Autowired
    OpenWeatherApiService openWeatherApiService;

    @GetMapping("/search")
    public String search(@RequestParam("location") String location, Model model) throws JsonProcessingException {
        model.addAttribute("response", openWeatherApiService.getLocations(location));
        return "searchResult";
    }
}

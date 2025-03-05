package com.example.weatherApp.controller;

import com.example.weatherApp.model.Location;
import com.example.weatherApp.repositories.LocationRepository;
import com.example.weatherApp.repositories.UserRepository;
import com.example.weatherApp.service.CookiesService;
import com.example.weatherApp.service.OpenWeatherApiService;
import com.example.weatherApp.service.SessionManagerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/homepage")
public class HomepageController {
    @Autowired
    CookiesService cookiesService;
    @Autowired
    SessionManagerService sessionManagerService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    OpenWeatherApiService openWeatherApiService;

    @GetMapping
    public String index(Model model, HttpServletRequest request) throws JsonProcessingException {
        if(!sessionManagerService.checkIfSessionValid(request)) {
            return "redirect:/login";
        }
        Long userId = cookiesService.getUserId(request.getCookies());
        model.addAttribute("username", userRepository.findById(userId).getName());
        model.addAttribute("savedLocations", openWeatherApiService.getSavedLocationsWeatherInfo(userId));
        return "homepage";
    }

    @PostMapping("/add")
    public String addLocation(@ModelAttribute Location location, Model model, HttpServletRequest request) {
        if(!sessionManagerService.checkIfSessionValid(request)) {
            return "redirect:/login";
        }
        location.setUserId(cookiesService.getUserId(request.getCookies()));
        locationRepository.save(location);
        return "redirect:/homepage";
    }

    @PostMapping("/delete")
    public String deleteLocation(@RequestParam("locationId") Long locationId, HttpServletRequest request) {
        if(!sessionManagerService.checkIfSessionValid(request)) {
            return "redirect:/login";
        }
        locationRepository.delete(locationId);
        return "redirect:/homepage";
    }
}

package com.example.weatherApp.controller;

import com.example.weatherApp.model.Location;
import com.example.weatherApp.repositories.LocationRepository;
import com.example.weatherApp.repositories.UserRepository;
import com.example.weatherApp.service.*;
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
    private CookiesService cookiesService;
    @Autowired
    private SessionManagerService sessionManagerService;
    @Autowired
    private UserService userService;
    @Autowired
    private OpenWeatherApiService openWeatherApiService;
    @Autowired
    private LocationService locationService;

    @GetMapping
    public String index(Model model, HttpServletRequest request) throws JsonProcessingException {
        if(!sessionManagerService.checkIfSessionValid(request)) {
            return "redirect:/login";
        }
        Long userId = cookiesService.getUserId(request.getCookies());
        model.addAttribute("username", userService.findUserById(userId).getName());
        model.addAttribute("savedLocations", openWeatherApiService.getSavedLocationsWeatherInfo(userId));
        return "homepage";
    }

    @PostMapping("/add")
    public String addLocation(@ModelAttribute Location location, HttpServletRequest request) {
        if(!sessionManagerService.checkIfSessionValid(request)) {
            return "redirect:/login";
        }
        location.setUser(userService.findUserById(cookiesService.getUserId(request.getCookies())));
        locationService.save(location);
        return "redirect:/homepage";
    }

    @PostMapping("/delete")
    public String deleteLocation(@RequestParam("locationId") Long locationId,
                                 HttpServletRequest request, Model model) {
        if(!sessionManagerService.checkIfSessionValid(request)) {
            return "redirect:/login";
        }

        Long userId = cookiesService.getUserId(request.getCookies());
        Location location = locationService.findById(locationId);
        if(location == null) {
            model.addAttribute("message", "Nothing to delete");
            return "redirect:/error";
        }
        if(location.getUser().getId().equals(userId)) {
            locationService.delete(locationId);
        }
        return "redirect:/homepage";
    }
}

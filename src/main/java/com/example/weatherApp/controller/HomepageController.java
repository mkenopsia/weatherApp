package com.example.weatherApp.controller;

import com.example.weatherApp.model.Location;
import com.example.weatherApp.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/homepage")
@RequiredArgsConstructor
public class HomepageController {
    private final CookiesService cookiesService;

    private final SessionManagerService sessionManagerService;

    private final UserService userService;

    private final OpenWeatherApiService openWeatherApiService;

    private final LocationService locationService;

    @GetMapping
    public String index(Model model, HttpServletRequest request) throws JsonProcessingException {
        if(!this.sessionManagerService.checkIfSessionValid(request)) {
            return "redirect:/login";
        }
        Long userId = this.cookiesService.getUserId(request.getCookies());
        model.addAttribute("username", this.userService.findUserById(userId).getName());
        model.addAttribute("savedLocations", this.openWeatherApiService.getSavedLocationsWeatherInfo(userId));
        return "homepage";
    }

    @PostMapping("/add")
    public String addLocation(@ModelAttribute Location location, HttpServletRequest request) {
        if(!this.sessionManagerService.checkIfSessionValid(request)) {
            return "redirect:/login";
        }
        location.setUser(userService.findUserById(cookiesService.getUserId(request.getCookies())));
        locationService.save(location);
        return "redirect:/homepage";
    }

    @PostMapping("/delete")
    public String deleteLocation(@RequestParam("locationId") Long locationId,
                                 HttpServletRequest request, Model model) {
        if(!this.sessionManagerService.checkIfSessionValid(request)) {
            return "redirect:/login";
        }

        Long userId = this.cookiesService.getUserId(request.getCookies());
        Location location = this.locationService.findById(locationId);
        if(location == null) {
            model.addAttribute("message", "Nothing to delete");
            return "redirect:/error";
        }
        if(location.getUser().getId().equals(userId)) {
            this.locationService.delete(locationId);
        }
        return "redirect:/homepage";
    }
}

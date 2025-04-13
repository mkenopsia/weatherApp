package com.example.weatherApp.controller;

import com.example.weatherApp.model.Location;
import com.example.weatherApp.model.User;
import com.example.weatherApp.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/homepage")
@RequiredArgsConstructor
public class HomepageController {

    private final UserService userService;

    private final OpenWeatherApiService openWeatherApiService;

    private final LocationService locationService;

    private final AuthService authService;

//    @ModelAttribute("user")
//    public User getUser(HttpServletRequest request) {
//        return this.userService.getUserFromCookies(request);
//    }

    @GetMapping
    public String index(Model model, HttpServletRequest request) throws JsonProcessingException {
        if(!this.authService.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        User user = this.userService.getUserFromCookies(request);
        model.addAttribute("username", user.getName());
        model.addAttribute("savedLocations", this.openWeatherApiService.getSavedLocationsWeatherInfo(user.getId()));
        return "homepage";
    }

    @PostMapping("/add")
    public String addLocation(@ModelAttribute("user") User user, @ModelAttribute Location location, HttpServletRequest request) {
        if(!this.authService.isUserLoggedIn(request)) {
            return "redirect:/login";
        }
        // TODO: вынести из аттрибута юзера в отдельный метод с добавлением локации
        // будет добавляться юзер в локацию и она будет сохраняться
        location.setUser(user);
        locationService.save(location);
        return "redirect:/homepage";
    }

    @PostMapping("/delete")
    public String deleteLocation(@ModelAttribute("user") User user,
                                 @RequestParam("locationId") Long locationId,
                                 HttpServletRequest request,
                                 Model model) {
        if(!this.authService.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        // TODO: вынести логику в сервис
        Location location = this.locationService.findById(locationId);
        if(location == null) {
            model.addAttribute("message", "Nothing to delete");
            return "redirect:/error";
        }
        if(location.getUser().getId().equals(user.getId())) {
            this.locationService.delete(locationId);
        }
        return "redirect:/homepage";
    }
}

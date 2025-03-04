package com.example.weatherApp.controller;

import com.example.weatherApp.model.Location;
import com.example.weatherApp.repositories.LocationRepository;
import com.example.weatherApp.repositories.UserRepository;
import com.example.weatherApp.service.CookiesService;
import com.example.weatherApp.service.SessionManagerService;
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

    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        if(!sessionManagerService.checkIfSessionValid(request)) {
            return "redirect:/login";
        }

        Long userId = cookiesService.getUserId(request.getCookies());
        model.addAttribute("username",userRepository.findById(userId).getName());
        //TODO передать в модель список сохраненных локаций
        return "homepage";
    }

    @PostMapping("/add")
    public String addLocation(@ModelAttribute Location location, Model model, HttpServletRequest request) {
        location.setUserId(cookiesService.getUserId(request.getCookies()));
        locationRepository.save(location);
        return "homepage";
    }
}

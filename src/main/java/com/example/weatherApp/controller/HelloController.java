package com.example.weatherApp.controller;

import com.example.weatherApp.service.CookiesService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @Autowired
    CookiesService cookiesService;

    @GetMapping("/hello")
    public String hello(HttpServletRequest request) {
        Long userId = cookiesService.getUserId(request.getCookies());
        if(userId == null) {
            return "redirect:/login";
        }
        return "hello";
    }
}

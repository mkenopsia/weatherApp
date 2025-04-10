package com.example.weatherApp.controller;

import com.example.weatherApp.model.Session;
import com.example.weatherApp.model.User;
import com.example.weatherApp.service.CookiesService;
import com.example.weatherApp.service.SessionManagerService;
import com.example.weatherApp.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CookiesService cookiesService;
    @Autowired
    private SessionManagerService sessionManagerService;

    @GetMapping("/registration")
    public String registration(HttpServletRequest request) {
        if(sessionManagerService.checkIfSessionValid(request)) {
            return "redirect:/homepage";
        }
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute User user, @RequestParam("confirm_password") String confirm, Model model) {
        User currUser = userService.findUserByName(user.getName());

        if(currUser != null) {
            model.addAttribute("message", "User with this name is already exists");
            return "registration";
        }

        if(!user.getPassword().equals(confirm)) {
            model.addAttribute("message", "Passwords must be similar");
            return "registration";
        }

        user.setRole("USER");
        userService.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginPage(HttpServletRequest request) {
        if(sessionManagerService.checkIfSessionValid(request)) {
            return "redirect:/homepage";
        }
        return "login";
    }

    @PostMapping("/login")
    public String logIn(@ModelAttribute User user, Model model, HttpServletResponse response) {
        User currUser = userService.findUserByName(user.getName());

        if(currUser == null) {
            model.addAttribute("message", "User with this name isn't exist");
            return "login";
        }

        if(!userService.checkPassword(user.getPassword(), currUser.getPassword())) {
            model.addAttribute("message", "Wrong password");
            return "login";
        }

        Session session = sessionManagerService.getSessionByUserId(currUser.getId());
        UUID sessionId = (session == null) ? null : session.getId(); // если для юзера нет сессии в таблице - создаём
        if(sessionId == null || sessionManagerService.isExpired(session)) {
            sessionId = sessionManagerService.createSession(currUser);
        }

        response.addCookie(cookiesService.getNewCookie(sessionId));

        return "redirect:/homepage";
    }

    @PostMapping("/logout")
    public String logout(Model model, HttpServletRequest request, HttpServletResponse response) {
        if(!sessionManagerService.checkIfSessionValid(request)) {
            return "redirect:/login";
        }
        response.addCookie(cookiesService.deleteCookie(request.getCookies()));
        return "redirect:/login";
    }
}

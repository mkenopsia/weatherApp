package com.example.weatherApp.controller;

import com.example.weatherApp.controller.payload.UserPayload;
import com.example.weatherApp.exceptions.PasswordException;
import com.example.weatherApp.exceptions.UserException;
import com.example.weatherApp.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Validated
public class UserController {

    private final AuthService authService;

    @GetMapping("/registration")
    public String registration(HttpServletRequest request) {
        if(this.authService.isUserLoggedIn(request)) {
            return "redirect:/homepage";
        }
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@Valid @ModelAttribute UserPayload user,
                               BindingResult bindingResult,
                               @RequestParam("confirm_password") String confirm,
                               Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "registration";
        }

        try {
            authService.registerUser(user, confirm);
            return "redirect:/login";
        } catch (UserException | PasswordException e) {
            model.addAttribute("message", e.getMessage());
            return "registration";
        }
    }

    @GetMapping("/login")
    public String getLoginPage(HttpServletRequest request) {
        if(this.authService.isUserLoggedIn(request)) {
            return "redirect:/homepage";
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@Valid @ModelAttribute UserPayload userPayload,
                            BindingResult bindingResult,
                            Model model,
                            HttpServletResponse response) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "login";
        }

        try {
            this.authService.loginUser(userPayload, response);
            return "redirect:/homepage";
        } catch (UserException | PasswordException e) {
            model.addAttribute("message", e.getMessage());
            return "login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        if(!this.authService.isUserLoggedIn(request)) {
            return "redirect:/login";
        }

        authService.logoutUser(request, response);
        return "redirect:/login";
    }
}

package com.example.weatherApp.service;

import com.example.weatherApp.controller.payload.UserPayload;
import com.example.weatherApp.exceptions.PasswordException;
import com.example.weatherApp.exceptions.UserException;
import com.example.weatherApp.model.Session;
import com.example.weatherApp.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserService userService;
    private final CookiesService cookiesService;
    private final SessionManagerService sessionManagerService;

    public void registerUser(UserPayload userPayload, String confirmPassword) throws UserException, PasswordException {
        if(this.userService.findUserByName(userPayload.name()).isPresent()) {
            throw new UserException("User with this name already exists");
        }
        if(!userPayload.password().equals(confirmPassword)) {
            throw new PasswordException("Passwords must be similar");
        }

        User user = new User();
        user.setName(userPayload.name());
        user.setPassword(userPayload.password());
        user.setRole("USER");
        this.userService.save(user);
    }

    public void loginUser(UserPayload userPayload, HttpServletResponse response) throws UserException, PasswordException {
        User user = this.userService.findUserByName(userPayload.name())
                .orElseThrow(() -> new UserException("User with this name doesn't exist"));

        if(!this.userService.checkPassword(userPayload.password(), user.getPassword())) {
            throw new PasswordException("Incorrect password");
        }

        Session session = this.sessionManagerService.getSessionByUserId(user.getId());
        UUID sessionId = this.sessionManagerService.getSessionId(session, user);

        response.addCookie(cookiesService.getNewCookie(sessionId));
    }

    public void logoutUser(HttpServletRequest request, HttpServletResponse response) {
        response.addCookie(cookiesService.deleteCookie(request.getCookies()));
    }

    public boolean isUserLoggedIn(HttpServletRequest request) {
        return this.sessionManagerService.checkIfSessionValid(request);
    }

}

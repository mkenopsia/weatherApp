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

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserService userService;
    private final CookiesService cookiesService;
    private final SessionManagerService sessionManagerService;

    public void registerUser(UserPayload userPayload, String confirmPassword) throws UserException, PasswordException {
        if(this.userService.findUserByName(userPayload.name()).isPresent()) {
            throw new UserException("Пользователь с таким именем уже существует");
        }
        if(!userPayload.password().equals(confirmPassword)) {
            throw new PasswordException("Введённые пароли не совпадают");
        }

        User user = new User();
        user.setName(userPayload.name());
        user.setPassword(userPayload.password());
        user.setRole("USER");
        this.userService.save(user);
    }

    public void loginUser(UserPayload userPayload, HttpServletResponse response) throws UserException, PasswordException {
        Optional<User> currUser = this.userService.findUserByName(userPayload.name());

        if(currUser.isEmpty()) {
            throw new UserException("Пользователь с таким именем не найден");
        }

        if(!this.userService.checkPassword(userPayload.password(), currUser.get().getPassword())) {
            throw new PasswordException("Неверный пароль");
        }

        Session session = this.sessionManagerService.getSessionByUserId(currUser.get().getId());
        UUID sessionId = this.sessionManagerService.getSessionId(session, currUser.get());

        response.addCookie(cookiesService.getNewCookie(sessionId));
    }

    public void logoutUser(HttpServletRequest request, HttpServletResponse response) {
        response.addCookie(cookiesService.deleteCookie(request.getCookies()));
    }

    public boolean isUserLoggedIn(HttpServletRequest request) {
        return this.sessionManagerService.checkIfSessionValid(request);
    }

}

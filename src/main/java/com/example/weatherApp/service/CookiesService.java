package com.example.weatherApp.service;

import com.example.weatherApp.model.Session;
import com.example.weatherApp.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CookiesService {

    @Autowired
    SessionManagerService sessionManagerService;

    public Long getUserId(Cookie[] cookies) {
        if (cookies == null)
            return null;
        String sessionId = null;
        for (Cookie cookie : cookies) {
            if ("sessionId".equals(cookie.getName())) {
                sessionId = cookie.getValue();
                break;
            }
        }

        return sessionManagerService.getUserId(UUID.fromString(sessionId));
    }

    public UUID getSessionId(Cookie[] cookies) {
        if (cookies == null)
            return null;
        String sessionId = null;
        for (Cookie cookie : cookies) {
            if ("sessionId".equals(cookie.getName())) {
                sessionId = cookie.getValue();
                break;
            }
        }

        return (sessionId != null) ? UUID.fromString(sessionId) : null;
    }

    public Cookie getIdentificatorCookie(Cookie[] cookies) {
        if (cookies == null)
            return null;
        for (Cookie cookie : cookies) {
            if ("sessionId".equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    public void deleteCookie(Cookie cookie) {
        cookie.setMaxAge(0);
    }

    public Cookie getNewCookie(UUID sessionId) {
        Cookie cookie = new Cookie("sessionId", sessionId.toString());
        cookie.setMaxAge(60 * 30); // 30 минут
        cookie.setPath("/");
        return cookie;
    }
}

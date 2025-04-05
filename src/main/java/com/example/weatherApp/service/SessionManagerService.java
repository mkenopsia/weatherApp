package com.example.weatherApp.service;

import com.example.weatherApp.model.Session;
import com.example.weatherApp.model.User;
import com.example.weatherApp.repositories.SessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.internal.SessionFactoryRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SessionManagerService {
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    CookiesService cookiesService;

    public UUID createSession(User user) {
        UUID sessionId = generateSessionId();
        Session newSession = new Session(sessionId, user, LocalDateTime.now().plusMinutes(30));
        sessionRepository.save(newSession);
        return sessionId;
    }

    public Long getUserId(UUID sessionId) {
        return sessionRepository.findById(sessionId).getUser().getId();
    }

    private UUID generateSessionId() {
        return UUID.randomUUID();
    }

    public void deleteSession(UUID sessionId) {
        sessionRepository.delete(getSessionById(sessionId));
    }

    public Session getSessionById(UUID id) {
        return sessionRepository.findById(id);
    }

    public Session getSessionByUserId(Long userId) {
        return sessionRepository.findByUserId(userId);
    }

    public boolean isExpired(Session session) {
        if(!session.getExpiresAt().isAfter(LocalDateTime.now())) { // если сессия истекла
            sessionRepository.delete(session);
            return true;
        }
        return false;
    }

    public boolean checkIfSessionValid(HttpServletRequest request) {
        UUID sessionId = cookiesService.getSessionId(request.getCookies());
        if(sessionId == null) { // если кукис удалился
            return false;
        }

        return true;
    }
}

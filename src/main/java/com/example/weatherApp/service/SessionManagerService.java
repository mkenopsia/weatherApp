package com.example.weatherApp.service;

import com.example.weatherApp.model.Session;
import com.example.weatherApp.model.User;
import com.example.weatherApp.repositories.SessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionManagerService {

    private final SessionRepository sessionRepository;

    private final CookiesService cookiesService;

    public UUID createSession(User user) {
        UUID sessionId = generateSessionId();
        Session newSession = new Session(sessionId, user, LocalDateTime.now().plusMinutes(30));
        sessionRepository.save(newSession);
        return sessionId;
    }

    public UUID getSessionId(Session session, User currUser) {
        UUID sessionId = (session == null) ? null : session.getId(); // если для юзера нет сессии в таблице - создаём
        if(sessionId == null || isExpired(session)) {
            sessionId = createSession(currUser);
        }
        else {
            prolongSession(session);
        }
        return sessionId;
    }

    public Long getUserId(UUID sessionId) {
        return sessionRepository.findById(sessionId)
                .map(Session::getUser)
                .map(User::getId)
                .orElse(null);
    }

    private UUID generateSessionId() {
        return UUID.randomUUID();
    }

    public void deleteSession(UUID sessionId) {
        sessionRepository.delete(getSessionById(sessionId));
    }

    public Session getSessionById(UUID id) {
        return sessionRepository.findById(id).orElse(null);
    }

    public Session getSessionByUserId(Long userId) {
        return sessionRepository.findByUserId(userId).orElse(null);
    }

    public boolean isExpired(Session session) {
        if (!session.getExpiresAt().isAfter(LocalDateTime.now())) { // если сессия истекла
            sessionRepository.delete(session);
            return true;
        }
        return false;
    }

    public boolean checkIfSessionValid(HttpServletRequest request) {
        UUID sessionId = cookiesService.getSessionId(request.getCookies());
        // если кукис удалился
        return sessionId != null;
    }

    public void prolongSession(Session session) {
        session.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        this.sessionRepository.updateExpirationTime(session);
    }
}

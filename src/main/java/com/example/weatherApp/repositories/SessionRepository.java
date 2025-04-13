package com.example.weatherApp.repositories;

import com.example.weatherApp.model.Session;
import com.example.weatherApp.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class SessionRepository {

    @PersistenceContext
    EntityManager entityManager;

    public void save(Session session) {
        entityManager.persist(session);
    }

    public Optional<Session> findById(UUID id) {
        try {
            TypedQuery<Session> query = entityManager.createQuery(
                    "SELECT s FROM Session s WHERE s.uuid = :id", Session.class);
            query.setParameter("id", id);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Session> findByUserId(Long userId) {
        try {
            TypedQuery<Session> query = entityManager.createQuery(
                    "SELECT s FROM Session s WHERE s.user.id = :userId", Session.class);
            query.setParameter("userId", userId);
            return Optional.of(query.getSingleResult());
        } catch(NoResultException e) {
            return Optional.empty();
        }
    }

    public void delete(Session session) {
        entityManager.remove(entityManager.contains(session) ? session : entityManager.merge(session));
    }

    public void updateExpirationTime(Session session) {
        entityManager.merge(session);
    }
}

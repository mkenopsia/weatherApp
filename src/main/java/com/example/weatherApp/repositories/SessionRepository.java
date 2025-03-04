package com.example.weatherApp.repositories;

import com.example.weatherApp.model.Session;
import com.example.weatherApp.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Transactional
public class SessionRepository {

    @PersistenceContext
    EntityManager entityManager;

    public void save(Session session) {
        entityManager.persist(session);
    }

    public Session findById(UUID id) {
        try {
            return entityManager.find(Session.class, id);
        } catch (Exception e) {
            return null;
        }
    }

    public Session findByUserId(String userId) {
        try {
            TypedQuery<Session> query = entityManager.createQuery(
                    "SELECT s FROM Session s WHERE s.userId = :userId", Session.class);
            query.setParameter("userId", userId);
            return query.getSingleResult(); // Может выбросить NoResultException
        } catch (NoResultException e) {
            return null;
        }
    }

    public void delete(Session session) {
        entityManager.remove(entityManager.contains(session) ? session : entityManager.merge(session));
    }
}

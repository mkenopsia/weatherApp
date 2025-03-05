package com.example.weatherApp.repositories;

import com.example.weatherApp.model.Location;
import com.example.weatherApp.model.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class LocationRepository {
    @PersistenceContext
    EntityManager entityManager;

    public void save(Location location) {
        entityManager.persist(location);
    }

    public Location findById(Long id) {
        return entityManager.find(Location.class, id);
    }

    public List<Location> findLocationsByUserId(Long userId) {
        try {
            TypedQuery<Location> query = entityManager.createQuery(
                    "SELECT l FROM Location l WHERE l.userId = :userId", Location.class);
            query.setParameter("userId", userId);
            return query.getResultList(); // Может выбросить NoResultException
        } catch (NoResultException e) {
            return null;
        }
    }

    public void delete(Long id) {
        Location location = findById(id);
        entityManager.remove(location);
    }
}

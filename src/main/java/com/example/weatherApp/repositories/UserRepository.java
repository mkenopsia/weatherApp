package com.example.weatherApp.repositories;

import com.example.weatherApp.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(User user) {
        entityManager.persist(user);
    }

    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    public User findByName(String name) {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.name = :name", User.class);
            query.setParameter("name", name);
            return query.getSingleResult(); // Может выбросить NoResultException
        } catch (NoResultException e) {
            // Обработка случая, когда пользователь не найден
            return null; // Или выбросить кастомное исключение
        }
    }
    
    public List<User> findAll() {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    public void update(User user) {
        entityManager.merge(user);
    }

    public void delete(User user) {
        entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
    }
}

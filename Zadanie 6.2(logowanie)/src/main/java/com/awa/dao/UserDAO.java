package com.awa.dao;

import com.awa.entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;

@Stateless
public class UserDAO {

    private final static String UNIT_NAME = "Projectbd_PU";

    @PersistenceContext(unitName = UNIT_NAME)
    protected EntityManager em;

    // --- BASIC CRUD ---

    public void create(User user) {
        em.persist(user);
    }
   public User loginUser(String username, String password) {
    if (username == null || password == null) {
        return null;
    }

    try {
        // Ищем пользователя по username и паролю
        return em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username AND u.passwordhash = :password",
                User.class)
                .setParameter("username", username)
                .setParameter("password", password) // уже хэшированный пароль
                .getSingleResult();
    } catch (NoResultException e) {
        return null; // пользователь не найден или неверный пароль
    }
}
    public User find(Object id) {
        return em.find(User.class, id);
    }

    // --- GET ALL USERS ---
    public List<User> getFullList() {
        List<User> list = null;

        Query query = em.createQuery("SELECT u FROM User u");

        try {
            list = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // --- FIND BY USERNAME ---
    public User findByUsername(String username) {
        try {
            return em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; 
        }
    }

    // --- FIND BY EMAIL ---
    public User findByEmail(String email) {
        try {
            return em.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // --- LOGIN CHECK ---
    // ВНИМАНИЕ! passwordhash = уже хешированный пароль
    public User login(String username, String passwordHash) {
        try {
            return em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username AND u.passwordhash = :password", 
                    User.class)
                    .setParameter("username", username)
                    .setParameter("password", passwordHash)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // неверный логин или пароль
        }
    }
    
   public List<String> getUserRole(User user) {
    if (user == null || user.getRole() == null) {
        return List.of(); // пустой список
    }

    return List.of(user.getRole().getName());
}
}
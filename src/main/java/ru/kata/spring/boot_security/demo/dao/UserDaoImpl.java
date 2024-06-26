package ru.kata.spring.boot_security.demo.dao;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private final EntityManager entityManager;

    private final PasswordEncoder passwordEncoder;


    public UserDaoImpl(EntityManager entityManager, PasswordEncoder passwordEncoder) {
        this.entityManager = entityManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return Optional.of(Optional.ofNullable(entityManager.find(User.class, id)).orElseThrow(() -> new EntityNotFoundException(String.format("User with id - '%s' not fount", id))));
    }

    @Override
    public Optional<User> findUserByName(String name) {
        User user = entityManager
                .createQuery("select u from User u where u.name = :username", User.class)
                .setParameter("username", name)
                .getSingleResult();
        return Optional.of(Optional.ofNullable(user).orElseThrow(() -> new EntityNotFoundException(String.format("User with name - '%s' not fount", name))));
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        User user = entityManager
                .createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
        return Optional.of(Optional.ofNullable(user).orElseThrow(() -> new EntityNotFoundException(String.format("User with email - '%s' not fount", email))));
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        entityManager.persist(user);
    }

    @Override
    public void removeUser(Long id) {
        if (entityManager.find(User.class, id) == null) {
            throw new NullPointerException("User not found");
        }
        entityManager.createQuery("DELETE FROM User  u WHERE u.id=:id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void updateUser(User user, Long id) {
        User userFromDB = findUserById(id).get();
        entityManager.merge(user);
        userFromDB.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}

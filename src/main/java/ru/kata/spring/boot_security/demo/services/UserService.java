package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findUserById(Long id);
    Optional<User> findUserByName(String name);
    Optional<User> findUserByEmail(String email);
    List<User> getAllUsers();
    void saveUser(User user);
    void removeUser(Long id);
    void updateUser(User user, Long id);
}

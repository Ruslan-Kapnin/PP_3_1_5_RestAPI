package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    void save(User user);

    void update(User user);

    void remove(Long id);

    User getById(Long id);

    User getByUsername(String username);

    List<User> getUsers();

    List<Role> getRoles();

    void init();
}

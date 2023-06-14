package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User " + username + " not found!");
        }
        return userOptional.get();
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findUsers();
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }


    @Transactional
    public void init() {
        Role roleAdmin = new Role("Admin");
        Role roleUser = new Role("User");
        roleRepository.save(roleAdmin);
        roleRepository.save(roleUser);
        //Password: admin
        save(new User("admin", "$2a$12$AegpJnpe9XwRq8/xGowYUutLOn50gyrg0GeB2SU5cyufrcdYX9QGq", "authority", 999, roleAdmin));
        //Password: user
        save(new User("user", "$2a$12$v1CKeFl.P46zabf.x1iD/uGNyCw7M/4wWEEGgFr2EPJ06QRE3oXdu", "Just User", 1, roleUser));
        save(new User("Walter", "$2a$12$geIevxWdyhwXVZbPgwcH..t.uBZDvdRkx4RvIApNxdiM8jvD1lEHS", "White", 52, roleAdmin));
        save(new User("Jesse", "$2a$12$fn/zm/m5JRpgHc6abXDQyOqQVaY3TFf6n00uUAAvuswV63iIWRlGy", "Pinkman", 25, roleUser));
        save(new User("Saul", "$2a$12$2FK30Zv.DSDELSlC8UfcJegibzelgxLtdbY9B/3h/dZrJyFt5y1e.", "Goodman", 32, roleUser));
        save(new User("Mike", "$2a$12$aXr8zPkZY3uJu7WR0vSb4eNOtbq3yp238T1bcoEQiGJlHKNHSLN1S", "Ehrmantraut", 69, roleUser));
    }
}

package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void save(User user) {
        if (!user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    @Override
    public void update(User user) {
        if (!user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
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
        save(new User("admin", "admin", "authority", 999, roleAdmin));
        save(new User("user", "user", "Just User", 1, roleUser));
        save(new User("Walter", "danger", "White", 52, roleAdmin));
        save(new User("Jesse", "bitch", "Pinkman", 25, roleUser));
        save(new User("Saul", "jimmy", "Goodman", 32, roleUser));
        save(new User("Mike", "HalfMeasures", "Ehrmantraut", 69, roleUser));
    }
}

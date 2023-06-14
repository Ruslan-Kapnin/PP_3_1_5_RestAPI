package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;


@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //<<<---------------------Get Mappings--------------------->>>\\
    @GetMapping("/admin")
    public String getAdminPanel(ModelMap model) {
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<User> users = userService.getUsers();
        model.addAttribute("users", users);
        model.addAttribute("user", loggedUser);
        model.addAttribute("listRoles", userService.getRoles());
        return "admin";
    }

    @GetMapping("/user")
    public String getUser(ModelMap model) {
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = loggedUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin"));
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("user", loggedUser);
        model.addAttribute("listRoles", userService.getRoles());
        return "user";
    }

    @GetMapping("/admin/new_user")
    public String newUser(ModelMap model) {
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", loggedUser);
        model.addAttribute("newUser", new User());
        model.addAttribute("listRoles", userService.getRoles());
        return "new_user";
    }

    @GetMapping("/admin/*/{id}")
    @ResponseBody
    public User getUserForModal(@PathVariable("id") Long id) {
        return userService.getById(id);
    }

    //^^^---------------------Get Mappings---------------------^^^\\



    @PostMapping("/admin/new_user")
    public String addUser(@ModelAttribute("user") User user) {
        if (!user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userService.save(user);
        return "redirect:/admin";
    }


    @DeleteMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.remove(id);
        return "redirect:/admin";
    }


    @PatchMapping("/admin/edit/{id}")
    public String updateUser(User user) {
        if (!user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userService.update(user);
        return "redirect:/admin";
    }
}

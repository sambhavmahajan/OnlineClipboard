package com.github.sambhavmahajan.onlineclipboard.controller;

import com.github.sambhavmahajan.onlineclipboard.model.Clipboard;
import com.github.sambhavmahajan.onlineclipboard.model.Dashboard;
import com.github.sambhavmahajan.onlineclipboard.model.Usr;
import com.github.sambhavmahajan.onlineclipboard.repo.UserRepo;
import com.github.sambhavmahajan.onlineclipboard.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(final UserService userService, UserRepo userRepo) {
        this.userService = userService;
    }
    @GetMapping("/login")
    public String login(HttpSession session, Model model) {
        Usr user = userService.isAuthenticated(session);
        if (user != null) {
            Dashboard dashboard = new Dashboard("Welcome " + user.getName(), user.getClipboardList());
            model.addAttribute("dashboard", dashboard);
            return "redirect:/dashboard";
        }
        return "login";
    }
    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        boolean ok = userService.login(username, password, session);
        if(!ok) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
        return "redirect:/dashboard";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        userService.logout(session);
        return "login";
    }
    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String username, String email, String password, HttpSession session, Model model) {
        Usr user = userService.findByUsernanme(username);
        if(user != null) {
            model.addAttribute("error", "Username is already in use");
            return "register";
        }
        user = new Usr();
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        userService.addUser(user);
        model.addAttribute("ok", "Registration Successful");
        return "register";
    }
    @GetMapping({"/dashboard", "/"})
    public String dashboard(HttpSession session, Model model) {
        Usr user = userService.isAuthenticated(session);
        if(user == null) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
        model.addAttribute("dashboard", new Dashboard("Welcome " + user.getName(), user.getClipboardList()));
        return "dashboard";
    }
}

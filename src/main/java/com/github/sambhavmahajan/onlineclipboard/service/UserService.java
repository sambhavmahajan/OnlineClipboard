package com.github.sambhavmahajan.onlineclipboard.service;

import com.github.sambhavmahajan.onlineclipboard.model.Usr;
import com.github.sambhavmahajan.onlineclipboard.repo.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepo userRepo;
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    @Transactional
    public boolean addUser(Usr user) {
        if(userRepo.findByUsername(user.getUsername()).isEmpty()) {
            userRepo.save(user);
            return true;
        }
        return false;
    }
    public boolean login(String username, String password, HttpSession session) {
        System.out.println(username + " " + password);
        Usr user = authenticate(username, password);
        if(user != null && user.getPassword().equals(password)) {
            session.setAttribute("id", user.getId());
            session.setAttribute("password", user.getPassword());
            return true;
        }
        return false;
    }
    public Usr isAuthenticated(HttpSession session) {
        UUID id = (UUID) session.getAttribute("id");
        if(id == null) return null;
        Optional<Usr> user = userRepo.findById(id);
        if(user.isEmpty()) return null;
        String password = user.get().getPassword();
        if(password.equals(session.getAttribute("password"))) {
            return user.get();
        }
        return null;
    }
    public Usr authenticate(String username, String password) {
        Optional<Usr> user = userRepo.findByUsername(username);
        if(user.isEmpty() || !user.get().getPassword().equals(password)) return null;
        return user.get();
    }
    protected Usr authenticate(UUID id, String password) {
        Optional<Usr> user = userRepo.findById(id);
        if(user.isEmpty() || !user.get().getPassword().equals(password)) return null;
        return user.get();
    }
    @Transactional
    protected void updateUser(Usr user) {
        this.userRepo.save(user);
    }
    public void logout(HttpSession session) {
        session.removeAttribute("id");
        session.removeAttribute("password");
    }
    public Usr findByUsernanme(String username) {
        Optional<Usr> user = userRepo.findByUsername(username);
        if(user.isEmpty()) return null;
        return user.get();
    }
}

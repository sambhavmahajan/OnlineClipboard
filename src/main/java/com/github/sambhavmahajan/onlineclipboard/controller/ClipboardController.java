package com.github.sambhavmahajan.onlineclipboard.controller;

import com.github.sambhavmahajan.onlineclipboard.model.Clipboard;
import com.github.sambhavmahajan.onlineclipboard.model.Dashboard;
import com.github.sambhavmahajan.onlineclipboard.model.Usr;
import com.github.sambhavmahajan.onlineclipboard.service.ClipboardService;
import com.github.sambhavmahajan.onlineclipboard.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/clip")
public class ClipboardController {
    private final ClipboardService clipboardService;
    private final UserService userService;
    public ClipboardController(ClipboardService clipboardService, UserService userService) {
        this.clipboardService = clipboardService;
        this.userService = userService;
    }
    @GetMapping("/{shortId}")
    @ResponseBody
    public String getContent(@PathVariable("shortId") String shortId, HttpSession session) {
        Clipboard clipboard = clipboardService.getByShortId(shortId);
        if(clipboard == null) {
            return "Invalid url!";
        }
        Usr user = userService.isAuthenticated(session);
        boolean isAuth =  (user != null);
        if((clipboard.isPrivate() && isAuth && clipboard.getOwner().equals(user))) {
            return clipboard.getContent();
        }
        if(clipboard.isPrivate() && !isAuth) {
            return "Access denied! This is a private clipboard";
        }
        return clipboard.getContent();
    }
    @PostMapping
    public String createClipboard(@RequestParam String title, @RequestParam String content, @RequestParam(required = false) String isPrivate, Model model, HttpSession session) {
        Usr user = userService.isAuthenticated(session);
        if(user == null) {
            return "redirect:/login";
        }
        Dashboard dashboard = new Dashboard("Welcome " + user.getName(), user.getClipboardList());
        Clipboard clipboard = new Clipboard();
        clipboard.setTitle(title);
        clipboard.setContent(content);
        clipboard.setOwner(user);
        if(isPrivate == null) clipboard.setPrivate(false);
        else clipboard.setPrivate(isPrivate.equals("true"));
        user.getClipboardList().add(clipboard);
        clipboardService.save(clipboard);
        userService.addUser(user);
        model.addAttribute("dashboard", dashboard);
        return "dashboard";
    }
}

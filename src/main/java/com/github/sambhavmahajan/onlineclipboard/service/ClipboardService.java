package com.github.sambhavmahajan.onlineclipboard.service;

import com.github.sambhavmahajan.onlineclipboard.model.Clipboard;
import com.github.sambhavmahajan.onlineclipboard.model.Usr;
import com.github.sambhavmahajan.onlineclipboard.repo.ClipboardRepo;
import com.github.sambhavmahajan.onlineclipboard.repo.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClipboardService {
    private final ClipboardRepo clipboardRepo;
    private final UserService userService;
    public ClipboardService(final ClipboardRepo clipboardRepo, UserService userService) {
        this.clipboardRepo = clipboardRepo;
        this.userService = userService;
    }
    @Transactional
    public boolean newClipboard(String title, String content, HttpSession session) {
        Usr usr = userService.isAuthenticated(session);
        if(usr == null) return false;
        Clipboard clipboard = new Clipboard();
        clipboard.setContent(content);
        clipboard.setOwner(usr);
        clipboard.setShortId(UUID.randomUUID().toString().replace("-", "").substring(0,6));
        clipboard.setTitle(title);
        clipboardRepo.save(clipboard);
        usr.getClipboardList().add(clipboard);
        userService.updateUser(usr);
        return true;
    }
    public List<Clipboard> getClipboards(HttpSession session) {
        UUID id = (UUID) session.getAttribute("id");
        String password = (String) session.getAttribute("password");
        Usr user = userService.authenticate(id, password);
        if(user == null) return null;
        return user.getClipboardList();
    }

    public Clipboard getByShortId(String shortId) {
        Optional<Clipboard> clipboard = clipboardRepo.findByShortId(shortId);
        return clipboard.orElse(null);
    }
    @Transactional
    public void save(Clipboard clipboard) {
        clipboard.setShortId(UUID.randomUUID().toString().replace("-", "").substring(0,6));
        clipboardRepo.save(clipboard);
    }
}

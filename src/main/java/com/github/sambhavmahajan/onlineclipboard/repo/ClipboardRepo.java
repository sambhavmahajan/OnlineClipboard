package com.github.sambhavmahajan.onlineclipboard.repo;

import com.github.sambhavmahajan.onlineclipboard.model.Clipboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClipboardRepo extends JpaRepository<Clipboard, UUID> {
    Optional<Clipboard> findByShortId(String shortId);
}

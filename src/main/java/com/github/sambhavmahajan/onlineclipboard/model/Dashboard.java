package com.github.sambhavmahajan.onlineclipboard.model;

import lombok.Getter;

import java.util.List;

@Getter
public class Dashboard {
    private final List<Clipboard> clipboards;
    private final String title;
    public Dashboard(String title, List<Clipboard> clipboards) {
        this.clipboards = clipboards;
        this.title = title;
    }
}

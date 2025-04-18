package com.github.sambhavmahajan.onlineclipboard.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CurrentTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Clipboard {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(unique = true)
    private String shortId;
    private String title;
    private String content;
    @CurrentTimestamp
    private Timestamp timestamp;
    @ManyToOne
    private Usr owner;
    private boolean isPrivate;
}

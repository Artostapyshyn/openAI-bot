package com.artostapyshyn.gptbot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "chat_log")
public class ChatLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="chat_id", nullable = false)
    private String chatId;

    @Column(name="message", nullable = false)
    private String message;

    @Column(name="timestamp", nullable = false)
    private LocalDateTime timestamp;
}
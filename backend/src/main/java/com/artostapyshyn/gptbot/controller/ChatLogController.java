package com.artostapyshyn.gptbot.controller;
import com.artostapyshyn.gptbot.model.ChatLog;
import com.artostapyshyn.gptbot.service.ChatLogService;
import com.artostapyshyn.gptbot.service.TelegramService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat-logs")
@CrossOrigin(maxAge = 3600, origins = "*")
@AllArgsConstructor
public class ChatLogController {

    private final ChatLogService chatLogService;

    private final TelegramService telegramBotService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<ChatLog>> getAllChatLogs(Authentication authentication) {
        List<ChatLog> chatLogs = chatLogService.findAll();
        return ResponseEntity.ok(chatLogs);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/send-message")
    public void sendMessageToUser(@RequestParam Long chatId, @RequestParam String message, Authentication authentication) {
        telegramBotService.sendMessage(chatId, message);
    }
}

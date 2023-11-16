package com.artostapyshyn.gptBot.service;

import com.artostapyshyn.gptBot.model.ChatLog;
import com.artostapyshyn.gptBot.repository.ChatLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ChatLogService {

    private final ChatLogRepository repository;

    public void saveChatLog(String chatId, String message) {
        ChatLog log = new ChatLog();
        log.setChatId(chatId);
        log.setMessage(message);
        log.setTimestamp(LocalDateTime.now());
        repository.save(log);
    }
}

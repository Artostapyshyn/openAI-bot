package com.artostapyshyn.gptbot.service;

import com.artostapyshyn.gptbot.repository.ChatLogRepository;
import com.artostapyshyn.gptbot.model.ChatLog;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    public void save(ChatLog chatLog) {
        repository.save(chatLog);
    }

    public List<ChatLog> findAll() {
        return repository.findAll();
    }
}


package com.artostapyshyn.gptbot.repository;

import com.artostapyshyn.gptbot.model.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {
}
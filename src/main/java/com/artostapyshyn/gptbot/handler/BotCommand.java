package com.artostapyshyn.gptbot.handler;

public interface BotCommand {
    void execute(Long chatId, String[] args);
}

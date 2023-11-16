package com.artostapyshyn.gptBot.handler;

public interface BotCommand {
    void execute(Long chatId, String[] args);
}

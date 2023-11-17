package com.artostapyshyn.gptbot.handler.impl;

import com.artostapyshyn.gptbot.handler.BotCommand;
import com.artostapyshyn.gptbot.service.TelegramService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class StartCommandHandler implements BotCommand {

    private final TelegramService telegramService;

    @Override
    public void execute(Long chatId, String[] args) {
        telegramService.sendMessage(chatId,
                "Hi, here you can use GPT-3!");
    }
}

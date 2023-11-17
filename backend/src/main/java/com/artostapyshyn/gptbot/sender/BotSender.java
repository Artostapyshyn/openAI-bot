package com.artostapyshyn.gptbot.sender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Component
public class BotSender extends DefaultAbsSender {

    @Value("${telegram.bot.token}")
    private String botToken;

    protected BotSender() {
        super(new DefaultBotOptions());
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}

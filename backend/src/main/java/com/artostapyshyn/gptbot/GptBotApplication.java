package com.artostapyshyn.gptbot;

import com.artostapyshyn.gptbot.bot.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GptBotApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(GptBotApplication.class, args);
    }

    @Autowired
    private TelegramBot telegramBot;

    @Override
    public void run(String... args) throws Exception {
        if (!telegramBot.isRegistered()) {
            telegramBot.botConnect();
        }
    }
}

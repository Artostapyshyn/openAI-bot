package com.artostapyshyn.gptbot;

import com.artostapyshyn.gptbot.bot.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

@SpringBootApplication
public class GptBotApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(GptBotApplication.class, args);
    }

    @Lazy
    @Autowired
    private TelegramBot telegramBot;

    @Override
    public void run(String... args) throws Exception {
        telegramBot.botConnect();
    }
}

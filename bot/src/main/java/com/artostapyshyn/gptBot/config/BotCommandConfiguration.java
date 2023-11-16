package com.artostapyshyn.gptBot.config;

import com.artostapyshyn.gptBot.handler.BotCommand;
import com.artostapyshyn.gptBot.handler.impl.StartCommandHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class BotCommandConfiguration {

    private final StartCommandHandler startCommandHandler;

    @Bean
    public Map<String, BotCommand> commandMap() {
        Map<String, BotCommand> commands = new HashMap<>();
        commands.put("/start", startCommandHandler);
        return commands;
    }
}

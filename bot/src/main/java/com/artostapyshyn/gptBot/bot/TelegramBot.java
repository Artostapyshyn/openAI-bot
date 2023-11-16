package com.artostapyshyn.gptBot.bot;

import com.artostapyshyn.gptBot.handler.BotCommand;
import com.artostapyshyn.gptBot.service.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Collections;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final String botToken;
    private final String botUsername;
    private final String openAiApiKey;
    private final Map<String, BotCommand> commandMap;
    private final TelegramService telegramService;

    public TelegramBot(@Value("${telegram.bot.token}") String botToken,
                       @Value("${telegram.bot.username}") String botUsername,
                       @Value("${openai.api.key}") String openAiApiKey,
                       @Qualifier("commandMap") Map<String, BotCommand> commandMap, TelegramService telegramService) {
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.openAiApiKey = openAiApiKey;
        this.commandMap = commandMap;
        this.telegramService = telegramService;
    }

    public void botConnect() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(this);
            log.info("Bot successfully started!");
        } catch (TelegramApiException e) {
            log.error("Error when starting gptBot. Details: {}", e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Received update: " + update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            System.out.println("Received message from user " + chatId + ": " + text);

//            String responseText = getResponseFromOpenAI(text);
            BotCommand command = commandMap.get(text);
            if (command != null) {
                log.info("Executing command: {}", text);
                command.execute(chatId, null);
            } else {
                log.warn("Unknown command received: {}", text);
                telegramService.sendMessage(chatId, "Unknown command: " + text);
            }
//            telegramService.sendMessage(chatId, responseText);
        } else {
            System.out.println("Received unexpected update: " + update);
        }
    }

    private String getResponseFromOpenAI(String userInput) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + openAiApiKey);

        String requestBody = "{\"prompt\": \"" + userInput + "\", \"max_tokens\": 150}";
        log.info("Sending request to OpenAI: {}", requestBody);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/engines/davinci/completions", request, String.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            try {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                String textResponse = jsonResponse.getJSONArray("choices").getJSONObject(0).getString("text");
                log.info("OpenAI response: {}", textResponse);
                return textResponse;
            } catch (JSONException e) {
                log.error("Error parsing JSON response from OpenAI: {}", e.getMessage());
                return "Error processing your request.";
            }
        } else {
            log.error("Error in OpenAI response: {} - {}", response.getStatusCode(), response.getBody());
            return "Sorry, I couldn't process your request.";
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}

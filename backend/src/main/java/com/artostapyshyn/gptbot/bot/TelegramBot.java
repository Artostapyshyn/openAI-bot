package com.artostapyshyn.gptbot.bot;

import com.artostapyshyn.gptbot.handler.BotCommand;
import com.artostapyshyn.gptbot.service.ChatLogService;
import com.artostapyshyn.gptbot.service.TelegramService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final ChatLogService chatLogService;

    private boolean registered = false;

    public TelegramBot(@Value("${telegram.bot.token}") String botToken,
                       @Value("${telegram.bot.username}") String botUsername,
                       @Value("${openai.api.key}") String openAiApiKey,
                       @Qualifier("commandMap") Map<String, BotCommand> commandMap, TelegramService telegramService, ChatLogService chatLogService) {
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.openAiApiKey = openAiApiKey;
        this.commandMap = commandMap;
        this.telegramService = telegramService;
        this.chatLogService = chatLogService;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void botConnect() throws TelegramApiException {
        if (!registered) {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            try {
                botsApi.registerBot(this);
                registered = true;
            } catch (TelegramApiException e) {
                log.error("Error when starting telegramBot. Details: {}", e.getMessage());
            }
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            chatLogService.saveChatLog(chatId.toString(), "User: " + text);

            BotCommand command = commandMap.get(text);
            if (command != null) {
                log.info("Executing command: {}", text);
                command.execute(chatId, null);
            } else {
                String responseText = getResponseFromOpenAI(text);
                chatLogService.saveChatLog(chatId.toString(), "Bot: " + responseText);

                telegramService.sendMessage(chatId, responseText);
            }
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

        String processedInput = userInput.replace("\"", "\\\"").trim();
        if (processedInput.isEmpty()) {
            return "Please provide a valid input.";
        }

        String requestBody = "{\"model\": \"davinci\", "
                + "\"prompt\": \"" + processedInput + "\", "
                + "\"max_tokens\": 50, "
                + "\"temperature\": 0.3, "
                + "\"top_p\": 1, "
                + "\"frequency_penalty\": 0.0}";

        log.info("Sending request to OpenAI: {}", requestBody);

        try {
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/completions", request, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                String textResponse = jsonResponse.getJSONArray("choices").getJSONObject(0).getString("text").trim();

                if (textResponse.isEmpty() || textResponse.endsWith("...")) {
                    return "I'm sorry, I couldn't generate a complete response. Could you please rephrase or clarify your query?";
                }

                log.info("OpenAI response: {}", textResponse);
                return textResponse;
            } else {
                log.error("Error in OpenAI response: {} - {}", response.getStatusCode(), response.getBody());
                return "Sorry, I couldn't process your request.";
            }
        } catch (Exception e) {
            log.error("Error processing OpenAI request: {}", e.getMessage());
            return "Error processing your request.";
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

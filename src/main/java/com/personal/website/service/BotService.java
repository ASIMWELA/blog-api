package com.personal.website.service;

import com.personal.website.entity.BotChatEntity;
import com.personal.website.payload.ApiResponse;
import com.personal.website.payload.bot.BotMessage;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface BotService {
    String getBotUsername();
    String getBotToken();
    void onUpdateReceived(Update update);
    BotChatEntity sendMessage(BotMessage botMessage);
    ResponseEntity<List<BotChatEntity>> getBotMessages();
}

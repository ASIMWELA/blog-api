package com.personal.website.controller;

import com.personal.website.entity.BotChatEntity;
import com.personal.website.payload.ApiResponse;
import com.personal.website.payload.bot.BotMessage;
import com.personal.website.service.BotService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bots")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BotUpdateController {
    BotService botService;

    @PostMapping( consumes = "application/json", produces = "application/json")
    public BotChatEntity sendMessageToAdmin(@NonNull @RequestBody BotMessage botMessage){
        return  botService.sendMessage(botMessage);
    }
    @GetMapping( consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<BotChatEntity>> getBotChatMessages(){
        return  botService.getBotMessages();
    }

}

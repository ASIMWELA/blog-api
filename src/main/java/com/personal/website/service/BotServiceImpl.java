package com.personal.website.service;


import com.personal.website.entity.BotChatEntity;
import com.personal.website.event.AdminMessageBroadCastedEvent;
import com.personal.website.exception.OperationNotAllowedException;
import com.personal.website.exception.OperationNotSuccessfulException;
import com.personal.website.payload.ApiResponse;
import com.personal.website.payload.bot.BotMessage;
import com.personal.website.repository.BotChatRepository;
import com.personal.website.utils.UidGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.ApiConstants;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class BotServiceImpl extends TelegramLongPollingBot implements BotService{

    RestTemplate restTemplate;
    BotChatRepository botChatRepository;
    ApplicationEventPublisher applicationEventPublisher;

    public BotServiceImpl(RestTemplate restTemplate, BotChatRepository botChatRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.restTemplate = restTemplate;
        this.botChatRepository = botChatRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @NonNull
    @Value("${app.bot.userName}")
    String botUserName;
    @NonNull
    @Value("${app.bot.secrete}")
    String botToken;

    @NonNull
    @Value("${app.bot.chatId}")
    int adminChatId;

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {

        //filter only messages from admin chat
        if(update.getMessage().getChatId() == adminChatId){
            BotChatEntity botChatEntity = BotChatEntity.builder()
                    .message(update.getMessage().getText())
                    .messageDate(new Date(update.getMessage().getDate()))
                    .messageFrom(update.getMessage().getFrom().getFirstName())
                    .build();

            botChatEntity.setUuid(UidGenerator.generateRandomString(12));
            botChatRepository.save(botChatEntity);

            //publish an event
            AdminMessageBroadCastedEvent event = new AdminMessageBroadCastedEvent(botChatEntity);
            applicationEventPublisher.publishEvent(event);
        }
    }

    @Override
    public BotChatEntity sendMessage(BotMessage botMessage) {
        BotMessage b = new BotMessage();
        b.setText(botMessage.getText());
        b.setChat_id(adminChatId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BotMessage> entity = new HttpEntity<>(b,headers);
        String response = restTemplate.postForObject(ApiConstants.BASE_URL+getBotToken()+"/sendMessage", entity, String.class);
        if(response != null){
            JSONObject jsonObject = new JSONObject(response);
            BotChatEntity botChatEntity = BotChatEntity.builder()
                    .message(jsonObject.getJSONObject("result").getString("text"))
                    .messageDate(new Date(jsonObject.getJSONObject("result").getInt("date")))
                    .build();
            botChatEntity.setUuid(UidGenerator.generateRandomString(12));
         return botChatRepository.save(botChatEntity);
        }else {
            throw new OperationNotSuccessfulException("Message not sent");
        }

    }
    @Override
    public ResponseEntity<List<BotChatEntity>> getBotMessages(){
        return new ResponseEntity<>(botChatRepository.getChatMessages(), HttpStatus.OK);
    }


}

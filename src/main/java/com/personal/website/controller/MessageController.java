package com.personal.website.controller;


import com.personal.website.entity.MessageEntity;
import com.personal.website.payload.ChatNotification;
import com.personal.website.repository.MessageRepository;
import com.personal.website.service.ChatMessageService;
import com.personal.website.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class MessageController {
    private MessageRepository messageRepository;
    private ChatMessageService chartMessageService;
    private SimpMessagingTemplate messagingTemplate;
    private ChatMessageService chatMessageService;
    private ChatRoomService chatRoomService;


    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);


    /*-------------------- Group (Public) chat--------------------*/
    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public MessageEntity sendMessage(@Payload MessageEntity chatMessage) {

        if (!chatMessage.getType().equals("TYPING")) {
            messageRepository.save(chatMessage);
        }

        if (chatMessage.getType().equals("LEAVE")) {
            chartMessageService.toggleUserPresence(chatMessage.getSender(), false);
        }

        return chatMessage;
    }

    @MessageMapping("/addUser")
    @SendTo("/topic/public")
    public MessageEntity addUser(@Payload MessageEntity chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Add user in web socket session

        chartMessageService.toggleUserPresence(chatMessage.getSender(), true);

        messageRepository.save(chatMessage);

        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());


        return chatMessage;
        //return chatMessage;

    }

    @MessageMapping("/toggleAdmin")
    public void toggleAdmin(@Payload MessageEntity chatMessage) {
        // Add user in web socket session

        if (chatMessage.getType().equals("JOIN")) {
            chartMessageService.toggleUserPresence(chatMessage.getSender(), true);
        }
        if (chatMessage.getType().equals("LEAVE")) {
            chartMessageService.toggleUserPresence(chatMessage.getSender(), false);
        }

    }

    @MessageMapping("/privateChat")
    public void processMessage(@Payload MessageEntity chatMessage) {
        String chatId = chatRoomService
                .getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);

        chatMessage.setChatId(chatId);

        MessageEntity saved = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(
                        saved.getId(),
                        saved.getSenderId(),
                        saved.getSender()));
    }

}

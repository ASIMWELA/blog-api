package com.personal.website.event;


import com.personal.website.entity.MessageEntity;
import com.personal.website.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Component
@FieldDefaults(makeFinal = true)
@RequiredArgsConstructor
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {

        logger.info("Received a new web socket connection" );
    }
    ChatMessageService chatMessageService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {
            logger.info("UserDto Disconnected : " + username);

            MessageEntity chatMessage = new MessageEntity();
            chatMessage.setType("LEAVE");
            chatMessage.setSender(username);

            chatMessageService.toggleUserPresence(username, true);

            messagingTemplate.convertAndSend("/topic/pubic", chatMessage);

        }
        

    }
}

package com.personal.website.event;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Component
public class AdminMessageBroadCastedEventListener implements ApplicationListener<AdminMessageBroadCastedEvent> {


    //send message to all users subscribed to this end point
    SimpMessagingTemplate messageSender;
    @Override
    public synchronized void onApplicationEvent(AdminMessageBroadCastedEvent adminMessageBroadCastedEvent) {
        messageSender.convertAndSend("/api/v1/admin-messages",  adminMessageBroadCastedEvent.getBotChatEntity());
    }
}

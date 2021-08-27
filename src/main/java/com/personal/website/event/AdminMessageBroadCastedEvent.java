package com.personal.website.event;

import com.personal.website.entity.BotChatEntity;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminMessageBroadCastedEvent extends ApplicationEvent {
    BotChatEntity botChatEntity;
    public AdminMessageBroadCastedEvent(BotChatEntity botChatEntity) {
        super(botChatEntity);
        this.botChatEntity = botChatEntity;
    }
    public BotChatEntity getBotChatEntity(){
        return botChatEntity;
    }
}

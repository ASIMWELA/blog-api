package com.personal.website.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity(name="chatRoom")
@Table(name="chat_room_table")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ChatRoom extends BaseEntity
{
    @Column(name="chat_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String chatId;
    @Column(name="sender_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String senderId;
    @Column(name="recipient_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String recipientId;
}

package com.personal.website.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.personal.website.payload.MessageStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="messages")
@Table(name="messages_table")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="message_id")
    Long id;
    @Column(name="sender")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String sender;
    @Column(name="receiver")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String receiver;
    @Column(name="sender_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String senderId;
    @Column(name="chatId")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String chatId;
    @Column(name="recipient_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String recipientId;
    @Column(name="type")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String type;
    @Column(name="content")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String content;
    @Column(name="message_status")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    MessageStatus status;
    @Column(name="created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime createdAt = LocalDateTime.now();
}

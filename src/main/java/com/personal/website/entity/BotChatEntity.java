package com.personal.website.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="bot_chat_table")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
public class BotChatEntity extends BaseEntity{
    @Column(name = "message", nullable = false)
    String message;
    @Column(name = "message_date", nullable = false)
    Date messageDate;
    @Column(name = "message_from")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String messageFrom;
}

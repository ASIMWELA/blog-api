package com.personal.website.payload.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BotResponse {
    boolean ok;
    BotResult result;
    BotChat chat;
    Date date;
    String text;
}

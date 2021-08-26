package com.personal.website.payload.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BotMessageFrom {
    int id;
    boolean is_bot;
    String first_name;
    String username;
}

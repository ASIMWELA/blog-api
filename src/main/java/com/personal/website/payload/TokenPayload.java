package com.personal.website.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class TokenPayload {
    String access_token;
    String type;
}

package com.personal.website.dto;


import com.personal.website.payload.TokenPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class LoginDto {
    TokenPayload token_payload;
    UserDto userDto;
}

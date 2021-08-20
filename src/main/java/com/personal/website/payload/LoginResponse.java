package com.personal.website.payload;

import com.personal.website.dto.UserDto;

public class LoginResponse
{

    private String ACCESS_TOKEN;

    private UserDto userDto;

    public LoginResponse(String ACCESS_TOKEN, UserDto userDto) {
        this.ACCESS_TOKEN = ACCESS_TOKEN;
        this.userDto = userDto;
    }

    public LoginResponse() {
    }

    public String getACCESS_TOKEN() {
        return ACCESS_TOKEN;
    }

    public void setACCESS_TOKEN(String ACCESS_TOKEN) {
        this.ACCESS_TOKEN = ACCESS_TOKEN;
    }

    public UserDto getUser() {
        return userDto;
    }

    public void setUser(UserDto userDto) {
        this.userDto = userDto;
    }
}

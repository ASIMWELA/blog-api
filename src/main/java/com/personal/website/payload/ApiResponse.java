package com.personal.website.payload;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiResponse
{
    boolean success;
    private String message;

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}

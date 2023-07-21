package com.poscodx.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatResponse {
    private String username;
    private String content;

    public static ChatResponse of(String username, String content){
        return new ChatResponse(username, content);
    }
}

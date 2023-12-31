package com.poscodx.dto;

import com.poscodx.domain.ChatType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatResponse {
    private String username;
    private String content;
    private ChatType type;

    public static ChatResponse of(String username, String content,ChatType type){
        return new ChatResponse(username, content, type);
    }
}

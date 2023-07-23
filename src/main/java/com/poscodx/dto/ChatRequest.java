package com.poscodx.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatRequest {
    private String nickname;
    private String content;
    private String job;
    private String dayNight;
}

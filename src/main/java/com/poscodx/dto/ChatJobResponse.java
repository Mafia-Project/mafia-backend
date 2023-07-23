package com.poscodx.dto;

import com.poscodx.domain.ChatType;
import com.poscodx.domain.JobType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatJobResponse {
    private String username;
    private String content;
    private ChatType type;
    private JobType jobType;


    public static ChatJobResponse of(String username, String content, ChatType type, JobType jobType) {
        return new ChatJobResponse(username, content, type, jobType);
    }
}

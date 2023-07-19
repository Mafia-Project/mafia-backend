package com.poscodx.dto;

import com.poscodx.domain.GameMessageType;
import com.poscodx.domain.JobType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NightEventRequest {
    private String nickname;
    private String target;
    private JobType job;


    @Override
    public String toString() {
        return "NightEventRequest{" +
                "nickname='" + nickname + '\'' +
                ", target='" + target + '\'' +
                ", job=" + job +
                '}';
    }
}

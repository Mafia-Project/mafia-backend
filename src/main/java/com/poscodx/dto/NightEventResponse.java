package com.poscodx.dto;

import com.poscodx.domain.GameMessageType;
import com.poscodx.domain.JobType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import static com.poscodx.domain.GameMessageType.NIGHT_EVENT;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class NightEventResponse {
    private String message;
    private JobType receiverJob;
    private GameMessageType messageType;

    public static NightEventResponse of(String message, JobType receiverJob, GameMessageType gameMessageType){
        return new NightEventResponse(message, receiverJob, gameMessageType);
    }
}

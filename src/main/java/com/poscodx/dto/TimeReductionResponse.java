package com.poscodx.dto;

import static com.poscodx.domain.GameMessageType.*;

import com.poscodx.domain.GameMessageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeReductionResponse {
    private Integer time;
    private GameMessageType type;

    public static TimeReductionResponse of(Integer time) {
        return new TimeReductionResponse(time, TIME_REDUCTION);
    }
}

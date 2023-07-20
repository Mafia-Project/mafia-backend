package com.poscodx.dto;

import static com.poscodx.domain.GameMessageType.*;

import com.poscodx.domain.GameMessageType;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteResultResponse {
    public String message;
    public GameMessageType type;

    public static VoteResultResponse of(String target) {
        String message = Objects.isNull(target) ? "투표결과 아무일도 일어나지 않았습니다." :
                String.format("%s님이 투표결과 죽었습니다.", target);
        return new VoteResultResponse(message, VOTE);
    }
}

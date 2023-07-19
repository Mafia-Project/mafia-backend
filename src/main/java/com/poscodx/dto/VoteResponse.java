package com.poscodx.dto;

import com.poscodx.domain.GameMessageType;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteResponse {
    private final Map<String, Integer> currentVote;
    private final GameMessageType type;

    public static VoteResponse of(Map<String, Integer> currentVote) {
        return new VoteResponse(currentVote, GameMessageType.VOTE);
    }
}

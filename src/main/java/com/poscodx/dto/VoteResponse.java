package com.poscodx.dto;

import com.poscodx.domain.GameMessageType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteResponse {
    private final List<CurrentVote> currentVotes;
    private final GameMessageType type;

    public static VoteResponse of(Map<String, Integer> currentVote) {
        List<CurrentVote> currentVotes = currentVote.keySet().stream()
                .map(key -> CurrentVote.of(key, currentVote.get(key)))
                .collect(Collectors.toList());
        return new VoteResponse(currentVotes, GameMessageType.CURRENT_VOTE);
    }

    @Getter
    @AllArgsConstructor
    static class CurrentVote{
        private String nickname;
        private Integer voteNum;

        public static CurrentVote of(String nickname, Integer voteNum){
            return new CurrentVote(nickname, voteNum);
        }
    }
}

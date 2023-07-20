package com.poscodx.domain;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class GameVote {
    private final Map<String, Integer> ballotBox;
    private final Map<String, String> voterCheck;

    public GameVote() {
        this.ballotBox = new ConcurrentHashMap<>();
        this.voterCheck = new ConcurrentHashMap<>();
    }

    public void addVote(String voter, String target) {
        if (hasDuplicateVote(voterCheck.get(voter), target)) {
            return;
        }

        if (voterCheck.containsKey(voter)) {
            String previousTarget = voterCheck.get(voter);
            ballotBox.computeIfPresent(previousTarget, (key, value) -> value - 1);
        }
        ballotBox.compute(target, (key, value) -> (value == null) ? 1 : value + 1);
        voterCheck.put(voter, target);
    }

    public String voteResult(){
        List<Entry<String, Integer>> sortedBallotBox = ballotBox.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());

        return verifyVoteResult(sortedBallotBox) ? null : sortedBallotBox.get(0).getKey();
    }

    private boolean verifyVoteResult(List<Entry<String, Integer>> sortedBallotBox) {
        return sortedBallotBox.isEmpty() || sortedBallotBox.size() > 1 &&
                sameNumberOfVotes(sortedBallotBox.get(0).getValue(), sortedBallotBox.get(1).getValue());
    }

    private boolean sameNumberOfVotes(Integer maximumNum1, Integer maximumNum2){
        return Objects.equals(maximumNum1, maximumNum2);
    }

    private boolean hasDuplicateVote(String previousTarget, String currentTarget){
        return Objects.nonNull(previousTarget) && previousTarget.equals(currentTarget);
    }
}

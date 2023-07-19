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

        if (sortedBallotBox.isEmpty() || verifyVoteResult(sortedBallotBox.size() > 1,
                sortedBallotBox.get(0).getValue().equals(sortedBallotBox.get(1).getValue()))) {
            return null;
        }
        return sortedBallotBox.get(0).getKey();
    }

    private static boolean verifyVoteResult(boolean sortedBallotBox, boolean sortedBallotBox1) {
        return sortedBallotBox && sortedBallotBox1;
    }

    private boolean hasDuplicateVote(String previousTarget, String currentTarget){
        return verifyVoteResult(Objects.nonNull(previousTarget), previousTarget.equals(currentTarget));
    }
}

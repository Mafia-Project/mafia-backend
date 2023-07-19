package com.poscodx.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Getter
@RequiredArgsConstructor
public class Game {
    @NonNull
    private final String key;
    @NonNull
    private boolean isPsychopathAllowed;
    @NonNull
    private boolean isReporterAllowed;
    @NonNull
    private int maximumPlayer;
    @NonNull
    private List<GamePlayer> playerList;

    private Map<JobType, String> nightSummary = new HashMap<>();


    public void addGamePlayer(GamePlayer gamePlayer) {
        if(playerList.size() < maximumPlayer) playerList.add(gamePlayer);
    }

    public void removeGamePlayer(GamePlayer gamePlayer) {
        playerList.remove(gamePlayer);
    }


    public List<GamePlayer> getGamePlayers() {
        return playerList;
    }

    public GamePlayer findGamePlayerByNickname(String nickName) {
        for(GamePlayer player : playerList){
            if(player.getNickname().equals(nickName)) return player;
        }
        return null;
    }

    public void setPsychopathAllowed(boolean psychopathAllowed) {
        isPsychopathAllowed = psychopathAllowed;
    }

    public void setReporterAllowed(boolean reporterAllowed) {
        isReporterAllowed = reporterAllowed;
    }

    public void setMaximumPlayer(int maximumPlayer) {
        this.maximumPlayer = maximumPlayer;
    }

    public void writeNightSummary(JobType jobType, String targetNickname){
        nightSummary.put(jobType, targetNickname);
    }

    public void clearNightSummary(){
        this.nightSummary.clear();
    }

    public void allocateJob(){
        Collections.shuffle(playerList);
        int mafiaNum = playerList.size() / 4;
        List<JobType> jobs = new ArrayList<>();

        for(int i = 0; i < mafiaNum; i++) jobs.add(JobType.MAFIA);
        if(isPsychopathAllowed) jobs.add(JobType.PSYCHOPATH);
        if(isPsychopathAllowed) jobs.add(JobType.REPORTER);
        jobs.add(JobType.POLICE);
        jobs.add(JobType.DOCTOR);

        for(int i = 0; i < jobs.size(); i++){
            playerList.get(i).setJob(jobs.get(i));
        }

    }
}

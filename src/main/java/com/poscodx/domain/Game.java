package com.poscodx.domain;


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

    private JobType psychopathJob;
    private List<GamePlayer> forPyscopathPlayerList;
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
        playerList.forEach(gamePlayer -> gamePlayer.setIsKilled(false));
        Collections.shuffle(playerList);
        int mafiaNum = playerList.size() / 4;
        List<JobType> jobs = new ArrayList<>();
        List<JobType> jobsPsychoPath = new ArrayList<>();

        for(int i = 0; i < mafiaNum; i++) jobs.add(JobType.MAFIA);
        if(isPsychopathAllowed) {
            List<JobType> psychoCandidate = new ArrayList<JobType>();
            psychoCandidate.add(JobType.POLICE);
            psychoCandidate.add(JobType.DOCTOR);
            psychoCandidate.add(JobType.REPORTER);
            Collections.shuffle(psychoCandidate);
            psychopathJob = psychoCandidate.get(0);
            jobs.add(JobType.PSYCHOPATH);
            System.out.println("PsychopathJob: " + psychopathJob);
        }


        if(isReporterAllowed) jobs.add(JobType.REPORTER);
        jobs.add(JobType.POLICE);
        jobs.add(JobType.DOCTOR);
        int citizenNum = playerList.size() - jobs.size();

        for(int i = 0; i < citizenNum; i++){
            jobs.add(JobType.CITIZEN);
        }

        Collections.shuffle(jobs);

        if(isPsychopathAllowed) {
            jobsPsychoPath.addAll(jobs);
            jobsPsychoPath.remove(JobType.PSYCHOPATH);
            jobsPsychoPath.add(psychopathJob);
            Collections.shuffle(jobsPsychoPath);


            for(int i = 0; i < jobs.size(); i++){
                if(jobs.get(i).equals(JobType.PSYCHOPATH) && !jobsPsychoPath.get(i).equals(psychopathJob)){
                    JobType tmp = jobsPsychoPath.get(jobsPsychoPath.indexOf(psychopathJob));
                    jobsPsychoPath.set(jobsPsychoPath.indexOf(psychopathJob), jobsPsychoPath.get(i));
                    jobsPsychoPath.set(i, tmp);
                    break;
                }
            }
        }

        System.out.println("Jobs Length: "+ jobs.size());
        for(int i = 0; i < jobs.size(); i++){
            playerList.get(i).setJob(jobs.get(i));
            if(isPsychopathAllowed) playerList.get(i).setJobPsychopath(jobsPsychoPath.get(i));
        }

    }

    public boolean checkDuplicateNickname(String nickname){
        for(GamePlayer gamePlayer : playerList){
            if (gamePlayer.getNickname().equals(nickname)) return false;
        }
        return true;
    }

    public long getAliveMafiaNumber(){
        return playerList.stream()
                .filter(player -> player.getJob().equals(JobType.MAFIA) && !player.getKilled()).count();
    }

    public long getAliveCitizenNumber(){
        return playerList.stream()
                .filter(player -> !player.getJob().equals(JobType.MAFIA) && !player.getKilled()).count();
    }
}

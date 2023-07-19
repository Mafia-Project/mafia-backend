package com.poscodx.domain;

import lombok.Getter;
import java.util.Objects;

@Getter
public class GamePlayer {
    private String nickname;
    private JobType job;
    private boolean killed;
    private boolean host;

    public GamePlayer(String nickname, boolean host){
        this.nickname = nickname;
        this.host = host;
    }

    public void setJob(JobType job){
        this.job = job;
    }

    public void setIsKilled(boolean killed){
        this.killed = killed;
    }

    public void setIsHost(boolean host){
        this.host = host;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof GamePlayer){
            return (((GamePlayer) o).nickname.equals(this.nickname));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname, job, killed, host);
    }

}

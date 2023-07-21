package com.poscodx.service.strategy;

import com.poscodx.domain.Game;
import com.poscodx.domain.GamePlayer;
import com.poscodx.domain.JobType;
import com.poscodx.dto.NightEventRequest;
import com.poscodx.service.GameInfoService;
import com.poscodx.service.GameJobService;
import com.poscodx.service.NightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameJobPsychopathService implements GameJobService {
    private final List<GameJobService> jobServices;
    private final GameInfoService gameInfoService;
    public boolean support(String job) {
        return JobType.PSYCHOPATH.name().equals(job);
    }

    @Override
    public void jobEvent(Game game, NightEventRequest request, boolean isPsychopath) {
        JobType psychopathJob = game.getPsychopathJob();
        jobServices.forEach(jobServices -> {
            if(jobServices.support(psychopathJob.toString()))
                jobServices.jobEvent(game, request, true);
        });
    }

}

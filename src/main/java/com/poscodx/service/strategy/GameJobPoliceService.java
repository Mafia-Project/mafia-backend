package com.poscodx.service.strategy;

import com.poscodx.domain.Game;
import com.poscodx.domain.GamePlayer;
import com.poscodx.domain.JobType;
import com.poscodx.dto.NightEventRequest;
import com.poscodx.service.GameJobService;
import com.poscodx.service.NightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameJobPoliceService implements GameJobService {
    private final NightService nightService;

    @Override
    public boolean support(String job) {
        return JobType.POLICE.name().equals(job);
    }

    @Override
    public void jobEvent(Game game, NightEventRequest request, boolean isPsychopath) {
        GamePlayer target = game.findGamePlayerByNickname(request.getTarget());
        JobType targetJob = isPsychopath?  target.getJobPsychopath() : target.getJob();
        JobType playerJob = isPsychopath? JobType.PSYCHOPATH : JobType.POLICE;
        String message = getMessage(target.getNickname(), targetJob);
        nightService.sendChoiceMessage(game.getKey(), message, playerJob);
    }

    private static String getMessage(String target, JobType targetJob) {
        return target + "님은 " + targetJob + " 입니다";
    }
}

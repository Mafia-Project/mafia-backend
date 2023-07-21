package com.poscodx.service.strategy;

import com.poscodx.domain.Game;
import com.poscodx.domain.JobType;
import com.poscodx.dto.NightEventRequest;
import com.poscodx.service.GameJobService;
import com.poscodx.service.NightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameJobReporterService implements GameJobService {

    private final NightService nightService;

    @Override
    public boolean support(String job) {
        return JobType.REPORTER.name().equals(job);
    }

    @Override
    public void jobEvent(Game game, NightEventRequest request, boolean isPsychopath) {
        if(!isPsychopath) game.writeNightSummary(JobType.REPORTER, request.getTarget());
        String message = getMessage(request.getTarget());
        JobType playerJob = isPsychopath? JobType.PSYCHOPATH : JobType.REPORTER;
        nightService.sendChoiceMessage(game.getKey(), message, playerJob);
    }

    private String getMessage(String target) {
        return target + "님을 지목하였습니다. 다음날에 타겟의 직업이 모두에게 공개됩니다.";
    }
}

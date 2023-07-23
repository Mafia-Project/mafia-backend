package com.poscodx.service.strategy;


import static com.poscodx.utils.SocketTopicUtils.*;

import com.poscodx.domain.ChatType;
import com.poscodx.domain.Game;
import com.poscodx.domain.JobType;
import com.poscodx.dto.ChatJobResponse;
import com.poscodx.dto.ChatResponse;
import com.poscodx.dto.NightEventRequest;
import com.poscodx.service.GameJobService;
import com.poscodx.service.NightService;
import com.poscodx.utils.SocketTopicUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameJobDoctorService implements GameJobService {

    private final NightService nightService;

    @Override
    public boolean support(String job) {
        return JobType.DOCTOR.name().equals(job);
    }

    @Override
    public void jobEvent(Game game, NightEventRequest request, boolean isPsychopath) {
        if(!isPsychopath) game.writeNightSummary(JobType.DOCTOR, request.getTarget());
        String message = getMessage(request.getTarget());
        JobType playerJob = isPsychopath? JobType.PSYCHOPATH : JobType.DOCTOR;
        nightService.sendChoiceMessage(game.getKey(), message, playerJob);
        nightService.sendJobEventMessage(game.getKey(), ChatJobResponse.of(SYSTEM_NAME, message, ChatType.JOB, playerJob));
    }

    private String getMessage(String target) {
        return target + "님을 지목하였습니다";
    }
}

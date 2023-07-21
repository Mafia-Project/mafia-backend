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
public class GameJobDoctorService implements GameJobService {

    private final NightService nightService;

    @Override
    public boolean support(String job) {
        return JobType.DOCTOR.name().equals(job);
    }

    @Override
    public void jobEvent(Game game, NightEventRequest request) {
        game.writeNightSummary(JobType.DOCTOR, request.getTarget());
        String message = getMessage(request.getTarget());
        nightService.sendChoiceMessage(game.getKey(), message, JobType.DOCTOR);
    }

    private String getMessage(String target) {
        return target + "님을 지목하였습니다";
    }
}
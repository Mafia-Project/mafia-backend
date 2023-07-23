package com.poscodx.service.strategy;

import static com.poscodx.domain.ChatType.*;
import static com.poscodx.domain.JobType.*;
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
public class GameJobMafiaService implements GameJobService {

    private final NightService nightService;
    @Override
    public boolean support(String job) {
        return MAFIA.name().equals(job);
    }

    @Override
    public void jobEvent(Game game, NightEventRequest request, boolean isPsychopath) {
        game.writeNightSummary(MAFIA, request.getTarget());
        String message = getMessage(request.getNickname(), request.getTarget());
        nightService.sendChoiceMessage(game.getKey(), message, MAFIA);
        nightService.sendJobEventMessage(game.getKey(), ChatJobResponse.of(SYSTEM_NAME, message, JOB, MAFIA));
    }

    private String getMessage(String mafia, String target) {
        return mafia + "님이 " + target + "님을 지목하였습니다";
    }
}

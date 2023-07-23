package com.poscodx.service;

import static com.poscodx.utils.MapUtils.*;
import static com.poscodx.utils.SocketTopicUtils.*;

import com.poscodx.domain.ChatType;
import com.poscodx.domain.Game;
import com.poscodx.domain.GameMessageType;
import com.poscodx.domain.GamePlayer;
import com.poscodx.domain.JobType;
import com.poscodx.dto.ChatJobResponse;
import com.poscodx.dto.ChatResponse;
import com.poscodx.dto.NightEventResponse;
import com.poscodx.utils.MapUtils;
import com.poscodx.utils.SocketTopicUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NightService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GameEventService gameEventService;
    private final GameInfoService gameInfoService;

    public void sendNightEndMessage(String roomKey, String message){
        var response = NightEventResponse.of(message,null ,GameMessageType.NIGHT_END);
        simpMessagingTemplate.convertAndSend(getRoomTopic(roomKey), toMap(response));
    }

    public void sendJobEventMessage(String roomKey, ChatResponse chatResponse){
        simpMessagingTemplate.convertAndSend(getChatTopic(roomKey), toMap(chatResponse));
    }
    public void sendJobEventMessage(String roomKey, ChatJobResponse chatResponse){
        simpMessagingTemplate.convertAndSend(getChatTopic(roomKey), toMap(chatResponse));
    }

    public void sendNightEndGameOverMessage(String roomKey, String message){
        var response = NightEventResponse.of(message,null ,GameMessageType.END);
        simpMessagingTemplate.convertAndSend(getRoomTopic(roomKey), toMap(response));
        simpMessagingTemplate.convertAndSend(getChatTopic(roomKey), toMap(ChatResponse.of(
                SYSTEM_NAME,
                message,
                ChatType.SYSTEM
        )));
    }

    public void sendChoiceMessage(String roomKey, String message, JobType jobType){
        var response = NightEventResponse.of(message, jobType ,GameMessageType.NIGHT_EVENT);
        simpMessagingTemplate.convertAndSend(getRoomTopic(roomKey), toMap(response));
    }

    public synchronized void nightEventResult(Game game){
        Map<JobType, List<String>> nightSummary = game.getNightSummary();
        if (!game.isStart() && !game.isNightEndAble()) return;

        if(Objects.nonNull(nightSummary.get(JobType.DOCTOR))){
            String doctorEvent = game.doctorEvent();
            if(Objects.nonNull(doctorEvent)) {
                String message = doctorEvent + "님이 의사에 의해서 살아났습니다!!";
                gameEventService.messageSent(game.getKey(), MapUtils.toMap(ChatResponse.of(SYSTEM_NAME, message, ChatType.SYSTEM)));
            }
        }

        if (Objects.nonNull(nightSummary.get(JobType.MAFIA)) && nightSummary.get(JobType.MAFIA).size() > 0) {
            List<String> targetNicknames = nightSummary.get(JobType.MAFIA);
            for (String targetNickname : targetNicknames) {
                game.findGamePlayerByNickname(targetNickname).die();
            }
            String message = String.join(",", targetNicknames) + "님이 마피아에 의해 살해당했습니다!";
            gameEventService.messageSent(game.getKey(), MapUtils.toMap(ChatResponse.of(SYSTEM_NAME, message, ChatType.SYSTEM)));
        }else{
            String message = "아무일도 일어나지 않았습니다.";
            gameEventService.messageSent(game.getKey(), MapUtils.toMap(ChatResponse.of(SYSTEM_NAME, message, ChatType.SYSTEM)));
        }

        if (Objects.nonNull(nightSummary.get(JobType.REPORTER))) {
            String targetNickname = nightSummary.get(JobType.REPORTER).get(0);

            JobType targetJob = game.findGamePlayerByNickname(targetNickname).getJob();
            String message = targetNickname + "님이" + targetJob.toString() + " (이)라는 기사가 특보로 실렸습니다!";
            gameEventService.messageSent(game.getKey(), MapUtils.toMap(ChatResponse.of(SYSTEM_NAME, message, ChatType.SYSTEM)));
        }

        game.changeNightEndAble(false);
        game.changeVoteResultAble(true);
        gameInfoService.sendUsers(game.getKey(), GameMessageType.NIGHT_END);
        game.clearNightSummary();
        gameEventService.confirmGameEndAfterDeathEvent(game, GameMessageType.NIGHT_END);
    }
}

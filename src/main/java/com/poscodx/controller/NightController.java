package com.poscodx.controller;

import com.poscodx.domain.Game;
import com.poscodx.domain.GamePlayer;
import com.poscodx.domain.JobType;
import com.poscodx.dto.NightEventRequest;
import com.poscodx.service.GameInfoService;

import com.poscodx.service.NightService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Objects;


@Controller
@RequiredArgsConstructor
public class NightController {
    private final NightService nightService;
    private final GameInfoService gameInfoService;
    @MessageMapping("/rooms/{id}/night-mafia")
    public void nightMafia(@DestinationVariable String id, NightEventRequest request) {
        Game game = gameInfoService.getGame(id);
        game.writeNightSummary(JobType.MAFIA, request.getNickname());
    }

    @MessageMapping("/rooms/{id}/night-police")
    public void nightPolice(@DestinationVariable String id, NightEventRequest request) {
        Game game = gameInfoService.getGame(id);
        GamePlayer target = game.findGamePlayerByNickname(request.getTarget());
        JobType targetJob = target.getJob();

        /*TODO
        *  SEND target은 ~~입니다.
        * */
    }

    @MessageMapping("/rooms/{id}/night-doctor")
    public void nightDoctor(@DestinationVariable String id, NightEventRequest request) {
        Game game = gameInfoService.getGame(id);
        game.writeNightSummary(JobType.DOCTOR, request.getNickname());
    }

    @MessageMapping("/rooms/{id}/night-reporter")
    public void nightReporter(@DestinationVariable String id, NightEventRequest request) {
        Game game = gameInfoService.getGame(id);
        game.writeNightSummary(JobType.REPORTER, request.getNickname());
    }

    @MessageMapping("/rooms/{id}/night-end")
    public void nightEnd(@DestinationVariable String id, NightEventRequest request) {
        Game game = gameInfoService.getGame(id);
        Map<JobType, String> nightSummary = game.getNightSummary();

        if(nightSummary.get(JobType.DOCTOR).equals(nightSummary.get(JobType.MAFIA))){
            /*
            * TODO
            *  SEND ~~가 의사에 의해 살아났습니다.
            * */
        }else{
            GamePlayer player = game.findGamePlayerByNickname(nightSummary.get(JobType.MAFIA));
            player.setIsHost(true);
            /*
             * TODO
             *  SEND ~~가 마피아에게 살해당했습니다.
             * */
        }

        if(Objects.nonNull(nightSummary.get(JobType.REPORTER))){
            String targetNickname = nightSummary.get(JobType.REPORTER);
            JobType targetJob = game.findGamePlayerByNickname(targetNickname).getJob();
            /*
            * TODO
            *  SEND ~~는 ~~라는 특보가 밝혀졌다(모두에게)
            * */
        }



    }

}

package com.poscodx.controller;

import com.poscodx.domain.Game;
import com.poscodx.domain.GameMessageType;
import com.poscodx.domain.GamePlayer;
import com.poscodx.domain.JobType;
import com.poscodx.dto.NightEventRequest;
import com.poscodx.service.GameInfoService;

import com.poscodx.service.GameJobService;
import com.poscodx.service.NightService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@Controller
@RequiredArgsConstructor
public class NightController {
    private final NightService nightService;
    private final GameInfoService gameInfoService;
    private final List<GameJobService> jobServices;

    @MessageMapping("/rooms/{roomKey}/night-mafia")
    public void nightMafia(@DestinationVariable String roomKey, NightEventRequest request) {
        Game game = gameInfoService.getGame(roomKey);
        game.writeNightSummary(JobType.MAFIA, request.getTarget());
        String message = request.getTarget() + "님을 지목하였습니다";
        nightService.sendChoiceMessage(roomKey, message, JobType.MAFIA);
    }

    @MessageMapping("/rooms/{roomKey}/night-police")
    public void nightPolice(@DestinationVariable String roomKey, NightEventRequest request) {
        Game game = gameInfoService.getGame(roomKey);
        GamePlayer target = game.findGamePlayerByNickname(request.getTarget());
        JobType targetJob = target.getJob();
        String message = target.getNickname() + "님은 " + targetJob + " 입니다";
//        String message = "김희아" + "님은 " + " 마피아" + " 입니다";
        nightService.sendChoiceMessage(roomKey, message, JobType.POLICE);
    }

    @MessageMapping("/rooms/{roomKey}/night-doctor")
    public void nightDoctor(@DestinationVariable String roomKey, NightEventRequest request) {
        Game game = gameInfoService.getGame(roomKey);
        game.writeNightSummary(JobType.DOCTOR, request.getTarget());
        String message = request.getTarget() + "님을 지목하였습니다";
//        String message = request.getNickname() + "님을 지목하였습니다";
        nightService.sendChoiceMessage(roomKey, message, JobType.DOCTOR);
    }

    @MessageMapping("/rooms/{roomKey}/night-reporter")
    public void nightReporter(@DestinationVariable String roomKey, NightEventRequest request) {
        Game game = gameInfoService.getGame(roomKey);
        game.writeNightSummary(JobType.REPORTER, request.getTarget());
        String message = request.getTarget() + "님을 지목하였습니다. 다음날에 타겟의 직업이 모두에게 공개됩니다.";
        nightService.sendChoiceMessage(roomKey, message, JobType.REPORTER);
    }

    @MessageMapping("/rooms/{roomKey}/night-end")
    public void nightEnd(@DestinationVariable String roomKey) {
        /*
        * TODO
        *  게임 끝나는 유무 계속 파악.
        * */
        Game game = gameInfoService.getGame(roomKey);
        List<GamePlayer> playerList = game.getPlayerList();


        Map<JobType, String> nightSummary = game.getNightSummary();

        System.out.println(nightSummary);

        /*
        * TODO
        *  nightSummary에서 찾으려고 하는 값이 없을 경우, 처리를 해줘야 할 필요가 있음
        *  내일 ㅋㅋ
        * */

        if(nightSummary.get(JobType.DOCTOR).equals(nightSummary.get(JobType.MAFIA))){
            String message = nightSummary.get(JobType.DOCTOR) + "님이 의사에 의해서 살아났습니다!!";
            nightService.sendNightEndMessage(roomKey, message);

        }else{
            GamePlayer player = game.findGamePlayerByNickname(nightSummary.get(JobType.MAFIA));
            player.setIsKilled(true);

            String message = nightSummary.get(JobType.MAFIA) + "님이 마피아에 의해 살해당했습니다!";
            nightService.sendNightEndMessage(roomKey, message);
        }

        if(Objects.nonNull(nightSummary.get(JobType.REPORTER))){
            String targetNickname = nightSummary.get(JobType.REPORTER);
            JobType targetJob = game.findGamePlayerByNickname(targetNickname).getJob();
            String reporterMessage = targetNickname + "님이"+ targetJob.toString() +" (이)라는 기사가 특보로 실렸습니다!";
            nightService.sendNightEndMessage(roomKey, reporterMessage);
        }


        //Game Over 유무 파악
        int aliveMafia = 0;
        int aliveCitizen = 0;
        for(GamePlayer gamePlayer : playerList){
            if(!gamePlayer.getKilled()){
                if(gamePlayer.getJob().equals(JobType.MAFIA)) aliveMafia+= 1;
                else aliveCitizen += 1;
            }
        }

        if(aliveMafia >= aliveCitizen){
            String endMessage = "마피아팀이 승리하였습니다.";
            nightService.sendNightEndGameOverMessage(roomKey, endMessage);
        }
        game.clearNightSummary();


        for(GamePlayer gamePlayer : game.getPlayerList()){
            System.out.println(gamePlayer);
        }
        System.out.println(aliveMafia + " " + aliveCitizen);
        System.out.println(nightSummary);

    }

    @MessageMapping("/rooms/{roomKey}/{job}")
    public void nightMafia(@DestinationVariable String roomKey,
                           @DestinationVariable String job,
                           NightEventRequest request) {
        Game game = gameInfoService.getGame(roomKey);
        jobServices.forEach(jobServices -> {
                            if(jobServices.support(job)){
                              jobServices.jobEvent(game, request);
                            }
                        });
    }
}

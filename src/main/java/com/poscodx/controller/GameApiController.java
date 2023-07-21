package com.poscodx.controller;

import com.poscodx.domain.Game;
import com.poscodx.domain.GameMessageType;
import com.poscodx.domain.GamePlayer;
import com.poscodx.domain.JobType;
import com.poscodx.dto.CreateRoomRequest;
import com.poscodx.dto.JoinRequest;
import com.poscodx.dto.NightEventRequest;
import com.poscodx.dto.TimeReductionRequest;
import com.poscodx.dto.VoteRequest;
import com.poscodx.service.GameInfoService;
import com.poscodx.service.GameJobService;
import com.poscodx.service.GameService;
import com.poscodx.service.NightService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GameApiController {

    private final GameService gameService;
    private final GameInfoService gameInfoService;
    private final NightService nightService;
    private final List<GameJobService> jobServices;


    @PostMapping("/createRoom")
    public ResponseEntity<Map<String,String>> createRoom(@RequestBody CreateRoomRequest request) {
        System.out.println(request);
        GamePlayer host = new GamePlayer(request.getNickname(), true);
        String id = gameInfoService.addGame(host, request.getUsePsychopath(), request.getUseReporter(), request.getPlayerNum());
        System.out.println();
        Map<String, String> result = new HashMap<>();
        result.put("roomKey",id);
        System.out.println(id);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/joinRoom/{roomKey}")
    public ResponseEntity<Map<String,String>> validateJoinRoom(@PathVariable String roomKey,@RequestBody JoinRequest request) {
        Map<String, String> result = new HashMap<>();
        GamePlayer user = new GamePlayer(request.getNickname(), true);
        Game game = gameInfoService.getGame(roomKey);
        if(Objects.isNull(game)) {
            result.put("result", "INVALID");
            return ResponseEntity.ok().body(result);
        }

        List<GamePlayer> gamePlayerList = game.getPlayerList();
        if(game.getMaximumPlayer() <= gamePlayerList.size()){
            result.put("result", "EXCEEDED");
            return ResponseEntity.ok().body(result);
        }

        if(!game.checkDuplicateNickname(request.getNickname())) {
            result.put("result", "NICKNAME");
            return ResponseEntity.ok().body(result);
        }

        result.put("result","OK");
        return ResponseEntity.ok().body(result);
    }


    @PostMapping("/rooms/{id}/games/time-reduction")
    public ResponseEntity<Void> timeReduction(@PathVariable String id, @RequestBody TimeReductionRequest request) {
        gameService.timeReduction(id, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rooms/{id}/vote")
    public ResponseEntity<Void> voteRequest(@PathVariable String id, @RequestBody VoteRequest request) {
        gameService.vote(id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rooms/{id}/vote")
    public ResponseEntity<Void> voteResult(@PathVariable String id) {
        gameService.voteResult(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rooms/{id}/start-game")
    public ResponseEntity<Void> startGame(@PathVariable String id) {
        Game game = gameInfoService.getGame(id);
        game.allocateJob();
        List<GamePlayer> playerList = game.getGamePlayers();
        for(GamePlayer gamePlayer:playerList){
            System.out.println(gamePlayer);
        }
        gameInfoService.sendUsers(id, GameMessageType.START);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rooms/{id}/games")
    public void jobEvent(@PathVariable String id,
                           @RequestBody NightEventRequest request) {
        Game game = gameInfoService.getGame(id);
        jobServices.forEach(jobServices -> {
            if(jobServices.support(request.getJob().toString())){
                jobServices.jobEvent(game, request, request.getJob().equals(JobType.PSYCHOPATH));
            }
        });
    }

    @PostMapping("/rooms/{roomKey}/night-end")
    public void nightEnd(@PathVariable String roomKey) {
        Game game = gameInfoService.getGame(roomKey);
        List<GamePlayer> playerList = game.getPlayerList();
        Map<JobType, String> nightSummary = game.getNightSummary();
        System.out.println(nightSummary);


        if(Objects.nonNull(nightSummary.get(JobType.DOCTOR)) &&
                Objects.nonNull(nightSummary.get(JobType.MAFIA))&&
                nightSummary.get(JobType.DOCTOR).equals(nightSummary.get(JobType.MAFIA))){
            String message = nightSummary.get(JobType.DOCTOR) + "님이 의사에 의해서 살아났습니다!!";
            nightService.sendNightEndMessage(roomKey, message);

        }else if(Objects.nonNull(nightSummary.get(JobType.MAFIA))) {
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

        gameInfoService.sendUsers(roomKey, GameMessageType.NIGHT_END);


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
    @PostMapping("/rooms/temp-game")
    public ResponseEntity<String> temGame(@RequestBody CreateRoomRequest request) {
        GamePlayer host = new GamePlayer(request.getNickname(), true);
        String id = gameInfoService.addGame(host, request.getUsePsychopath(), request.getUseReporter(), request.getPlayerNum());
        Game game = gameInfoService.getGame(id);
        for (int i = 0; i < request.getPlayerNum() - 2; i++) {
            game.addGamePlayer(new GamePlayer("nickname" + i, false));
        }
        return ResponseEntity.ok(id);
    }
}

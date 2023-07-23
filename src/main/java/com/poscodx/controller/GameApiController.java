package com.poscodx.controller;

import static com.poscodx.utils.MapUtils.toMap;
import static com.poscodx.utils.SocketTopicUtils.SYSTEM_NAME;

import com.poscodx.domain.ChatType;
import com.poscodx.domain.Game;
import com.poscodx.domain.GameMessageType;
import com.poscodx.domain.GamePlayer;
import com.poscodx.domain.JobType;
import com.poscodx.dto.ChatResponse;
import com.poscodx.dto.CreateRoomRequest;
import com.poscodx.dto.JoinRequest;
import com.poscodx.dto.NightEventRequest;
import com.poscodx.dto.TimeReductionRequest;
import com.poscodx.dto.VoteRequest;
import com.poscodx.service.GameEventService;
import com.poscodx.service.GameInfoService;
import com.poscodx.service.GameJobService;
import com.poscodx.service.GameService;
import com.poscodx.service.NightService;
import com.poscodx.utils.MapUtils;
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
    private final GameEventService gameEventService;
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
        gameInfoService.sendUsers(id, GameMessageType.START);
        for (GamePlayer gamePlayer : game.getGamePlayers()) {
            String jobName = gamePlayer.getJob().equals(JobType.PSYCHOPATH) ? game.getPsychopathJob().name()
                    : gamePlayer.getJob().name();
            gameEventService.messageSent(id, toMap(
                    ChatResponse.of(
                            gamePlayer.getNickname(),
                            String.format("당신의 직업은 '%s' 입니다.", jobName),
                            ChatType.JOB)
            ));
        }
        gameEventService.messageSent(id, MapUtils.toMap(
                ChatResponse.of(SYSTEM_NAME, "밤이 시작되어 마피아가 활동합니다.", ChatType.SYSTEM))
        );
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
    public ResponseEntity<Void> nightEnd(@PathVariable String roomKey) {
        Game game = gameInfoService.getGame(roomKey);
        nightService.nightEventResult(game);
        return ResponseEntity.ok().build();
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

package com.poscodx.controller;
import com.poscodx.domain.Game;
import com.poscodx.domain.GamePlayer;
import com.poscodx.dto.CreateRoomRequest;
import com.poscodx.dto.JoinRequest;
import com.poscodx.dto.TimeReductionRequest;
import com.poscodx.service.GameInfoService;
import com.poscodx.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.poscodx.dto.VoteRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GameApiController {

    private final GameService gameService;
    private final GameInfoService gameInfoService;

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
    public ResponseEntity<Map<String,String>> joinRoom(@PathVariable String roomKey,@RequestBody JoinRequest request) {
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
        gameService.result(id);
        return ResponseEntity.ok().build();
    }
}

package com.poscodx.controller;

import com.poscodx.domain.Game;
import com.poscodx.domain.GamePlayer;
import com.poscodx.dto.CreateRoomRequest;
import com.poscodx.dto.TimeReductionRequest;
import com.poscodx.service.GameInfoService;
import com.poscodx.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GameApiController {

    private final GameService gameService;
    private final GameInfoService gameInfoService;

    @PostMapping("/createRoom")
    public ResponseEntity<Void> createRoom(@RequestBody CreateRoomRequest request) {
        System.out.println(request);
        GamePlayer host = new GamePlayer(request.getNickname(), true);
        String id = gameInfoService.addGame(host, request.getUsePsychopath(), request.getUseReporter(), request.getPlayerNum());
        Game game = gameInfoService.getGame(id);
        for(int i = 1; i < game.getMaximumPlayer()+1; i++){
            game.addGamePlayer(new GamePlayer("player" + i, false));
        }
        game.allocateJob();

        for
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rooms/{id}/games/time-reduction")
    public ResponseEntity<Void> timeReduction(@PathVariable String id, @RequestBody TimeReductionRequest request) {
        gameService.timeReduction(id, request);
        return ResponseEntity.ok().build();
    }
}

package com.poscodx.controller;

import com.poscodx.dto.CreateRoomRequest;
import com.poscodx.dto.TimeReductionRequest;
import com.poscodx.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GameApiController {

    private final GameService gameService;

    @PostMapping("/createRoom")
    public ResponseEntity<Void> createRoom(@RequestBody CreateRoomRequest request) {
        System.out.println(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rooms/{id}/games/time-reduction")
    public ResponseEntity<Void> timeReduction(@PathVariable String id, @RequestBody TimeReductionRequest request) {
        gameService.timeReduction(id, request);
        return ResponseEntity.ok().build();
    }
}

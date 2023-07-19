package com.poscodx.controller;

import com.poscodx.dto.TimeReductionRequest;
import com.poscodx.dto.VoteRequest;
import com.poscodx.service.GameService;
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

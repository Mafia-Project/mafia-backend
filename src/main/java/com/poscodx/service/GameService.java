package com.poscodx.service;

import com.poscodx.domain.Game;
import com.poscodx.domain.GamePlayer;
import com.poscodx.dto.TimeReductionRequest;
import com.poscodx.dto.TimeReductionResponse;
import com.poscodx.dto.VoteRequest;
import com.poscodx.dto.VoteResponse;
import com.poscodx.dto.VoteResultResponse;
import com.poscodx.repository.GameInfo;
import com.poscodx.repository.VoteRepository;
import com.poscodx.utils.MapUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final VoteRepository voteRepository;

    public void timeReduction(String id, TimeReductionRequest request) {
        var response = TimeReductionResponse.of(request.getNickname(), request.getTime() / 2);
        simpMessagingTemplate.convertAndSend(getTopic(id), MapUtils.toMap(response));
    }
    public void vote(String id, VoteRequest request) {
        var gameVote = voteRepository.save(id, request.getVoter(), request.getTarget());
        var response = VoteResponse.of(gameVote.getBallotBox());
        simpMessagingTemplate.convertAndSend(getTopic(id), MapUtils.toMap(response));
    }

    public void result(String id){
        VoteResultResponse response = getVoteResultResponse(id);
        voteRepository.remove(id);
        simpMessagingTemplate.convertAndSend(getTopic((id)), MapUtils.toMap(response));
    }

    private VoteResultResponse getVoteResultResponse(String id) {
        var gameVote = voteRepository.findById(id).orElse(null);
        if (Objects.isNull(gameVote) || Objects.isNull(gameVote.voteResult())) return VoteResultResponse.of(null);
        return VoteResultResponse.of(gameVote.voteResult());
    }


    private String getTopic(String id){
        return String.format("/sub/rooms/%s", id);
    }
}

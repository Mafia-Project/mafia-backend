package com.poscodx.service;

import static com.poscodx.utils.SocketTopicUtils.*;
import static com.poscodx.utils.SocketTopicUtils.getChatTopic;
import static com.poscodx.utils.SocketTopicUtils.getRoomTopic;

import com.poscodx.domain.GameVote;
import com.poscodx.dto.ChatResponse;
import com.poscodx.dto.TimeReductionRequest;
import com.poscodx.dto.TimeReductionResponse;
import com.poscodx.dto.VoteRequest;
import com.poscodx.dto.VoteResponse;
import com.poscodx.repository.VoteRepository;
import com.poscodx.utils.MapUtils;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final VoteRepository voteRepository;
    private final GameEventService gameEventService;

    public void timeReduction(String id, TimeReductionRequest request) {
        var response = TimeReductionResponse.of(request.getTime() / 2);
        simpMessagingTemplate.convertAndSend(getRoomTopic(id), MapUtils.toMap(response));
        gameEventService.messageSent(id, MapUtils.toMap(
                ChatResponse.of(SYSTEM_NAME, String.format(TIME_REDUCTION_MASSAGE, request.getNickname()))
        ));
    }

    public void vote(String id, VoteRequest request) {
        var gameVote = voteRepository.save(id, request.getVoter(), request.getTarget());
        var response = VoteResponse.of(gameVote.getBallotBox());
        simpMessagingTemplate.convertAndSend(getRoomTopic(id), MapUtils.toMap(response));
    }

    public void result(String id) {
        var gameVote = voteRepository.findById(id).orElse(null);
        voteRepository.remove(id);
        String target = getVoteResultTarget(gameVote);
        gameEventService.playerDeadEvent(id, target);
        gameEventService.messageSent(id, MapUtils.toMap(
                ChatResponse.of(SYSTEM_NAME, voteResultMessage(target))
        ));
    }

    private String getVoteResultTarget(GameVote gameVote) {
        return Objects.isNull(gameVote) || Objects.isNull(gameVote.voteResult()) ? null : gameVote.voteResult();
    }

    private String voteResultMessage(String target){
        return Objects.isNull(target) ? VOTE_RESULT_NONE_MASSAGE :
                String.format(VOTE_RESULT_KILL_MASSAGE, target);
    }
}

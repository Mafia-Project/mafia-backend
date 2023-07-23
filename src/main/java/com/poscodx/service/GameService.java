package com.poscodx.service;

import static com.poscodx.domain.GameMessageType.*;
import static com.poscodx.utils.MapUtils.*;
import static com.poscodx.utils.SocketTopicUtils.*;
import static com.poscodx.utils.SocketTopicUtils.getRoomTopic;

import com.poscodx.domain.ChatType;
import com.poscodx.domain.Game;
import com.poscodx.domain.GameVote;
import com.poscodx.dto.ChatResponse;
import com.poscodx.dto.TimeReductionRequest;
import com.poscodx.dto.TimeReductionResponse;
import com.poscodx.dto.VoteRequest;
import com.poscodx.dto.VoteResponse;
import com.poscodx.repository.VoteRepository;
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
    private final GameInfoService gameInfoService;

    public void timeReduction(String id, TimeReductionRequest request) {
        var response = TimeReductionResponse.of(request.getTime() / 2);
        simpMessagingTemplate.convertAndSend(getRoomTopic(id), toMap(response));
        gameEventService.messageSent(id, toMap(
                ChatResponse.of(SYSTEM_NAME, String.format(TIME_REDUCTION_MASSAGE, request.getNickname()), ChatType.SYSTEM))
        );
    }

    public void vote(String id, VoteRequest request) {
        var gameVote = voteRepository.save(id, request.getVoter(), request.getTarget());
        var response = VoteResponse.of(gameVote.getBallotBox());
        simpMessagingTemplate.convertAndSend(getRoomTopic(id), toMap(response));
    }

    public synchronized void voteResult(String id) {
        var gameVote = voteRepository.findById(id).orElse(null);
        Game game = gameInfoService.getGame(id);
        if (!game.isStart() || !game.isVoteResultAble()) {
            return;
        }

        voteRepository.remove(id);
        game.changeVoteResultAble(false);
        game.changeNightEndAble(true);
        String target = getVoteResultTarget(gameVote);
        gameEventService.messageSent(id, toMap(
                ChatResponse.of(SYSTEM_NAME, voteResultMessage(target), ChatType.SYSTEM)
        ));
        gameEventService.playerDeadEvent(id, target, VOTE_RESULT);
    }


    private String getVoteResultTarget(GameVote gameVote) {
        return Objects.isNull(gameVote) || Objects.isNull(gameVote.voteResult()) ? null : gameVote.voteResult();
    }

    private String voteResultMessage(String target){
        return Objects.isNull(target) ? VOTE_RESULT_NONE_MASSAGE :
                String.format(VOTE_RESULT_KILL_MASSAGE, target);
    }
}

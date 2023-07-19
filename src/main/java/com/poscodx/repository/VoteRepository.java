package com.poscodx.repository;

import com.poscodx.domain.GameVote;
import java.util.Optional;

public interface VoteRepository {

    GameVote save(String id, String voter, String target);
    Optional<GameVote> findById(String id);

    void remove(String id);

}

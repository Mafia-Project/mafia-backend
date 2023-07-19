package com.poscodx.repository;

import com.poscodx.domain.GameVote;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class VoteInMemoryRepository implements VoteRepository {

    private static final Map<String, GameVote> store = new HashMap<>();

    @Override
    public GameVote save(String id, String voter, String target){
        GameVote gameVote = store.computeIfAbsent(id, k -> new GameVote());
        gameVote.addVote(voter, target);
        return gameVote;
    }

    @Override
    public Optional<GameVote> findById(String id){
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void remove(String id) {
        store.remove(id);
    }
}

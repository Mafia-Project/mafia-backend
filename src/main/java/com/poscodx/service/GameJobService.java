package com.poscodx.service;

import com.poscodx.domain.Game;
import com.poscodx.dto.NightEventRequest;

public interface GameJobService {
    boolean support(String job);
    void jobEvent(Game game, NightEventRequest request, boolean isPsychopath);
}

package com.poscodx.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameEventService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    //살인 이벤트 메시지
    // response : 유저의 최신정보


    //채팅방 메시지
    //response : front-end 참조
}

package com.poscodx.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CreateRoomRequest {
    private String nickname;
    private Integer playerNum;
    private Boolean useReporter;
    private Boolean usePsychopath;

    private Boolean isHost;
}

package com.poscodx.dto;

import com.poscodx.domain.GameMessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameMassage {

    private GameMessageType type;
}

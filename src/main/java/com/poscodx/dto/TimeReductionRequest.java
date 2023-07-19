package com.poscodx.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TimeReductionRequest {
    private String nickname;
    private Integer time;
}

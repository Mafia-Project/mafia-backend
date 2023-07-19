package com.poscodx.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VoteRequest {
    private String voter;
    private String target;
}

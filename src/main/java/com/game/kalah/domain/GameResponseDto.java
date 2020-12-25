package com.game.kalah.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameResponseDto {
    private Long id;
    private String gameUrl;
    Map<Integer, Integer> pitStatus;
}

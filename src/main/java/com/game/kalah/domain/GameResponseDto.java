package com.game.kalah.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class GameResponseDto {
    private Long id;
    private String gameUrl;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Map<Integer, Integer> pitStatus;
}

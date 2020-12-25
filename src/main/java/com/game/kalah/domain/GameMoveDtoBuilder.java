package com.game.kalah.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GameMoveDtoBuilder {
    private int[] pits;
    private int pitNumber;
    private int playerCategory;
}

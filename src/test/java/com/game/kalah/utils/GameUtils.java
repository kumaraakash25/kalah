package com.game.kalah.utils;

import com.game.kalah.domain.GameMoveDtoBuilder;
import com.game.kalah.domain.enums.PlayerCategory;

import java.util.Arrays;

public class GameUtils {

    public static int[] initialise6Pit6StoneBoard() {
        int[] pits = new int[14];
        Arrays.fill(pits, 6);
        // Setting the number of stones in house pit to zero
        pits[6] = 0;
        pits[13] = 0;
        return pits;
    }

    public static GameMoveDtoBuilder buildGameMove(int pitNumber, int[] pits, PlayerCategory playerCategory) {
        return GameMoveDtoBuilder.builder()
                .pitNumber(pitNumber)
                .pits(pits)
                .playerCategory(playerCategory.getCategory())
                .build();
    }
}

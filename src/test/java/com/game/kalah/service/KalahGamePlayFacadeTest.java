package com.game.kalah.service;

import com.game.kalah.domain.GameMoveDtoBuilder;
import com.game.kalah.domain.enums.PlayerCategory;
import com.game.kalah.utils.GameUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class KalahGamePlayFacadeTest {

    @BeforeEach
    public void setup() {
        facade = new KalahGamePlayFacade(6, 2);
    }
    KalahGamePlayFacade facade;

    @Test
    public void verifyBoardState_AfterSuccessfulMove() {
        int[] pits = GameUtils.initialise6Pit6StoneBoard();
        GameMoveDtoBuilder move = GameUtils.buildGameMove(3, pits, PlayerCategory.HUMAN);
        facade.isGameOverAfterMove(move);
        int[] expectedPitState = {6, 6, 0, 7, 7, 7, 1, 7, 7, 6, 6, 6, 6, 0};
        assertArrayEquals(expectedPitState, pits);
    }

    @Test
    public void verifyExceptionThrown_IfInvalidMove() {
        // Given
        int[] pits = GameUtils.initialise6Pit6StoneBoard();
        GameMoveDtoBuilder move = GameUtils.buildGameMove(3, pits, PlayerCategory.COMPUTER);
        // Expected exception
        assertThrows(IllegalArgumentException.class, () -> facade.isGameOverAfterMove(move));
    }

    @Test
    public void verifyBoardState_WhenLastStoneIsLeft_CaptureOppositePlayerStones() {
        // Creating a scenario when the last stone will be left in pit 6 after the player move.
        int[] pits = {0, 0, 0, 0, 2, 0, 15, 8, 9, 5, 2, 8, 16, 12};
        GameMoveDtoBuilder move = GameUtils.buildGameMove(5, pits, PlayerCategory.HUMAN);
        boolean isGameOver = facade.isGameOverAfterMove(move);
        // Initial stones = 15 + 1(when move from pit 5)+1(when last left in pit 6)+ 8(from opposite player's pit)
        assertEquals(25, pits[6]);
        assertTrue(isGameOver);
    }
}
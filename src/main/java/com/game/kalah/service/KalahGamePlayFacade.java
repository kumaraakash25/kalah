package com.game.kalah.service;

import com.game.kalah.domain.GameMoveDtoBuilder;
import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public class KalahGamePlayFacade {

    private final int nonHousePitPerPlayer;
    private final int numberOfPlayersPerGame;

    /**
     * Validates the player move
     * Distributes the stones from the selected pitNumber
     * Checks for the last stone left in either of the board
     * Returns true if the game expects further moves
     *
     * @param gameMoveDtoBuilder
     * @return
     */
    public boolean isGameOverAfterMove(GameMoveDtoBuilder gameMoveDtoBuilder) {
        int pitNumber = gameMoveDtoBuilder.getPitNumber();
        int[] pits = gameMoveDtoBuilder.getPits();
        int playerCategory = gameMoveDtoBuilder.getPlayerCategory();
        validateMove(pits, pitNumber, playerCategory);
        distributeStones(pits, pitNumber);
        return checkGameOver(pits, pitNumber);
    }

    /**
     * Distributes stones in adjacent pits including the player's house, excluding the opponent's house
     */
    private void distributeStones(int[] pits, int pitNumber) {
        int currentPitIndex = pitNumber - 1;
        int stonesToDistribute = pits[currentPitIndex];
        pits[currentPitIndex] = 0; // Empty current pit
        int opponentHousePitIndex = getOpponentHousePitIndex(pitNumber);
        // Start distributing stones in the pits till no more left
        while (stonesToDistribute > 0) {
            currentPitIndex++;
            stonesToDistribute--;
            // If currentPitIndex = opponent's house index then don't add stone into pit move to the next pit
            if (currentPitIndex == opponentHousePitIndex) {
                currentPitIndex++;
            }
            // Restart from the beginning of the array when the end is reached
            if (currentPitIndex > pits.length - 1) {
                currentPitIndex = 0;
            }
            pits[currentPitIndex] += 1;
        }
    }

    /**
     * Returns the opponent house pit index
     */
    private int getOpponentHousePitIndex(int pitNumber) {
        int HOUSE_PIT_DEFAULT_INDEX = nonHousePitPerPlayer;
        int pitIndex = pitNumber - 1;
        return pitIndex < HOUSE_PIT_DEFAULT_INDEX ? HOUSE_PIT_DEFAULT_INDEX * numberOfPlayersPerGame + 1 : HOUSE_PIT_DEFAULT_INDEX;
    }

    /**
     * Validates the pitId and throws RuntimeException if invalid
     */
    private void validateMove(int[] pits, int pitNumber, int playerCategory) {
        int pitIndex = pitNumber - 1;
        if (pitIndex < 0 || pitIndex > pits.length - 1) {
            throw new IllegalArgumentException("Invalid input, pit cannot be less than 0 or more than number of pits on board");
        }
        if (pits[pitIndex] == 0) {
            throw new IllegalArgumentException("No more stones in pit, choose a different pit");
        }
        // Stone movement from house pit is not allowed
        if (pitIndex == nonHousePitPerPlayer ||
                pitIndex == nonHousePitPerPlayer * 2 + 1) {
            throw new IllegalArgumentException("Invalid move, cannot pick stones from home pit");
        }
        // If player category is 1 the player owns the first half of the board
        // which means the player can choose only from pits 0...6, otherwise pits 7...13 belong to the player
        if (playerCategory == 1 && pitIndex > nonHousePitPerPlayer) {
            throw new IllegalArgumentException("Invalid move, cannot pick stones from opponent's pit");
        }
        if (playerCategory == 2 && pitIndex < nonHousePitPerPlayer) {
            throw new IllegalArgumentException("Invalid move, cannot pick stones from opponent's pit");
        }
    }

    /**
     * Checks if the last stone is left in the player pit. If yes then take all the opponent
     * stones and place in own house pit.
     */
    private boolean checkGameOver(int[] pits, int pitNumber) {
        int HOUSE_PIT_DEFAULT_INDEX = nonHousePitPerPlayer;
        int start;
        int end;
        if (pitNumber > HOUSE_PIT_DEFAULT_INDEX) {
            start = HOUSE_PIT_DEFAULT_INDEX + 1;
            end = HOUSE_PIT_DEFAULT_INDEX * numberOfPlayersPerGame;
        } else {
            start = 0;
            end = HOUSE_PIT_DEFAULT_INDEX - 1;
        }
        int[] playerPitArray = Arrays.copyOfRange(pits, start, end + 1);
        int stones = Arrays.stream(playerPitArray).sum();
        if (stones == 1) {
            addStonesInHousePit(pits, playerPitArray, pitNumber);
            return true;
        }
        return false;
    }

    /**
     * Capture opponent's stone into own house pit
     */
    private void addStonesInHousePit(int[] pits, int[] playerPitArray, int pitNumber) {
        int HOUSE_PIT_DEFAULT_INDEX = nonHousePitPerPlayer;
        int pitIndexWithLastLeftoverStone = Arrays.binarySearch(playerPitArray, 1);
        if (pitNumber > HOUSE_PIT_DEFAULT_INDEX) {
            int housePitIndex = HOUSE_PIT_DEFAULT_INDEX * numberOfPlayersPerGame + 1;
            pits[housePitIndex] = pits[housePitIndex] +
                    pits[HOUSE_PIT_DEFAULT_INDEX * numberOfPlayersPerGame - pitIndexWithLastLeftoverStone] + 1;
        } else {
            pits[HOUSE_PIT_DEFAULT_INDEX] = pits[HOUSE_PIT_DEFAULT_INDEX] +
                    pits[2 * HOUSE_PIT_DEFAULT_INDEX - pitIndexWithLastLeftoverStone] + 1;
        }
        pits[pitIndexWithLastLeftoverStone] = 0;
    }
}

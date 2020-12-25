package com.game.kalah.service;

import com.game.kalah.domain.Game;
import com.game.kalah.domain.enums.PlayerCategory;

/**
 * Interface that has the following methods
 */
public interface GameService {

    /**
     * Creates a new game, upon user request. Initialises all the objects(game, board, player)
     * required in order to begin the game play
     */
    Game createGame();

    /**
     * Makes the game move as per the user provided gameId and pitNumber. Move Validation must done while implementation
     *
     * @param gameId
     * @param pitNumber
     * @return
     * @throws RuntimeException         if the player tries to make a move when not allowed
     * @throws IllegalArgumentException if the pitNumber is invalid or not belonging to player
     */
    Game makeMove(Long gameId, int pitNumber, PlayerCategory playerCategory);
}

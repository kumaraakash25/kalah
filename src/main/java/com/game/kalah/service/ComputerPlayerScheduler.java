package com.game.kalah.service;

import com.game.kalah.domain.Game;
import com.game.kalah.domain.enums.GameStatus;
import com.game.kalah.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.game.kalah.domain.enums.PlayerCategory.COMPUTER;

/**
 * The scheduler makes the COMPUTER player move. After successful human player move is complete, those games are
 * picked up by the scheduler that have a pending move from the Computer player.
 */
@Component
@RequiredArgsConstructor
public class ComputerPlayerScheduler {

    private final GameService gameService;
    private final GameRepository gameRepository;

    /**
     * Generates a random number in range from 8 to 13 and makes the move
     */
    @Scheduled(fixedRate = 10000)
    public void computerMove() {
        List<Game> games = gameRepository.findAllByGameStatus(GameStatus.WAITING_FOR_COMPUTER_MOVE);
        games.forEach(game -> {
            int pitNumber = (int) (Math.random() * ((13 - 8) + 1)) + 8;
            MDC.put("gameId", game.getGameId().toString());
            gameService.makeMove(game.getGameId(), pitNumber, COMPUTER);
            MDC.clear();
        });
    }
}

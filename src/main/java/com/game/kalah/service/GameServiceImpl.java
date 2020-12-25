package com.game.kalah.service;

import com.game.kalah.domain.Board;
import com.game.kalah.domain.Game;
import com.game.kalah.domain.GameMoveDtoBuilder;
import com.game.kalah.domain.Player;
import com.game.kalah.domain.enums.GameStatus;
import com.game.kalah.domain.enums.PlayerCategory;
import com.game.kalah.repository.GameRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static com.game.kalah.domain.enums.GameStatus.*;

@Slf4j
@Data
@Service
public class GameServiceImpl implements GameService {

    @Value("${game.pits}")
    private int totalPits;
    @Value("${game.initial-stones-per-pit}")
    private int initialStonesPerPit;
    @Value("${game.non-house-pit-per-player}")
    private int nonHousePitPerPlayer;
    @Value("${game.players:2}")
    private int numberOfPlayersPerGame;

    private final BoardService boardService;
    private final PlayerService playerService;
    private final GameRepository gameRepository;

    private KalahGamePlayFacade gamePlayFacade;

    @PostConstruct
    public void init() {
        gamePlayFacade = new KalahGamePlayFacade(nonHousePitPerPlayer, numberOfPlayersPerGame);
    }

    public Game createGame() {
        Board board = boardService.initialiseBoard(totalPits, initialStonesPerPit, nonHousePitPerPlayer);
        List<Player> players = playerService.createPlayers();
        return gameRepository.save(Game.builder()
                .board(board)
                .player(players)
                .gameStatus(GameStatus.NEW)
                .build());
    }

    /**
     * Validates the player Move and sets the game status to IN_PROGRESS
     * Loads the board state from the database
     * Builds up a moveDto and makes the move for the player and marks the gme status for next player to mke the move
     *
     * @param gameId
     * @param pitNumber
     * @param category
     * @return
     */
    @Transactional
    public Game makeMove(Long gameId, int pitNumber, PlayerCategory category) {
        log.info("Making a move from pit number {} for game {}", pitNumber, gameId);
        Game game = gameRepository.findById(gameId).orElseThrow();
        validateMove(game, category);
        updateGameStatusToProgress(game);

        int[] pits = boardService.loadBoard(game.getBoard());
        Predicate<Player> playerPredicate = player -> player.getCategory().equals(category.getCategory());
        Player player = game.getPlayer().stream().filter(playerPredicate).findFirst().orElseThrow();
        GameMoveDtoBuilder gameMove = buildMove(pits, pitNumber, player);
        boolean isGameOver = gamePlayFacade.isGameOverAfterMove(gameMove);

        // Sets up the game state as per result after player move
        GameStatus status = gameStatus(isGameOver, category);
        game.setGameStatus(status);
        game.getBoard().setBoardState(Arrays.toString(pits));

        return gameRepository.save(game);
    }

    private void updateGameStatusToProgress(Game game) {
        game.setGameStatus(GameStatus.MOVE_IN_PROGRESS);
        gameRepository.save(game);
    }

    private void validateMove(Game game, PlayerCategory category) {
        GameStatus status = game.getGameStatus();
        if (status.equals(GameStatus.MOVE_IN_PROGRESS)) {
            throw new RuntimeException("Move in progress. Please wait..");
        }
        if (category.equals(PlayerCategory.HUMAN) && status.equals(WAITING_FOR_COMPUTER_MOVE)) {
            throw new RuntimeException("Waiting for computer move");
        }
    }

    private GameStatus gameStatus(boolean isGameOver, PlayerCategory category) {
        if (isGameOver) {
            return FINISH;
        } else {
            switch (category) {
                case HUMAN:
                    return WAITING_FOR_COMPUTER_MOVE;
                case COMPUTER:
                    return WAITING_FOR_HUMAN_MOVE;
            }
        }
        return INVALID;
    }

    private GameMoveDtoBuilder buildMove(int[] pits, int pitNumber, Player player) {
        return GameMoveDtoBuilder.builder()
                .pitNumber(pitNumber)
                .pits(pits)
                .playerCategory(player.getCategory())
                .build();
    }

}

package com.game.kalah.service;

import com.game.kalah.domain.Board;
import com.game.kalah.domain.Game;
import com.game.kalah.domain.Player;
import com.game.kalah.domain.enums.GameStatus;
import com.game.kalah.domain.enums.PlayerCategory;
import com.game.kalah.repository.GameRepository;
import com.game.kalah.utils.GameUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.game.kalah.domain.enums.GameStatus.MOVE_IN_PROGRESS;
import static com.game.kalah.domain.enums.GameStatus.WAITING_FOR_COMPUTER_MOVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {

    @Mock
    BoardService boardService;
    @Mock
    PlayerService playerService;
    @Mock
    GameRepository gameRepository;
    @InjectMocks
    GameServiceImpl gameService;
    KalahGamePlayFacade facade;

    @BeforeEach
    public void setup() {
        facade = new KalahGamePlayFacade(6, 2);
        gameService.setGamePlayFacade(facade);
    }

    @Test
    public void verifyRepoCallCount_WhenNewGameIsCreated() {
        gameService.createGame();
        verify(boardService, times(1)).initialiseBoard(anyInt(), anyInt(), anyInt());
        verify(playerService, times(1)).createPlayers();
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    public void verifyExceptionIsThrown_WhenPlayerMakesMoveOutOfTurn() {
        Player player = Player.builder().category(PlayerCategory.HUMAN.getCategory()).build();
        when(gameRepository.findById(anyLong()))
                .thenReturn(Optional.of(
                        Game.builder()
                                .gameStatus(WAITING_FOR_COMPUTER_MOVE)
                                .player(List.of(player))
                                .build()));
        assertThrows(RuntimeException.class, () -> gameService.makeMove(1L, 2, PlayerCategory.HUMAN));
    }


    @Test
    public void testGameStatusUpdatedAToWAITING_FOR_COMPUTER_MOVE_AfterHumanMove() {
        Player player = Player.builder().category(PlayerCategory.HUMAN.getCategory()).build();
        int[] board = GameUtils.initialise6Pit6StoneBoard();
        Game game = Game.builder()
                .gameStatus(GameStatus.WAITING_FOR_HUMAN_MOVE)
                .player(List.of(player))
                .board(Board.builder().boardState(Arrays.toString(board)).build())
                .build();
        when(boardService.loadBoard(any(Board.class))).thenReturn(board);
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(game));
        when(gameRepository.save(game)).then((Answer<Game>) invocation -> {
            Game invokedGame = (Game) invocation.getArguments()[0];
            assertEquals(MOVE_IN_PROGRESS, invokedGame.getGameStatus());
            return invokedGame;
        }).then((Answer<Game>) invocation -> {
            Game invokedGame = (Game) invocation.getArguments()[0];
            assertEquals(WAITING_FOR_COMPUTER_MOVE, invokedGame.getGameStatus());
            return invokedGame;
        });
        gameService.makeMove(1L, 2, PlayerCategory.HUMAN);
    }
}
package com.game.kalah.service;

import com.game.kalah.domain.Game;
import com.game.kalah.domain.enums.GameStatus;
import com.game.kalah.domain.enums.PlayerCategory;
import com.game.kalah.repository.GameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.game.kalah.domain.enums.PlayerCategory.COMPUTER;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComputerPlayerSchedulerTest {

    @Mock
    private GameService gameService;
    @Mock
    private GameRepository gameRepository;
    @InjectMocks
    private ComputerPlayerScheduler scheduler;

    @Test
    void computerMove() {
        Long gameId = 123L;
        when(gameRepository.findAllByGameStatus(GameStatus.WAITING_FOR_COMPUTER_MOVE)).thenReturn(List.of(
                Game.builder().gameId(gameId).gameStatus(GameStatus.WAITING_FOR_COMPUTER_MOVE).build()));
        scheduler.computerMove();
        verify(gameService, times(1)).makeMove(anyLong(), anyInt(), any(PlayerCategory.class));
    }
}
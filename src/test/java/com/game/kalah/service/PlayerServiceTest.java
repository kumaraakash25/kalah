package com.game.kalah.service;

import com.game.kalah.domain.Player;
import com.game.kalah.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.List;

import static com.game.kalah.domain.enums.PlayerCategory.COMPUTER;
import static com.game.kalah.domain.enums.PlayerCategory.HUMAN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    PlayerRepository playerRepository;
    @InjectMocks
    PlayerService playerService;

    @Test
    void createPlayers() {
        when(playerRepository.save(any(Player.class))).then((Answer<Player>) invocation -> (Player) invocation.getArguments()[0]);
        List<Player> players = playerService.createPlayers();
        assertEquals(2, players.size());
        assertEquals((int)players.get(0).getCategory(), HUMAN.getCategory());
        assertEquals((int)players.get(1).getCategory(), COMPUTER.getCategory());
    }
}
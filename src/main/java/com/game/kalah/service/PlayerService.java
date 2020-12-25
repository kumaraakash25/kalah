package com.game.kalah.service;

import com.game.kalah.domain.Player;
import com.game.kalah.domain.enums.PlayerCategory;
import com.game.kalah.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    /**
     * Creates and saves 2 players to the database. The first player HUMAN and second player COMPUTER
     *
     * @return
     */
    public List<Player> createPlayers() {
        return List.of(PlayerCategory.values()).stream()
                .sorted(Comparator.comparing(PlayerCategory::getCategory))
                .map(category -> {
                    Player player = Player.builder().category(category.getCategory()).build();
                    return playerRepository.save(player);
                })
                .collect(Collectors.toList());
    }

}

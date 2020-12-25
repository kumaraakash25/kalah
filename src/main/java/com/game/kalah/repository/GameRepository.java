package com.game.kalah.repository;

import com.game.kalah.domain.Game;
import com.game.kalah.domain.enums.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findAllByGameStatus(GameStatus gameStatus);
}

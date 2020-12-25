package com.game.kalah.controller;

import com.game.kalah.domain.Game;
import com.game.kalah.domain.GameResponseDto;
import com.game.kalah.domain.enums.PlayerCategory;
import com.game.kalah.service.GameService;
import com.game.kalah.util.ResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final ResponseMapper mapper;

    @PostMapping("/games")
    public ResponseEntity<GameResponseDto> createGame() {
        Game game = gameService.createGame();
        MDC.put("gameId", game.getGameId().toString());
        log.info("New game created");
        GameResponseDto responseDto = mapper.mapResponse(game);
        MDC.clear();
        return ResponseEntity.created(URI.create(responseDto.getGameUrl())).body(responseDto);
    }

    @PutMapping("/games/{gameId}/pits/{pitId}")
    public ResponseEntity<GameResponseDto> makeMove(@PathVariable("gameId") Long gameId, @PathVariable("pitId") int pitId) {
        MDC.put("gameId", gameId.toString());
        Game game = gameService.makeMove(gameId, pitId, PlayerCategory.HUMAN);
        GameResponseDto responseDto = mapper.mapResponse(game);
        MDC.clear();
        return ResponseEntity.ok().body(responseDto);
    }
}

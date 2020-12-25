package com.game.kalah.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.kalah.domain.Game;
import com.game.kalah.domain.GameResponseDto;
import com.game.kalah.domain.enums.GameStatus;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;

import java.util.*;
import java.util.function.Function;


@Component
@RequiredArgsConstructor
public class ResponseMapper {

    @Value("${game.url}")
    private String gameUrl;

    ObjectMapper mapper = new ObjectMapper();

    /**
     * Generates the response DTO based on the GameState
     * @param game
     * @return
     */
    @SneakyThrows
    public GameResponseDto mapResponse(Game game) {
        Long gameId = game.getGameId();
        String gameResponseUrl = new UriTemplate(gameUrl).expand(gameId).toString();

        GameResponseDto gameResponseDto = GameResponseDto.builder().id(gameId).gameUrl(gameResponseUrl).build();
        if (!game.getGameStatus().equals(GameStatus.NEW)) {
            Integer[] boardStatus = mapper.readValue(game.getBoard().getBoardState(), Integer[].class);
            Function<Integer[], Map> transformer = new ListToMapTransformerFunction();
            var map = transformer.apply(boardStatus);
            gameResponseDto.setPitStatus(map);
        }
        return gameResponseDto;
    }
}



package com.game.kalah.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.kalah.domain.Board;
import com.game.kalah.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    ObjectMapper mapper = new ObjectMapper();

    /**
     * Initialises board with the total number of pits and sets the house pit to 0
     * @param totalPits
     * @param initialStonesPerPit
     * @param nonHousePitPerPlayer
     * @return
     */
    public Board initialiseBoard(int totalPits, int initialStonesPerPit, int nonHousePitPerPlayer) {
        int[] pits = new int[totalPits];
        Arrays.fill(pits, initialStonesPerPit);
        // Setting the number of stones in house pit to zero
        pits[nonHousePitPerPlayer] = 0;
        pits[nonHousePitPerPlayer * 2 + 1] = 0;
        Board board = boardRepository.save(Board.builder().boardState(Arrays.toString(pits)).build());
        return board;
    }

    /**
     * Converts the board state to an integer array
     * @param board
     * @return
     */
    @SneakyThrows
    public int[] loadBoard(Board board) {
        return mapper.readValue(board.getBoardState(), int[].class);
    }
}

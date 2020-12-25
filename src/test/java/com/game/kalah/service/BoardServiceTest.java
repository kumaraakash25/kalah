package com.game.kalah.service;

import com.game.kalah.domain.Board;
import com.game.kalah.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    BoardRepository boardRepository;
    @InjectMocks
    BoardService boardService;

    @Test
    void initialiseBoard() {
        when(boardRepository.save(any(Board.class))).then(new Answer<Board>() {
            @Override
            public Board answer(InvocationOnMock invocation) throws Throwable {
                Board board = (Board) invocation.getArguments()[0];
                return board;
            }
        });

        Board board = boardService.initialiseBoard(14, 6, 6);
        int[] expectedBoardState = {6,6,6,6,6,6,0,6,6,6,6,6,6,0};
        assertEquals(Arrays.toString(expectedBoardState), board.getBoardState());
    }
}
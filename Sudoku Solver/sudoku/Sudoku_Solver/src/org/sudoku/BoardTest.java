package org.sudoku;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void init_board() {
    }

    @Test
    void deepCopy() {
        Board board = Board.makeTestBoard1();
        Board board2 = board.deepCopy();

//        System.out.println(board);
//        System.out.println(board2);
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                Cell c1 = board.board_cells[i][j];
                Cell c2 = board2.board_cells[i][j];
                assert c1.equals(c2);
            }
        }
        assertEquals(board.toString(), board2.toString());
    }

    @Test
    void getCellValue() {
    }

    @Test
    void isCellNull() {
    }

    @Test
    void setCellValue() {
    }

    @Test
    void makeBoard() {
    }

    @Test
    void testToString() {
    }

    @Test
    void validate_cell() {
    }

    @Test
    void validate_board() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void narrowCellCandidates() {
    }

    @Test
    void narrowBoardCandidates() {
    }

    @Test
    void boardWithNarrowedCandidates() {
    }

    @Test
    void fullyNarrowCandidates() {
    }

    @Test
    void boardWithFullyNarrowedCandidates() {
    }

    @Test
    void isCompletelySolved() {
    }

    @Test
    void solveBoard() {
    }

    @Test
    void makeTestBoard1() {
    }
}
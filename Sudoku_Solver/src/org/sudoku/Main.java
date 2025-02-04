package org.sudoku;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        Board board = new Board();
        board.makeBoard();
        // Board board = Board.makeTestBoard6();
        System.out.println("Original board: " + "\n" + board + "\n");
        System.out.println("Board is valid: " + board.validate_board() + "\n");
        // Board board = Board.makeTestBoard2();
        Board solvedBoard = board.deepCopy();
        solvedBoard = solvedBoard.solveBoard();
        System.out.println("Solved board: " + "\n" + solvedBoard + "\n");
        System.out.println("Board is completely solved: " + solvedBoard.isCompletelySolved());
    }
}
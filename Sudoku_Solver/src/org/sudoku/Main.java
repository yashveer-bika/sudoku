package org.sudoku;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // Board board = new Board();
        // board.makeBoard();
        Board board = Board.makeTestBoard6();
        System.out.println(board);

        /* ArrayList<Cell> adjacentCells = board.getAdjacentCells(board.getCell(0, 0));
        for (Cell cell: adjacentCells) {
            System.out.println("row: " + cell.getRow() + ", col: " + cell.getCol() + ", value: " + cell.getValue() + "\n");
        }

         */

        Board solvedBoard = board.deepCopy();
        solvedBoard = solvedBoard.solveBoard();
        System.out.println(solvedBoard);
        System.out.println("Board is completely solved: " + solvedBoard.isCompletelySolved());

        /*
        Board board2 = Board.makeTestBoard6();
        board2.solveBoard();
        System.out.println("TestBoard7 solution = TestBoard6 solution: " + board.equals(board2));
        */
    }
}
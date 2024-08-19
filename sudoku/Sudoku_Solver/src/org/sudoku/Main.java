package org.sudoku;

public class Main {

    public static void main(String[] args) {
        /* Board board = new Board();
        System.out.println(board);
        board.makeBoard();
        System.out.println(board);
         */
        Board board = Board.makeTestBoard7();

        System.out.println(board);
        System.out.println(Board.original_board.equals(board));

        // Board solvedBoard = board.deepCopy();
        if (board != null) {
            board = board.solveBoard();
            System.out.println(board);
            System.out.println("Board is completely solved: " + board.isCompletelySolved());
        } else {
            System.out.println("Original board is null");
        }

        Board board2 = Board.makeTestBoard6();
        board2.solveBoard();
        System.out.println("TestBoard7 solution = TestBoard6 solution: " + board.equals(board2));

    }
}
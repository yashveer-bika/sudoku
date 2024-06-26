package org.sudoku;

public class Main {

    public static void main(String[] args) {
        /* Board board = new Board();
        System.out.println(board);
        board.makeBoard();
        System.out.println(board);
         */
        Board board = Board.makeTestBoard1();
        // System.out.println(board);
        Board board2 = board.deepCopy();
        // Board board2 = board.boardWithNarrowedCandidates();
        System.out.println(board==board2); // should be false since distinct objects
        System.out.println(board.equals(board2)); // should be true
        board2 = board2.boardWithNarrowedCandidates();
        // System.out.println(board);
        // System.out.println(board2);
        System.out.println(board==board2); // should be false since distinct objects
        System.out.println(board.equals(board2)); // should be false since the board_cells fields shouldn't be equal
        /* Board board3 = board.boardWithFullyNarrowedCandidates();
        System.out.println(board);
        System.out.println(board==board3);

         */
        /*
        for (int i=0; i<9; i++) {
            System.out.println(board.board[4][5].getPossibleValues()[i] + " ");
        }
        System.out.println("Is null: " + board.board[4][5].getIsNull());
        System.out.println("Num possible values: " + board.board[4][5].getNumPossibleValues() + "\n");
        System.out.println("Validated board: " + board.validate_board());
        System.out.println(board);
        // board.narrowCellCandidates(board.board[4][5]);
        // System.out.println(board);
        // board = board.boardWithFullyNarrowedCandidates();
        // System.out.println(board);


         */

    }
}
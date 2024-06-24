// import java.lang.String;
import java.util.Queue;
import java.util.Scanner;


public class Board {
    Cell[][] board = new Cell[9][9];
    static int num_solved_boards = 0;
    static Queue<Board> boards_to_test;
    // static Queue<Board> solved_boards;
    Board solved_board;
    boolean is_original = true; // if this board represents the original inputted board

    public void init_board() {
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                board[i][j] = new Cell(i, j, -1);
            }
        }
    }

    public Board() {
        init_board();
    }

    public int getCellValue(int row, int col) {
        return board[row][col].getValue();
    }

    /* boolean isValueInRange1To9(int value) {
        if (value >= 1 && value <= 9) {
            return true;
        } else return false;
    }
    boolean isCellInRange1To9(int row, int col) {
        return isValueInRange1To9(getCellValue(row, col));
    }
    */

    public boolean isCellNull(int row, int col) {
        return board[row][col].getIsNull();
    }

    public void setCellValue(int row, int col, int new_value) {
        if (new_value >= 1 && new_value <= 9) {
            board[row][col].setValue(new_value);
        }
    }

    public void makeBoard() {
        for (int row=0; row<9; row++) {
            int curr_col = 0;
            Scanner row_reader = new Scanner(System.in);
            System.out.println("Enter the digits separated by a space (for blank cells, input value not in the " +
                    "range [1,9] inclusive, " + "from left to right for row number " + (row+1) + ": ");
            while (curr_col < 9) {
                setCellValue(row, curr_col, row_reader.nextInt());
                curr_col++;
            }
        }
    }

    public String toString() {
        String board_string = "";
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                if (getCellValue(i,j) >= 1 && getCellValue(i,j) <= 9) {
                    board_string += getCellValue(i,j) + " ";
                }
                else {
                    board_string += "X ";
                }
                if ((j+1) % 3 == 0 && j != 8) {
                    board_string += "|| "; // using this to separate the columns of the 9 3x3 boxes, but not at the rightmost edge of the board
                }
            }
            board_string += '\n'; // newline to indicate new row
            if ((i+1) % 3 == 0 && i != 8) {
                board_string += "------------------------------" + '\n';
                // using this to separate the rows of the 9 3x3 boxes, but not at the bottom of the board
            }
        }
        return board_string;
    }
    public boolean validate_cell(Cell cell) {
        if (cell.getIsNull()) {
            return true; // don't want to worry about validating unfilled cells
        }
        for (int k = 0; k < 9; k++) {
            if (k != cell.getRow() && cell.getValue() == board[k][cell.getCol()].getValue()) {
                return false;
            }
            if (k != cell.getCol() && cell.getValue() == board[cell.getRow()][k].getValue()) {
                return false;
            }
        }
        // now to validate the cell box-wise
        int boxRowWise = (int) (cell.getRow() / 3); // 0 if the first of 3 in a row of boxes, 1 if second, or 2 if third
        int boxColWise = (int) (cell.getCol() / 3); // 0 if first of 3 in a column of boxes, 1 if second, or 2 if third
        for (int i=3*boxRowWise; i < 3*boxRowWise + 3; i++) {
            for (int j=3*boxColWise; j < 3*boxColWise + 3; j++) { // checking all cells in the 3x3 box of the subject cell
                if (i != cell.getRow() && j != cell.getCol() && board[i][j].getValue() == cell.getValue()) {
                    return false; // this returns false if any cells in the same 3x3 box as the subject cell
                    // have the same value
                }
            }
        }
        return true;
    }

    public boolean validate_board() {
        for (int i=0; i<9; i++) {
            for (int j = 0; j < 9; j++) { // first 3x3 box
                if (!validate_cell(board[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean equals(Board board2) {
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                if (this.board[i][j].getValue() != board2.board[i][j].getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void narrowCellCandidates(Cell cell) {
        if (!(cell.getIsNull())) {
            return; // don't want to focus on candidates of already solved or prefilled cells
        }
        for (int k=0; k<9; k++) {
            if (k != cell.getCol()) {
                cell.removePossibleValue(board[cell.getRow()][k].getValue());
            }
            if (k != cell.getRow()) {
                cell.removePossibleValue(board[k][cell.getCol()].getValue());
            }
        }
        // narrowing candidates box-wise
        int boxRowWise = (int) (cell.getRow() / 3);
        int boxColWise = (int) (cell.getCol() / 3);
        for (int i=3*boxRowWise; i < 3*boxRowWise + 3; i++) {
            for (int j=3*boxColWise; j<3*boxColWise + 3; j++) {
                if (!(i == cell.getRow() && j == cell.getCol())) {
                    cell.removePossibleValue(board[i][j].getValue());
                }
            }
        }
        // removePossibleValues() also takes care of setting a cell to a value if it has only 1 possible value
    }

    public void narrowBoardCandidates() {
        if (!validate_board()) return;
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                narrowCellCandidates(board[i][j]);
            }
        }
    }

    public Board boardWithNarrowedCandidates() {
        if (!validate_board()) return null;
        Board narrowedBoard = this;
        narrowedBoard.narrowBoardCandidates();
        return narrowedBoard;
    }
    // this seems to be working because when I input the test board from Saturday 6/22 2:00 PM screenshot,
    // it correctly adds 9 to R2C4, 6 to R2C9, 7 to R3C3, 5 to R3C6, 8 to R3C8, 4 to R4C1, 6 to R4C2, 3 to R4C5,
    // 5 to R5C4, 2 to R7C3 (since 7 is previously added to R3C3), 1 to R7C5, 6 to R7C6, 9 to R8C6, 4 to R9C3,
    // 8 to R9C7 and 1 to R9C8

    public void fullyNarrowCandidates() {
        if (this == null) return;
        Board board2 = this.boardWithNarrowedCandidates();
        while (this != board2) { //TODO this part doesn't seem to be working properly,
            // cause when I call a narrowing candidate method again, it narrows more candidates
            // for example, in 5th row 6th column, you can remove a 9 from possible values and only end up with 7
            // and if I call another narrowing method, it does indeed remove the 9 from possible values of board[4][5]
            // and sets its value to 7
            // I can call candidate-narrowing methods multiple times and it can still narrow candidates each time
            this.narrowBoardCandidates();
            board2.narrowBoardCandidates();
        }
        // this.narrowBoardCandidates();
    }

    public Board boardWithFullyNarrowedCandidates() {
        Board board2 = this;
        board2.fullyNarrowCandidates();

        return board2;
    }

    public boolean isCompletelySolved() {
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                if (this.board[i][j].getIsNull() == true) {
                    return false;
                }
            }
        }
        return true;
    }

    public Board solveBoard() {
        if (this.validate_board() == false && this.is_original == true) {
            System.out.println("Invalid original board");
            return null;

        } else if (this.validate_board() == false) {
            if (!(boards_to_test.isEmpty())) {
                boards_to_test.poll().solveBoard();
                //TODO ask do I return this previous line or just call the function?
            }
        } else {
            if (this.isCompletelySolved()) {
                if (num_solved_boards == 0) {
                    System.out.println("Board already completely solved");
                    return this;
                } else {
                    num_solved_boards++;
                    solved_board = this;
                    if (num_solved_boards > 1) {
                        System.out.println("Multiple possible solutions found. Here is one: \n" + solved_board);
                        return this;
                    }
                }
            }
            boards_to_test.add(this);
            while (!(boards_to_test.isEmpty())) { // TODO do we need to add a "num_solved_boards < 2" condition here?

                Board new_guessed_board = boards_to_test.poll();
                new_guessed_board.fullyNarrowCandidates();

                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (new_guessed_board.isCellNull(i, j)) {
                            new_guessed_board.setCellValue(i, j, new_guessed_board.board[i][j].getPossibleValues()[0]); // new board with a guessed value filled in
                            new_guessed_board.solveBoard(); //TODO does this make the original call of solveBoard return something?
                        }
                    }
                }

                if (num_solved_boards > 1) {
                    System.out.println("Multiple possible solutions found. Here is one: \n " + solved_board);
                    return null;
                }

            }
        }
        if (this.num_solved_boards == 0) {
            System.out.println("No possible solutions found");
            return null;
        }
        System.out.println("Solved board: \n" + solved_board);
        return solved_board;
        // we already took care of the case where we have more than 1 possible solution inside the while loop

    }

    public static Board makeTestBoard1() {
        Board test_board_1 = new Board();
        test_board_1.setCellValue(0, 1, 2);
        test_board_1.setCellValue(1, 0, 1);
        test_board_1.setCellValue(1, 1, 5);
        test_board_1.setCellValue(1, 2, 8);
        test_board_1.setCellValue(1, 7, 3);
        test_board_1.setCellValue(2, 0, 3);
        test_board_1.setCellValue(2, 1, 4);
        test_board_1.setCellValue(2, 3, 1);
        test_board_1.setCellValue(2, 4, 6);
        test_board_1.setCellValue(2, 6, 9);
        test_board_1.setCellValue(2, 8, 2);
        test_board_1.setCellValue(3, 2, 9);
        test_board_1.setCellValue(3, 3, 2);
        test_board_1.setCellValue(3, 5, 8);
        test_board_1.setCellValue(3, 6, 1);
        test_board_1.setCellValue(3, 7, 7);
        test_board_1.setCellValue(3, 8, 5);
        test_board_1.setCellValue(4, 4, 4);
        test_board_1.setCellValue(5, 1, 3);
        test_board_1.setCellValue(5, 2, 5);
        test_board_1.setCellValue(5, 3, 6);
        test_board_1.setCellValue(5, 5, 1);
        test_board_1.setCellValue(6, 3, 3);
        test_board_1.setCellValue(6, 6, 5);
        test_board_1.setCellValue(6, 7, 9);
        test_board_1.setCellValue(6, 8, 4);
        test_board_1.setCellValue(7, 0, 5);
        test_board_1.setCellValue(7, 1, 1);
        test_board_1.setCellValue(7, 2, 3);
        test_board_1.setCellValue(7, 3, 4);
        test_board_1.setCellValue(7, 4, 8);
        test_board_1.setCellValue(7, 8, 7);
        test_board_1.setCellValue(8, 0, 6);
        test_board_1.setCellValue(8, 1, 9);
        test_board_1.setCellValue(8, 3, 7);
        test_board_1.setCellValue(8, 4, 5);
        test_board_1.setCellValue(8, 5, 2);
        test_board_1.setCellValue(8, 8, 3);
        return test_board_1;
    }

    /* public Board solveBoard() {
        if (this.validate_board() == false) {
            return null;
        }
        Board solvedBoard = this;

        while (num_solved_boards < 2) {
            // TODO: make solvedBoard equal to the solved board of the original board, or null if not solvable
            // remember that if there is one empty cell in a row, it must be the last remaining digit. same for columns / boxes
            // remember that if n cells in the same row occupy the same n values, then those n values cannot be in any other cell in the row; same for columns / boxes
            // maybe I can create a cell class to store the digit value of a cell or NULL if it is not filled in, as well as possible values if it is NULL
            // I can preemptively narrow things down
            // can write search algorithm on my board with backtracking, IE trying to solve values directly first
            // and then guessing and checking other values
            // Yashu recommends making a representation of a board that is a graph where each cell is a node
            // and all of its neighbors are its neighboring cells
            if (solvedBoard.validate_board() == false) {
                return null;
            } else return solvedBoard;
        }
        if (num_solved_boards == 0) {
            System.out.println("No possible solutions");
        }
        if (num_solved_boards > 1) {
            System.out.println("Multiple possible solutions");
        }

    } */



}

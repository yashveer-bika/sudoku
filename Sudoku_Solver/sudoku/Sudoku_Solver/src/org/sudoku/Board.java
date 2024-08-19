package org.sudoku;


import java.util.Scanner;


public class Board {
    Cell[][] board_cells = new Cell[9][9];
    public boolean is_valid = true;
    static Board original_board = null;
    static Board solved_board = null; // used to represent the solved board if there is only 1 possible, or a solved board if another solution is found

    public void init_board() {
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                board_cells[i][j] = new Cell(i, j, -1);
            }
        }
    }

    public Board() {
        init_board();
        if (original_board == null) {
            original_board = this;
        }
        // original_board = this;
    }

    // public Board deepCopy() throws CloneNotSupportedException {
    public Board deepCopy() {
        Board new_board = new Board();
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                new_board.board_cells[i][j] = new Cell(this.board_cells[i][j]);
            }
        }
        return new_board;
    }

    public int getCellValue(int row, int col) {
        return board_cells[row][col].getValue();
    }

    public boolean isCellSolved(int row, int col) {
        return board_cells[row][col].getIsSolved();
    }

    public void setCellValue(int row, int col, int new_value) {
        if (new_value >= 1 && new_value <= 9) {
            board_cells[row][col].setValue(new_value);
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
        if (!(cell.getIsSolved())) {
            return true; // don't want to worry about validating unfilled cells
        }
        for (int k = 0; k < 9; k++) {
            if (k != cell.getRow() && cell.getValue() == board_cells[k][cell.getCol()].getValue()) {
                return false;
            }
            if (k != cell.getCol() && cell.getValue() == board_cells[cell.getRow()][k].getValue()) {
                return false;
            }
        }
        // now to validate the cell box-wise
        int boxRowWise = (int) (cell.getRow() / 3); // 0 if the first of 3 in a row of boxes, 1 if second, or 2 if third
        int boxColWise = (int) (cell.getCol() / 3); // 0 if first of 3 in a column of boxes, 1 if second, or 2 if third
        for (int i=3*boxRowWise; i < 3*boxRowWise + 3; i++) {
            for (int j=3*boxColWise; j < 3*boxColWise + 3; j++) { // checking all cells in the 3x3 box of the subject cell
                if (i != cell.getRow() && j != cell.getCol() && board_cells[i][j].getValue() == cell.getValue()) {
                    return false; // this returns false if any cells in the same 3x3 box as the subject cell have the same value
                }
            }
        }
        return true;
    }

    public int numCellsWithPossibleValueInRow(int row, int target_value) { // returns # of cells w/ target_value as a possible value in the specified row of board_cells
        if (!(target_value >= 1 && target_value <= 9)) return 0;
        int num_candidate_cells = 0;
        for (int i=0; i<9; i++) {
            if (board_cells[row][i].containsPossibleValue(target_value)) {
                num_candidate_cells += 1;
            }
        }

        return num_candidate_cells; // number of cells in a row with target_value as a possible value
    }

    public boolean samePossibleValueInRow(Cell cell, int target_value) { // returns true if another cell has a possible value of target_value in the same row as the target cell
        if (!(target_value >= 1 && target_value <= 9)) return false;
        if (cell.getValue() == target_value) return false;
        if (numCellsWithPossibleValueInRow(cell.getRow(), target_value) == 1) { // this cell is the only one in its row with target_value as a possible value
            return false;
        }
        return true;
    }

    public int numCellsWithPossibleValueInCol(int col, int target_value) { // returns # of cells w/ target_value as a possible value in the specified column of board_cells
        if (!(target_value >= 1 && target_value <= 9)) return 0;
        int num_candidate_cells = 0;
        for (int i=0; i<9; i++) {
            if (board_cells[i][col].containsPossibleValue(target_value)) {
                num_candidate_cells += 1;
            }
        }

        return num_candidate_cells;
    }

    public boolean samePossibleValueInCol(Cell cell, int target_value) { // returns true if another cell has a possible value of target_value in the same col as the target cell
        if (!(target_value >= 1 && target_value <= 9)) return false;
        // if (!(cell.containsPossibleValue(target_value))) return false;
        if (cell.getValue() == target_value) return false;
        // now we know that cell.possibleValues[value-1] == value past this point
        if (numCellsWithPossibleValueInCol(cell.getCol(), target_value) == 1) { // this cell is the only one in its column with target_value as a possible value
            return false;
        }
        return true;
    }

    public int numCellsWithPossibleValueInCellBox(Cell cell, int target_value) { // # of cells in the same box of board_cells as the given cell w/ target_value as a possible value
        if (!(target_value >= 1 && target_value <= 9)) return 0;
        if (cell.getValue() == target_value) return 1;
        int num_candidate_cells = 0;
        int box_start_row = 3*((int)(cell.getRow() / 3));
        int box_start_col = 3*((int)(cell.getCol() / 3));
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (board_cells[box_start_row+i][box_start_col+j].containsPossibleValue(target_value)) {
                    num_candidate_cells += 1;
                }
            }
        }
        return num_candidate_cells;
    }

    public boolean samePossibleValueInCellBox(Cell cell, int target_value) { // returns true if another cell has a possible value of target_value in the same box as the target cell
        if (!(target_value >= 1 && target_value <= 9)) return false;
        // if (!(cell.containsPossibleValue(target_value))) return false;
        if (cell.getValue() == target_value) return false;
        // now we know that cell.possibleValues[value-1] == value past this point
        if (numCellsWithPossibleValueInCellBox(cell, target_value) == 1) {
            return false;
        }
        return true;
    }

    public int UniquePossibleValue(Cell cell) { // gets a value if the target cell is the only cell in its row, column, or box that has that as a possible value
        if (cell.getIsSolved()) return cell.getValue();
        for (int i=0; i<9; i++) {
            if (!(cell.possibleValues[i] >= 1 && cell.possibleValues[i] <= 9)) continue;
            if ((!(samePossibleValueInRow(cell, cell.possibleValues[i]))) ||
                    (!(samePossibleValueInCol(cell, cell.possibleValues[i])))
                    || (!(samePossibleValueInCellBox(cell, cell.possibleValues[i])))) {
                return cell.possibleValues[i];
            }
        }
        return -1;
    }

    public void setPossibleValueIfUnique(Cell cell) { // set a cell to a value if
        // it is the only cell in a row, column, or box that can have a specific value
        // returns true if a value was set this way or false otherwise
        if (cell.getIsSolved()) return; // this cell is already filled in
        if (UniquePossibleValue(cell) != -1) { // this cell has a possible value that is unique to its row, column, and/or box
            cell.setValue(UniquePossibleValue(cell));
        }
        // if a cell didn't have a unique possible value set this way, it doesn't have a unique possible value for its row, column, or box, so do nothing
    }

    public boolean validate_board() {
        for (int i=0; i<9; i++) {
            for (int j = 0; j < 9; j++) { // first 3x3 box
                if (!validate_cell(board_cells[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean equals(Board board2) {
        if (board2 == null) {
            if (this == null) return true;
            else return false;
        }
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                if (!(this.board_cells[i][j].equals(board2.board_cells[i][j]))) return false;
            }
        }
        return true;
    }

    public void narrowCellCandidates(Cell cell) {
        if (cell.getIsSolved()) {
            return;
        }

        for (int k=0; k<9; k++) {
            if (k != cell.getCol()) {
                cell.removePossibleValue(board_cells[cell.getRow()][k].getValue());
            }
            if (k != cell.getRow()) {
                cell.removePossibleValue(board_cells[k][cell.getCol()].getValue());
            }
        }
        // narrowing candidates box-wise
        int boxRowWise = (int) (cell.getRow() / 3);
        int boxColWise = (int) (cell.getCol() / 3);
        for (int i=3*boxRowWise; i < 3*boxRowWise + 3; i++) {
            for (int j=3*boxColWise; j<3*boxColWise + 3; j++) {
                if (!(i == cell.getRow() && j == cell.getCol())) {
                    cell.removePossibleValue(board_cells[i][j].getValue());
                }
            }
        }
        setPossibleValueIfUnique(cell); // excluding this line can make the test boards 2 to 3 not fully solved
        // removePossibleValues() also takes care of setting a cell to a value if it has only 1 possible value

    }

    public void narrowBoardCandidates() {
        if (!validate_board()) return;
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                narrowCellCandidates(board_cells[i][j]);
            }
        }
    }


    public Board boardWithNarrowedCandidates() { // used if I want to have a separate Board object representing an original Board object with narrowBoardCandidates() applied
        if (!validate_board()) return null;
        Board narrowedBoard = this.deepCopy();
        narrowedBoard.narrowBoardCandidates();
        return narrowedBoard;
    }


    public void fullyNarrowCandidates() {
        if (this == null) return;
        if (!(this.validate_board())) return;
        Board board2 = this.deepCopy().boardWithNarrowedCandidates();
        while (!(this.equals(board2))) {
            if (!(this.equals(this.boardWithNarrowedCandidates()))) {
                this.narrowBoardCandidates();
            } else break;
            board2.narrowBoardCandidates();
        }
    }


    public Board boardWithFullyNarrowedCandidates() { // used if I want to have a separate Board object representing an original Board object with fullyNarrowCandidates() applied
        Board board2 = this.deepCopy();
        board2.fullyNarrowCandidates();
        return board2;
    }

    public boolean isCompletelySolved() {
        for (int i=0; i<9; i++) {
            for (int j=0; j<9; j++) {
                if (!(this.board_cells[i][j].getIsSolved())) {
                    return false;
                }
            }
        }
        return true;
    }

    public int[] firstUnsolvedCell() {
        int[] unsolvedCellIndices = new int[2];
        unsolvedCellIndices[0] = -1;
        unsolvedCellIndices[1] = -1;
        if (this.isCompletelySolved()) return unsolvedCellIndices;
        else {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (!(board_cells[i][j].getIsSolved())) {
                        unsolvedCellIndices[0] = i;
                        unsolvedCellIndices[1] = j;
                        return unsolvedCellIndices;
                    }
                }
            }
        }
        return unsolvedCellIndices;
    }

    public void setFirstPossibleGuess() {
        board_cells[firstUnsolvedCell()[0]][firstUnsolvedCell()[1]].setFirstPossibleValue();
    }

    public Board solveBoard() {
        if (!(original_board.validate_board())) {
            System.out.println("Invalid original board\n");
            return null;
        }
        if (!(this.validate_board())) {
            return null;
        }
        if (original_board.isCompletelySolved()) {
            System.out.println("Board already completely solved!\n");
            return original_board;
        }

        this.fullyNarrowCandidates();
        if (!(this.validate_board())) return null;
        if (this.isCompletelySolved()) {
            if (solved_board != null && !(this.equals(solved_board))) {
                System.out.println("Multiple possible solutions found. Here is one:\n" + this.toString());
            } else {
                solved_board = this;
            }
            return solved_board;
        }
        else {
            Board newBoard = this.deepCopy();
            int[] firstEmptyCellIndices = this.firstUnsolvedCell();
            int firstPossibleGuess = newBoard.board_cells[firstEmptyCellIndices[0]][firstEmptyCellIndices[1]].getFirstPossibleValue();

            newBoard.setFirstPossibleGuess();
            if (newBoard.solveBoard() == null) {
                this.board_cells[firstEmptyCellIndices[0]][firstEmptyCellIndices[1]].removePossibleValue(firstPossibleGuess);
                return this.solveBoard();
            } else return newBoard.solveBoard();

        }
    }


    public static Board makeTestBoard1() { // Easy difficulty in Sudoku.com app
        Board test_board = new Board();
        test_board.setCellValue(0, 1, 2);
        test_board.setCellValue(1, 0, 1);
        test_board.setCellValue(1, 1, 5);
        test_board.setCellValue(1, 2, 8);
        test_board.setCellValue(1, 7, 3);
        test_board.setCellValue(2, 0, 3);
        test_board.setCellValue(2, 1, 4);
        test_board.setCellValue(2, 3, 1);
        test_board.setCellValue(2, 4, 6);
        test_board.setCellValue(2, 6, 9);
        test_board.setCellValue(2, 8, 2);
        test_board.setCellValue(3, 2, 9);
        test_board.setCellValue(3, 3, 2);
        test_board.setCellValue(3, 5, 8);
        test_board.setCellValue(3, 6, 1);
        test_board.setCellValue(3, 7, 7);
        test_board.setCellValue(3, 8, 5);
        test_board.setCellValue(4, 4, 4);
        test_board.setCellValue(5, 1, 3);
        test_board.setCellValue(5, 2, 5);
        test_board.setCellValue(5, 3, 6);
        test_board.setCellValue(5, 5, 1);
        test_board.setCellValue(6, 3, 3);
        test_board.setCellValue(6, 6, 5);
        test_board.setCellValue(6, 7, 9);
        test_board.setCellValue(6, 8, 4);
        test_board.setCellValue(7, 0, 5);
        test_board.setCellValue(7, 1, 1);
        test_board.setCellValue(7, 2, 3);
        test_board.setCellValue(7, 3, 4);
        test_board.setCellValue(7, 4, 8);
        test_board.setCellValue(7, 8, 7);
        test_board.setCellValue(8, 0, 6);
        test_board.setCellValue(8, 1, 9);
        test_board.setCellValue(8, 3, 7);
        test_board.setCellValue(8, 4, 5);
        test_board.setCellValue(8, 5, 2);
        test_board.setCellValue(8, 8, 3);
        return test_board;
    }

    public static Board makeTestBoard2() { // Expert difficulty in Sudoku.com app
        Board test_board = new Board();
        test_board.setCellValue(0, 2, 8);
        test_board.setCellValue(0, 5, 5);
        test_board.setCellValue(0, 6, 3);
        test_board.setCellValue(1, 2, 6);
        test_board.setCellValue(1, 5, 2);
        test_board.setCellValue(1, 8, 7);
        test_board.setCellValue(2, 4, 9);
        test_board.setCellValue(2, 7, 8);
        test_board.setCellValue(3, 1, 4);
        test_board.setCellValue(4, 2, 1);
        test_board.setCellValue(4, 3, 9);
        test_board.setCellValue(4, 4, 8);
        test_board.setCellValue(5, 0, 8);
        test_board.setCellValue(5, 3, 6);
        test_board.setCellValue(5, 5, 3);
        test_board.setCellValue(5, 7, 4);
        test_board.setCellValue(6, 2, 5);
        test_board.setCellValue(6, 4, 2);
        test_board.setCellValue(6, 7, 6);
        test_board.setCellValue(7, 0, 9);
        test_board.setCellValue(7, 3, 3);
        test_board.setCellValue(7, 6, 4);
        test_board.setCellValue(7, 8, 5);
        test_board.setCellValue(8, 1, 7);
        test_board.setCellValue(8, 6, 9);
        return test_board;
    }

    public static Board makeTestBoard3() { // Master difficulty in Sudoku.com app
        Board test_board = new Board();
        test_board.setCellValue(0, 1, 2);
        test_board.setCellValue(0, 2, 4);
        test_board.setCellValue(0, 8, 7);
        test_board.setCellValue(1, 1, 6);
        test_board.setCellValue(1, 2, 7);
        test_board.setCellValue(1, 6, 3);
        test_board.setCellValue(1, 8, 2);
        test_board.setCellValue(2, 4, 4);
        test_board.setCellValue(2, 6, 5);
        test_board.setCellValue(3, 2, 6);
        test_board.setCellValue(3, 4, 3);
        test_board.setCellValue(3, 6, 8);
        test_board.setCellValue(3, 7, 5);
        test_board.setCellValue(4, 0, 4);
        test_board.setCellValue(4, 2, 5);
        test_board.setCellValue(4, 4, 8);
        test_board.setCellValue(4, 7, 2);
        test_board.setCellValue(5, 7, 7);
        test_board.setCellValue(6, 0, 7);
        test_board.setCellValue(6, 3, 3);
        test_board.setCellValue(6, 5, 2);
        test_board.setCellValue(7, 1, 1);
        test_board.setCellValue(8, 2, 8);
        test_board.setCellValue(8, 5, 4);
        test_board.setCellValue(8, 8, 9);
        return test_board;
    }

    public static Board makeTestBoard4() { // Master difficulty on Sudoku.com app
        Board test_board = new Board();
        test_board.setCellValue(0, 4, 6);
        test_board.setCellValue(0, 7, 1);
        test_board.setCellValue(1, 1, 1);
        test_board.setCellValue(1, 6, 7);
        test_board.setCellValue(1, 7, 8);
        test_board.setCellValue(2, 0, 6);
        test_board.setCellValue(2, 2, 3);
        test_board.setCellValue(2, 4, 9);
        test_board.setCellValue(3, 0, 7);
        test_board.setCellValue(3, 1, 6);
        test_board.setCellValue(4, 7, 6);
        test_board.setCellValue(5, 4, 8);
        test_board.setCellValue(5, 6, 9);
        test_board.setCellValue(5, 7, 7);
        test_board.setCellValue(5, 8, 4);
        test_board.setCellValue(6, 0, 8);
        test_board.setCellValue(6, 2, 1);
        test_board.setCellValue(6, 3, 9);
        test_board.setCellValue(6, 4, 2);
        test_board.setCellValue(6, 6, 5);
        test_board.setCellValue(7, 1, 5);
        test_board.setCellValue(7, 4, 7);
        test_board.setCellValue(7, 5, 3);
        test_board.setCellValue(8, 1, 4);
        test_board.setCellValue(8, 5, 1);
        return test_board;
    }

    public static Board makeTestBoard5() { // Extreme difficulty in Sudoku.com app
        Board test_board = new Board();
        test_board.setCellValue(0, 0, 1);
        test_board.setCellValue(0, 5, 8);
        test_board.setCellValue(1, 1, 5);
        test_board.setCellValue(1, 4, 4);
        test_board.setCellValue(2, 1, 7);
        test_board.setCellValue(2, 7, 8);
        test_board.setCellValue(2, 8, 2);
        test_board.setCellValue(3, 3, 5);
        test_board.setCellValue(3, 6, 7);
        test_board.setCellValue(3, 8, 1);
        test_board.setCellValue(4, 3, 1);
        test_board.setCellValue(4, 8, 3);
        test_board.setCellValue(5, 3, 9);
        test_board.setCellValue(5, 4, 6);
        test_board.setCellValue(5, 5, 4);
        test_board.setCellValue(6, 0, 6);
        test_board.setCellValue(6, 2, 9);
        test_board.setCellValue(6, 7, 1);
        test_board.setCellValue(7, 2, 4);
        test_board.setCellValue(7, 5, 5);
        test_board.setCellValue(7, 6, 9);
        test_board.setCellValue(8, 6, 6);
        return test_board;
    }

    public static Board makeTestBoard6() { // Expert difficulty on Sudoku2Pro App
        Board test_board = new Board();
        test_board.setCellValue(0, 1, 6);
        test_board.setCellValue(0, 6, 4);
        test_board.setCellValue(1, 2, 8);
        test_board.setCellValue(1, 4, 1);
        test_board.setCellValue(1, 7, 2);
        test_board.setCellValue(2, 0, 1);
        test_board.setCellValue(2, 2, 4);
        test_board.setCellValue(2, 6, 8);
        test_board.setCellValue(2, 7, 9);
        test_board.setCellValue(2, 8, 7);
        test_board.setCellValue(3, 0, 4);
        test_board.setCellValue(3, 2, 2);
        test_board.setCellValue(3, 5, 6);
        test_board.setCellValue(3, 6, 1);
        test_board.setCellValue(3, 8, 9);
        test_board.setCellValue(5, 5, 1);
        test_board.setCellValue(5, 6, 2);
        test_board.setCellValue(6, 1, 8);
        test_board.setCellValue(6, 4, 2);
        test_board.setCellValue(6, 5, 7);
        test_board.setCellValue(6, 6, 9);
        test_board.setCellValue(7, 3, 8);
        test_board.setCellValue(7, 4, 6);

        test_board.setCellValue(8, 0, 5);
        test_board.setCellValue(8, 3, 4);
        test_board.setCellValue(8, 5, 9);
        test_board.setCellValue(8, 8, 8);
        return test_board;
    }

    public static Board makeTestBoard7() { // Modified from test board 6 so it can have multiple solutions
        Board test_board_6 = new Board();
        test_board_6.setCellValue(2, 0, 1);
        test_board_6.setCellValue(2, 2, 4);
        test_board_6.setCellValue(2, 6, 8);
        test_board_6.setCellValue(2, 7, 9);
        test_board_6.setCellValue(2, 8, 7);
        test_board_6.setCellValue(3, 0, 4);
        test_board_6.setCellValue(3, 2, 2);
        test_board_6.setCellValue(3, 5, 6);
        test_board_6.setCellValue(3, 6, 1);
        test_board_6.setCellValue(3, 8, 9);
        test_board_6.setCellValue(5, 5, 1);
        test_board_6.setCellValue(5, 6, 2);
        test_board_6.setCellValue(6, 1, 8);
        test_board_6.setCellValue(6, 4, 2);
        test_board_6.setCellValue(6, 5, 7);
        test_board_6.setCellValue(6, 6, 9);
        test_board_6.setCellValue(7, 3, 8);
        test_board_6.setCellValue(7, 4, 6);

        test_board_6.setCellValue(8, 0, 5);
        test_board_6.setCellValue(8, 3, 4);
        test_board_6.setCellValue(8, 5, 9);
        test_board_6.setCellValue(8, 8, 8);
        return test_board_6;
    }


}
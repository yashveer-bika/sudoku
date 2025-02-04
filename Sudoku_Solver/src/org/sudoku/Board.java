package org.sudoku;


import java.util.ArrayList;
import java.util.Scanner;


public class Board {
    Cell[][] board_cells = new Cell[9][9];
    // static Board original_board = null;
    // static Board solved_board = null; // used to represent the solved board if there is only 1 possible, or a solved board if another solution is found

    static int dim_min = 0; // index of minimum row / col
    static int dim_max = dim_min + 8; // index of maximum row / col
    public void init_board() {
        for (int row=dim_min; row<=dim_max; row++) {
            for (int col=dim_min; col<=dim_max; col++) {
                board_cells[row][col] = new Cell(row, col, -1);
            }
        }
        // no need to potentially adjust cell indexing strategy in future using getCell
        // since init_board can always be done with this way of indexing regardless
    }

    public Board() {
        init_board();

    }

    public Board deepCopy() {
        Board new_board = new Board();
        for (int row=dim_min; row<=dim_max; row++) {
            for (int col=dim_min; col<=dim_max; col++) {
                new_board.board_cells[row][col] = new Cell(this.getCell(row, col));
            }
        }
        return new_board;
    }

    public void makeBoard() {
        for (int row=dim_min; row<=dim_max; row++) {
            int curr_col = dim_min;
            Scanner row_reader = new Scanner(System.in);
            System.out.println("Enter the digits separated by a space (for blank cells, input value not in the " +
                    "range [1,9] inclusive, " + "from left to right for row number " + (row+1) + ": ");
            while (curr_col <= dim_max) {
                setCellValue(row, curr_col, row_reader.nextInt());
                curr_col++;
            }
        }
    }

    public String toString() {
        String board_string = "";
        for (int row = dim_min; row <= dim_max; row++) {
            for (int col = dim_min; col <= dim_max; col++) {
                if (getCell(row, col).cellValueIsValid()) {
                    board_string += getCellValue(row, col) + " ";
                } else {
                    board_string += "X ";
                }
                if ((col + 1) % 3 == 0 && col != dim_max) {
                    board_string += "|| ";
                    // using this to separate the columns of the 9 3x3 boxes, but not at the rightmost edge of the board
                }
            }
            board_string += '\n'; // newline to indicate new row
            if ((row + 1) % 3 == 0 && row != dim_max) {
                board_string += "------------------------------" + '\n';
                // using this to separate the rows of the 9 3x3 boxes, but not at the bottom of the board
            }
        }
        return board_string;
    }

    public Cell getCell(int row, int col) {
        return board_cells[row][col];
        // this is a function in case someone wants to change
        // the indexing method for calling board_cells[row][col]
        // such as if they want to do it from 1 through 9 instead of 0 through 8
    }

    public int getCellValue(int row, int col) {
        return getCell(row, col).getValue();
    }

    public void setCellValue(int row, int col, int new_value) {
        getCell(row, col).setValue(new_value);
    }

    public ArrayList<Cell> getAdjacentCells(Cell cell) {
        // return an ArrayList of Cells adjacent to the input cell,
        // IE cells in the same row, column, and/or box
        // this can be useful in other methods such as
        // eliminating possible values and checking validity
        ArrayList<Cell> adjacentCells = new ArrayList<Cell>();
        for (int row=dim_min; row<=dim_max; row++) {
            if (row != cell.getRow()) {
                adjacentCells.add(getCell(row, cell.getCol()));
            }
        }
        for (int col=dim_min; col<=dim_max; col++) {
            if (col != cell.getCol()) {
                adjacentCells.add(getCell(cell.getRow(), col));
            }
        }
        int[] boxIndices = cell.getBoxIndices();
        for (int row=boxIndices[0]; row<=boxIndices[1]; row++) {
            for (int col=boxIndices[2]; col<=boxIndices[3]; col++) {
                if (!(row == cell.getRow() && col == cell.getCol()) && !(adjacentCells.contains(getCell(row, col)))) {
                    adjacentCells.add(getCell(row, col));
                }
            }
        }
        return adjacentCells;
    }

    public boolean validate_cell(Cell cell) {
        if (!(cell.getIsSolved())) {
            return true; // don't want to worry about validating unfilled cells
        }
        ArrayList<Cell> adjacentCells = getAdjacentCells(cell);
        for (Cell adjacentCell: adjacentCells) {
            if (adjacentCell.getValue() == cell.getValue()) {
                return false;
            }
        }
        return true;
    }

    // removePossibleValue sets a unique possible value if it exists and narrowCellCandidates checks all adjacent cells
    // for any cell that has a specified actual value to remove from adjacent cells' possibleValues field
    // so no need to check for a unique possible value since removePossibleValue takes care of that

    public boolean validate_board() {
        for (int row=dim_min; row<=dim_max; row++) {
            for (int col = dim_min; col <= dim_max; col++) { // first 3x3 box
                if (!validate_cell(getCell(row, col))) {
                    return false;
                    // the cell is not valid and thus nor is the board
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
        for (int row=dim_min; row<=dim_max; row++) {
            for (int col=dim_min; col<=dim_max; col++) {
                if (!(this.getCell(row, col).equals(board2.getCell(row, col)))) return false;
            }
        }
        return true;
    }

    public void narrowCellCandidates(Cell cell) {
        if (cell.getIsSolved()) {
            return;
        }
        ArrayList<Cell> adjacentCells = getAdjacentCells(cell);
        for (Cell adjacentCell: adjacentCells) {
            cell.removePossibleValue(adjacentCell.getValue());
        }
        // removePossibleValues() also takes care of setting a cell to a value if it has only 1 possible value
    }

    public void narrowBoardCandidates() {
        if (!validate_board()) return;
        for (int row=dim_min; row<=dim_max; row++) {
            for (int col=dim_min; col<=dim_max; col++) {
                narrowCellCandidates(getCell(row, col));
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
            this.narrowBoardCandidates();
            board2.narrowBoardCandidates();
        }
    }

    public boolean isCompletelySolved() {
        for (int row=dim_min; row<=dim_max; row++) {
            for (int col=dim_min; col<=dim_max; col++) {
                if (!(this.getCell(row, col).getIsSolved())) {
                    return false;
                }
            }
        }
        return true;
    }

    public int[] getFirstUnsolvedCellIndices() {
        // the indices of the first unsolved cell, for use in
        // the guessing part of our algorithm
        int[] unsolvedCellIndices = new int[2];
        unsolvedCellIndices[0] = -1;
        unsolvedCellIndices[1] = -1;
        if (this.isCompletelySolved()) return unsolvedCellIndices;
        // so we don't jump into the loop in the first place when the
        // board is completely solved, in order to save computation resources
        else {
            for (int row=dim_min; row<=dim_max; row++) {
                for (int col=dim_min; col <=dim_max; col++) {
                    if (!(getCell(row, col).getIsSolved())) {
                        unsolvedCellIndices[0] = row;
                        unsolvedCellIndices[1] = col;
                        return unsolvedCellIndices;
                    }
                }
            }
        }
        return unsolvedCellIndices;
    }

    public void setFirstPossibleGuess() {
        int[] firstUnsolvedCellIndices = getFirstUnsolvedCellIndices();
        if (firstUnsolvedCellIndices[0] < dim_min || firstUnsolvedCellIndices[0] > dim_max || firstUnsolvedCellIndices[1] < dim_min || firstUnsolvedCellIndices[1] > dim_max) return;
        this.getCell(firstUnsolvedCellIndices[0], firstUnsolvedCellIndices[1]).setFirstPossibleValue();
    }

    public Board solveBoard() {
        if (!(this.validate_board())) {
            System.out.println("Invalid original board\n");
            return null;
        }
        this.fullyNarrowCandidates();
        if (!(this.validate_board())) return null; // this line is to prevent working on invalid boards
        if (this.isCompletelySolved()) {
            return this;
        }
        else {
            Board newBoard = this.deepCopy();
            int[] firstEmptyCellIndices = this.getFirstUnsolvedCellIndices();
            // this works because it is a block corresponding to the board not being completely solved
            int firstPossibleGuess = newBoard.getCell(firstEmptyCellIndices[0], firstEmptyCellIndices[1]).getFirstPossibleValue();

            newBoard.setFirstPossibleGuess();
            if (newBoard.solveBoard() == null) {
                this.getCell(firstEmptyCellIndices[0], firstEmptyCellIndices[1]).removePossibleValue(firstPossibleGuess);
                return this.solveBoard();
            } else return newBoard.solveBoard();

        }
    }

    // ############################################

    // the following methods are to make sample boards
    // for use in testing our code

    // ############################################

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
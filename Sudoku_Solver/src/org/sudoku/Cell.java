package org.sudoku;

import java.util.Arrays;

// public class Cell implements Cloneable {
public class Cell {
    int value;
    int row;
    int col;
    boolean isSolved = false;
    int numPossibleValues;
    int[] possibleValues = new int[9];

    public Cell(int row, int col, int value) {
        this.setValue(value);
        this.row = row;
        this.col = col;

        this.initPossibleValues();
        // this cell is unsolved and we need to initialize possibleValues for it


    }

    public Cell(Cell other) {
        this.setRow(other.getRow());
        this.setCol(other.getCol());
        this.setValue(other.getValue());
        this.setIsSolved(other.getIsSolved());

        this.setNumPossibleValues(other.getNumPossibleValues());
        // System.arraycopy(other.possibleValues, 0, this.possibleValues, 0, 9);
        this.possibleValues = other.possibleValues.clone();

    }
    public boolean equals(Cell cell) {
        return this.value == cell.value
                && this.row == cell.row
                && this.col == cell.col
                && this.isSolved == cell.isSolved
                && this.numPossibleValues == cell.numPossibleValues
                && Arrays.equals(this.possibleValues, cell.possibleValues)
                ;
    }

    public boolean containsPossibleValue(int target_value) {
        if (this.getIsSolved()) {
            if (this.getValue() == target_value) return true;
            else return false;
        }
        if (!(target_value >= 1 && target_value <= 9)) return false;
        if (possibleValues[target_value - 1] == target_value) return true;
        else return false;
    }

    public void initPossibleValues() {
        if (!(this.isSolved)) {
            for (int i = 0; i < 9; i++) {
                this.possibleValues[i] = i + 1;
            }
            this.numPossibleValues = 9;
        }  /* else {
            /* for (int i = 0; i < 9; i++) {
                this.possibleValues[i] = -1;
            }
            this.possibleValues[this.value - 1] = value;
            this.numPossibleValues = 1;


            this.solve(this.value);
            */
    }

    public void removePossibleValue(int value) {
        // if (value >= 1 && value <= 9 && this.possibleValues[value-1] >= 1 && this.possibleValues[value-1] <= 9)
        if (value >= 1 && value <= 9 && this.possibleValues[value-1] == value) {
            this.possibleValues[value-1] = -1;
            this.numPossibleValues -= 1;
        }
        if (this.numPossibleValues == 1) {
            for (int i=0; i<9; i++) {
                if (this.possibleValues[i] == i+1) {
                    this.setValue(i+1);
                }
            }
        }
    }
    public int getValue() {
        return this.value;
    }
    public void setValue(int new_value) {
        this.value = new_value;
        if (new_value >= 1 && new_value <= 9) {

            this.isSolved = true;
            this.numPossibleValues = 1;
            for (int i=0; i<9; i++) {
                this.possibleValues[i] = -1;
            }
            this.possibleValues[this.getValue()-1] = this.getValue();
        }
    }
    public int[] getPossibleValues() {
        return this.possibleValues;
    }
    public int getFirstPossibleValue() {
        for (int i=0; i<9; i++) {
            if (this.getPossibleValues()[i] != -1) {
                return this.getPossibleValues()[i];
            }
        }
        return -1;
    }
    public void setFirstPossibleValue() {
        this.setValue(this.getFirstPossibleValue());
    }
    public boolean getIsSolved() {
        return this.isSolved;
    }
    public int getNumPossibleValues() {
        return this.numPossibleValues;
    }
    public int getRow() {
        return this.row;
    }
    public int getCol() {
        return this.col;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public void setCol(int col) {
        this.col = col;
    }
    public void setIsSolved(boolean isSolved) {
        this.isSolved = isSolved;
    }
    public void setNumPossibleValues(int numPossibleValues) {
        this.numPossibleValues = numPossibleValues;
    }

    public boolean isInSameRow(Cell other) {
        return this.row == other.row;
    }

    public boolean isInSameCol(Cell other) {
        return this.col == other.col;
    }

    public boolean isInSameBox(Cell other) {
        int thisBoxRowWise = 3 * ((int) this.row/3); // if this is in the leftmost, middle, or rightmost box in a row
        int thisBoxColWise = 3 * ((int) this.col / 3); // if this is in the top, middle, or bottom box in a column
        int otherBoxRowWise = 3 * ((int) other.row / 3); // same as for the thisBoxRowWise but for the Cell other
        int otherBoxColWise = 3 * ((int) other.col / 3);
        return (thisBoxRowWise == otherBoxRowWise && thisBoxColWise == otherBoxColWise);
    }

    public boolean isAdjacent(Cell other) {
        return (isInSameBox(other) || isInSameCol(other) || isInSameBox(other));
    }

}

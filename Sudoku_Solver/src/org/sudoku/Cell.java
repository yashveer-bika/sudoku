package org.sudoku;

import java.util.Arrays;

// public class Cell implements Cloneable {
public class Cell {
    int value;
    int row;
    int col;

    int[] possibleValues = new int[9];

    public Cell(int row, int col, int value) {
        this.setValue(value);
        this.row = row;
        this.col = col;
        this.initPossibleValues(); // remember this will do nothing
        // if the value is in the range 1 to 9 inclusive and thus getIsSolved() returns true

    }

    public Cell(Cell other) {
        this.setRow(other.getRow());
        this.setCol(other.getCol());
        this.setValue(other.getValue());
        // System.arraycopy(other.possibleValues, 0, this.possibleValues, 0, 9);
        this.possibleValues = other.possibleValues.clone();

    }
    public boolean equals(Cell cell) {
        return this.value == cell.value
                && this.row == cell.row
                && this.col == cell.col
                && this.getIsSolved() == cell.getIsSolved()
                && this.getNumPossibleValues() == cell.getNumPossibleValues()
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
        if (!(this.getIsSolved())) {
            for (int i = 0; i < 9; i++) {
                this.possibleValues[i] = i + 1;
            }

        }
    }

    public void removePossibleValue(int value) {
        // if (value >= 1 && value <= 9 && this.possibleValues[value-1] >= 1 && this.possibleValues[value-1] <= 9)
        if (value >= 1 && value <= 9 && this.possibleValues[value-1] == value) {
            this.possibleValues[value-1] = -1;
        }
        if (this.getNumPossibleValues() == 1) {
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

        if (new_value >= 1 && new_value <= 9) {
            this.value = new_value;
            for (int i=0; i<9; i++) {
                this.possibleValues[i] = -1;
            }
            this.possibleValues[new_value-1] = new_value;
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
        // return this.isSolved;
        if (this.value >= 1 && this.value <= 9) {
            return true;
        }
        return false;
    }
    public int getNumPossibleValues() {
        // return this.numPossibleValues;
        int numValues = 0;
        for (int i=0; i<9; i++) {
            if (possibleValues[i] != -1) {
                numValues += 1;
            }
        }
        return numValues;
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

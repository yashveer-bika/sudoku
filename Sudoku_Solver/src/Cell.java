package org.sudoku;

import java.util.Arrays;

public class Cell {
    int value;
    int row;
    int col;
    boolean isNull = true;
    int numPossibleValues;
    int[] possibleValues = new int[9];
    public Cell(int row, int col, int value) {
        this.value = value;
        if (value >= 1 && value <= 9) {
            this.isNull = false;
            this.value = value;
        }
        this.row = row;
        this.col = col;
        this.initPossibleValues();
    }

    public boolean equals(Cell cell) {

        return this.value == cell.value
                && this.row == cell.row
                && this.col == cell.col
                && this.isNull == cell.isNull
                && this.numPossibleValues == cell.numPossibleValues
                && this.row == cell.row
                && Arrays.equals(this.possibleValues, cell.possibleValues)
                ;
    }

    public void initPossibleValues() {
        for (int i=0; i<9; i++) {
            this.possibleValues[i] = i+1;
        }
        this.numPossibleValues = 9;
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
        if (this.value >= 1 && this.value <= 9) this.isNull = false;
    }
    public int[] getPossibleValues() {
        return this.possibleValues;
    }
    public boolean getIsNull() {
        return this.isNull;
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
}

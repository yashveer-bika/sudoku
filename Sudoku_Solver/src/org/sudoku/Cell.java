package org.sudoku;

import java.util.Arrays;
import java.util.List;

// public class Cell implements Cloneable {
public class Cell {
    int value;
    int row;
    int col;
    boolean isNull = true;
    int numPossibleValues;
    int[] possibleValues = new int[9];
    public Cell(int row, int col, int value) {
        this.value = value;
        this.row = row;
        this.col = col;
        if (value >= 1 && value <= 9) {
            this.isNull = false;
        } else {
            this.initPossibleValues();
            // this cell is unsolved and we need to initialize possibleValues for it
        }
    }
    public Cell(Cell other) {
        this.setRow(other.getRow());
        this.setCol(other.getCol());
        this.setValue(other.getValue());
        this.setIsNull(other.getIsNull());
        this.setNumPossibleValues(other.getNumPossibleValues());

        System.arraycopy(other.possibleValues, 0, this.possibleValues, 0, 9);
        //TODO does this copy the same references for each index of possibleValues?
    }


    /*
    @Override
    public Cell clone() throws CloneNotSupportedException {
        Object obj = super.clone();
        Cell newCell = (Cell) obj;
        // now to deep clone mutable fields, which is just possibleValues in this class
        for (int i=0; i<9; i++) {
            newCell.possibleValues[i] = this.possibleValues[i];
        }
        return newCell;
    }

     */

    public boolean equals(Cell cell) {

        return this.value == cell.value
                && this.row == cell.row
                && this.col == cell.col
                && this.isNull == cell.isNull
                && this.numPossibleValues == cell.numPossibleValues
                && Arrays.equals(this.possibleValues, cell.possibleValues)
                ;
    }

    public boolean containsPossibleValue(int target_value) {
        if (!(this.getIsNull())) {
            if (this.getValue() == target_value) return true;
            else return false;
        }
        if (!(target_value >= 1 && target_value <= 9)) return false;
        if (possibleValues[target_value-1] == target_value) return true;
        else return false;
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
    public void setRow(int row) {
        this.row = row;
    }
    public void setCol(int col) {
        this.col = col;
    }
    public void setIsNull(boolean isNull) {
        this.isNull = isNull;
    }
    public void setNumPossibleValues(int numPossibleValues) {
        this.numPossibleValues = numPossibleValues;
    }

}

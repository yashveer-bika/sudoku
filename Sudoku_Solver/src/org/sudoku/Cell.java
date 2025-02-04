package org.sudoku;

import java.sql.Array;
import java.util.ArrayList;

public class Cell {
    int value = -1;
    int row;
    int col;

    static int min_value = 1;
    static int max_value = 9;
    // min and max values for a cell

    ArrayList<Integer> possibleValues = new ArrayList<Integer>();
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
        this.possibleValues = (ArrayList<Integer>) other.possibleValues.clone();

    }
    public boolean equals(Cell other) {
        return this.value == other.value
                && this.row == other.row
                && this.col == other.col
                && this.getIsSolved() == other.getIsSolved()
                && this.possibleValues.equals(other.possibleValues)
                ;
    }

    public boolean valueIsValid(int value) {
        return (value >= min_value && value <= max_value);
    }
    public boolean cellValueIsValid() {
        return (this.getValue() >= min_value && this.getValue() <= max_value);
    }

    public boolean containsPossibleValue(int target_value) {
        if (this.getIsSolved()) {
            if (this.getValue() == target_value) return true;
            else return false;
        }
        if (!(valueIsValid(target_value))) return false;
        if (possibleValues.contains(target_value)) return true;
        return false;
    }

    public void initPossibleValues() {
        if (!(this.getIsSolved())) {
            for (int i = min_value; i <= max_value; i++) {
                this.possibleValues.add(i);
            }
        } else {
            this.possibleValues.clear();
            this.possibleValues.add(this.getValue());
        }
    }

    public void removePossibleValue(int value) {
        if (value >= min_value && value <= max_value && this.possibleValues.contains(value)) {
            this.possibleValues.remove(Integer.valueOf(value));
        }
        if (this.getNumPossibleValues() == 1) {
            this.setValue(this.possibleValues.get(0));
        }
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int new_value) {
        if (valueIsValid(new_value)) {
            this.value = new_value;
            this.possibleValues.clear();
            this.possibleValues.add(new_value);
        }
    }

    public int[] getBoxIndices() {
        int[] boxIndices = new int[4];
        boxIndices[0] = 3 * ((int) this.row/3); // lower index (above on board) of box's row
        boxIndices[1] = boxIndices[0] + 2; // higher index (below on board) of box's row
        boxIndices[2] = 3 * ((int) this.col/3); // lower index (left on board) of box's col
        boxIndices[3] = boxIndices[2] + 2; // higher index (right on board) of box's col
        return boxIndices;
    }

    public ArrayList<Integer> getPossibleValues() {
        return this.possibleValues;
    }
    public int getFirstPossibleValue() {
        if (!this.possibleValues.isEmpty()) return this.possibleValues.get(0);
        return -1;
    }
    public void setFirstPossibleValue() {
        this.setValue(this.getFirstPossibleValue());
    }
    public boolean getIsSolved() {
        if (this.value >= min_value && this.value <= max_value) {
            return true;
        }
        return false;
    }
    public int getNumPossibleValues() {
        return this.possibleValues.size();
        // making a function for this in case I want to
        // get the # of possible values with a different method in the future
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
        int[] thisBoxIndices = this.getBoxIndices();
        int[] otherBoxIndices = other.getBoxIndices();
        return (thisBoxIndices[0] == otherBoxIndices[0] &&
                thisBoxIndices[1] == otherBoxIndices[1] &&
                thisBoxIndices[2] == otherBoxIndices[2] &&
                thisBoxIndices[3] == otherBoxIndices[3]);
        // For now, we know that there is a constant difference of 2 between lower and higher box indices so we
        // only need to compare one set of corresponding indices for row and column
        // if this changes for some use-case in the future,
        // we can still use this method. This also improves readability
    }
    public boolean isAdjacent(Cell other) {
        return (isInSameRow(other) || isInSameCol(other) || isInSameBox(other));
    }
}

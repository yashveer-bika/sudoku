package org.sudoku;

import java.util.Arrays;
import java.util.ArrayList;

// public class Cell implements Cloneable {
public class Cell {
    int value;
    int row;
    int col;

    // int[] possibleValues = new int[9];
    ArrayList<Integer> possibleValues = new ArrayList<>();


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
        // this.possibleValues = other.possibleValues.clone();
        this.possibleValues = (ArrayList) other.possibleValues.clone();

    }
    public boolean equals(Cell other) {
        return this.value == other.value
                && this.row == other.row
                && this.col == other.col
                && this.getIsSolved() == other.getIsSolved()
                // && this.getNumPossibleValues() == other.getNumPossibleValues()
                // && Arrays.equals(this.possibleValues, cell.possibleValues)
                && this.possibleValues.equals(other.possibleValues)
                ;
    }

    public boolean containsPossibleValue(int target_value) {
        if (this.getIsSolved()) {
            if (this.getValue() == target_value) return true;
            else return false;
        }
        if (!(target_value >= 1 && target_value <= 9)) return false;
        // if (possibleValues[target_value - 1] == target_value) return true;
        if (possibleValues.contains(target_value)) return true;
        return false;
    }

    public void initPossibleValues() {
        if (!(this.getIsSolved())) {
            for (int i = 0; i < 9; i++) {
                // this.possibleValues[i] = i + 1;
                this.possibleValues.add(i+1);
            }

        }
    }

    public void removePossibleValue(int value) {
        // if (value >= 1 && value <= 9 && this.possibleValues[value-1] >= 1 && this.possibleValues[value-1] <= 9)
        /*
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

         */
        if (value >= 1 && value <= 9 && this.possibleValues.contains(value)) {
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

        if (new_value >= 1 && new_value <= 9) {
            this.value = new_value;
            /* for (int i=0; i<9; i++) {
                this.possibleValues[i] = -1;
            }
            this.possibleValues[new_value-1] = new_value;
             */
            this.possibleValues.clear();
            this.possibleValues.add(new_value);
        }
    }

    public int[] getBoxIndices() {
        // return null;
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
        /*
        for (int i=0; i<9; i++) {
            if (this.getPossibleValues()[i] != -1) {
                return this.getPossibleValues()[i];
            }
        }

         */
        if (!this.possibleValues.isEmpty()) return this.possibleValues.get(0);
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
        /* int numValues = 0;
        for (int i=0; i<9; i++) {
            if (possibleValues[i] != -1) {
                numValues += 1;
            }
        }
        return numValues;

         */
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
        /*
        int thisBoxRowWise = 3 * ((int) this.row/3); // if this is in the leftmost, middle, or rightmost box in a row

        int thisBoxColWise = 3 * ((int) this.col / 3); // if this is in the top, middle, or bottom box in a column
        int otherBoxRowWise = 3 * ((int) other.row / 3); // same as for the thisBoxRowWise but for the Cell other
        int otherBoxColWise = 3 * ((int) other.col / 3);
        return (thisBoxRowWise == otherBoxRowWise && thisBoxColWise == otherBoxColWise);
        */
        int[] thisBoxIndices = this.getBoxIndices();
        int[] otherBoxIndices = other.getBoxIndices();
        return (thisBoxIndices[0] == otherBoxIndices[0] && thisBoxIndices[2] == otherBoxIndices[2]);
        // we know that there is a constant difference of 2 between lower and higher box indices so we
        // only need to compare one set of corresponding indices for row and column
        // if this changes for some use-case in the future,
        // we can rewrite this method to test for equality between all 4 pairs of indices
    }

    public boolean isAdjacent(Cell other) {
        return (isInSameRow(other) || isInSameCol(other) || isInSameBox(other));
    }

}

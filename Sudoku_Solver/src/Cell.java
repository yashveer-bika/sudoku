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
        }
        this.row = row;
        this.col = col;
        this.initPossibleValues();
    }
    void initPossibleValues() {
        for (int i=0; i<9; i++) {
            this.possibleValues[i] = i+1;
        }
        this.numPossibleValues = 9;
    }
    void removePossibleValue(int value) {
        if (value >= 1 && value <= 9) {
            this.possibleValues[value-1] = -99;
            this.numPossibleValues -= 1;
        }
    }
    public int getValue() {
        return this.value;
    }
    public void setValue(int new_value) {
        this.value = new_value;
        if (this.value >= 1 && this.value <= 9) this.isNull = false;
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

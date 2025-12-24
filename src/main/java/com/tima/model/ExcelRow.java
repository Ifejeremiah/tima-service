package com.tima.model;

public class ExcelRow {

    private final int rowNumber;
    private final String col1;
    private final String col2;
    private final String col3;

    public ExcelRow(int rowNumber, String col1, String col2, String col3) {
        this.rowNumber = rowNumber;
        this.col1 = col1;
        this.col2 = col2;
        this.col3 = col3;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public String getCol1() {
        return col1;
    }

    public String getCol2() {
        return col2;
    }

    public String getCol3() {
        return col3;
    }
}


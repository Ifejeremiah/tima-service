package com.tima.io;

import com.tima.model.ExcelRow;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreeCellSheetHandler implements XSSFSheetXMLHandler.SheetContentsHandler {

    private final Map<Integer, String> cellValues = new HashMap<>();
    private final List<ExcelRow> rows = new ArrayList<>();
    private int excelRow;

    @Override
    public void startRow(int rowNum) {
        excelRow = rowNum + 1; // Excel numbering
        cellValues.clear();
    }

    @Override
    public void cell(String cellRef, String value, XSSFComment comment) {
        int colIndex = columnIndex(cellRef);
        if (colIndex < 3) {
            cellValues.put(colIndex, value);
        }
    }

    @Override
    public void endRow(int rowNum) {
        String c1 = cellValues.getOrDefault(0, "");
        String c2 = cellValues.getOrDefault(1, "");
        String c3 = cellValues.getOrDefault(2, "");

        rows.add(new ExcelRow(excelRow, c1, c2, c3));
    }

    public List<ExcelRow> getRows() {
        return rows;
    }

    // Convert "A1" → 0, "B1" → 1, "C1" → 2
    private int columnIndex(String cellRef) {
        int col = 0;
        for (char ch : cellRef.toCharArray()) {
            if (Character.isLetter(ch)) {
                col = col * 26 + (ch - 'A' + 1);
            } else {
                break;
            }
        }
        return col - 1;
    }
}


/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Krokhin
 * Created: 17-Aug-2010 18:36:37
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters.xls;

import org.apache.poi.ss.util.CellReference;

public class Cell {
    private int row;
    private int col;

    public Cell(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public Cell(CellReference originalCell) {
        this(originalCell.getCol(), originalCell.getRow());
    }

    public Cell(Cell cell) {
        col = cell.getCol();
        row = cell.getRow();
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public CellReference toCellReference() {
        return new CellReference(row, col);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj instanceof Cell) {
            if (getCol() != ((Cell)obj).getCol()) return false;
            if (getRow() != ((Cell)obj).getRow()) return false;

            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return Character.toString((char) ('A' + (char) col)) + row;
    }
}

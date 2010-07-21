package com.haulmont.cuba.report.formatters.tools;

import com.sun.star.container.NoSuchElementException;
import com.sun.star.lang.*;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.table.XCell;
import com.sun.star.table.XTableRows;
import com.sun.star.text.XText;
import com.sun.star.text.XTextTable;
import com.sun.star.text.XTextTablesSupplier;
import com.sun.star.uno.Any;

import static com.haulmont.cuba.report.formatters.tools.ODTUnoConverter.*;

/**
 * Created by IntelliJ IDEA.
 * User: fontanenko
 * Date: 23.06.2010
 * Time: 14:16:09
 * To change this template use File | Settings | File Templates.
 */
public class ODTTableHelper {

    public static XTextTable getTableByName(XTextTablesSupplier xTextTablesSupplier, String tableName) throws NoSuchElementException, WrappedTargetException {
        return (XTextTable) ((Any) xTextTablesSupplier.getTextTables().getByName(tableName)).getObject();
    }

    public static XCell getXCell(XTextTable xTextTable, int col, int row) throws IndexOutOfBoundsException {
        return asXCellRange(xTextTable).getCellByPosition(col, row);
    }

    public static XCell getXCell(XTextTable xTextTable, String cellName) {
        return xTextTable.getCellByName(cellName);
    }


    public static String getCellText(XTextTable xTextTable, int col, int row) throws IndexOutOfBoundsException {
        return asXText(getXCell(xTextTable, col, row)).getString();
    }

    public static void setCellText(XTextTable xTextTable, int col, int row, String text) throws IndexOutOfBoundsException {
        asXText(getXCell(xTextTable, col, row)).setString(text);
    }

    public static void setCellText(XTextTable xTextTable, String cellName, String text) {
        asXText(xTextTable.getCellByName(cellName)).setString(text);
    }

    public static void setCellXText(XTextTable xTextTable, int col, int row, XText xText) throws com.sun.star.lang.IndexOutOfBoundsException {
        //asXText(getXCell(xTextTable, col, row));
    }

    public static void deleteRow(XTextTable xTextTable, int row) {
        XTableRows xTableRows = xTextTable.getRows();
        xTableRows.removeByIndex(row, 1);
    }

    public static void deleteLastRow(XTextTable xTextTable) {
        XTableRows xTableRows = xTextTable.getRows();
        xTableRows.removeByIndex(xTableRows.getCount() - 1, 1);
    }

    public static void insertRowToEnd(XTextTable xTextTable) {
        XTableRows xTableRows = xTextTable.getRows();
        xTableRows.insertByIndex(xTableRows.getCount(), 1);
    }

}

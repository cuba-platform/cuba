package com.haulmont.cuba.report.formatters.tools;

import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XNameAccess;
import com.sun.star.frame.XController;
import com.sun.star.frame.XDispatchHelper;
import com.sun.star.frame.XDispatchProvider;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.table.XTableRows;
import com.sun.star.text.XTextTable;
import com.sun.star.text.XTextTableCursor;
import com.sun.star.uno.Any;
import com.sun.star.uno.Type;

import static com.haulmont.cuba.report.formatters.tools.ODTHelper.copy;
import static com.haulmont.cuba.report.formatters.tools.ODTHelper.paste;
import static com.haulmont.cuba.report.formatters.tools.ODTUnoConverter.*;

/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: FONTANENKO VASILIY
 * Created: 12.10.2010 19:21:36
 *
 * $Id$
 */

public class ODTTableHelper {

    public static String[] getTablesNames(XComponent xComponent) {
        XNameAccess tables = asXTextTablesSupplier(xComponent).getTextTables();
        return tables.getElementNames();
    }

    public static XTextTable getTableByName(XComponent xComponent, String tableName) throws NoSuchElementException, WrappedTargetException {
        XNameAccess tables = asXTextTablesSupplier(xComponent).getTextTables();
        return (XTextTable) ((Any) tables.getByName(tableName)).getObject();
    }

    public static XCell getXCell(XTextTable xTextTable, int col, int row) throws IndexOutOfBoundsException {
        return asXCellRange(xTextTable).getCellByPosition(col, row);
    }

    public static XCell getXCell(XTextTable xTextTable, String cellName) {
        return xTextTable.getCellByName(cellName);
    }

    public static void selectRow(XController xController, XTextTable xTextTable, int row) throws com.sun.star.uno.Exception {
        String[] cellNames = xTextTable.getCellNames();
        int colCount = xTextTable.getColumns().getCount();
        String firstCellName = cellNames[row * colCount];
        String lastCellName = cellNames[row * colCount + colCount - 1];
        XTextTableCursor xTextTableCursor = xTextTable.createCursorByCellName(firstCellName);
        xTextTableCursor.gotoCellByName(lastCellName, true);
        // stupid shit. It works only if XCellRange was created via cursor. why????
        // todo: refactor this if possible
        XCellRange xCellRange = asXCellRange(xTextTable).getCellRangeByName(xTextTableCursor.getRangeName());
        // and why do we need Any here?
        asXSelectionSupplier(xController).select(new Any(new Type(XCellRange.class), xCellRange));
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

    public static void duplicateLastRow(XDispatchHelper xDispatchHelper, XController xController, XTextTable xTextTable) throws com.sun.star.uno.Exception, WrappedTargetException, IllegalArgumentException {
        int lastRowNum = xTextTable.getRows().getCount() - 1;
        selectRow(xController, xTextTable, lastRowNum);
        XDispatchProvider xDispatchProvider = asXDispatchProvider(xController.getFrame());
        copy(xDispatchHelper, xDispatchProvider);
        insertRowToEnd(xTextTable);
        selectRow(xController, xTextTable, ++lastRowNum);
        paste(xDispatchHelper, xDispatchProvider);
    }
}

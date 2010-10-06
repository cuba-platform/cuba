/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Vasiliy Fontanenko
 * Created: 23.06.2010 14:16:09
 *
 * $Id$
 */

package com.haulmont.cuba.report.formatters.tools;

import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.lang.*;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.table.*;
import com.sun.star.text.*;
import com.sun.star.uno.Any;
import com.sun.star.uno.UnoRuntime;
import static com.haulmont.cuba.report.formatters.tools.ODTUnoConverter.*;

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
        XCell xcell = getXCell(xTextTable,col,row);
        XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xcell);
        
    }

    public static XText getCellXText(XTextTable xTextTable, int col, int row) throws IndexOutOfBoundsException {
        XCell xcell = getXCell(xTextTable,col,row);
        XText xCellText = (XText) UnoRuntime.queryInterface(XText.class, xcell);
        return xCellText;
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

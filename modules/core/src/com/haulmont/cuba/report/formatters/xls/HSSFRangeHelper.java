/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 18.01.2011 11:20:18
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters.xls;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFName;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public final class HSSFRangeHelper {
    private HSSFRangeHelper() {
    }

    public static boolean isMergeRegionInsideNamedRange(Integer rangeFirstRow, Integer rangeFirstColumn, Integer rangeLastRow, Integer rangeLastColumn, Integer regionFirstRow, Integer regionFirstColumn, Integer regionLastRow, Integer regionLastColumn) {
        return regionFirstColumn >= rangeFirstColumn && regionFirstColumn <= rangeLastColumn &&
                regionLastColumn >= rangeFirstColumn && regionLastColumn <= rangeLastColumn &&
                regionFirstRow >= rangeFirstRow && regionFirstRow <= rangeLastRow &&
                regionLastRow >= rangeFirstRow && regionLastRow <= rangeLastRow;
    }

    public static boolean isNamedRangeInsideMergeRegion(Integer rangeFirstRow, Integer rangeFirstColumn, Integer rangeLastRow, Integer rangeLastColumn, Integer regionFirstRow, Integer regionFirstColumn, Integer regionLastRow, Integer regionLastColumn) {
        return regionFirstColumn <= rangeFirstColumn && regionFirstColumn <= rangeLastColumn &&
                regionLastColumn >= rangeFirstColumn && regionLastColumn >= rangeLastColumn &&
                regionFirstRow <= rangeFirstRow && regionFirstRow <= rangeLastRow &&
                regionLastRow >= rangeFirstRow && regionLastRow >= rangeLastRow;
    }

    public static CellReference[] getRangeContent(HSSFWorkbook workbook, String rangeName) {
        return getAreaForRange(workbook, rangeName).getAllReferencedCells();
    }

    public static AreaReference getAreaForRange(HSSFWorkbook workbook, String rangeName) {
        int rangeNameIdx = workbook.getNameIndex(rangeName);
        if (rangeNameIdx == -1) return null;

        HSSFName aNamedRange = workbook.getNameAt(rangeNameIdx);
        return new AreaReference(aNamedRange.getReference());
    }

    public static HSSFSheet getTemplateSheetForRangeName(HSSFWorkbook workbook, String rangeName) {
        int rangeNameIdx = workbook.getNameIndex(rangeName);
        if (rangeNameIdx == -1) return null;

        HSSFName aNamedRange = workbook.getNameAt(rangeNameIdx);
        String sheetName = aNamedRange.getSheetName();
        return workbook.getSheet(sheetName);
    }
}

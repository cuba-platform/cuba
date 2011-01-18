/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 18.01.2011 11:10:15
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters.xls;

import com.haulmont.cuba.report.Band;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.util.CellReference;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

import static com.haulmont.cuba.report.formatters.AbstractFormatter.*;

public final class HSSFCellHelper {
    private HSSFCellHelper() {
    }

    /**
     * Copies template cell to result cell and fills it with band data
     *
     * @param band         - band
     * @param templateCell - template cell
     * @param resultCell   - result cell
     */
    public static void updateValueCell(Band band, HSSFCell templateCell, HSSFCell resultCell) {
        String parameterName = templateCell.toString();
        parameterName = unwrapParameterName(parameterName);

        if (StringUtils.isEmpty(parameterName)) return;

        if (!band.getData().containsKey(parameterName)) {
            resultCell.setCellValue(templateCell.getRichStringCellValue());
            return;
        }

        Object parameterValue = band.getData().get(parameterName);

        if (parameterValue == null)
            resultCell.setCellType(HSSFCell.CELL_TYPE_BLANK);
        else if (parameterValue instanceof Number)
            resultCell.setCellValue(((Number) parameterValue).doubleValue());
        else if (parameterValue instanceof Boolean)
            resultCell.setCellValue((Boolean) parameterValue);
        else if (parameterValue instanceof Date)
            resultCell.setCellValue((Date) parameterValue);
        else resultCell.setCellValue(new HSSFRichTextString(parameterValue.toString()));
    }

    /**
     * Inlines band data to cell.
     * No formatting supported now.
     *
     * @param cell - cell to inline data
     * @param band - data source
     * @return string with inlined band data
     */
    public static String inlineBandDataToCellString(HSSFCell cell, Band band) {
        String resultStr = "";
        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
            HSSFRichTextString richString = cell.getRichStringCellValue();
            if (richString != null) resultStr = richString.getString();
        } else {
            if (cell.toString() != null) resultStr = cell.toString();
        }

        if (!"".equals(resultStr)) return insertBandDataToString(band, resultStr);

        return "";
    }

    /**
     * Detects if cell contains only one template to inleine value
     *
     * @param cell - cell
     * @return -
     */
    public static boolean isOneValueCell(HSSFCell cell) {
        boolean result = true;
        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
            String value = cell.getRichStringCellValue().getString();

            if (value.lastIndexOf("${") != 0)
                result = false;
            else
                result = value.indexOf("}") == value.length() - 1;
        }
        return result;
    }

    public static HSSFCell getCellFromReference(CellReference cref, HSSFSheet templateSheet) {
        return getCellFromReference(templateSheet, cref.getCol(), cref.getRow());
    }

    public static HSSFCell getCellFromReference(HSSFSheet templateSheet, int colIndex, int rowIndex) {
        HSSFRow row = templateSheet.getRow(rowIndex);
        row = row == null ? templateSheet.createRow(rowIndex) : row;
        HSSFCell cell = row.getCell(colIndex);
        cell = cell == null ? row.createCell(colIndex) : cell;
        return cell;
    }
}

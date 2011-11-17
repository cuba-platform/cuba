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
import com.haulmont.cuba.report.ReportValueFormat;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;

import java.awt.Dimension;
import java.util.Date;
import java.util.HashMap;

import static com.haulmont.cuba.report.formatters.AbstractFormatter.insertBandDataToString;
import static com.haulmont.cuba.report.formatters.AbstractFormatter.unwrapParameterName;

public final class HSSFCellHelper {
    private HSSFCellHelper() {
    }

    /**
     * Copies template cell to result cell and fills it with band data
     *
     * @param rootBand     Root band
     * @param band         Band
     * @param templateCell Template cell
     * @param resultCell   Result cell
     * @param patriarch    Toplevel container for shapes in a sheet
     */
    public static void updateValueCell(Band rootBand, Band band, HSSFCell templateCell, HSSFCell resultCell,
                                       HSSFPatriarch patriarch) {
        String parameterName = templateCell.toString();
        parameterName = unwrapParameterName(parameterName);

        if (StringUtils.isEmpty(parameterName)) return;

        if (!band.getData().containsKey(parameterName)) {
            resultCell.setCellValue(templateCell.getRichStringCellValue());
            return;
        }

        Object parameterValue = band.getData().get(parameterName);
        HashMap<String, ReportValueFormat> valuesFormats = rootBand.getValuesFormats();

        if (parameterValue == null)
            resultCell.setCellType(HSSFCell.CELL_TYPE_BLANK);
        else if (parameterValue instanceof Number)
            resultCell.setCellValue(((Number) parameterValue).doubleValue());
        else if (parameterValue instanceof Boolean)
            resultCell.setCellValue((Boolean) parameterValue);
        else if (parameterValue instanceof Date)
            resultCell.setCellValue((Date) parameterValue);
        else if (valuesFormats.containsKey(parameterName)) {
            String formatString = valuesFormats.get(parameterName).getFormatString();
            ImageExtractor imageExtractor = new ImageExtractor(formatString, parameterValue);
            if (ImageExtractor.isImage(formatString)) {
                paintImageToCell(resultCell, patriarch, imageExtractor);
            }
        } else
            resultCell.setCellValue(new HSSFRichTextString(parameterValue.toString()));
    }

    private static void paintImageToCell(HSSFCell resultCell, HSSFPatriarch patriarch, ImageExtractor imageExtractor) {
        ImageExtractor.Image image = imageExtractor.extract();
        if (image != null) {
            int targetHeight = image.getHeight();
            resultCell.getRow().setHeightInPoints(targetHeight);
            HSSFSheet sheet = resultCell.getSheet();
            HSSFWorkbook workbook = sheet.getWorkbook();

            int pictureIdx = workbook.addPicture(image.getContent(), Workbook.PICTURE_TYPE_JPEG);

            CreationHelper helper = workbook.getCreationHelper();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(resultCell.getColumnIndex());
            anchor.setRow1(resultCell.getRowIndex());
            anchor.setCol2(resultCell.getColumnIndex());
            anchor.setRow2(resultCell.getRowIndex());
            HSSFPicture picture = patriarch.createPicture(anchor, pictureIdx);
            Dimension imageDimension = picture.getImageDimension();
            double actualHeight = imageDimension.getHeight();
            picture.resize((double) targetHeight / actualHeight);
            picture.resize();
        }
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

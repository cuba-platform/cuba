/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 07.05.2010 18:34:07
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.report.Band;
import com.haulmont.cuba.report.Orientation;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.record.PaletteRecord;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class XLSFormatter extends AbstractFormatter {
    private HSSFWorkbook templateWorkbook;
    private HSSFSheet currentTemplateSheet = null;

    private int rownum;
    private int colnum;
    private int rowsAddedByVerticalBand = 0;

    private HSSFWorkbook resultWorkbook;
    private Map<String, List<CellRangeAddress>> mergeRegionsForRangeNames = new HashMap<String, List<CellRangeAddress>>();
    private Map<HSSFSheet, HSSFSheet> templateToResultSheetsMapping = new HashMap<HSSFSheet, HSSFSheet>();

    public XLSFormatter(FileDescriptor template) throws IOException {
        try {
            templateWorkbook = new HSSFWorkbook(getFileInputStream(template));
            resultWorkbook = new HSSFWorkbook();

            cloneWorkbookStyles();

            for (int sheetNumber = 0; sheetNumber < templateWorkbook.getNumberOfSheets(); sheetNumber++)
                cloneSheet(sheetNumber);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        rownum = 0;
        colnum = 0;
    }

    /**
     * Clones sheet with number sheetNumber from template workbook to result
     *
     * @param sheetNumber number of sheet to be copied
     */
    private void cloneSheet(int sheetNumber) {
        HSSFSheet templateSheet = templateWorkbook.getSheetAt(sheetNumber);
        initMergeRegions(templateSheet);
        HSSFSheet resultSheet = resultWorkbook.createSheet();
        templateToResultSheetsMapping.put(templateSheet, resultSheet);
        int lastColNum = -1;
        // todo: find better way
        for (int rowNum = 0; rowNum <= templateSheet.getLastRowNum(); rowNum++) {
            HSSFRow row = templateSheet.getRow(rowNum);
            int rowColNum = (row != null) ? row.getLastCellNum() : -1;
            lastColNum = Math.max(rowColNum, lastColNum);
        }
        for (int i = 0; i < lastColNum; i++)
            resultSheet.setColumnWidth(i, templateSheet.getColumnWidth(i));
    }

    /**
     * Clones styles for cells and palette from template workbook
     */
    private void cloneWorkbookStyles() {
        HSSFCellStyle cellStyle = resultWorkbook.createCellStyle();
        cellStyle.cloneStyleFrom(templateWorkbook.createCellStyle());

        HSSFPalette customPalette = templateWorkbook.getCustomPalette();
        for (short i = PaletteRecord.FIRST_COLOR_INDEX; i < PaletteRecord.FIRST_COLOR_INDEX + PaletteRecord.STANDARD_PALETTE_SIZE; i++) {
            HSSFColor color = customPalette.getColor(i);
            if (color == null) continue;

            short[] colors = color.getTriplet();
            resultWorkbook.getCustomPalette().setColorAtIndex(i, (byte) colors[0], (byte) colors[1], (byte) colors[2]);
        }
    }

    /**
     * Method creates mapping [rangeName -> List<CellRangeAddress>]. List contains all merge regions for this named range
     *
     * @param currentSheet - sheet which contains merge regions
     *                     <p/>
     *                     todo: if merged regions writes wrong - look on methods isMergeRegionInsideNamedRange & isNamedRangeInsideMergeRegion
     *                     todo: how to recognize if merge region must be copied with named range
     */
    private void initMergeRegions(HSSFSheet currentSheet) {
        int rangeNumber = templateWorkbook.getNumberOfNames();
        for (int i = 0; i < rangeNumber; i++) {
            HSSFName aNamedRange = templateWorkbook.getNameAt(i);
            AreaReference aref = new AreaReference(aNamedRange.getReference());
            Integer rangeFirstRow = aref.getFirstCell().getRow();
            Integer rangeFirstColumn = (int) aref.getFirstCell().getCol();
            Integer rangeLastRow = aref.getLastCell().getRow();
            Integer rangeLastColumn = (int) aref.getLastCell().getCol();

            CellRangeAddress mergedRegion;
            int j = 0;
            do {
                mergedRegion = currentSheet.getMergedRegion(j++);
                if (mergedRegion != null) {
                    Integer regionFirstRow = mergedRegion.getFirstRow();
                    Integer regionFirstColumn = mergedRegion.getFirstColumn();
                    Integer regionLastRow = mergedRegion.getLastRow();
                    Integer regionLastColumn = mergedRegion.getLastColumn();

                    if (isMergeRegionInsideNamedRange(rangeFirstRow, rangeFirstColumn, rangeLastRow, rangeLastColumn,
                            regionFirstRow, regionFirstColumn, regionLastRow, regionLastColumn)
                            ||
                            isNamedRangeInsideMergeRegion(rangeFirstRow, rangeFirstColumn, rangeLastRow, rangeLastColumn,
                                    regionFirstRow, regionFirstColumn, regionLastRow, regionLastColumn)
                            ) {
                        String name = aNamedRange.getNameName();

                        if (mergeRegionsForRangeNames.get(name) == null) {
                            ArrayList<CellRangeAddress> list = new ArrayList<CellRangeAddress>();
                            list.add(mergedRegion);
                            mergeRegionsForRangeNames.put(name, list);
                        } else {
                            mergeRegionsForRangeNames.get(name).add(mergedRegion);
                        }
                    }
                }
            } while (mergedRegion != null);
        }
    }

    private boolean isMergeRegionInsideNamedRange(Integer rangeFirstRow, Integer rangeFirstColumn, Integer rangeLastRow, Integer rangeLastColumn, Integer regionFirstRow, Integer regionFirstColumn, Integer regionLastRow, Integer regionLastColumn) {
        return regionFirstColumn >= rangeFirstColumn && regionFirstColumn <= rangeLastColumn &&
                regionLastColumn >= rangeFirstColumn && regionLastColumn <= rangeLastColumn &&
                regionFirstRow >= rangeFirstRow && regionFirstRow <= rangeLastRow &&
                regionLastRow >= rangeFirstRow && regionLastRow <= rangeLastRow;
    }

    private boolean isNamedRangeInsideMergeRegion(Integer rangeFirstRow, Integer rangeFirstColumn, Integer rangeLastRow, Integer rangeLastColumn, Integer regionFirstRow, Integer regionFirstColumn, Integer regionLastRow, Integer regionLastColumn) {
        return regionFirstColumn <= rangeFirstColumn && regionFirstColumn <= rangeLastColumn &&
                regionLastColumn >= rangeFirstColumn && regionLastColumn >= rangeLastColumn &&
                regionFirstRow <= rangeFirstRow && regionFirstRow <= rangeLastRow &&
                regionLastRow >= rangeFirstRow && regionLastRow >= rangeLastRow;
    }

    public byte[] createDocument(Band rootBand) {
        for (Band childBand : rootBand.getChildren()) {
            writeBand(childBand);
        }

        updateFormulas();

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            resultWorkbook.write(byteArrayOutputStream);
            byte[] result = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Method which recalculates regions for group formulas
     */
    protected void updateFormulas() {
    }

    private void writeBand(Band childBand) {
        String rangeName = childBand.getName();
        HSSFSheet templateSheet = getTemplateSheetForRangeName(rangeName);

        if (templateSheet != currentTemplateSheet) { //todo: reimplement. store rownum for each sheet.
            currentTemplateSheet = templateSheet;
            rownum = 0;
        }

        HSSFSheet resultSheet = templateToResultSheetsMapping.get(templateSheet);

        if (Orientation.HORIZONTAL.equals(childBand.getOrientation())) {
            rownum += rowsAddedByVerticalBand;
            rowsAddedByVerticalBand = 0;
            colnum = 0;
            writeHorizontalBand(childBand, templateSheet, resultSheet);

            addRangeLinks(childBand);
        } else {
            rowsAddedByVerticalBand = writeVerticalBand(childBand, templateSheet, resultSheet);
        }
    }

    /**
     * Adds link between range in template and result workbooks
     */
    private void addRangeLinks(Band childBand) {
        AreaReference areaReference = getAreaForRange(childBand.getName());
    }

    /**
     * Method writes horizontal band
     * Note: Only one band for row is supported. Now we think that many bands for row aren't usable.
     *
     * @param band          - band to write
     * @param templateSheet - template sheet
     * @param resultSheet   - result sheet
     */
    private void writeHorizontalBand(Band band, HSSFSheet templateSheet, HSSFSheet resultSheet) {
        String rangeName = band.getName();
        CellReference[] crefs = getRangeContent(rangeName);

        if (crefs != null) {
            ArrayList<HSSFRow> resultRows = new ArrayList<HSSFRow>();

            int currentRowNum = -1;
            int currentRowCount = -1;
            int currentColumnCount = 0;
            int offset = 0;

            copyMergeRegions(resultSheet, rangeName, rownum, getCellFromReference(crefs[0], templateSheet).getColumnIndex());

            for (CellReference cellRef : crefs) {
                HSSFCell templateCell = getCellFromReference(cellRef, templateSheet);
                HSSFRow resultRow;
                if (templateCell.getRowIndex() != currentRowNum) { //create new row
                    resultRow = resultSheet.createRow(rownum++);
                    resultRows.add(resultRow);

                    currentRowNum = templateCell.getRowIndex();
                    currentRowCount++;
                    currentColumnCount = 0;
                    offset = templateCell.getColumnIndex();
                } else {                                          // or write cell to current row
                    resultRow = resultRows.get(currentRowCount);
                    currentColumnCount++;
                }

                copyCellFromTemplate(resultRow, offset + currentColumnCount, templateCell, band);
            }
        }
        for (Band child : band.getChildren()) {
            writeBand(child);
        }
    }

    /**
     * Method writes vertical band
     * Note: no child support for vertical band ;)
     *
     * @param band          - band to write
     * @param templateSheet - template sheet
     * @param resultSheet   - result sheet
     * @return number of inserted rows
     */
    private int writeVerticalBand(Band band, HSSFSheet templateSheet, HSSFSheet resultSheet) {
        String rangeName = band.getName();
        CellReference[] crefs = getRangeContent(rangeName);

        if (crefs != null) {
            colnum = colnum == 0 ? getCellFromReference(crefs[0], templateSheet).getColumnIndex() : colnum;
            copyMergeRegions(resultSheet, rangeName, rownum, colnum);
            for (int i = 0; i < crefs.length; i++) {
                if (!rowExists(resultSheet, rownum + i)) {
                    resultSheet.createRow(rownum + i);
                }
            }

            for (int i = 0; i < crefs.length; i++) {
                HSSFCell templateCell = getCellFromReference(crefs[i], templateSheet);
                copyCellFromTemplate(resultSheet.getRow(rownum + i), colnum, templateCell, band);
            }

            colnum++;
            return crefs.length;
        }
        return 0;
    }

    private void copyCellFromTemplate(HSSFRow resultRow, int resultColumn, HSSFCell templateCell, Band band) {
        if (templateCell == null) return;

        HSSFCell resultCell = resultRow.createCell(resultColumn);
        HSSFCellStyle resultStyle = resultWorkbook.createCellStyle();
        resultStyle.cloneStyleFrom(templateCell.getCellStyle());
        resultCell.setCellStyle(resultStyle);

        if (templateCell.getCellType() == HSSFCell.CELL_TYPE_STRING && isOneValueCell(templateCell)) {
            String parameterName = templateCell.toString();
            parameterName = parameterName.substring("${".length(), parameterName.length() - "}".length());

            if (StringUtils.isEmpty(parameterName)) return;

            Object parameterValue = band.getData().get(parameterName);
            if (parameterValue == null) {
                resultCell.setCellValue(templateCell.getRichStringCellValue());
                return;
            }

            if (parameterValue instanceof Number)
                resultCell.setCellValue(((Number) parameterValue).doubleValue());
            else if (parameterValue instanceof Boolean)
                resultCell.setCellValue((Boolean) parameterValue);
            else if (parameterValue instanceof Date)
                resultCell.setCellValue((Date) parameterValue);
            else resultCell.setCellValue(new HSSFRichTextString(parameterValue.toString()));

        } else if (templateCell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
            resultCell.setCellFormula(inlineBandDataToCellString(templateCell, band));
        } else {
            resultCell.setCellValue(new HSSFRichTextString(inlineBandDataToCellString(templateCell, band)));
        }
    }

    protected boolean isOneValueCell(HSSFCell cell) {
        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
            String value = cell.getRichStringCellValue().getString();

            if (value.lastIndexOf("${") != 0) return false;

            return value.indexOf("}") == value.length() - 1;
        } else return true;
    }

    /**
     * Inlines band data to cell.
     * No formatting supported now.
     *
     * @param cell - cell to inline data
     * @param band - data source
     * @return string with inlined band data
     */
    private String inlineBandDataToCellString(HSSFCell cell, Band band) {
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

    //--------------------------Excel methods-------------------------------------//

    private boolean rowExists(HSSFSheet sheet, int rowNumber) {
        return sheet.getRow(rowNumber) != null;
    }

    private HSSFCell getCellFromReference(CellReference cref, HSSFSheet templateSheet) {
        HSSFRow row = templateSheet.getRow(cref.getRow());
        row = row == null ? templateSheet.createRow(cref.getRow()) : row;
        HSSFCell cell = row.getCell((int) cref.getCol());
        cell = cell == null ? row.createCell((int) cref.getCol()) : cell;
        return cell;
    }

    private CellReference[] getRangeContent(String rangeName) {
        return getAreaForRange(rangeName).getAllReferencedCells();
    }

    private AreaReference getAreaForRange(String rangeName) {
        int rangeNameIdx = templateWorkbook.getNameIndex(rangeName);
        if (rangeNameIdx == -1) return null;

        HSSFName aNamedRange = templateWorkbook.getNameAt(rangeNameIdx);
        return new AreaReference(aNamedRange.getReference());
    }

    private HSSFSheet getTemplateSheetForRangeName(String rangeName) {
        int rangeNameIdx = templateWorkbook.getNameIndex(rangeName);
        if (rangeNameIdx == -1) return null;

        HSSFName aNamedRange = templateWorkbook.getNameAt(rangeNameIdx);
        String sheetName = aNamedRange.getSheetName();
        return templateWorkbook.getSheet(sheetName);
    }

    /**
     * Create new merge regions in result sheet identically to range's merge regions from template.
     *
     * @param resultSheet            - result sheet
     * @param rangeName              - range name
     * @param firstTargetRangeRow    - first column of target range
     * @param firstTargetRangeColumn - first column of target range
     */
    private void copyMergeRegions(HSSFSheet resultSheet, String rangeName, int firstTargetRangeRow, int firstTargetRangeColumn) {
        int rangeNameIdx = templateWorkbook.getNameIndex(rangeName);
        if (rangeNameIdx == -1) return;

        HSSFName aNamedRange = templateWorkbook.getNameAt(rangeNameIdx);
        AreaReference aref = new AreaReference(aNamedRange.getReference());
        int column = aref.getFirstCell().getCol();
        int row = aref.getFirstCell().getRow();

        List<CellRangeAddress> regionsList = mergeRegionsForRangeNames.get(rangeName);
        if (regionsList != null)
            for (CellRangeAddress cra : regionsList)
                if (cra != null) {
                    int regionHeight = cra.getLastRow() - cra.getFirstRow() + 1;
                    int regionWidth = cra.getLastColumn() - cra.getFirstColumn() + 1;

                    int regionVOffset = cra.getFirstRow() - row;
                    int regionHOffset = cra.getFirstColumn() - column;

                    CellRangeAddress cra2 = cra.copy();
                    cra2.setFirstColumn(regionHOffset + firstTargetRangeColumn);
                    cra2.setLastColumn(regionHOffset + regionWidth - 1 + firstTargetRangeColumn);

                    cra2.setFirstRow(regionVOffset + firstTargetRangeRow);
                    cra2.setLastRow(regionVOffset + regionHeight - 1 + firstTargetRangeRow);

                    resultSheet.addMergedRegion(cra2);
                }
    }
}

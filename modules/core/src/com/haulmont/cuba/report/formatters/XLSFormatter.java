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
import com.haulmont.cuba.report.formatters.xls.Area;
import com.haulmont.cuba.report.formatters.xls.AreaAlign;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.PaletteRecord;
import org.apache.poi.hssf.record.EscherAggregate;
import org.apache.poi.hssf.record.formula.AreaPtg;
import org.apache.poi.hssf.record.formula.Ptg;
import org.apache.poi.hssf.record.formula.RefPtg;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ddf.*;

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

    private Map<Area, List<Area>> areasDependency = new LinkedHashMap<Area, List<Area>>();
    private List<Integer> orderedPicturesId = new ArrayList();


    public XLSFormatter(FileDescriptor template) throws IOException {
        templateWorkbook = new HSSFWorkbook(getFileInputStream(template));
        resultWorkbook = new HSSFWorkbook();

        cloneWorkbookStyles();
        copyAllPictures();

        for (int sheetNumber = 0; sheetNumber < templateWorkbook.getNumberOfSheets(); sheetNumber++) {
            cloneSheet(sheetNumber);
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

        resultSheet.setDisplayGridlines(templateSheet.isDisplayGridlines());
        resultWorkbook.setSheetName(sheetNumber, templateWorkbook.getSheetName(sheetNumber));
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

        List<HSSFPictureData> pictures = templateWorkbook.getAllPictures();
        for (HSSFPictureData picture : pictures) {
            resultWorkbook.addPicture(picture.getData(), picture.getFormat());
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

        for (Map.Entry<Area, List<Area>> entry : areasDependency.entrySet()) {
            Area original = entry.getKey();

            for (Area dependent : entry.getValue()) {
                updateBandFormula(original, dependent);
            }
        }

        for (int sheetNumber = 0; sheetNumber < templateWorkbook.getNumberOfSheets(); sheetNumber++) {
            HSSFSheet templateSheet = templateWorkbook.getSheetAt(sheetNumber);
            HSSFSheet resultSheet = resultWorkbook.getSheetAt(sheetNumber);

            copyPicturesOnSheet(templateSheet, resultSheet);
        }

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

    private void copyAllPictures() {
        List<HSSFPictureData> pictures = templateWorkbook.getAllPictures();
        for (HSSFPictureData picture : pictures) {
            orderedPicturesId.add(resultWorkbook.addPicture(picture.getData(), picture.getFormat()));
        }
    }

    private void updateBandFormula(Area original, Area dependent) {
        HSSFSheet templateSheet = getTemplateSheetForRangeName(original.getName());
        HSSFSheet resultSheet = templateToResultSheetsMapping.get(templateSheet);

        AreaReference area = dependent.toAreaReference();
        for (CellReference cell : area.getAllReferencedCells()) {
            HSSFCell resultCell = getCellFromReference(cell, resultSheet);

            if (resultCell.getCellType() != HSSFCell.CELL_TYPE_FORMULA) continue;

            Ptg[] ptgs = HSSFFormulaParser.parse(resultCell.getCellFormula(), templateWorkbook);

            for (int i = 0; i < ptgs.length; i++) {
                if (ptgs[i] instanceof AreaPtg)
                    updateAreaPtg((AreaPtg) ptgs[i]);
                else if (ptgs[i] instanceof RefPtg)
                    updateRefPtg(original, dependent, (RefPtg) ptgs[i]);
            }

            String calculatedFormula = HSSFFormulaParser.toFormulaString(templateWorkbook, ptgs);
            resultCell.setCellFormula(calculatedFormula);
        }
    }

    private void copyPicturesOnSheet(HSSFSheet templateSheet, HSSFSheet resultSheet) {
        List<HSSFClientAnchor> list = getAllAnchors(templateSheet);
        HSSFPatriarch workingPatriarch = resultSheet.createDrawingPatriarch();

        int i = 0;
        for (HSSFClientAnchor anchor : list) {
            Area areaReference = getAreaByCoordinate(anchor.getCol1(), anchor.getRow1());
            List<Area> dependent = areasDependency.get(areaReference);

            if (dependent != null && !dependent.isEmpty()) {
                Area destination = dependent.get(0);

                int col = anchor.getCol1() - areaReference.getTopLeft().getCol() + destination.getTopLeft().getCol();
                int row = anchor.getRow1() - areaReference.getTopLeft().getRow() + destination.getTopLeft().getRow();

                anchor.setCol1(col);
                anchor.setRow1(row);
            }

            areaReference = getAreaByCoordinate(anchor.getCol2(), anchor.getRow2());
            dependent = areasDependency.get(areaReference);

            if (dependent != null && !dependent.isEmpty()) {
                Area destination = dependent.get(0);

                int col = anchor.getCol2() - areaReference.getTopLeft().getCol() + destination.getTopLeft().getCol();
                int row = anchor.getRow2() - areaReference.getTopLeft().getRow() + destination.getTopLeft().getRow();

                anchor.setCol2(col);
                anchor.setRow2(row);
            }

            workingPatriarch.createPicture(anchor, orderedPicturesId.get(i++));
        }
    }

    private void updateRefPtg(Area originalContainingArea, Area dependentContainingArea, RefPtg current) {
        Area areaReference = getAreaByCoordinate(current.getColumn(), current.getRow());

        if (areaReference == null) return;

        Area destination;
        if (areaReference.equals(originalContainingArea)) destination = dependentContainingArea;
        else {
            List<Area> dependent = areasDependency.get(areaReference);
            if (dependent == null || dependent.isEmpty()) return;
            destination = dependent.get(0);
        }

        int col = current.getColumn() - areaReference.getTopLeft().getCol() + destination.getTopLeft().getCol();
        int row = current.getRow() - areaReference.getTopLeft().getRow() + destination.getTopLeft().getRow();

        current.setColumn(col);
        current.setRow(row);
    }

    private void updateAreaPtg(AreaPtg current) {
        Area areaReference = getAreaByCoordinate(current.getFirstColumn(), current.getFirstRow());

        List<Area> dependent = areasDependency.get(areaReference);

        if (dependent == null || dependent.isEmpty()) return;

        if (areaReference.getAlign() == AreaAlign.HORIZONTAL) {
            int minRow = Integer.MAX_VALUE;
            int maxRow = -1;
            for (Area currentArea : dependent) {
                int rowMin = currentArea.getTopLeft().getRow();
                int rowMax = currentArea.getBottomRight().getRow();

                if (rowMin < minRow) minRow = rowMin;
                if (rowMax > maxRow) maxRow = rowMax;
            }

            current.setFirstRow(minRow);
            current.setLastRow(maxRow);
        } else {
            int minCol = Integer.MAX_VALUE;
            int maxCol = -1;
            for (Area currentArea : dependent) {
                int colMin = currentArea.getTopLeft().getCol();
                int colMax = currentArea.getBottomRight().getCol();

                if (colMin < minCol) minCol = colMax;
                if (colMax > maxCol) maxCol = colMax;
            }

            current.setFirstColumn(minCol);
            current.setLastColumn(maxCol);
        }
    }

    private Area getAreaByCoordinate(int col, int row) {
        for (Area areaReference : areasDependency.keySet()) {
            if (areaReference.getTopLeft().getCol() > col) continue;
            if (areaReference.getTopLeft().getRow() > row) continue;
            if (areaReference.getBottomRight().getCol() < col) continue;
            if (areaReference.getBottomRight().getRow() < row) continue;

            return areaReference;
        }

        return null;
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
        } else {
            rowsAddedByVerticalBand = writeVerticalBand(childBand, templateSheet, resultSheet);
        }
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
        AreaReference templateRange = getAreaForRange(templateWorkbook, rangeName);
        CellReference[] crefs = templateRange.getAllReferencedCells();

        CellReference topLeft, bottomRight;
        AreaReference resultRange;

        if (crefs != null) {
            ArrayList<HSSFRow> resultRows = new ArrayList<HSSFRow>();

            int currentRowNum = -1;
            int currentRowCount = -1;
            int currentColumnCount = 0;
            int offset = 0;

            topLeft = new CellReference(rownum, 0);
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

            bottomRight = new CellReference(rownum - 1, offset + currentColumnCount);
            resultRange = new AreaReference(topLeft, bottomRight);

            addDependency(new Area(band.getName(), AreaAlign.HORIZONTAL, templateRange),
                    new Area(band.getName(), AreaAlign.HORIZONTAL, resultRange));
        }
        for (Band child : band.getChildren()) {
            writeBand(child);
        }
    }

    private void addDependency(Area main, Area dependent) {
        List<Area> set = areasDependency.get(main);

        if (set == null) {
            set = new ArrayList<Area>();
            areasDependency.put(main, set);
        }

        set.add(dependent);
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

        if (templateCell.getCellType() == HSSFCell.CELL_TYPE_STRING && isOneValueCell(templateCell))
            updateValueCell(band, templateCell, resultCell);
        else if (templateCell.getCellType() == HSSFCell.CELL_TYPE_FORMULA)
            resultCell.setCellFormula(inlineBandDataToCellString(templateCell, band));
        else
            resultCell.setCellValue(new HSSFRichTextString(inlineBandDataToCellString(templateCell, band)));
    }

    protected boolean isOneValueCell(HSSFCell cell) {
        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
            String value = cell.getRichStringCellValue().getString();

            if (value.lastIndexOf("${") != 0) return false;

            return value.indexOf("}") == value.length() - 1;
        } else return true;
    }

    private void updateValueCell(Band band, HSSFCell templateCell, HSSFCell resultCell) {
        String parameterName = templateCell.toString();
        parameterName = parameterName.substring("${".length(), parameterName.length() - "}".length());

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
        return getCellFromReference(templateSheet, cref.getCol(), cref.getRow());
    }

    private HSSFCell getCellFromReference(HSSFSheet templateSheet, int colIndex, int rowIndex) {
        HSSFRow row = templateSheet.getRow(rowIndex);
        row = row == null ? templateSheet.createRow(rowIndex) : row;
        HSSFCell cell = row.getCell(colIndex);
        cell = cell == null ? row.createCell(colIndex) : cell;
        return cell;
    }

    private CellReference[] getRangeContent(String rangeName) {
        return getAreaForRange(templateWorkbook, rangeName).getAllReferencedCells();
    }

    private AreaReference getAreaForRange(HSSFWorkbook workbook, String rangeName) {
        int rangeNameIdx = workbook.getNameIndex(rangeName);
        if (rangeNameIdx == -1) return null;

        HSSFName aNamedRange = workbook.getNameAt(rangeNameIdx);
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


    public List<HSSFClientAnchor> getAllAnchors(HSSFSheet sheet) {
        List<HSSFClientAnchor> pictures = new ArrayList<HSSFClientAnchor>();
        List<EscherRecord> escherRecords = getEscherAggregate(sheet).getEscherRecords();
        searchForAnchors(escherRecords, pictures);
        return pictures;
    }

    private void searchForAnchors(List escherRecords, List<HSSFClientAnchor> pictures) {
        Iterator recordIter = escherRecords.iterator();
        HSSFClientAnchor anchor = null;
        while (recordIter.hasNext()) {
            Object obj = recordIter.next();
            if (obj instanceof EscherRecord) {
                EscherRecord escherRecord = (EscherRecord) obj;
                if (escherRecord instanceof EscherClientAnchorRecord) {
                    EscherClientAnchorRecord anchorRecord = (EscherClientAnchorRecord) escherRecord;
                    if (anchor == null) anchor = new HSSFClientAnchor();
                    anchor.setDx1(anchorRecord.getDx1());
                    anchor.setDx2(anchorRecord.getDx2());
                    anchor.setDy1(anchorRecord.getDy1());
                    anchor.setDy2(anchorRecord.getDy2());
                    anchor.setRow1(anchorRecord.getRow1());
                    anchor.setRow2(anchorRecord.getRow2());
                    anchor.setCol1(anchorRecord.getCol1());
                    anchor.setCol2(anchorRecord.getCol2());
                }
                // Recursive call.
                searchForAnchors(escherRecord.getChildRecords(), pictures);
            }
        }
        if (anchor != null)
            pictures.add(anchor);
    }


    private Map<String, EscherAggregate> sheetToEscherAggregate = new HashMap<String, EscherAggregate>();

    private EscherAggregate getEscherAggregate(HSSFSheet sheet) {
        EscherAggregate agg = sheetToEscherAggregate.get(sheet.getSheetName());
        if (agg == null) {
            agg = sheet.getDrawingEscherAggregate();
            sheetToEscherAggregate.put(sheet.getSheetName(), agg);
        }
        return agg;
    }
}

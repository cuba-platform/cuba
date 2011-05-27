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

import com.haulmont.cuba.report.Band;
import com.haulmont.cuba.report.Orientation;
import com.haulmont.cuba.report.ReportOutputType;
import com.haulmont.cuba.report.exception.ReportFormatterException;
import com.haulmont.cuba.report.formatters.xls.*;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.EscherAggregate;
import org.apache.poi.hssf.record.PaletteRecord;
import org.apache.poi.hssf.record.formula.AreaPtg;
import org.apache.poi.hssf.record.formula.Ptg;
import org.apache.poi.hssf.record.formula.RefPtg;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import static com.haulmont.cuba.report.formatters.xls.HSSFCellHelper.*;
import static com.haulmont.cuba.report.formatters.xls.HSSFPicturesHelper.getAllAnchors;
import static com.haulmont.cuba.report.formatters.xls.HSSFRangeHelper.*;

public class XLSFormatter extends AbstractFormatter {
    private HSSFWorkbook templateWorkbook;
    private HSSFSheet currentTemplateSheet = null;

    private XlsFontCache fontCache = new XlsFontCache();
    private XlsStyleCache styleCache = new XlsStyleCache();

    private int rownum;
    private int colnum;
    private int rowsAddedByVerticalBand = 0;
    private int rowsAddedByHorizontalBand = 0;

    private HSSFWorkbook resultWorkbook;
    private Map<String, List<SheetRange>> mergeRegionsForRangeNames = new HashMap<String, List<SheetRange>>();
    private Map<HSSFSheet, HSSFSheet> templateToResultSheetsMapping = new HashMap<HSSFSheet, HSSFSheet>();
    private Map<String, Bounds> templateBounds = new HashMap<String, Bounds>();

    private Map<Area, List<Area>> areasDependency = new LinkedHashMap<Area, List<Area>>();
    private List<Integer> orderedPicturesId = new ArrayList<Integer>();
    private Map<String, EscherAggregate> sheetToEscherAggregate = new HashMap<String, EscherAggregate>();

    private AreaDependencyHelper areaDependencyHelper = new AreaDependencyHelper();

    public XLSFormatter() {
        registerReportExtension("xls");
        registerReportExtension("xlt");

        registerReportOutput(ReportOutputType.XLS);

        defaultOutputType = ReportOutputType.XLS;
    }

    private void initWorkbook() throws IOException {
        templateWorkbook = new HSSFWorkbook(getFileInputStream(templateFile));
        resultWorkbook = new HSSFWorkbook();

        cloneWorkbookDataFormats();
        cloneWorkbookStyles();
        copyAllPictures();

        for (int sheetNumber = 0; sheetNumber < templateWorkbook.getNumberOfSheets(); sheetNumber++) {
            cloneSheet(sheetNumber);
        }

        rownum = 0;
        colnum = 0;
    }

    private void cloneWorkbookDataFormats() {
    }

    public void createDocument(Band rootBand, ReportOutputType outputType, OutputStream outputStream) {

        if (templateFile == null)
            throw new NullPointerException();

        try {
            initWorkbook();
        } catch (Exception e) {
            throw new ReportFormatterException(e);
        }

        processDocument(rootBand);

        try {
            resultWorkbook.write(outputStream);
        } catch (Exception e) {
            throw new ReportFormatterException(e);
        }
    }

    private void processDocument(Band rootBand) {
        for (Band childBand : rootBand.getChildrenList()) {
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
    }

    private void writeBand(Band band) {
        String rangeName = band.getName();
        HSSFSheet templateSheet = getTemplateSheetForRangeName(templateWorkbook, rangeName);

        if (templateSheet != currentTemplateSheet) { //todo: reimplement. store rownum for each sheet.
            currentTemplateSheet = templateSheet;
            rownum = 0;
        }

        HSSFSheet resultSheet = templateToResultSheetsMapping.get(templateSheet);

        if (Orientation.HORIZONTAL.equals(band.getOrientation())) {
            colnum = 0;
            writeHorizontalBand(band, templateSheet, resultSheet);
        } else {
            writeVerticalBand(band, templateSheet, resultSheet);
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
            addRangeBounds(band, crefs);

            ArrayList<HSSFRow> resultRows = new ArrayList<HSSFRow>();

            int currentRowNum = -1;
            int currentRowCount = -1;
            int currentColumnCount = 0;
            int offset = 0;

            topLeft = new CellReference(rownum, 0);
            copyMergeRegions(resultSheet, rangeName, rownum + rowsAddedByHorizontalBand,
                    getCellFromReference(crefs[0], templateSheet).getColumnIndex());

            for (CellReference cellRef : crefs) {
                HSSFCell templateCell = getCellFromReference(cellRef, templateSheet);
                HSSFRow resultRow;
                if (templateCell.getRowIndex() != currentRowNum) { //create new row
                    resultRow = resultSheet.createRow(rownum + rowsAddedByHorizontalBand);
                    rowsAddedByHorizontalBand += 1;
                    resultRow.setHeight(templateCell.getRow().getHeight());
                    resultRows.add(resultRow);

                    currentRowNum = templateCell.getRowIndex();
                    currentRowCount++;
                    currentColumnCount = 0;
                    offset = templateCell.getColumnIndex();
                } else {                                          // or write cell to current row
                    resultRow = resultRows.get(currentRowCount);
                    currentColumnCount++;
                }

                copyCellFromTemplate(templateCell, resultRow, offset + currentColumnCount, band);
            }

            bottomRight = new CellReference(rownum + rowsAddedByHorizontalBand - 1, offset + currentColumnCount);
            resultRange = new AreaReference(topLeft, bottomRight);

            areaDependencyHelper.addDependency(new Area(band.getName(), AreaAlign.HORIZONTAL, templateRange),
                    new Area(band.getName(), AreaAlign.HORIZONTAL, resultRange));
        }

        for (Band child : band.getChildrenList()) {
            writeBand(child);
        }

        rownum += rowsAddedByHorizontalBand;
        rowsAddedByHorizontalBand = 0;
        rownum += rowsAddedByVerticalBand;
        rowsAddedByVerticalBand = 0;
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
    private void writeVerticalBand(Band band, HSSFSheet templateSheet, HSSFSheet resultSheet) {
        String rangeName = band.getName();
        CellReference[] crefs = getRangeContent(templateWorkbook, rangeName);

        if (crefs != null) {
            addRangeBounds(band, crefs);

            Bounds thisBounds = templateBounds.get(band.getName());
            Bounds parentBounds = templateBounds.get(band.getParentBand().getName());
            int localRowNum = parentBounds != null ? rownum + thisBounds.row0 - parentBounds.row0 : rownum;

            colnum = colnum == 0 ? getCellFromReference(crefs[0], templateSheet).getColumnIndex() : colnum;
            copyMergeRegions(resultSheet, rangeName, localRowNum, colnum);

            int firstRow = crefs[0].getRow();
            int firstColumn = crefs[0].getCol();

            for (CellReference cref : crefs) {//create necessary rows
                int currentRow = cref.getRow();
                final int rowOffset = currentRow - firstRow;
                if (!rowExists(resultSheet, localRowNum + rowOffset)) {
                    resultSheet.createRow(localRowNum + rowOffset);
                    rowsAddedByVerticalBand++;
                }
            }

            for (CellReference cref : crefs) {
                int currentRow = cref.getRow();
                int currentColumn = cref.getCol();
                final int rowOffset = currentRow - firstRow;
                final int columnOffset = currentColumn - firstColumn;

                HSSFCell templateCell = getCellFromReference(cref, templateSheet);
                copyCellFromTemplate(templateCell, resultSheet.getRow(localRowNum + rowOffset), colnum + columnOffset, band);
            }

            colnum += crefs[crefs.length - 1].getCol() - firstColumn + 1;
        }

        if ("Root".equals(band.getParentBand().getName())) {
            List<Band> sameBands = band.getParentBand().getChildrenByName(band.getName());
            if (sameBands.size() > 0 && sameBands.get(sameBands.size() - 1) == band) {//check if this vertical band is last   
                rownum += rowsAddedByVerticalBand;
                rowsAddedByVerticalBand = 0;
            }
        }
    }

    /**
     * <p>
     * Method creates mapping [rangeName -> List< CellRangeAddress >]. <br/>
     * List contains all merge regions for this named range
     * </p>
     * todo: if merged regions writes wrong - look on methods isMergeRegionInsideNamedRange & isNamedRangeInsideMergeRegion
     * todo: how to recognize if merge region must be copied with named range
     *
     * @param currentSheet Sheet which contains merge regions
     */
    private void initMergeRegions(HSSFSheet currentSheet) {
        int rangeNumber = templateWorkbook.getNumberOfNames();
        for (int i = 0; i < rangeNumber; i++) {
            HSSFName aNamedRange = templateWorkbook.getNameAt(i);

            AreaReference aref = new AreaReference(aNamedRange.getRefersToFormula());

            Integer rangeFirstRow = aref.getFirstCell().getRow();
            Integer rangeFirstColumn = (int) aref.getFirstCell().getCol();
            Integer rangeLastRow = aref.getLastCell().getRow();
            Integer rangeLastColumn = (int) aref.getLastCell().getCol();

            for (int j = 0; j < currentSheet.getNumMergedRegions(); j++) {
                CellRangeAddress mergedRegion = currentSheet.getMergedRegion(j);
                if (mergedRegion != null) {
                    Integer regionFirstRow = mergedRegion.getFirstRow();
                    Integer regionFirstColumn = mergedRegion.getFirstColumn();
                    Integer regionLastRow = mergedRegion.getLastRow();
                    Integer regionLastColumn = mergedRegion.getLastColumn();

                    boolean mergedInsideNamed = isMergeRegionInsideNamedRange(
                            rangeFirstRow, rangeFirstColumn, rangeLastRow, rangeLastColumn,
                            regionFirstRow, regionFirstColumn, regionLastRow, regionLastColumn);

                    boolean namedInsideMerged = isNamedRangeInsideMergeRegion(
                            rangeFirstRow, rangeFirstColumn, rangeLastRow, rangeLastColumn,
                            regionFirstRow, regionFirstColumn, regionLastRow, regionLastColumn);

                    if (mergedInsideNamed || namedInsideMerged) {
                        String name = aNamedRange.getNameName();
                        SheetRange sheetRange = new SheetRange(mergedRegion, currentSheet.getSheetName());
                        if (mergeRegionsForRangeNames.get(name) == null) {
                            ArrayList<SheetRange> list = new ArrayList<SheetRange>();
                            list.add(sheetRange);
                            mergeRegionsForRangeNames.put(name, list);
                        } else {
                            mergeRegionsForRangeNames.get(name).add(sheetRange);
                        }
                    }
                }
            }
        }
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

        // copy orientation, margins, etc

        HSSFPrintSetup templatePintSetup = templateSheet.getPrintSetup();
        HSSFPrintSetup resultPrintSetup = resultSheet.getPrintSetup();
        resultPrintSetup.setNoOrientation(templatePintSetup.getNoOrientation());
        resultPrintSetup.setLandscape(templatePintSetup.getLandscape());
        resultPrintSetup.setHeaderMargin(templatePintSetup.getHeaderMargin());
        resultPrintSetup.setFooterMargin(templatePintSetup.getFooterMargin());
        resultPrintSetup.setScale(templatePintSetup.getScale());
        resultPrintSetup.setPaperSize(templatePintSetup.getPaperSize());
        resultPrintSetup.setFitHeight(templatePintSetup.getFitHeight());
        resultPrintSetup.setFitWidth(templatePintSetup.getFitWidth());
        resultPrintSetup.setLeftToRight(templatePintSetup.getLeftToRight());
        resultPrintSetup.setHResolution(templatePintSetup.getHResolution());
        resultPrintSetup.setVResolution(templatePintSetup.getVResolution());
        resultPrintSetup.setUsePage(templatePintSetup.getUsePage());
        resultPrintSetup.setCopies(templatePintSetup.getCopies());
        resultPrintSetup.setDraft(templatePintSetup.getDraft());
        resultPrintSetup.setNoColor(templatePintSetup.getNoColor());
        resultPrintSetup.setNotes(templatePintSetup.getNotes());
        resultPrintSetup.setPageStart(templatePintSetup.getPageStart());
        resultPrintSetup.setOptions(templatePintSetup.getOptions());
        resultPrintSetup.setValidSettings(templatePintSetup.getValidSettings());

        resultSheet.setMargin(Sheet.LeftMargin, templateSheet.getMargin(Sheet.LeftMargin));
        resultSheet.setMargin(Sheet.RightMargin, templateSheet.getMargin(Sheet.RightMargin));
        resultSheet.setMargin(Sheet.TopMargin, templateSheet.getMargin(Sheet.TopMargin));
        resultSheet.setMargin(Sheet.BottomMargin, templateSheet.getMargin(Sheet.BottomMargin));

        resultWorkbook.setSheetName(sheetNumber, templateWorkbook.getSheetName(sheetNumber));
    }

    /**
     * Clones styles for cells and palette from template workbook
     */
    private void cloneWorkbookStyles() {
//        HSSFCellStyle cellStyle = resultWorkbook.createCellStyle();
//        cellStyle.cloneStyleRelationsFrom(templateWorkbook.createCellStyle());

        HSSFPalette customPalette = templateWorkbook.getCustomPalette();
        for (short i = PaletteRecord.FIRST_COLOR_INDEX;
             i < PaletteRecord.FIRST_COLOR_INDEX + PaletteRecord.STANDARD_PALETTE_SIZE; i++) {
            HSSFColor color = customPalette.getColor(i);
            if (color != null) {
                short[] colors = color.getTriplet();
                resultWorkbook.getCustomPalette().setColorAtIndex(i, (byte) colors[0], (byte) colors[1], (byte) colors[2]);
            }
        }

        List<HSSFPictureData> pictures = templateWorkbook.getAllPictures();
        for (HSSFPictureData picture : pictures) {
            resultWorkbook.addPicture(picture.getData(), picture.getFormat());
        }
    }

    /**
     * copies template cell to result row into result column. Fills this cell with data from band
     *
     * @param templateCell - template cell
     * @param resultRow    - result row
     * @param resultColumn - result column
     * @param band         - band
     */
    private void copyCellFromTemplate(HSSFCell templateCell, HSSFRow resultRow, int resultColumn, Band band) {
        if (templateCell != null) {
            HSSFCell resultCell = resultRow.createCell(resultColumn);

            // trouble with maximum font count
            // try to use font cache
            HSSFCellStyle templateStyle = templateCell.getCellStyle();
            HSSFCellStyle resultStyle = copyCellStyle(templateStyle);
            resultCell.setCellStyle(resultStyle);
//            resultCell.setCellType(HSSFCell.CELL_TYPE_STRING);

            int cellType = templateCell.getCellType();
            if (cellType == HSSFCell.CELL_TYPE_STRING && isOneValueCell(templateCell))
                updateValueCell(band, templateCell, resultCell);
            else if (cellType == HSSFCell.CELL_TYPE_FORMULA)
                resultCell.setCellFormula(inlineBandDataToCellString(templateCell, band));
            else if (cellType == HSSFCell.CELL_TYPE_STRING)
                resultCell.setCellValue(new HSSFRichTextString(inlineBandDataToCellString(templateCell, band)));
            else
                resultCell.setCellValue(inlineBandDataToCellString(templateCell, band));
        }
    }

    private HSSFCellStyle copyCellStyle(HSSFCellStyle templateStyle) {
        HSSFCellStyle resultStyle = resultWorkbook.createCellStyle();

        resultStyle.cloneStyleRelationsFrom(templateStyle);
        HSSFFont font = fontCache.getFont(templateStyle.getFont(templateWorkbook));
        if (font != null)
            resultStyle.setFont(fontCache.processFont(font));
        else {
            resultStyle.cloneFontFrom(templateStyle);
            fontCache.processFont(resultStyle.getFont(resultWorkbook));
        }

        return styleCache.processCellStyle(resultStyle);
    }

    /**
     * Create new merge regions in result sheet identically to range's merge regions from template.
     * Not support copy of frames and rules
     *
     * @param resultSheet            - result sheet
     * @param rangeName              - range name
     * @param firstTargetRangeRow    - first column of target range
     * @param firstTargetRangeColumn - first column of target range
     */
    private void copyMergeRegions(HSSFSheet resultSheet, String rangeName,
                                  int firstTargetRangeRow, int firstTargetRangeColumn) {
        int rangeNameIdx = templateWorkbook.getNameIndex(rangeName);
        if (rangeNameIdx == -1) return;

        HSSFName aNamedRange = templateWorkbook.getNameAt(rangeNameIdx);
        AreaReference aref = new AreaReference(aNamedRange.getRefersToFormula());
        int column = aref.getFirstCell().getCol();
        int row = aref.getFirstCell().getRow();

        List<SheetRange> regionsList = mergeRegionsForRangeNames.get(rangeName);
        if (regionsList != null)
            for (SheetRange sheetRange : regionsList) {
                if (resultSheet.getSheetName().equals(sheetRange.getSheetName())) {
                    CellRangeAddress cra = sheetRange.getCellRangeAddress();
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
    }

    /**
     * This method adds range bounds to cache. Key is bandName
     *
     * @param band  - band
     * @param crefs - range
     */
    private void addRangeBounds(Band band, CellReference[] crefs) {
        if (templateBounds.containsKey(band.getName()))
            return;
        Bounds bounds = new Bounds(crefs[0].getRow(), crefs[0].getCol(), crefs[crefs.length - 1].getRow(), crefs[crefs.length - 1].getCol());
        templateBounds.put(band.getName(), bounds);
    }

    private void updateBandFormula(Area original, Area dependent) {
        HSSFSheet templateSheet = getTemplateSheetForRangeName(templateWorkbook, original.getName());
        HSSFSheet resultSheet = templateToResultSheetsMapping.get(templateSheet);

        AreaReference area = dependent.toAreaReference();
        for (CellReference cell : area.getAllReferencedCells()) {
            HSSFCell resultCell = getCellFromReference(cell, resultSheet);

            if (resultCell.getCellType() != HSSFCell.CELL_TYPE_FORMULA) continue;

            Ptg[] ptgs = HSSFFormulaParser.parse(resultCell.getCellFormula(), templateWorkbook);

            for (Ptg ptg : ptgs) {
                if (ptg instanceof AreaPtg)
                    areaDependencyHelper.updateAreaPtg((AreaPtg) ptg);
                else if (ptg instanceof RefPtg)
                    areaDependencyHelper.updateRefPtg(original, dependent, (RefPtg) ptg);
            }

            String calculatedFormula = HSSFFormulaParser.toFormulaString(templateWorkbook, ptgs);
            resultCell.setCellFormula(calculatedFormula);
        }
    }

    /**
     * Copies all pictures from template workbook to result workbook
     */
    private void copyAllPictures() {
        List<HSSFPictureData> pictures = templateWorkbook.getAllPictures();
        for (HSSFPictureData picture : pictures) {
            orderedPicturesId.add(resultWorkbook.addPicture(picture.getData(), picture.getFormat()));
        }
    }

    /**
     * Returns EscherAggregate from sheet
     *
     * @param sheet - HSSFSheet
     * @return - EscherAggregate from sheet
     */
    private EscherAggregate getEscherAggregate(HSSFSheet sheet) {
        EscherAggregate agg = sheetToEscherAggregate.get(sheet.getSheetName());
        if (agg == null) {
            agg = sheet.getDrawingEscherAggregate();
            sheetToEscherAggregate.put(sheet.getSheetName(), agg);
        }
        return agg;
    }

    /**
     * Copies all pictures from template sheet to result sheet
     *
     * @param templateSheet - template sheet
     * @param resultSheet   - result sheet
     */
    private void copyPicturesOnSheet(HSSFSheet templateSheet, HSSFSheet resultSheet) {
        List<HSSFClientAnchor> list = getAllAnchors(getEscherAggregate(templateSheet));
        HSSFPatriarch workingPatriarch = resultSheet.createDrawingPatriarch();

        int i = 0;
        for (HSSFClientAnchor anchor : list) {
            Cell topLeft = areaDependencyHelper.getCellFromTemplate(new Cell(anchor.getCol1(), anchor.getRow1()));
            anchor.setCol1(topLeft.getCol());
            anchor.setRow1(topLeft.getRow());

            Cell bottomRight = areaDependencyHelper.getCellFromTemplate(new Cell(anchor.getCol2(), anchor.getRow2()));
            anchor.setCol2(bottomRight.getCol());
            anchor.setRow2(bottomRight.getRow());

            workingPatriarch.createPicture(anchor, orderedPicturesId.get(i++));
        }
    }

    private boolean rowExists(HSSFSheet sheet, int rowNumber) {
        return sheet.getRow(rowNumber) != null;
    }

    /**
     * In this class colected all methods which works with area's dependencies
     */
    private class AreaDependencyHelper {
        void updateCell(Cell cell) {
            Area areaReference = areaDependencyHelper.getAreaByCoordinate(cell.getCol(), cell.getRow());
            List<Area> dependent = areasDependency.get(areaReference);

            if (dependent != null && !dependent.isEmpty()) {
                Area destination = dependent.get(0);

                int col = cell.getCol() - areaReference.getTopLeft().getCol() + destination.getTopLeft().getCol();
                int row = cell.getRow() - areaReference.getTopLeft().getRow() + destination.getTopLeft().getRow();

                cell.setCol(col);
                cell.setRow(row);
            }
        }

        Cell getCellFromTemplate(Cell cell) {
            Cell newCell = new Cell(cell);
            updateCell(newCell);
            return newCell;
        }

        /**
         * Adds area dependency for formula calculations
         *
         * @param main      Main area
         * @param dependent Dependent area
         */
        private void addDependency(Area main, Area dependent) {
            List<Area> set = areasDependency.get(main);

            if (set == null) {
                set = new ArrayList<Area>();
                areasDependency.put(main, set);
            }
            set.add(dependent);
        }

        void updateRefPtg(Area originalContainingArea, Area dependentContainingArea, RefPtg current) {
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

        void updateAreaPtg(AreaPtg current) {
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

        Area getAreaByCoordinate(int col, int row) {
            for (Area areaReference : areasDependency.keySet()) {
                if (areaReference.getTopLeft().getCol() > col) continue;
                if (areaReference.getTopLeft().getRow() > row) continue;
                if (areaReference.getBottomRight().getCol() < col) continue;
                if (areaReference.getBottomRight().getRow() < row) continue;

                return areaReference;
            }

            return null;
        }
    }

    /**
     * Cell range at sheet
     */
    private class SheetRange {
        private CellRangeAddress cellRangeAddress;
        private String sheetName;

        private SheetRange(CellRangeAddress cellRangeAddress, String sheetName) {
            this.cellRangeAddress = cellRangeAddress;
            this.sheetName = sheetName;
        }

        public CellRangeAddress getCellRangeAddress() {
            return cellRangeAddress;
        }

        public String getSheetName() {
            return sheetName;
        }
    }

    private static class Bounds {
        public final int row0;
        public final int column0;
        public final int row1;
        public final int column1;

        private Bounds(int row0, int column0, int row1, int column1) {
            this.row0 = row0;
            this.column0 = column0;
            this.row1 = row1;
            this.column1 = column1;
        }

        /*public int verticalOffset(Bounds bounds) {
            if (bounds == null || bounds.row1 <= row0) {
                return row1 - row0 + 1;
            } else {
                return row1 - bounds.row1;
            }
        }*/
    }
}

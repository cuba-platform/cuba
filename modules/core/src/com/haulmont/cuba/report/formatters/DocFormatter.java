/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 12.05.2010 17:15:27
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.report.Band;
import com.haulmont.cuba.report.ReportOutputType;
import com.haulmont.cuba.report.exception.ReportFormatterException;
import com.haulmont.cuba.report.formatters.exception.FailedToConnectToOpenOfficeAPIExceptionReport;
import com.haulmont.cuba.report.formatters.tools.*;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XIndexAccess;
import com.sun.star.frame.*;
import com.sun.star.io.XInputStream;
import com.sun.star.lang.*;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.table.*;
import com.sun.star.text.*;
import com.sun.star.uno.*;
import com.sun.star.util.*;
import com.sun.star.view.XSelectionSupplier;
import com.sun.star.xml.dom.XDocument;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.Exception;
import java.lang.RuntimeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.haulmont.cuba.report.formatters.tools.ODTHelper.*;
import static com.haulmont.cuba.report.formatters.tools.ODTTableHelper.*;
import static com.haulmont.cuba.report.formatters.tools.ODTUnoConverter.*;

public class DocFormatter extends AbstractFormatter {
    private String openOfficePath;
    private Band rootBand;
    private Map<String, TableTemplate> tableTemplates;
    private FileDescriptor docTemplate;
    private ReportOutputType reportOutputType;
    private ODTContextGroup contextGroup;
    private XComponent xComponent;

    private Log log = LogFactory.getLog(DocFormatter.class);

    private DocFormatter() {
        tableTemplates = new HashMap<String, TableTemplate>();
    }

    public DocFormatter(FileDescriptor template, ReportOutputType reportOutputType) {
        this();
        this.docTemplate = template;
        this.reportOutputType = reportOutputType;
    }

    public byte[] createDocument(Band rootBand) {
        this.rootBand = rootBand;
        openOfficePath = ConfigProvider.getConfig(ServerConfig.class).getOpenOfficePath();

        String bands = "";
        for (Band b : rootBand.getChildren())
            bands += b.getName() + "|";
        if (rootBand.getChildren().size() == 0)
            log.info("RootBand is empty");

        XComponentLoader xComponentLoader;
        try {
            contextGroup = createXComponentGroup(openOfficePath);
            xComponentLoader = contextGroup.getLoader();
        } catch (Exception e) {
            throw new FailedToConnectToOpenOfficeAPIExceptionReport("Please check OpenOffice path: " + openOfficePath);
        }
        try {
            XInputStream xis = getXInputStream(docTemplate);
            xComponent = loadXComponent(xComponentLoader, xis);
            // Handling tables
            XTextTablesSupplier tablesSupplier = asXTextTablesSupplier(xComponent);
            createTableTemplates(tablesSupplier);
            fillTablesWithBandData(tablesSupplier);
            deleteTemplateRows(tablesSupplier);
            // Handling text
//            replaceValueExpressionsInDocument(asXTextDocument(xComponent)); ED - I think we have to replace aliases as in XLS formatter - select ${smth} in text and fund parameters in band
            replaceAllAliasesInDocument(asXTextDocument(xComponent));
            // Saving document to output stream and closing
            return saveAndClose(xComponent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] saveAndClose(XComponent xComponent) throws Exception {
        OOOutputStream ooos = new OOOutputStream();
        String filterName;
        if (ReportOutputType.PDF.equals(reportOutputType)) {
            filterName = "writer_pdf_Export";
        } else {
            filterName = "writer8";
        }
        saveXComponent(xComponent, ooos, filterName);
        closeXComponent(xComponent);
        return ooos.toByteArray();
    }

    /**
     * Old method - now use <code>replaceAllAliasesInDocument</code>
     */
    @Deprecated
    private void replaceValueExpressionsInDocument(XTextDocument xTextDocument) throws NoSuchElementException, WrappedTargetException, IllegalArgumentException, PropertyVetoException, UnknownPropertyException {
        //todo: do refactoring!!
        if (rootBand.getChildren() != null) {
            for (Band child : rootBand.getChildren()) {
                Map<String, Object> bandData = child.getData();
                for (Map.Entry<String, Object> entry : bandData.entrySet()) {
                    String valueExpression = "${" + child.getName() + "." + entry.getKey() + "}";
                    Object bandValue = entry.getValue();
                    if (bandValue == null) bandValue = "";
                    String bandValueStr = bandValue instanceof Entity ? ((Instance) bandValue).getInstanceName() : bandValue.toString();
                    ODTHelper.replaceInDocument(xTextDocument, valueExpression, bandValueStr, false);
                }
            }
            ODTHelper.replaceInDocument(xTextDocument, "\\$\\{.+?\\}", "", true);
        }
    }

    /**
     * Replaces all aliases (${bandname.paramname} in document).
     * If there is not appropriate band or alias is bad - throws RuntimeException
     *
     * @param xTextDocument - document
     */
    private void replaceAllAliasesInDocument(XTextDocument xTextDocument) {
        XReplaceable xReplaceable = (XReplaceable) UnoRuntime.queryInterface(XReplaceable.class, xTextDocument);
        XSearchDescriptor searchDescriptor = xReplaceable.createSearchDescriptor();
        // regexp: \$\{[^\.]+?[a-zA-Z0-9\.]*[^\.]\}
        searchDescriptor.setSearchString("\\$\\{[^\\.]+?[a-zA-Z0-9\\.]*[^\\.]\\}");
        try {
            searchDescriptor.setPropertyValue("SearchRegularExpression", true);
            XIndexAccess indexAccess = xReplaceable.findAll(searchDescriptor);
            for (int i = 0; i < indexAccess.getCount(); i++) {
                XTextRange o = asXTextRange(indexAccess.getByIndex(i));
                String alias = o.getString().replaceAll("[\\{|\\}|\\$]", "");
                String[] parts = alias.split("\\.");

                if (parts == null || parts.length < 2)
                    throw new ReportFormatterException("Bad alias : " + o.getString());

                String bandName = parts[0];
                Band band = bandName.equals("Root") ? rootBand : rootBand.getChildByName(bandName);

                if (band == null) throw new ReportFormatterException("No band for alias : " + alias);
                StringBuffer paramName = new StringBuffer();
                for (int j = 1; j < parts.length; j++) {
                    paramName.append(parts[j]);
                    if (j != parts.length - 1) paramName.append(".");
                }

                Object parameter = band.getParameter(paramName.toString());
                o.setString(parameter != null ? parameter.toString() : "");
            }
        } catch (Exception ex) {
            //throw new ReportFormatterException(ex);
        }
    }

    private void fillTablesWithBandData(XTextTablesSupplier xTextTablesSupplier) throws NoSuchElementException, WrappedTargetException, IndexOutOfBoundsException {
        // Iterating over all table templates
        for (TableTemplate tableTemplate : tableTemplates.values()) {
            // Getting table corresponding to tepmlate
            XTextTable xTextTable = getTableByName(xTextTablesSupplier, tableTemplate.getTableName());

            /* todo: remove hack for table with one column
            boolean hastemp = addTempColumnToTable(xTextTable);
            try {
                copyTableRow(xTextTable, 0);
            } catch (Exception e) {
                throw new ReportFormatterException("Error in table copy");
            } */

            // Finding all bands with name of table wich is now processed
            for (Band band : rootBand.getChildren()) {
                if (band.getName().trim().equals(tableTemplate.getTableName())) {
                    //replaceBandsInRow(xTextTable, 0, band);
                    // Inserting new row to table
                    insertRowToEnd(xTextTable);
                    int lastRow = xTextTable.getRows().getCount() - 1;
                    // Iterating over all cells templates, specified for this table and processing them
                    for (Integer column : tableTemplate.getColumnsTemplates().keySet()) {
                        //setCellText(xTextTable, column, lastRow, processCellTemplate(tableTemplate.getColumnTemplate(column), band));
                        setCellText(xTextTable, column, lastRow, insertBandDataToString(band, tableTemplate.getColumnTemplate(column)));
                    }
                }
            }

            //removeTempColumFromTable(xTextTable, hastemp);
        }
    }

    //Copy table and merge copy with original
    private void copyTable(XTextTable xTable) throws com.sun.star.uno.Exception {
        // All type casts make through ([Class])UnoRuntime.queryInterface([Class].class, object)
        XMultiComponentFactory xMCF = contextGroup.getMCF();
        XComponentContext xContext = contextGroup.getContext();

        // Get current controller
        XTextDocument xTextDocument = asXTextDocument(xComponent);
        XController currController = xTextDocument.getCurrentController();

        // Get selection supplier
        XSelectionSupplier xSelectionSupplier = asXSelectionSupplier(currController);

        // Get cell names
        String[] arrCellNames = xTable.getCellNames();

        // Create a table cursor starting at the first cell
        XTextTableCursor xTextTableCursor = xTable.createCursorByCellName(arrCellNames[0]);

        // Move cursor to the last cell in the table
        xTextTableCursor.gotoCellByName(arrCellNames[arrCellNames.length - 1], true);

        // Get the table as XCellRange interface
        XCellRange xTableCellRange = asXCellRange(xTable);

        // Create the cell range using the table cursor
        XCellRange xCellRange = xTableCellRange.getCellRangeByName(xTextTableCursor.getRangeName());

        // Select the cell range constructed above
        xSelectionSupplier.select(new Any(new Type(xCellRange.getClass()), xCellRange));

        // Get Dispatch Helper to dispatch commands
        // xContext is the local or remote context , i.e. the office context (XComponentContext)
        // one can obtain this in various ways, the simpler being Bootstrap.bootstrap()
        XDispatchHelper xDispatchHelper = asXDispatchHelper(
                xMCF.createInstanceWithContext("com.sun.star.frame.DispatchHelper", xContext));

        // We have the selection
        // Execute copy command
        XDispatchProvider xFrame = asXDispatchProvider(currController.getFrame());
        xDispatchHelper.executeDispatch(xFrame, ".uno:Copy", "", 0, new PropertyValue[]{new PropertyValue()});

        // Move the cursor below the table, to paste the contents that we copied

        // Get the view cursor
        XTextViewCursorSupplier xCursorSupplier = asXTextCursorSupplier(currController);
        XTextViewCursor xViewCursor = xCursorSupplier.getViewCursor();

        // The cursor is now positioned at the beginning of text in the last cell
        // Get at the end of table
        xViewCursor.gotoEnd(false);

        // Step out of the table
        // This also skips to the next line
        xViewCursor.goRight((short) 1, false);

        // Now we invoke a paste command
        // Here I had to do a hack, because if I did the paste command at this point,
        //  an empty line remained between the two tables
        // If someone knows how to do this without the hack, please please post it here :)
        xViewCursor.setString(" ");
        xViewCursor.collapseToStart();

        // Perform Paste command
        xFrame = asXDispatchProvider(currController.getFrame());
        xDispatchHelper.executeDispatch(xFrame, ".uno:Paste", "", 0, new PropertyValue[]{new PropertyValue()});

        // Put the cursor into the first cell of the first table
        XCellRange xTableCells = asXCellRange(xTable);
        xCellRange = xTableCells.getCellRangeByPosition(0, 0, 0, 0);
        xSelectionSupplier.select(new Any(new Type(xCellRange.getClass()), xCellRange));

        // Focus (don't know if is needed)
        currController.getFrame().getContainerWindow().setFocus();

        // Perform merge for the two tables
        xFrame = asXDispatchProvider(currController.getFrame());
        xDispatchHelper.executeDispatch(xFrame, ".uno:MergeTable", "", 0, new PropertyValue[]{new PropertyValue()});

        // Now we have to delete the space character that we have inserted before performing paste command
        // TODO
    }

    private boolean addTempColumnToTable(XTextTable xTable) {
        int columnCount = xTable.getColumns().getCount();
        if (columnCount < 2) {
            xTable.getColumns().insertByIndex(columnCount, 1);
        }
        return columnCount < 2;
    }

    private void removeTempColumFromTable(XTextTable xTable, boolean hastemp) {
        if (hastemp) {
            int count = xTable.getColumns().getCount();
            xTable.getColumns().removeByIndex(count - 1, 1);
        }
    }

    private XCellRange getRowCells(XTextTable xTable, int row) {
        XCellRange xCellRange = null;
        XMultiComponentFactory xMCF = contextGroup.getMCF();
        XComponentContext xContext = contextGroup.getContext();

        // Get current controller
        XTextDocument xTextDocument = asXTextDocument(xComponent);
        XController currController = xTextDocument.getCurrentController();

        // Get selection supplier
        XSelectionSupplier xSelectionSupplier = asXSelectionSupplier(currController);

        // Get cell names
        int columnCount = xTable.getColumns().getCount();

        XCellRange xTableCellRange = asXCellRange(xTable);
        try {
            xCellRange = xTableCellRange.getCellRangeByPosition(0, row, columnCount - 1, row);
        } catch (Exception e) {
            new ReportFormatterException(e);
        }
        return xCellRange;
    }

    /* Copy table row and add to original table
     * @param xTable working table
     * @param row row in table for copy */
    private void copyTableRow(XTextTable xTable, int row) {
        try {
            // All type casts make through ([Class])UnoRuntime.queryInterface([Class].class, object)
            XMultiComponentFactory xMCF = contextGroup.getMCF();
            XComponentContext xContext = contextGroup.getContext();

            // Get current controller
            XTextDocument xTextDocument = asXTextDocument(xComponent);
            XController currController = xTextDocument.getCurrentController();

            // Get selection supplier
            XSelectionSupplier xSelectionSupplier = asXSelectionSupplier(currController);

            // Get cell names
            int columnCount = xTable.getColumns().getCount();
            String[] arrCellNames = xTable.getCellNames();

            // Create a table cursor starting at the first cell
            String firstCell = arrCellNames[row * columnCount];
            String lastCell = arrCellNames[(row + 1) * columnCount - 1];

            // Get the table as XCellRange interface
            XCellRange xTableCellRange = asXCellRange(xTable);
            XCellRange xCellRange = null;
            XTextTableCursor xTextTableCursor = xTable.createCursorByCellName(firstCell);

            // Move cursor to the last cell in the table
            //xTextTableCursor.gotoCellByName(lastCell, true);
            // Create the cell range using the table cursor
            // String rangeName = xTextTableCursor.getRangeName();
            // xCellRange = xTableCellRange.getCellRangeByName(rangeName);
            // xSelectionSupplier.select(new Any(new Type(xCellRange.getClass()), xCellRange));

            xCellRange = xTableCellRange.getCellRangeByPosition(0, row, columnCount - 1, row);
            xSelectionSupplier.select(new Any(new Type(xCellRange.getClass()), xCellRange));

            // Get Dispatch Helper to dispatch commands
            // xContext is the local or remote context , i.e. the office context (XComponentContext)
            // one can obtain this in various ways, the simpler being Bootstrap.bootstrap()
            XDispatchHelper xDispatchHelper = asXDispatchHelper(
                    xMCF.createInstanceWithContext("com.sun.star.frame.DispatchHelper", xContext));

            // We have the selection
            // Execute copy command
            XDispatchProvider xFrame = asXDispatchProvider(currController.getFrame());
            xDispatchHelper.executeDispatch(xFrame, ".uno:Copy", "", 0, new PropertyValue[]{new PropertyValue()});

            // Move the cursor below the table, to paste the contents that we copied

            // Get the view cursor
            XTextViewCursorSupplier xCursorSupplier = asXTextCursorSupplier(currController);
            XTextViewCursor xViewCursor = xCursorSupplier.getViewCursor();

            // The cursor is now positioned at the beginning of text in the last cell
            // Get at the end of table
            xViewCursor.gotoEnd(false);

            // Step out of the table
            // This also skips to the next line
            xViewCursor.goRight((short) 1, false);

            // Now we invoke a paste command
            // Here I had to do a hack, because if I did the paste command at this point,
            //  an empty line remained between the two tables
            // If someone knows how to do this without the hack, please please post it here :)
            xViewCursor.setString(" ");
            xViewCursor.collapseToStart();

            // Perform Paste command
            xFrame = asXDispatchProvider(currController.getFrame());
            xDispatchHelper.executeDispatch(xFrame, ".uno:Paste", "", 0, new PropertyValue[]{new PropertyValue()});

            // Put the cursor into the first cell of the first table
            XCellRange xTableCells = asXCellRange(xTable);
            xCellRange = xTableCells.getCellRangeByPosition(0, 0, 0, 0);
            xSelectionSupplier.select(new Any(new Type(xCellRange.getClass()), xCellRange));

            // Focus (don't know if is needed)
            currController.getFrame().getContainerWindow().setFocus();

            // Perform merge for the two tables
            xFrame = asXDispatchProvider(currController.getFrame());
            xDispatchHelper.executeDispatch(xFrame, ".uno:MergeTable", "", 0, new PropertyValue[]{new PropertyValue()});
        }
        catch (Exception ex) {
            throw new ReportFormatterException(ex);
        }
    }

    private void copyRow(XTextTable xTable, int row) {
        try {
            // All type casts make through ([Class])UnoRuntime.queryInterface([Class].class, object)
            XMultiComponentFactory xMCF = contextGroup.getMCF();
            XComponentContext xContext = contextGroup.getContext();

            // Get current controller
            XTextDocument xTextDocument = asXTextDocument(xComponent);
            XController currController = xTextDocument.getCurrentController();

            // Get selection supplier
            XSelectionSupplier xSelectionSupplier = asXSelectionSupplier(currController);

            // Get cell names
            int columnCount = xTable.getColumns().getCount();
            String[] arrCellNames = xTable.getCellNames();

            // Create a table cursor starting at the first cell
            String firstCell = arrCellNames[row * columnCount];
            String lastCell = arrCellNames[(row + 1) * columnCount - 1];

            // Get the table as XCellRange interface
            XCellRange xTableCellRange = asXCellRange(xTable);
            XCellRange xCellRange = null;
            XTextTableCursor xTextTableCursor = xTable.createCursorByCellName(firstCell);

            // Move cursor to the last cell in the table
            //xTextTableCursor.gotoCellByName(lastCell, true);
            // Create the cell range using the table cursor
            // String rangeName = xTextTableCursor.getRangeName();
            // xCellRange = xTableCellRange.getCellRangeByName(rangeName);
            // xSelectionSupplier.select(new Any(new Type(xCellRange.getClass()), xCellRange));

            xCellRange = xTableCellRange.getCellRangeByPosition(0, row, columnCount - 1, row);
            xSelectionSupplier.select(new Any(new Type(xCellRange.getClass()), xCellRange));

            // Get Dispatch Helper to dispatch commands
            // xContext is the local or remote context , i.e. the office context (XComponentContext)
            // one can obtain this in various ways, the simpler being Bootstrap.bootstrap()
            XDispatchHelper xDispatchHelper = asXDispatchHelper(
                    xMCF.createInstanceWithContext("com.sun.star.frame.DispatchHelper", xContext));

            // We have the selection
            // Execute copy command
            XDispatchProvider xFrame = asXDispatchProvider(currController.getFrame());
            xDispatchHelper.executeDispatch(xFrame, ".uno:Copy", "", 0, new PropertyValue[]{new PropertyValue()});
        }
        catch (Exception ex) {
            throw new ReportFormatterException(ex);
        }
    }

    private void pasteRow(XTextTable xTable) {
        XMultiComponentFactory xMCF = contextGroup.getMCF();
        XComponentContext xContext = contextGroup.getContext();

        XTextDocument xTextDocument = asXTextDocument(xComponent);
        XController currController = xTextDocument.getCurrentController();

        // Get selection supplier
        XSelectionSupplier xSelectionSupplier = asXSelectionSupplier(currController);

        // Get the view cursor
        XTextViewCursorSupplier xCursorSupplier = asXTextCursorSupplier(currController);
        XTextViewCursor xViewCursor = xCursorSupplier.getViewCursor();

        // The cursor is now positioned at the beginning of text in the last cell
        // Get at the end of table
        xViewCursor.gotoEnd(false);

        // Step out of the table
        // This also skips to the next line
        xViewCursor.goRight((short) 1, false);

        // Now we invoke a paste command
        // Here I had to do a hack, because if I did the paste command at this point,
        //  an empty line remained between the two tables
        // If someone knows how to do this without the hack, please please post it here :)
        xViewCursor.setString(" ");
        xViewCursor.collapseToStart();

        // Perform Paste command
        XDispatchProvider xFrame = asXDispatchProvider(currController.getFrame());
        XDispatchHelper xDispatchHelper = null;
        try {
            xDispatchHelper = asXDispatchHelper(
                    xMCF.createInstanceWithContext("com.sun.star.frame.DispatchHelper", xContext));
            xDispatchHelper.executeDispatch(xFrame, ".uno:Paste", "", 0, new PropertyValue[]{new PropertyValue()});

            // Put the cursor into the first cell of the first table
            XCellRange xTableCells = asXCellRange(xTable);
            XCellRange xCellRange = xTableCells.getCellRangeByPosition(0, 0, 0, 0);
            xSelectionSupplier.select(new Any(new Type(xCellRange.getClass()), xCellRange));

            // Focus (don't know if is needed)
            currController.getFrame().getContainerWindow().setFocus();

            // Perform merge for the two tables
            xFrame = asXDispatchProvider(currController.getFrame());
            xDispatchHelper.executeDispatch(xFrame, ".uno:MergeTable", "", 0, new PropertyValue[]{new PropertyValue()});

            // Now we have to delete the space character that we have inserted before performing paste command
            // TODO   
        } catch (Exception e) {
            throw new ReportFormatterException(e);
        }
    }

    private void replaceBandsInRow(XTextTable xTable, int row, Band band) {
        try {
            int columnCount = xTable.getColumns().getCount();

            XTextDocument xTextDocument = asXTextDocument(xComponent);
            XReplaceable xTextRepl = asXReplaceable(xTextDocument);

            if (xTextRepl != null) {
                List<String> parametersToInsert = new ArrayList<String>();
                // Ищем параметры                    
                for (int i = 0; i < columnCount; i++) {
                    XText cellText = getCellXText(xTable, i, row);
                    String sourceStr = cellText.getString();
                    Pattern namePattern = Pattern.compile("\\$\\{.+?\\}", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = namePattern.matcher(sourceStr);
                    while (matcher.find()) {
                        parametersToInsert.add(matcher.group().replace("${", "").replace("}", ""));
                    }
                }
                // Обрабатываем найденные параметры
                for (String parameterName : parametersToInsert) {
                    //Строки для замен
                    Object value = band.getData().get(parameterName);
                    String valueStr = value != null ? value.toString() : "";
                    String search = "${" + parameterName + "}";

                    XReplaceDescriptor xRepDesc = xTextRepl.createReplaceDescriptor();
                    xRepDesc.setSearchString(search);
                    xRepDesc.setReplaceString(valueStr);

                    xTextRepl.replaceAll(xRepDesc);
                }
            }

        } catch (Exception e) {
            throw new ReportFormatterException(e);
        }
    }

    private void createTableTemplates(XTextTablesSupplier xTextTablesSupplier) throws NoSuchElementException, WrappedTargetException, IndexOutOfBoundsException {
        // todo: store whole cell as docTemplate, not only cell text
        tableTemplates.clear();
        String[] tablesNames = xTextTablesSupplier.getTextTables().getElementNames();
        XTextTable xTextTable;
        for (String tableName : tablesNames) {
//            if (bandExists(rootBand, tableName)) { ED - I think we have to look through all tables, not only with data  
            xTextTable = getTableByName(xTextTablesSupplier, tableName);
            TableTemplate tableTemplate = createTableTemplate(xTextTable, tableName);
            tableTemplates.put(tableTemplate.getTableName(), tableTemplate);
//            }
        }
        // Delete templates without value expressions
        ArrayList<String> deleteMe = new ArrayList<String>();
        for (TableTemplate tableTemplate : tableTemplates.values()) {
            if (!tableTemplate.haveValueExpressions()) {
                deleteMe.add(tableTemplate.getTableName());
            }
        }
        for (String key : deleteMe) {
            tableTemplates.remove(key);
        }
    }

    private boolean bandExists(Band band, String name) {
        if (band.getName().equals(name)) {
            return true;
        }
        for (Band child : band.getChildren()) {
            if (bandExists(child, name)) {
                return true;
            }
        }
        return false;
    }

    private TableTemplate createTableTemplate(XTextTable xTextTable, String tableName) throws com.sun.star.lang.IndexOutOfBoundsException {
        TableTemplate tableTemplate = new TableTemplate();
        tableTemplate.setTableName(tableName.trim());
        int colCount = xTextTable.getColumns().getCount();
        int lastRow = xTextTable.getRows().getCount() - 1;
        tableTemplate.setTemplateRow(lastRow);
        for (int i = 0; i < colCount; i++) {
            String cellText = getCellText(xTextTable, i, lastRow);
            tableTemplate.addColumnTemplate(i, cellText);
        }
        return tableTemplate;
    }

    private void deleteTemplateRows(XTextTablesSupplier xTextTablesSupplier) throws NoSuchElementException, WrappedTargetException {
        for (String tableName : tableTemplates.keySet()) {
            XTextTable xTextTable = getTableByName(xTextTablesSupplier, tableName);
            deleteRow(xTextTable, tableTemplates.get(tableName).getTemplateRow());
        }
    }
}

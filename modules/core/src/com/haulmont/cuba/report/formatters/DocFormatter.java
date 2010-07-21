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
import com.haulmont.cuba.report.formatters.exception.FailedToConnectToOpenOfficeAPIException;
import com.haulmont.cuba.report.formatters.tools.OOOutputStream;
import com.haulmont.cuba.report.formatters.tools.TableTemplate;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XEnumeration;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.io.XInputStream;
import com.sun.star.lang.*;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.text.*;

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
        XComponentLoader xComponentLoader;
        try {
            xComponentLoader = createXComponentLoader(openOfficePath);
        } catch (Exception e) {
            throw new FailedToConnectToOpenOfficeAPIException("Please check OpenOffice path: " + openOfficePath);
        }
        try {
            XInputStream xis = getXInputStream(docTemplate);
            XComponent xComponent = loadXComponent(xComponentLoader, xis);
            // Handling tables
            XTextTablesSupplier tablesSupplier = asXTextTablesSupplier(xComponent);
            createTableTemplates(tablesSupplier);
            fillTablesWithBandData(tablesSupplier);
            deleteTemplateRows(tablesSupplier);
            // Handling text
            replaceValueExpressionsInDocumentText(asXTextDocument(xComponent));
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

    private void replaceValueExpressionsInDocumentText(XTextDocument xTextDocument) throws NoSuchElementException, WrappedTargetException, IllegalArgumentException, PropertyVetoException, UnknownPropertyException {
        XText xText = xTextDocument.getText();
        XEnumerationAccess paragraphsAccess = asXEnumerationAccess(xText);
        XEnumeration paragraphs = paragraphsAccess.createEnumeration();
        while (paragraphs.hasMoreElements()) {
            XTextContent xTextContent = asXTextContent(paragraphs.nextElement());
            XServiceInfo xServiceInfo = asXServiceInfo(xTextContent);
            // Accessing only paragraphs, not tables
            if (!xServiceInfo.supportsService("com.sun.star.text.TextTable")) {
                XEnumerationAccess textPortionsAccess = asXEnumerationAccess(xTextContent);
                XEnumeration textPortions = textPortionsAccess.createEnumeration();
                while (textPortions.hasMoreElements()) {
                    XTextRange xTextPortion = asXTextRange(textPortions.nextElement());
                    replaceValueExpressionsInTextPortion(xTextPortion);
                }
            }
        }
    }

    private void replaceValueExpressionsInTextPortion(XTextRange xTextPortion) throws NoSuchElementException, WrappedTargetException, IllegalArgumentException, PropertyVetoException, UnknownPropertyException {
        String text = xTextPortion.getString();
        List<String> valueExpressions = new ArrayList<String>();
        Pattern namePattern = Pattern.compile(VALUE_EXPRESSION_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = namePattern.matcher(text);
        while (matcher.find()) {
            valueExpressions.add(matcher.group());
        }
        for (String valueExpression : valueExpressions) {
            String bandName = parseValueExpression(valueExpression)[0];
            String propertyName = parseValueExpression(valueExpression)[1];
            Band band = rootBand.getChildByName(bandName);
            Object bandValue = null;
            if (band != null) {
                bandValue = band.getData().get(propertyName);
            }
            if (bandValue == null) bandValue = "";
            String bandValueStr = bandValue instanceof Entity ? ((Instance) bandValue).getInstanceName() : bandValue.toString();
            text = text.replaceAll("\\$\\{" + bandName + "\\." + propertyName + "\\}", bandValueStr);
        }
        xTextPortion.setString(text);
    }

    private void fillTablesWithBandData(XTextTablesSupplier xTextTablesSupplier) throws NoSuchElementException, WrappedTargetException, IndexOutOfBoundsException {
        // Iterating over all table templates
        for (TableTemplate tableTemplate : tableTemplates.values()) {
            // Getting table corresponding to tepmlate
            XTextTable xTextTable = getTableByName(xTextTablesSupplier, tableTemplate.getTableName());
            // Finding all bands with name of table wich is now processed
            for (Band band : rootBand.getChildren()) {
                if (band.getName().trim().equals(tableTemplate.getTableName())) {
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
        }
    }

    private void createTableTemplates(XTextTablesSupplier xTextTablesSupplier) throws NoSuchElementException, WrappedTargetException, IndexOutOfBoundsException {
        // todo: store whole cell as docTemplate, not only cell text
        tableTemplates.clear();
        String[] tablesNames = xTextTablesSupplier.getTextTables().getElementNames();
        XTextTable xTextTable;
        for (String tableName : tablesNames) {
            xTextTable = getTableByName(xTextTablesSupplier, tableName);
            TableTemplate tableTemplate = createTableTemplate(xTextTable, tableName);
            tableTemplates.put(tableTemplate.getTableName(), tableTemplate);
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

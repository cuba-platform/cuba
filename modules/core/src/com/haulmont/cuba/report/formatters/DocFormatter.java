/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Vasiliy Fontanenko
 * Created: 12.10.2010 19:21:36
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters;

import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.report.Band;
import com.haulmont.cuba.report.ReportOutputType;
import com.haulmont.cuba.report.ReportValueFormat;
import com.haulmont.cuba.report.exception.ReportFormatterException;
import com.haulmont.cuba.report.formatters.doctags.HtmlContentTagHandler;
import com.haulmont.cuba.report.formatters.doctags.ImageTagHandler;
import com.haulmont.cuba.report.formatters.doctags.TagHandler;
import com.haulmont.cuba.report.formatters.exception.FailedToConnectToOpenOfficeException;
import com.haulmont.cuba.report.formatters.oo.*;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XIndexAccess;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDispatchHelper;
import com.sun.star.io.IOException;
import com.sun.star.io.XInputStream;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XComponent;
import com.sun.star.table.XCell;
import com.sun.star.text.*;
import com.sun.star.util.XReplaceable;
import com.sun.star.util.XSearchDescriptor;
import org.apache.commons.lang.StringUtils;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

import static com.haulmont.cuba.report.formatters.oo.ODTHelper.*;
import static com.haulmont.cuba.report.formatters.oo.ODTTableHelper.*;
import static com.haulmont.cuba.report.formatters.oo.ODTUnoConverter.*;

/**
 * Document formatter for '.doc' filetype
 */
public class DocFormatter extends AbstractFormatter {

    private static final String SEARCH_REGULAR_EXPRESSION = "SearchRegularExpression";
    private static final String ROOT_BAND_NAME = "Root";

    private static final String PDF_OUTPUT_FILE = "writer_pdf_Export";
    private static final String MS_WORD_OUTPUT_FILE = "MS Word 97";

    /**
     * Chain of responsibility for tags
     */
    private static List<TagHandler> tagHandlers = new ArrayList<TagHandler>();

    static {
        // Image tag
        tagHandlers.add(new ImageTagHandler());
        // HTML Content tag
        tagHandlers.add(new HtmlContentTagHandler());
    }

    private OOOConnection connection;
    private Band rootBand;

    private XComponent xComponent;
    private OfficeComponent officeComponent;

    public DocFormatter() {
        registerReportExtension("doc");
        registerReportExtension("odt");

        registerReportOutput(ReportOutputType.DOC);
        registerReportOutput(ReportOutputType.PDF);

        defaultOutputType = ReportOutputType.HTML;

        connectToOffice();
    }

    private void connectToOffice() {
        String openOfficePath = ConfigProvider.getConfig(ServerConfig.class).getOpenOfficePath();
        try {
            connection = OOOConnector.createConnection(openOfficePath);
        } catch (Exception ex) {
            throw new FailedToConnectToOpenOfficeException("Please check OpenOffice path: " + openOfficePath);
        }
    }

    public void createDocument(Band rootBand, ReportOutputType outputType, OutputStream outputStream) {

        if (templateFile == null)
            throw new NullPointerException();

        this.rootBand = rootBand;
        try {
            XInputStream xis = getXInputStream(templateFile);
            XComponentLoader xComponentLoader = connection.createXComponentLoader();
            xComponent = loadXComponent(xComponentLoader, xis);

            officeComponent = new OfficeComponent(connection, xComponentLoader, xComponent);

            // Lock clipboard
            synchronized (ClipBoardHelper.class) {
                // Handling tables
                fillTables();
            }
            // Handling text
            replaceAllAliasesInDocument();
            // Saving document to output stream and closing
            saveAndClose(xComponent, outputType, outputStream);
        } catch (java.lang.Exception ex) {
            throw new java.lang.RuntimeException(ex);
        }
    }

    private void saveAndClose(XComponent xComponent, ReportOutputType outputType, OutputStream outputStream)
            throws IOException {
        OOOutputStream ooos = new OOOutputStream(outputStream);
        String filterName;
        if (ReportOutputType.PDF.equals(outputType)) {
            filterName = PDF_OUTPUT_FILE;
        } else {
            filterName = MS_WORD_OUTPUT_FILE;
        }
        saveXComponent(xComponent, ooos, filterName);
        closeXComponent(xComponent);
    }

    private void fillTables() throws com.sun.star.uno.Exception {
        List<String> tablesNames = getTablesNames(xComponent);
        tablesNames.retainAll(rootBand.getBandDefinitionNames());

        XDispatchHelper xDispatchHelper = connection.createXDispatchHelper();
        for (String tableName : tablesNames) {
            Band band = findBand(rootBand, tableName);
            XTextTable xTextTable = getTableByName(xComponent, tableName);
            if (band != null) {
                // todo remove this hack!
                // try to select one cell without it workaround
                int columnCount = xTextTable.getColumns().getCount();
                if (columnCount < 2)
                    xTextTable.getColumns().insertByIndex(columnCount, 1);
                fillTable(tableName, band.getParentBand(), xTextTable, xDispatchHelper);
                // end of workaround ->
                if (columnCount < 2)
                    xTextTable.getColumns().removeByIndex(columnCount, 1);
            } else {
                if (haveValueExpressions(xTextTable))
                    deleteLastRow(xTextTable);
            }
        }
    }

    public boolean haveValueExpressions(XTextTable xTextTable) {
        int lastrow = xTextTable.getRows().getCount() - 1;
        try {
            for (int i = 0; i < xTextTable.getColumns().getCount(); i++) {
                String templateText = asXText(ODTTableHelper.getXCell(xTextTable, i, lastrow)).getString();
                if (namePattern.matcher(templateText).find()) {
                    return true;
                }
            }
        } catch (Exception e) {
            throw new ReportFormatterException(e);
        }
        return false;
    }

    private void fillTable(String name, Band parentBand, XTextTable xTextTable, XDispatchHelper xDispatchHelper)
            throws com.sun.star.uno.Exception {
        boolean displayDeviceUnavailable = ConfigProvider.getConfig(ServerConfig.class).getDisplayDeviceUnavailable();
        if (!displayDeviceUnavailable) {
            ClipBoardHelper.clear();
        }
        int startRow = xTextTable.getRows().getCount() - 1;
        List<Band> childrenBands = parentBand.getChildrenList();
        for (Band child : childrenBands) {
            if (name.equals(child.getName())) {
                duplicateLastRow(xDispatchHelper, asXTextDocument(xComponent).getCurrentController(), xTextTable);
            }
        }
        int i = startRow;
        for (Band child : childrenBands) {
            if (name.equals(child.getName())) {
                fillRow(child, xTextTable, i);
                i++;
            }
        }
        deleteLastRow(xTextTable);
    }

    private void fillRow(Band band, XTextTable xTextTable, int row)
            throws com.sun.star.lang.IndexOutOfBoundsException, NoSuchElementException, WrappedTargetException {
        int colCount = xTextTable.getColumns().getCount();
        for (int col = 0; col < colCount; col++) {
            fillCell(band, getXCell(xTextTable, col, row));
        }
    }

    private void fillCell(Band band, XCell xCell) throws NoSuchElementException, WrappedTargetException {
        String cellText = preformatCellText(asXText(xCell).getString());
        List<String> parametersToInsert = new ArrayList<String>();
        Matcher matcher = namePattern.matcher(cellText);
        while (matcher.find()) {
            parametersToInsert.add(unwrapParameterName(matcher.group()));
        }
        for (String parameterName : parametersToInsert) {
            XText xText = asXText(xCell);
            XTextCursor xTextCursor = xText.createTextCursor();

            String paramStr = "${" + parameterName + "}";
            int index = cellText.indexOf(paramStr);
            while (index >= 0) {
                xTextCursor.gotoStart(false);
                xTextCursor.goRight((short) (index + paramStr.length()), false);
                xTextCursor.goLeft((short) paramStr.length(), true);

                insertValue(xText, xTextCursor, band, parameterName);
                cellText = preformatCellText(xText.getString());

                index = cellText.indexOf(paramStr);
            }
        }
    }

    private String formatString(Object value, String valueName) {
        String valueString = "";
        HashMap<String, ReportValueFormat> formats = rootBand.getValuesFormats();
        if (value != null) {
            if (formats != null) {
                if (formats.containsKey(valueName)) {
                    String formatString = formats.get(valueName).getFormatString();
                    if (value instanceof Number) {
                        DecimalFormat decimalFormat = new DecimalFormat(formatString);
                        valueString = decimalFormat.format(value);
                    } else if (value instanceof Date) {
                        SimpleDateFormat dateformat = new SimpleDateFormat(formatString);
                        valueString = dateformat.format(value);
                    } else
                        valueString = value.toString();
                } else {
                    if (value instanceof Date)
                        valueString = defaultDateFormat(value);
                    else
                        valueString = value.toString();
                }
            } else if (value instanceof Date)
                valueString = defaultDateFormat(value);
        }
        return valueString;
    }

    private String defaultDateFormat(Object value) {
        String valueString = "";
        String defaultDateFormat = "dd.MM.yyyy";
        if (value != null)
            if (value instanceof Date) {
                SimpleDateFormat dateformat = new SimpleDateFormat(defaultDateFormat);
                dateformat.applyPattern(defaultDateFormat);
                valueString = dateformat.format(value);
            } else
                valueString = value.toString();
        return valueString;
    }

    /**
     * Replaces all aliases ${bandname.paramname} in document text.
     *
     * @throws ReportFormatterException If there is not appropriate band or alias is bad
     */
    private void replaceAllAliasesInDocument() {
        XTextDocument xTextDocument = asXTextDocument(xComponent);
        XReplaceable xReplaceable = asXReplaceable(xTextDocument);
        XSearchDescriptor searchDescriptor = xReplaceable.createSearchDescriptor();
        searchDescriptor.setSearchString(ALIAS_WITH_BAND_NAME_PATTERN);
        try {
            searchDescriptor.setPropertyValue(SEARCH_REGULAR_EXPRESSION, true);
            XIndexAccess indexAccess = xReplaceable.findAll(searchDescriptor);
            for (int i = 0; i < indexAccess.getCount(); i++) {
                XTextRange textRange = asXTextRange(indexAccess.getByIndex(i));
                String alias = unwrapParameterName(textRange.getString());
                String[] parts = alias.split("\\.");

                if (parts == null || parts.length < 2)
                    throw new ReportFormatterException("Bad alias : " + textRange.getString());

                String bandName = parts[0];
                Band band = bandName.equals(ROOT_BAND_NAME) ? rootBand : rootBand.getChildByName(bandName);

                if (band == null)
                    throw new ReportFormatterException("No band for alias : " + alias);

                String paramName = StringUtils.join(parts, '.', 1, parts.length);
                insertValue(textRange.getText(), textRange, band, paramName);
            }
        } catch (Exception ex) {
            throw new ReportFormatterException(ex);
        }
    }

    private void insertValue(XText text, XTextRange textRange, Band band, String paramName) {
        String fullParamName = band.getFullName() + "." + paramName;
        Object paramValue = band.getParameter(paramName);

        HashMap<String, ReportValueFormat> formats = rootBand.getValuesFormats();
        try {
            boolean handled = false;

            if (paramValue != null) {
                if ((formats != null) && (formats.containsKey(fullParamName))) {
                    String format = formats.get(fullParamName).getFormatString();
                    // Handle doctags
                    for (TagHandler tagHandler : tagHandlers) {
                        Matcher matcher = tagHandler.getTagPattern().matcher(format);
                        if (matcher.find()) {
                            tagHandler.handleTag(officeComponent, text, textRange, paramValue, matcher);
                            handled = true;
                        }
                    }
                }
                if (!handled) {
                    String valueString = formatString(paramValue, fullParamName);
                    text.insertString(textRange, valueString, true);
                }
            } else
                text.insertString(textRange, "", true);
        } catch (Exception ex) {
            throw new ReportFormatterException("Insert data error");
        }
    }

    private Band findBand(Band band, String name) {
        if (band.getName().equals(name)) {
            return band;
        }
        for (Band child : band.getChildrenList()) {
            Band fromChild = findBand(child, name);
            if (fromChild != null) {
                return fromChild;
            }
        }
        return null;
    }
}
/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.gui.export;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.IdProxy;
import com.haulmont.cuba.core.entity.annotation.IgnoreUserTimeZone;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.data.GroupInfo;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.util.LocaleUtil;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Use this class to export {@link com.haulmont.cuba.gui.components.Table} into Excel format
 * and show using {@link ExportDisplay}.
 * <br>Just create an instance of this class and invoke one of <code>exportTable</code> methods.
 */
public class ExcelExporter {
    protected static final int COL_WIDTH_MAGIC = 48;

    private static final int SPACE_COUNT = 10;

    public static final int MAX_ROW_COUNT = 65535;

    protected HSSFWorkbook wb;

    protected HSSFFont boldFont;

    protected HSSFFont stdFont;

    protected HSSFSheet sheet;

    protected HSSFCellStyle timeFormatCellStyle;

    protected HSSFCellStyle dateFormatCellStyle;

    protected HSSFCellStyle dateTimeFormatCellStyle;

    protected HSSFCellStyle integerFormatCellStyle;

    protected HSSFCellStyle doubleFormatCellStyle;

    protected ExcelAutoColumnSizer[] sizers;

    protected String trueStr;

    protected String falseStr;

    protected boolean exportAggregation = true;

    protected final Messages messages;

    protected final UserSessionSource userSessionSource;

    protected final MetadataTools metadataTools;

    protected boolean isRowNumberExceeded = false;

    public enum ExportMode {
        SELECTED_ROWS,
        ALL_ROWS
    }

    public ExcelExporter() {
        messages = AppBeans.get(Messages.NAME);
        userSessionSource = AppBeans.get(UserSessionSource.NAME);
        metadataTools = AppBeans.get(MetadataTools.NAME);

        trueStr = messages.getMessage(ExcelExporter.class, "excelExporter.true");
        falseStr = messages.getMessage(ExcelExporter.class, "excelExporter.false");
    }

    public void exportTable(Table table, ExportDisplay display) {
        exportTable(table, table.getColumns(), display);
    }

    public void exportTable(Table table, List<Table.Column> columns, ExportDisplay display, ExportMode exportMode) {
        exportTable(table, columns, false, display, null, null, exportMode);
    }

    protected void createWorkbookWithSheet() {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet("Export");
    }

    protected void createFonts() {
        stdFont = wb.createFont();
        boldFont = wb.createFont();
        boldFont.setBold(true);
    }

    protected void createAutoColumnSizers(int count) {
        sizers = new ExcelAutoColumnSizer[count];
    }

    public void exportTable(Table table, List<Table.Column> columns, ExportDisplay display) {
        exportTable(table, columns, false, display);
    }

    public void exportTable(Table table, List<Table.Column> columns, Boolean exportExpanded, ExportDisplay display) {
        exportTable(table, columns, exportExpanded, display, null);
    }

    public void exportTable(Table table, List<Table.Column> columns, Boolean exportExpanded,
                            ExportDisplay display, List<String> filterDescription) {
        exportTable(table, columns, exportExpanded, display, filterDescription, null, ExportMode.ALL_ROWS);
    }

    public void exportTable(Table<Entity> table, List<Table.Column> columns, Boolean exportExpanded,
                            ExportDisplay display, List<String> filterDescription, String fileName, ExportMode exportMode) {

        if (display == null) {
            throw new IllegalArgumentException("ExportDisplay is null");
        }

        createWorkbookWithSheet();
        createFonts();
        createFormats();

        int r = 0;
        if (filterDescription != null) {
            for (r = 0; r < filterDescription.size(); r++) {
                String line = filterDescription.get(r);
                HSSFRow row = sheet.createRow(r);
                if (r == 0) {
                    HSSFRichTextString richTextFilterName = new HSSFRichTextString(line);
                    richTextFilterName.applyFont(boldFont);
                    row.createCell(0).setCellValue(richTextFilterName);
                } else {
                    row.createCell(0).setCellValue(line);
                }
            }
            r++;
        }
        HSSFRow row = sheet.createRow(r);
        createAutoColumnSizers(columns.size());

        float maxHeight = sheet.getDefaultRowHeightInPoints();

        CellStyle headerCellStyle = wb.createCellStyle();
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        for (Table.Column column : columns) {
            String caption = column.getCaption();

            int countOfReturnSymbols = StringUtils.countMatches(caption, "\n");
            if (countOfReturnSymbols > 0) {
                maxHeight = Math.max(maxHeight, (countOfReturnSymbols + 1) * sheet.getDefaultRowHeightInPoints());
                headerCellStyle.setWrapText(true);
            }
        }
        row.setHeightInPoints(maxHeight);

        for (int c = 0; c < columns.size(); c++) {
            Table.Column column = columns.get(c);
            String caption = column.getCaption();

            HSSFCell cell = row.createCell(c);
            HSSFRichTextString richTextString = new HSSFRichTextString(caption);
            richTextString.applyFont(boldFont);
            cell.setCellValue(richTextString);

            ExcelAutoColumnSizer sizer = new ExcelAutoColumnSizer();
            sizer.notifyCellValue(caption, boldFont);
            sizers[c] = sizer;

            cell.setCellStyle(headerCellStyle);
        }

        CollectionDatasource datasource = table.getDatasource();
        if (exportMode == ExportMode.SELECTED_ROWS && table.getSelected().size() > 0) {
            Set<Entity> selected = table.getSelected();
            List<Entity> ordered = ((Collection<Entity>) datasource.getItems()).stream()
                    .filter(selected::contains)
                    .collect(Collectors.toList());
            for (Entity item : ordered) {
                if (checkIsRowNumberExceed(r)) {
                    break;
                }

                createRow(table, columns, 0, ++r, item.getId());
            }
        } else {
            if (table.isAggregatable() && exportAggregation) {
                if(table.getAggregationStyle() == Table.AggregationStyle.TOP) {
                    r = createAggregatableRow(table, columns, ++r, 1, datasource);
                }
            }
            if (table instanceof TreeTable) {
                TreeTable treeTable = (TreeTable) table;
                HierarchicalDatasource ds = treeTable.getDatasource();
                for (Object itemId : ds.getRootItemIds()) {
                    if (checkIsRowNumberExceed(r)) {
                        break;
                    }

                    r = createHierarhicalRow(treeTable, columns, exportExpanded, r, itemId);
                }
            } else if (table instanceof GroupTable && datasource instanceof GroupDatasource
                    && ((GroupDatasource) datasource).hasGroups()) {
                GroupDatasource ds = (GroupDatasource) datasource;

                for (Object item : ds.rootGroups()) {
                    if (checkIsRowNumberExceed(r)) {
                        break;
                    }

                    r = createGroupRow((GroupTable) table, columns, ++r, (GroupInfo) item, 0);
                }
            } else {
                for (Object itemId : datasource.getItemIds()) {
                    if (checkIsRowNumberExceed(r)) {
                        break;
                    }

                    createRow(table, columns, 0, ++r, itemId);
                }
            }
            if (table.isAggregatable() && exportAggregation) {
                if(table.getAggregationStyle() == Table.AggregationStyle.BOTTOM) {
                    r = createAggregatableRow(table, columns, ++r, 1, datasource);
                }
            }
        }

        for (int c = 0; c < columns.size(); c++) {
            sheet.setColumnWidth(c, sizers[c].getWidth() * COL_WIDTH_MAGIC);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            wb.write(out);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write document", e);
        }
        if (fileName == null) {
            fileName = messages.getTools().getEntityCaption(datasource.getMetaClass());
        }

        display.show(new ByteArrayDataProvider(out.toByteArray()), fileName + ".xls", ExportFormat.XLS);
    }

    public void exportDataGrid(DataGrid dataGrid, ExportDisplay display) {
        exportDataGrid(dataGrid, dataGrid.getColumns(), display);
    }

    public void exportDataGrid(DataGrid dataGrid, List<DataGrid.Column> columns, ExportDisplay display) {
        exportDataGrid(dataGrid, columns, display, null, null, ExportMode.ALL_ROWS);
    }

    public void exportDataGrid(DataGrid dataGrid, List<DataGrid.Column> columns, ExportDisplay display,
                               ExportMode exportMode) {
        exportDataGrid(dataGrid, columns, display, null, null, exportMode);
    }

    public void exportDataGrid(DataGrid dataGrid, List<DataGrid.Column> columns, ExportDisplay display,
                               List<String> filterDescription) {
        exportDataGrid(dataGrid, columns, display, filterDescription, null, ExportMode.ALL_ROWS);
    }

    public void exportDataGrid(DataGrid<Entity> dataGrid, List<DataGrid.Column> columns,
                               ExportDisplay display, List<String> filterDescription,
                               String fileName, ExportMode exportMode) {
        if (display == null) {
            throw new IllegalArgumentException("ExportDisplay is null");
        }

        createWorkbookWithSheet();
        createFonts();
        createFormats();

        int r = 0;
        if (filterDescription != null) {
            for (r = 0; r < filterDescription.size(); r++) {
                String line = filterDescription.get(r);
                HSSFRow row = sheet.createRow(r);
                if (r == 0) {
                    HSSFRichTextString richTextFilterName = new HSSFRichTextString(line);
                    richTextFilterName.applyFont(boldFont);
                    row.createCell(0).setCellValue(richTextFilterName);
                } else {
                    row.createCell(0).setCellValue(line);
                }
            }
            r++;
        }
        HSSFRow row = sheet.createRow(r);
        createAutoColumnSizers(columns.size());

        float maxHeight = sheet.getDefaultRowHeightInPoints();

        CellStyle headerCellStyle = wb.createCellStyle();
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        for (DataGrid.Column column : columns) {
            String caption = column.getCaption();

            int countOfReturnSymbols = StringUtils.countMatches(caption, "\n");
            if (countOfReturnSymbols > 0) {
                maxHeight = Math.max(maxHeight, (countOfReturnSymbols + 1) * sheet.getDefaultRowHeightInPoints());
                headerCellStyle.setWrapText(true);
            }
        }
        row.setHeightInPoints(maxHeight);

        for (int c = 0; c < columns.size(); c++) {
            DataGrid.Column column = columns.get(c);
            String caption = column.getCaption();

            HSSFCell cell = row.createCell(c);
            HSSFRichTextString richTextString = new HSSFRichTextString(caption);
            richTextString.applyFont(boldFont);
            cell.setCellValue(richTextString);

            ExcelAutoColumnSizer sizer = new ExcelAutoColumnSizer();
            sizer.notifyCellValue(caption, boldFont);
            sizers[c] = sizer;

            cell.setCellStyle(headerCellStyle);
        }

        CollectionDatasource datasource = dataGrid.getDatasource();
        if (exportMode == ExportMode.SELECTED_ROWS && dataGrid.getSelected().size() > 0) {
            Set<Entity> selected = dataGrid.getSelected();
            List<Entity> ordered = ((Collection<Entity>) datasource.getItems()).stream()
                    .filter(selected::contains)
                    .collect(Collectors.toList());
            for (Entity item : ordered) {
                if (checkIsRowNumberExceed(r)) {
                    break;
                }

                createDataGridRow(dataGrid, columns, 0, ++r, item.getId());
            }
        } else {
            for (Object itemId : datasource.getItemIds()) {
                if (checkIsRowNumberExceed(r)) {
                    break;
                }

                createDataGridRow(dataGrid, columns, 0, ++r, itemId);
            }
        }

        for (int c = 0; c < columns.size(); c++) {
            sheet.setColumnWidth(c, sizers[c].getWidth() * COL_WIDTH_MAGIC);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            wb.write(out);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write document", e);
        }
        if (fileName == null) {
            fileName = messages.getTools().getEntityCaption(datasource.getMetaClass());
        }

        display.show(new ByteArrayDataProvider(out.toByteArray()), fileName + ".xls", ExportFormat.XLS);
    }

    protected void createFormats() {
        timeFormatCellStyle = wb.createCellStyle();
        timeFormatCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("h:mm"));

        dateFormatCellStyle = wb.createCellStyle();
        dateFormatCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));

        dateTimeFormatCellStyle = wb.createCellStyle();
        dateTimeFormatCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));

        integerFormatCellStyle = wb.createCellStyle();
        integerFormatCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

        DataFormat format = wb.createDataFormat();
        doubleFormatCellStyle = wb.createCellStyle();
        doubleFormatCellStyle.setDataFormat(format.getFormat("#,##0.################"));
    }

    @SuppressWarnings("unchecked")
    protected int createHierarhicalRow(TreeTable table, List<Table.Column> columns,
                                       Boolean exportExpanded, int rowNumber, Object itemId) {
        HierarchicalDatasource hd = table.getDatasource();
        createRow(table, columns, 0, ++rowNumber, itemId);
        if (BooleanUtils.isTrue(exportExpanded) && !table.isExpanded(itemId) && !hd.getChildren(itemId).isEmpty()) {
            return rowNumber;
        } else {
            Collection children = hd.getChildren(itemId);
            if (children != null && !children.isEmpty()) {
                for (Object id : children) {
                    if (BooleanUtils.isTrue(exportExpanded) && !table.isExpanded(id) && !hd.getChildren(id).isEmpty()) {
                        createRow(table, columns, 0, ++rowNumber, id);
                        continue;
                    }
                    rowNumber = createHierarhicalRow(table, columns, exportExpanded, rowNumber, id);
                }
            }
        }
        return rowNumber;
    }

    @SuppressWarnings("unchecked")
    protected int createAggregatableRow(Table table, List<Table.Column> columns, int rowNumber,
                                        int aggregatableRow, CollectionDatasource datasource) {
        HSSFRow row = sheet.createRow(rowNumber);
        Map<Object, Object> results = table.getAggregationResults();

        int i = 0;
        for (Table.Column column : columns) {
            AggregationInfo agr = column.getAggregation();
            if (agr != null) {
                Object key = agr.getPropertyPath() != null ? agr.getPropertyPath() : column.getId();
                Object aggregationResult = results.get(key);
                if (aggregationResult != null) {
                    HSSFCell cell = row.createCell(i);
                    formatValueCell(cell, aggregationResult, null, i, rowNumber, 0, null);
                }
            }
            i++;
        }
        return rowNumber;
    }

    @SuppressWarnings("unchecked")
    protected int createGroupRow(GroupTable table, List<Table.Column> columns, int rowNumber,
                                 GroupInfo groupInfo, int groupNumber) {
        GroupDatasource ds = table.getDatasource();

        HSSFRow row = sheet.createRow(rowNumber);
        Map<Object, Object> aggregations = table.isAggregatable()
                ? table.getAggregationResults(groupInfo)
                : Collections.emptyMap();

        int i = 0;
        int initialGroupNumber = groupNumber;
        for (Table.Column column : columns) {
            if (i == initialGroupNumber) {
                HSSFCell cell = row.createCell(i);
                Object val = groupInfo.getValue();

                if (val == null) {
                    val = messages.getMessage(getClass(), "excelExporter.empty");
                }

                Collection children = table.getDatasource().getGroupItemIds(groupInfo);
                if (children.isEmpty()) {
                    return rowNumber;
                }

                Integer groupChildCount = null;
                if (table.isShowItemsCountForGroup()) {
                    groupChildCount = children.size();
                }

                Object captionValue = val;

                Element xmlDescriptor = column.getXmlDescriptor();
                if (xmlDescriptor != null && StringUtils.isNotEmpty(xmlDescriptor.attributeValue("captionProperty"))) {
                    String captionProperty = xmlDescriptor.attributeValue("captionProperty");

                    Object itemId = children.iterator().next();
                    Instance item = ds.getItemNN(itemId);
                    captionValue = item.getValueEx(captionProperty);
                }

                @SuppressWarnings("unchecked")
                GroupTable.GroupCellValueFormatter<Entity> groupCellValueFormatter =
                        table.getGroupCellValueFormatter();

                if (groupCellValueFormatter != null) {
                    // disable separate "(N)" printing
                    groupChildCount = null;

                    List<Entity> groupItems = ((Collection<Object>) ds.getGroupItemIds(groupInfo)).stream()
                            .map((Function<Object, Entity>) ds::getItem)
                            .collect(Collectors.toList());

                    GroupTable.GroupCellContext<Entity> cellContext = new GroupTable.GroupCellContext<>(
                            groupInfo, captionValue, metadataTools.format(captionValue), groupItems
                    );

                    captionValue = groupCellValueFormatter.format(cellContext);
                }

                MetaPropertyPath columnId = (MetaPropertyPath) column.getId();
                formatValueCell(cell, captionValue, columnId, groupNumber++, rowNumber, 0, groupChildCount);
            } else {
                AggregationInfo agr = column.getAggregation();
                if (agr != null) {
                    Object key = agr.getPropertyPath() != null ? agr.getPropertyPath() : column.getId();
                    Object aggregationResult = aggregations.get(key);
                    if (aggregationResult != null) {
                        HSSFCell cell = row.createCell(i);
                        formatValueCell(cell, aggregationResult, null, i, rowNumber, 0, null);
                    }
                }
            }

            i++;
        }

        int oldRowNumber = rowNumber;
        List<GroupInfo> children = ds.getChildren(groupInfo);
        if (children.size() > 0) {
            for (GroupInfo child : children) {
                rowNumber = createGroupRow(table, columns, ++rowNumber, child, groupNumber);
            }
        } else {
            Collection<Object> itemIds = ds.getGroupItemIds(groupInfo);
            for (Object itemId : itemIds) {
                createRow(table, columns, groupNumber, ++rowNumber, itemId);
            }
        }

        if (checkIsRowNumberExceed(rowNumber)) {
            sheet.groupRow(oldRowNumber + 1, MAX_ROW_COUNT);
        } else {
            sheet.groupRow(oldRowNumber + 1, rowNumber);
        }

        return rowNumber;
    }

    @SuppressWarnings("unchecked")
    protected void createRow(Table table, List<Table.Column> columns, int startColumn, int rowNumber, Object itemId) {
        if (startColumn >= columns.size()) {
            return;
        }

        if (rowNumber > MAX_ROW_COUNT) {
            return;
        }

        HSSFRow row = sheet.createRow(rowNumber);
        Instance instance = table.getDatasource().getItem(itemId);

        int level = 0;
        if (table instanceof TreeTable) {
            level = ((TreeTable) table).getLevel(itemId);
        }
        for (int c = startColumn; c < columns.size(); c++) {
            HSSFCell cell = row.createCell(c);

            Table.Column column = columns.get(c);
            Object cellValue = null;

            MetaPropertyPath propertyPath = null;
            if (column.getId() instanceof MetaPropertyPath) {
                propertyPath = (MetaPropertyPath) column.getId();

                Table.Printable printable = table.getPrintable(column);
                if (printable != null) {
                    cellValue = printable.getValue((Entity) instance);
                } else {
                    Element xmlDescriptor = column.getXmlDescriptor();
                    if (xmlDescriptor != null && StringUtils.isNotEmpty(xmlDescriptor.attributeValue("captionProperty"))) {
                        String captionProperty = xmlDescriptor.attributeValue("captionProperty");
                        cellValue = InstanceUtils.getValueEx(instance, captionProperty);
                    } else {
                        cellValue = InstanceUtils.getValueEx(instance, propertyPath.getPath());
                    }
                    if (column.getFormatter() != null)
                        cellValue = column.getFormatter().format(cellValue);
                }
            } else {
                Table.Printable printable = table.getPrintable(column);
                if (printable != null) {
                    cellValue = printable.getValue((Entity) instance);
                }
            }

            formatValueCell(cell, cellValue, propertyPath, c, rowNumber, level, null);
        }
    }

    protected void createDataGridRow(DataGrid dataGrid, List<DataGrid.Column> columns,
                                     int startColumn, int rowNumber, Object itemId) {
        if (startColumn >= columns.size()) {
            return;
        }
        HSSFRow row = sheet.createRow(rowNumber);
        Instance instance = dataGrid.getDatasource().getItem(itemId);

        int level = 0;
        for (int c = startColumn; c < columns.size(); c++) {
            HSSFCell cell = row.createCell(c);

            DataGrid.Column column = columns.get(c);
            Object cellValue = null;

            MetaPropertyPath propertyPath = null;
            DataGrid.ColumnGenerator generator;
            if (column.getPropertyPath() != null) {
                propertyPath = column.getPropertyPath();

                cellValue = InstanceUtils.getValueEx(instance, propertyPath.getPath());

                if (column.getFormatter() != null) {
                    cellValue = column.getFormatter().format(cellValue);
                }
            } else if ((generator = dataGrid.getColumnGenerator(column.getId())) != null) {
                DataGrid.ColumnGeneratorEvent event =
                        new DataGrid.ColumnGeneratorEvent(dataGrid, instance, column.getId());
                cellValue = generator.getValue(event);

                if (cellValue == null && Boolean.class.equals(generator.getType())) {
                    cellValue = false;
                }
            }

            formatValueCell(cell, cellValue, propertyPath, c, rowNumber, level, null);
        }
    }

    protected String createSpaceString(int level) {
        if (level == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level * SPACE_COUNT; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    protected void formatValueCell(HSSFCell cell, @Nullable Object cellValue, @Nullable MetaPropertyPath metaPropertyPath,
                                   int sizersIndex, int notificationRequired, int level, @Nullable Integer groupChildCount) {

        if (cellValue == null) {
            if (metaPropertyPath != null
                    && metaPropertyPath.getRange().isDatatype()) {
                Class javaClass = metaPropertyPath.getRange().asDatatype().getJavaClass();
                if (Boolean.class.equals(javaClass)) {
                    cellValue = false;
                }
            } else {
                return;
            }
        }

        String childCountValue = "";
        if (groupChildCount != null) {
            childCountValue = " (" + groupChildCount + ")";
        }

        if (cellValue instanceof IdProxy) {
            cellValue = ((IdProxy) cellValue).get();
        }

        if (cellValue instanceof Number) {
            Number n = (Number) cellValue;
            Datatype datatype = null;
            if (metaPropertyPath != null) {
                Range range = metaPropertyPath.getMetaProperty().getRange();
                if (range.isDatatype()) {
                    datatype = range.asDatatype();
                }
            }

            datatype = datatype == null ? Datatypes.getNN(n.getClass()) : datatype;
            String str;
            // level is used for TreeTable, so level with 0 doesn't create spacing
            // and we should skip it
            if (sizersIndex == 0 && level > 0) {
                str = createSpaceString(level) + datatype.format(n);
                cell.setCellValue(str);
            } else {
                try {
                    str = datatype.format(n);
                    Number result = (Number) datatype.parse(str);
                    if (result != null) {
                        if (n instanceof Integer || n instanceof Long || n instanceof Byte || n instanceof Short) {
                            cell.setCellValue(result.longValue());
                            cell.setCellStyle(integerFormatCellStyle);
                        } else {
                            cell.setCellValue(result.doubleValue());
                            cell.setCellStyle(doubleFormatCellStyle);
                        }
                    }
                } catch (ParseException e) {
                    throw new RuntimeException("Unable to parse numeric value", e);
                }
                cell.setCellType(CellType.NUMERIC);
            }
            if (sizers[sizersIndex].isNotificationRequired(notificationRequired)) {
                sizers[sizersIndex].notifyCellValue(str, stdFont);
            }
        } else if (cellValue instanceof Date) {
            Class javaClass = null;
            boolean supportTimezones = false;
            TimeZone timeZone = userSessionSource.getUserSession().getTimeZone();
            if (metaPropertyPath != null) {
                MetaProperty metaProperty = metaPropertyPath.getMetaProperty();
                if (metaProperty.getRange().isDatatype()) {
                    javaClass = metaProperty.getRange().asDatatype().getJavaClass();
                }
                Boolean ignoreUserTimeZone = metadataTools.getMetaAnnotationValue(metaProperty, IgnoreUserTimeZone.class);
                supportTimezones = timeZone != null
                        && Objects.equals(Date.class, javaClass)
                        && !Boolean.TRUE.equals(ignoreUserTimeZone);
            }
            Date date = (Date) cellValue;
            if (supportTimezones) {
                TimeZone currentTimeZone = LocaleUtil.getUserTimeZone();
                try {
                    LocaleUtil.setUserTimeZone(timeZone);
                    cell.setCellValue(date);
                } finally {
                    if (Objects.equals(currentTimeZone, TimeZone.getDefault())) {
                        LocaleUtil.resetUserTimeZone();
                    } else {
                        LocaleUtil.setUserTimeZone(currentTimeZone);
                    }
                }
            } else {
                cell.setCellValue(date);
            }

            if (Objects.equals(java.sql.Time.class, javaClass)) {
                cell.setCellStyle(timeFormatCellStyle);
            } else if (Objects.equals(java.sql.Date.class, javaClass)) {
                cell.setCellStyle(dateFormatCellStyle);
            } else {
                cell.setCellStyle(dateTimeFormatCellStyle);
            }
            if (sizers[sizersIndex].isNotificationRequired(notificationRequired)) {
                String str = Datatypes.getNN(Date.class).format(date);
                sizers[sizersIndex].notifyCellValue(str, stdFont);
            }
        } else if (cellValue instanceof Boolean) {
            String str = "";
            if (sizersIndex == 0) {
                str += createSpaceString(level);
            }
            str += ((Boolean) cellValue) ? trueStr : falseStr;
            cell.setCellValue(new HSSFRichTextString(str));
            if (sizers[sizersIndex].isNotificationRequired(notificationRequired)) {
                sizers[sizersIndex].notifyCellValue(str, stdFont);
            }
        } else if (cellValue instanceof EnumClass) {
            String nameKey = cellValue.getClass().getSimpleName() + "." + cellValue.toString();
            final String message = sizersIndex == 0 ? createSpaceString(level) + messages.getMessage(cellValue.getClass(), nameKey)
                    : messages.getMessage(cellValue.getClass(), nameKey);

            cell.setCellValue(message + childCountValue);
            if (sizers[sizersIndex].isNotificationRequired(notificationRequired)) {
                sizers[sizersIndex].notifyCellValue(message, stdFont);
            }
        } else if (cellValue instanceof Entity) {
            Entity entityVal = (Entity) cellValue;
            String instanceName = entityVal.getInstanceName();
            String str = sizersIndex == 0 ? createSpaceString(level) + instanceName : instanceName;
            str = str + childCountValue;
            cell.setCellValue(new HSSFRichTextString(str));
            if (sizers[sizersIndex].isNotificationRequired(notificationRequired)) {
                sizers[sizersIndex].notifyCellValue(str, stdFont);
            }
        } else if (cellValue instanceof Collection) {
            String str = "";
            cell.setCellValue(new HSSFRichTextString(str));
            if (sizers[sizersIndex].isNotificationRequired(notificationRequired)) {
                sizers[sizersIndex].notifyCellValue(str, stdFont);
            }
        } else {
            String strValue = cellValue == null ? "" : cellValue.toString();
            String str = sizersIndex == 0 ? createSpaceString(level) + strValue : strValue;
            str = str + childCountValue;
            cell.setCellValue(new HSSFRichTextString(str));
            if (sizers[sizersIndex].isNotificationRequired(notificationRequired)) {
                sizers[sizersIndex].notifyCellValue(str, stdFont);
            }
        }
    }

    protected boolean checkIsRowNumberExceed(int r) {
        return isRowNumberExceeded = r >= MAX_ROW_COUNT;
    }

    /**
     * @return true if exported table contains more than 65536 records
     */
    public boolean isXlsMaxRowNumberExceeded() {
        return isRowNumberExceeded;
    }

    public void setExportAggregation(boolean exportAggregation) {
        this.exportAggregation = exportAggregation;
    }

    public boolean getExportAggregation() {
        return exportAggregation;
    }
}
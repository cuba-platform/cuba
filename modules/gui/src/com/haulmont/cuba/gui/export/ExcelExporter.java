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
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TreeTable;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.data.GroupInfo;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.dom4j.Element;

import javax.annotation.Nullable;
import javax.persistence.TemporalType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Use this class to export {@link com.haulmont.cuba.gui.components.Table} into Excel format
 * and show using {@link ExportDisplay}.
 * <br>Just create an instance of this class and invoke one of <code>exportTable</code> methods.
 *
 */
public class ExcelExporter {
    protected static final int COL_WIDTH_MAGIC = 48;

    private static final int SPACE_COUNT = 10;

    protected HSSFWorkbook wb;

    protected HSSFFont boldFont;

    private HSSFFont stdFont;

    protected HSSFSheet sheet;

    private HSSFCellStyle timeFormatCellStyle;

    private HSSFCellStyle dateFormatCellStyle;

    private HSSFCellStyle integerFormatCellStyle;

    private HSSFCellStyle doubleFormatCellStyle;

    protected ExcelAutoColumnSizer[] sizers;

    private final String trueStr;

    private final String falseStr;

    private final Messages messages;

    public enum ExportMode {
        SELECTED_ROWS,
        ALL_ROWS
    }

    public ExcelExporter() {
        messages = AppBeans.get(Messages.NAME);

        trueStr = messages.getMessage(getClass(), "excelExporter.true");
        falseStr = messages.getMessage(getClass(), "excelExporter.false");
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
        boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
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
        headerCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
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
            for (Entity item : table.getSelected()) {
                createRow(table, columns, 0, ++r, item.getId());
            }
        } else {
            if (table instanceof TreeTable) {
                TreeTable treeTable = (TreeTable) table;
                HierarchicalDatasource ds = treeTable.getDatasource();
                for (Object itemId : ds.getRootItemIds()) {
                    r = createHierarhicalRow(treeTable, columns, exportExpanded, r, itemId);
                }
            } else if (table instanceof GroupTable && datasource instanceof GroupDatasource
                    && ((GroupDatasource) datasource).hasGroups()) {
                GroupDatasource ds = (GroupDatasource) datasource;
                for (Object item : ds.rootGroups()) {
                    r = createGroupRow((GroupTable) table, columns, ++r, (GroupInfo) item, 0);
                }
            } else {
                for (Object itemId : datasource.getItemIds()) {
                    createRow(table, columns, 0, ++r, itemId);
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
            throw new RuntimeException(e);
        }
        if (fileName == null) {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            fileName = messageTools.getEntityCaption(datasource.getMetaClass());
        }

        display.show(new ByteArrayDataProvider(out.toByteArray()), fileName + ".xls", ExportFormat.XLS);
    }

    protected void createFormats() {
        timeFormatCellStyle = wb.createCellStyle();
        timeFormatCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));

        dateFormatCellStyle = wb.createCellStyle();
        dateFormatCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));

        integerFormatCellStyle = wb.createCellStyle();
        integerFormatCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

        doubleFormatCellStyle = wb.createCellStyle();
        doubleFormatCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
    }

    protected int createHierarhicalRow(TreeTable table, List<Table.Column> columns,
                                       Boolean exportExpanded, int rowNumber, Object itemId) {
        HierarchicalDatasource hd = table.getDatasource();
        createRow(table, columns, 0, ++rowNumber, itemId);
        if (BooleanUtils.isTrue(exportExpanded) && !table.isExpanded(itemId) && !hd.getChildren(itemId).isEmpty()) {
            return rowNumber;
        } else {
            final Collection children = hd.getChildren(itemId);
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

    protected int createGroupRow(GroupTable table, List<Table.Column> columns, int rowNumber, GroupInfo groupInfo, int groupNumber) {
        GroupDatasource ds = table.getDatasource();

        HSSFRow row = sheet.createRow(rowNumber);
        HSSFCell cell = row.createCell(groupNumber);
        Object val = groupInfo.getValue();

        if (val == null) {
            val = messages.getMessage(getClass(), "excelExporter.empty");
        }

        MetaPropertyPath propertyPath = (MetaPropertyPath) groupInfo.getProperty();
        Table.Column column = table.getColumn(propertyPath.toString());
        Element xmlDescriptor = column.getXmlDescriptor();
        if (xmlDescriptor != null && StringUtils.isNotEmpty(xmlDescriptor.attributeValue("captionProperty"))) {
            String captionProperty = xmlDescriptor.attributeValue("captionProperty");
            Collection children = table.getDatasource().getGroupItemIds(groupInfo);
            if (children.isEmpty()) {
                return rowNumber;
            }

            Object itemId = children.iterator().next();
            Instance item = table.getDatasource().getItem(itemId);
            Object captionValue = item.getValueEx(captionProperty);
            formatValueCell(cell, captionValue, groupNumber++, rowNumber, 0, true);
        } else {
            formatValueCell(cell, val, groupNumber++, rowNumber, 0, true);
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
        sheet.groupRow(oldRowNumber + 1, rowNumber);
        return rowNumber;
    }

    protected void createRow(Table table, List<Table.Column> columns, int startColumn, int rowNumber, Object itemId) {
        if (startColumn >= columns.size()) {
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
            boolean isFull = true;

            if (column.getId() instanceof MetaPropertyPath) {
                Table.Printable printable = table.getPrintable(column);
                if (printable != null) {
                    cellValue = printable.getValue((Entity) instance);
                } else {
                    Element xmlDescriptor = column.getXmlDescriptor();
                    if (xmlDescriptor != null && StringUtils.isNotEmpty(xmlDescriptor.attributeValue("captionProperty"))) {
                        String captionProperty = xmlDescriptor.attributeValue("captionProperty");
                        cellValue = InstanceUtils.getValueEx(instance, captionProperty);
                    } else {
                        cellValue = InstanceUtils.getValueEx(instance, ((MetaPropertyPath) column.getId()).getPath());
                    }
                    if (column.getFormatter() != null)
                        cellValue = column.getFormatter().format(cellValue);
                    TemporalType tt = (TemporalType) ((MetaPropertyPath) column.getId()).getMetaProperty().getAnnotations().get("temporal");
                    if (tt != null && tt == TemporalType.DATE)
                        isFull = false;
                }
            } else {
                Table.Printable printable = table.getPrintable(column);
                if (printable != null) {
                    cellValue = printable.getValue((Entity) instance);
                }
            }

            formatValueCell(cell, cellValue, c, rowNumber, level, isFull);
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

    protected void formatValueCell(HSSFCell cell, @Nullable Object cellValue,
                                   int sizersIndex, int notificationReqiured, int level, boolean isFull) {
        if (cellValue == null)
            return;

        if (cellValue instanceof Number) {
            Number n = (Number) cellValue;
            final Datatype datatype = Datatypes.getNN(n.getClass());
            String str;
            if (sizersIndex == 0) {
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
                    throw new RuntimeException(e);
                }
                cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            }
            if (sizers[sizersIndex].isNotificationRequired(notificationReqiured)) {
                sizers[sizersIndex].notifyCellValue(str, stdFont);
            }
        } else if (cellValue instanceof Date) {
            cell.setCellValue(((Date) cellValue));

            if (isFull)
                cell.setCellStyle(timeFormatCellStyle);
            else
                cell.setCellStyle(dateFormatCellStyle);

            if (sizers[sizersIndex].isNotificationRequired(notificationReqiured)) {
                String str = Datatypes.getNN(Date.class).format(cellValue);
                sizers[sizersIndex].notifyCellValue(str, stdFont);
            }
        } else if (cellValue instanceof Boolean) {
            String str = "";
            if (sizersIndex == 0) {
                str += createSpaceString(level);
            }
            str += ((Boolean) cellValue) ? trueStr : falseStr;
            cell.setCellValue(new HSSFRichTextString(str));
            if (sizers[sizersIndex].isNotificationRequired(notificationReqiured)) {
                sizers[sizersIndex].notifyCellValue(str, stdFont);
            }
        } else if (cellValue instanceof EnumClass) {
            String nameKey = cellValue.getClass().getSimpleName() + "." + cellValue.toString();
            final String message = sizersIndex == 0 ? createSpaceString(level) + messages.getMessage(cellValue.getClass(), nameKey)
                    : messages.getMessage(cellValue.getClass(), nameKey);

            cell.setCellValue(message);
            if (sizers[sizersIndex].isNotificationRequired(notificationReqiured)) {
                sizers[sizersIndex].notifyCellValue(message, stdFont);
            }
        } else if (cellValue instanceof Entity) {
            Entity entityVal = (Entity) cellValue;
            String instanceName = entityVal.getInstanceName();
            String str = sizersIndex == 0 ? createSpaceString(level) + instanceName : instanceName;
            cell.setCellValue(new HSSFRichTextString(str));
            if (sizers[sizersIndex].isNotificationRequired(notificationReqiured)) {
                sizers[sizersIndex].notifyCellValue(str, stdFont);
            }
        } else if (cellValue instanceof Collection) {
            String str = "";
            cell.setCellValue(new HSSFRichTextString(str));
            if (sizers[sizersIndex].isNotificationRequired(notificationReqiured)) {
                sizers[sizersIndex].notifyCellValue(str, stdFont);
            }
        } else {
            String str = sizersIndex == 0 ? createSpaceString(level) + cellValue.toString() : cellValue.toString();
            cell.setCellValue(new HSSFRichTextString(str));
            if (sizers[sizersIndex].isNotificationRequired(notificationReqiured)) {
                sizers[sizersIndex].notifyCellValue(str, stdFont);
            }
        }
    }
}
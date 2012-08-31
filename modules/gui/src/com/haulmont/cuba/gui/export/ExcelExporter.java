/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.06.2009 11:45:24
 *
 * $Id$
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
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TreeTable;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.data.GroupInfo;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import org.apache.commons.lang.BooleanUtils;
import org.apache.poi.hssf.usermodel.*;

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
 */
public class ExcelExporter {
    private static final int COL_WIDTH_MAGIC = 48;

    private static final int SPACE_COUNT = 10;

    private HSSFWorkbook wb;

    private HSSFFont boldFont;

    private HSSFFont stdFont;

    private HSSFSheet sheet;

    private ExcelAutoColumnSizer[] sizers;

    private final String trueStr = MessageProvider.getMessage(getClass(), "excelExporter.true");

    private final String falseStr = MessageProvider.getMessage(getClass(), "excelExporter.false");

    public void exportTable(Table table, ExportDisplay display) {
        exportTable(table, table.getColumns(), display);
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
         if (display == null)
            throw new IllegalArgumentException("ExportDisplay is null");

        createWorkbookWithSheet();
        createFonts();

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
        }

        CollectionDatasource datasource = table.getDatasource();
        if (table instanceof TreeTable) {
            TreeTable treeTable = (TreeTable) table;
            HierarchicalDatasource ds = (HierarchicalDatasource) treeTable.getDatasource();
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
        for (int c = 0; c < columns.size(); c++) {
            sheet.setColumnWidth(c, sizers[c].getWidth() * COL_WIDTH_MAGIC);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            wb.write(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String fileName = AppBeans.get(MessageTools.class).getEntityCaption(datasource.getMetaClass());
        display.show(new ByteArrayDataProvider(out.toByteArray()), fileName + ".xls", ExportFormat.XLS);
    }

    protected int createHierarhicalRow(TreeTable table, List<Table.Column> columns,
                                       Boolean exportExpanded, int rowNumber, Object itemId) {
        HierarchicalDatasource hd = (HierarchicalDatasource) table.getDatasource();
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
        GroupDatasource ds = (GroupDatasource) table.getDatasource();

        HSSFRow row = sheet.createRow(rowNumber);
        HSSFCell cell = row.createCell(groupNumber);
        Object val = groupInfo.getValue();
        val = val == null ? MessageProvider.getMessage(getClass(), "excelExporter.empty") : val;
        formatValueCell(cell, val, groupNumber++, rowNumber, 0, true);

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
        Instance instance = (Instance) table.getDatasource().getItem(itemId);

        int level = 0;
        if (table instanceof TreeTable) {
            level = ((TreeTable)table).getLevel(itemId);
        }
        for (int c = startColumn; c < columns.size(); c++) {
            HSSFCell cell = row.createCell(c);

            Table.Column column = columns.get(c);
            if (column.getId() instanceof MetaPropertyPath) {
                Object val = InstanceUtils.getValueEx(instance, ((MetaPropertyPath) column.getId()).getPath());
                TemporalType tt = (TemporalType) ((MetaPropertyPath) column.getId()).getMetaProperty().getAnnotations().get("temporal");
                boolean isFull = true;
                if (tt != null && tt == TemporalType.DATE)
                    isFull = false;
                formatValueCell(cell, val, c, rowNumber, level, isFull);
            }
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

    protected void formatValueCell(HSSFCell cell, Object val, int sizersIndex, int notificationReqiured, int level, boolean isFull) {
        if (val == null)
            return;
        if (val instanceof Number) {
            Number n = (Number) val;
            final Datatype datatype = Datatypes.get(n.getClass());
            String str;
            if (sizersIndex == 0) {
                str = createSpaceString(level) + datatype.format(n);
                cell.setCellValue(str);
            } else {
                try {
                    str = datatype.format(n);
                    Number result = (Number) datatype.parse(str);
                    if (n instanceof Integer || n instanceof Long || n instanceof Byte || n instanceof Short) {
                        cell.setCellValue(result.longValue());
                    } else {
                        cell.setCellValue(result.doubleValue());
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            }
            if (sizers[sizersIndex].isNotificationRequired(notificationReqiured)) {
                sizers[sizersIndex].notifyCellValue(str, stdFont);
            }
        } else if (val instanceof Date) {
            cell.setCellValue(((Date) val));

            final HSSFCellStyle cellStyle = wb.createCellStyle();
            if (isFull)
                cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
            else
                cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
            cell.setCellStyle(cellStyle);

            if (sizers[sizersIndex].isNotificationRequired(notificationReqiured)) {
                String str = Datatypes.get(Date.class).format((Date) val);
                sizers[sizersIndex].notifyCellValue(str, stdFont);
            }
        } else if (val instanceof Boolean) {
            String str = "";
            if (sizersIndex == 0) {
                str += createSpaceString(level);
            }
            str += ((Boolean) val) ? trueStr : falseStr;
            cell.setCellValue(new HSSFRichTextString(str));
            if (sizers[sizersIndex].isNotificationRequired(notificationReqiured)) {
                sizers[sizersIndex].notifyCellValue(str, stdFont);
            }
        } else if (val instanceof EnumClass) {
            String nameKey = val.getClass().getSimpleName() + "." + val.toString();
            final String message = sizersIndex == 0 ? createSpaceString(level) + MessageProvider.getMessage(val.getClass(), nameKey)
                    : MessageProvider.getMessage(val.getClass(), nameKey);
            cell.setCellValue(message);
            if (sizers[sizersIndex].isNotificationRequired(notificationReqiured)) {
                sizers[sizersIndex].notifyCellValue(message, stdFont);
            }
        } else if (val instanceof Entity){
            Entity entityVal = (Entity) val;
            String instanceName = ((Instance) entityVal).getInstanceName();
            String str = sizersIndex == 0 ? createSpaceString(level) + instanceName : instanceName;
            cell.setCellValue(new HSSFRichTextString(str));
            if (sizers[sizersIndex].isNotificationRequired(notificationReqiured)) {
                sizers[sizersIndex].notifyCellValue(str, stdFont);
            }
        } else if (val instanceof Collection){
            String str = "";
            cell.setCellValue(new HSSFRichTextString(str));
            if (sizers[sizersIndex].isNotificationRequired(notificationReqiured)) {
                sizers[sizersIndex].notifyCellValue(str, stdFont);
            }
        }
        else {
            String str = sizersIndex == 0 ? createSpaceString(level) + val.toString() : val.toString();
            cell.setCellValue(new HSSFRichTextString(str));
            if (sizers[sizersIndex].isNotificationRequired(notificationReqiured)) {
                sizers[sizersIndex].notifyCellValue(str, stdFont);
            }
        }
    }
}

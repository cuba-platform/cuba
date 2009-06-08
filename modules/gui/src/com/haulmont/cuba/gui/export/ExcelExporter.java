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

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.apache.poi.hssf.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

public class ExcelExporter
{
    private static final int COL_WIDTH_MAGIC = 48;
    private static final String DATE_FMT = "dd/MM/yyyy HH:mm:ss";

    public void exportTable(Table table, ExportDisplay display) {
        if (display == null)
            throw new IllegalArgumentException("ExportDisplay is null");

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Export");

        int r = 0;
        HSSFRow row = sheet.createRow(0);

        HSSFFont stdFont = wb.createFont();
        HSSFFont boldFont = wb.createFont();
        boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        List<Table.Column> columns = table.getColumns();

        ExcelAutoColumnSizer[] sizers = new ExcelAutoColumnSizer[columns.size()];

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

        String trueStr = MessageProvider.getMessage(getClass(), "excelExporter.true");
        String falseStr = MessageProvider.getMessage(getClass(), "excelExporter.false");

        CollectionDatasource datasource = table.getDatasource();
        for (Object itemId : datasource.getItemIds()) {
            row = sheet.createRow(++r);
            Instance instance = (Instance) datasource.getItem(itemId);
            for (int c = 0; c < columns.size(); c++) {
                HSSFCell cell = row.createCell(c);

                Table.Column column = columns.get(c);
                Object val = InstanceUtils.getValueEx(instance, ((MetaPropertyPath) column.getId()).getPath());
                if (val == null)
                    continue;
                if (val instanceof BigDecimal) {
                    cell.setCellValue(((BigDecimal) val).doubleValue());
                    if (sizers[c].isNotificationRequired(r)) {
                        String str = String.valueOf(((BigDecimal) val).doubleValue());
                        sizers[c].notifyCellValue(str, stdFont);
                    }
                } else if (val instanceof Date) {
                    cell.setCellValue(((Date) val));
                    if (sizers[c].isNotificationRequired(r)) {
                        String str = new SimpleDateFormat(DATE_FMT).format(val);
                        sizers[c].notifyCellValue(str, stdFont);
                    }
                } else if (val instanceof Boolean) {
                    String str = ((Boolean) val) ? trueStr : falseStr;
                    cell.setCellValue(new HSSFRichTextString(str));
                    if (sizers[c].isNotificationRequired(r)) {
                        sizers[c].notifyCellValue(str, stdFont);
                    }
                } else if (val instanceof EnumClass) {
                    String nameKey = val.getClass().getSimpleName() + "." + val.toString();
                    final String message = MessageProvider.getMessage(val.getClass(), nameKey);
                    cell.setCellValue(message);
                    if (sizers[c].isNotificationRequired(r)) {
                        sizers[c].notifyCellValue(message, stdFont);
                    }
                } else {
                    cell.setCellValue(new HSSFRichTextString(val.toString()));
                    if (sizers[c].isNotificationRequired(r)) {
                        sizers[c].notifyCellValue(val.toString(), stdFont);
                    }
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

        String fileName = datasource.getMetaClass().getName();
        display.show(out.toByteArray(), fileName, ExportFormat.XLS);
    }
}

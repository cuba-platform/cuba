/*
 * Copyright (c) 2008-2020 Haulmont.
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
 */

package com.haulmont.cuba.gui.export.helper;

import com.haulmont.cuba.gui.export.ExcelOptions;
import com.haulmont.cuba.gui.export.ExportFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Helper class for export to XLS file.
 */
public class XlsExportHelper implements ExcelExportHelper {

    protected ExcelOptions excelOptions;

    public XlsExportHelper() {
        this.excelOptions = new ExcelOptions(65535, ExportFormat.XLS, ".xls", 48);
    }

    /**
     * @return new instance of {@link org.apache.poi.hssf.usermodel.HSSFWorkbook}
     */
    @Override
    public Workbook createWorkbook() {
        return new HSSFWorkbook();
    }

    /**
     * @param str text
     * @return new instance of {@link HSSFRichTextString} with specified text
     */
    @Override
    public RichTextString createRichTextString(String str) {
        return new HSSFRichTextString(str);
    }

    @Override
    public ExcelOptions getExcelOptions() {
        return excelOptions;
    }

    @Override
    public boolean isRowNumberExceed(int r) {
        return r >= excelOptions.getMaxRowCount();
    }
}

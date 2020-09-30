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
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Helper class for export to XLSX file.
 */
public class XlsxExportHelper implements ExcelExportHelper {

    protected ExcelOptions excelOptions;

    public XlsxExportHelper() {
        this.excelOptions = new ExcelOptions(1048576, ExportFormat.XLSX, ".xlsx", 50);
    }

    /**
     * @return new instance of {@link org.apache.poi.xssf.usermodel.XSSFWorkbook}
     */
    @Override
    public Workbook createWorkbook() {
        return new XSSFWorkbook();
    }

    /**
     *
     * @param str text
     * @return new instance of {@link org.apache.poi.xssf.usermodel.XSSFRichTextString} with specified text
     */
    @Override
    public RichTextString createRichTextString(String str) {
        return new XSSFRichTextString(str);
    }

    @Override
    public ExcelOptions getExcelOptions() {
        return excelOptions;
    }

    @Override
    public boolean isRowNumberExceed(int r) {
        return false;
    }
}

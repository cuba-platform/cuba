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
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Helper class for export to Excel file.
 * Methods are implemented for XLS and XLSX export.
 */
public interface ExcelExportHelper {

    /**
     * @return new instance of {@link org.apache.poi.ss.usermodel.Workbook} implementation
     */
    Workbook createWorkbook();

    /**
     * @param str text
     * @return new instance of {@link org.apache.poi.ss.usermodel.RichTextString} implementation with specified text
     */
    RichTextString createRichTextString(String str);

    /**
     * @return options for XLS or XLSX export
     */
    ExcelOptions getExcelOptions();

    /**
     * @param r row number
     * @return true if specified row number exceeds the maximum
     */
    boolean isRowNumberExceed(int r);
}

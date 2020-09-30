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

package com.haulmont.cuba.gui.export;

/**
 * DTO for parameters required for export to Excel file
 */
public class ExcelOptions {
    protected int maxRowCount;
    protected ExportFormat exportFormat;
    protected String extension;
    protected int colWidthMagic;

    public ExcelOptions(int maxRowCount, ExportFormat exportFormat, String extension, int colWidthMagic) {
        this.maxRowCount = maxRowCount;
        this.exportFormat = exportFormat;
        this.extension = extension;
        this.colWidthMagic = colWidthMagic;
    }

    public ExportFormat getExportFormat() {
        return exportFormat;
    }

    public String getExtension() {
        return extension;
    }

    public int getColWidthMagic() {
        return colWidthMagic;
    }

    public int getMaxRowCount() {
        return maxRowCount;
    }
}

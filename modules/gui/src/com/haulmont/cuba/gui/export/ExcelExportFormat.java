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

public enum ExcelExportFormat {
    /**
     * XLS format for export
     */
    XLS,
    /**
     * XLSX format for export
     */
    XLSX,
    /**
     * Export format should be taken from the setting {@link com.haulmont.cuba.client.ClientConfig#getDefaultExcelExportFormat()}
     */
    DEFAULT
}

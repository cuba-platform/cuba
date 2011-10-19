/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.export;

import com.haulmont.cuba.report.ReportOutputType;

import java.util.HashMap;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class ReportPrintHelper {

    private static HashMap<ReportOutputType, ExportFormat> exportFormats = new HashMap<ReportOutputType, ExportFormat>();

    static {
        exportFormats.put(ReportOutputType.XLS, ExportFormat.XLS);
        exportFormats.put(ReportOutputType.DOC, ExportFormat.DOC);
        exportFormats.put(ReportOutputType.PDF, ExportFormat.PDF);
        exportFormats.put(ReportOutputType.HTML, ExportFormat.HTML);
    }

    public static ExportFormat getExportFormat(ReportOutputType outputType) {
        return exportFormats.get(outputType);
    }
}
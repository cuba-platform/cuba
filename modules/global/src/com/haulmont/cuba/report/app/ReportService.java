/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 07.05.2010 15:11:12
 *
 * $Id$
 */
package com.haulmont.cuba.report.app;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.report.Report;
import com.haulmont.cuba.report.ReportOutputType;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public interface ReportService {
    String NAME = "report_ReportService";

    byte[] createReport(Report report, ReportOutputType format, Map<String, Object> params) throws IOException;

    FileDescriptor createAndSaveReport(Report report, Map<String, Object> params, String fileName) throws IOException;

    Report reloadReport(Report report);

    /**
     * Exports all reports and their templates into one zip archive. Each report is exported into a separete zip
     * archive with 2 files (report.xml and a template file (for example MyReport.doc)).
     * For example:
     * return byte[] (bytes of zip arhive)
     * -- MegaReport.zip
     * ---- report.xml
     * ---- Mega report.xls
     * -- Other report.zip
     * ---- report.xml
     * ---- other report.odt
     *
     * @param reports Collection of Report objects to be exported.
     * @return ZIP byte array with zip archives inside.
     */
    byte[] exportReports(Collection<Report> reports) throws IOException, FileStorageException;

    /**
     * Imports reports from ZIP archive. Archive file format is described in exportReports method.
     *
     * @param zipBytes ZIP archive as a byte array.
     * @return Collection of imported reports.
     * @throws IOException
     * @throws FileStorageException
     */
    Collection<Report> importReports(byte[] zipBytes) throws IOException, FileStorageException;
}

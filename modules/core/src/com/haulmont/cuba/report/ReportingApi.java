/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.report;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * API for reporting
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public interface ReportingApi {
    String NAME = "report_ReportingApi";

    ReportOutputDocument createReport(Report report, Map<String, Object> params) throws IOException;

    ReportOutputDocument createReport(Report report, String templateCode, Map<String, Object> params) throws IOException;

    ReportOutputDocument createReport(Report report, ReportTemplate template, Map<String, Object> params) throws IOException;

    Report reloadReport(Report report) ;

    byte[] exportReports(Collection<Report> reports) throws IOException, FileStorageException;

    FileDescriptor createAndSaveReport(Report report,
                                              Map<String, Object> params, String fileName) throws IOException;

    FileDescriptor createAndSaveReport(Report report, String templateCode,
                                              Map<String, Object> params, String fileName) throws IOException;

    FileDescriptor createAndSaveReport(Report report, ReportTemplate template,
                                              Map<String, Object> params, String fileName) throws IOException;

    Collection<Report> importReports(byte[] zipBytes) throws IOException, FileStorageException;
}

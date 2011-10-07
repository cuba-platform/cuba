/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 07.05.2010 15:12:05
 *
 * $Id$
 */
package com.haulmont.cuba.report;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.report.app.ReportService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Service(ReportService.NAME)
public class ReportServiceBean implements ReportService {

    @Override
    public ReportOutputDocument createReport(Report report, Map<String, Object> params) throws IOException {
        ReportingApi uploadingApi = Locator.lookup(ReportingApi.NAME);
        return uploadingApi.createReport(report, params);
    }

    @Override
    public ReportOutputDocument createReport(Report report, String templateCode, Map<String, Object> params) throws IOException {
        ReportingApi uploadingApi = Locator.lookup(ReportingApi.NAME);
        return uploadingApi.createReport(report, templateCode, params);
    }

    @Override
    public ReportOutputDocument createReport(Report report, ReportTemplate template, Map<String, Object> params) throws IOException {
        ReportingApi uploadingApi = Locator.lookup(ReportingApi.NAME);
        return uploadingApi.createReport(report, template, params);
    }

    @Override
    public Report reloadReport(Report report) {
        ReportingApi uploadingApi = Locator.lookup(ReportingApi.NAME);
        return uploadingApi.reloadReport(report);
    }

    @Override
    public byte[] exportReports(Collection<Report> reports) throws IOException, FileStorageException {
        ReportingApi uploadingApi = Locator.lookup(ReportingApi.NAME);
        return uploadingApi.exportReports(reports);
    }

    @Override
    public FileDescriptor createAndSaveReport(Report report,
                                              Map<String, Object> params, String fileName) throws IOException {
        ReportingApi uploadingApi = Locator.lookup(ReportingApi.NAME);
        return uploadingApi.createAndSaveReport(report, params, fileName);
    }

    @Override
    public FileDescriptor createAndSaveReport(Report report, String templateCode,
                                              Map<String, Object> params, String fileName) throws IOException {
        ReportingApi uploadingApi = Locator.lookup(ReportingApi.NAME);
        return uploadingApi.createAndSaveReport(report, templateCode, params, fileName);
    }

    @Override
    public FileDescriptor createAndSaveReport(Report report, ReportTemplate template,
                                              Map<String, Object> params, String fileName) throws IOException {
        ReportingApi uploadingApi = Locator.lookup(ReportingApi.NAME);
        return uploadingApi.createAndSaveReport(report, template, params, fileName);
    }

    @Override
    public Collection<Report> importReports(byte[] zipBytes) throws IOException, FileStorageException {
        ReportingApi uploadingApi = Locator.lookup(ReportingApi.NAME);
        return uploadingApi.importReports(zipBytes);
    }
}
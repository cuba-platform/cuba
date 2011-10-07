/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 03.06.2010 12:06:08
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters;

import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.report.Band;
import com.haulmont.cuba.report.CustomReport;
import com.haulmont.cuba.report.Report;
import com.haulmont.cuba.report.ReportTemplate;

import java.util.Map;

public class CustomFormatter implements Formatter {
    private Report report;
    private ReportTemplate template;
    private Map<String, Object> params;

    public CustomFormatter(Report report, ReportTemplate template, Map<String, Object> params) {
        this.report = report;
        this.params = params;
        this.template = template;
    }

    @Override
    public byte[] createDocument(Band rootBand) {
        Class clazz = ScriptingProvider.loadClass(template.getCustomClass());
        try {
            CustomReport customReport = (CustomReport) clazz.newInstance();
            return customReport.createReport(report, params);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Artamonov Yuryi
 * Created: 29.03.11 16:43
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.report.Band;
import com.haulmont.cuba.report.ReportOutputType;

import java.io.OutputStream;

/**
 * Interface for main Report Formatters
 *
 * @see DocFormatter
 * @see XLSFormatter
 * @see HtmlFormatter
 */
public interface ReportEngine {
    FileDescriptor getTemplateFile();

    void setTemplateFile(FileDescriptor templateFile);

    ReportOutputType getDefaultOutputType();

    boolean hasSupportReport(String reportExtension, ReportOutputType outputType);

    void createDocument(Band rootBand, ReportOutputType outputType, OutputStream outputStream);
}
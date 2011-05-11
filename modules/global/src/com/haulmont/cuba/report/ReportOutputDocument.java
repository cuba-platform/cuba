/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.report;

import java.io.Serializable;

/**
 * Output from ReportService
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class ReportOutputDocument implements Serializable {

    private static final long serialVersionUID = 9168523006847042457L;

    private Report report;

    private ReportOutputType outputType;

    private byte[] content;

    public ReportOutputDocument(Report report, ReportOutputType outputType, byte[] content) {
        this.report = report;
        this.outputType = outputType;
        this.content = content;
    }

    public Report getReport() {
        return report;
    }

    public ReportOutputType getOutputType() {
        return outputType;
    }

    public byte[] getContent() {
        return content;
    }
}

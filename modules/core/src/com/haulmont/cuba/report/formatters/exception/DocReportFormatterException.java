/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Vasiliy Fontanenko
 * Created: 22.06.2010 13:58:31
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters.exception;

import com.haulmont.cuba.report.exception.ReportFormatterException;

public class DocReportFormatterException extends ReportFormatterException {
    public DocReportFormatterException() {
    }

    public DocReportFormatterException(String message) {
        super(message);
    }

    public DocReportFormatterException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocReportFormatterException(Throwable cause) {
        super(cause);
    }
}

package com.haulmont.cuba.report.formatters.exception;

import com.haulmont.cuba.report.exception.ReportFormatterException;

/**
 * Created by IntelliJ IDEA.
 * User: fontanenko
 * Date: 22.06.2010
 * Time: 13:58:31
 * To change this template use File | Settings | File Templates.
 */
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

package com.haulmont.cuba.report.formatters.exception;

/**
 * Created by IntelliJ IDEA.
 * User: fontanenko
 * Date: 22.06.2010
 * Time: 13:57:49
 * To change this template use File | Settings | File Templates.
 */
public class FailedToConnectToOpenOfficeAPIExceptionReport extends DocReportFormatterException {
    public FailedToConnectToOpenOfficeAPIExceptionReport() {
    }

    public FailedToConnectToOpenOfficeAPIExceptionReport(String message) {
        super(message);
    }

    public FailedToConnectToOpenOfficeAPIExceptionReport(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedToConnectToOpenOfficeAPIExceptionReport(Throwable cause) {
        super(cause);
    }
}

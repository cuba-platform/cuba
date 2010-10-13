package com.haulmont.cuba.report.formatters.exception;

/**
 * Created by IntelliJ IDEA.
 * User: fontanenko
 * Date: 22.06.2010
 * Time: 13:57:49
 * To change this template use File | Settings | File Templates.
 */
public class FailedToConnectToOpenOfficeException extends DocReportFormatterException {
    public FailedToConnectToOpenOfficeException() {
    }

    public FailedToConnectToOpenOfficeException(String message) {
        super(message);
    }

    public FailedToConnectToOpenOfficeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedToConnectToOpenOfficeException(Throwable cause) {
        super(cause);
    }
}

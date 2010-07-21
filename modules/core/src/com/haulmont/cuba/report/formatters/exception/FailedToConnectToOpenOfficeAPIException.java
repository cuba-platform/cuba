package com.haulmont.cuba.report.formatters.exception;

/**
 * Created by IntelliJ IDEA.
 * User: fontanenko
 * Date: 22.06.2010
 * Time: 13:57:49
 * To change this template use File | Settings | File Templates.
 */
public class FailedToConnectToOpenOfficeAPIException extends DocFormatterException {
    public FailedToConnectToOpenOfficeAPIException() {
    }

    public FailedToConnectToOpenOfficeAPIException(String message) {
        super(message);
    }

    public FailedToConnectToOpenOfficeAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedToConnectToOpenOfficeAPIException(Throwable cause) {
        super(cause);
    }
}

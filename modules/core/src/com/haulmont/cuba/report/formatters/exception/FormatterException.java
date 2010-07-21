package com.haulmont.cuba.report.formatters.exception;

/**
 * Created by IntelliJ IDEA.
 * User: fontanenko
 * Date: 22.06.2010
 * Time: 13:53:29
 * To change this template use File | Settings | File Templates.
 */
public class FormatterException extends RuntimeException {

    public FormatterException() {
    }

    public FormatterException(String message) {
        super(message);
    }

    public FormatterException(String message, Throwable cause) {
        super(message, cause);
    }

    public FormatterException(Throwable cause) {
        super(cause);
    }
}

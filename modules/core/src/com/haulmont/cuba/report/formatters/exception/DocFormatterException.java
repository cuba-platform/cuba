package com.haulmont.cuba.report.formatters.exception;

/**
 * Created by IntelliJ IDEA.
 * User: fontanenko
 * Date: 22.06.2010
 * Time: 13:58:31
 * To change this template use File | Settings | File Templates.
 */
public class DocFormatterException extends FormatterException {
    public DocFormatterException() {
    }

    public DocFormatterException(String message) {
        super(message);
    }

    public DocFormatterException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocFormatterException(Throwable cause) {
        super(cause);
    }
}

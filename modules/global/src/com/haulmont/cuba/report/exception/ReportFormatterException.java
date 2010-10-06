/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Yuryi Artamonov
 * Created: ${DATE} ${TIME}
 *
 * $Id$
 */

package com.haulmont.cuba.report.exception;

public class ReportFormatterException extends RuntimeException {

    public ReportFormatterException() {
    }

    public ReportFormatterException(String message) {
        super(message);
    }
       
    public ReportFormatterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReportFormatterException(Throwable cause) {
        super(cause);
    }
}

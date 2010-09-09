/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 02.09.2010 15:13:29
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui.charts;

@SuppressWarnings("serial")
public class ChartException extends Exception {
    public ChartException() {
    }

    public ChartException(String message) {
        super(message);
    }

    public ChartException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChartException(Throwable cause) {
        super(cause);
    }
}

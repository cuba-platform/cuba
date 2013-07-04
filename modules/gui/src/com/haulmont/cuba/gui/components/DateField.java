/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.components;

/**
 * @author abramov
 * @version $Id$
 */
public interface DateField extends Field {

    String NAME = "dateField";

    public enum Resolution {
        MSEC,
        SEC,
        MIN,
        HOUR,
        DAY,
        MONTH,
        YEAR
    }

    Resolution getResolution();
    void setResolution(Resolution resolution);

    String getDateFormat();
    void setDateFormat(String dateFormat);
}
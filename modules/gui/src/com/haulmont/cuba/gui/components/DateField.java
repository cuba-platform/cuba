/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
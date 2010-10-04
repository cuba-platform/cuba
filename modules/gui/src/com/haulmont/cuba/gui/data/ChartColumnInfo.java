/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 04.10.2010 14:38:21
 *
 * $Id$
 */
package com.haulmont.cuba.gui.data;

import java.io.Serializable;

public class ChartColumnInfo implements Serializable {
    private final Serializable property;
    private final Class<? extends Number> type;
    private final String caption;

    private static final long serialVersionUID = 4289110867589348233L;

    public ChartColumnInfo(Serializable property, Class<? extends Number> type) {
        this(property, type, null);
    }

    public ChartColumnInfo(Serializable property, Class<? extends Number> type, String caption) {
        this.caption = caption;
        this.property = property;
        this.type = type;
    }

    public String getCaption() {
        return caption;
    }

    public Serializable getProperty() {
        return property;
    }

    public Class<? extends Number> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ChartColumnInfo{" +
                "caption='" + caption + '\'' +
                ", property=" + property +
                ", type=" + type +
                '}';
    }
}

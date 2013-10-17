/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 28.08.2009 15:13:06
 *
 * $Id$
 */
package com.haulmont.cuba.toolkit.gwt.client;

public class ColumnWidth {
    private final String colId;
    private final int width;

    private static final String DELIM = ",";

    public ColumnWidth(String colId, int width) {
        this.colId = colId;
        this.width = width;
    }

    public String getColId() {
        return colId;
    }

    public int getWidth() {
        return width;
    }

    public static ColumnWidth deSerialize(String s) {
        String[] as = s.split(DELIM);
        return new ColumnWidth(as[0], Integer.parseInt(as[1]));
    }

    @Override
    public String toString() {
        return colId + DELIM + width;
    }
}

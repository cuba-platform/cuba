/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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

/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * Author: Nikolay Krokhin
 * Created: 17-Aug-2010 18:40:53
 *
 * $Id$
 */
package com.haulmont.cuba.report.formatters.xls;

import org.apache.poi.ss.util.AreaReference;

public class Area {

    private Cell topLeft;
    private Cell bottomRight;

    private AreaAlign align;
    private String name;

    public Area(int left, int top, int right, int bottom) {
        topLeft = new Cell(left, top);
        bottomRight = new Cell(right, bottom);
    }

    public Area(AreaReference areaReference) {
        topLeft = new Cell(areaReference.getFirstCell());
        bottomRight = new Cell(areaReference.getLastCell());
    }

    public Area(String name, AreaAlign align, AreaReference areaReference) {
        this(areaReference);
        this.name = name;
        this.align = align;
    }

    public Cell getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(Cell topLeft) {
        this.topLeft = topLeft;
    }

    public Cell getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(Cell bottomRight) {
        this.bottomRight = bottomRight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AreaAlign getAlign() {
        return align;
    }

    public void setAlign(AreaAlign align) {
        this.align = align;
    }

    public AreaReference toAreaReference() {
        return new AreaReference(topLeft.toCellReference(), bottomRight.toCellReference());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj instanceof Area) {
            if (!getTopLeft().equals(((Area)obj).getTopLeft())) return false;
            if (!getBottomRight().equals(((Area)obj).getBottomRight())) return false;

            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + getTopLeft() + ":" + getBottomRight() + "]";
    }

    @Override
     public int hashCode() {
        return toString().hashCode();
    }
}

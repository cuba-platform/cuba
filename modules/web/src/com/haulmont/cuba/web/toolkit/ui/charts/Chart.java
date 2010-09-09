/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 06.09.2010 10:18:10
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui.charts;

import com.vaadin.data.Item;
import com.vaadin.data.Property;

import java.io.Serializable;
import java.util.Collection;

public interface Chart extends Serializable {

    String getCaption();
    void setCaption(String caption);

    void addColumnProperty(Object propertyId, Class<?> classType);

    Collection<?> getColumnPropertyIds();

    String getColumnCaption(Object propertyId);
    void setColumnCaption(Object propertyId, String caption);

    Property getColumnProperty(Object itemId, Object propertyId);

    Number getColumnValue(Object itemId, Object columnPropertyId);

    Object addRow(String caption);
    Object addRow(Object itemId, String caption);
    Object addRow(Object[] values, Object itemId, String caption);

    Collection<?> getRowIds();

    Object getRowCaptionPropertyId();
    void setRowCaptionPropertyId(Object rowCaptionPropertyId);

    String getRowCaption(Object itemId);
    Item getRow(Object itemId);

    String getColumnAxisLabel();
    void setColumnAxisLabel(String label);

    String getValueAxisLabel();
    void setValueAxisLabel(String label);

    boolean isLegend();
    void setLegend(boolean needLegend);

    int getChartWidth();
    void setChartWidth(int chartWidth);

    int getChartHeight();
    void setChartHeight(int chartHeight);

    enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    interface HasOrientation {
        Orientation getOrientation();
        void setOrientation(Orientation orientation);
    }

    interface ViewIn3D {
        boolean is3D();
        void set3D(boolean b);
    }
}

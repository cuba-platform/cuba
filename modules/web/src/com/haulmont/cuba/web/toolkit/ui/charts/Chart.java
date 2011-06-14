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


import com.vaadin.data.Container;
import com.vaadin.ui.Component;

public interface Chart extends Component {

    String getVendor();

    boolean getHasLegend();
    void setHasLegend(boolean hasLegend);

    int getChartWidth();
    void setChartWidth(int chartWidth);

    int getChartHeight();
    void setChartHeight(int chartHeight);

    enum AxisType {
        NUMBER,
        DATE
    }

    enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    interface HasAxisLabels {
        String getArgumentAxisLabel();
        void setArgumentAxisLabel(String label);

        String getValueAxisLabel();
        void setValueAxisLabel(String label);
    }

    interface HasValueAxisType {
        AxisType getValueAxisType();
        void setValueAxisType(AxisType axisType);
    }

    interface HasArgumentAxisType {
        AxisType getArgumentAxisType();
        void setArgumentAxisType(AxisType axisType);
    }

    interface HasOrientation {
        Orientation getOrientation();
        void setOrientation(Orientation orientation);
    }

    interface ViewIn3D {
        boolean is3D();
        void set3D(boolean is3D);
    }
}

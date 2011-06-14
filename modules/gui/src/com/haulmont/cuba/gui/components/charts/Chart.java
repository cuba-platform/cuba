/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 07.09.2010 15:28:07
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.charts;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.CollectionDatasource;

/** Root chart component */
public interface Chart extends Component, Component.Wrapper, Component.BelongToFrame,
        Component.HasXmlDescriptor, Component.Expandable, Component.HasCaption {

    boolean getHasLegend();
    void setHasLegend(boolean hasLegend);

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
        void set3D(boolean b);
    }
}

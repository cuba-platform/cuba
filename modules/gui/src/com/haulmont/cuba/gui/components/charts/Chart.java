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

    /** Returns chart datasource */
    CollectionDatasource getCollectionDatasource();
    /** Sets chart datasource */
    void setCollectionDatasource(CollectionDatasource datasource);

    /** Adds chart column (category) and its caption */
    void addColumn(MetaPropertyPath propertyId, String caption);

    /** Returns chart row (serie) caption property id */
    MetaPropertyPath getRowCaptionPropertyId();
    /** Sets chart row caption property id */
    void setRowCaptionPropertyId(MetaPropertyPath propertyId);

    /** Returns chart columns axis label */
    String getColumnAxisLabel();
    /** Sets chart column axis label */
    void setColumnAxisLabel(String label);

    /** Returns chart values axis label */
    String getValueAxisLabel();
    /** Sets chart values axis label */
    void setValueAxisLabel(String label);

    /** Returns <code>true</code> if the chart has a legend */
    boolean isLegend();
    /** Sets chart legend state*/
    void setLegend(boolean needLegend);

    /** Chart component that can be displayed in 3D */
    interface ViewIn3D {
        /** Indicates that chart displays in 3D */
        boolean is3D();
        /** Sets chart 3D state */
        void set3D(boolean b);
    }

    /** Chart component that has horizontal or vertical orientation */
    interface HasOrientation {
        /** Returns chart orientation */
        Orientation getOrientation();
        /** Sets chart orientation */
        void setOrientation(Orientation orientation);
    }

    enum Orientation {
        VERTICAL,
        HORIZONTAL
    }
}

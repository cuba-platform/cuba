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

public interface Chart extends Component, Component.Wrapper, Component.BelongToFrame,
        Component.HasXmlDescriptor, Component.Expandable, Component.HasCaption {

    CollectionDatasource getCollectionDatasource();
    void setCollectionDatasource(CollectionDatasource datasource);

    void addColumn(MetaPropertyPath propertyId, String caption);

    MetaPropertyPath getRowCaptionPropertyId();
    void setRowCaptionPropertyId(MetaPropertyPath propertyId);

    String getColumnAxisLabel();
    void setColumnAxisLabel(String label);

    String getValueAxisLabel();
    void setValueAxisLabel(String label);

    boolean isLegend();
    void setLegend(boolean needLegend);

    interface ViewIn3D {
        boolean is3D();
        void set3D(boolean b);
    }

    interface HasOrientation {
        Orientation getOrientation();
        void setOrientation(Orientation orientation);
    }

    enum Orientation {
        VERTICAL,
        HORIZONTAL
    }
}

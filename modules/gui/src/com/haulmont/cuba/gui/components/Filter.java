/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 14.10.2009 13:59:08
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.FilterEntity;

public interface Filter
        extends Component.Container, Component.BelongToFrame,
        Component.HasXmlDescriptor, Component.HasSettings {
    CollectionDatasource getDatasource();

    void setDatasource(CollectionDatasource datasource);

    void setFilterEntity(FilterEntity filterEntity);

    void apply();

    /**
     * Low-level method, don't invoke from application code
     */
    void loadFiltersAndApplyDefault();

    void setUseMaxResults(boolean useMaxResults);

    boolean getUseMaxResults();

    void setApplyTo(Component component);
    Component getApplyTo();
}

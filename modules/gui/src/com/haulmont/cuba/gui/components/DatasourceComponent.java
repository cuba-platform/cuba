/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.chile.core.model.MetaProperty;

/**
 * A component that represents data from one property of a datasource.
 *
 * @author gorodnov
 * @version $Id$
 */
public interface DatasourceComponent extends Component, Component.HasValue {

    /**
     * @return datasource instance
     */
    Datasource getDatasource();

    /**
     * @return datasource property
     */
    MetaProperty getMetaProperty();

    /**
     * Set datasource and its property.
     */
    void setDatasource(Datasource datasource, String property);
}
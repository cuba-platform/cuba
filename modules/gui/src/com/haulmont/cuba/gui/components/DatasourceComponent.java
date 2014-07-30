/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaPropertyPath;
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
     * @deprecated Use {@link #getMetaPropertyPath()}
     * @return datasource property
     */
    @Deprecated
    MetaProperty getMetaProperty();

    /**
     * @return datasource property path
     */
    MetaPropertyPath getMetaPropertyPath();

    /**
     * Set datasource and its property.
     */
    void setDatasource(Datasource datasource, String property);
}
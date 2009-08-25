/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 20.08.2009 11:30:32
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.chile.core.model.MetaProperty;

public interface DatasourceComponent extends Component, Component.Field {
    Datasource getDatasource();
    MetaProperty getMetaProperty();

    void setDatasource(Datasource datasource, String property);
}

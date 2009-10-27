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

public interface Filter
        extends Component.Container, Component.BelongToFrame,
            Component.HasXmlDescriptor, Component.HasSettings
{
    CollectionDatasource getDatasource();
    void setDatasource(CollectionDatasource datasource);
}

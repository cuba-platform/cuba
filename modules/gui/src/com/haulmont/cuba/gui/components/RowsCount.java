/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.02.11 14:52
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.CollectionDatasource;

public interface RowsCount extends Component.BelongToFrame, Component.HasXmlDescriptor {

    CollectionDatasource getDatasource();
    void setDatasource(CollectionDatasource datasource);
}

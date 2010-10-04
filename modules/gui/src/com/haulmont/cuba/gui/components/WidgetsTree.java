/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 03.08.2010 16:47:53
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.HierarchicalDatasource;

public interface WidgetsTree extends Tree {

    void setWidgetBuilder(WidgetBuilder widgetBuilder);

    interface WidgetBuilder {
        Component build(HierarchicalDatasource datasource, Object itemId, boolean leaf);
    }
}

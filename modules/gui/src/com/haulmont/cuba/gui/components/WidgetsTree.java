/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.HierarchicalDatasource;

/**
 * @author gorodnov
 * @version $Id$
 */
public interface WidgetsTree extends Tree {

    String NAME = "widgetsTree";

    void setWidgetBuilder(WidgetBuilder widgetBuilder);

    interface WidgetBuilder {
        Component build(HierarchicalDatasource datasource, Object itemId, boolean leaf);
    }
}
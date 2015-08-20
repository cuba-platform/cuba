/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;

/**
 * @author gorodnov
 * @version $Id$
 */
public interface WidgetsTree<E extends Entity> extends Tree<E> {

    String NAME = "widgetsTree";

    void setWidgetBuilder(WidgetBuilder widgetBuilder);

    interface WidgetBuilder {
        Component build(HierarchicalDatasource datasource, Object itemId, boolean leaf);
    }
}
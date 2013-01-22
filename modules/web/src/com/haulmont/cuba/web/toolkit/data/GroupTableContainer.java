/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 10.11.2009 18:00:23
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.data;

import com.vaadin.data.Container;

import java.util.Collection;

public interface GroupTableContainer extends Container.Sortable {
    void groupBy(Object[] properties);

    boolean isGroup(Object id);

    Collection<?> rootGroups();

    boolean hasChildren(Object id);

    Collection<?> getChildren(Object id);

    Object getGroupProperty(Object id);

    Object getGroupPropertyValue(Object id);

    Collection<?> getGroupItemIds(Object id);

    int getGroupItemsCount(Object id);

    boolean hasGroups();

    Collection<?> getGroupProperties();

    void expandAll();
    void expand(Object id);

    void collapseAll();
    void collapse(Object id);

    boolean isExpanded(Object id);
}

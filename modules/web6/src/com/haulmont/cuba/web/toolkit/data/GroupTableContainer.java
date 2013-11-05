/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.data;

import com.vaadin.data.Container;

import java.util.Collection;

/**
 * @author gorodnov
 * @version $Id$
 */
public interface GroupTableContainer extends TableContainer, Container.Sortable {
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
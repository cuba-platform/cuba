/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;

import javax.annotation.Nullable;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface Tree<E extends Entity> extends ListComponent<E>, Component.Editable {

    String NAME = "tree";

    void expandTree();
    void expand(Object itemId);

    void collapseTree();
    void collapse(Object itemId);

    boolean isExpanded(Object itemId);

    CaptionMode getCaptionMode();
    void setCaptionMode(CaptionMode captionMode);

    String getCaptionProperty();
    void setCaptionProperty(String captionProperty);

    String getHierarchyProperty();
    void setDatasource(HierarchicalDatasource datasource);

    @Override
    HierarchicalDatasource getDatasource();

    /**
     * Assign action to be executed on double click inside a tree node.
     */
    void setItemClickAction(Action action);
    Action getItemClickAction();

    void setStyleProvider(@Nullable StyleProvider styleProvider);

    void addStyleProvider(StyleProvider styleProvider);

    void removeStyleProvider(StyleProvider styleProvider);

    interface StyleProvider<E extends Entity> {
        @Nullable
        String getStyleName(E entity);
    }
}
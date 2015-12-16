/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
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

    /**
     * Expand tree including specified level
     *
     * @param level level of Tree nodes to expand, if passed level = 1 then root items will be expanded
     * @throws IllegalArgumentException if level < 1
     */
    void expandUpTo(int level);

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

    /**
     * Repaint UI representation of the tree including style providers and icon providers without refreshing the tree data.
     */
    void repaint();
}
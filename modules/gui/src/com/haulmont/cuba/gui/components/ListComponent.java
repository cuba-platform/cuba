/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.core.entity.Entity;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.Collection;

/**
 * @author abramov
 * @version $Id$
 */
public interface ListComponent extends Component, Component.BelongToFrame, Component.ActionsHolder {
    boolean isMultiSelect();
    void setMultiSelect(boolean multiselect);

    @Nullable
    <T extends Entity> T getSingleSelected();
    <T extends Entity> Set<T> getSelected();

    void setSelected(@Nullable Entity item);
    void setSelected(Collection<Entity> items);

    CollectionDatasource getDatasource();

    void refresh();
}
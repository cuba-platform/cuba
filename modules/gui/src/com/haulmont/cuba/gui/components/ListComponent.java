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
public interface ListComponent<E extends Entity> extends Component, Component.BelongToFrame, Component.ActionsHolder {

    boolean isMultiSelect();
    void setMultiSelect(boolean multiselect);

    @Nullable
    E getSingleSelected();

    Set<E> getSelected();

    void setSelected(@Nullable E item);
    void setSelected(Collection<E> items);

    CollectionDatasource getDatasource();

    /**
     * Allows to set icons for particular rows in the table.
     *
     * @param <E> entity class
     */
    interface IconProvider<E extends Entity> {
        /**
         * Called by {@link Table} to get an icon to be shown for a row.
         *
         * @param entity an entity instance represented by the current row
         * @return icon name or null to show no icon
         */
        @Nullable
        String getItemIcon(E entity);
    }

    void refresh();
}
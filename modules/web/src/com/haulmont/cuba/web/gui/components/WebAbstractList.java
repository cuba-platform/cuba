/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.vaadin.ui.AbstractSelect;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @param <T>
 * @author abramov
 * @version $Id$
 */
public abstract class WebAbstractList<T extends AbstractSelect, E extends Entity>
    extends
        WebAbstractActionsHolderComponent<T>
    implements
        ListComponent<E> {

    protected CollectionDatasource datasource;

    @Override
    public boolean isMultiSelect() {
        return component.isMultiSelect();
    }

    @Override
    public void setMultiSelect(boolean multiselect) {
        component.setMultiSelect(multiselect);
    }

    @SuppressWarnings("unchecked")
    @Override
    public E getSingleSelected() {
        final Set selected = getSelectedItemIds();
        return selected == null || selected.isEmpty() ?
                null : (E) datasource.getItem(selected.iterator().next());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<E> getSelected() {
        Set<Object> itemIds = getSelectedItemIds();

        if (itemIds != null) {
            Set res = new LinkedHashSet<>();
            for (Object id : itemIds) {
                Entity item = datasource.getItem(id);
                if (item != null)
                    res.add(item);
            }
            return res;
        } else {
            return Collections.emptySet();
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    protected Set<Object> getSelectedItemIds() {
        final Object value = component.getValue();
        if (value == null) {
            return null;
        } else if (value instanceof Set) {
            return (Set) value;
        } else if (value instanceof Collection) {
            return new LinkedHashSet((Collection) value);
        } else {
            return Collections.singleton(value);
        }
    }

    @Override
    public void setSelected(E item) {
        if (item == null) {
            component.setValue(null);
        } else {
            setSelected(Collections.singletonList(item));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setSelected(Collection<E> items) {
        Set itemIds = new HashSet();
        for (Entity item : items) {
            if (!datasource.containsItem(item.getId())) {
                throw new IllegalStateException("Datasource doesn't contain items");
            }
            itemIds.add(item.getId());
        }
        setSelectedIds(itemIds);
    }

    protected void setSelectedIds(Collection<Object> itemIds) {
        if (component.isMultiSelect()) {
            component.setValue(itemIds);
        } else {
            component.setValue(itemIds.size() > 0 ? itemIds.iterator().next() : null);
        }
    }

    @Override
    public void refresh() {
        datasource.refresh();
    }

    @Override
    public CollectionDatasource getDatasource() {
        return datasource;
    }

    @Override
    protected void attachAction(Action action) {
        if (action instanceof Action.HasTarget) {
            ((Action.HasTarget) action).setTarget(this);
        }

        super.attachAction(action);
    }
}
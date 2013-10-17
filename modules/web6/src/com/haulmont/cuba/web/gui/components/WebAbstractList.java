/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.vaadin.ui.AbstractSelect;

import java.util.*;

/**
 * @param <T>
 * @author abramov
 * @version $Id$
 */
public abstract class WebAbstractList<T extends AbstractSelect>
    extends
        WebAbstractActionsHolderComponent<T>
    implements
        ListComponent
{
    protected CollectionDatasource datasource;

    @Override
    public boolean isMultiSelect() {
        return component.isMultiSelect();
    }

    @Override
    public void setMultiSelect(boolean multiselect) {
        component.setMultiSelect(multiselect);
    }

    @Override
    public <T extends Entity> T getSingleSelected() {
        final Set selected = getSelectedItemIds();
        return selected == null || selected.isEmpty() ?
                null : (T) datasource.getItem(selected.iterator().next());
    }

    @Override
    public <T extends Entity> Set<T> getSelected() {
        Set<Object> itemIds = getSelectedItemIds();

        if (itemIds != null) {
            Set<T> res = new LinkedHashSet<>();
            for (Object id : itemIds) {
                Entity item = datasource.getItem(id);
                if (item != null)
                    res.add((T) item);
            }
            return res;
        } else {
            return Collections.emptySet();
        }
    }

    protected Set<Object> getSelectedItemIds() {
        final Object value = component.getValue();
        if (value == null) {
            return null;
        } else if (value instanceof Set) {
            return (Set) value;
        } else if (value instanceof Collection) {
            return new LinkedHashSet<Object>((Collection) value);
        } else {
            return Collections.singleton(value);
        }
    }

    @Override
    public void setSelected(Entity item) {
        if (item == null) {
            component.setValue(null);
        } else {
            setSelected(Collections.singletonList(item));
        }
    }

    @Override
    public void setSelected(Collection<Entity> items) {
        Set itemIds = new HashSet();
        for (Entity item : items) {
            if (!datasource.containsItem(item.getId())) {
                throw new IllegalStateException("Datasource doesn't contain items");
            }
            itemIds.add(item.getId());
        }
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
}
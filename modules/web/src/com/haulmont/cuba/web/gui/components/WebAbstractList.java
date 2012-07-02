package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.vaadin.ui.AbstractSelect;

import java.util.*;

public abstract class WebAbstractList<T extends AbstractSelect>
    extends
        WebAbstractActionsHolderComponent<T>
    implements
        ListComponent
{
    protected CollectionDatasource datasource;

    public boolean isMultiSelect() {
        return component.isMultiSelect();
    }

    public void setMultiSelect(boolean multiselect) {
        component.setMultiSelect(multiselect);
    }

    public <T extends Entity> T getSingleSelected() {
        final Set selected = getSelectedItemIds();
        return selected == null || selected.isEmpty() ?
                null : (T) datasource.getItem(selected.iterator().next());
    }

    public Set getSelected() {
        final Set<Object> itemIds = getSelectedItemIds();

        if (itemIds != null) {
            final Set<Object> res = new LinkedHashSet<Object>();
            for (Object id : itemIds) {
                final Object o = datasource.getItem(id);
                if (o != null) res.add(o);
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

    public void setSelected(Entity item) {
        if (item == null) {
            component.setValue(null);
        } else {
            setSelected(Collections.singletonList(item));
        }
    }

    public void setSelected(Collection<Entity> items) {
        Set itemIds = new HashSet();
        for (Entity item : items) {
            if (!datasource.containsItem(item.getId())) {
                throw new IllegalStateException("Datasource doen't contain items");
            }
            itemIds.add(item.getId());
        }
        component.setValue(itemIds.size() == 1 ? itemIds.iterator().next() : itemIds);
    }

    public void refresh() {
        datasource.refresh();
    }

    public CollectionDatasource getDatasource() {
        return datasource;
    }
}

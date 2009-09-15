/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 18:12:13
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.toolkit.ui.FilterSelect;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.vaadin.data.Property;
import com.vaadin.data.Item;
import com.vaadin.ui.AbstractSelect;

import java.util.Collection;
import java.util.HashSet;

public class LookupField
    extends
        AbstractOptionsField<FilterSelect>
    implements
        com.haulmont.cuba.gui.components.LookupField, Component.Wrapper
{
    private String nullName;
    private FilterMode filterMode;

    public LookupField() {
        this.component = new FilterSelect() {
            @Override
            public void setPropertyDataSource(Property newDataSource) {
                super.setPropertyDataSource(new PropertyAdapter(newDataSource) {
                    public Object getValue() {
                        final Object o = itemProperty.getValue();
                        return getKeyFromValue(o);
                    }

                    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
                        final Object v = getValueFromKey(newValue);
                        itemProperty.setValue(v);
                    }
                });
            }
        };
        attachListener(component);
        component.setImmediate(true);
        component.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_ITEM);
        component.setFixedTextBoxWidth(true);
        setFilterMode(FilterMode.CONTAINS);
    }

    protected Object getKeyFromValue(Object value) {
        if (value instanceof Enum) {
            return value;
        } else {
            if (optionsDatasource != null) {
                return optionsDatasource.getItemId((Entity) value);
            } else {
                return (value instanceof Entity) ? ((Entity) value).getId() : value;
            }
        }
    }

    protected <T> T getValueFromKey(Object key) {
        if (key == null) return null;
        if (key instanceof Enum) { return (T)key; }

        Object v;
        if (optionsDatasource != null) {
            if (Datasource.State.INVALID.equals(optionsDatasource.getState())) {
                optionsDatasource.refresh();
            }
            v = (T) optionsDatasource.getItem(key);
        } else {
            v = key;
        }

        return (T) v;
    }

    public void setFilterMode(FilterMode mode) {
        filterMode = mode;
        component.setFilteringMode(ComponentsHelper.convertFilterMode(mode));
    }

    public FilterMode getFilterMode() {
        return filterMode;
    }

    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);
        component.setNullSelectionAllowed(!required);
    }

    @Override
    public <T> T getValue() {
        final Object key = super.getValue();
        return (T)getValueFromKey(key);
    }

    @Override
    public void setValue(Object value) {
        super.setValue(getKeyFromValue(value));
    }

    public String getNullName() {
        return nullName;
    }

    public void setNullName(String nullName) {
        this.nullName = nullName;
//        component.setNullSelectionItemId(nullName);
//        component.requestRepaint();
    }

    @Override
    public void setOptionsDatasource(CollectionDatasource datasource) {
        this.optionsDatasource = datasource;
        component.setContainerDataSource(new DsWrapper(datasource, true));

        if (captionProperty != null) {
            component.setItemCaptionPropertyId(optionsDatasource.getMetaClass().getProperty(captionProperty));
        }
    }

    private class DsWrapper extends CollectionDsWrapper {

        public DsWrapper(CollectionDatasource datasource) {
            super(datasource);
        }

        public DsWrapper(CollectionDatasource datasource, boolean autoRefresh) {
            super(datasource, autoRefresh);
        }

        public DsWrapper(CollectionDatasource datasource, Collection<MetaPropertyPath> properties) {
            super(datasource, properties);
        }

        public DsWrapper(CollectionDatasource datasource, Collection<MetaPropertyPath> properties, boolean autoRefresh) {
            super(datasource, properties, autoRefresh);
        }

        @Override
        public Collection getItemIds() {
            Collection itemIds = super.getItemIds();
            Object valueKey = LookupField.super.getValue();
            if (valueKey != null && !itemIds.contains(valueKey)) {
                itemIds = new HashSet(itemIds);
                itemIds.add(valueKey);
            }
            return itemIds;
        }

        @Override
        public Item getItem(Object itemId) {
            Item item = super.getItem(itemId);
            if (item == null && LookupField.this.datasource != null) {
                Entity containingEntity = LookupField.this.datasource.getItem();

                Object value;
                if (LookupField.this.metaPropertyPath != null)
                    value = InstanceUtils.getValueEx((Instance) containingEntity, LookupField.this.metaPropertyPath.getPath());
                else
                    value = ((Instance) containingEntity).getValue(LookupField.this.metaProperty.getName());

                if (value instanceof Entity && ((Entity) value).getId().equals(itemId)) {
                    item = getItemWrapper(value);
                }
            }
            return item;
        }

        @Override
        public int size() {
            return getItemIds().size();
        }

        @Override
        public boolean containsId(Object itemId) {
            Collection itemIds = getItemIds();
            return itemIds.contains(itemId);
        }
    }
}
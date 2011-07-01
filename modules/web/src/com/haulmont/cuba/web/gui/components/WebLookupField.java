/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 18:12:13
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.cuba.web.gui.data.DsManager;
import com.haulmont.cuba.web.toolkit.ui.FilterSelect;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect;

import java.util.Collection;
import java.util.HashSet;

public class WebLookupField
        extends
        WebAbstractOptionsField<FilterSelect>
        implements
        LookupField, Component.Wrapper {
    private Object nullOption;
    private FilterMode filterMode;
    private NewOptionHandler newOptionHandler;

    public WebLookupField() {
        this.component = new FilterSelect() {
            @Override
            public void setPropertyDataSource(Property newDataSource) {

                if (newDataSource == null) {
                    super.setPropertyDataSource(null);
                } else {
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
            }
        };
        attachListener(component);
        component.setImmediate(true);
        component.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_ITEM);
        component.setFixedTextBoxWidth(true);

        setFilterMode(FilterMode.CONTAINS);

        setNewOptionAllowed(false);
        component.setNewItemHandler(new AbstractSelect.NewItemHandler() {
            public void addNewItem(String newItemCaption) {
                if (newOptionHandler == null) {
                    throw new IllegalStateException("New item handler cannot be NULL");
                }
                newOptionHandler.addNewOption(newItemCaption);
            }
        });
    }

    @Override
    public boolean isMultiSelect() {
        return false;
    }

    @Override
    public void setMultiSelect(boolean multiselect) {
        // Don't support multiselection for Lookup
    }

    protected Object getKeyFromValue(Object value) {
        if (value instanceof Enum) {
            return value;
        } else {
            if (optionsDatasource != null) {
                return optionsDatasource.getItemId((Entity) value);
            } else {
                if ((optionsList != null) || (optionsMap != null))
                    return value;
                return (value instanceof Entity) ? ((Entity) value).getId() : value;
            }
        }
    }

    protected <T> T getValueFromKey(Object key) {
        if (key == null) return null;
        if (key instanceof Enum) {
            return (T) key;
        }

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
        component.setFilteringMode(WebComponentsHelper.convertFilterMode(mode));
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
        return (T) getValueFromKey(key);
    }

    @Override
    protected void attachListener(FilterSelect component) {
        component.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                final Object value = getValue();
                fireValueChanged(prevValue, value);
                prevValue = value;
                if (optionsDatasource != null) {
                    optionsDatasource.setItem((Entity) value);
                }
            }
        });
    }

    @Override
    public void setValue(Object value) {
        super.setValue(getKeyFromValue(value));
    }

    public Object getNullOption() {
        return nullOption;
    }

    public void setNullOption(Object nullOption) {
        this.nullOption = nullOption;
        component.setNullSelectionItemId(nullOption);
    }

    @Override
    public void setOptionsDatasource(CollectionDatasource datasource) {
        this.optionsDatasource = datasource;
        this.optionsDsManager = new DsManager(datasource, this);
        component.setContainerDataSource(new DsWrapper(datasource, true, optionsDsManager));

        if (captionProperty != null) {
            component.setItemCaptionPropertyId(optionsDatasource.getMetaClass().getProperty(captionProperty));
        }
    }

    public boolean isNewOptionAllowed() {
        return component.isNewItemsAllowed();
    }

    public void setNewOptionAllowed(boolean newItemAllowed) {
        component.setNewItemsAllowed(newItemAllowed);
    }

    public NewOptionHandler getNewOptionHandler() {
        return newOptionHandler;
    }

    public void setNewOptionHandler(NewOptionHandler newItemHandler) {
        this.newOptionHandler = newItemHandler;
    }

    @Override
    public void setDescriptionProperty(String descriptionProperty) {
        super.setDescriptionProperty(descriptionProperty);
        component.setShowOptionsDescriptions(descriptionProperty != null);
        if (optionsDatasource != null) {
            component.setItemDescriptionPropertyId(optionsDatasource.getMetaClass().getProperty(descriptionProperty));
        }
    }

    public void disablePaging() {
        component.disablePaging();
    }

    private class DsWrapper extends CollectionDsWrapper implements com.vaadin.data.Container.Ordered {

        public DsWrapper(CollectionDatasource datasource, DsManager dsManager) {
            super(datasource, dsManager);
        }

        public DsWrapper(CollectionDatasource datasource, boolean autoRefresh, DsManager dsManager) {
            super(datasource, autoRefresh, dsManager);
        }

        public DsWrapper(CollectionDatasource datasource, Collection<MetaPropertyPath> properties, DsManager dsManager) {
            super(datasource, properties, dsManager);
        }

        public DsWrapper(CollectionDatasource datasource, Collection<MetaPropertyPath> properties, boolean autoRefresh, DsManager dsManager) {
            super(datasource, properties, autoRefresh, dsManager);
        }

        @Override
        public Collection getItemIds() {
            Collection itemIds = super.getItemIds();
            Object valueKey = WebLookupField.super.getValue();
            if (valueKey != null && !itemIds.contains(valueKey)) {
                itemIds = new HashSet(itemIds);
                itemIds.add(valueKey);
            }
            return itemIds;
        }

        @Override
        public Item getItem(Object itemId) {
            Item item = super.getItem(itemId);
            if (item == null && WebLookupField.this.datasource != null) {
                Entity containingEntity = WebLookupField.this.datasource.getItem();

                Object value;
                if (WebLookupField.this.metaPropertyPath != null)
                    value = InstanceUtils.getValueEx(containingEntity, WebLookupField.this.metaPropertyPath.getPath());
                else
                    value = containingEntity.getValue(WebLookupField.this.metaProperty.getName());

                if (value instanceof Entity && ((Entity) value).getId().equals(itemId)) {
                    item = getItemWrapper(value);
                }
            }
            return item;
        }

        @Override
        public int size() {
            if (datasource instanceof CollectionDatasource.Lazy) {
                if (Datasource.State.INVALID.equals(datasource.getState()))
                    datasource.refresh();

                if (datasource.size() == 0) {
                    Object valueKey = WebLookupField.super.getValue();
                    return (valueKey != null && !datasource.containsItem(valueKey)) ? 1 : 0;
                }

                return datasource.size();
            } else {
                return getItemIds().size();
            }
        }

        @Override
        public boolean containsId(Object itemId) {
            if (datasource instanceof CollectionDatasource.Lazy) {
                return datasource.containsItem(itemId);
            } else {
                Collection itemIds = getItemIds();
                return itemIds.contains(itemId);
            }
        }

        public Object nextItemId(Object itemId) {
            if (datasource instanceof CollectionDatasource.Ordered)
                return ((CollectionDatasource.Ordered) datasource).nextItemId(itemId);
            else
                throw new UnsupportedOperationException();
        }

        public Object prevItemId(Object itemId) {
            if (datasource instanceof CollectionDatasource.Ordered)
                return ((CollectionDatasource.Ordered) datasource).prevItemId(itemId);
            else
                throw new UnsupportedOperationException();
        }

        public Object firstItemId() {
            if (datasource instanceof CollectionDatasource.Ordered) {
                Object itemId = ((CollectionDatasource.Ordered) datasource).firstItemId();
                if (itemId == null && WebLookupField.this.datasource != null) {
                    Entity containingEntity = WebLookupField.this.datasource.getItem();

                    Object value;
                    if (WebLookupField.this.metaPropertyPath != null)
                        value = InstanceUtils.getValueEx(containingEntity, WebLookupField.this.metaPropertyPath.getPath());
                    else
                        value = containingEntity.getValue(WebLookupField.this.metaProperty.getName());

                    if (value instanceof Entity) {
                        return ((Entity) value).getId();
                    } else
                        return value;
                } else
                    return itemId;
            } else
                throw new UnsupportedOperationException();
        }

        public Object lastItemId() {
            if (datasource instanceof CollectionDatasource.Ordered)
                return ((CollectionDatasource.Ordered) datasource).lastItemId();
            else
                throw new UnsupportedOperationException();
        }

        public boolean isFirstId(Object itemId) {
            if (datasource instanceof CollectionDatasource.Ordered)
                return ((CollectionDatasource.Ordered) datasource).isFirstId(itemId);
            else
                throw new UnsupportedOperationException();
        }

        public boolean isLastId(Object itemId) {
            if (datasource instanceof CollectionDatasource.Ordered)
                return ((CollectionDatasource.Ordered) datasource).isLastId(itemId);
            else
                throw new UnsupportedOperationException();
        }

        public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }
    }
}
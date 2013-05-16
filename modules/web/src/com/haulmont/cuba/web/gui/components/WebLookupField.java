/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.OptionsDsWrapper;
import com.haulmont.cuba.web.toolkit.ui.FilterSelect;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.AbstractSelect;
import org.apache.commons.lang.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author abramov
 * @version $Id$
 */
public class WebLookupField
        extends WebAbstractOptionsField<FilterSelect>
        implements LookupField, Component.Wrapper {

    protected Object nullOption;
    protected FilterMode filterMode;
    protected NewOptionHandler newOptionHandler;

    protected Object missingValue = null;

    protected ComponentErrorHandler componentErrorHandler;

    public WebLookupField() {
        createComponent();

        attachListener(component);
        component.setImmediate(true);
        component.setItemCaptionMode(AbstractSelect.ItemCaptionMode.ITEM);
//        vaadin7
//        component.setFixedTextBoxWidth(true);
        component.setInvalidAllowed(false);
        component.setInvalidCommitted(true);

        setFilterMode(FilterMode.CONTAINS);

        setNewOptionAllowed(false);
        component.setNewItemHandler(new AbstractSelect.NewItemHandler() {
            @Override
            public void addNewItem(String newItemCaption) {
                if (newOptionHandler == null) {
                    throw new IllegalStateException("New item handler cannot be NULL");
                }
                newOptionHandler.addNewOption(newItemCaption);
            }
        });
    }

    protected void createComponent() {
        this.component = new FilterSelect() {
            @Override
            public void setPropertyDataSource(Property newDataSource) {
                if (newDataSource == null)
                    super.setPropertyDataSource(null);
                else
                    super.setPropertyDataSource(new LookupPropertyAdapter(newDataSource));
            }

            @Override
            public void setComponentError(ErrorMessage componentError) {
                boolean handled = false;
                if (componentErrorHandler != null)
                    handled = componentErrorHandler.handleError(componentError);

                if (!handled)
                    super.setComponentError(componentError);
            }
        };
    }

    @Override
    public boolean isMultiSelect() {
        return false;
    }

    @Override
    public void setMultiSelect(boolean multiselect) {
        // Don't support multiselection for Lookup
    }

    @Override
    protected Object getKeyFromValue(Object value) {
        if (value instanceof Enum) {
            return value;
        } else {
            if (optionsDatasource != null) {
                if (Datasource.State.INVALID == optionsDatasource.getState()) {
                    optionsDatasource.refresh();
                }
                return (value instanceof Entity) ? ((Entity) value).getId() : value;
            } else {
                if ((optionsList != null) || (optionsMap != null))
                    return value;
                return (value instanceof Entity) ? ((Entity) value).getId() : value;
            }
        }
    }

    @Override
    protected <T> T getValueFromKey(Object key) {
        if (key == null) return null;
        if (key instanceof Enum) {
            return (T) key;
        }

        Object v;
        if (optionsDatasource != null) {
            if (Datasource.State.INVALID == optionsDatasource.getState()) {
                optionsDatasource.refresh();
            }
            v = (T) optionsDatasource.getItem(key);
        } else {
            v = key;
        }

        return (T) v;
    }

    @Override
    public void setFilterMode(FilterMode mode) {
        filterMode = mode;
        component.setFilteringMode(WebComponentsHelper.convertFilterMode(mode));
    }

    @Override
    public FilterMode getFilterMode() {
        return filterMode;
    }

    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);
        component.setNullSelectionAllowed(!required);
    }

    @Override
    protected void attachListener(FilterSelect component) {
        component.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
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
    public Object getNullOption() {
        return nullOption;
    }

    @Override
    public void setNullOption(Object nullOption) {
        this.nullOption = nullOption;
        component.setNullSelectionItemId(nullOption);
    }

    @Override
    public void setOptionsDatasource(CollectionDatasource datasource) {
        this.optionsDatasource = datasource;
        component.setContainerDataSource(new LookupOptionsDsWrapper(datasource, true));

        if (captionProperty != null) {
            component.setItemCaptionPropertyId(optionsDatasource.getMetaClass().getProperty(captionProperty));
        }
    }

    @Override
    public boolean isNewOptionAllowed() {
        return component.isNewItemsAllowed();
    }

    @Override
    public void setNewOptionAllowed(boolean newItemAllowed) {
        component.setNewItemsAllowed(newItemAllowed);
    }

    @Override
    public NewOptionHandler getNewOptionHandler() {
        return newOptionHandler;
    }

    @Override
    public void setNewOptionHandler(NewOptionHandler newItemHandler) {
        this.newOptionHandler = newItemHandler;
    }

    @Override
    public void setDescriptionProperty(String descriptionProperty) {
        super.setDescriptionProperty(descriptionProperty);
//        vaadin7
//        component.setShowOptionsDescriptions(descriptionProperty != null);
//        if (optionsDatasource != null) {
//            component.setItemDescriptionPropertyId(optionsDatasource.getMetaClass().getProperty(descriptionProperty));
//        }
    }

    @Override
    public void disablePaging() {
//        vaadin7
//        component.disablePaging();
    }

    protected class LookupOptionsDsWrapper extends OptionsDsWrapper {

        public LookupOptionsDsWrapper(CollectionDatasource datasource, boolean autoRefresh) {
            super(datasource, autoRefresh);
        }

        @Override
        public boolean containsId(Object itemId) {
            boolean containsFlag = super.containsId(itemId);
            if (!containsFlag)
                missingValue = itemId;
            return true;
        }

        @Override
        public Collection getItemIds() {
            Collection itemIds = super.getItemIds();
            if (missingValue != null && !itemIds.contains(missingValue)) {
                Collection newItemIds = new ArrayList(itemIds.size() + 1);
                newItemIds.add(missingValue);
                for (Object itemId : itemIds)
                    newItemIds.add(itemId);
                itemIds = newItemIds;
            }
            return itemIds;
        }

        @Override
        public Item getItem(Object itemId) {
            if (ObjectUtils.equals(missingValue, itemId))
                return getItemWrapper(missingValue);

            return super.getItem(itemId);
        }

        @Override
        public int size() {
            int size = super.size();
            if (missingValue != null)
                size++;
            return size;
        }
    }

    protected class LookupPropertyAdapter extends PropertyAdapter {
        public LookupPropertyAdapter(Property itemProperty) {
            super(itemProperty);
        }

        @Override
        public Object getValue() {
            Object value = itemProperty.getValue();
            if (optionsDatasource != null &&
                    !ObjectUtils.equals(missingValue, value) &&
                    !optionsDatasource.containsItem(value)) {
                missingValue = value;
            }
            return value;
        }

        @Override
        public void setValue(Object newValue) throws ReadOnlyException {
            if (optionsDatasource != null &&
                    !ObjectUtils.equals(missingValue, newValue) &&
                    !optionsDatasource.containsItem(newValue)) {
                missingValue = newValue;
            }
            itemProperty.setValue(newValue);
        }
    }

    protected interface ComponentErrorHandler {
        boolean handleError(ErrorMessage message);
    }
}
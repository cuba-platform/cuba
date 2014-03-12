/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.EnumerationContainer;
import com.haulmont.cuba.web.gui.data.ObjectContainer;
import com.haulmont.cuba.web.gui.data.OptionsDsWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaComboBox;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.AbstractSelect;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author abramov
 * @version $Id$
 */
public class WebLookupField extends WebAbstractOptionsField<CubaComboBox> implements LookupField {

    protected Object nullOption;
    protected Entity nullEntity;

    protected FilterMode filterMode;
    protected NewOptionHandler newOptionHandler;

    protected Object missingValue = null;

    protected ComponentErrorHandler componentErrorHandler;

    protected Messages messages = AppBeans.get(Messages.NAME);

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
        this.component = new CubaComboBox() {
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
                return value;
            } else {
                return value;
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
            v = optionsDatasource.getItem(key);
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
    protected void attachListener(CubaComboBox component) {
        component.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (settingValue)
                    return;

                final Object value = getValue();

                Object newValue = fireValueChanging(prevValue, value);

                final Object oldValue = prevValue;
                prevValue = newValue;

                // use setting block value only for ValueChangingListener
                settingValue = true;
                if (!ObjectUtils.equals(value, newValue)) {
                    WebLookupField.this.component.setValue(newValue);
                }
                settingValue = false;

                if (optionsDatasource != null) {
                    optionsDatasource.setItem((Entity) value);
                }
                fireValueChanged(oldValue, newValue);
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
        if (nullOption != null) {
            if (optionsDatasource != null) {
                initNullEntity();
            } else {
                component.setNullSelectionItemId(nullOption);
            }
        } else {
            component.setNullSelectionItemId(null);
        }
    }

    protected void initNullEntity() {
        nullEntity = new AbstractNotPersistentEntity() {
            @Override
            public String getInstanceName() {
                if (nullOption instanceof Instance) {
                    return InstanceUtils.getInstanceName((Instance) nullOption);
                }

                if (nullOption == null) {
                    return "";
                } else {
                    return nullOption.toString();
                }
            }

            // Used for captionProperty of null entity
            @Override
            public <T> T getValue(String s) {
                return (T) getInstanceName();
            }
        };
        component.setNullSelectionItemId(nullEntity);
    }

    @Override
    public void setOptionsDatasource(CollectionDatasource datasource) {
        this.optionsDatasource = datasource;
        component.setContainerDataSource(new LookupOptionsDsWrapper(datasource, true));

        if (captionProperty != null) {
            component.setItemCaptionPropertyId(optionsDatasource.getMetaClass().getProperty(captionProperty));
        }

        if (nullOption != null) {
            initNullEntity();
        }

        assignAutoDebugId();
    }

    @Override
    protected String getAlternativeDebugId() {
        if (id != null) {
            return id;
        }
        if (datasource != null && StringUtils.isNotEmpty(datasource.getId()) && metaPropertyPath != null) {
            return getClass().getSimpleName() + "_" + datasource.getId() + "_" + metaPropertyPath.toString();
        }
        if (optionsDatasource != null &&  StringUtils.isNotEmpty(optionsDatasource.getId())) {
            return getClass().getSimpleName() + "_" + optionsDatasource.getId();
        }

        return getClass().getSimpleName();
    }

    @Override
    protected EnumerationContainer createEnumContainer(List options) {
        return new NullNameAwareEnumContainer(options);
    }

    @Override
    protected ObjectContainer createObjectContainer(List opts) {
        return new NullNameAwareObjectContainer(opts);
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

    @Deprecated
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
            //noinspection unchecked
            Collection<Object> itemIds = super.getItemIds();
            Collection<Object> additionalItemIds = null;

            if (nullOption != null) {
                additionalItemIds = new LinkedHashSet<>();
                additionalItemIds.add(nullEntity);
            }

            if (missingValue != null && !itemIds.contains(missingValue)) {
                if (additionalItemIds == null) {
                    additionalItemIds = new LinkedHashSet<>();
                }
                additionalItemIds.add(missingValue);
            }

            if (additionalItemIds != null) {
                additionalItemIds.addAll(itemIds);
                return additionalItemIds;
            }

            return itemIds;
        }

        @Override
        public Item getItem(Object itemId) {
            if (ObjectUtils.equals(missingValue, itemId)) {
                return getItemWrapper(missingValue);
            }
            if (ObjectUtils.equals(nullEntity, itemId)) {
                return getItemWrapper(nullEntity);
            }

            return super.getItem(itemId);
        }

        @Override
        public int size() {
            int size = super.size();
            if (missingValue != null)
                size++;
            if (nullOption != null)
                size++;
            return size;
        }

        @Override
        public Object firstItemId() {
            if (nullEntity != null) {
                return nullEntity;
            }

            return super.firstItemId();
        }

        @Override
        public Object lastItemId() {
            if (size() == 0)
                return nullEntity;

            return super.lastItemId();
        }

        @Override
        public Object nextItemId(Object itemId) {
            if (ObjectUtils.equals(nullEntity, itemId))
                return super.firstItemId();

            return super.nextItemId(itemId);
        }

        @Override
        public Object prevItemId(Object itemId) {
            if (ObjectUtils.equals(nullEntity, super.firstItemId())) {
                return nullEntity;
            }
            if (ObjectUtils.equals(missingValue, super.firstItemId())) {
                return missingValue;
            }

            return super.prevItemId(itemId);
        }

        @Override
        public boolean isFirstId(Object itemId) {
            if (ObjectUtils.equals(nullEntity, itemId)) {
                return true;
            }
            if (ObjectUtils.equals(missingValue, itemId)) {
                return true;
            }

            return super.isFirstId(itemId);
        }

        @Override
        public boolean isLastId(Object itemId) {
            if (size() == 0 && ObjectUtils.equals(nullEntity, itemId))
                return true;

            return super.isLastId(itemId);
        }
    }

    protected class NullNameAwareEnumContainer extends EnumerationContainer {

        public NullNameAwareEnumContainer(List<Enum> values) {
            super(values);
        }

        @Override
        public Collection getItemIds() {
            //noinspection unchecked
            Collection<Object> itemIds = super.getItemIds();
            if (nullOption != null) {
                Collection<Object> withNull = new LinkedHashSet<>();
                withNull.add(nullOption);
                withNull.addAll(itemIds);
                itemIds = withNull;
            }
            return itemIds;
        }

        @Override
        public Item getItem(Object itemId) {
            if (ObjectUtils.equals(nullOption, itemId)) {
                return new NullOptionItem();
            }

            return super.getItem(itemId);
        }
    }

    protected class NullNameAwareObjectContainer extends ObjectContainer {

        public NullNameAwareObjectContainer(List values) {
            super(values);
        }

        @Override
        public Collection getItemIds() {
            //noinspection unchecked
            Collection<Object> itemIds = super.getItemIds();
            if (nullOption != null) {
                Collection<Object> withNull = new LinkedHashSet<>();
                withNull.add(nullOption);
                withNull.addAll(itemIds);
                itemIds = withNull;
            }
            return itemIds;
        }

        @Override
        public Item getItem(Object itemId) {
            if (ObjectUtils.equals(nullOption, itemId)) {
                return new NullOptionItem();
            }

            return super.getItem(itemId);
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

    protected class NullOptionItem implements Item {
        @Override
        public Property getItemProperty(Object id) {
            return null;
        }

        @Override
        public Collection<?> getItemPropertyIds() {
            return Collections.emptyList();
        }

        @Override
        public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
            return false;
        }

        @Override
        public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
            return false;
        }

        @Override
        public String toString() {
            if (nullOption == null) {
                return "";
            }
            if (nullOption instanceof Enum) {
                return messages.getMessage((Enum) nullOption);
            }
            if (nullOption instanceof Entity) {
                return InstanceUtils.getInstanceName((Instance) nullOption);
            }
            return nullOption.toString();
        }
    }

    protected interface ComponentErrorHandler {
        boolean handleError(ErrorMessage message);
    }
}
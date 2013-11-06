/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.cuba.web.gui.data.EnumerationContainer;
import com.haulmont.cuba.web.toolkit.ui.FilterSelect;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.terminal.ErrorMessage;
import com.vaadin.ui.AbstractSelect;
import org.apache.commons.lang.ObjectUtils;

import javax.annotation.Nullable;
import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
public class WebLookupField
        extends WebAbstractOptionsField<FilterSelect>
        implements LookupField {

    protected Object nullOption;
    protected Entity nullEntity;
    protected FilterMode filterMode;
    protected NewOptionHandler newOptionHandler;

    protected ComponentErrorHandler componentErrorHandler;

    public WebLookupField() {
        createComponent();

        attachListener(component);
        component.setImmediate(true);
        component.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_ITEM);
        component.setFixedTextBoxWidth(true);
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
                if (newDataSource == null) {
                    super.setPropertyDataSource(null);
                } else {
                    super.setPropertyDataSource(new LookupPropertyAdapter(newDataSource) {
                        @Override
                        public Object getValue() {
                            final Object o = itemProperty.getValue();
                            return getKeyFromValue(o);
                        }

                        @Override
                        public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
                            if (!optionsInitialization) {
                                Object v = getValueFromKey(newValue);
                                if (newValue != null) {
                                    if (v == null && !items.containsId(v)) {
                                        Object valueKey = WebLookupField.super.getValue();
                                        if (newValue == valueKey)
                                            v = getValueFromDs();
                                    }
                                }
                                itemProperty.setValue(v);
                            }
                        }
                    });
                }
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

    protected Object getValueFromDs() {
        Object value;
        Entity containingEntity = this.datasource.getItem();
        if (this.metaPropertyPath != null)
            value = InstanceUtils.getValueEx(containingEntity, this.metaPropertyPath.getPath());
        else
            value = containingEntity.getValue(this.metaProperty.getName());
        return value;
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
    public <T> T getValue() {
        final Object key = super.getValue();
        return (T) getValueFromKey(key);
    }

    @Override
    protected void attachListener(FilterSelect component) {
        component.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (settingValue)
                    return;

                settingValue = true;

                final Object value = getValue();

                Object newValue = fireValueChanging(prevValue, value);

                final Object oldValue = prevValue;
                prevValue = newValue;

                if (!ObjectUtils.equals(value, newValue)) {
                    WebLookupField.this.component.setValue(newValue);
                }
                fireValueChanged(oldValue, newValue);

                if (optionsDatasource != null) {
                    optionsDatasource.setItem((Entity) value);
                }

                settingValue = false;
            }
        });
    }

    @Override
    public void setValue(@Nullable Object value) {
        super.setValue(getKeyFromValue(value));
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
                return WebLookupField.this.nullOption.toString();
            }
        };
        component.setNullSelectionItemId(nullEntity.getId());
    }

    @Override
    public void setOptionsDatasource(CollectionDatasource datasource) {
        this.optionsDatasource = datasource;
        component.setContainerDataSource(new DsWrapper(datasource, true));

        if (captionProperty != null) {
            component.setItemCaptionPropertyId(optionsDatasource.getMetaClass().getProperty(captionProperty));
        }

        if (nullOption != null) {
            initNullEntity();
        }
    }

    @Override
    protected EnumerationContainer createEnumContainer(List options) {
        return new NullNameAwareEnumContainer(options);
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
        component.setShowOptionsDescriptions(descriptionProperty != null);
        if (optionsDatasource != null) {
            component.setItemDescriptionPropertyId(optionsDatasource.getMetaClass().getProperty(descriptionProperty));
        }
    }

    @Override
    public void disablePaging() {
        component.disablePaging();
    }

    protected abstract class LookupPropertyAdapter extends PropertyAdapter {
        public LookupPropertyAdapter(Property itemProperty) {
            super(itemProperty);
        }

        public Object getInternalValue() {
            return itemProperty.getValue();
        }
    }

    // Shows LookupFiled value even if it is not present in options list
    protected class DsWrapper extends CollectionDsWrapper implements com.vaadin.data.Container.Ordered {

        private Object previousValue = null;

        public DsWrapper(CollectionDatasource datasource, boolean autoRefresh) {
            super(datasource, autoRefresh);
        }

        @Override
        public Collection getItemIds() {
            //noinspection unchecked
            Collection<Object> itemIds = super.getItemIds();
            Object valueKey = WebLookupField.super.getValue();
            if (valueKey != null && previousValue == null)
                previousValue = valueKey;

            boolean modifiable = false;
            if (valueKey != null && !itemIds.contains(valueKey)) {
                itemIds = new HashSet<>(itemIds);
                itemIds.add(valueKey);
                modifiable = true;
            } else if (previousValue != null && !itemIds.contains(previousValue)) {
                itemIds = new HashSet<>();
                itemIds.add(previousValue);
                modifiable = true;
            }

            if (nullOption != null) {
                if (!modifiable)
                    itemIds = new HashSet<>(itemIds);
                itemIds.add(nullEntity);
            }

            return itemIds;
        }

        @Override
        public Item getItem(Object itemId) {
            Item item = super.getItem(itemId);
            if (item == null) {
                if (nullEntity != null && nullEntity.getId().equals(itemId)) {
                    item = getItemWrapper(nullEntity);
                } else if (WebLookupField.this.datasource != null) {
                    Object value = WebLookupField.this.getValueFromDs();

                    if (value instanceof Entity && ((Entity) value).getId().equals(itemId)) {
                        item = getItemWrapper(value);
                    }
                } else if (WebLookupField.this.component.getPropertyDataSource() != null) {
                    // For work in field group
                    Property propertyDataSource = WebLookupField.this.component.getPropertyDataSource();
                    Object value;
                    if (propertyDataSource instanceof LookupPropertyAdapter)
                        value = ((LookupPropertyAdapter) propertyDataSource).getInternalValue();
                    else
                        value = propertyDataSource.getValue();

                    if (value instanceof Entity && ((Entity) value).getId().equals(itemId)) {
                        item = getItemWrapper(value);
                    }
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

        @Override
        public Object nextItemId(Object itemId) {
            if (datasource instanceof CollectionDatasource.Ordered)
                if (nullEntity != null && nullEntity.getId().equals(itemId))
                    return ((CollectionDatasource.Ordered) datasource).firstItemId();
                else
                    return ((CollectionDatasource.Ordered) datasource).nextItemId(itemId);
            else
                throw new UnsupportedOperationException();
        }

        @Override
        public Object prevItemId(Object itemId) {
            if (datasource instanceof CollectionDatasource.Ordered)
                return ((CollectionDatasource.Ordered) datasource).prevItemId(itemId);
            else
                throw new UnsupportedOperationException();
        }

        @Override
        public Object firstItemId() {
            if (nullEntity != null) {
                return nullEntity.getId();
            }

            if (datasource instanceof CollectionDatasource.Ordered) {
                Object itemId = ((CollectionDatasource.Ordered) datasource).firstItemId();
                if (itemId == null && WebLookupField.this.datasource != null) {
                    Object value = WebLookupField.this.getValueFromDs();

                    if (value instanceof Entity) {
                        return ((Entity) value).getId();
                    } else
                        return value;
                } else
                    return itemId;
            } else
                throw new UnsupportedOperationException();
        }

        @Override
        public Object lastItemId() {
            if (size() == 0)
                return nullEntity.getId();

            if (datasource instanceof CollectionDatasource.Ordered)
                return ((CollectionDatasource.Ordered) datasource).lastItemId();
            else
                throw new UnsupportedOperationException();
        }

        @Override
        public boolean isFirstId(Object itemId) {
            if (nullEntity != null && nullEntity.getId().equals(itemId))
                return true;

            if (datasource instanceof CollectionDatasource.Ordered)
                return ((CollectionDatasource.Ordered) datasource).isFirstId(itemId);
            else
                throw new UnsupportedOperationException();
        }

        @Override
        public boolean isLastId(Object itemId) {
            if (size() == 0 && nullEntity != null && nullEntity.getId().equals(itemId))
                return true;

            if (datasource instanceof CollectionDatasource.Ordered)
                return ((CollectionDatasource.Ordered) datasource).isLastId(itemId);
            else
                throw new UnsupportedOperationException();
        }

        @Override
        public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }
    }

    protected class NullNameAwareEnumContainer extends EnumerationContainer {

        public NullNameAwareEnumContainer(List values) {
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
            if (nullOption != null && nullOption.equals(itemId)) {
                return new Item() {
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
                        return String.valueOf(nullOption);
                    }
                };
            }
            return super.getItem(itemId);
        }
    }

    protected interface ComponentErrorHandler {
        boolean handleError(ErrorMessage message);
    }
}
/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
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
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ErrorMessage;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.*;

import static com.vaadin.event.ShortcutAction.KeyCode;
import static com.vaadin.event.ShortcutAction.ModifierKey;
import static com.vaadin.ui.AbstractSelect.ItemCaptionMode;

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
        component.setItemCaptionMode(ItemCaptionMode.ITEM);
        component.setInvalidAllowed(false);
        component.setInvalidCommitted(true);

        setFilterMode(FilterMode.CONTAINS);

        setNewOptionAllowed(false);
        component.setNewItemHandler(newItemCaption -> {
            if (newOptionHandler == null) {
                throw new IllegalStateException("New item handler cannot be NULL");
            }
            newOptionHandler.addNewOption(newItemCaption);
        });

        component.addShortcutListener(new ShortcutListener("clearShortcut", KeyCode.DELETE, new int[]{ModifierKey.SHIFT}) {
            @Override
            public void handleAction(Object sender, Object target) {
                if (!isRequired() && isEnabled() && isEditable()) {
                    setValue(null);
                }
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
        // Don't support multiselect for LookupField
    }

    protected Object getValueFromOptions(Object value) {
        if (optionsDatasource != null && value instanceof Entity) {
            if (Datasource.State.INVALID == optionsDatasource.getState()) {
                optionsDatasource.refresh();
            }
            Object itemId = ((Entity) value).getId();
            if (optionsDatasource.containsItem(itemId)) {
                value = optionsDatasource.getItem(itemId);
            }
        }

        return value;
    }

    @Override
    public void setValue(@Nullable Object value) {
        super.setValue(getValueFromOptions(value));

        checkMissingValue();
    }

    protected void checkMissingValue() {
        if (missingValue != null && component.getValue() != missingValue) {
            missingValue = null;
            if (component.getContainerDataSource() instanceof LookupFieldDsWrapper) {
                ((LookupFieldDsWrapper) component.getContainerDataSource()).forceItemSetNotification();
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getValue() {
        final Object value = super.getValue();
        return (T) getValueFromOptions(value);
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
        component.addValueChangeListener(event -> {
            final Object value = getValue();
            final Object oldValue = prevValue;
            prevValue = value;

            if (optionsDatasource != null) {
                optionsDatasource.setItem((Entity) value);
            }
            fireValueChanged(oldValue, value);

            checkMissingValue();
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
            setInputPrompt(null);
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

        checkMissingValue();

        assignAutoDebugId();
    }

    @Override
    public void setOptionsList(List optionsList) {
        super.setOptionsList(optionsList);

        checkMissingValue();
    }

    @Override
    public void setOptionsMap(Map<String, Object> options) {
        super.setOptionsMap(options);

        checkMissingValue();
    }

    @Override
    public void setOptionsEnum(Class<? extends EnumClass> optionsEnum) {
        super.setOptionsEnum(optionsEnum);

        checkMissingValue();
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
    public boolean isTextInputAllowed() {
        return component.isTextInputAllowed();
    }

    @Override
    public void setTextInputAllowed(boolean textInputAllowed) {
        component.setTextInputAllowed(textInputAllowed);
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
    public String getInputPrompt() {
        return component.getInputPrompt();
    }

    @Override
    public void setInputPrompt(String inputPrompt) {
        if (StringUtils.isNotBlank(inputPrompt)) {
            setNullOption(null);
        }
        component.setInputPrompt(inputPrompt);
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

    protected interface LookupFieldDsWrapper {
        void forceItemSetNotification();
    }

    protected class LookupOptionsDsWrapper extends OptionsDsWrapper implements LookupFieldDsWrapper {

        public LookupOptionsDsWrapper(CollectionDatasource datasource, boolean autoRefresh) {
            super(datasource, autoRefresh);
        }

        @Override
        public void forceItemSetNotification() {
            fireItemSetChanged();
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

    protected class NullNameAwareEnumContainer extends EnumerationContainer implements LookupFieldDsWrapper {

        public NullNameAwareEnumContainer(List<Enum> values) {
            super(values);
        }

        @Override
        public void forceItemSetNotification() {
            fireItemSetChanged();
        }

        @Override
        public boolean containsId(Object itemId) {
            boolean containsFlag = super.containsId(itemId);
            if (!containsFlag) {
                missingValue = itemId;
            }
            return true;
        }

        @Override
        public Collection getItemIds() {
            //noinspection unchecked
            Collection<Object> itemIds = super.getItemIds();
            Collection<Object> additionalItemIds = null;

            if (nullOption != null) {
                additionalItemIds = new LinkedHashSet<>();
                additionalItemIds.add(nullOption);
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
            if (ObjectUtils.equals(nullOption, itemId)) {
                return new NullOptionItem();
            }
            if (ObjectUtils.equals(missingValue, itemId)) {
                return new EnumerationItem(missingValue);
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
    }

    protected class NullNameAwareObjectContainer extends ObjectContainer implements LookupFieldDsWrapper {
        public NullNameAwareObjectContainer(List values) {
            super(values);
        }

        @Override
        public void forceItemSetNotification() {
            fireItemSetChanged();
        }

        @Override
        public boolean containsId(Object itemId) {
            boolean containsFlag = super.containsId(itemId);
            if (!containsFlag) {
                missingValue = itemId;
            }
            return true;
        }

        @Override
        public Collection getItemIds() {
            //noinspection unchecked
            Collection<Object> itemIds = super.getItemIds();
            Collection<Object> additionalItemIds = null;

            if (nullOption != null) {
                additionalItemIds = new LinkedHashSet<>();
                additionalItemIds.add(nullOption);
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
            if (ObjectUtils.equals(nullOption, itemId)) {
                return new NullOptionItem();
            }
            if (ObjectUtils.equals(missingValue, itemId)) {
                return new ObjectItem(missingValue);
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
    }

    protected class LookupPropertyAdapter extends PropertyAdapter {
        public LookupPropertyAdapter(Property itemProperty) {
            super(itemProperty);
        }

        @Override
        public Object getValue() {
            Object value = itemProperty.getValue();
            adoptMissingValue(value);
            return value;
        }

        @Override
        public void setValue(Object newValue) throws ReadOnlyException {
            adoptMissingValue(newValue);
            itemProperty.setValue(newValue);
        }

        protected void adoptMissingValue(Object value) {
            if (!ObjectUtils.equals(missingValue, value)) {
                if (optionsDatasource != null && !optionsDatasource.containsItem(value)) {
                    missingValue = value;
                } else if (optionsList != null && !optionsList.contains(value)) {
                    missingValue = value;
                } else if (optionsMap != null && !optionsMap.containsValue(value)) {
                    missingValue = value;
                }
            }
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
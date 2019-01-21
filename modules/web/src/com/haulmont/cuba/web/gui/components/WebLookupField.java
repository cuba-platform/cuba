/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.EnumerationContainer;
import com.haulmont.cuba.web.gui.data.ObjectContainer;
import com.haulmont.cuba.web.gui.data.OptionsDsWrapper;
import com.haulmont.cuba.web.gui.data.UnsubscribableDsWrapper;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.toolkit.ui.CubaComboBox;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ErrorMessage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.*;

import static com.vaadin.event.ShortcutAction.KeyCode;
import static com.vaadin.event.ShortcutAction.ModifierKey;
import static com.vaadin.ui.AbstractSelect.ItemCaptionMode;

public class WebLookupField extends WebAbstractOptionsField<CubaComboBox> implements LookupField {

    protected Object nullOption;
    protected Entity nullEntity;

    protected NewOptionHandler newOptionHandler;

    protected Object missingValue = null;

    protected ComponentErrorHandler componentErrorHandler;

    protected boolean nullOptionVisible = true;
    protected OptionIconProvider optionIconProvider;

    protected IconResolver iconResolver = AppBeans.get(IconResolver.class);
    protected OptionsStyleProvider optionsStyleProvider;
    protected FilterPredicate filterPredicate;

    public WebLookupField() {
        createComponent();

        attachListener(component);
        component.setImmediate(true);
        component.setItemCaptionMode(ItemCaptionMode.ITEM);
        component.setInvalidAllowed(false);
        component.setInvalidCommitted(true);

        Configuration configuration = AppBeans.get(Configuration.NAME);
        setPageLength(configuration.getConfig(ClientConfig.class).getLookupFieldPageLength());

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
        component.setFilteringMode(WebWrapperUtils.toVaadinFilterMode(mode));
    }

    @Override
    public FilterMode getFilterMode() {
        return WebWrapperUtils.toFilterMode(component.getFilteringMode());
    }

    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);

        component.setNullSelectionAllowed(!required && nullOptionVisible);
    }

    @Override
    protected void attachListener(CubaComboBox component) {
        component.addValueChangeListener(vEvent -> {
            final Object value = getValue();
            final Object oldValue = prevValue;
            prevValue = value;

            if (hasValidationError()) {
                setValidationError(null);
            }

            if (optionsDatasource != null) {
                if (value != null && !(value instanceof Entity)) {
                    throw new IllegalStateException(String.format(
                            "Attempting to set non-entity value '%s' to LookupField with optionsDatasource for attribute '%s'",
                            value, getMetaProperty().getName()));
                }
                //noinspection unchecked
                optionsDatasource.setItem((Entity) value);
            }

            if (!InstanceUtils.propertyValueEquals(oldValue, value)) {
                ValueChangeEvent event = new ValueChangeEvent(this, oldValue, value);
                getEventRouter().fireEvent(ValueChangeListener.class, ValueChangeListener::valueChanged, event);
            }

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
        //noinspection IncorrectCreateEntity
        nullEntity = new BaseUuidEntity() {
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
    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;

        tryToAssignCaptionProperty();
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
        super.setCaptionMode(captionMode);

        tryToAssignCaptionProperty();
    }

    protected void setupDsAutoRefresh(OptionsDsWrapper ds) {
    }

    @Override
    public void setOptionsDatasource(CollectionDatasource datasource) {
        if (this.datasource == datasource)
            return;

        if (this.optionsDatasource != null) {
            com.vaadin.data.Container container = component.getContainerDataSource();
            if (container instanceof UnsubscribableDsWrapper) {
                UnsubscribableDsWrapper wrapper = (UnsubscribableDsWrapper) container;
                wrapper.unsubscribe();
            }
            component.setContainerDataSource(null);
        }

        this.optionsDatasource = datasource;

        if (datasource != null) {
            LookupOptionsDsWrapper optionsDsWrapper = new LookupOptionsDsWrapper(datasource, true);

            setupDsAutoRefresh(optionsDsWrapper);

            component.setContainerDataSource(optionsDsWrapper);

            tryToAssignCaptionProperty();

            if (nullOption != null)
                initNullEntity();

            checkMissingValue();

            assignAutoDebugId();
        }
    }

    protected void tryToAssignCaptionProperty() {
        if (optionsDatasource != null && captionProperty != null && getCaptionMode() == CaptionMode.PROPERTY) {
            MetaPropertyPath propertyPath = optionsDatasource.getMetaClass().getPropertyPath(captionProperty);

            if (propertyPath != null && component.getContainerDataSource() != null) {
                ((LookupOptionsDsWrapper) component.getContainerDataSource()).addProperty(propertyPath);
                component.setItemCaptionPropertyId(propertyPath);
            } else {
                throw new IllegalArgumentException(String.format("Can't find property for given caption property: %s", captionProperty));
            }
        }
    }

    @Override
    public void setOptionsList(List optionsList) {
        super.setOptionsList(optionsList);

        checkMissingValue();
    }

    @Override
    public void setOptionsMap(Map<String, ?> options) {
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
        if (optionsDatasource != null && StringUtils.isNotEmpty(optionsDatasource.getId())) {
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
    public boolean isAutomaticPopupOnFocus() {
        return false;
    }

    @Override
    public void setAutomaticPopupOnFocus(boolean automaticPopupOnFocus) {
        // do nothing
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
    public int getPageLength() {
        return component.getPageLength();
    }

    @Override
    public void setPageLength(int pageLength) {
        component.setPageLength(pageLength);
    }

    @Override
    public void setNullOptionVisible(boolean nullOptionVisible) {
        this.nullOptionVisible = nullOptionVisible;
        component.setNullSelectionAllowed(!isRequired() && nullOptionVisible);
    }

    @Override
    public boolean isNullOptionVisible() {
        return nullOptionVisible;
    }

    @SuppressWarnings("unchecked")
    public void setOptionIconProvider(OptionIconProvider<?> optionIconProvider) {
        setOptionIconProvider(Object.class, (OptionIconProvider) optionIconProvider);
    }

    @Override
    public <T> void setOptionIconProvider(Class<T> optionClass, OptionIconProvider<T> optionIconProvider) {
        if (this.optionIconProvider != optionIconProvider) {
            // noinspection unchecked
            this.optionIconProvider = optionIconProvider;

            if (optionIconProvider == null) {
                component.setOptionIconProvider(null);
            } else {
                component.setOptionIconProvider(itemId -> {
                    T typedItem = optionClass.cast(itemId);

                    String resourceId;
                    try {
                        // noinspection unchecked
                        resourceId = optionIconProvider.getItemIcon(typedItem);
                    } catch (Exception e) {
                        LoggerFactory.getLogger(WebLookupField.class)
                                .warn("Error invoking OptionIconProvider getItemIcon method", e);
                        return null;
                    }

                    return iconResolver.getIconResource(resourceId);
                });
            }
        }
    }

    public OptionIconProvider<?> getOptionIconProvider() {
        return optionIconProvider;
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
    public void setLookupSelectHandler(Runnable selectHandler) {
        // do nothing
    }

    @Override
    public Collection getLookupSelectedItems() {
        return Collections.singleton(getValue());
    }

    @Override
    public void commit() {
        super.commit();
    }

    @Override
    public void discard() {
        super.discard();
    }

    @Override
    public boolean isBuffered() {
        return super.isBuffered();
    }

    @Override
    public void setBuffered(boolean buffered) {
        super.setBuffered(buffered);
    }

    @Override
    public boolean isModified() {
        return super.isModified();
    }

    @Override
    public void setOptionsStyleProvider(OptionsStyleProvider optionsStyleProvider) {
        this.optionsStyleProvider = optionsStyleProvider;

        if (optionsStyleProvider != null) {
            component.setItemStyleGenerator((comboBox, item) ->
                    optionsStyleProvider.getItemStyleName(this, item));
        } else {
            component.setItemStyleGenerator(null);
        }
    }

    @Override
    public OptionsStyleProvider getOptionsStyleProvider() {
        return optionsStyleProvider;
    }

    @Override
    public void setFilterPredicate(FilterPredicate filterPredicate) {
        this.filterPredicate = filterPredicate;

        if (filterPredicate != null) {
            component.setFilterPredicate(filterPredicate::test);
        } else {
            component.setFilterPredicate(null);
        }
    }

    @Override
    public FilterPredicate getFilterPredicate() {
        return filterPredicate;
    }

    protected interface LookupFieldDsWrapper {
        void forceItemSetNotification();
    }

    protected class LookupOptionsDsWrapper extends OptionsDsWrapper implements LookupFieldDsWrapper {
        public LookupOptionsDsWrapper(CollectionDatasource datasource, boolean autoRefresh) {
            super(datasource, autoRefresh);
        }

        public void addProperty(MetaPropertyPath property) {
            if (!properties.contains(property)) {
                properties.add(property);
            }
        }

        @Override
        public void forceItemSetNotification() {
            fireItemSetChanged();
        }

        @Override
        public boolean containsId(Object itemId) {
            boolean optionsContainItem = super.containsId(itemId);
            if (!optionsContainItem) {
                missingValue = itemId;
                // refresh all item captions, forget old items
                itemsCache.clear();
            }
            return true;
        }

        @Override
        public Collection getItemIds() {
            Collection items = super.getItemIds();
            List<Object> optionsWithNullOrMissing = null;

            if (nullOption != null) {
                optionsWithNullOrMissing = new ArrayList<>(items.size() + 2);

                optionsWithNullOrMissing.add(nullEntity);
            }

            if (missingValue != null && !items.contains(missingValue)) {
                if (optionsWithNullOrMissing == null) {
                    optionsWithNullOrMissing = new ArrayList<>(items.size() + 1);
                }
                optionsWithNullOrMissing.add(missingValue);
            }

            if (optionsWithNullOrMissing == null) {
                return items;
            }

            optionsWithNullOrMissing.addAll(items);

            return optionsWithNullOrMissing;
        }

        @Override
        public Item getItem(Object itemId) {
            if (Objects.equals(missingValue, itemId)) {
                return getItemWrapper(missingValue);
            }
            if (Objects.equals(nullEntity, itemId)) {
                return getItemWrapper(nullEntity);
            }

            return super.getItem(itemId);
        }

        @Override
        public int size() {
            int size = super.size();
            if (missingValue != null) {
                size++;
            }
            if (nullOption != null) {
                size++;
            }
            return size;
        }

        @Override
        public Object firstItemId() {
            if (nullEntity != null) {
                return nullEntity;
            }
            if (missingValue != null) {
                return missingValue;
            }

            return super.firstItemId();
        }

        @Override
        public Object lastItemId() {
            int size = size();

            if (size == 1) {
                if (nullEntity != null) {
                    return nullEntity;
                }
                if (missingValue != null) {
                    return missingValue;
                }
            } else if (size == 2) {
                if (missingValue != null && nullEntity != null) {
                    return missingValue;
                }
            }

            return super.lastItemId();
        }

        @Override
        public Object nextItemId(Object itemId) {
            if (Objects.equals(nullEntity, itemId)) {
                if (missingValue != null) {
                    return missingValue;
                }

                return super.firstItemId();
            }

            if (Objects.equals(missingValue, itemId)) {
                return super.firstItemId();
            }

            return super.nextItemId(itemId);
        }

        @Override
        public Object prevItemId(Object itemId) {
            if (Objects.equals(nullEntity, itemId)) {
                return null;
            }

            if (Objects.equals(missingValue, itemId)) {
                if (nullEntity != null) {
                    return nullEntity;
                }

                return null;
            }

            return super.prevItemId(itemId);
        }

        @Override
        public boolean isFirstId(Object itemId) {
            if (Objects.equals(nullEntity, itemId)) {
                return true;
            }
            if (nullEntity == null) {
                if (Objects.equals(missingValue, itemId)) {
                    return true;
                }
            }

            return super.isFirstId(itemId);
        }

        @Override
        public boolean isLastId(Object itemId) {
            int size = size();
            if (size == 1) {
                if (Objects.equals(nullEntity, itemId)) {
                    return true;
                }
                if (Objects.equals(missingValue, itemId)) {
                    return true;
                }
            } else if (size == 2) {
                if (missingValue != null && nullEntity != null
                        && Objects.equals(missingValue, itemId)) {
                    return true;
                }
            }

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
            if (Objects.equals(nullOption, itemId)) {
                return new NullOptionItem();
            }
            if (Objects.equals(missingValue, itemId)) {
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
            if (Objects.equals(nullOption, itemId)) {
                return new NullOptionItem();
            }
            if (Objects.equals(missingValue, itemId)) {
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
            if (!Objects.equals(missingValue, value)) {
                Object itemId = null;
                if (value instanceof Entity) {
                    itemId = ((Entity) value).getId();
                }

                if (optionsDatasource != null && !optionsDatasource.containsItem(itemId)) {
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
        protected Messages messages = AppBeans.get(Messages.NAME);

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
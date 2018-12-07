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

import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.OptionsList;
import com.haulmont.cuba.gui.components.data.meta.EntityOptions;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.components.data.meta.OptionsBinding;
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.components.data.options.MapOptions;
import com.haulmont.cuba.gui.components.data.options.ContainerOptions;
import com.haulmont.cuba.gui.components.data.options.OptionsBinder;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.web.widgets.CubaListSelect;
import com.vaadin.v7.data.util.IndexedContainer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WebOptionsList<V, I> extends WebAbstractField<CubaListSelect, V>
        implements OptionsList<V, I>, InitializingBean {

    protected MetadataTools metadataTools;
    protected ApplicationContext applicationContext;

    protected OptionsBinding<I> optionsBinding;

    protected Function<? super I, String> optionCaptionProvider;

    protected String captionProperty;

    @SuppressWarnings("unchecked")
    public WebOptionsList() {
        component = createComponent();
    }

    protected CubaListSelect createComponent() {
        return new CubaListSelect();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initComponent(component);
    }

    protected void initComponent(CubaListSelect component) {
        component.setContainerDataSource(new IndexedContainer());
        component.setItemCaptionGenerator(this::generateItemCaption);

        attachListener(component);
    }

    protected String generateDefaultItemCaption(I item) {
        if (valueBinding != null && valueBinding.getSource() instanceof EntityValueSource) {
            EntityValueSource entityValueSource = (EntityValueSource) valueBinding.getSource();
            return metadataTools.format(item, entityValueSource.getMetaPropertyPath().getMetaProperty());
        }

        return metadataTools.format(item);
    }

    protected String generateItemCaption(Object objectItem) {
        //noinspection unchecked
        I item = (I) objectItem;

        if (item == null) {
            return null;
        }

        if (optionCaptionProvider != null) {
            return optionCaptionProvider.apply(item);
        }

        return generateDefaultItemCaption(item);
    }

    @Inject
    public void setMetadataTools(MetadataTools metadataTools) {
        this.metadataTools = metadataTools;
    }

    @Inject
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

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
    protected V convertToModel(Object componentRawValue) {
        if (isMultiSelect()) {
            Set collectionValue = (Set) componentRawValue;

            List<I> itemIds = getCurrentItems();
            Stream<I> selectedItemsStream = itemIds.stream()
                    .filter(collectionValue::contains);

            if (valueBinding != null) {
                Class<V> targetType = valueBinding.getSource().getType();

                if (List.class.isAssignableFrom(targetType)) {
                    return (V) selectedItemsStream.collect(Collectors.toList());
                }

                if (Set.class.isAssignableFrom(targetType)) {
                    return (V) selectedItemsStream.collect(Collectors.toCollection(LinkedHashSet::new));
                }
            }

            return (V) selectedItemsStream.collect(Collectors.toCollection(LinkedHashSet::new));
        }

        return super.convertToModel(componentRawValue);
    }

    @Override
    public V getValue() {
        return convertToModel(component.getValue());
    }

    @Override
    public void setValue(V value) {
        super.setValue(value);

        V oldValue = internalValue;
        internalValue = value;

        if (isMultiSelect()) {
            //noinspection unchecked
            if (isCollectionValuesChanged((Collection<I>) value, (Collection<I>) oldValue)) {
                ValueChangeEvent<V> event = new ValueChangeEvent<>(this, oldValue, value, false);
                publish(ValueChangeEvent.class, event);
            }
        } else if (!fieldValueEquals(value, oldValue)) {
            ValueChangeEvent<V> event = new ValueChangeEvent<>(this, oldValue, value, false);
            publish(ValueChangeEvent.class, event);
        }
    }

    @Override
    protected void componentValueChanged(Object newComponentValue, boolean userOriginated) {
        if (userOriginated) {
            CollectionContainer collectionContainer = null;
            if (optionsBinding.getSource() instanceof ContainerOptions) {
                collectionContainer = ((ContainerOptions) optionsBinding.getSource()).getContainer();
                collectionContainer.mute();
            }

            super.componentValueChanged(newComponentValue, userOriginated);

            if (collectionContainer != null) {
                collectionContainer.unmute(CollectionContainer.UnmuteEventsMode.FIRE_REFRESH_EVENT);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected List<I> getCurrentItems() {
        IndexedContainer container = (IndexedContainer) component.getContainerDataSource();

        return (List<I>) container.getItemIds();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object convertToPresentation(V modelValue) {
        if (isMultiSelect()) {
            if (modelValue instanceof List) {
                return new LinkedHashSet<I>((Collection<? extends I>) modelValue);
            }
        }

        return super.convertToPresentation(modelValue);
    }

    @Override
    public Options<I> getOptions() {
        return optionsBinding != null ? optionsBinding.getSource() : null;
    }

    @Override
    public void setOptions(Options<I> options) {
        if (this.optionsBinding != null) {
            this.optionsBinding.unbind();
            this.optionsBinding = null;
        }

        if (options != null) {
            OptionsBinder optionsBinder = applicationContext.getBean(OptionsBinder.NAME, OptionsBinder.class);
            this.optionsBinding = optionsBinder.bind(options, this, this::setItemsToPresentation);
            this.optionsBinding.activate();
        }

        if (options instanceof MapOptions) {
            setCaptionMode(CaptionMode.MAP_ENTRY);
        } else {
            setCaptionMode(CaptionMode.ITEM);
        }
    }

    @Override
    protected void setValueToPresentation(Object value) {
        component.setValueIgnoreReadOnly(value);
    }

    protected void setItemsToPresentation(Stream<I> options) {
        List<I> itemIds = options.collect(Collectors.toList());
        component.setContainerDataSource(new IndexedContainer(itemIds));
    }

    @Override
    public boolean isNullOptionVisible() {
        return component.isNullSelectionAllowed();
    }

    @Override
    public void setNullOptionVisible(boolean nullOptionVisible) {
        component.setNullSelectionAllowed(nullOptionVisible);
    }

    @Override
    public void setOptionCaptionProvider(Function<? super I, String> optionCaptionProvider) {
        this.optionCaptionProvider = optionCaptionProvider;

        component.markAsDirty();
    }

    @Override
    public Function<? super I, String> getOptionCaptionProvider() {
        return optionCaptionProvider;
    }

    @Override
    public CaptionMode getCaptionMode() {
        return WebWrapperUtils.toCaptionMode(component.getItemCaptionMode());
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
        if (this.getCaptionMode() != captionMode) {
            switch (captionMode) {
                case PROPERTY:
                    if (Strings.isNullOrEmpty(getCaptionProperty())) {
                        throw new IllegalStateException("Can't set CaptionMode = " + captionMode +
                                " if the captionProperty is null");
                    }

                    if (!EntityOptions.class.isAssignableFrom(optionsBinding.getSource().getClass())) {
                        throw new IllegalStateException("Can't set CaptionMode = " + captionMode +
                                " for non EntityOptions class");
                    }

                    setOptionCaptionProvider(this::generateOptionPropertyCaption);
                    break;
                case MAP_ENTRY:
                    if (!MapOptions.class.isAssignableFrom(optionsBinding.getSource().getClass())) {
                        throw new IllegalStateException("Can't set CaptionMode = " + captionMode +
                                " for non MapOptions class");
                    }

                    Map<String, I> optionsMap = ((MapOptions<I>) getOptions()).getItemsCollection();
                    BiMap<String, I> biMap = ImmutableBiMap.copyOf(optionsMap);
                    setOptionCaptionProvider(v -> biMap.inverse().get(v));
                    break;
                case ITEM:
                default:
                    // set null to use default behaviour
                    setOptionCaptionProvider(null);
            }
            component.setItemCaptionMode(WebWrapperUtils.toVaadinCaptionMode(captionMode));
        }
    }

    @Override
    public String getCaptionProperty() {
        return captionProperty;
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;
    }

    @Override
    public void focus() {
        component.focus();
    }

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }

    protected String generateOptionPropertyCaption(I item) {
        return ((Entity) item).getValueEx(captionProperty);
    }

    protected boolean isCollectionValuesChanged(Collection<I> value, Collection<I> oldValue) {
        if (value == null && oldValue == null) {
            return false;
        } else if (value == null || oldValue == null) {
            return true;
        }

        if (value.size() != oldValue.size()) {
            return true;
        }
        for (I item : value) {
            if (!oldValue.contains(item)) {
                return true;
            }
        }
        return false;
    }
}
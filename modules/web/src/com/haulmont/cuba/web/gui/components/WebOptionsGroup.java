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

import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.gui.components.data.EntityValueSource;
import com.haulmont.cuba.gui.components.data.OptionsBinding;
import com.haulmont.cuba.gui.components.data.OptionsSource;
import com.haulmont.cuba.gui.components.data.options.OptionsBinder;
import com.haulmont.cuba.web.widgets.CubaOptionGroup;
import com.haulmont.cuba.web.widgets.client.optiongroup.OptionGroupOrientation;
import com.vaadin.v7.data.util.IndexedContainer;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

public class WebOptionsGroup<V, I> extends WebAbstractField<CubaOptionGroup, V> implements OptionsGroup<V, I> {

    protected MetadataTools metadataTools;
    protected ApplicationContext applicationContext;

    protected OptionsBinding<I> optionsBinding;

    protected Function<? super I, String> optionCaptionProvider;

    @SuppressWarnings("unchecked")
    public WebOptionsGroup() {
        component = createComponent();
        component.setContainerDataSource(new IndexedContainer());
        component.setItemCaptionGenerator(o -> generateItemCaption((I) o));

        attachListener(component);
    }

    protected CubaOptionGroup createComponent() {
        return new CubaOptionGroup();
    }

    protected String generateDefaultItemCaption(I item) {
        if (valueBinding != null && valueBinding.getSource() instanceof EntityValueSource) {
            EntityValueSource entityValueSource = (EntityValueSource) valueBinding.getSource();
            return metadataTools.format(item, entityValueSource.getMetaPropertyPath().getMetaProperty());
        }

        return metadataTools.format(item);
    }

    protected String generateItemCaption(I item) {
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

    @Override
    public Orientation getOrientation() {
        switch (component.getOrientation()) {
            case HORIZONTAL:
                return Orientation.HORIZONTAL;
            case VERTICAL:
                return Orientation.VERTICAL;
            default:
                throw new RuntimeException("Unsupproted orientation of OptionGroup");
        }
    }

    @Override
    public void setOrientation(Orientation orientation) {
        checkNotNull(orientation, "Orientation must not be null");

        if (orientation == Orientation.HORIZONTAL) {
            component.setOrientation(OptionGroupOrientation.HORIZONTAL);
        } else {
            component.setOrientation(OptionGroupOrientation.VERTICAL);
        }
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
    public void setLookupSelectHandler(Runnable selectHandler) {
        // do nothing
    }

    @Override
    public Collection getLookupSelectedItems() {
        Object value = getValue();
        return (value instanceof Collection)
                ? (Collection) value
                : Collections.singleton(value);
    }

    @Override
    public OptionsSource<I> getOptionsSource() {
        return optionsBinding != null ? optionsBinding.getSource() : null;
    }

    @Override
    public void setOptionsSource(OptionsSource<I> optionsSource) {
        if (this.optionsBinding != null) {
            this.optionsBinding.unbind();
            this.optionsBinding = null;
        }

        if (optionsSource != null) {
            OptionsBinder optionsBinder = applicationContext.getBean(OptionsBinder.NAME, OptionsBinder.class);
            this.optionsBinding = optionsBinder.bind(optionsSource, this, this::setItemsToPresentation);
            this.optionsBinding.activate();
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
    public void setOptionCaptionProvider(Function<? super I, String> optionCaptionProvider) {
        this.optionCaptionProvider = optionCaptionProvider;
    }

    @Override
    public Function<? super I, String> getOptionCaptionProvider() {
        return optionCaptionProvider;
    }

    @Override
    public CaptionMode getCaptionMode() {
        // vaadin8
        return CaptionMode.ITEM;
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
        // vaadin8
    }

    @Override
    public String getCaptionProperty() {
        // vaadin8
        return null;
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        // vaadin8
    }

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }
}
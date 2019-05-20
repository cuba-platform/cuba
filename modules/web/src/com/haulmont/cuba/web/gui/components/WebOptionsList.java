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

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.OptionsList;
import com.haulmont.cuba.gui.components.data.DataAwareComponentsTools;
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.components.data.meta.OptionsBinding;
import com.haulmont.cuba.gui.components.data.options.OptionsBinder;
import com.haulmont.cuba.web.widgets.CubaListSelect;
import com.vaadin.v7.data.util.IndexedContainer;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WebOptionsList<V, I> extends WebAbstractField<CubaListSelect, V>
        implements OptionsList<V, I>, InitializingBean {

    protected MetadataTools metadataTools;

    protected OptionsBinding<I> optionsBinding;

    protected Function<? super I, String> optionCaptionProvider;

    public WebOptionsList() {
        component = createComponent();
    }

    protected CubaListSelect createComponent() {
        return new CubaListSelect();
    }

    @Override
    public void afterPropertiesSet() {
        initComponent(component);
    }

    protected void initComponent(CubaListSelect component) {
        component.setContainerDataSource(new IndexedContainer());
        component.setItemCaptionGenerator(this::generateItemCaption);
        component.setRequiredError(null);

        component.setDoubleClickHandler(this::onDoubleClick);

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

            //noinspection RedundantCast
            Stream<I> selectedItemsStream = collectionValue.stream()
                    .filter(item -> itemIds.isEmpty() || itemIds.contains((I) item));

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
        return internalValue;
    }

    @Override
    public void setValue(V value) {
        setValueToPresentation(convertToPresentation(value));
    }

    @Override
    protected boolean fieldValueEquals(V value, V oldValue) {
        if (!isMultiSelect()) {
            return super.fieldValueEquals(value, oldValue);
        }

        //noinspection unchecked
        return equalCollections((Collection<V>) value, (Collection<V>) oldValue);
    }

    protected boolean equalCollections(Collection<V> a, Collection<V> b) {
        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return CollectionUtils.isEqualCollection(a, b);
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
            OptionsBinder optionsBinder = beanLocator.get(OptionsBinder.NAME, OptionsBinder.class);
            this.optionsBinding = optionsBinder.bind(options, this, this::setItemsToPresentation);
            this.optionsBinding.activate();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void valueBindingConnected(ValueSource<V> valueSource) {
        super.valueBindingConnected(valueSource);

        if (valueSource instanceof EntityValueSource) {
            DataAwareComponentsTools dataAwareComponentsTools = beanLocator.get(DataAwareComponentsTools.class);
            dataAwareComponentsTools.setupOptions(this, (EntityValueSource) valueSource);
        }
    }

    @Override
    protected void setValueToPresentation(Object value) {
        component.setValueToComponent(value);
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

    protected boolean isCollectionValuesChanged(Collection<I> value, Collection<I> oldValue) {
        return value != oldValue;
    }

    @SuppressWarnings("unchecked")
    protected void onDoubleClick(Object item) {
        if (hasSubscriptions(DoubleClickEvent.class)) {
            DoubleClickEvent<I> event = new DoubleClickEvent<>(this, (I) item);
            publish(DoubleClickEvent.class, event);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addDoubleClickListener(Consumer<DoubleClickEvent<I>> listener) {
        return getEventHub().subscribe(DoubleClickEvent.class, (Consumer) listener);
    }
}
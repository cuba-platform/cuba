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
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.TwinColumn;
import com.haulmont.cuba.gui.components.data.ConversionException;
import com.haulmont.cuba.gui.components.data.Options;
import com.haulmont.cuba.gui.components.data.meta.EntityOptions;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.components.data.meta.OptionsBinding;
import com.haulmont.cuba.gui.components.data.options.MapOptions;
import com.haulmont.cuba.gui.components.data.options.OptionsBinder;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.CubaTwinColSelect;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WebTwinColumn<V> extends WebV8AbstractField<CubaTwinColSelect<V>, Set<V>, Collection<V>>
        implements TwinColumn<V>, InitializingBean {

    protected OptionsBinding<V> optionsBinding;

    protected Function<? super V, String> optionCaptionProvider;
    protected OptionStyleProvider<V> optionStyleProvider;

    protected int columns;

    protected MetadataTools metadataTools;

    protected IconResolver iconResolver = AppBeans.get(IconResolver.class);

    protected String captionProperty;
    protected CaptionMode captionMode;

    public WebTwinColumn() {
        component = createComponent();
        attachValueChangeListener(component);
    }

    protected CubaTwinColSelect<V> createComponent() {
        return new CubaTwinColSelect<>();
    }

    @Override
    public void afterPropertiesSet() {
        initComponent(component);
    }

    protected void initComponent(CubaTwinColSelect<V> component) {
        component.setItemCaptionGenerator(this::generateItemCaption);
    }

    @Inject
    protected void setMetadataTools(MetadataTools metadataTools) {
        this.metadataTools = metadataTools;
    }

    @Override
    public void setOptions(Options<V> options) {
        if (this.optionsBinding != null) {
            this.optionsBinding.unbind();
            this.optionsBinding = null;
        }

        if (options != null) {
            OptionsBinder optionsBinder = beanLocator.get(OptionsBinder.NAME);
            this.optionsBinding = optionsBinder.bind(options, this, this::setItemsToPresentation);
            this.optionsBinding.activate();
        }

        if (options instanceof MapOptions) {
            setCaptionMode(CaptionMode.MAP_ENTRY);
        } else {
            setCaptionMode(CaptionMode.ITEM);
        }
    }

    protected void setItemsToPresentation(Stream<V> options) {
        component.setItems(options);

        // set value to Vaadin component as it removes value after setItems
        Collection<V> optionValues = getValue();
        if (CollectionUtils.isNotEmpty(optionValues)) {
            List<V> items = getOptions().getOptions().collect(Collectors.toList());

            Set<V> values = new HashSet<>();
            for (V value : optionValues) {
                if (items.contains(value)) {
                    values.add(value);
                }
            }

            component.setValue(values);
        }
    }

    @Override
    protected Set<V> convertToPresentation(Collection<V> modelValue) throws ConversionException {
        if (modelValue instanceof Set) {
            return (Set<V>) modelValue;
        }

        return modelValue == null ?
                new LinkedHashSet<>() : new LinkedHashSet<>(modelValue);
    }

    @Override
    protected Collection<V> convertToModel(Set<V> componentRawValue) throws ConversionException {
        Stream<V> items = optionsBinding == null ? Stream.empty()
                : optionsBinding.getSource().getOptions().filter(componentRawValue::contains);

        if (valueBinding != null) {
            Class<?> targetType = valueBinding.getSource().getType();

            if (List.class.isAssignableFrom(targetType)) {
                return items.collect(Collectors.toList());
            } else if (Set.class.isAssignableFrom(targetType)) {
                return items.collect(Collectors.toCollection(LinkedHashSet::new));
            }
        }

        return items.collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Options<V> getOptions() {
        return optionsBinding != null ? optionsBinding.getSource() : null;
    }

    @Override
    public void setOptionCaptionProvider(Function<? super V, String> captionProvider) {
        this.optionCaptionProvider = captionProvider;

        component.setItemCaptionGenerator(this::generateItemCaption);
    }

    protected String generateItemCaption(V item) {
        if (item == null) {
            return null;
        }

        if (optionCaptionProvider != null) {
            return optionCaptionProvider.apply(item);
        }

        return generateDefaultItemCaption(item);
    }

    protected String generateDefaultItemCaption(V item) {
        if (valueBinding != null && valueBinding.getSource() instanceof EntityValueSource) {
            EntityValueSource entityValueSource = (EntityValueSource) valueBinding.getSource();
            return metadataTools.format(item, entityValueSource.getMetaPropertyPath().getMetaProperty());
        }

        return metadataTools.format(item);
    }

    @Override
    public Function<? super V, String> getOptionCaptionProvider() {
        return optionCaptionProvider;
    }

    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public void setColumns(int columns) {
        this.columns = columns;
        // see javaDoc Vaadin 7 com.vaadin.ui.TwinColSelect#setColumns(int) for formula
        component.setWidth((columns * 2 + 4) + columns + "em");
    }

    @Override
    public int getRows() {
        return component.getRows();
    }

    @Override
    public void setRows(int rows) {
        component.setRows(rows);
    }

    @Override
    public void setOptionStyleProvider(OptionStyleProvider<V> optionStyleProvider) {
        this.optionStyleProvider = optionStyleProvider;

        if (optionStyleProvider != null) {
            component.setOptionStyleProvider(optionStyleProvider::getStyleName);
        } else {
            component.setOptionStyleProvider(null);
        }
    }

    @Override
    public OptionStyleProvider<V> getOptionStyleProvider() {
        return optionStyleProvider;
    }

    @Override
    public void setAddAllBtnEnabled(boolean enabled) {
        component.setAddAllBtnEnabled(enabled);
    }

    @Override
    public boolean isAddAllBtnEnabled() {
        return component.isAddAllBtnEnabled();
    }

    @Override
    public void setLeftColumnCaption(String leftColumnCaption) {
        component.setLeftColumnCaption(leftColumnCaption);
    }

    @Override
    public String getLeftColumnCaption() {
        return component.getLeftColumnCaption();
    }

    @Override
    public void setRightColumnCaption(String rightColumnCaption) {
        component.setRightColumnCaption(rightColumnCaption);
    }

    @Override
    public String getRightColumnCaption() {
        return component.getRightColumnCaption();
    }

    @Override
    public CaptionMode getCaptionMode() {
        return captionMode;
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
        if (this.captionMode != captionMode) {
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

                    Map<String, V> optionsMap = ((MapOptions<V>) getOptions()).getItemsCollection();
                    BiMap<String, V> biMap = ImmutableBiMap.copyOf(optionsMap);
                    setOptionCaptionProvider(v -> biMap.inverse().get(v));
                    break;
                case ITEM:
                default:
                    // set null to use default behaviour
                    setOptionCaptionProvider(null);
            }
            this.captionMode = captionMode;
        }
    }

    protected String generateOptionPropertyCaption(V item) {
        return ((Entity) item).getValueEx(captionProperty);
    }

    @Override
    public String getCaptionProperty() {
        return captionProperty;
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;

        setCaptionMode(CaptionMode.PROPERTY);
    }
}
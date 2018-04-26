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
import com.haulmont.cuba.gui.components.data.OptionsBinder;
import com.haulmont.cuba.gui.components.data.OptionsBinding;
import com.haulmont.cuba.gui.components.data.OptionsSource;
import com.haulmont.cuba.web.widgets.CubaOptionGroup;
import com.haulmont.cuba.web.widgets.client.optiongroup.OptionGroupOrientation;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

public class WebOptionsGroup<V> extends WebAbstractField<CubaOptionGroup, V> implements OptionsGroup<V> {

    @Inject
    protected MetadataTools metadataTools;
    @Inject
    protected ApplicationContext applicationContext;

    protected OptionsBinding<V> optionsBinding;

    protected Orientation orientation = Orientation.VERTICAL;

    protected Function<? super V, String> optionCaptionProvider;

    public WebOptionsGroup() {
        component = new CubaOptionGroup();

        attachListener(component);
    }

    protected String generateDefaultItemCaption(V item) {
        if (valueBinding != null && valueBinding.getSource() instanceof EntityValueSource) {
            EntityValueSource entityValueSource = (EntityValueSource) valueBinding.getSource();
            return metadataTools.format(item, entityValueSource.getMetaPropertyPath().getMetaProperty());
        }

        return metadataTools.format(item);
    }

    protected String generateItemCaption(V item) {
        if (item == null) {
            return "";
        }

        if (optionCaptionProvider != null) {
            return optionCaptionProvider.apply(item);
        }

        return generateDefaultItemCaption(item);
    }

    @Override
    public boolean isMultiSelect() {
        return component.isMultiSelect();
    }

    @Override
    public void setMultiSelect(boolean multiselect) {
        component.setMultiSelect(true);
    }

    @Override
    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(Orientation orientation) {
        checkNotNull(orientation, "Orientation must not be null");

        if (orientation != this.orientation) {
            if (orientation == Orientation.HORIZONTAL) {
                component.setOrientation(OptionGroupOrientation.HORIZONTAL);
            } else {
                component.setOrientation(OptionGroupOrientation.VERTICAL);
            }
            this.orientation = orientation;
        }
    }

    @Override
    protected V convertToModel(Object componentRawValue) {
        // todo
        return super.convertToModel(componentRawValue);
    }

    @Override
    protected Object convertToPresentation(V modelValue) {
        // todo
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
    public OptionsSource<V> getOptionsSource() {
        return optionsBinding != null ? optionsBinding.getSource() : null;
    }

    @Override
    public void setOptionsSource(OptionsSource<V> optionsSource) {
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

    protected void setItemsToPresentation(Stream<V> options) {
//        todo
//        component.setItems(this::filterItemTest, options.collect(Collectors.toList()));
    }

    @Override
    public void setOptionCaptionProvider(Function<? super V, String> optionCaptionProvider) {
        this.optionCaptionProvider = optionCaptionProvider;
    }

    @Override
    public Function<? super V, String> getOptionCaptionProvider() {
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
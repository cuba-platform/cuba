/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.web.gui.components;


import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.chile.core.datatypes.impl.IntegerDatatype;
import com.haulmont.chile.core.datatypes.impl.LongDatatype;
import com.haulmont.cuba.gui.components.Slider;
import com.haulmont.cuba.gui.components.data.ConversionException;
import com.haulmont.cuba.gui.components.data.DataAwareComponentsTools;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.web.widgets.CubaSlider;
import org.springframework.util.NumberUtils;

import javax.inject.Inject;

public class WebSlider<V extends Number> extends WebV8AbstractField<CubaSlider<V>, V, V> implements Slider<V> {

    protected Datatype<V> datatype;

    protected DataAwareComponentsTools dataAwareComponentsTools;

    public WebSlider() {
        component = createComponent();

        attachValueChangeListener(component);
    }

    protected CubaSlider<V> createComponent() {
        return new CubaSlider<>();
    }

    @Inject
    public void setDataAwareComponentsTools(DataAwareComponentsTools dataAwareComponentsTools) {
        this.dataAwareComponentsTools = dataAwareComponentsTools;
    }

    @Override
    public void setMin(V min) {
        component.setMin(convertToDouble(min));
    }

    @Override
    public V getMin() {
        return convertFromDouble(component.getMin());
    }

    @Override
    public void setMax(V max) {
        component.setMax(convertToDouble(max));
    }

    @Override
    public V getMax() {
        return convertFromDouble(component.getMax());
    }

    @Override
    public void setResolution(int resolution) {
        if (resolution > 0
                && (datatype instanceof IntegerDatatype || datatype instanceof LongDatatype)) {
            throw new IllegalArgumentException(
                    String.format("Slider cannot have resolution for datatype: '%s'", datatype));
        }

        component.setResolution(resolution);
    }

    @Override
    public int getResolution() {
        return component.getResolution();
    }

    @Override
    public void setUpdateValueOnClick(boolean updateValueOnClick) {
        component.setUpdateValueOnClick(updateValueOnClick);
    }

    @Override
    public boolean isUpdateValueOnCLick() {
        return component.isUpdateValueOnClick();
    }

    @Override
    public Orientation getOrientation() {
        return WebWrapperUtils.fromVaadinSliderOrientation(component.getOrientation());
    }

    @Override
    public void setOrientation(Orientation orientation) {
        component.setOrientation(WebWrapperUtils.toVaadinSliderOrientation(orientation));
    }

    @Override
    public Datatype<V> getDatatype() {
        if (datatype == null) {
            datatype = loadDatatype();
        }
        return datatype;
    }

    @Override
    public void setDatatype(Datatype<V> datatype) {
        dataAwareComponentsTools.checkValueSourceDatatypeMismatch(datatype, getValueSource());

        this.datatype = datatype;
    }

    @SuppressWarnings("unchecked")
    protected Datatype<V> loadDatatype() {
        if (valueBinding != null
                && valueBinding.getSource() instanceof EntityValueSource) {
            EntityValueSource entityValueSource = (EntityValueSource) valueBinding.getSource();
            return entityValueSource.getMetaPropertyPath()
                    .getRange()
                    .asDatatype();
        }

        DatatypeRegistry datatypeRegistry = beanLocator.get(DatatypeRegistry.class);
        return (Datatype<V>) datatypeRegistry.get(Double.class);
    }

    @Override
    protected void attachValueChangeListener(CubaSlider<V> component) {
        component.getInternalComponent()
                .addValueChangeListener(event ->
                        componentValueChanged(event.getOldValue(), event.getValue(), event.isUserOriginated()));
    }

    protected void componentValueChanged(Double prevComponentValue, Double newComponentValue, boolean isUserOriginated) {
        V prevValue = convertFromDouble(prevComponentValue);
        V newValue = convertFromDouble(newComponentValue);

        componentValueChanged(prevValue, newValue, isUserOriginated);
    }

    @SuppressWarnings("unchecked")
    protected V convertFromDouble(Double componentValue) throws ConversionException {
        if (componentValue == null) {
            return null;
        }

        Datatype<V> datatype = getDatatype();
        return (V) NumberUtils.convertNumberToTargetClass(componentValue, datatype.getJavaClass());
    }

    protected Double convertToDouble(V value) {
        return value != null
                ? value.doubleValue()
                : null;
    }
}

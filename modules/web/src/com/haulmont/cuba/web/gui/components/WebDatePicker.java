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
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.util.DateTimeUtils;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.DatePicker;
import com.haulmont.cuba.gui.components.data.ConversionException;
import com.haulmont.cuba.gui.components.data.DataAwareComponentsTools;
import com.haulmont.cuba.gui.components.data.EntityValueSource;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.value.DatasourceValueSource;
import com.haulmont.cuba.web.widgets.CubaDatePicker;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.ui.InlineDateField;
import org.springframework.beans.factory.InitializingBean;

import java.time.LocalDate;
import java.util.Date;

public class WebDatePicker<V extends Date> extends WebV8AbstractField<InlineDateField, LocalDate, V>
        implements DatePicker<V>, InitializingBean {

    protected Resolution resolution = Resolution.DAY;

    public WebDatePicker() {
        this.component = new CubaDatePicker();

        attachValueChangeListener(component);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Messages messages = applicationContext.getBean(Messages.class);
        component.setDateOutOfRangeMessage(messages.getMainMessage("datePicker.dateOutOfRangeMessage"));
    }

    @Override
    public Resolution getResolution() {
        return resolution;
    }

    @Override
    public void setResolution(Resolution resolution) {
        Preconditions.checkNotNullArgument(resolution);

        this.resolution = resolution;
        DateResolution vResolution;
        switch (resolution) {
            case MONTH:
                vResolution = com.vaadin.shared.ui.datefield.DateResolution.MONTH;
                break;
            case YEAR:
                vResolution = com.vaadin.shared.ui.datefield.DateResolution.YEAR;
                break;
            case DAY:
            default:
                vResolution = com.vaadin.shared.ui.datefield.DateResolution.DAY;
                break;
        }

        component.setResolution(vResolution);
    }

    // VAADIN8: gg, need to use?
    /*protected boolean checkRange(Date value) {
        if (updatingInstance) {
            return true;
        }

        if (value != null) {
            if (component.getRangeStart() != null && value.before(component.getRangeStart())) {
                handleDateOutOfRange(value);
                return false;
            }

            if (component.getRangeEnd() != null && value.after(component.getRangeEnd())) {
                handleDateOutOfRange(value);
                return false;
            }
        }

        return true;
    }*/

    // VAADIN8: gg, need to use?
    /*protected void handleDateOutOfRange(Date value) {
        if (getFrame() != null) {
            Messages messages = AppBeans.get(Messages.NAME);
            getFrame().showNotification(messages.getMainMessage("datePicker.dateOutOfRangeMessage"),
                    Frame.NotificationType.TRAY);
        }

        updatingInstance = true;
        try {
            component.setValue(internalValue);
        } finally {
            updatingInstance = false;
        }
    }*/

/*  todo
    @Override
    public void setDatasource(Datasource datasource, String property) {
        if ((datasource == null && property != null) || (datasource != null && property == null))
            throw new IllegalArgumentException("Datasource and property should be either null or not null at the same time");

        if (datasource == this.datasource && ((metaPropertyPath != null && metaPropertyPath.toString().equals(property)) ||
                (metaPropertyPath == null && property == null)))
            return;

        if (this.datasource != null) {
            metaProperty = null;
            metaPropertyPath = null;

            component.setPropertyDataSource(null);

            //noinspection unchecked
            this.datasource.removeItemChangeListener(weakItemChangeListener);
            weakItemChangeListener = null;

            //noinspection unchecked
            this.datasource.removeItemPropertyChangeListener(weakItemPropertyChangeListener);
            weakItemPropertyChangeListener = null;

            this.datasource = null;

            if (itemWrapper != null) {
                itemWrapper.unsubscribe();
            }

            disableBeanValidator();
        }

        if (datasource != null) {
            //noinspection unchecked
            this.datasource = datasource;

            MetaClass metaClass = datasource.getMetaClass();
            resolveMetaPropertyPath(metaClass, property);

            component.addValueChangeListener(event -> {
                if (!checkRange(component.getValue())) {
                    return;
                }

                updateInstance();
            });

            itemChangeListener = e -> {
                if (updatingInstance) {
                    return;
                }
                Date value = getEntityValue(e.getItem());
                setValueToFields(value);
                fireValueChanged(value);
            };

            weakItemChangeListener = new WeakItemChangeListener(datasource, itemChangeListener);
            //noinspection unchecked
            datasource.addItemChangeListener(weakItemChangeListener);

            itemPropertyChangeListener = e -> {
                if (updatingInstance) {
                    return;
                }
                if (e.getProperty().equals(metaPropertyPath.toString())) {
                    setValueToFields((Date) e.getValue());
                    fireValueChanged(e.getValue());
                }
            };

            weakItemPropertyChangeListener = new WeakItemPropertyChangeListener(datasource, itemPropertyChangeListener);
            //noinspection unchecked
            datasource.addItemPropertyChangeListener(weakItemPropertyChangeListener);

            if (datasource.getState() == Datasource.State.VALID && datasource.getItem() != null) {
                if (property.equals(metaPropertyPath.toString())) {
                    Date value = getEntityValue(datasource.getItem());
                    setValueToFields(value);
                    fireValueChanged(value);
                }
            }

            initRequired(metaPropertyPath);

            if (metaProperty.isReadOnly()) {
                setEditable(false);
            }

            initBeanValidator();
            setDateRangeByProperty(metaProperty);
        }
    }*/

    @Override
    protected void valueBindingConnected(ValueSource<V> valueSource) {
        super.valueBindingConnected(valueSource);

        if (valueSource instanceof EntityValueSource) {
            DataAwareComponentsTools dataAwareComponentsTools = applicationContext.getBean(DataAwareComponentsTools.class);
            EntityValueSource entityValueSource = (EntityValueSource) valueSource;

            dataAwareComponentsTools.setupDateRange(this, entityValueSource);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected V convertToModel(LocalDate componentRawValue) throws ConversionException {
        if (componentRawValue == null) {
            return null;
        }

        Date datePickerDate = DateTimeUtils.asDate(componentRawValue);

        ValueSource<V> valueSource = getValueSource();
        if (valueSource instanceof EntityValueSource) {
            MetaPropertyPath metaPropertyPath = ((DatasourceValueSource) valueSource).getMetaPropertyPath();
            MetaProperty metaProperty = metaPropertyPath.getMetaProperty();
            if (metaProperty != null) {
                Class javaClass = metaProperty.getRange().asDatatype().getJavaClass();
                if (javaClass.equals(java.sql.Date.class)) {
                    return (V) new java.sql.Date(datePickerDate.getTime());
                }
            }
        }

        return (V) datePickerDate;
    }

    @Override
    protected LocalDate convertToPresentation(Date modelValue) throws ConversionException {
        return DateTimeUtils.asLocalDate(modelValue);
    }

    @Override
    public Date getRangeStart() {
        return DateTimeUtils.asDate(component.getRangeStart());
    }

    @Override
    public void setRangeStart(Date rangeStart) {
        component.setRangeStart(DateTimeUtils.asLocalDate(rangeStart));
    }

    @Override
    public Date getRangeEnd() {
        return DateTimeUtils.asDate(component.getRangeEnd());
    }

    @Override
    public void setRangeEnd(Date rangeEnd) {
        component.setRangeEnd(DateTimeUtils.asLocalDate(rangeEnd));
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
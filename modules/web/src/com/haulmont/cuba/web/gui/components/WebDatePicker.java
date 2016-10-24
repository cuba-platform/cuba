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

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.DatePicker;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaDatePicker;
import com.vaadin.ui.InlineDateField;

import java.util.Collection;
import java.util.Date;

public class WebDatePicker extends WebAbstractField<InlineDateField> implements DatePicker {

    protected Resolution resolution = Resolution.DAY;

    public WebDatePicker() {
        this.component = new CubaDatePicker();
        attachListener(component);
        component.setImmediate(true);
        component.setInvalidCommitted(true);

        Messages messages = AppBeans.get(Messages.NAME);
        component.setDateOutOfRangeMessage(messages.getMainMessage("datePicker.dateOutOfRangeMessage"));
    }

    @Override
    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new ItemWrapper(datasource, datasource.getMetaClass(), propertyPaths) {
            @Override
            protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
                return new PropertyWrapper(item, propertyPath);
            }
        };
    }

    @Override
    public Resolution getResolution() {
        return resolution;
    }

    @Override
    public void setResolution(Resolution resolution) {
        Preconditions.checkNotNullArgument(resolution);

        this.resolution = resolution;
        com.vaadin.shared.ui.datefield.Resolution vResolution;
        switch (resolution) {
            case MONTH:
                vResolution = com.vaadin.shared.ui.datefield.Resolution.MONTH;
                break;
            case YEAR:
                vResolution = com.vaadin.shared.ui.datefield.Resolution.YEAR;
                break;
            case DAY:
                vResolution = com.vaadin.shared.ui.datefield.Resolution.DAY;
                break;
            default:
                vResolution = com.vaadin.shared.ui.datefield.Resolution.DAY;
                break;
        }

        component.setResolution(vResolution);
    }

    @Override
    public Date getRangeStart() {
        return component.getRangeStart();
    }

    @Override
    public void setRangeStart(Date rangeStart) {
        component.setRangeStart(rangeStart);
    }

    @Override
    public Date getRangeEnd() {
        return component.getRangeEnd();
    }

    @Override
    public void setRangeEnd(Date rangeEnd) {
        component.setRangeEnd(rangeEnd);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Date getValue() {
        return super.getValue();
    }
}
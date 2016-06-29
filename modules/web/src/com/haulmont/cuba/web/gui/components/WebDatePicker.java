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
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.DatePicker;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.WeakItemChangeListener;
import com.haulmont.cuba.gui.data.impl.WeakItemPropertyChangeListener;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaDatePicker;
import com.vaadin.ui.InlineDateField;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Date;

public class WebDatePicker extends WebAbstractField<InlineDateField> implements DatePicker {

    protected Resolution resolution = Resolution.DAY;

    protected Datasource.ItemChangeListener itemChangeListener;
    protected Datasource.ItemPropertyChangeListener itemPropertyChangeListener;

    public WebDatePicker() {
        this.component = new CubaDatePicker();
        attachListener(component);
        component.setImmediate(true);
        component.setInvalidCommitted(true);

        Messages messages = AppBeans.get(Messages.NAME);
        component.setDateOutOfRangeMessage(messages.getMessage(WebAbstractTable.class, "datePicker.dateOutOfRangeMessage"));
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

    protected Date getEntityValue(Entity item) {
        return InstanceUtils.getValueEx(item, metaPropertyPath.getPath());
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        MetaClass metaClass = datasource.getMetaClass();
        resolveMetaPropertyPath(metaClass, property);

        itemChangeListener = e -> {
            Date value = getEntityValue(e.getItem());
            this.setValue(value);
        };
        //noinspection unchecked
        datasource.addItemChangeListener(new WeakItemChangeListener(datasource, itemChangeListener));

        itemPropertyChangeListener = e -> {
            if (e.getProperty().equals(metaPropertyPath.toString())) {
                this.setValue(e.getValue());
            }
        };
        //noinspection unchecked
        datasource.addItemPropertyChangeListener(new WeakItemPropertyChangeListener(datasource, itemPropertyChangeListener));

        if (datasource.getState() == Datasource.State.VALID && datasource.getItem() != null) {
            if (property.equals(metaPropertyPath.toString())) {
                Date value = getEntityValue(datasource.getItem());
                this.setValue(value);
            }
        }

        setRequired(metaProperty.isMandatory());
        if (StringUtils.isEmpty(getRequiredMessage())) {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            setRequiredMessage(messageTools.getDefaultRequiredMessage(metaClass, property));
        }

        if (metaProperty.isReadOnly()) {
            setEditable(false);
        }
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
}

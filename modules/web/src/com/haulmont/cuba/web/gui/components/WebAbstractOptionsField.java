/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 06.03.2009 15:12:08
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Enumeration;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.cuba.web.gui.data.EnumerationContainer;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.ObjectContainer;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect;

import java.util.*;

public abstract class WebAbstractOptionsField<T extends com.vaadin.ui.AbstractSelect>
    extends
        WebAbstractField<T>
    implements
        com.haulmont.cuba.gui.components.Field, Component.Wrapper
{
    protected List optionsList;
    protected Map<String, Object> optionsMap;
    protected CollectionDatasource optionsDatasource;

    protected CaptionMode captionMode = CaptionMode.ITEM;
    protected String captionProperty;

    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        final MetaClass metaClass = datasource.getMetaClass();
        this.metaProperty = metaClass.getProperty(property);
        if (metaProperty == null)
            throw new RuntimeException(String.format("Property '%s' not found in class %s", property, metaClass));

        setMultiSelect(metaProperty.getRange().getCardinality().isMany());

        final MetaPropertyPath propertyPath = new MetaPropertyPath(metaProperty.getDomain(), metaProperty);
        final ItemWrapper wrapper = new ItemWrapper(datasource, Collections.singleton(propertyPath));
        final Property itemProperty = wrapper.getItemProperty(propertyPath);

        component.setPropertyDataSource(itemProperty);

        setRequired(metaProperty.isMandatory());

        if (metaProperty.getRange().isEnum()) {
            final Enumeration enumeration = metaProperty.getRange().asEnumeration();
            final Class<Enum> javaClass = enumeration.getJavaClass();

            optionsList = Arrays.asList(javaClass.getEnumConstants());
            component.setContainerDataSource(new EnumerationContainer(optionsList));
            setCaptionMode(CaptionMode.ITEM);
        }
    }

    public void setOptionsMap(Map<String, Object> options) {
        if (metaProperty == null) {
            List opts = new ArrayList();
            for (String key : options.keySet()) {
                Object itemId = options.get(key);
                component.setItemCaption(itemId, key);
                opts.add(itemId);
            }
            component.setContainerDataSource(new ObjectContainer(opts));
            component.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_EXPLICIT_DEFAULTS_ID);
            this.optionsMap = options;
        }
        else {
            if (metaProperty.getRange().isEnum()) {
                List constants = Arrays.asList(metaProperty.getRange().asEnumeration().getJavaClass().getEnumConstants());
                List opts = new ArrayList();

                for (String key : options.keySet()) {
                    Object itemId = options.get(key);
                    component.setItemCaption(itemId, key);
                    if (!constants.contains(itemId)) {
                        throw new UnsupportedOperationException(itemId + " is not of class of meta property" + metaProperty);
                    }
                    opts.add(itemId);
                }
                this.optionsList = opts;
                component.setContainerDataSource(new EnumerationContainer(opts));
                setCaptionMode(CaptionMode.ITEM);
            }
            else {
                throw new UnsupportedOperationException();
            }
        }
    }

    public void setOptionsList(List optionsList) {
        if (metaProperty != null) {
            if (metaProperty.getRange().isEnum()) {
                component.setContainerDataSource(new EnumerationContainer(optionsList));
                setCaptionMode(CaptionMode.ITEM);
            } else {
                throw new UnsupportedOperationException();
            }
            this.optionsList = optionsList;
        } else if (!optionsList.isEmpty()) {
            final Object o = optionsList.iterator().next();
            if (o instanceof Enum) {
                component.setContainerDataSource(new EnumerationContainer(optionsList));
            } else {
                component.setContainerDataSource(new ObjectContainer(optionsList));
            }
            setCaptionMode(CaptionMode.ITEM);
            this.optionsList = optionsList;
        } else {
            component.setContainerDataSource(new ObjectContainer(Collections.EMPTY_LIST));
        }
    }

    public boolean isMultiSelect() {
        return component.isMultiSelect();
    }

    public void setMultiSelect(boolean multiselect) {
        component.setMultiSelect(multiselect);
    }

    public CaptionMode getCaptionMode() {
        return captionMode;
    }

    public void setCaptionMode(CaptionMode captionMode) {
        this.captionMode = captionMode;
        switch (captionMode) {
            case ITEM: {
                component.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_ITEM);
                break;
            }
            case PROPERTY: {
                component.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
                break;
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }

    public String getCaptionProperty() {
        return captionProperty;
    }

    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;
        if (optionsDatasource != null) {
            component.setItemCaptionPropertyId(optionsDatasource.getMetaClass().getProperty(captionProperty));
        }
    }

    public List getOptionsList() {
        return optionsList;
    }

    public Map<String, Object> getOptionsMap() {
        return optionsMap;
    }

    public CollectionDatasource getOptionsDatasource() {
        return optionsDatasource;
    }

    public void setOptionsDatasource(CollectionDatasource datasource) {
        this.optionsDatasource = datasource;
        component.setContainerDataSource(new CollectionDsWrapper(datasource, true));

        if (captionProperty != null) {
            component.setItemCaptionPropertyId(optionsDatasource.getMetaClass().getProperty(captionProperty));
        }
    }

    protected abstract <T> T getValueFromKey(Object key);
    protected abstract Object getKeyFromValue(Object value);
}

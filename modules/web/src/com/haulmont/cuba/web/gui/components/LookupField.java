/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 18:12:13
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.CollectionDatasourceWrapper;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.itmill.toolkit.ui.Select;
import com.itmill.toolkit.ui.AbstractSelect;
import com.itmill.toolkit.data.Property;

public class LookupField
    extends
        AbstractField<Select>
    implements
        com.haulmont.cuba.gui.components.LookupField, Component.Wrapper
{
    private CollectionDatasource lookupDatasource;
    private CaptionMode captionMode = CaptionMode.ITEM;
    private String captionProperty;

    public LookupField() {
        this.component = new Select();
        component.setImmediate(true);
        component.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_ITEM);
    }

    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;
        this.property = property;

        final MetaClass metaClass = datasource.getMetaClass();
        final MetaProperty metaProperty = metaClass.getProperty(property);

        final ItemWrapper wrapper = new ItemWrapper(datasource, metaClass.getProperties());
        final Property itemProperty = wrapper.getItemProperty(metaProperty);

        component.setPropertyDataSource(new Property() {
            public Object getValue() {
                final Object value = itemProperty.getValue();
                return (value instanceof Entity) ? ((Entity) value).getId() : value;
            }

            public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
                if (newValue instanceof Entity) {
                    itemProperty.setValue(newValue);
                } else {
                    if (lookupDatasource != null) {
                        newValue = lookupDatasource.getItem(newValue);
                    }
                }
                itemProperty.setValue(newValue);
            }

            public Class getType() {
                return itemProperty.getType();
            }

            public boolean isReadOnly() {
                return itemProperty.isReadOnly();
            }

            public void setReadOnly(boolean newStatus) {
                itemProperty.setReadOnly(newStatus);
            }
        });

        setRequired(metaProperty.isMandatory());
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
            default :{
                throw new UnsupportedOperationException();
            }
        }
    }

    public String getCaptionProperty() {
        return captionProperty;
    }

    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;
        if (lookupDatasource != null) {
            component.setItemCaptionPropertyId(lookupDatasource.getMetaClass().getProperty(captionProperty));
        }
    }

    public void setLookupDatasource(CollectionDatasource datasource) {
        lookupDatasource = datasource;
        component.setContainerDataSource(new CollectionDatasourceWrapper(datasource, true));

        if (captionProperty != null) {
            component.setItemCaptionPropertyId(lookupDatasource.getMetaClass().getProperty(captionProperty));
        }
    }

    @Override
    public <T> T getValue() {
        if (lookupDatasource != null) {
            final Object key = super.getValue();
            return (T) lookupDatasource.getItem(key);
        } else {
            return super.<T>getValue();
        }
    }

    @Override
    public void setValue(Object value) {
        // TODO (abramov) need to be changed
        super.setValue(((Entity) value).getId());
    }

    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);
        component.setNullSelectionAllowed(!required);
    }
}
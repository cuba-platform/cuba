/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 05.03.2009 16:55:25
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.gui.data.CollectionDatasourceWrapper;
import com.haulmont.cuba.core.entity.Entity;
import com.itmill.toolkit.ui.OptionGroup;
import com.itmill.toolkit.ui.AbstractSelect;

public class OptionsGroup
    extends
        com.haulmont.cuba.web.gui.components.AbstractField<OptionGroup>
    implements
        com.haulmont.cuba.gui.components.OptionsGroup, Component.Wrapper
{
    private CollectionDatasource optionsDatasource;

    private com.haulmont.cuba.gui.components.LookupField.CaptionMode captionMode = LookupField.CaptionMode.ITEM;
    private String captionProperty;

    public OptionsGroup() {
        component = new OptionGroup();
        component.setImmediate(true);
    }

    public CollectionDatasource getOptionsDatasource() {
        return optionsDatasource;
    }

    public void setOptionsDatasource(CollectionDatasource datasource) {
        this.optionsDatasource = datasource;
        component.setContainerDataSource(new CollectionDatasourceWrapper(datasource));
    }

    @Override
    public <T> T getValue() {
        if (optionsDatasource != null) {
            final Object key = super.getValue();
            return (T) optionsDatasource.getItem(key);
        } else {
            return super.<T>getValue();
        }
    }

    @Override
    public void setValue(Object value) {
        // TODO (abramov) need to be changed
        super.setValue(((Entity) value).getId());
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
        if (optionsDatasource != null) {
            component.setItemCaptionPropertyId(optionsDatasource.getMetaClass().getProperty(captionProperty));
        }
    }
}

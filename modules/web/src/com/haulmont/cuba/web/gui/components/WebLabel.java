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

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.compatibility.ComponentValueListenerWrapper;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaLabel;
import com.haulmont.cuba.web.toolkit.ui.converters.StringToDatatypeConverter;
import com.haulmont.cuba.web.toolkit.ui.converters.StringToEntityConverter;
import com.haulmont.cuba.web.toolkit.ui.converters.StringToEnumConverter;
import com.vaadin.shared.ui.label.ContentMode;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class WebLabel extends WebAbstractComponent<com.vaadin.ui.Label> implements Label {

    public static final String CAPTION_STYLE = "c-label-caption-on-left";

    protected List<ValueChangeListener> listeners = null;

    protected Datasource<Entity> datasource;
    protected MetaProperty metaProperty;
    protected MetaPropertyPath metaPropertyPath;

    protected Formatter formatter;

    protected String oldValue = "";

    protected ItemWrapper itemWrapper;

    public WebLabel() {
        component = new CubaLabel();
        component.setSizeUndefined();
        component.addValueChangeListener(event -> {
            String newValue = component.getValue();
            fireValueChanged(oldValue, newValue);
            oldValue = newValue;
        });
    }

    @Override
    public Datasource getDatasource() {
        return datasource;
    }

    @Override
    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        return metaPropertyPath;
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        if ((datasource == null && property != null) || (datasource != null && property == null))
            throw new IllegalArgumentException("Datasource and property should be either null or not null at the same time");

        if (datasource == this.datasource && metaPropertyPath != null && metaPropertyPath.toString().equals(property))
            return;

        if (this.datasource != null)
            unsubscribeDatasource();

        if (datasource != null) {
            // noinspection unchecked
            this.datasource = datasource;
            this.metaPropertyPath = resolveMetaPropertyPath(datasource.getMetaClass(), property);
            this.metaProperty = metaPropertyPath.getMetaProperty();

            switch (metaProperty.getType()) {
                case ASSOCIATION:
                    component.setConverter(new StringToEntityConverter() {
                        @Override
                        public Formatter getFormatter() {
                            return WebLabel.this.formatter;
                        }
                    });
                    break;

                case DATATYPE:
                    component.setConverter(new StringToDatatypeConverter(metaProperty.getRange().asDatatype()) {
                        @Override
                        public Formatter getFormatter() {
                            return WebLabel.this.formatter;
                        }
                    });
                    break;

                case ENUM:
                    //noinspection unchecked
                    component.setConverter(new StringToEnumConverter((Class<Enum>) metaProperty.getJavaType()) {
                        @Override
                        public Formatter getFormatter() {
                            return WebLabel.this.formatter;
                        }
                    });
                    break;

                default:
                    component.setConverter(new StringToDatatypeConverter(Datatypes.getNN(String.class)) {
                        @Override
                        public Formatter getFormatter() {
                            return WebLabel.this.formatter;
                        }
                    });
                    break;
            }

            itemWrapper = createDatasourceWrapper(datasource, Collections.singleton(this.metaPropertyPath));
            component.setPropertyDataSource(itemWrapper.getItemProperty(this.metaPropertyPath));
        }
    }

    protected void unsubscribeDatasource() {
        datasource = null;
        metaProperty = null;
        metaPropertyPath = null;
        component.setPropertyDataSource(null);

        if (itemWrapper != null) {
            itemWrapper.unsubscribe();
            itemWrapper = null;
        }
    }

    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new ItemWrapper(datasource, datasource.getMetaClass(), propertyPaths);
    }

    @Override
    public <T> T getValue() {
        return (T) component.getValue();
    }

    @Override
    public void setValue(Object value) {
        if (metaProperty != null) {
            if (datasource.getItem() != null) {
                InstanceUtils.setValueEx(datasource.getItem(), metaPropertyPath.getPath(), value);
            }
        } else {
            String text = formatValue(value);
            component.setValue(text);
        }
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public void setEditable(boolean editable) {
    }

    @Override
    public void addListener(ValueListener listener) {
        addValueChangeListener(new ComponentValueListenerWrapper(listener));
    }

    @Override
    public void removeListener(ValueListener listener) {
        removeValueChangeListener(new ComponentValueListenerWrapper(listener));
    }

    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        if (listeners == null) {
            listeners = new LinkedList<>();
        }

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    @Override
    public Formatter getFormatter() {
        return formatter;
    }

    @Override
    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    protected void fireValueChanged(Object prevValue, Object value) {
        if (listeners != null) {
            for (ValueChangeListener listener : new ArrayList<>(listeners)) {
                listener.valueChanged(new ValueChangeEvent(this, prevValue, value));
            }
        }
    }

    public String formatValue(Object value) {
        String text;
        if (formatter == null) {
            if (value == null) {
                text = "";
            } else {
                MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);

                if (metaProperty != null) {
                    text = metadataTools.format(value, metaProperty);
                } else {
                    text = metadataTools.format(value);
                }
            }
        } else {
            text = formatter.format(value);
        }
        return text;
    }

    @Override
    public boolean isHtmlEnabled() {
        return component.getContentMode() == ContentMode.HTML;
    }

    @Override
    public void setHtmlEnabled(boolean htmlEnabled) {
        component.setContentMode(htmlEnabled ? ContentMode.HTML : ContentMode.TEXT);
    }

    protected MetaPropertyPath resolveMetaPropertyPath(MetaClass metaClass, String property) {
        MetaPropertyPath metaPropertyPath = AppBeans.get(MetadataTools.NAME, MetadataTools.class)
                .resolveMetaPropertyPath(metaClass, property);
        Preconditions.checkNotNullArgument(metaPropertyPath, "Could not resolve property path '%s' in '%s'", property, metaClass);

        return metaPropertyPath;
    }

    @Override
    public String getCaption() {
        return null;
    }

    @Override
    public void setCaption(String caption) {
        // do nothing
    }

    @Override
    public String getDescription() {
        return component.getDescription();
    }

    @Override
    public void setDescription(String description) {
        component.setDescription(description);
    }

    @Override
    public void setIcon(String icon) {
        super.setIcon(icon);
        if (!StringUtils.isEmpty(icon)) {
            getComposition().addStyleName(CAPTION_STYLE);
        } else {
            getComposition().removeStyleName(CAPTION_STYLE);
        }
    }
}
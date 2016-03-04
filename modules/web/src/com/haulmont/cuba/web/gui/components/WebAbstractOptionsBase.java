/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.ui.converters.ObjectToObjectConverter;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.AbstractSelect;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * @author petunin
 */
public class WebAbstractOptionsBase<T extends com.vaadin.ui.AbstractSelect> extends WebAbstractOptionsField<T> {

    @Override
    public void setOptionsDatasource(CollectionDatasource datasource) {
        super.setOptionsDatasource(datasource);

        assignAutoDebugId();
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        super.setDatasource(datasource, property);

        assignAutoDebugId();
    }

    protected <V> V getValueFromKey(Object key) {
        if (key instanceof Collection) {
            final Set<Object> set = new LinkedHashSet<>();
            for (Object o : (Collection) key) {
                Object t = getValue(o);
                set.add(t);
            }
            return (V) set;
        } else {
            final Object o = getValue(key);
            return wrapAsCollection(o);
        }
    }

    protected Object getValue(Object o) {
        Object t;
        if (o instanceof Enum) {
            t = o;
        } else if (o instanceof Entity) {
            t = o;
        } else if (optionsDatasource != null) {
            if (Datasource.State.INVALID == optionsDatasource.getState()) {
                optionsDatasource.refresh();
            }
            t = optionsDatasource.getItem(o);
        } else {
            t = o;
        }
        return t;
    }

    @Override
    public void setValue(Object value) {
        // TODO (abramov) need to be changed
        super.setValue(getKeyFromValue(value));
    }

    protected Object getKeyFromValue(Object value) {
        Object v;
        if (isMultiSelect()) {
            if (value instanceof Collection) {
                final Set<Object> set = new LinkedHashSet<>();
                for (Object o : (Collection) value) {
                    Object t = getKey(o);
                    set.add(t);
                }
                v = set;
            } else {
                v = getKey(value);
            }
        } else {
            v = getKey(value);
        }

        return v;
    }

    protected Object getKey(Object o) {
        Object t;
        if (o instanceof Entity) {
            if (optionsDatasource != null) {
                if (Datasource.State.INVALID == optionsDatasource.getState()) {
                    optionsDatasource.refresh();
                }
                return ((Entity) o).getId();
            } else {
                if ((optionsList != null) || (optionsMap != null)) {
                    return o;
                }
                return ((Entity) o).getId();
            }
        } /*else if (o instanceof Enum) {
            t = o;
        } */ else {
            t = o;
        }
        return t;
    }

    protected void initDefaults(AbstractSelect component){
        component.setImmediate(true);
        component.setInvalidCommitted(true);
        component.setItemCaptionMode(AbstractSelect.ItemCaptionMode.ITEM);
        component.setConverter(new ObjectToObjectConverter());
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public <T> T getValue() {
        if (optionsDatasource != null) {
            final Object key = super.getValue();
            return getValueFromKey(key);
        } else {
            return wrapAsCollection(super.getValue());
        }
    }

    @Override
    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new ItemWrapper(datasource, datasource.getMetaClass(), propertyPaths) {

            @Override
            protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
                return new CollectionPropertyWrapper(item, propertyPath);
            }
        };
    }

    public static class CollectionPropertyWrapper extends PropertyWrapper {
        public CollectionPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
            super(item, propertyPath);
        }

        @Override
        public void setValue(Object newValue) throws ReadOnlyException, Converter.ConversionException {
            if (newValue instanceof Collection) {
                Class propertyType = propertyPath.getMetaProperty().getJavaType();
                if (Set.class.isAssignableFrom(propertyType)) {
                    newValue = new LinkedHashSet<>((Collection<?>) newValue);
                } else if (List.class.isAssignableFrom(propertyType)) {
                    newValue = new ArrayList<>((Collection<?>) newValue);
                }
            }
            super.setValue(newValue);
        }

        @Override
        public Object getValue() {
            Object value = super.getValue();
            if (value instanceof Collection) {
                Class propertyType = propertyPath.getMetaProperty().getJavaType();
                if (Set.class.isAssignableFrom(propertyType)) {
                    value = new LinkedHashSet<>((Collection<?>) value);
                } else if (List.class.isAssignableFrom(propertyType)) {
                    value = new ArrayList<>((Collection<?>) value);
                }
            }
            return value;
        }

        @Override
        public Class getType() {
            return Object.class;
        }
    }

    @Override
    protected String getAlternativeDebugId() {
        if (id != null) {
            return id;
        }
        if (datasource != null && StringUtils.isNotEmpty(datasource.getId()) && metaPropertyPath != null) {
            return getClass().getSimpleName() + datasource.getId() + "_" + metaPropertyPath.toString();
        }
        if (optionsDatasource != null && StringUtils.isNotEmpty(optionsDatasource.getId())) {
            return getClass().getSimpleName() + optionsDatasource.getId();
        }

        return getClass().getSimpleName();
    }
}
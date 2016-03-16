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

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.TwinColumn;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaTwinColSelect;
import com.haulmont.cuba.web.toolkit.ui.converters.ObjectToObjectConverter;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractSelect;

import java.util.*;

/**
 */
public class WebTwinColumn extends WebAbstractOptionsField<CubaTwinColSelect> implements TwinColumn {

    private StyleProvider styleProvider;

    public WebTwinColumn() {
        component = new CubaTwinColSelect() {
            @Override
            public void setPropertyDataSource(Property newDataSource) {
                super.setPropertyDataSource(new PropertyAdapter(newDataSource) {
                    @Override
                    public Object getValue() {
                        final Object o = itemProperty.getValue();
                        return getKeyFromValue(o);
                    }

                    @Override
                    public void setValue(Object newValue) throws ReadOnlyException, Converter.ConversionException {
                        final Object v = getValueFromKey(newValue);
                        itemProperty.setValue(v);
                    }
                });
            }

            @Override
            public Resource getItemIcon(Object itemId) {
                if (styleProvider != null) {
                    @SuppressWarnings({"unchecked"})
                    final Entity item = optionsDatasource.getItem(itemId);
                    final String resURL = styleProvider.getItemIcon(item, isSelected(itemId));

                    return resURL == null ? null : WebComponentsHelper.getResource(resURL);
                } else {
                    return null;
                }
            }
        };
        attachListener(component);
        component.setImmediate(true);
        component.setItemCaptionMode(AbstractSelect.ItemCaptionMode.ITEM);
        component.setMultiSelect(true);
        component.setInvalidAllowed(false);
        component.setInvalidCommitted(true);
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
            Class propertyType = propertyPath.getMetaProperty().getJavaType();
            if (Set.class.isAssignableFrom(propertyType)) {
                if (newValue == null) {
                    newValue = new HashSet();
                } else {
                    if (newValue instanceof Collection) {
                        newValue = new HashSet<>((Collection<?>) newValue);
                    } else {
                        newValue = Collections.singleton(newValue);
                    }
                }
            } else if (List.class.isAssignableFrom(propertyType)) {
                if (newValue == null) {
                    newValue = new ArrayList();
                } else {
                    if (newValue instanceof Collection) {
                        newValue = new ArrayList<>((Collection<?>) newValue);
                    } else {
                        newValue = Collections.singletonList(newValue);
                    }
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
                    value = new HashSet<>((Collection<?>) value);
                } else if (List.class.isAssignableFrom(propertyType)) {
                    value = new LinkedHashSet<>((Collection<?>) value);
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
    public <T> T getValue() {
        if (optionsDatasource != null) {
            final Object key = super.getValue();
            return getValueFromKey(key);
        } else {
            return wrapAsCollection(super.getValue());
        }
    }

    @Override
    public void setValue(Object value) {
        super.setValue(getKeyFromValue(value));
    }

    @Override
    public int getColumns() {
        return component.getColumns();
    }

    @Override
    public void setColumns(int columns) {
        component.setColumns(columns);
    }

    @Override
    public int getRows() {
        return component.getRows();
    }

    @Override
    public void setRows(int rows) {
        component.setRows(rows);
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        super.setDatasource(datasource, property);
        component.setConverter(new ObjectToObjectConverter());

        assignAutoDebugId();
    }

    @Override
    public void setStyleProvider(final StyleProvider styleProvider) {
        this.styleProvider = styleProvider;
        if (styleProvider != null) {
            component.setStyleGenerator(new CubaTwinColSelect.OptionStyleGenerator() {
                public String generateStyle(AbstractSelect source, Object itemId, boolean selected) {
                    final Entity item = optionsDatasource.getItem(itemId);
                    return styleProvider.getStyleName(item, itemId, component.isSelected(itemId));
                }
            });
        } else {
            component.setStyleGenerator(null);
        }
    }

    @Override
    public void setAddAllBtnEnabled(boolean enabled) {
        component.setAddAllBtnEnabled(enabled);
    }

    @Override
    public boolean isAddAllBtnEnabled() {
        return component.isAddAllBtnEnabled();
    }

    protected <T> T getValueFromKey(Object key) {
        if (key instanceof Collection) {
            final Set<Object> set = new LinkedHashSet<>();
            for (Object o : (Collection) key) {
                Object t = getValue(o);
                set.add(t);
            }
            return (T) set;
        } else {
            final Object o = getValue(key);
            return (T) wrapAsCollection(o);
        }
    }

    protected Object getValue(Object o) {
        Object t;
        if (o instanceof Enum) {
            t = o;
        } else if (o instanceof Entity) {
            t = o;
        } else {
            t = optionsDatasource.getItem(o);
        }
        return t;
    }

    protected Object getKeyFromValue(Object value) {
        if (value == null) {
            return null;
        }
        final Set<Object> set = new HashSet<>();
        if (value instanceof Collection) {
            for (Object o : (Collection) value) {
                Object t = getKey(o);
                set.add(t);
            }

        } else {
            getKey(value);
            set.add(getKey(value));
        }
        return set;
    }

    protected Object getKey(Object o) {
        Object t;
        if (o instanceof Entity) {
            t = ((Entity) o).getId();
        } /*else if (o instanceof Enum) {
            t = o;
        }*/ else {
            t = o;
        }
        return t;
    }
}
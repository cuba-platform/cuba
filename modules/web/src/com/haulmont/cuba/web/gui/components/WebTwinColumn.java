/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Component;
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
 * @author gorodnov
 * @version $Id$
 */
public class WebTwinColumn
        extends
            WebAbstractOptionsField<CubaTwinColSelect>
        implements
            TwinColumn, Component.Wrapper {

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
        return new ItemWrapper(datasource, propertyPaths) {

            @Override
            protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
                return new CollectionPropertyWrapper(item, propertyPath);
            }
        };
    }

    public class CollectionPropertyWrapper extends PropertyWrapper {

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
                        newValue = new HashSet((Collection) newValue);
                    } else {
                        newValue = Collections.singleton(newValue);
                    }
                }
            } else if (List.class.isAssignableFrom(propertyType)) {
                if (newValue == null) {
                    newValue = new ArrayList();
                } else {
                    if (newValue instanceof Collection) {
                        newValue = new ArrayList((Collection) newValue);
                    } else {
                        newValue = Collections.singletonList(newValue);
                    }
                }
            }
            super.setValue(newValue);
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
    }

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

    @Override
    protected <T> T getValueFromKey(Object key) {
        if (key instanceof Collection) {
            final Set<Object> set = new LinkedHashSet<Object>();
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

    @Override
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
        } else if (o instanceof Enum) {
            t = o;
        } else {
            t = o;
        }
        return t;
    }
}
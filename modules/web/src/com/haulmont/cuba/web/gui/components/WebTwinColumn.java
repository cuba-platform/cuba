/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 05.08.2010 17:08:49
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TwinColumn;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.ui.TwinColumnSelect;
import com.vaadin.data.Property;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.AbstractSelect;

import java.util.*;

public class WebTwinColumn
        extends
            WebAbstractOptionsField<TwinColumnSelect>
        implements
            TwinColumn, Component.Wrapper
{

    private Object nullOption;

    private StyleProvider styleProvider;
    
    public WebTwinColumn() {
        component = new TwinColumnSelect() {
            @Override
            public void setPropertyDataSource(Property newDataSource) {
                super.setPropertyDataSource(new PropertyAdapter(newDataSource) {
                    public Object getValue() {
                        final Object o = itemProperty.getValue();
                        return getKeyFromValue(o);
                    }

                    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
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
        component.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_ITEM);
        component.setMultiSelect(true);
        component.setInvalidAllowed(false);
        component.setInvalidCommitted(true);
    }

    @Override
    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new ItemWrapper(datasource, propertyPaths) {
            private static final long serialVersionUID = 5362825971897808953L;

            @Override
            protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
                return new CollectionPropertyWrapper(item, propertyPath);
            }
        };
    }

    public class CollectionPropertyWrapper extends PropertyWrapper {
        private static final long serialVersionUID = -7658086655306380094L;

        public CollectionPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
            super(item, propertyPath);
        }

        @Override
        public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
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
            return (T) getValueFromKey(key);
        } else {
            return (T) wrapAsCollection(super.getValue());
        }
    }

    @Override
    public void setValue(Object value) {
        //todo gorodnov: need to be changed as in WebOptionsGroup
        super.setValue(getKeyFromValue(value));
    }

    public Object getNullOption() {
        return nullOption;
    }

    public void setNullOption(Object nullOption) {
        this.nullOption = nullOption;
        component.setNullSelectionItemId(nullOption);
    }

    public int getColumns() {
        return component.getColumns();
    }

    public void setColumns(int columns) {
        component.setColumns(columns);
    }

    public int getRows() {
        return component.getRows();
    }

    public void setRows(int rows) {
        component.setRows(rows);
    }

    @Override
    public void setDescriptionProperty(String descriptionProperty) {
        super.setDescriptionProperty(descriptionProperty);
        component.setShowOptionsDescriptions(descriptionProperty != null);
        if (optionsDatasource != null) {
            component.setItemDescriptionPropertyId(optionsDatasource.getMetaClass().getProperty(descriptionProperty));
        }
    }

    public void setStyleProvider(final StyleProvider styleProvider) {
        this.styleProvider = styleProvider;
        if (styleProvider != null) {
            component.setStyleGenerator(new TwinColumnSelect.OptionStyleGenerator() {
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
        Object v;
        if (isMultiSelect()) {
            if (value instanceof Collection) {
                final Set<Object> set = new HashSet<Object>();
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
            t = ((Entity) o).getId();
        } else if (o instanceof Enum) {
            t = o;
        } else {
            t = o;
        }
        return t;
    }
}

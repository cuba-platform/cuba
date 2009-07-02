/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 14:34:57
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DatasourceListener;
import com.haulmont.cuba.web.gui.data.CollectionDsWrapper;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.gui.data.SortableCollectionDsWrapper;
import com.itmill.toolkit.data.Item;
import com.itmill.toolkit.data.Property;
import com.itmill.toolkit.data.Validator;
import com.itmill.toolkit.terminal.Resource;
import com.itmill.toolkit.ui.BaseFieldFactory;
import org.dom4j.Element;

import java.util.*;

public class Table
    extends
        AbstractTable<com.itmill.toolkit.ui.Table>
    implements
        com.haulmont.cuba.gui.components.Table, Component.Wrapper
{

    public Table() {
        component = new com.itmill.toolkit.ui.Table() {
            @Override
            public Resource getItemIcon(Object itemId) {
                if (styleProvider != null) {
                    @SuppressWarnings({"unchecked"})
                    final Entity item = datasource.getItem(itemId);
                    final String resURL = styleProvider.getItemIcon(item);

                    return resURL == null ? null : ComponentsHelper.getResource(resURL);
                } else {
                    return null;
                }
            }
        };
        initComponent(component);
    }

    @Override
    protected void initComponent(com.itmill.toolkit.ui.Table component) {
        super.initComponent(component);

        component.setSelectable(true);
        component.setFieldFactory(new BaseFieldFactory() {
            @Override
            public com.itmill.toolkit.ui.Field createField(Class type, com.itmill.toolkit.ui.Component uiContext) {
                return super.createField(type, uiContext);
            }

            @Override
            public com.itmill.toolkit.ui.Field createField(Property property, com.itmill.toolkit.ui.Component uiContext) {
                return super.createField(property, uiContext);
            }

            @Override
            public com.itmill.toolkit.ui.Field createField(Item item, Object propertyId, com.itmill.toolkit.ui.Component uiContext) {
                return super.createField(item, propertyId, uiContext);
            }

            @Override
            public com.itmill.toolkit.ui.Field createField(com.itmill.toolkit.data.Container container, Object itemId, Object propertyId, com.itmill.toolkit.ui.Component uiContext) {
                final com.itmill.toolkit.ui.Field field;
                MetaPropertyPath propertyPath = (MetaPropertyPath) propertyId;
                final Column column = columns.get(propertyPath);
                final Range range = propertyPath.getRange();
                if (range != null) {
                    if (range.isClass()) {
                        final LookupField lookupField = new LookupField();
                        final CollectionDatasource optionsDatasource = getOptionsDatasource(range.asClass(), column);
//                        final Entity item = optionsDatasource.getItem(itemId);

                        lookupField.setOptionsDatasource(optionsDatasource);
//                        lookupField.setDatasource(getDatasource(), metaProperty.getName());

                        field = (com.itmill.toolkit.ui.Field) ComponentsHelper.unwrap(lookupField);
                    } else if (range.isEnum()) {
                        final LookupField lookupField = new LookupField();
                        if (propertyPath.get().length > 1) throw new UnsupportedOperationException();

                        lookupField.setDatasource(getDatasource(), propertyPath.getMetaProperty().getName());
                        lookupField.setOptionsList(range.asEnumiration().getValues());

                        field = (com.itmill.toolkit.ui.Field) ComponentsHelper.unwrap(lookupField);
                    } else {
                        field = super.createField(container, itemId, propertyId, uiContext);
                    }
                } else {
                    field = super.createField(container, itemId, propertyId, uiContext);
                }

                boolean required = requiredColumns.containsKey(column);
                field.setRequired(required);
                if (required)
                    field.setRequiredError(requiredColumns.get(column));

                Set<com.haulmont.cuba.gui.components.Field.Validator> validators = validatorsMap.get(column);
                if (validators != null) {
                    for (final com.haulmont.cuba.gui.components.Field.Validator validator : validators) {

                        if (field instanceof com.itmill.toolkit.ui.AbstractField) {

                            field.addValidator(new Validator() {
                                public void validate(Object value) throws InvalidValueException {
                                    if ((!field.isRequired() && value == null))
                                        return;
                                    try {
                                        validator.validate(value);
                                    } catch (ValidationException e) {
                                        throw new InvalidValueException(e.getMessage());
                                    }
                                }

                                public boolean isValid(Object value) {
                                    return (!field.isRequired() && value == null) || validator.isValid(value);
                                }
                            });
                            ((com.itmill.toolkit.ui.AbstractField) field).setValidationVisible(false);
                        }
                    }
                }
                return field;
            }
        });
        
        component.setColumnCollapsingAllowed(true);
        component.setColumnReorderingAllowed(true);
    }

    @Override
    protected void addGeneratedColumn(Object id, Object generator) {
        component.addGeneratedColumn(id, (com.itmill.toolkit.ui.Table.ColumnGenerator) generator);
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        component.setEditable(editable);
//        component.setRowHeaderMode(
//                editable ?
//                    com.itmill.toolkit.ui.Table.ROW_HEADER_MODE_INDEX :
//                    com.itmill.toolkit.ui.Table.ROW_HEADER_MODE_HIDDEN);
    }

    protected CollectionDsWrapper createContainerDatasource(CollectionDatasource datasource, Collection<MetaPropertyPath> columns) {
        return datasource instanceof CollectionDatasource.Sortable ?
            new SortableTableDsWrapper(datasource, columns) :
            new TableDsWrapper(datasource, columns);
    }

    protected void setVisibleColumns(List<MetaPropertyPath> columnsOrder) {
        component.setVisibleColumns(columnsOrder.toArray());
    }

    protected void setColumnHeader(MetaPropertyPath propertyPath, String caption) {
        component.setColumnHeader(propertyPath, caption);
    }

    public void setRowHeaderMode(com.haulmont.cuba.gui.components.Table.RowHeaderMode rowHeaderMode) {
        switch (rowHeaderMode) {
            case NONE: {
                component.setRowHeaderMode(com.itmill.toolkit.ui.Table.ROW_HEADER_MODE_HIDDEN);
                break;
            }
            case ICON: {
                component.setRowHeaderMode(com.itmill.toolkit.ui.Table.ROW_HEADER_MODE_ICON_ONLY);
                break;
            }
            default: {
                throw new UnsupportedOperationException();
            }
        }
    }

    public void setStyleProvider(final StyleProvider styleProvider) {
        this.styleProvider = styleProvider;
        if (styleProvider == null) { component.setCellStyleGenerator(null); return; }

        component.setCellStyleGenerator(new com.itmill.toolkit.ui.Table.CellStyleGenerator() {
            public String getStyle(Object itemId, Object propertyId) {
                @SuppressWarnings({"unchecked"})
                final Entity item = datasource.getItem(itemId);
                return styleProvider.getStyleName(item, propertyId);
            }
        });
    }

    public void applySettings(Element element) {
        final Element columnsElem = element.element("columns");
        if (columnsElem != null) {
            if (!Datasource.State.VALID.equals(getDatasource().getState())) {
                getDatasource().addListener(new DatasourceListener<Entity>() {
                    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                    }

                    public void stateChanged(Datasource ds, Datasource.State prevState, Datasource.State state) {
                        __applySettings(columnsElem);
                    }

                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                    }
                });
            } else {
                __applySettings(columnsElem);
            }
        }
    }

    private void __applySettings(Element columnsElem) {
        Object[] oldColumns = component.getVisibleColumns();
        List<Object> newColumns = new ArrayList<Object>();
        // add columns from saved settings
        for (Element colElem : Dom4j.elements(columnsElem, "columns")) {
            for (Object column : oldColumns) {
                if (column.toString().equals(colElem.attributeValue("id"))) {
                    newColumns.add(column);

                    String width = colElem.attributeValue("width");
                    if (width != null)
                        component.setColumnWidth(column, Integer.valueOf(width));

                    String visible = colElem.attributeValue("visible");
                    if (visible != null)
                        try {
                            component.setColumnCollapsed(column, !Boolean.valueOf(visible));
                        } catch (IllegalAccessException e) {
                            // ignore
                        }
                    break;
                }
            }
        }
        // add columns not saved in settings (perhaps new)
        for (Object column : oldColumns) {
            if (!newColumns.contains(column)) {
                newColumns.add(column);
            }
        }
        // if the table contains only one column, always show it
        if (newColumns.size() == 1) {
            try {
                component.setColumnCollapsed(newColumns.get(0), false);
            } catch (IllegalAccessException e) {
                //
            }
        }

        component.setVisibleColumns(newColumns.toArray());
    }

    public boolean saveSettings(Element element) {
        Element columnsElem = element.element("columns");
        if (columnsElem != null)
            element.remove(columnsElem);
        columnsElem = element.addElement("columns");

        Object[] visibleColumns = component.getVisibleColumns();
        for (Object column : visibleColumns) {
            Element colElem = columnsElem.addElement("columns");
            colElem.addAttribute("id", column.toString());

            int width = component.getColumnWidth(column);
            if (width > -1)
                colElem.addAttribute("width", String.valueOf(width));

            Boolean visible = !component.isColumnCollapsed(column);
            colElem.addAttribute("visible", visible.toString());
        }
        return true;
    }

    protected class TableDsWrapper extends CollectionDsWrapper {
        public TableDsWrapper(CollectionDatasource datasource) {
            super(datasource);
        }

        public TableDsWrapper(CollectionDatasource datasource, Collection<MetaPropertyPath> properties) {
            super(datasource, properties);
        }

        @Override
        protected void createProperties(View view, MetaClass metaClass) {
            if (columns.isEmpty()) {
                super.createProperties(view, metaClass);
            } else {
                for (Map.Entry<MetaPropertyPath, Column> entry : columns.entrySet()) {
                    final MetaPropertyPath propertyPath = entry.getKey();
                    if (view == null || ViewHelper.contains(view, propertyPath)) {
                        properties.add(propertyPath);
                    }
                }
            }
        }

        @Override
        protected ItemWrapper createItemWrapper(Object item) {
            return new ItemWrapper(item, properties) {
                @Override
                protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
                    final PropertyWrapper wrapper = new TablePropertyWrapper(item, propertyPath);

                    return wrapper;
                }
            };
        }

    }

    protected class SortableTableDsWrapper extends SortableCollectionDsWrapper {
        public SortableTableDsWrapper(CollectionDatasource datasource) {
            super(datasource);
        }

        public SortableTableDsWrapper(CollectionDatasource datasource, Collection<MetaPropertyPath> properties) {
            super(datasource, properties);
        }

        @Override
        protected void createProperties(View view, MetaClass metaClass) {
            if (columns.isEmpty()) {
                super.createProperties(view, metaClass);
            } else {
                for (Map.Entry<MetaPropertyPath, Column> entry : columns.entrySet()) {
                    final MetaPropertyPath propertyPath = entry.getKey();
                    if (view == null || ViewHelper.contains(view, propertyPath)) {
                        properties.add(propertyPath);
                    }
                }
            }
        }

        @Override
        protected ItemWrapper createItemWrapper(Object item) {
            return new ItemWrapper(item, properties) {
                @Override
                protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
                    final PropertyWrapper wrapper = new TablePropertyWrapper(item, propertyPath);

                    return wrapper;
                }
            };
        }
    }

}

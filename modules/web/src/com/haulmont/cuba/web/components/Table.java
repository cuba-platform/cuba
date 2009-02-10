/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 14:34:57
 * $Id$
 */
package com.haulmont.cuba.web.components;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.web.data.CollectionDatasourceWrapper;
import com.haulmont.cuba.web.data.ItemWrapper;
import com.haulmont.cuba.web.data.PropertyWrapper;
import com.itmill.toolkit.data.Property;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.Label;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.*;

public class Table
    extends
        AbstractComponent<com.itmill.toolkit.ui.Table>
    implements
        com.haulmont.cuba.gui.components.Table, Component.Wrapper
{
    protected CollectionDatasource datasource;

    protected Map<MetaProperty, Table.Column> columns = new HashMap<MetaProperty, Column>();

    protected List<Action> actionsOrder = new LinkedList<Action>();
    protected BiMap<Action, com.itmill.toolkit.event.Action> actions = new HashBiMap<Action,com.itmill.toolkit.event.Action>();

    private boolean editable;

    public Table() {
        component = new com.itmill.toolkit.ui.Table();

        component.setSelectable(true);
        component.setMultiSelect(false);
        component.setNullSelectionAllowed(false);
        component.setImmediate(true);

        component.addActionHandler(new ActionsAdapter());
        component.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                final Set<Object> itemIds = getSelecetdItemIds();
                if (itemIds == null || itemIds.isEmpty()) {
                    datasource.setItem(null);
                } else if (itemIds.size() == 1) {
                    final Object id = itemIds.iterator().next();
                    datasource.setItem(datasource.getItem(id));
                } else {
                    datasource.setItem(null);
                }
            }
        });
    }

    public boolean isMultiSelect() {
        return component.isMultiSelect();
    }

    public void setMultiSelect(boolean multiselect) {
        component.setMultiSelect(multiselect);
    }

    public <T> T getSingleSelected() {
        final Set selected = getSelecetdItemIds();
        return selected == null || selected.isEmpty() ?
                null : (T) datasource.getItem(selected.iterator().next());
    }

    public Set getSelected() {
        final Set<Object> itemIds = getSelecetdItemIds();

        if (itemIds != null) {
            final HashSet<Object> res = new HashSet<Object>();
            for (Object id : itemIds) {
                final Object o = datasource.getItem(id);
                res.add(o);
            }
            return res;
        } else {
            return Collections.emptySet();
        }
    }

    public void addAction(final Action action) {
        actions.put(action, new ActionWrapper(action));
        actionsOrder.add(action);
    }

    public void removeAction(Action action) {
        actions.remove(action);
        actionsOrder.remove(action);
    }

    public Collection<Action> getActions() {
        return actions.keySet();
    }

    public Action getAction(String id) {
        for (Action action : getActions()) {
            if (ObjectUtils.equals(action.getId(), id)) {
                return action;
            }
        }
        return null;
    }

    protected Set<Object> getSelecetdItemIds() {
        final Object value = component.getValue();
        if (value == null) {
            return null;
        } else if (value instanceof Collection) {
            return (Set) component.getValue();
        } else {
            return Collections.singleton(value);
        }
    }

    public List<Column> getColumns() {
        // TODO implement column order
        return new ArrayList<Column>(columns.values());
    }

    public void addColumn(Column column) {
        component.addContainerProperty(column.getId(), column.getType(), null);
        columns.put((MetaProperty) column.getId(), column);
    }

    public void removeColumn(Column column) {
        component.removeContainerProperty(column.getId());
        columns.remove((MetaProperty) column.getId());
    }

    public CollectionDatasource getDatasource() {
        return datasource;
    }

    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;
        final CollectionDatasourceWrapper ds = new TableDatasourceWrapper(datasource);

        for (MetaProperty metaProperty : (Collection<MetaProperty>)ds.getContainerPropertyIds()) {
            final Column column = columns.get(metaProperty);
            if (column != null && !column.isEditable()) {
                if (metaProperty.getRange().isClass()) {
                    component.addGeneratedColumn(metaProperty, new ReadOnlyAssociationGenerator(column));
                } else if (metaProperty.getRange().isDatatype()) {
                    if (editable) {
                        component.addGeneratedColumn(metaProperty, new ReadOnlyDatatypeGenerator());
                    }
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        }

        component.setContainerDataSource(ds);

        for (MetaProperty metaProperty : (Collection<MetaProperty>)ds.getContainerPropertyIds()) {
            final Column column = columns.get(metaProperty);

            final String caption;
            if (column != null) {
                caption = StringUtils.capitalize(column.getCaption() != null ? column.getCaption() : metaProperty.getName());
            } else {
                caption = StringUtils.capitalize(metaProperty.getName());
            }

            component.setColumnHeader(metaProperty, caption);
        }

        final Collection<MetaProperty> collection = component.getContainerPropertyIds();
        if (!columns.isEmpty()) {
            for (MetaProperty metaProperty : collection) {
                if (!columns.containsKey(metaProperty)) {
                    component.removeContainerProperty(metaProperty);
                }
            }
        }
    }

    private class ActionsAdapter implements com.itmill.toolkit.event.Action.Handler {
        public com.itmill.toolkit.event.Action[] getActions(Object target, Object sender) {
            final List<com.itmill.toolkit.event.Action> res = new ArrayList();
            for (Action action : actionsOrder) {
//                if (action.isEnabled()) {
                    res.add(actions.get(action));
//                }
            }
            return res.toArray(new com.itmill.toolkit.event.Action[]{});
        }

        public void handleAction(com.itmill.toolkit.event.Action tableAction, Object sender, Object target) {
            final Action action = actions.inverse().get(tableAction);
            if (action != null) {
                action.actionPerform(Table.this);
            }
        }
    }

    private static class ActionWrapper extends com.itmill.toolkit.event.Action {
        private final Action action;

        public ActionWrapper(Action action) {
            super(action.getCaption());
            this.action = action;
        }

        @Override
        public String getCaption() {
            return action.getCaption();
        }
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        component.setEditable(editable);
//        component.setRowHeaderMode(
//                editable ?
//                    com.itmill.toolkit.ui.Table.ROW_HEADER_MODE_INDEX :
//                    com.itmill.toolkit.ui.Table.ROW_HEADER_MODE_HIDDEN);
    }

    private class TableDatasourceWrapper extends CollectionDatasourceWrapper {
        public TableDatasourceWrapper(CollectionDatasource datasource) {
            super(datasource);
        }

        @Override
        protected void createProperties(View view, MetaClass metaClass) {
            if (columns.isEmpty()) {
                super.createProperties(view, metaClass);
            } else {
                for (Map.Entry<MetaProperty, Column> entry : columns.entrySet()) {
                    final MetaProperty metaProperty = entry.getKey();
                    if (view != null && view.getProperty(metaProperty.getName()) != null) {
                        properties.add(metaProperty);
                    }
                }
            }
        }

        @Override
        protected ItemWrapper createItemWrapper(Object item) {
            return new ItemWrapper(item, properties) {
                @Override
                protected PropertyWrapper createPropertyWrapper(Object item, MetaProperty property) {
                    final PropertyWrapper wrapper = new PropertyWrapper(item, property);
                    final Column column = Table.this.columns.get(property);
                    if (column != null) {
                        wrapper.setReadOnly(!column.isEditable());
                    }

                    return wrapper;
                }
            };
        }
    }

    protected class ReadOnlyAssociationGenerator implements com.itmill.toolkit.ui.Table.ColumnGenerator {
        private Column column;

        public ReadOnlyAssociationGenerator(Column column) {
            this.column = column;
        }

        public com.itmill.toolkit.ui.Component generateCell(com.itmill.toolkit.ui.Table source, final Object itemId, Object columnId) {
            Property property = source.getItem(itemId).getItemProperty(columnId);
            final Object value = property.getValue();

            final Button component = new Button();
            component.setData(value);
            component.setCaption(value == null ? "" : value.toString());
            component.setStyleName("link");
            component.addListener(new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    final Element element = column.getXmlDescriptor();

                    final String onClickAction = element.attributeValue("onClick");
                    if (!StringUtils.isEmpty(onClickAction)) {
                        final com.haulmont.cuba.gui.components.Window window = Table.this.getWindow();
                        window.openEditor(onClickAction, value, WindowManager.OpenType.THIS_TAB);
                    }
                }
            });

            return component;
        }
    }

    private static class ReadOnlyDatatypeGenerator implements com.itmill.toolkit.ui.Table.ColumnGenerator {
        public com.itmill.toolkit.ui.Component generateCell(com.itmill.toolkit.ui.Table source, Object itemId, Object columnId) {
            Property property = source.getItem(itemId).getItemProperty(columnId);
            final Object value = property.getValue();

            final Label label = new Label(value == null ? null : property.toString());
            label.setImmediate(true);

            return label;
        }
    }
}

/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 14:34:57
 * $Id$
 */
package com.haulmont.cuba.web.components;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.data.CollectionDatasourceWrapper;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.itmill.toolkit.data.Property;
import com.itmill.toolkit.event.ItemClickEvent;
import com.itmill.toolkit.ui.*;
import com.itmill.toolkit.ui.Button;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class Table
    extends
        AbstractComponent<com.itmill.toolkit.ui.Table>
    implements
        com.haulmont.cuba.gui.components.Table, Component.Wrapper
{
    protected CollectionDatasource datasource;

    protected List<Action> actionsOrder = new LinkedList<Action>();
    protected BiMap<Action, com.itmill.toolkit.event.Action> actions =
            new HashBiMap<Action,com.itmill.toolkit.event.Action>();

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
        final List<Column> res = new ArrayList<Column>();

        final Collection columns = component.getContainerPropertyIds();
        for (Object columnId : columns) {
            final Column column = new Column(columnId);
            column.setType(component.getType(columnId));

            res.add(column);
        }

        return res;
    }

    public void addColumn(Column column) {
        component.addContainerProperty(column.getId(), column.getType(), null);
    }

    public void removeColumn(Column column) {
        component.removeContainerProperty(column.getId());
    }

    public CollectionDatasource getDatasource() {
        return datasource;
    }

    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;
        final CollectionDatasourceWrapper ds =
                new CollectionDatasourceWrapper(datasource);

        for (MetaProperty metaProperty : (Collection<MetaProperty>)ds.getContainerPropertyIds()) {
            if (metaProperty.getRange().isClass()) {
                component.addGeneratedColumn(metaProperty, new com.itmill.toolkit.ui.Table.ColumnGenerator() {
                    public com.itmill.toolkit.ui.Component generateCell(com.itmill.toolkit.ui.Table source, Object itemId, Object columnId) {
                        Property property = source.getItem(itemId).getItemProperty(columnId);
                        final Object value = property.getValue();

                        final com.itmill.toolkit.ui.Button component = new com.itmill.toolkit.ui.Button();
                        component.setData(value);
                        component.setCaption(value == null ? "" : value.toString());
                        component.setStyleName("link");
                        component.addListener(new com.itmill.toolkit.ui.Button.ClickListener() {
                            public void buttonClick(Button.ClickEvent event) {
                                //To change body of implemented methods use File | Settings | File Templates.
                            }
                        });

                        return component;
                    }
                });
            }
        }

        component.setContainerDataSource(ds);

        for (MetaProperty metaProperty : (Collection<MetaProperty>)ds.getContainerPropertyIds()) {
            component.setColumnHeader(metaProperty, StringUtils.capitalize(metaProperty.getName()));
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
}

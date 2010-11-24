/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 16.02.2009 17:27:55
 * $Id$
 */
package com.haulmont.cuba.web.gui;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewProperty;
import com.haulmont.cuba.gui.FrameContext;
import com.haulmont.cuba.core.global.MetadataHelper;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.TableActionsHelper;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.gui.data.impl.DsContextImpl;
import com.haulmont.cuba.gui.data.impl.GenericDataService;
import com.haulmont.cuba.web.gui.components.WebButton;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.WebTable;
import com.haulmont.cuba.web.toolkit.ui.VerticalActionsLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Map;

public class GenericBrowserWindow extends WebWindow
{
    protected WebTable table;
    protected Layout actionsToolbar;

    @Override
    protected Component createLayout() {
        final VerticalLayout layout = new VerticalActionsLayout();

        table = createTable();
        actionsToolbar = createActionsToolbar(table);

        final Component component = WebComponentsHelper.getComposition(table);
        component.setSizeFull();

        layout.addComponent(actionsToolbar);
        layout.addComponent(component);
        layout.setExpandRatio(component, 1);

        layout.setMargin(true);
        layout.setSpacing(true);

        return layout;
    }

    protected WebTable createTable() {
        final WebTable table = new WebTable();
        table.setMultiSelect(true);

        final TableActionsHelper helper = new TableActionsHelper(this, table);

        helper.createCreateAction();
        helper.createEditAction();
        helper.createRemoveAction();
        helper.createRefreshAction();

        return table;
    }

    protected Layout createActionsToolbar(WebTable table) {
        final HorizontalLayout layout = new HorizontalLayout();

        final Collection<Action> actions = table.getActions();
        for (Action action : actions) {
            final WebButton button = new WebButton();
            button.setAction(action);

            layout.addComponent(WebComponentsHelper.getComposition(button));
        }

        return layout;
    }

    protected void init(Map<String,Object> params) {
        MetaClass metaClass = getMetaClass(params);
        View view = getView(params);

        if (metaClass == null) throw new UnsupportedOperationException();
        initCaption(metaClass);

        final DsContextImpl dsContext = createDsContext(metaClass, view, params);
        setDsContext(dsContext);
        setContext(dsContext.getWindowContext());

        final CollectionDatasource ds = dsContext.get(metaClass.getName());
        ds.refresh();

        if (view != null) {
            for (ViewProperty viewProperty : view.getProperties()) {
                final String name = viewProperty.getName();
                final MetaProperty metaProperty = metaClass.getProperty(name);

                final com.haulmont.cuba.gui.components.Table.Column column =
                        new com.haulmont.cuba.gui.components.Table.Column(new MetaPropertyPath(ds.getMetaClass(), metaProperty));
                column.setType(MetadataHelper.getTypeClass(metaProperty));

                table.addColumn(column);
            }
        } else {
            for (MetaProperty metaProperty : metaClass.getProperties()) {
                if (MetadataHelper.isSystem(metaProperty)) continue;

                final Range range = metaProperty.getRange();
                if (range == null) continue;

                final Range.Cardinality cardinality = range.getCardinality();
                if (Range.Cardinality.ONE_TO_ONE.equals(cardinality) ||
                        Range.Cardinality.MANY_TO_ONE.equals(cardinality))
                {
                    final com.haulmont.cuba.gui.components.Table.Column column =
                            new com.haulmont.cuba.gui.components.Table.Column(new MetaPropertyPath(ds.getMetaClass(), metaProperty));
                    column.setType(MetadataHelper.getTypeClass(metaProperty));

                    table.addColumn(column);
                }
            }
        }
        
        table.setDatasource(ds);
        initEditable(params);
    }

    protected void initEditable(Map<String, Object> params) {
        String editable = (String) params.get("param$editable");
        if (!StringUtils.isEmpty(editable)) {
            table.setEditable(BooleanUtils.toBoolean(editable));
        }
    }

    protected void initCaption(MetaClass metaClass) {
        setCaption("Browse " + metaClass.getName());
    }

    protected View getView(Map<String, Object> params) {
        final MetaClass metaClass = getMetaClass(params);

        final Object o = params.get("param$view");
        if (o == null) return null;

        if (o instanceof View) {
            return (View) o;
        } else if (o instanceof String) {
            return MetadataProvider.getViewRepository().getView(metaClass, (String)o);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    protected MetaClass getMetaClass(Map<String, Object> params) {
        final Object o = params.get("param$metaClass");
        if (o == null) return null;

        if (o instanceof MetaClass) {
            return (MetaClass) o;
        } else if (o instanceof Class) {
            return MetadataProvider.getSession().getClass((Class<?>) o);
        } else if (o instanceof String) {
            return MetadataProvider.getSession().getClass((String) o);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    protected DsContextImpl createDsContext(MetaClass metaClass, View view, Map<String, Object> params) {
        final GenericDataService dataservice = new GenericDataService();
        final DsContextImpl context = new DsContextImpl(dataservice);

        context.setWindowContext(new FrameContext(this, params));

        final CollectionDatasource ds = createDatasource(context, metaClass, view);
        context.register(ds);

        return context;
    }

    protected CollectionDatasourceImpl createDatasource(DsContext context, MetaClass metaClass, View view) {
        DataService dataservice = context.getDataService();
        return new CollectionDatasourceImpl(context, dataservice, metaClass.getName(), metaClass, view == null ? null : view.getName());
    }
}

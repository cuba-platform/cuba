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
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.*;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.gui.data.impl.DsContextImpl;
import com.haulmont.cuba.gui.data.impl.GenericDataService;
import com.haulmont.cuba.gui.FrameContext;
import com.haulmont.cuba.gui.components.TableActionsHelper;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.web.gui.components.ComponentsHelper;
import com.haulmont.cuba.web.gui.components.Table;
import com.haulmont.cuba.web.gui.components.Button;
import com.itmill.toolkit.ui.*;

import java.util.Map;
import java.util.Collection;

public class GenericBrowserWindow extends Window
{
    protected Table table;
    protected Layout actionsToolbar;

    @Override
    protected Component createLayout() {
        final VerticalLayout layout = new VerticalLayout();

        table = createTable();
        actionsToolbar = createActionsToolbar(table);

        final Component component = ComponentsHelper.unwrap(table);
        component.setSizeFull();

        layout.addComponent(actionsToolbar);
        layout.addComponent(component);
        layout.setExpandRatio(component, 1);

        layout.setMargin(true);
        layout.setSpacing(true);

        return layout;
    }

    protected Table createTable() {
        final Table table = new Table();

        final TableActionsHelper helper = new TableActionsHelper(this, table);

        helper.createCreateAction();
        helper.createEditAction();
        helper.createRemoveAction();
        helper.createRefreshAction();

        return table;
    }

    protected Layout createActionsToolbar(Table table) {
        final HorizontalLayout layout = new HorizontalLayout();

        final Collection<Action> actions = table.getActions();
        for (Action action : actions) {
            final Button button = new Button();
            button.setAction(action);

            layout.addComponent(ComponentsHelper.unwrap(button));
        }

        return layout;
    }

    protected void init(Map<String,Object> params) {
        MetaClass metaClass = getMetaClass(params);
        View view = getView(params);

        if (metaClass == null) throw new UnsupportedOperationException();
        initCaption(metaClass);

        final DsContextImpl context = createDsContext(metaClass, view);
        setDsContext(context);

        final CollectionDatasource ds = context.get(metaClass.getName());
        ds.refresh();

        table.setDatasource(ds);
    }

    protected void initCaption(MetaClass metaClass) {
        setCaption("Browse " + metaClass.getName());
    }

    protected View getView(Map<String, Object> params) {
        final MetaClass metaClass = getMetaClass(params);

        final Object o = params.get("parameter$view");
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
        final Object o = params.get("parameter$metaClass");
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

    protected DsContextImpl createDsContext(MetaClass metaClass, View view) {
        final GenericDataService dataservice = new GenericDataService(false);
        final DsContextImpl context = new DsContextImpl(dataservice);

        context.setContext(new FrameContext(this));

        final CollectionDatasource ds = createDatasource(context, metaClass, view);
        context.register(ds);

        return context;
    }

    protected CollectionDatasourceImpl createDatasource(DsContext context, MetaClass metaClass, View view) {
        DataService dataservice = context.getDataService();
        return new CollectionDatasourceImpl(context, dataservice, metaClass.getName(), metaClass, view == null ? null : view.getName());
    }
}

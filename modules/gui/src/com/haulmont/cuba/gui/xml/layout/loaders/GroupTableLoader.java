/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 25.11.2009 13:37:46
 *
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.dom4j.Element;

import java.util.List;
import java.util.ArrayList;

public class GroupTableLoader extends AbstractTableLoader<GroupTable> {
    public GroupTableLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    protected GroupTable createComponent(
            ComponentsFactory factory
    ) throws InstantiationException, IllegalAccessException {
        return factory.createComponent("groupTable");
    }

    @Override
    protected List<Table.Column> loadColumns(
            final Table component,
            Element columnsElement,
            CollectionDatasource ds
    ) {
        final List<Table.Column> columns = new ArrayList<Table.Column>();

        final Element groupElement = columnsElement.element("group");
        if (groupElement != null) {
            columns.addAll(super.loadColumns(component, groupElement, ds));
            final List<Object> groupProperties = new ArrayList<Object>(columns.size());
            for (Table.Column column : columns) {
                groupProperties.add(column.getId());
            }
            context.addLazyTask(new PostInitTask() {
                public void execute(Context context, IFrame window) {
                    ((GroupTable) component).groupBy(groupProperties.toArray());
                }
            });
        }

        columns.addAll(super.loadColumns(component, columnsElement, ds));
        return columns;
    }
}

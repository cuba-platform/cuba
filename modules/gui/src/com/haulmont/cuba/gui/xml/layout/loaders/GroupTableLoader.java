/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.List;
import java.util.ArrayList;

/**
 * @author gorodnov
 * @version $Id$
 */
public class GroupTableLoader extends TableLoader {

    public GroupTableLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    protected void initComponent(Table table, Element element, Component parent) {
        super.initComponent(table, element, parent);

        GroupTable component = (GroupTable) table;

        String fixedGroupingString = element.attributeValue("fixedGrouping");
        if (StringUtils.isNotEmpty(fixedGroupingString)) {
            component.setFixedGrouping(Boolean.valueOf(fixedGroupingString));
        }
    }

    @Override
    protected List<Table.Column> loadColumns(final Table component, Element columnsElement, CollectionDatasource ds) {
        final List<Table.Column> columns = new ArrayList<>();

        final Element groupElement = columnsElement.element("group");
        if (groupElement != null) {
            columns.addAll(super.loadColumns(component, groupElement, ds));
            final List<Object> groupProperties = new ArrayList<>(columns.size());
            for (Table.Column column : columns) {
                if (column.isGroupAllowed()) {
                    groupProperties.add(column.getId());
                }
            }
            context.addPostInitTask(new PostInitTask() {
                public void execute(Context context, Frame window) {
                    ((GroupTable) component).groupBy(groupProperties.toArray());
                }
            });
        }

        columns.addAll(super.loadColumns(component, columnsElement, ds));

        if (!columns.isEmpty()) {
            final List<Object> groupDisallowedProperties = new ArrayList<>(columns.size());
            for (Table.Column column : columns) {
                if (!column.isGroupAllowed()) {
                    groupDisallowedProperties.add(column.getId());
                }
            }
            context.addPostInitTask(new PostInitTask() {
                public void execute(Context context, Frame window) {
                    ((GroupTable) component).disableGroupBy(groupDisallowedProperties);
                }
            });
        }

        return columns;
    }
}
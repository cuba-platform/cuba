/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 13:24:14
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.MetadataHelper;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class TableLoader extends ComponentLoader {
    protected ComponentsFactory factory;
    protected DsContext dsContext;
    protected LayoutLoaderConfig config;

    public TableLoader(LayoutLoaderConfig config, ComponentsFactory factory, DsContext dsContext) {
        this.config = config;
        this.factory = factory;
        this.dsContext = dsContext;
    }

    public Component loadComponent(
            ComponentsFactory factory,
            Element element
    ) throws InstantiationException, IllegalAccessException
    {
        final Table table = factory.createComponent("table");

        loadId(table, element);

        final Element columnsElement = element.element("columns");
        final Element rowsElement = element.element("rows");

        final String datasource = rowsElement.attributeValue("datasource");

        if (!StringUtils.isBlank(datasource)) {
            final CollectionDatasource ds = dsContext.get(datasource);
            table.setDatasource(ds);

            if (columnsElement != null) {
                Set<Table.Column> availableColumns = new HashSet<Table.Column>();
                for (Element columnElement : (Collection<Element>)columnsElement.elements("column")) {
                    availableColumns.add(loadColumn(columnElement, ds));
                }

                final List<Table.Column> columns = table.getColumns();
                for (Table.Column column : columns) {
                    if (!availableColumns.contains(column)) {
                        table.removeColumn(column);
                    }
                }
            }
        } else {
            throw new UnsupportedOperationException();
        }

        return table;
    }

    private Table.Column loadColumn(Element columnElement, Datasource ds) {
        final String id = columnElement.attributeValue("id");

        final MetaClass metaClass = ds.getMetaClass();
        final MetaProperty metaProperty = metaClass.getProperty(id);
        final Table.Column column = new Table.Column(metaProperty);

        column.setType(MetadataHelper.getPropertyTypeClass(metaProperty));

        return column;
    }
}

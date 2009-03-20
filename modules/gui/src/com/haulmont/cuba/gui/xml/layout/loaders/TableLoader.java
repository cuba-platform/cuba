/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 13:24:14
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.gui.MetadataHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TableLoader extends ComponentLoader {
    protected ComponentsFactory factory;
    protected LayoutLoaderConfig config;

    public TableLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context);
        this.config = config;
        this.factory = factory;
    }

    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException
    {
        final Table component = factory.createComponent("table");

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadEditable(component, element);

        final Element columnsElement = element.element("columns");
        final Element rowsElement = element.element("rows");

        final String datasource = rowsElement.attributeValue("datasource");

        if (!StringUtils.isBlank(datasource)) {
            final CollectionDatasource ds = context.getDSContext().get(datasource);
            Set<Table.Column> availableColumns = new HashSet<Table.Column>();

            if (columnsElement != null) {
                for (Element columnElement : (Collection<Element>)columnsElement.elements("column")) {
                    availableColumns.add(loadColumn(columnElement, ds));
                }
            }

            for (Table.Column column : availableColumns) {
                component.addColumn(column);
            }

            component.setDatasource(ds);
        } else {
            throw new UnsupportedOperationException();
        }

        final String multiselect = element.attributeValue("multiselect");
        component.setMultiSelect(BooleanUtils.toBoolean(multiselect));

        addAssignWindowTask(component);

        return component;
    }

    private Table.Column loadColumn(Element element, Datasource ds) {
        final String id = element.attributeValue("id");

        final MetaClass metaClass = ds.getMetaClass();
        final MetaProperty metaProperty = metaClass.getPropertyEx(id);
        if (metaProperty == null)
            throw new IllegalStateException(String.format("Property '%s' not found in entity '%s'", id, metaClass.getName()));
        
        final Table.Column column = new Table.Column(metaProperty);

        final String editable = element.attributeValue("editable");
        if (!StringUtils.isEmpty(editable)) {
            column.setEditable(BooleanUtils.toBoolean(editable));
        }

        loadCaption(column, element);

        column.setXmlDescriptor(element);
        column.setType(MetadataHelper.getPropertyTypeClass(metaProperty));

        return column;
    }
}

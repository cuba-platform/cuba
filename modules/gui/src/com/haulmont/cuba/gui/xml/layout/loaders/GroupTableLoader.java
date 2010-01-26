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

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.dom4j.Element;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GroupTableLoader extends AbstractTableLoader<GroupTable> {
    public GroupTableLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    protected GroupTable createComponent(ComponentsFactory factory) throws InstantiationException, IllegalAccessException {
        return factory.createComponent("groupTable");
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent)
            throws InstantiationException, IllegalAccessException {

        final GroupTable component = (GroupTable) super.loadComponent(factory, element, parent);

        final Element groupElement = element.element("group");
        if (groupElement != null) {
            CollectionDatasource ds = component.getDatasource();
            if (ds == null) {
                throw new IllegalStateException("Table must have a datasource before groups initialization");
            }
            loadGroups(component, groupElement, ds);
        }

        return component;
    }

    private void loadGroups(final GroupTable component, Element element, CollectionDatasource ds) throws InstantiationException {
        final List<Element> propertyElements = element.elements("property");
        if (propertyElements.isEmpty()) {
            throw new InstantiationException("<group> element must contain at least one <property> element");
        }

        final Set<MetaPropertyPath> properties = new LinkedHashSet<MetaPropertyPath>();
        final MetaClass metaClass = ds.getMetaClass();

        for (final Element propertyElement : propertyElements) {
            final String propetyId = propertyElement.attributeValue("id");
            properties.add(metaClass.getPropertyEx(propetyId));
        }

        context.addLazyTask(new LazyTask() {
            public void execute(Context context, IFrame frame) {
                component.groupBy(properties.toArray());
            }
        });
    }
}

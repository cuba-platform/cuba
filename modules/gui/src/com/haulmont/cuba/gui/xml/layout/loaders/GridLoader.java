/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 03.02.2009 12:59:26
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.GridLayout;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.dom4j.Element;

import java.util.Collection;
import java.util.List;

public class GridLoader extends ContainerLoader implements com.haulmont.cuba.gui.xml.layout.ComponentLoader {
    public GridLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        GridLayout component = factory.createComponent("grid");

        loadId(component, element);

        final Element columnsElement = element.element("columns");
        final Element rowsElement = element.element("rows");

        final List<Element> columnElements = columnsElement.elements("column");
        component.setColumns(columnElements.size());

        final List<Element> rowElements = rowsElement.elements("row");
        component.setRows(rowElements.size());

        int row = 0;
        for (Element rowElement : rowElements) {
            loadSubComponents(component, rowElement, row);
            row++;
        }

        return component;
    }

    protected void loadSubComponents(GridLayout component, Element element, int row) {
        final LayoutLoader loader = new LayoutLoader(context, factory, config);
        int col = 0;
        for (Element subElement : (Collection<Element>)element.elements()) {
            final Component subComponent = loader.loadComponent(subElement);
            component.add(subComponent, col, row);
            col++;
        }
    }

}

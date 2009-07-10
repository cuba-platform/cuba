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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.BooleanUtils;
import org.dom4j.Element;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class GridLayoutLoader extends ContainerLoader implements com.haulmont.cuba.gui.xml.layout.ComponentLoader {
    protected boolean[][] spanMatrix;

    public GridLayoutLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        GridLayout component = factory.createComponent("grid");

        loadId(component, element);
        loadVisible(component, element);

        loadStyleName(component, element);

        final Element columnsElement = element.element("columns");
        final Element rowsElement = element.element("rows");

        final List<Element> columnElements = columnsElement.elements("column");
        component.setColumns(columnElements.size());
        int i = 0;
        for (Element columnElement : columnElements) {
            final String flex = columnElement.attributeValue("flex");
            if (!StringUtils.isEmpty(flex)) {
                component.setColumnExpandRatio(i, Float.parseFloat(flex));
            }
            i++;
        }

        final List<Element> rowElements = rowsElement.elements("row");
        final Set<Element> invisibleRows = new HashSet<Element>();

        int rowCount = 0;
        for (Element rowElement : rowElements) {
            String visible = rowElement.attributeValue("visible");
            if (visible == null) {
                final Element e = rowElement.element("visible");
                if (e != null) {
                    visible = e.getText();
                }
            }

            if (!StringUtils.isEmpty(visible)) {
                Boolean value = evaluateBoolean(visible);

                if (BooleanUtils.toBoolean(value)) {
                    rowCount++;
                } else {
                    invisibleRows.add(rowElement);
                }
            } else {
                rowCount++;
            }
        }

        component.setRows(rowCount);

        int j = 0;
        for (Element rowElement : rowElements) {
            final String flex = rowElement.attributeValue("flex");
            if (!StringUtils.isEmpty(flex)) {
                component.setRowExpandRatio(j, Float.parseFloat(flex));
            }
            j++;
        }

        spanMatrix = new boolean[columnElements.size()][rowElements.size()];

        int row = 0;
        for (Element rowElement : rowElements) {
            if (!invisibleRows.contains(rowElement)) {
                loadSubComponents(component, rowElement, row);
                row++;
            }
        }

        loadSpacing(component, element);
        loadMargin(component, element);

        loadWidth(component, element);
        loadHeight(component, element);

        return component;
    }

    protected void loadSubComponents(GridLayout component, Element element, int row) {
        final LayoutLoader loader = new LayoutLoader(context, factory, config);
        loader.setLocale(getLocale());
        loader.setMessagesPack(getMessagesPack());

        int col = 0;

        for (Element subElement : (Collection<Element>)element.elements()) {
            final Component subComponent = loader.loadComponent(subElement, component);

            String colspan = subElement.attributeValue("colspan");
            String rowspan = subElement.attributeValue("rowspan");

            while (spanMatrix[col][row]) {
                col++;
            }

            if (StringUtils.isEmpty(colspan) && StringUtils.isEmpty(rowspan)) {
                component.add(subComponent, col, row);
            } else {
                int cspan = StringUtils.isEmpty(colspan) ? 1 : Integer.parseInt(colspan);
                int rspan = StringUtils.isEmpty(rowspan) ? 1 : Integer.parseInt(rowspan);

                fillSpanMatrix(col, row, cspan, rspan);
                component.add(subComponent, col, row, col + cspan, row + rspan);
            }

            col++;
        }
    }

    private void fillSpanMatrix(int col, int row, int cspan, int rspan) {
        for (int i = col; i < (col + cspan); i++) {
            for (int j = col; j < (row + rspan); j++) {
                if (spanMatrix[i][j]) throw new IllegalStateException();
                spanMatrix[i][j] = true;
            }
        }
    }

}

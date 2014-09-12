/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.GridLayout;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.QuasiComponent;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
public class GridLayoutLoader extends ContainerLoader implements com.haulmont.cuba.gui.xml.layout.ComponentLoader {
    protected boolean[][] spanMatrix;

    public GridLayoutLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        GridLayout component = factory.createComponent("grid");

        loadId(component, element);
        loadEnable(component, element);
        loadVisible(component, element);

        loadStyleName(component, element);

        final Element columnsElement = element.element("columns");
        final Element rowsElement = element.element("rows");

        int columnCount;
        @SuppressWarnings("unchecked")
        final List<Element> columnElements = columnsElement.elements("column");
        if (columnElements.size() == 0) {
            try {
                columnCount = Integer.parseInt(columnsElement.attributeValue("count"));
            } catch (NumberFormatException e) {
                throw new GuiDevelopmentException("'grid' element must contain either a set of 'column' elements or a 'count' attribute",
                        context.getFullFrameId(), "Grid ID", component.getId());
            }
            component.setColumns(columnCount);
            for (int i = 0; i < columnCount; i++) {
                component.setColumnExpandRatio(i, 1);
            }
        } else {
            columnCount = columnElements.size();
            component.setColumns(columnCount);
            int i = 0;
            for (Element columnElement : columnElements) {
                final String flex = columnElement.attributeValue("flex");
                if (!StringUtils.isEmpty(flex)) {
                    component.setColumnExpandRatio(i, Float.parseFloat(flex));
                }
                i++;
            }
        }

        @SuppressWarnings("unchecked")
        final List<Element> rowElements = rowsElement.elements("row");
        final Set<Element> invisibleRows = new HashSet<>();

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
                Boolean value = Boolean.valueOf(visible);

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

        spanMatrix = new boolean[columnCount][rowElements.size()];

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

        loadAlign(component, element);

        assignFrame(component);

        return component;
    }

    protected void loadSubComponents(GridLayout component, Element element, int row) {
        final LayoutLoader loader = new LayoutLoader(context, factory, config);
        loader.setLocale(getLocale());
        loader.setMessagesPack(getMessagesPack());

        int col = 0;

        //noinspection unchecked
        for (Element subElement : (Collection<Element>) element.elements()) {
            final Component subComponent = loader.loadComponent(subElement, component);

            String colspan = subElement.attributeValue("colspan");
            String rowspan = subElement.attributeValue("rowspan");

            while (spanMatrix[col][row]) {
                col++;
            }

            if (StringUtils.isEmpty(colspan) && StringUtils.isEmpty(rowspan)) {
                addSubComponent(component, subComponent, col, row, col, row);
            } else {
                int cspan = 1;
                int rspan = 1;

                if (StringUtils.isNotEmpty(colspan)) {
                    cspan = Integer.parseInt(colspan);
                    if (cspan < 1) {
                        throw new GuiDevelopmentException("GridLayout colspan can not be less than 1",
                                context.getFullFrameId(), "colspan", cspan);
                    }
                    if (cspan == 1) {
                        LogFactory.getLog(getClass()).warn("Do not use colspan=\"1\", it will have no effect");
                    }
                }

                if (StringUtils.isNotEmpty(rowspan)) {
                    rspan = Integer.parseInt(rowspan);
                    if (rspan < 1) {
                        throw new GuiDevelopmentException("GridLayout rowspan can not be less than 1",
                                context.getFullFrameId(), "rowspan", rspan);
                    }
                    if (rspan == 1) {
                        LogFactory.getLog(getClass()).warn("Do not use rowspan=\"1\", it will have no effect");
                    }
                }

                fillSpanMatrix(col, row, cspan, rspan);

                int endColumn = col + cspan - 1;
                int endRow = row + rspan - 1;

                addSubComponent(component, subComponent, col, row, endColumn, endRow);
            }

            col++;
        }
    }

    protected void addSubComponent(GridLayout grid, Component subComponent, int c1, int r1, int c2, int r2) {
        if (subComponent instanceof QuasiComponent) {
            Collection<Component> realComponents = ((QuasiComponent) subComponent).getRealComponents();
            if (realComponents.size() == 1) {
                Component comp = realComponents.iterator().next();
                grid.remove(comp);
                grid.add(comp, c1, r1, c2, r2);
            } else {
                Label label = factory.createComponent(Label.NAME);
                grid.add(label, c1, r1, c2, r2);
            }
        } else {
            grid.add(subComponent, c1, r1, c2, r2);
        }
    }

    protected void fillSpanMatrix(int col, int row, int cspan, int rspan) {
        for (int i = col; i < (col + cspan); i++) {
            for (int j = row; j < (row + rspan); j++) {
                if (spanMatrix[i][j]) {
                    throw new GuiDevelopmentException("Grid layout prohibits component overlapping",
                            context.getFullFrameId());
                }

                spanMatrix[i][j] = true;
            }
        }
    }
}
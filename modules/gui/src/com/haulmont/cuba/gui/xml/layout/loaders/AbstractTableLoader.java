/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 06.04.2009 11:44:49
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.bali.util.ReflectionHelper;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.lang.reflect.Constructor;

public abstract class AbstractTableLoader<T extends Table> extends ComponentLoader {
    protected ComponentsFactory factory;
    protected LayoutLoaderConfig config;

    public AbstractTableLoader(Context context, ComponentsFactory factory, LayoutLoaderConfig config) {
        super(context);
        this.factory = factory;
        this.config = config;
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        final T component = createComponent(factory);

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);
        loadEditable(component, element);
        loadValidators(component, element);

        loadStyleName(component, element);

        loadHeight(component, element);
        loadWidth(component, element);

        loadExpandable(component, element);

        final Element columnsElement = element.element("columns");
        final Element rowsElement = element.element("rows");

        final String rowHeaderMode = rowsElement.attributeValue("headerMode");
        if (!StringUtils.isEmpty(rowHeaderMode)) {
            component.setRowHeaderMode(Table.RowHeaderMode.valueOf(rowHeaderMode));
        }

        final String datasource = rowsElement.attributeValue("datasource");

        if (!StringUtils.isBlank(datasource)) {
            final CollectionDatasource ds = context.getDsContext().get(datasource);
            if (ds == null) {
                throw new IllegalStateException("Cannot find data source by name: " + datasource);
            }
            List<Table.Column> availableColumns = new ArrayList<Table.Column>();

            if (columnsElement != null) {
                //noinspection unchecked
                for (Element columnElement : (Collection<Element>) columnsElement.elements("column")) {
                    String visible = columnElement.attributeValue("visible");
                    if (visible == null) {
                        final Element e = columnElement.element("visible");
                        if (e != null) {
                            visible = e.getText();
                        }
                    }

                    if (StringUtils.isEmpty(visible) || evaluateBoolean(visible)) {
                        availableColumns.add(loadColumn(columnElement, ds));
                    }
                }
            }

            for (Table.Column column : availableColumns) {
                component.addColumn(column);
                loadValidators(component, column);
                loadRequired(component, column);
            }

            component.setDatasource(ds);
        } else {
            throw new UnsupportedOperationException();
        }

        final String multiselect = element.attributeValue("multiselect");
        component.setMultiSelect(BooleanUtils.toBoolean(multiselect));

        final String s = element.attributeValue("pagingMode");
        if (!StringUtils.isEmpty(s)) {
            final Table.PagingMode pagingMode = Table.PagingMode.valueOf(s);
            component.setPagingMode(pagingMode);

            if (pagingMode == Table.PagingMode.PAGE) {
                Element pagingElement = element.element("paging");
                if (pagingElement != null) {
                    loadPaging(component, pagingElement);
                }
            }
        }

        addAssignWindowTask(component);

        return component;
    }

    private void loadPaging(Table component, final Element element) {
        component.setPagingProvider(new Table.PagingProvider() {
            public String firstCaption() {
                return loadResourceString(element.attributeValue("firstCaption"));
            }

            public String prevCaption() {
                return loadResourceString(element.attributeValue("prevCaption"));
            }

            public String nextCaption() {
                return loadResourceString(element.attributeValue("nextCaption"));
            }

            public String lastCaption() {
                return loadResourceString(element.attributeValue("lastCaption"));
            }

            public String pageLengthSelectorCaption() {
                return loadResourceString(element.attributeValue("selectCaption"));
            }

            public boolean showPageLengthSelector() {
                String selectPageLength = element.attributeValue("selectPageLength");
                return selectPageLength != null && "true".equals(selectPageLength);
            }

            public int[] pageLengths() {
                if (showPageLengthSelector()) {
                    String s = element.attributeValue("availablePageLengths");
                    if (!StringUtils.isEmpty(s)) {
                        String[] lengths = s.split(",");

                        int[] result = new int[lengths.length];
                        for (int i = 0; i < lengths.length; i++) {
                            result[i] = Integer.parseInt(lengths[i]);
                        }
                        return result;
                    } else {
                        return new int[] {10, 30, 50, 100};
                    }
                }
                return null;
            }
        });
    }

    private void loadRequired(T component, Table.Column column) {
        Element element = column.getXmlDescriptor();
        final String required = element.attributeValue("required");
        if (!StringUtils.isEmpty(required)) {
            String requiredMsg = element.attributeValue("requiredMessage");
            component.setRequired(column, BooleanUtils.toBoolean(required), loadResourceString(requiredMsg));
        }
    }

    private void loadValidators(T component, Table.Column column) {
        final List<Element> validatorElements = column.getXmlDescriptor().elements("validator");

        for (Element validatorElement : validatorElements) {
            final String className = validatorElement.attributeValue("class");
            final Class<Field.Validator> aClass = ReflectionHelper.getClass(className);

            try {
                final Constructor<Field.Validator> constructor = aClass.getConstructor(Element.class);
                try {
                    final Field.Validator validator = constructor.newInstance(validatorElement);
                    component.addValidator(column, validator);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            } catch (NoSuchMethodException e) {
                try {
                    final Field.Validator validator = aClass.newInstance();
                    component.addValidator(column, validator);
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
    }

    protected abstract T createComponent(ComponentsFactory factory) throws InstantiationException, IllegalAccessException;

    private Table.Column loadColumn(Element element, Datasource ds) {
        final String id = element.attributeValue("id");

        final MetaClass metaClass = ds.getMetaClass();
        final MetaPropertyPath metaPropertyPath = metaClass.getPropertyEx(id);
        if (metaPropertyPath == null)
            throw new IllegalStateException(String.format("Property '%s' not found in entity '%s'", id, metaClass.getName()));

        final Table.Column column = new Table.Column(metaPropertyPath);

        String editable = element.attributeValue("editable");
        if (editable == null) {
            final Element e = element.element("editable");
            if (e != null) {
                editable = e.getText();
            }
        }

        if (!StringUtils.isEmpty(editable)) {
            column.setEditable(evaluateBoolean(editable));
        }

        String collapsed = element.attributeValue("collapsed");
        if (collapsed == null) {
            final Element e = element.element("collapsed");
            if (e != null) {
                collapsed = e.getText();
            }
        }

        if (!StringUtils.isEmpty(collapsed)) {
            column.setCollapsed(evaluateBoolean(collapsed));
        }

        loadCaption(column, element);

        column.setXmlDescriptor(element);
        column.setType(metaPropertyPath.getRangeJavaClass());

        String width = element.attributeValue("width");
        if (!StringUtils.isBlank(width)) {
            try {
                column.setWidth(Integer.parseInt(width));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Property 'width' must contain only numeric value");
            }
        }

        column.setFormatter(loadFormatter(element));

        return column;
    }

    protected Formatter loadFormatter(Element element) {
        final Element formatterElement = element.element("formatter");
        if (formatterElement != null) {
            final String className = formatterElement.attributeValue("class");
            final Class<Formatter> aClass = ReflectionHelper.getClass(className);
            try {
                final Constructor<Formatter> constructor = aClass.getConstructor(Element.class);
                try {
                    return constructor.newInstance(formatterElement);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            } catch (NoSuchMethodException e) {
                try {
                    return aClass.newInstance();
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            }
        } else {
            return null;
        }
    }

    protected void loadValidators(T component, Element element) {
        final List<Element> validatorElements = element.elements("validator");

        for (Element validatorElement : validatorElements) {
            final String className = validatorElement.attributeValue("class");
            final Class<Field.Validator> aClass = ReflectionHelper.getClass(className);

            try {
                final Constructor<Field.Validator> constructor = aClass.getConstructor(Element.class);
                try {
                    final Field.Validator validator = constructor.newInstance(validatorElement);
                    component.addValidator(validator);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            } catch (NoSuchMethodException e) {
                try {
                    final Field.Validator validator = aClass.newInstance();
                    component.addValidator(validator);
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
    }

    protected void loadSortable(T component, Element element) {
        final String sortable = element.attributeValue("sortable");
        if (!StringUtils.isEmpty(sortable) && isBoolean(sortable)) {
            component.setSortable(Boolean.valueOf(sortable));
        }
    }
}

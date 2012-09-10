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
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.ListActionType;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractTableLoader<T extends Table> extends ComponentLoader {

    protected ComponentsFactory factory;
    protected LayoutLoaderConfig config;

    public AbstractTableLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context);
        this.factory = factory;
        this.config = config;
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        final T component = createComponent(factory);

        assignXmlDescriptor(component, element);
        loadId(component, element);

        assignFrame(component);

        loadVisible(component, element);
        loadEditable(component, element);
        loadValidators(component, element);

        loadStyleName(component, element);

        loadHeight(component, element);
        loadWidth(component, element);

        loadSortable(component, element);
        loadAggregatable(component, element);

        loadPresentations(component, element);

        loadActions(component, element);

        final Element columnsElement = element.element("columns");
        final Element rowsElement = element.element("rows");
        if (rowsElement == null)
            throw new IllegalStateException("Table doesn't have 'rows' element");

        final String rowHeaderMode = rowsElement.attributeValue("headerMode");
        if (!StringUtils.isEmpty(rowHeaderMode)) {
            component.setRowHeaderMode(Table.RowHeaderMode.valueOf(rowHeaderMode));
        }

        loadButtonsPanel(component, element);

        loadRowsCount(component, element); // must be before datasource setting

        final String datasource = rowsElement.attributeValue("datasource");
        if (StringUtils.isBlank(datasource))
            throw new IllegalStateException("Table.rows element doesn't have 'datasource' attribute");

        Datasource ds = context.getDsContext().get(datasource);
        if (ds == null)
            throw new IllegalStateException("Cannot find data source by name: " + datasource);
        if (!(ds instanceof CollectionDatasource))
            throw new IllegalStateException("Not a CollectionDatasource: " + datasource);

        CollectionDatasource cds = (CollectionDatasource) ds;
        List<Table.Column> availableColumns;

        if (columnsElement != null) {
            availableColumns = loadColumns(component, columnsElement, cds);
        } else {
            availableColumns = new ArrayList<Table.Column>();
        }

        for (Table.Column column : availableColumns) {
            component.addColumn(column);
            loadValidators(component, column);
            loadRequired(component, column);
        }

        component.setDatasource(cds);

        final String multiselect = element.attributeValue("multiselect");
        component.setMultiSelect(BooleanUtils.toBoolean(multiselect));

        return component;
    }

    protected void loadRowsCount(T table, Element element) {
        Element rowsCountEl = element.element("rowsCount");
        if (rowsCountEl != null) {
            RowsCount rowsCount = factory.createComponent("rowsCount");
            table.setRowsCount(rowsCount);
        }
    }

    protected List<Table.Column> loadColumns(Table component, Element columnsElement, CollectionDatasource ds) {
        final List<Table.Column> columns = new ArrayList<Table.Column>();
        //noinspection unchecked
        for (final Element columnElement : (Collection<Element>) columnsElement.elements("column")) {
            String visible = columnElement.attributeValue("visible");
            if (visible == null) {
                final Element e = columnElement.element("visible");
                if (e != null) {
                    visible = e.getText();
                }
            }

            if (StringUtils.isEmpty(visible) || evaluateBoolean(visible)) {
                columns.add(loadColumn(columnElement, ds));
            }
        }
        return columns;
    }

    private void loadAggregatable(Table component, Element element) {
        String aggregatable = element.attributeValue("aggregatable");
        if (!StringUtils.isEmpty(aggregatable)) {
            component.setAggregatable(BooleanUtils.toBoolean(aggregatable));
            String showTotalAggregation = element.attributeValue("showTotalAggregation");
            if (!StringUtils.isEmpty(showTotalAggregation)) {
                component.setShowTotalAggregation(BooleanUtils.toBoolean(showTotalAggregation));
            }
        }
    }

    private void loadButtonsPanel(Component.HasButtonsPanel component, Element element)
            throws InstantiationException, IllegalAccessException {
        Element panelElement = element.element("buttonsPanel");
        if (panelElement != null) {
            Window window = ComponentsHelper.getWindow((Component.BelongToFrame) component);
            String alwaysVisible = panelElement.attributeValue("alwaysVisible");

            if (!(window instanceof Window.Lookup) || "true".equals(alwaysVisible)) {
                ButtonsPanelLoader loader = (ButtonsPanelLoader) getLoader("buttonsPanel");
                ButtonsPanel panel = (ButtonsPanel) loader.loadComponent(factory, panelElement, null);

                component.setButtonsPanel(panel);
            }
        }
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

        if (!validatorElements.isEmpty()) {
            for (Element validatorElement : validatorElements) {
                final Field.Validator validator = loadValidator(validatorElement);
                if (validator != null) {
                    component.addValidator(column, validator);
                }
            }
        } else if (column.isEditable()) {
            MetaPropertyPath propertyPath = (MetaPropertyPath) column.getId();
            Field.Validator validator = getDefaultValidator(propertyPath.getMetaProperty());
            if (validator != null) {
                component.addValidator(column, validator);
            }
        }
    }

    protected abstract T createComponent(ComponentsFactory factory) throws InstantiationException, IllegalAccessException;

    private Table.Column loadColumn(Element element, Datasource ds) {
        final String id = element.attributeValue("id");

        final MetaClass metaClass = ds.getMetaClass();
        final MetaPropertyPath metaPropertyPath = metaClass.getPropertyPath(id);

        final Table.Column column = new Table.Column(metaPropertyPath != null ? metaPropertyPath : id);

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
        if (column.getCaption() == null) {
            String columnCaption;
            if (column.getId() instanceof MetaPropertyPath) {
                columnCaption = AppBeans.get(MessageTools.class).getPropertyCaption(((MetaPropertyPath) column.getId()).getMetaProperty());
            } else {
                Class<?> declaringClass = ds.getMetaClass().getJavaClass();
                String className = declaringClass.getName();
                int i = className.lastIndexOf('.');
                if (i > -1)
                    className = className.substring(i + 1);
                columnCaption = messages.getMessage(declaringClass, className + "." + id);
            }
            column.setCaption(columnCaption);
        }

        column.setXmlDescriptor(element);
        if (metaPropertyPath != null)
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

        loadAggregation(column, element);
        loadCalculatable(column, element);

        return column;
    }

    private void loadAggregation(Table.Column column, Element columnElement) {
        Element aggregationElement = columnElement.element("aggregation");
        if (aggregationElement != null) {
            final AggregationInfo aggregation = new AggregationInfo();
            aggregation.setPropertyPath(column.getId());
            aggregation.setType(AggregationInfo.Type.valueOf(aggregationElement.attributeValue("type")));
            Formatter formatter = loadFormatter(aggregationElement);
            aggregation.setFormatter(formatter == null ? column.getFormatter() : formatter);
            column.setAggregation(aggregation);
        }
    }

    private void loadCalculatable(Table.Column column, Element columnElement) {
        String calc = columnElement.attributeValue("calculatable");
        if (!StringUtils.isEmpty(calc) && isBoolean(calc)) {
            column.setCalculatable(Boolean.valueOf(calc));
        }
    }

    protected Formatter loadFormatter(Element element) {
        final Element formatterElement = element.element("formatter");
        if (formatterElement != null) {
            final String formatterType = formatterElement.attributeValue("type");
            final String className;
            if (formatterType != null) {
                Table.Column.FormatterType ftype = Table.Column.FormatterType.valueOf(formatterType);
                className = ftype.getFormatterClass().getName();
            }
            else {
                className = formatterElement.attributeValue("class");
            }

            Class<Formatter> aClass = scripting.loadClass(className);
            if (aClass == null)
                throw new IllegalStateException("Class " + className + " is not found");
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
            final Field.Validator validator = loadValidator(validatorElement);
            if (validator != null) {
                component.addValidator(validator);
            }
        }
    }

    protected void loadSortable(T component, Element element) {
        final String sortable = element.attributeValue("sortable");
        if (!StringUtils.isEmpty(sortable) && isBoolean(sortable)) {
            component.setSortable(Boolean.valueOf(sortable));
        }
    }

    protected com.haulmont.cuba.gui.xml.layout.ComponentLoader getLoader(String name) throws IllegalAccessException, InstantiationException {
        Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader> loaderClass = config.getLoader(name);
        if (loaderClass == null) {
            throw new IllegalStateException(String.format("Unknown component '%s'", name));
        }

        com.haulmont.cuba.gui.xml.layout.ComponentLoader loader;
        try {
            final Constructor<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader> constructor =
                    loaderClass.getConstructor(Context.class, LayoutLoaderConfig.class, ComponentsFactory.class);
            loader = constructor.newInstance(context, config, factory);

            loader.setLocale(locale);
            loader.setMessagesPack(messagesPack);
        } catch (Throwable e) {
            loader = loaderClass.newInstance();
            loader.setLocale(locale);
            loader.setMessagesPack(messagesPack);
        }

        return loader;
    }

    @Override
    protected Action loadDeclarativeAction(Component.ActionsHolder actionsHolder, Element element) {
        String id = element.attributeValue("id");
        if (id == null)
            throw new IllegalStateException("No action id provided");

        if (StringUtils.isBlank(element.attributeValue("invoke"))) {
            // Try to create a standard list action
            for (ListActionType type : ListActionType.values()) {
                if (type.getId().equals(id)) {
                    return type.createAction((ListComponent) actionsHolder);
                }
            }
        }

        return super.loadDeclarativeAction(actionsHolder, element);
    }
}

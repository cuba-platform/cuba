/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.aggregation.AggregationStrategy;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author abramov
 * @version $Id$
 */
public abstract class AbstractTableLoader extends ActionsHolderLoader {

    private Log log = LogFactory.getLog(getClass());

    protected ComponentsFactory factory;
    protected LayoutLoaderConfig config;

    protected MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
    protected DynamicAttributesGuiTools dynamicAttributesGuiTools = AppBeans.get(DynamicAttributesGuiTools.NAME);
    protected DynamicAttributes dynamicAttributes = AppBeans.get(DynamicAttributes.NAME);

    public AbstractTableLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context);
        this.factory = factory;
        this.config = config;
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        Table component = (Table) factory.createComponent(element.getName());

        initComponent(component, element, parent);

        return component;
    }

    protected void initComponent(Table component, Element element, Component parent) {
        assignXmlDescriptor(component, element);
        loadId(component, element);

        assignFrame(component);

        loadEnable(component, element);
        loadVisible(component, element);
        loadEditable(component, element);
        loadValidators(component, element);

        loadAlign(component, element);
        loadStyleName(component, element);

        loadHeight(component, element);
        loadWidth(component, element);

        loadSortable(component, element);
        loadReorderingAllowed(component, element);
        loadColumnControlVisible(component, element);
        loadAggregatable(component, element);
        loadAggregationStyle(component, element);

        loadPresentations(component, element);

        loadActions(component, element);
        loadContextMenuEnabled(component, element);
        loadMultiLineCells(component, element);

        final Element columnsElement = element.element("columns");
        final Element rowsElement = element.element("rows");

        if (rowsElement == null) {
            throw new GuiDevelopmentException("Table doesn't have 'rows' element", context.getCurrentIFrameId(),
                    "Table ID", element.attributeValue("id"));
        }

        String rowHeaderMode = rowsElement.attributeValue("rowHeaderMode");
        if (StringUtils.isBlank(rowHeaderMode)) {
            rowHeaderMode = rowsElement.attributeValue("headerMode");
            if (StringUtils.isNotBlank(rowHeaderMode)) {
                log.warn("Attribute headerMode is deprecated. Use rowHeaderMode.");
            }
        }

        if (!StringUtils.isEmpty(rowHeaderMode)) {
            component.setRowHeaderMode(Table.RowHeaderMode.valueOf(rowHeaderMode));
        }

        loadButtonsPanel(component, element);

        loadRowsCount(component, element); // must be before datasource setting

        final String datasource = rowsElement.attributeValue("datasource");
        if (StringUtils.isBlank(datasource)) {
            throw new GuiDevelopmentException("Table 'rows' element doesn't have 'datasource' attribute",
                    context.getCurrentIFrameId(), "Table ID", element.attributeValue("id"));
        }
        context.getFullFrameId();

        Datasource ds = context.getDsContext().get(datasource);
        if (ds == null) {
            throw new GuiDevelopmentException("Can't find datasource by name: " + datasource, context.getCurrentIFrameId());
        }

        if (!(ds instanceof CollectionDatasource)) {
            throw new GuiDevelopmentException("Not a CollectionDatasource: " + datasource, context.getCurrentIFrameId());
        }

        CollectionDatasource cds = (CollectionDatasource) ds;
        List<Table.Column> availableColumns;

        if (columnsElement != null) {
            availableColumns = loadColumns(component, columnsElement, cds);
        } else {
            availableColumns = new ArrayList<>();
        }

        for (Table.Column column : availableColumns) {
            component.addColumn(column);
            loadValidators(component, column);
            loadRequired(component, column);
        }

        addDynamicAttributes(component, ds, availableColumns);

        component.setDatasource(cds);

        final String multiselect = element.attributeValue("multiselect");
        component.setMultiSelect(BooleanUtils.toBoolean(multiselect));
    }

    protected void addDynamicAttributes(Table component, Datasource ds, List<Table.Column> availableColumns) {
        if (metadataTools.isPersistent(ds.getMetaClass())) {
            Set<CategoryAttribute> attributesToShow =
                    dynamicAttributesGuiTools.getAttributesToShowOnTheScreen(ds.getMetaClass(), context.getFullFrameId(), component.getId());
            if (CollectionUtils.isNotEmpty(attributesToShow)) {
                ds.setLoadDynamicAttributes(true);
                for (CategoryAttribute attribute : attributesToShow) {
                    final MetaPropertyPath metaPropertyPath = DynamicAttributesUtils.getMetaPropertyPath(ds.getMetaClass(), attribute);

                    Object columnWithSameId = CollectionUtils.find(availableColumns, new org.apache.commons.collections.Predicate() {
                        @Override
                        public boolean evaluate(Object o) {
                            return ((Table.Column) o).getId().equals(metaPropertyPath);
                        }
                    });

                    if (columnWithSameId != null) {
                        continue;
                    }

                    final Table.Column column = new Table.Column(metaPropertyPath);
                    column.setCaption(attribute.getName());
                    column.setEditable(true);
                    component.addColumn(column);
                }
            }

            dynamicAttributesGuiTools.listenDynamicAttributesChanges(ds);
        }
    }

    protected void loadMultiLineCells(Table table, Element element) {
        final String allowMultiStringCells = element.attributeValue("allowMultiStringCells");
        if (StringUtils.isNotBlank(allowMultiStringCells)) {
            table.setAllowMultiStringCells(BooleanUtils.toBoolean(allowMultiStringCells));
        }

        final String multiLineCells = element.attributeValue("multiLineCells");
        if (StringUtils.isNotBlank(multiLineCells)) {
            table.setMultiLineCells(BooleanUtils.toBoolean(multiLineCells));
        }
    }

    protected void loadContextMenuEnabled(Table table, Element element) {
        final String allowPopupMenu = element.attributeValue("allowPopupMenu");
        if (StringUtils.isNotBlank(allowPopupMenu)) {
            table.setAllowPopupMenu(BooleanUtils.toBoolean(allowPopupMenu));
        }

        final String contextMenuEnabled = element.attributeValue("contextMenuEnabled");
        if (StringUtils.isNotBlank(contextMenuEnabled)) {
            table.setContextMenuEnabled(BooleanUtils.toBoolean(contextMenuEnabled));
        }
    }

    protected void loadRowsCount(Table table, Element element) {
        Element rowsCountElement = element.element("rowsCount");
        if (rowsCountElement != null) {
            RowsCount rowsCount = factory.createComponent(RowsCount.class);
            rowsCount.setOwner(table);
            table.setRowsCount(rowsCount);
        }
    }

    protected List<Table.Column> loadColumns(Table component, Element columnsElement, CollectionDatasource ds) {
        final List<Table.Column> columns = new ArrayList<>();
        //noinspection unchecked
        for (final Element columnElement : (Collection<Element>) columnsElement.elements("column")) {
            String visible = columnElement.attributeValue("visible");
            if (StringUtils.isEmpty(visible) || Boolean.valueOf(visible)) {
                columns.add(loadColumn(columnElement, ds));
            }
        }
        return columns;
    }

    protected void loadAggregatable(Table component, Element element) {
        String aggregatable = element.attributeValue("aggregatable");
        if (!StringUtils.isEmpty(aggregatable)) {
            component.setAggregatable(BooleanUtils.toBoolean(aggregatable));
            String showTotalAggregation = element.attributeValue("showTotalAggregation");
            if (!StringUtils.isEmpty(showTotalAggregation)) {
                component.setShowTotalAggregation(BooleanUtils.toBoolean(showTotalAggregation));
            }
        }
    }

    protected void loadAggregationStyle(Table component, Element element) {
        String aggregationStyle = element.attributeValue("aggregationStyle");
        if (!StringUtils.isEmpty(aggregationStyle)) {
            component.setAggregationStyle(Table.AggregationStyle.valueOf(aggregationStyle));
        }
    }

    protected void loadButtonsPanel(Component.HasButtonsPanel component, Element element) {
        Element panelElement = element.element("buttonsPanel");
        if (panelElement != null) {
            Window window = ComponentsHelper.getWindowImplementation((Component.BelongToFrame) component);

            ButtonsPanelLoader loader = (ButtonsPanelLoader) getLoader(ButtonsPanel.NAME);
            ButtonsPanel panel = (ButtonsPanel) loader.loadComponent(factory, panelElement, null);

            component.setButtonsPanel(panel);

            String alwaysVisible = panelElement.attributeValue("alwaysVisible");
            panel.setVisible(!(window instanceof Window.Lookup) || "true".equals(alwaysVisible));
        }
    }

    protected void loadRequired(Table component, Table.Column column) {
        Element element = column.getXmlDescriptor();
        final String required = element.attributeValue("required");
        if (!StringUtils.isEmpty(required)) {
            String requiredMsg = element.attributeValue("requiredMessage");
            component.setRequired(column, BooleanUtils.toBoolean(required), loadResourceString(requiredMsg));
        }
    }

    protected void loadValidators(Table component, Table.Column column) {
        @SuppressWarnings("unchecked") final
        List<Element> validatorElements = column.getXmlDescriptor().elements("validator");

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

    protected Table.Column loadColumn(Element element, Datasource ds) {
        final String id = element.attributeValue("id");

        final MetaPropertyPath metaPropertyPath = AppBeans.get(MetadataTools.NAME, MetadataTools.class)
                .resolveMetaPropertyPath(ds.getMetaClass(), id);

        final Table.Column column = new Table.Column(metaPropertyPath != null ? metaPropertyPath : id);

        String editable = element.attributeValue("editable");
        if (editable == null) {
            // todo artamonov remove in 5.3
            final Element e = element.element("editable");
            if (e != null) {
                editable = e.getText();
            }
        }

        if (!StringUtils.isEmpty(editable)) {
            column.setEditable(Boolean.valueOf(editable));
        }

        String collapsed = element.attributeValue("collapsed");
        if (collapsed == null) {
            // todo artamonov remove in 5.3
            final Element e = element.element("collapsed");
            if (e != null) {
                collapsed = e.getText();
            }
        }

        if (!StringUtils.isEmpty(collapsed)) {
            column.setCollapsed(Boolean.valueOf(collapsed));
        }

        String groupAllowed = element.attributeValue("groupAllowed");
        if (StringUtils.isNotEmpty(groupAllowed)) {
            column.setGroupAllowed(Boolean.valueOf(groupAllowed));
        }

        loadCaption(column, element);
        loadDescription(column, element);

        if (column.getCaption() == null) {
            String columnCaption;
            if (column.getId() instanceof MetaPropertyPath) {
                MetaPropertyPath mpp = (MetaPropertyPath) column.getId();
                MetaProperty metaProperty = mpp.getMetaProperty();
                String propertyName = metaProperty.getName();

                if (DynamicAttributesUtils.isDynamicAttribute(metaProperty)) {
                    CategoryAttribute categoryAttribute = DynamicAttributesUtils.getCategoryAttribute(metaProperty);
                    columnCaption = categoryAttribute != null ? categoryAttribute.getName() : propertyName;
                } else {
                    MetaClass propertyMetaClass = metadataTools.getPropertyEnclosingMetaClass(mpp);
                    columnCaption = messageTools.getPropertyCaption(propertyMetaClass, propertyName);
                }
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

        String width = loadThemeString(element.attributeValue("width"));
        if (!StringUtils.isBlank(width)) {
            if (StringUtils.endsWith(width, "px")) {
                width = StringUtils.substring(width, 0, width.length() - 2);
            }
            try {
                column.setWidth(Integer.parseInt(width));
            } catch (NumberFormatException e) {
                throw new GuiDevelopmentException("Property 'width' must contain only numeric value",
                        context.getCurrentIFrameId(), "width", element.attributeValue("width"));
            }
        }
        String align = element.attributeValue("align");
        if (StringUtils.isNotBlank(align)) {
            column.setAlignment(Table.ColumnAlignment.valueOf(align));
        }

        column.setFormatter(loadFormatter(element));

        loadAggregation(column, element);
        loadCalculatable(column, element);
        loadMaxTextLength(column, element);

        return column;
    }

    protected void loadAggregation(Table.Column column, Element columnElement) {
        Element aggregationElement = columnElement.element("aggregation");
        if (aggregationElement != null) {
            final AggregationInfo aggregation = new AggregationInfo();
            aggregation.setPropertyPath((MetaPropertyPath) column.getId());
            String aggregationType = aggregationElement.attributeValue("type");
            if (StringUtils.isNotEmpty(aggregationType)) {
                aggregation.setType(AggregationInfo.Type.valueOf(aggregationType));
            }

            Formatter formatter = loadFormatter(aggregationElement);
            aggregation.setFormatter(formatter == null ? column.getFormatter() : formatter);
            column.setAggregation(aggregation);

            String strategyClass = aggregationElement.attributeValue("strategyClass");
            if (StringUtils.isNotEmpty(strategyClass)) {
                Class<Object> aggregationClass = scripting.loadClass(strategyClass);
                if (aggregationClass == null) {
                    throw new GuiDevelopmentException(String.format("Class %s is not found", strategyClass), context.getFullFrameId());
                }

                try {
                    AggregationStrategy customStrategy = (AggregationStrategy) aggregationClass.newInstance();
                    aggregation.setStrategy(customStrategy);
                } catch (Exception e) {
                    throw new RuntimeException("Unable to instantiate strategy for aggregation", e);
                }
            }

            if (aggregationType == null && strategyClass == null) {
                throw new GuiDevelopmentException("Incorrect aggregation - type or strategyClass is required", context.getFullFrameId());
            }
        }
    }

    protected void loadCalculatable(Table.Column column, Element columnElement) {
        String calc = columnElement.attributeValue("calculatable");
        if (!StringUtils.isEmpty(calc)) {
            column.setCalculatable(Boolean.valueOf(calc));
        }
    }

    protected void loadMaxTextLength(Table.Column column, Element columnElement) {
        String maxTextLength = columnElement.attributeValue("maxTextLength");
        if (!StringUtils.isBlank(maxTextLength)) {
            column.setMaxTextLength(Integer.parseInt(maxTextLength));
        }
    }

    protected Formatter loadFormatter(Element element) {
        final Element formatterElement = element.element("formatter");
        if (formatterElement != null) {
            final String className = formatterElement.attributeValue("class");

            if (StringUtils.isEmpty(className)) {
                throw new GuiDevelopmentException("Formatter's attribute 'class' is not specified", context.getCurrentIFrameId());
            }

            Class<Formatter> aClass = scripting.loadClass(className);
            if (aClass == null) {
                throw new GuiDevelopmentException(String.format("Class %s is not found", className), context.getFullFrameId());
            }

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

    protected void loadValidators(Table component, Element element) {
        @SuppressWarnings("unchecked")
        final List<Element> validatorElements = element.elements("validator");

        for (Element validatorElement : validatorElements) {
            final Field.Validator validator = loadValidator(validatorElement);
            if (validator != null) {
                component.addValidator(validator);
            }
        }
    }

    protected void loadSortable(Table component, Element element) {
        final String sortable = element.attributeValue("sortable");
        if (!StringUtils.isEmpty(sortable)) {
            component.setSortable(Boolean.valueOf(sortable));
        }
    }

    protected void loadReorderingAllowed(Table component, Element element) {
        final String reorderingAllowed = element.attributeValue("reorderingAllowed");
        if (!StringUtils.isEmpty(reorderingAllowed)) {
            component.setColumnReorderingAllowed(Boolean.valueOf(reorderingAllowed));
        }
    }

    protected void loadColumnControlVisible(Table component, Element element) {
        final String columnControlVisible = element.attributeValue("columnControlVisible");
        if (!StringUtils.isEmpty(columnControlVisible)) {
            component.setColumnControlVisible(Boolean.valueOf(columnControlVisible));
        }
    }

    protected com.haulmont.cuba.gui.xml.layout.ComponentLoader getLoader(String name) {
        Class<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader> loaderClass = config.getLoader(name);
        if (loaderClass == null) {
            throw new GuiDevelopmentException("Unknown component: " + name, context.getFullFrameId());
        }

        com.haulmont.cuba.gui.xml.layout.ComponentLoader loader;
        try {
            final Constructor<? extends com.haulmont.cuba.gui.xml.layout.ComponentLoader> constructor =
                    loaderClass.getConstructor(Context.class, LayoutLoaderConfig.class, ComponentsFactory.class);
            loader = constructor.newInstance(context, config, factory);

            loader.setLocale(locale);
            loader.setMessagesPack(messagesPack);
        } catch (NoSuchMethodException e) {
            try {
                loader = loaderClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e1) {
                throw new GuiDevelopmentException("Loader instantiation error: " + e1, context.getFullFrameId());
            }
            loader.setLocale(locale);
            loader.setMessagesPack(messagesPack);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new GuiDevelopmentException("Loader instantiation error: " + e, context.getFullFrameId());
        }

        return loader;
    }
}
/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.google.common.base.Strings;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.FilterImplementation;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author krivopustov
 * @version $Id$
 */
public class FilterLoader extends ComponentLoader {

    public static final String DEFAULT_FILTER_ID = "filterWithoutId";

    public FilterLoader(Context context) {
        super(context);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        final Filter filter = (Filter) factory.createComponent(element.getName());
        initFilter(filter, element);
        return filter;
    }

    @Override
    protected void loadId(Component component, Element element) {
        super.loadId(component, element);
        if (Strings.isNullOrEmpty(component.getId())) {
            component.setId(DEFAULT_FILTER_ID);
        }
    }

    protected void initFilter(final Filter filter, final Element element) {
        assignXmlDescriptor(filter, element);
        loadId(filter, element);
        loadVisible(filter, element);
        loadEnable(filter, element);
        loadStyleName(filter, element);
        loadMargin(filter, element);
        loadCaption(filter, element);
        loadWidth(filter, element, "100%");
        loadCollapsible(filter, element, true);

        String useMaxResults = element.attributeValue("useMaxResults");
        filter.setUseMaxResults(useMaxResults == null || Boolean.valueOf(useMaxResults));

        String textMaxResults = element.attributeValue("textMaxResults");
        filter.setTextMaxResults(Boolean.valueOf(textMaxResults));

        final String manualApplyRequired = element.attributeValue("manualApplyRequired");
        filter.setManualApplyRequired(BooleanUtils.toBooleanObject(manualApplyRequired));

        String editable = element.attributeValue("editable");
        filter.setEditable(editable == null || Boolean.valueOf(editable));

        String columnsQty = element.attributeValue("columnsCount");
        if (!Strings.isNullOrEmpty(columnsQty))
            filter.setColumnsCount(Integer.valueOf(columnsQty));

        String folderActionsEnabled = element.attributeValue("folderActionsEnabled");
        if (folderActionsEnabled != null) {
            filter.setFolderActionsEnabled(Boolean.valueOf(folderActionsEnabled));
        }

        String datasource = element.attributeValue("datasource");
        if (!StringUtils.isBlank(datasource)) {
            CollectionDatasource ds = (CollectionDatasource) context.getDsContext().get(datasource);
            if (ds == null)
                throw new GuiDevelopmentException("Can't find datasource by name: " + datasource, context.getCurrentFrameId());
            filter.setDatasource(ds);
        }

        assignFrame(filter);

        final Frame frame = context.getFrame();
        final String applyTo = element.attributeValue("applyTo");
        if (!StringUtils.isEmpty(applyTo)) {
            context.addPostInitTask(new PostInitTask() {
                @Override
                public void execute(Context context, Frame window) {
                    Component c = frame.getComponent(applyTo);
                    if (c == null) {
                        throw new GuiDevelopmentException("Can't apply filter to component with ID: " + applyTo, context.getFullFrameId());
                    }
                    filter.setApplyTo(c);
                }
            });
        }

        context.addPostInitTask(
                new PostInitTask() {
                    @Override
                    public void execute(Context context, Frame window) {
                        ((FilterImplementation)filter).loadFiltersAndApplyDefault();
                    }
                }
        );
    }
}
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
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author krivopustov
 * @version $Id$
 */
public class FilterLoader extends AbstractComponentLoader<Filter> {

    public static final String DEFAULT_FILTER_ID = "filterWithoutId";

    @Override
    protected void loadId(Component component, Element element) {
        super.loadId(component, element);

        if (Strings.isNullOrEmpty(component.getId())) {
            component.setId(DEFAULT_FILTER_ID);
        }
    }

    @Override
    public void createComponent() {
        resultComponent = (Filter) factory.createComponent(Filter.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadVisible(resultComponent, element);
        loadEnable(resultComponent, element);
        loadStyleName(resultComponent, element);
        loadMargin(resultComponent, element);
        loadCaption(resultComponent, element);
        loadWidth(resultComponent, element, "100%");
        loadCollapsible(resultComponent, element, true);

        String useMaxResults = element.attributeValue("useMaxResults");
        resultComponent.setUseMaxResults(useMaxResults == null || Boolean.valueOf(useMaxResults));

        String textMaxResults = element.attributeValue("textMaxResults");
        resultComponent.setTextMaxResults(Boolean.valueOf(textMaxResults));

        final String manualApplyRequired = element.attributeValue("manualApplyRequired");
        resultComponent.setManualApplyRequired(BooleanUtils.toBooleanObject(manualApplyRequired));

        String editable = element.attributeValue("editable");
        resultComponent.setEditable(editable == null || Boolean.valueOf(editable));

        String columnsQty = element.attributeValue("columnsCount");
        if (!Strings.isNullOrEmpty(columnsQty)) {
            resultComponent.setColumnsCount(Integer.valueOf(columnsQty));
        }

        String folderActionsEnabled = element.attributeValue("folderActionsEnabled");
        if (folderActionsEnabled != null) {
            resultComponent.setFolderActionsEnabled(Boolean.valueOf(folderActionsEnabled));
        }

        String datasource = element.attributeValue("datasource");
        if (!StringUtils.isBlank(datasource)) {
            CollectionDatasource ds = (CollectionDatasource) context.getDsContext().get(datasource);
            if (ds == null) {
                throw new GuiDevelopmentException("Can't find datasource by name: " + datasource, context.getCurrentFrameId());
            }
            resultComponent.setDatasource(ds);
        }

        Frame frame = context.getFrame();
        String applyTo = element.attributeValue("applyTo");
        if (!StringUtils.isEmpty(applyTo)) {
            context.addPostInitTask((context1, window) -> {
                Component c = frame.getComponent(applyTo);
                if (c == null) {
                    throw new GuiDevelopmentException("Can't apply component to component with ID: " + applyTo, context1.getFullFrameId());
                }
                resultComponent.setApplyTo(c);
            });
        }

        context.addPostInitTask((context1, window) -> ((FilterImplementation) resultComponent).loadFiltersAndApplyDefault());
    }
}
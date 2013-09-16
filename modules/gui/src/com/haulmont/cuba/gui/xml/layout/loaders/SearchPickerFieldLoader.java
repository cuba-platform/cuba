/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.SearchPickerField;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author artamonov
 * @version $Id$
 */
public class SearchPickerFieldLoader extends SearchFieldLoader {

    public SearchPickerFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        SearchPickerField component = (SearchPickerField) super.loadComponent(factory, element, parent);

        final String metaClass = element.attributeValue("metaClass");
        if (!StringUtils.isEmpty(metaClass)) {
            component.setMetaClass(AppBeans.get(Metadata.class).getSession().getClass(metaClass));
        }

        loadActions(component, element);
        if (component.getActions().isEmpty()) {
            component.addLookupAction();
            component.addOpenAction();
        }

        String minSearchStringLength = element.attributeValue("minSearchStringLength");
        if (StringUtils.isNotEmpty(minSearchStringLength)) {
            component.setMinSearchStringLength(Integer.parseInt(minSearchStringLength));
        }

        return component;
    }

    @Override
    protected Action loadDeclarativeAction(Component.ActionsHolder actionsHolder, Element element) {
        String id = element.attributeValue("id");
        if (id == null)
            throw new GuiDevelopmentException("No action ID provided", context.getFullFrameId());

        if (StringUtils.isBlank(element.attributeValue("invoke"))) {
            // Try to create a standard picker action
            for (PickerField.ActionType type : PickerField.ActionType.values()) {
                if (type.getId().equals(id)) {
                    return type.createAction((PickerField) actionsHolder);
                }
            }
        }

        return super.loadDeclarativeAction(actionsHolder, element);
    }
}

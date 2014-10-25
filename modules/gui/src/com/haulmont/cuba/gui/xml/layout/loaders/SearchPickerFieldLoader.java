/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.*;
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
    protected void initComponent(Field field, Element element, Component parent) {
        super.initComponent(field, element, parent);

        SearchPickerField component = (SearchPickerField) field;

        String metaClass = element.attributeValue("metaClass");
        if (!StringUtils.isEmpty(metaClass)) {
            Metadata metadata = AppBeans.get(Metadata.NAME);
            component.setMetaClass(metadata.getSession().getClass(metaClass));
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

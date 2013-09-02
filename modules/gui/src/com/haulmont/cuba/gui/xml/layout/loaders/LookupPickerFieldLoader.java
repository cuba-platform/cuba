/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.LookupPickerField;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class LookupPickerFieldLoader extends LookupFieldLoader {

    public LookupPickerFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        LookupPickerField component = (LookupPickerField) super.loadComponent(factory, element, parent);

        final String metaClass = element.attributeValue("metaClass");
        if (!StringUtils.isEmpty(metaClass)) {
            component.setMetaClass(MetadataProvider.getSession().getClass(metaClass));
        }

        loadActions(component, element);
        if (component.getActions().isEmpty()) {
            component.addLookupAction();
            component.addOpenAction();
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
